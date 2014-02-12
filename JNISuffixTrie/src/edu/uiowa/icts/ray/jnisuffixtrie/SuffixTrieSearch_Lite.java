package edu.uiowa.icts.ray.jnisuffixtrie;

import edu.uiowa.icts.ray.cachemap.CacheMap;
import edu.uiowa.icts.ray.cachemap.SuffixTrieAccessionNode;
import edu.uiowa.icts.ray.database.DBConnect;
import edu.uiowa.icts.ray.ntmanipulation.NTManipulation;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 *
 * Switched from Short, Integer to Long, Long due to callback restrictions in JNI
 * from C++. Convert unsigned short and unsigned int to String then to Long. No way
 * to return unsigned short.
 * @author Ray
 */
public class SuffixTrieSearch_Lite {
    // global search results, don't want covering solutions - 0 mm -> x mm
    // accession short = adjusted offset (which is offset - leading mismatches)
    private ConcurrentHashMap<Integer, HashSet<Integer>> solutions = new ConcurrentHashMap<Integer, HashSet<Integer>>();
    private ConcurrentHashMap<String, ConcurrentHashMap<Integer, HashSet<Integer>>> searchedSegments =
            new ConcurrentHashMap<String, ConcurrentHashMap<Integer, HashSet<Integer>>>();
    private int totalSolutions = 0;
    private int uniqueSolutions = 0;

    // timer variables
    private double searchStart;
    private double searchEnd;
    private double searchTotal;

    // logger variables
    private static Logger logger = Logger.getLogger(SuffixTrieSearch_Lite.class);
    private static SimpleLayout layout = new SimpleLayout();
    private static Appender appender = new ConsoleAppender(layout);
    private static Level logLevel = Level.INFO;    // D, I, W, E, F

    // connection variables
    private Properties results;
    private Properties suffixTrie;

    // thread variables
    private final int maxConnections;
    private Connection conn;
    private List<Connection> connections = new ArrayList<Connection>();
    private List<Thread> threads = new ArrayList<Thread>();
    
    // SuffixTrie pointer
    private SuffixTrie_Lite st = null;
    private NTManipulation nt = new NTManipulation();
    private CacheMap<Integer, SuffixTrieAccessionNode> cm = null;
    private DBConnect db = null;
    
    // db query
    private final String insertSolution;
    private final CallableStatement batchInsert;
    private static final DecimalFormat percentDF = new DecimalFormat("###.######%");

    public SuffixTrieSearch_Lite(SuffixTrie_Lite st, Properties results, Properties suffixTrie, 
            int maxConnections, CacheMap<Integer, SuffixTrieAccessionNode> cm, DBConnect db) throws SQLException {
        // setup logger
        SuffixTrieSearch_Lite.logger.setLevel(logLevel);
        SuffixTrieSearch_Lite.logger.addAppender(appender);

        // class pointers
        this.st = st;
        this.cm = cm;
        this.db = db;
        
        // add connection variables
        this.results = results;
        this.suffixTrie = suffixTrie;
        this.maxConnections = maxConnections;
        
        // set up batch insert into results
        this.insertSolution = "INSERT INTO " + results.getProperty("schema") + ".search_result "
                + "(search_result_id, search_seq, search_seq_rc, result_seq, "
                + "result_accession, result_accession_offset, result_offset, time_stamp) VALUES "
                + "(nextval('" + results.getProperty("schema") + ".search_results_sequence')"
                + ",?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";
        this.conn = newConnection(this.results);
        this.batchInsert = conn.prepareCall(insertSolution);
    }

    /**
     * Method newConnection - Connects to the database and then returns the database connection
     * @return  the database connection
     */
    private Connection newConnection(Properties properties) {
        Connection c = null;
        try {
            // load and register the JDBC driver
            Class.forName(properties.getProperty("driver")).newInstance();

            // connect to the database
            c = DriverManager.getConnection(properties.getProperty("connection"), 
                    properties.getProperty("username"), properties.getProperty("password"));

            // turn off autocommit
            c.setAutoCommit(false);

        } catch (Exception e) {
            SuffixTrieSearch_Lite.logger.error("Connection attempt failed");
            e.printStackTrace();
        }
        return c;
    }

