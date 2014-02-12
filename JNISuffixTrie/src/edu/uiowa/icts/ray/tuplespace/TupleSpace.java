
package edu.uiowa.icts.ray.tuplespace;

import edu.uiowa.icts.ray.cachemap.CacheMap;
import edu.uiowa.icts.ray.cachemap.SuffixTrieAccessionNode;
import edu.uiowa.icts.ray.database.DBConnect;
import edu.uiowa.icts.ray.jnisuffixtrie.SuffixTrieSearch_Lite;
import edu.uiowa.icts.ray.jnisuffixtrie.SuffixTrie_Lite;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 * This class simulated a tuple space engine. It has a generic thread whose
 * sole purpose in life is to ping the database for new entries for it to
 * process. When it finds one, it spawns a new SuffixTrieSearch_Lite thread
 * for the given sequence. After the search is done, it populates the solution
 * database and lets the calling application know the search is done.
 * 
 * Singleton design pattern
 * @author Ray Hylock
 */
public class TupleSpace {
    // logger variables
    private static Logger logger = Logger.getLogger(TupleSpace.class);
    private static SimpleLayout layout = new SimpleLayout();
    private static Appender appender = new ConsoleAppender(layout);
    private static Level logLevel = Level.INFO;    // D, I, W, E, F
    
    // connection variables
    private static Properties results;
    private static Properties suffixTrie;
    private static String updateGetSQL;
    private static String updateDoneSQL;
    private static String selectSQL;
    
    // thread variables
    private static int MAX_SIMUL_SEARCH_THREADS;
    private static int MAX_SIMUL_THREADS_PER_SEARCH;
    private static int SLEEP;
    private static int SEARCH_CACHEMAP_CAPACITY;
    private static int SEARCH_CACHEMAP_MAX_MB_MEMORY;
    private static Connection conn;
    private static List<Connection> connections = new ArrayList<Connection>();
    private static List<Thread> threads = new ArrayList<Thread>();
    
    // class pointers
    private static SuffixTrie_Lite st = null;
    private static CacheMap<Integer, SuffixTrieAccessionNode> cm = null;
    private static DBConnect db = null;
    
    /**
     * Start the db listener thread; runs forever
     * @throws SQLException 
     */
    private TupleSpace() {
        // create the threads for the batch processes
        Connection c = TupleSpace.newConnection(results);
        tupleSpace ts = new tupleSpace(conn, SLEEP);
        Thread t = new Thread(ts);
        t.start();
    }
    
    /**
     * 
     * @param st                        the SuffixTrie_Lite object
     * @param results                   the results database connection details
     * @param suffixTrie                the suffixTrie database connection details
     * @param maxSearchThreads          the maximum simultaneous search threads
     * @param maxThreadsPerSearch       the maximum simultaneous threads per search
     * @param sleep                     the main thread sleep time between pings
     * @param cacheSize                 the size of the cachemap
     * @param cacheMemory               the maximum size of the application before we begin removing elements
     * @param db                        the static suffixTrie database object for node results
     * @return 
     */
    public static TupleSpace getInstance(SuffixTrie_Lite st, Properties results, Properties suffixTrie,
            int maxSearchThreads, int maxThreadsPerSearch, int sleep, int cacheSize, int cacheMemory, DBConnect db) {
        // setup logger
        TupleSpace.logger.setLevel(logLevel);
        TupleSpace.logger.addAppender(appender);

        // class pointers
        TupleSpace.st = st;
        TupleSpace.db = db;

        // connection information
        TupleSpace.results = results;
        TupleSpace.suffixTrie = suffixTrie;
        TupleSpace.MAX_SIMUL_SEARCH_THREADS = maxSearchThreads;
        TupleSpace.MAX_SIMUL_THREADS_PER_SEARCH = maxThreadsPerSearch;
        TupleSpace.SLEEP = sleep;
        TupleSpace.SEARCH_CACHEMAP_CAPACITY = cacheSize;
        TupleSpace.SEARCH_CACHEMAP_MAX_MB_MEMORY = cacheMemory;
        
        TupleSpace.updateGetSQL = "UPDATE " + results.getProperty("schema") + ".search_status SET status = 1 WHERE search_seq = ?" ;
        TupleSpace.updateDoneSQL = "UPDATE " + results.getProperty("schema") + ".search_status SET status = 2 WHERE search_seq = ?" ;
        TupleSpace.selectSQL = "SELECT search_seq FROM " + results.getProperty("schema") + ".search_status WHERE status = 0 LIMIT 1";
        
        TupleSpace.conn = newConnection(results);
        
        // create a global cachemap with the false option becuase cm is not writing to db
        TupleSpace.cm = new CacheMap<Integer, SuffixTrieAccessionNode>(SEARCH_CACHEMAP_CAPACITY, SEARCH_CACHEMAP_MAX_MB_MEMORY, false);
        
        return TupleSpaceHolder.INSTANCE;
    }
    