    public boolean spawnThread(int[][] ia, String seq, int leadingMismatches) throws SQLException{
        boolean spawned = false;
        //if (connections.size() < this.maxConnections) {
            // create the threads for the batch processes
            Connection c = this.newConnection(suffixTrie);
            searchThread st = new searchThread(ia, seq, leadingMismatches, c);
            st.run();
            /*Thread t = new Thread(st);
            threads.add(t);
            connections.add(c);
            t.start();*/
            spawned = true;
            SuffixTrieSearch_Lite.logger.debug("thread started");
        //}
        return spawned;
    }

    /**
     * Search is NOT case sensitive
     * 
     * @param startSeq
     * @throws InterruptedException
     * @throws SQLException 
     */
    public void search(String startSeq) throws InterruptedException, SQLException {
        // start the timer and clear solutions
        searchStart = System.currentTimeMillis();
        this.solutions.clear();

        SuffixTrieSearch_Lite.logger.debug("-----search-----");
        
        // get the rc to search
        String seq = nt.reverseComplement(startSeq); 

        /**
         * Build all 0, 1, 2, and 3 mismatches
         * Search segments within each mismatch string
         */
        int len = seq.length();

        // zero mismatch
        int[][] iaa = {{0, len}};  // [seq][offset, length]

        SuffixTrieSearch_Lite.logger.debug(seq);
        // spawn threads only if there are enough connections
        while (!this.spawnThread(iaa, seq, 0)) {
            Thread.sleep(1); // wait 1ms then try to spawn a thread again
        }
        
        // one mismatch
        for (int i = 0; i < len; i++) {
            SuffixTrieSearch_Lite.logger.debug("---1 mm-search " + i + "---");
            int[][] ia = new int[2][2];
            ia[0][0] = 0;
            ia[0][1] = i;
            ia[1][0] = i+1;
            ia[1][1] = len-i-1;

            ia = this.sortSegments(ia);

            for(int a=0; a<ia.length; a++){
                if(ia[0][1] != 0){
                    SuffixTrieSearch_Lite.logger.debug("  " + ia[a][0] + " - " + ia[a][1] + ": " + seq.substring(ia[a][0], ia[a][1] + ia[a][0]));
                }
            }
            if(i == 0){
                // spawn threads only if there are enough connections
                while (!this.spawnThread(ia, seq, 1)) {
                    Thread.sleep(1); // wait 1ms and then continue
                }
            } else {
                // spawn threads only if there are enough connections
                while (!this.spawnThread(ia, seq, 0)) {
                    Thread.sleep(1); // wait 1ms and then continue
                }
            }
        }

        // two mismatchs
        for (int i=0; i<len; i++) {
            for(int j=i+1; j<len; j++){
                SuffixTrieSearch_Lite.logger.debug("---2 mm-search " + i + ", " + j + "---");
                int[][] ia = new int[3][2];
                ia[0][0] = 0;
                ia[0][1] = i;
                ia[1][0] = i+1;
                ia[1][1] = j-i-1;
                ia[2][0] = Math.min(j+1, len-1);     // can't go over the length
                // don't want a negative value if j = len-1
                if(len-j-1 < 0){
                    ia[2][1] = 0;
                } else {
                    ia[2][1] = len-j-1;
                }

                ia = this.sortSegments(ia);

                for(int a=0; a<ia.length; a++){
                    if(ia[0][1] != 0){
                        SuffixTrieSearch_Lite.logger.debug("  " + ia[a][0] + " - " + ia[a][1] + ": " + seq.substring(ia[a][0], ia[a][1] + ia[a][0]));
                    }
                }
                if(i == 0 && j == 1){
                    // spawn threads only if there are enough connections
                    while (!this.spawnThread(ia, seq, 2)) {
                        Thread.sleep(1); // wait 1ms and then continue
                    }
                } else if(i == 0){
                    // spawn threads only if there are enough connections
                    while (!this.spawnThread(ia, seq, 1)) {
                        Thread.sleep(1); // wait 1ms and then continue
                    }
                } else {
                    // spawn threads only if there are enough connections
                    while (!this.spawnThread(ia, seq, 0)) {
                        Thread.sleep(1); // wait 1ms and then continue
                    }
                }
            }
        }

        // three mismatchs
        for (int i=0; i<len; i++) {
            for(int j=i+1; j<len; j++){
                for(int k=j+1; k<len; k++){
                    SuffixTrieSearch_Lite.logger.debug("---3 mm-search " + i + ", " + j + ", " + k + "---");
                    int[][] ia = new int[4][2];
                    ia[0][0] = 0;
                    ia[0][1] = i;
                    ia[1][0] = i+1;
                    ia[1][1] = j-i-1;
                    ia[2][0] = j+1;
                    ia[2][1] = k-j-1;
                    ia[3][0] = Math.min(k+1, len-1);     // can't go over the length
                    // don't want a negative value if k = len-1
                    if(len-k-1 < 0){
                        ia[3][1] = 0;
                    } else {
                        ia[3][1] = len-k-1;
                    }

                    ia = this.sortSegments(ia);

                    for(int a=0; a<ia.length; a++){
                        if(ia[0][1] != 0){
                            SuffixTrieSearch_Lite.logger.debug("  " + ia[a][0] + " - " + ia[a][1] + ": " + seq.substring(ia[a][0], ia[a][1] + ia[a][0]));
                        }
                    }
                    if(i == 0 && j == 1 && k == 2){
                        // spawn threads only if there are enough connections
                        while (!this.spawnThread(ia, seq, 3)) {
                            Thread.sleep(1); // wait 1ms and then continue
                        }
                    } else if(i == 0 && j == 1){
                        // spawn threads only if there are enough connections
                        while (!this.spawnThread(ia, seq, 2)) {
                            Thread.sleep(1); // wait 1ms and then continue
                        }
                    } else if(i == 0){
                        // spawn threads only if there are enough connections
                        while (!this.spawnThread(ia, seq, 1)) {
                            Thread.sleep(1); // wait 1ms and then continue
                        }
                    } else {
                        // spawn threads only if there are enough connections
                        while (!this.spawnThread(ia, seq, 0)) {
                            Thread.sleep(1); // wait 1ms and then continue
                        }
                    }
                }
            }
        }

        // wait until all threads have completed before finishing the algorithm
        this.finalizeThreads();

        /**
         * Write solutions to db
         */
        Iterator it = this.solutions.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry entry = (Map.Entry) it.next();
            int accession = ((Integer) entry.getKey()).intValue();
            HashSet<Integer> offsets = (HashSet<Integer>) entry.getValue();
            Iterator o = offsets.iterator();
            SuffixTrieSearch_Lite.logger.info(" solutions");
            int accessionLength = SuffixTrie_Lite.getSeqLength(accession);
            while(o.hasNext()){
                int offset = ((Integer) o.next()).intValue();
                int resultOffset = 0;       // leading overhang nt's
                // if the offset < 0, then we have a leading overhang
                if(offset < 0){
                    resultOffset = offset *-1;
                    offset = 0;
                }
                
                // if the first term is larger than the second, then we have a lagging overhang
                int endOffset = Math.min(offset + len - resultOffset, accessionLength);
                String range = SuffixTrie_Lite.getSeqRange(accession, offset, endOffset);
                
                SuffixTrieSearch_Lite.logger.info("    start: " + startSeq);
                SuffixTrieSearch_Lite.logger.info("    accession: " + accession);
                SuffixTrieSearch_Lite.logger.info("    offset: " + offset);
                SuffixTrieSearch_Lite.logger.info("    searchseq: " + seq);
                SuffixTrieSearch_Lite.logger.info("    substring: " + range);
                SuffixTrieSearch_Lite.logger.info("    result offset: " + resultOffset);
                int mismatches = 0;
                // inner offset fixes issues with leading overhangs
                int innerOffset = 0;
                // issues with lagging overhangs
                int max = seq.length();
                if(resultOffset == 0 && seq.length() > range.length()) max = range.length();
                for(int m=resultOffset; m<max; m++){
                    if(seq.charAt(m) != range.charAt(innerOffset)) mismatches++;
                    innerOffset++;
                }
                SuffixTrieSearch_Lite.logger.info("    mismatches: " + mismatches);
                SuffixTrieSearch_Lite.logger.info("    %identity: " + percentDF.format((((double) (seq.length()-mismatches))/(double) seq.length())));
                SuffixTrieSearch_Lite.logger.info("");
                
                // populate the batch statement
                this.batchInsert.setString(1, startSeq);                    // search_seq
                this.batchInsert.setString(2, seq);                         // search_seq_rc
                this.batchInsert.setString(3, range);                       // result_seq
                this.batchInsert.setString(4, String.valueOf(accession));   // result_accession
                this.batchInsert.setInt(5, offset);                         // result_accession_offset
                this.batchInsert.setInt(6, resultOffset);                   // result_offset
                this.batchInsert.addBatch();
            }
        }
        