    private static class TupleSpaceHolder {
        private static final TupleSpace INSTANCE = new TupleSpace();
    }
    
    /**
     * Method newConnection - Connects to the database and then returns the database connection
     * @return  the database connection
     */
    private static Connection newConnection(Properties props) {
        Connection c = null;
        try {
            // load and register the JDBC driver
            Class.forName(props.getProperty("driver")).newInstance();

            // connect to the database
            c = DriverManager.getConnection(props.getProperty("connection"), 
                    props.getProperty("username"), props.getProperty("password"));

            // turn off autocommit
            c.setAutoCommit(false);

        } catch (Exception e) {
            TupleSpace.logger.error("Connection attempt failed");
        }
        return c;
    }

    public boolean spawnSearchMechanism(String seq) throws SQLException{
        boolean spawned = false;
        if (connections.size() < TupleSpace.MAX_SIMUL_SEARCH_THREADS) {
            // create the threads for the batch processes
            Connection c = TupleSpace.newConnection(results);
            searchThread search = new searchThread(seq, c);
            Thread t = new Thread(search);
            threads.add(t);
            connections.add(c);
            t.start();
            spawned = true;
        }
        return spawned;
    }
    
    /**
     * Begin threads
     */
    private class tupleSpace implements Runnable{
        private Connection conn;
        private int sleep;
        private tupleSpace(Connection conn, int sleep){
            this.conn = conn;
            TupleSpace.logger.info("TupleSpace is now waiting...");
        }
        
        // simply checks the database for a search seq with 0, grabs it, and starts a search thread
        public void run() {
            while(true){
                try {
                    // initialize the statement variable
                    Statement stmt = conn.createStatement();

                    ResultSet rs = stmt.executeQuery(selectSQL);

                    if(rs.next()){
                        String seq = rs.getString(1); 
                        
                        // spawn threads only if there are enough connections
                        while (!spawnSearchMechanism(seq)) {
                            try {
                                Thread.sleep(1); // wait 1ms and then continue
                            } catch (InterruptedException ex) {
                                java.util.logging.Logger.getLogger(TupleSpace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
                        }
                                                
                        // create the statement; no other process can grab this using the current configuration
                        CallableStatement cstmt = conn.prepareCall(updateGetSQL);
                        cstmt.setString(1, seq);
                        cstmt.executeUpdate();
                        conn.commit();
                    }
                } catch (SQLException ex) {
                    java.util.logging.Logger.getLogger(TupleSpace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
                
                // the true while loop just continues to search the db every x milliseconds spawning search threads
                try {
                    Thread.sleep(sleep);
                } catch (InterruptedException ex) {
                    java.util.logging.Logger.getLogger(TupleSpace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                }
            }
        }
        
    }
    
    /**
     * Class searchThread - This class implements the threads for each connection.
     */
    private class searchThread implements Runnable {
        String seq;
        Connection connection;
        Statement stmt;
        /**
         * 
         * @param seq
         * @param connection
         * @throws SQLException 
         */
        private searchThread(String seq, Connection connection) throws SQLException {
            this.seq = seq;
            this.connection = connection;
            this.stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            this.connection.setAutoCommit(false);
            this.stmt.setFetchSize(10);
        }
        public void run() {
            try {
                logger.info("beginning search on: " + seq);
                SuffixTrieSearch_Lite sts = new SuffixTrieSearch_Lite(st, results, suffixTrie, MAX_SIMUL_THREADS_PER_SEARCH, cm, db);
                sts.search(seq);
                
                // create the statement; no other process can grab this using the current configuration
                CallableStatement cstmt = conn.prepareCall(updateDoneSQL);
                cstmt.setString(1, seq);
                cstmt.executeUpdate();
                conn.commit();
                
                // clean up the threads and connections
                threads.remove(Thread.currentThread());
                connections.remove(connection);
                try {
                    connection.close();
                } catch (SQLException ex) {
                    TupleSpace.logger.error("Could not close connection in thread.");
                }
                logger.info("ending search on: " + seq);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(TupleSpace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(TupleSpace.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Method finalizeThreads - This method watches each thread and waits until
     * all of them has finished
     * @throws SQLException
     * @throws InterruptedException
     */
    private void finalizeThreads() throws SQLException, InterruptedException {
        TupleSpace.logger.info("Waiting for threads to complete");
        while (threads.size() > 0) {
            for (int t=0; t<threads.size(); t++){
                if (!threads.get(t).isAlive()){
                    threads.remove(t);
                }
            }
            Thread.sleep(1); // wait 1ms and then continue
        }
        TupleSpace.logger.info("Threads completed");
    }
}