        // write the soluitons to the results database
        this.batchInsert.executeBatch();
        this.conn.commit();

        // stop the timer, compute the difference, and print out the time
        searchEnd = System.currentTimeMillis();
        searchTotal = searchEnd - searchStart;
        SuffixTrieSearch_Lite.logger.info("Total solutions found: " + this.totalSolutions +
                ", unique solutions: " + this.uniqueSolutions);
        
        SuffixTrieSearch_Lite.logger.info("Searching took " + searchTotal + " ms");
    }


    /**
     * Begin threads
     */
    /**
     * Class searchThread - This class implements the threads for each connection.
     */
    //private class searchThread implements Runnable {
    private class searchThread {
        /**
         * This will reduce search complexity to O(1) on one end from |nmax|
         * accession = (offset,...)
         */
        private ConcurrentHashMap<Integer, HashSet<Integer>> tempResults = new ConcurrentHashMap<Integer, HashSet<Integer>>();
        private LinkedHashMap<Integer, HashSet<Integer>> searchResults = new LinkedHashMap<Integer, HashSet<Integer>>();
        private LinkedHashMap<Integer, HashSet<Integer>> sortedSearchResults = new LinkedHashMap<Integer, HashSet<Integer>>();
        private HashSet<Integer> outer = new HashSet<Integer>();   // search sequence offsets
        private HashSet<Integer> inner = new HashSet<Integer>();   // compare sequence offsets
        private List<Integer> removeShort = new ArrayList<Integer>();
        private List<Integer> removeInt = new ArrayList<Integer>();
        private List<Integer> update = new ArrayList<Integer>();
        
        /**
         * Variables
         */
        private Connection connection = null;
        private Statement stmt = null;
        private int[][] segments;
        private String seq;
        private SuffixTrieAccessionNode tempNode = null;
        private int leadingMismatches;

        // native methods
        private native String search(char[] seq, int length);

        /**
         * Constructor searchThread - Stores the table name and connection information.
         * For the connection, we only want <i>TYPE_FORWARD_ONLY</i> and <i>CONCUR_READ_ONLY</i>
         * access, <i>AutoCommit</i> to off, and a fetch size of around 200000.
         * @param tableName             the table name to crawl
         * @param leadingMismatches     the number of leading mismatches allowed - necessary for front overhangs
         * @param connection            the connection information
         * @throws SQLException
         */
        private searchThread(int[][] segments, String seq, int leadingMismatches, Connection connection) throws SQLException {
            this.segments = segments;
            this.seq = seq;
            this.connection = connection;
            this.stmt = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            this.connection.setAutoCommit(false);
            this.stmt.setFetchSize(10);
            this.leadingMismatches = leadingMismatches;
            SuffixTrieSearch_Lite.logger.debug("  st lm: " + leadingMismatches);
        }
        
        /**
         * Override method run - This is where all of the thread work is done.
         */
        public void run() {
            // search each segment
            for(int a=0; a<segments.length; a++){
                // if we've already searched for this segment, retrieve the results
                String subseq = seq.substring(segments[a][0], segments[a][1] + segments[a][0]);
                SuffixTrieSearch_Lite.logger.debug("seq: " + this.seq + ", sub: " + subseq);
                this.tempResults.clear();
                if(searchedSegments.containsKey(subseq)){
                    this.tempResults.putAll(searchedSegments.get(subseq));
                    SuffixTrieSearch_Lite.logger.debug("  searched");

                // else search the trie for the segment and store the results
                } else {
                    String s = this.search(subseq.toCharArray(), subseq.length());
                    if(s != null){
                        int nodeID = (int) Integer.parseInt(s);
                        // check the cachemap else get from db
                        tempNode = (SuffixTrieAccessionNode) cm.get(nodeID);
                        if(tempNode != null){
                            this.tempResults.putAll(tempNode.getAccessions());
                            SuffixTrieSearch_Lite.logger.debug(" matched to node: " + nodeID + ", " + tempNode.getAccessions());
                        } else {
                            try {
                                // check the database
                                tempNode = db.readSTAObj(nodeID);
                                cm.put(tempNode.getID(), tempNode);
                                if(tempNode != null){
                                    this.tempResults.putAll(tempNode.getAccessions());
                                    SuffixTrieSearch_Lite.logger.debug(" matched to node: " + nodeID + ", " + tempNode.getAccessions());
                                } else {
                                    SuffixTrieSearch_Lite.logger.error("Could not find node " + nodeID + " in database.");
                                }
                            } catch (SQLException ex) {
                                java.util.logging.Logger.getLogger(SuffixTrieSearch_Lite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                java.util.logging.Logger.getLogger(SuffixTrieSearch_Lite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                java.util.logging.Logger.getLogger(SuffixTrieSearch_Lite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                            }
                        }
                        ConcurrentHashMap<Integer, HashSet<Integer>> t = new ConcurrentHashMap<Integer, HashSet<Integer>>();
                        Iterator tempIt = this.tempResults.entrySet().iterator();
                        while(tempIt.hasNext()){
                            Map.Entry entry = (Map.Entry) tempIt.next();
                            Integer key = (Integer) entry.getKey();
                            HashSet<Integer> n = new HashSet<Integer>();
                            Iterator vIt = ((HashSet<Integer>) entry.getValue()).iterator();
                            while(vIt.hasNext()){
                                n.add((Integer) vIt.next());
                            }
                            t.put(key, n);
                        }
                        
                        SuffixTrieSearch_Lite.logger.debug("  global: " + subseq + " = " + t);
                        searchedSegments.put(subseq, t);
                    }
                    SuffixTrieSearch_Lite.logger.debug("Searching the trie took " + searchTotal + " ms");
                }

                // returned no hits for the set
                if (this.tempResults.isEmpty()) {
                    SuffixTrieSearch_Lite.logger.debug(" terminating search");
                    break;
                // else compare the sets
                } else {
                    this.compareSolutions(a, segments, tempResults);
                    if(this.searchResults.isEmpty()) break;
                }
            }

            /**
             * Only add solutions that have not been fully covered fully
             * by existing sequences.
             */
            if (this.searchResults.size() > 0) {
                Iterator it = this.searchResults.entrySet().iterator();
                // for the first set of global solutions
                boolean first = false;
                if(solutions.isEmpty()){
                    first = true;
                }
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Integer accession = (Integer) entry.getKey();
                    String accessionString = SuffixTrie_Lite.getAccessionName(accession);
                    HashSet<Integer> values = (HashSet<Integer>) entry.getValue();
                    Iterator<Integer> itv = values.iterator();

                    // only add solution if it has not already been covered by another (fully covered)
                    while(itv.hasNext()) {
                        int value = ((Integer) itv.next()).intValue();
                        totalSolutions++;
                        if(first){
                            uniqueSolutions++;
                            HashSet<Integer> iohs;
                            if(solutions.containsKey((int) accession)){
                                iohs = solutions.get((int) accession);
                            } else {
                                iohs = new HashSet<Integer>();
                            }
                            iohs.add((int) (value - leadingMismatches));
                            solutions.put((int) accession, iohs);
                            SuffixTrieSearch_Lite.logger.debug("  solution: " + accession + " - " +
                                accessionString + ", " + value + " - " + leadingMismatches);
                        } else {
                            boolean removed = false;
                            HashSet<Integer> iohs = solutions.get((int) accession);
                            if(iohs != null){
                                if(iohs.contains((int) (value - leadingMismatches))){
                                    removed = true;
                                // add offset to accession
                                } else {
                                    uniqueSolutions++;
                                    iohs.add((int) value - leadingMismatches);
                                }
                            // add accession and offset
                            } else {
                                uniqueSolutions++;
                                HashSet<Integer> n = new HashSet<Integer>();
                                n.add((int) value - leadingMismatches);
                                solutions.put((int) accession, n);
                            }
                            SuffixTrieSearch_Lite.logger.debug("  solution: " + accession + " - " +
                                accessionString + ", " + value + " - " + leadingMismatches +
                                ", overlap: " + removed);
                        }
                    }
                }
            }
            // clean up the threads and connections
            threads.remove(Thread.currentThread());
            connections.remove(connection);
            try {
                connection.close();
            } catch (SQLException ex) {
                SuffixTrieSearch_Lite.logger.error("Could not close connection in thread.");
            }
            SuffixTrieSearch_Lite.logger.debug("-----Removed thread " + Thread.currentThread().getName() + "-----");
        }
    
        private void compareSolutions(int startSeq, int[][] segments, ConcurrentHashMap<Integer, HashSet<Integer>> comp) {
            // if there are no previous matches, then we are on the first (or only if mismatch of 0) set of solutions
            SuffixTrieSearch_Lite.logger.debug("  comp: " + comp);
            SuffixTrieSearch_Lite.logger.debug("  serT: " + searchResults);
            if (startSeq == 0) {
                SuffixTrieSearch_Lite.logger.debug("  add initial");
                this.searchResults.clear();
                Iterator tempIt = comp.entrySet().iterator();
                while(tempIt.hasNext()){
                    Map.Entry entry = (Map.Entry) tempIt.next();
                    Integer key = (Integer) entry.getKey();
                    HashSet<Integer> n = new HashSet<Integer>();
                    Iterator vIt = ((HashSet<Integer>) entry.getValue()).iterator();
                    while(vIt.hasNext()){
                        n.add((Integer) vIt.next());
                    }
                    this.searchResults.put(key, n);
                }

            } else {
                SuffixTrieSearch_Lite.logger.debug("  union");
                removeShort.clear();
                Iterator it = this.searchResults.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Integer key = (Integer) entry.getKey();
                    outer = (HashSet<Integer>) entry.getValue();
                    SuffixTrieSearch_Lite.logger.debug("    compare: " + key);

                    // if we have an overlapping accession
                    if (comp.containsKey(key)) {
                        // offset of the first search sequence integer + the length of the match + the size of the mismatch = offset of com
                        removeInt.clear();
                        update.clear(); 
                        inner.clear();
                        inner.addAll((HashSet<Integer>) comp.get(key));
                        SuffixTrieSearch_Lite.logger.debug("      search: " + outer + ", compare: " + inner);

                        // iterate over the HashSet of offset values
                        Iterator<Integer> iti = outer.iterator();
                        while (iti.hasNext()) {
                            int outerOffset = iti.next().intValue();
                            int offset = outerOffset + segments[startSeq][2];
                            if (offset >= 0){
                                if (!inner.contains(offset)) {
                                    removeInt.add(outerOffset);
                                    SuffixTrieSearch_Lite.logger.debug("       remove: " + outerOffset);

                                // update the offset if going to right-to-left
                                } else if(segments[startSeq][2] < 0){
                                    this.update.add(outerOffset + segments[startSeq][2]);
                                    SuffixTrieSearch_Lite.logger.debug("       update: " + outerOffset + " to " + (outerOffset + segments[startSeq][2]));
                                }
                            } else {
                                this.removeInt.add(outerOffset);
                                SuffixTrieSearch_Lite.logger.debug("       remove: " + outerOffset);
                            }
                        }

                        // remove elements not used and remove sequences without any offsets (cannot merge with updates)
                        for (int i = 0; i < removeInt.size(); i++) {
                            outer.remove(removeInt.get(i));
                            SuffixTrieSearch_Lite.logger.debug("       removed: " + removeInt.get(i));
                            if (outer.isEmpty()) {
                                this.removeShort.add(key);
                            }
                        }

                        // rebuild outer with the updated values; either all or none will be updated
                        if(this.update.size() > 0){
                            outer.clear();
                            for(int u=0; u<this.update.size(); u++){
                                outer.add(this.update.get(u));
                            }
                        }
                    } else {
                        this.removeShort.add(key);
                    }
                }
                for (int i = 0; i < this.removeShort.size(); i++) {
                    this.searchResults.remove(this.removeShort.get(i));
                }
            }
            SuffixTrieSearch_Lite.logger.debug("  serE: " + searchResults);
        }
    } /* end thread*/

    /*private void sortSearchResults() {
        this.sortedSearchResults = sortLHMLong(this.searchResults);
        if (this.sortedSearchResults.size() > 0) {
            SuffixTrieSearch_Lite.logger.debug("Accession #: # of matches");
            Iterator it = this.sortedSearchResults.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                String key = (String) entry.getKey();
                List<Integer> value = (List<Integer>) entry.getValue();
                SuffixTrieSearch_Lite.logger.debug("  " + key + ": " + value);
            }
        } else {
            SuffixTrieSearch_Lite.logger.debug("No matches");
        }
        // stop the timer, compute the difference, and print out the time
        searchEnd = System.currentTimeMillis();
        searchTotal = searchEnd - searchStart;
    }*/

    /**
     * Sorts the temporary array tempArray[offset, length]
     * @param a
     * @return 
     */
    private int[][] sortSegments(int[][] a){
        int[][] ia;
        LinkedHashMap<Integer, Integer> s = new LinkedHashMap<Integer, Integer>();
        for(int i=0; i<a.length; i++){
            if(a[i][1] != 0){
                s.put(i, a[i][1]);
            }
        }
        
        int minOffset = 999999999;
        LinkedHashMap<Integer, Integer> ss = sortLHMInteger(s);
        Iterator<Integer> it = ss.keySet().iterator();
        int i = 0;
        ia = new int[ss.size()][3];
        while(it.hasNext()){
            int index = it.next();
            ia[i][0] = a[index][0];
            ia[i][1] = a[index][1];
            if(minOffset == 999999999){
                ia[i][2] = 0;
                minOffset = a[index][0];
            } else {
                int gap = a[index][0] - minOffset;
                ia[i][2] = gap;
                if(gap < minOffset) minOffset = a[index][0];
            }
            i++;
        }
        return ia;
    }

    /**
     * Sorts a LinkedHashMap<Integer, HashSet<Integer> by key
     * @param map   the map to sort
     * @return      the sorted LinkedHashMap
     *
     * usage:
     * LinkedHashMap<Integer, HashSet<Integer>> lhm = sortLHMShort(this.searchResults);
     */
    private static LinkedHashMap<Integer, HashSet<Integer>> sortLHMLong(LinkedHashMap map) {
        List list = new LinkedList(map.entrySet());
        // sort on key
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
            }
        });

        // put sorted list into map again
        LinkedHashMap sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    private static LinkedHashMap<Integer, Integer> sortLHMInteger(LinkedHashMap map) {
        List list = new LinkedList(map.entrySet());
        // sort on value
        Collections.sort(list, new Comparator() {

            // 2 - 1 = descending order
            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
            }
        });

        // put sorted list into map again
        LinkedHashMap sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }

    /**
     * Method finalizeThreads - This method watches each thread and waits until
     * all of them has finished
     * @throws SQLException
     * @throws InterruptedException
     */
    private void finalizeThreads() throws SQLException, InterruptedException {
        SuffixTrieSearch_Lite.logger.debug("Waiting for threads to complete");
        while (threads.size() > 0) {
            for (int t=0; t<threads.size(); t++){
                if (!threads.get(t).isAlive()){
                    threads.remove(t);
                }
            }
            Thread.sleep(1); // wait 1ms and then continue
        }
        SuffixTrieSearch_Lite.logger.debug("Threads completed");
    }
}
