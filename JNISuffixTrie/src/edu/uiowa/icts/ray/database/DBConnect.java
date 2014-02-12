package edu.uiowa.icts.ray.database;

import com.carrotsearch.hppc.IntOpenHashSet;
import edu.uiowa.icts.ray.cachemap.SuffixTrieAccessionNode;
import edu.uiowa.icts.ray.cachemap.SuffixTrieNode;
import edu.uiowa.icts.ray.jnisuffixtrie.SuffixTrie_Lite;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 * This handles all database work: connecting, reading, writing, updating, and closing
 * IMPORTANT! This class does the serialization so the global variables must be defined as transient
 *
 * @author Ray Hylock
 *
 */
public class DBConnect {
    // connection variables
    private final String driver_class;
    private final String connectionURL;
    private final String userID;
    private final String userPassword;
    //private final String schema;

    // miscellaneous variables
    private static final long serialVersionUID = 1L;
    private static boolean haveCommited = false;
    private static int commitsSinceLastGC = 0;
    private static int MAX_BATCH_COMMIT = 0;
    private static int MAX_COMMITED_BEFORE_GC = 0;

    // prepared statements - suffixTrieAccessions; the values within a node
    private final String createSuffixTrieAccessionsTable;
    private final String updateSTAObjSQL;
    private final String insertSTAObjSQL;
    private final String checkSTAObjSQL;
    private final String readSTAObjSQL;
    private final String readAllSTASQL;
    private final String dropSTATable;
    private final String checkSTATable;
    
    // prepared statements - accessions; the accessions and nt's themselves
    private final String createAccessionsTable;
    private final String updateAObjSQL;
    private final String insertAObjSQL;
    private final String checkAObjSQL;
    private final String readAObjSQL;
    private final String readAllASQL;
    private final String dropATable;
    private final String checkATable;
    
    // prepared statements - suffixTrie; the trie structure
    private final String createSuffixTrieTable;
    private final String updateSTObjSQL;
    private final String insertSTObjSQL;
    private final String checkSTObjSQL;
    private final String readSTObjSQL;
    private final String readAllSTSQL;
    private final String dropSTTable;
    private final String checkSTTable;
    private long maxNodeID = 0;
    
    // query variables
    private static Connection conn = null;
    private static CallableStatement bistmt_sta;
    private static CallableStatement bustmt_sta;
    private IntOpenHashSet batchListi_sta = new IntOpenHashSet();
    private IntOpenHashSet batchListu_sta = new IntOpenHashSet();
    private IntOpenHashSet nodesInDB_sta = new IntOpenHashSet();
    private static CallableStatement bistmt_st;
    private static CallableStatement bustmt_st;
    private IntOpenHashSet batchListi_st = new IntOpenHashSet();
    private IntOpenHashSet batchListu_st = new IntOpenHashSet();
    private IntOpenHashSet nodesInDB_st = new IntOpenHashSet();
    private static CallableStatement bistmt_a;
    private static CallableStatement bustmt_a;
    private HashSet<String> batchListi_a = new HashSet<String>();
    private HashSet<String> batchListu_a = new HashSet<String>();
    private HashSet<String> nodesInDB_a = new HashSet<String>();
    
    // logger variables
    private static Logger logger = Logger.getLogger(DBConnect.class);
    private static SimpleLayout layout = new SimpleLayout();
    private static Appender appender = new ConsoleAppender(layout);
    private static Level logLevel = Level.INFO;    // D, I, W, E, F

    // object pointers
    private static SuffixTrie_Lite st = null;

    /****************************************************************************
     *                        DATABASE SETUP METTHODS                           *
     ****************************************************************************/
    public DBConnect(Properties properties, int maxCommit, int maxBeforeGC) throws SQLException {
        // setup logger
        DBConnect.logger.setLevel(logLevel);
        DBConnect.logger.addAppender(appender);
        
        // set batch values
        MAX_BATCH_COMMIT = maxCommit;
        MAX_COMMITED_BEFORE_GC = maxBeforeGC;
        
        // grab database connection properties
        this.connectionURL = properties.getProperty("connection");
        this.driver_class = properties.getProperty("driver");
        this.userID = properties.getProperty("username");
        this.userPassword = properties.getProperty("password");
        //this.schema = properties.getProperty("schema");

        // setup the statements used throughout
        /*updateObjSQL = "UPDATE " + this.schema + ".suffixTrie SET obj = ? WHERE id = ?" ;
        insertObjSQL = "INSERT INTO " + this.schema + ".suffixTrie(id, obj) VALUES (?, ?)";
        checkObjSQL = "SELECT id FROM " + this.schema + ".suffixTrie WHERE id = ?";
        readObjSQL = "SELECT * FROM " + this.schema + ".suffixTrie WHERE id = ?";
        dropTable = "DROP TABLE " + this.schema + ".suffixTrie";
        checkTable = "SELECT * FROM pg_tables WHERE schemaname = '" + this.schema.toLowerCase() + "' AND tablename = 'suffixtrie'";
        createTable = " CREATE TABLE " + this.schema + ".suffixTrie ( " +
                      " id    NUMERIC, " +
                      " obj	BYTEA, " +
                      " CONSTRAINT suffixTrie_pk PRIMARY KEY(id) " +
                      " )";*/
        
        updateSTAObjSQL = "UPDATE suffixTrieAccessions SET obj = ? WHERE id = ?" ;
        insertSTAObjSQL = "INSERT INTO suffixTrieAccessions(id, obj) VALUES (?, ?)";
        checkSTAObjSQL = "SELECT id FROM suffixTrieAccessions WHERE id = ?";
        readSTAObjSQL = "SELECT * FROM suffixTrieAccessions WHERE id = ?";
        readAllSTASQL = "SELECT id FROM suffixTrieAccessions";
        dropSTATable = "DROP TABLE suffixTrieAccessions";
        checkSTATable = "SELECT * FROM pg_tables WHERE tablename = 'suffixtrieaccessions'";
        createSuffixTrieAccessionsTable = " CREATE TABLE suffixTrieAccessions ( " +
                      " id    NUMERIC, " +
                      " obj	BYTEA, " +
                      " CONSTRAINT suffixTrieAccessions_pk PRIMARY KEY(id) " +
                      " )";

        updateSTObjSQL = "UPDATE suffixTrie SET obj = ? WHERE id = ?" ;
        insertSTObjSQL = "INSERT INTO suffixTrie(id, obj) VALUES (?, ?)";
        checkSTObjSQL = "SELECT id FROM suffixTrie WHERE id = ?";
        readSTObjSQL = "SELECT * FROM suffixTrie WHERE id = ?";
        readAllSTSQL = "SELECT * FROM suffixTrie";
        dropSTTable = "DROP TABLE suffixTrie";
        checkSTTable = "SELECT * FROM pg_tables WHERE tablename = 'suffixtrie'";
        createSuffixTrieTable = " CREATE TABLE suffixTrie ( " +
                      " id    NUMERIC, " +
                      " obj	BYTEA, " +
                      " CONSTRAINT suffixTrie_pk PRIMARY KEY(id) " +
                      " )";

        updateAObjSQL = "UPDATE accessions SET sequence = ?, accession = ? WHERE id = ?" ;
        insertAObjSQL = "INSERT INTO accessions (id, accession, sequence) VALUES (?, ?, ?)";
        checkAObjSQL = "SELECT accession FROM accessions WHERE accession = ?";
        readAObjSQL = "SELECT * FROM accessions WHERE accession = ?";
        readAllASQL = "SELECT * FROM accessions";
        dropATable = "DROP TABLE accessions";
        checkATable = "SELECT * FROM pg_tables WHERE tablename = 'accessions'";
        createAccessionsTable = " CREATE TABLE accessions ( " +
                      " id          numeric, " +
                      " accession   text, " +
                      " sequence    text, " +
                      " CONSTRAINT accessions_pk PRIMARY KEY(id) " +
                      " )";

        // connect
        this.connect();
    }

    public static void setSuffixTrie(SuffixTrie_Lite st){
        DBConnect.st = st;
    }

    /**
     * Connects to the database and then returns the connection
     */
    public void connect() throws SQLException {
        try {
            // load and register the JDBC driver
            Class.forName(driver_class).newInstance();

            // connect to the database
            conn = DriverManager.getConnection(connectionURL, userID, userPassword);

            // turn off auto commit to save time
            conn.setAutoCommit(false);
        }
        catch (Exception e) {
            DBConnect.logger.error("Connection attempt failed");
            e.printStackTrace();
        }

        //set batch insert and update statements
        bistmt_sta = conn.prepareCall(insertSTAObjSQL);
        bustmt_sta = conn.prepareCall(updateSTAObjSQL);
        bistmt_st = conn.prepareCall(insertSTObjSQL);
        bustmt_st = conn.prepareCall(updateSTObjSQL);
        bistmt_a = conn.prepareCall(insertAObjSQL);
        bustmt_a = conn.prepareCall(updateAObjSQL);
    }

    /**
     * This will create a new suffix trie accession relation if one doesn't exist
     * @throws SQLException
     */
    private void newSTAIfNeeded() throws SQLException{
        boolean found = false;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(checkSTATable);
        if(rs.next()) found = true;
        if(!found) this.resetSuffixTrieAccessions();
    }

    /**
     * This will create a new suffix trie relation if one doesn't exist
     * @throws SQLException
     */
    private void newSTIfNeeded() throws SQLException{
        boolean found = false;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(checkSTTable);
        if(rs.next()){
            found = true;
        } else {
            st.addRoot('Z');
        }
        if(!found) this.resetSuffixTrie();
    }

    /**
     * This will create a new accession relation if one doesn't exist
     * @throws SQLException
     */
    private void newAIfNeeded() throws SQLException{
        boolean found = false;
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(checkATable);
        if(rs.next()) found = true;
        if(!found) this.resetAccessions();
    }

    /**
     * Resets all of the relations
     * @throws SQLException
     */
    public void resetAllRelations() throws SQLException{
        this.resetAccessions();
        this.resetSuffixTrie();
        this.resetSuffixTrieAccessions();
        st.addRoot('Z');
    }

    /**
     * Reads in the suffix trie and accession information for appending to the trie
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void appendToSuffixTrie() throws SQLException, IOException, ClassNotFoundException{
        this.newAIfNeeded();
        this.newSTIfNeeded();
        this.newSTAIfNeeded();
        this.readAllA();
        this.readAllST();
        this.readAllSTA();
        this.maxNodeID++;   // add one for the next node
        // jni call
        st.setNodeCounter(String.valueOf(this.maxNodeID));
    }

    /**
     * Reads in the suffix trie
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readInSuffixTrie() throws SQLException, IOException, ClassNotFoundException{
        this.readAllST();

        // may not need these
        /*this.maxNodeID++;   // add one for the next node
        // jni call
        st.setNodeCounter(String.valueOf(this.maxNodeID));*/
    }

    /**
     * Reads in the accessions
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public void readInAccessions() throws SQLException, IOException, ClassNotFoundException{
        this.readAllA();
    }

    private void resetSuffixTrieAccessions() throws SQLException{
        // initialize the statement variable
        Statement stmt = conn.createStatement();
        
        // suffix trie accessions
        ResultSet rs = stmt.executeQuery(checkSTATable);
        if(rs.next()){
            try {
                stmt.execute(dropSTATable);
            } catch (SQLException e) {
                DBConnect.logger.error("DROP TABLE attempt failed");
                //e.printStackTrace();
            }
        }
        // query and execution for CREATE TABLE
        stmt.execute(createSuffixTrieAccessionsTable);

        //set batch insert and update statements
        bistmt_sta = conn.prepareCall(insertSTAObjSQL);
        bustmt_sta = conn.prepareCall(updateSTAObjSQL);

        // commit the changes
        this.commit();
    }
    private void resetSuffixTrie() throws SQLException{
        // initialize the statement variable
        Statement stmt = conn.createStatement();

        // suffix trie
        ResultSet rs = stmt.executeQuery(checkSTTable);
        if(rs.next()){
            try {
                stmt.execute(dropSTTable);
            } catch (SQLException e) {
                DBConnect.logger.error("DROP TABLE attempt failed");
                //e.printStackTrace();
            }
        }
        // query and execution for CREATE TABLE
        stmt.execute(createSuffixTrieTable);

        //set batch insert and update statements
        bistmt_st = conn.prepareCall(insertSTObjSQL);
        bustmt_st = conn.prepareCall(updateSTObjSQL);

        // commit the changes
        this.commit();
    }
    private void resetAccessions() throws SQLException{
        // initialize the statement variable
        Statement stmt = conn.createStatement();

        // accessions
        ResultSet rs = stmt.executeQuery(checkATable);
        if(rs.next()){
            try {
                stmt.execute(dropATable);
            } catch (SQLException e) {
                DBConnect.logger.error("DROP TABLE attempt failed");
                //e.printStackTrace();
            }
        }
        // query and execution for CREATE TABLE
        stmt.execute(createAccessionsTable);

        //set batch insert and update statements
        bistmt_a = conn.prepareCall(insertAObjSQL);
        bustmt_a = conn.prepareCall(updateAObjSQL);

        // commit the changes
        this.commit();
    }

    /**
     * Closes the connection
     * @throws SQLException
     */
    public void close() throws SQLException, IOException {
        conn.close();
    }

    /**
     * Commits the database
     * @throws SQLException
     */
    public void commit() throws SQLException {
        conn.commit();
    }

    /**
     * Have we committed a transaction yet
     * @return  true if the application has commited
     */
    public boolean getHasCommited(){
        return haveCommited;
    }
    
    /**
     * Check to see if the id exists in the database via a hashmap and not
     * a direct database query - suffix trie accessions
     * @param id    the node id to check
     * @return      true if exists
     */
    public boolean checkSTADB(int id){
        return (this.nodesInDB_sta.contains(id)) ? true : false;
    }

    /**
     * Check to see if the id exists in the database via a hashmap and not
     * a direct database query - suffix trie
     * @param id    the node id to check
     * @return      true if exists
     */
    public boolean checkSTDB(int id){
        return (this.nodesInDB_st.contains(id)) ? true : false;
    }
    
    /**
     * Check to see if the id exists in the database via a hashmap and not
     * a direct database query - accessions
     * @param accession     the accession to check
     * @return      true if exists
     */
    public boolean checkADB(String accession){
        return (this.nodesInDB_a.contains(accession)) ? true : false;
    }

    /****************************************************************************
     *                      INSERT AND UPDATE METTHODS                          *
     ****************************************************************************/

    /**
     * Writes a suffix trie accession object to the database
     * @param obj   Object to be stored
     * @throws Exception
     */
    public void insertSTAObj(SuffixTrieAccessionNode obj) throws Exception {
        // create the statement
        CallableStatement stmt = null;

        // get insert statement
        stmt = conn.prepareCall(insertSTAObjSQL);

        // create the output streams
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objOstream = new ObjectOutputStream(baos);

        // write the object to the binary stream
        objOstream.writeObject(obj);
        objOstream.flush();
	objOstream.close();

        // set the parameters and add it to the batch
        stmt.setInt(1, obj.getID());
        stmt.setBytes(2, baos.toByteArray());
        stmt.executeUpdate();

        // close the statement
        stmt.close();
    }
    /**
     * Writes a suffix trie object to the database
     * @param obj   Object to be stored
     * @throws Exception
     */
    public void insertSTObj(SuffixTrieNode obj) throws Exception {
        // create the statement
        CallableStatement stmt = null;

        // get insert statement
        stmt = conn.prepareCall(insertSTObjSQL);

        // create the output streams
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objOstream = new ObjectOutputStream(baos);

        // write the object to the binary stream
        objOstream.writeObject(obj);
        objOstream.flush();
	objOstream.close();

        // set the parameters and add it to the batch
        stmt.setInt(1, obj.getID());
        stmt.setBytes(2, baos.toByteArray());
        stmt.executeUpdate();

        // close the statement
        stmt.close();
    }
    /**
     * Writes an accession entry to the database
     * @param accession     the accession number
     * @param sequence      the sequence itself
     * @throws Exception
     */
    public void insertAObj(String accession, StringBuilder sequence) throws Exception {
        // create the statement
        CallableStatement stmt = null;

        // get insert statement
        stmt = conn.prepareCall(insertAObjSQL);

        // set the parameters and add it to the batch
        stmt.setString(1, accession);
        stmt.setString(2, sequence.toString());
        stmt.executeUpdate();

        // close the statement
        stmt.close();
    }

    /**
     * Updates a suffix trie accession record in the database
     * @param obj   the node to update
     * @throws IOException
     * @throws SQLException
     */
    public void updateObj(SuffixTrieAccessionNode obj) throws IOException, SQLException {
        // create the statement using the prepared statement updateObjSQL
        CallableStatement stmt = conn.prepareCall(updateSTAObjSQL);	// create the statement

        // create the output streams
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objOstream = new ObjectOutputStream(baos);

        // write the object to the binary stream
        objOstream.writeObject(obj);
        objOstream.flush();
	objOstream.close();

        // set the parameters for the update (obj then id)
        stmt.setBytes(1, baos.toByteArray());
        stmt.setInt(2, obj.getID());
        stmt.executeUpdate();

        // close the statement and return the success answer (true/false)
        stmt.close();
    }
    /**
     * Updates a suffix trie record in the database
     * @param obj   the node to update
     * @throws IOException
     * @throws SQLException
     */
    public void updateSTObj(SuffixTrieNode obj) throws IOException, SQLException {
        // create the statement using the prepared statement updateObjSQL
        CallableStatement stmt = conn.prepareCall(updateSTObjSQL);	// create the statement

        // create the output streams
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objOstream = new ObjectOutputStream(baos);

        // write the object to the binary stream
        objOstream.writeObject(obj);
        objOstream.flush();
	objOstream.close();

        // set the parameters for the update (obj then id)
        stmt.setBytes(1, baos.toByteArray());
        stmt.setInt(2, obj.getID());
        stmt.executeUpdate();

        // close the statement and return the success answer (true/false)
        stmt.close();
    }
    /**
     * Updates an accession record in the database
     * @param obj   the node to update
     * @throws IOException
     * @throws SQLException
     */
    public void updateAObj(String accession, StringBuilder sequence) throws IOException, SQLException {
        // create the statement using the prepared statement updateObjSQL
        CallableStatement stmt = conn.prepareCall(updateAObjSQL);	// create the statement

        // set the parameters for the update
        stmt.setString(1, sequence.toString());
        stmt.setString(2, accession);
        stmt.executeUpdate();

        // close the statement and return the success answer (true/false)
        stmt.close();
    }

    /**
     * This creates a suffix trie accession batch of inserts/updates
     * @param obj   the object
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void batchSTAObj(SuffixTrieAccessionNode obj) throws IOException, SQLException, ClassNotFoundException {
        // conditionally execute the batch
        if(this.batchListi_sta.size() > MAX_BATCH_COMMIT){
            DBConnect.logger.info("Execute due to max batch size");
            //this.executeSTABatchCommands();
            this.executeBatchCommands();
            
        /**
         * If we are running out of TOTAL memory, execute the batch
         * It is currently set to 2000MB remaining before it executes
         */
        }   else if(((Runtime.getRuntime().maxMemory() - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))/1024/1024) < 2000){
            DBConnect.logger.info("Execute due to memory shortage");
            //this.executeSTABatchCommands();
            this.executeBatchCommands();
        } 

        // create the output streams
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objOstream = new ObjectOutputStream(baos);

        // write the object to the binary stream
        objOstream.writeObject(obj);
        objOstream.flush();
	objOstream.close();

        /**
         * If the object is not in the database and is not already in the insert
         * queue, then add it to the insert batch, else, add it to the update batch
         */
        if(!this.nodesInDB_sta.contains(obj.getID()) && !batchListi_sta.contains(obj.getID())){
            bistmt_sta.setInt(1, obj.getID());
            bistmt_sta.setBytes(2, baos.toByteArray());
            bistmt_sta.addBatch();

            // add the id to the batchList for comparisons
            batchListi_sta.add(obj.getID());

        } else {
            bustmt_sta.setBytes(1, baos.toByteArray());
            bustmt_sta.setInt(2, obj.getID());
            bustmt_sta.addBatch();

            // add the id to the batchList for comparisons
            batchListu_sta.add(obj.getID());
        }
    }
    /**
     * This creates a suffix trie batch of inserts/updates
     * @param obj   the object
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void batchSTObj(SuffixTrieNode obj) throws IOException, SQLException, ClassNotFoundException {
        // conditionally execute the batch
        if(this.batchListi_st.size() > MAX_BATCH_COMMIT){
            DBConnect.logger.info("Execute due to max batch size");
            //this.executeSTBatchCommands();
            this.executeBatchCommands();

        /**
         * If we are running out of TOTAL memory, execute the batch
         * It is currently set to 2000MB remaining before it executes
         */
        }   else if(((Runtime.getRuntime().maxMemory() - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))/1024/1024) < 2000){
            DBConnect.logger.info("Execute due to memory shortage");
            //this.executeSTBatchCommands();
            this.executeBatchCommands();
        }

        // create the output streams
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream objOstream = new ObjectOutputStream(baos);

        // write the object to the binary stream
        objOstream.writeObject(obj);
        objOstream.flush();
	objOstream.close();

        /**
         * If the object is not in the database and is not already in the insert
         * queue, then add it to the insert batch, else, add it to the update batch
         */
        if(!this.nodesInDB_st.contains(obj.getID()) && !batchListi_st.contains(obj.getID())){
            bistmt_st.setInt(1, obj.getID());
            bistmt_st.setBytes(2, baos.toByteArray());
            bistmt_st.addBatch();

            // add the id to the batchList for comparisons
            batchListi_st.add(obj.getID());

        } else {
            bustmt_st.setBytes(1, baos.toByteArray());
            bustmt_st.setInt(2, obj.getID());
            bustmt_st.addBatch();

            // add the id to the batchList for comparisons
            batchListu_st.add(obj.getID());
        }
    }
    /**
     * This creates an accessions batch of inserts/updates
     * @param id        the accession serial id
     * @param accession the accession number
     * @param sequence  the sequence
     * @throws IOException
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void batchAObj(int id, String accession, StringBuilder sequence) throws IOException, SQLException, ClassNotFoundException {
        // conditionally execute the batch
        if(this.batchListi_a.size() > MAX_BATCH_COMMIT){
            DBConnect.logger.info("Execute due to max batch size");
            //this.executeABatchCommands();
            this.executeBatchCommands();

        /**
         * If we are running out of TOTAL memory, execute the batch
         * It is currently set to 2000MB remaining before it executes
         */
        }   else if(((Runtime.getRuntime().maxMemory() - (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))/1024/1024) < 2000){
            DBConnect.logger.info("Execute due to memory shortage");
            //this.executeABatchCommands();
            this.executeBatchCommands();
        }
        
        /**
         * If the object is not in the database and is not already in the insert
         * queue, then add it to the insert batch, else, add it to the update batch
         */
        if(!this.nodesInDB_a.contains(accession) && !batchListi_a.contains(accession)){
            bistmt_a.setInt(1, id);
            bistmt_a.setString(2, accession);
            bistmt_a.setString(3, sequence.toString());
            bistmt_a.addBatch();

            // add the id to the batchList for comparisons
            batchListi_a.add(accession);

        } else {
            bustmt_a.setString(1, sequence.toString());
            bustmt_a.setString(2, accession);
            bustmt_a.setInt(3, id);
            bustmt_a.addBatch();

            // add the id to the batchList for comparisons
            batchListu_a.add(accession);
        }
    }

    /**
     * Checks to see if the id is designated as an insert or update. If so,
     * then the batch is executed.
     * @param id    the node id to check
     * @throws SQLException
     * @throws IOException 
     */
    public void checkSTABatch(int id) throws SQLException, IOException{
        if(batchListi_sta.contains(id) || batchListu_sta.contains(id)){
            //this.executeSTABatchCommands();
            this.executeBatchCommands();
        }
    }

    /**
     * Checks to see if the id is designated as an insert or update. If so,
     * then the batch is executed.
     * @param id    the node id to check
     * @throws SQLException
     * @throws IOException
     */
    public void checkSTBatch(int id) throws SQLException, IOException{
        if(batchListi_st.contains(id) || batchListu_st.contains(id)){
            //this.executeSTBatchCommands();
            this.executeBatchCommands();
        }
    }

    /**
     * Checks to see if the id is designated as an insert or update. If so,
     * then the batch is executed.
     * @param accession     accession id to check
     * @throws SQLException
     * @throws IOException
     */
    public void checkABatch(String accession) throws SQLException, IOException{
        if(batchListi_a.contains(accession) || batchListu_a.contains(accession)){
            //this.executeABatchCommands();
            this.executeBatchCommands();
        }
    }

    /**
     * Executes all of the batch commands inserting/updating
     * @throws SQLException
     */
    public void executeBatchCommands() throws SQLException, IOException{
        // execute the batch statements: insert first then update
        //DBConnect.logger.info("Executing suffix trie accession batch..." + batchListi_sta.size() + ", " + batchListu_sta.size());
        DBConnect.logger.info("Executing batch files ...");
        commitsSinceLastGC += batchListi_sta.size() + batchListu_sta.size();
        // need to execute all batches because we are closing the connection
        bistmt_sta.executeBatch();
        bustmt_sta.executeBatch();
        bistmt_st.executeBatch();
        bustmt_st.executeBatch();
        bistmt_a.executeBatch();
        bustmt_a.executeBatch();
        this.haveCommited = true;
        this.commit();

        // close the statments
        bistmt_sta.close();
        bustmt_sta.close();
        bistmt_st.close();
        bustmt_st.close();
        bistmt_a.close();
        bustmt_a.close();

        /**
         * Close the connection to the database
         * The connection MUST BE closed in order for the GC to collect the statements
         */
        this.close();

        // clear the batch lists
        this.nodesInDB_sta.addAll(this.batchListi_sta);
        batchListi_sta.clear();
        batchListu_sta.clear();
        this.nodesInDB_st.addAll(this.batchListi_st);
        batchListi_st.clear();
        batchListu_st.clear();
        this.nodesInDB_a.addAll(this.batchListi_a);
        batchListi_a.clear();
        batchListu_a.clear();

        // run the garbage collector
        if(commitsSinceLastGC > MAX_COMMITED_BEFORE_GC){
            DBConnect.logger.info("Running gc due to max nodes reached");
            System.gc();
            System.runFinalization();
            commitsSinceLastGC = 0;
        }

        // rReestablish a connection to the database
        this.connect();
        DBConnect.logger.info("Finished executing batch");
    }
    
    /**
     * Executes the suffix trie batch file for inserting/updating
     * @throws SQLException
     */
    /*public void executeSTBatchCommands() throws SQLException, IOException{
        // execute the batch statements: insert first then update
        DBConnect.logger.info("Executing the suffix trie batch..." + batchListi_st.size() + ", " + batchListu_st.size());
        commitsSinceLastGC += batchListi_st.size() + batchListu_st.size();
        // need to execute all batches because we are closing the connection
        bistmt_sta.executeBatch();
        bustmt_sta.executeBatch();
        bistmt_st.executeBatch();
        bustmt_st.executeBatch();
        bistmt_a.executeBatch();
        bustmt_a.executeBatch();
        this.haveCommited = true;
        this.commit();

        // close the statments
        bistmt_st.close();
        bustmt_st.close();*/

        /**
         * Close the connection to the database
         * The connection MUST BE closed in order for the GC to collect the statements
         */
        /*this.close();

        // clear the batch lists
        this.nodesInDB_st.addAll(this.batchListi_st);
        batchListi_st.clear();
        batchListu_st.clear();

        // run the garbage collector
        if(commitsSinceLastGC > MAX_COMMITED_BEFORE_GC){
            DBConnect.logger.info("Running gc due to max nodes reached");
            System.gc();
            System.runFinalization();
            commitsSinceLastGC = 0;
        }

        // rReestablish a connection to the database
        this.connect();
        DBConnect.logger.info("Finished executing batch");
    }*/

    /**
     * Executes the accession batch file for inserting/updating
     * @throws SQLException
     */
    /*public void executeABatchCommands() throws SQLException, IOException{
        // execute the batch statements: insert first then update
        DBConnect.logger.info("Executing the accession batch..." + batchListi_a.size() + ", " + batchListu_a.size());
        commitsSinceLastGC += batchListi_a.size() + batchListu_a.size();
        // need to execute all batches because we are closing the connection
        bistmt_sta.executeBatch();
        bustmt_sta.executeBatch();
        bistmt_st.executeBatch();
        bustmt_st.executeBatch();
        bistmt_a.executeBatch();
        bustmt_a.executeBatch();
        this.haveCommited = true;
        this.commit();

        // close the statments
        bistmt_a.close();
        bustmt_a.close();*/

        /**
         * Close the connection to the database
         * The connection MUST BE closed in order for the GC to collect the statements
         */
        /*this.close();

        // clear the batch lists
        this.nodesInDB_a.addAll(this.batchListi_a);
        batchListi_a.clear();
        batchListu_a.clear();

        // run the garbage collector
        if(commitsSinceLastGC > MAX_COMMITED_BEFORE_GC){
            DBConnect.logger.info("Running gc due to max nodes reached");
            System.gc();
            System.runFinalization();
            commitsSinceLastGC = 0;
        }

        // rReestablish a connection to the database
        this.connect();
        DBConnect.logger.info("Finished executing batch");
    }*/

    /****************************************************************************
     *                          RETRIEVAL METTHODS                              *
     ****************************************************************************/
    /**
     * Reads a suffix trie accession object from the database
     * @param id    the id of the object from the database
     * @return      return the object
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public SuffixTrieAccessionNode readSTAObj(int id) throws SQLException, IOException, ClassNotFoundException {
        // initialize the variables
        SuffixTrieAccessionNode obj = null;
        PreparedStatement stmt = null;
        byte [] newArray = null;

        // set the statement to readSTAObjSQL and set the id/pk
        stmt = conn.prepareStatement(readSTAObjSQL);        // create the statement
        stmt.setInt(1, id);                                 // set the first parameter to id/pk

        // execute the statement and store the results
        ResultSet rs = stmt.executeQuery();

        // grab the first result, if nothing, do nothing
        if (rs.next()) {
            // read the object, create the input stream, and store it
            newArray = rs.getBytes(2);
            ByteArrayInputStream bais = new ByteArrayInputStream(newArray);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = (SuffixTrieAccessionNode) ois.readObject();
            ois.close();
        }

        // close the statement and return the object (SuffixTrieAccessionNode)
        stmt.close();
        return obj;
    }

    /**
     * Reads in the entire suffix trie accessions from the database
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void readAllSTA() throws SQLException, IOException, ClassNotFoundException {
        // initialize the variables
        this.haveCommited = true;       // need this to use the read in sequences
        PreparedStatement stmt = null;
        int id = 0;

        // set the statement to readSTObjSQL and set the id/pk
        stmt = conn.prepareStatement(readAllSTASQL);         // create the statement

        // execute the statement and store the results
        ResultSet rs = stmt.executeQuery();

        // grab the results
        while (rs.next()) {
            // get the id
            id = rs.getInt(1);

            // add to list
            this.nodesInDB_sta.add(id);
        }

        // close the statement and return the object (SuffixTrieNode)
        stmt.close();
    }

    /**
     * Reads a suffix trie object from the database
     * @param id    the id of the object from the database
     * @return      return the object
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public SuffixTrieNode readSTObj(int id) throws SQLException, IOException, ClassNotFoundException {
        // initialize the variables
        SuffixTrieNode obj = null;
        PreparedStatement stmt = null;
        byte [] newArray = null;

        // set the statement to readSTObjSQL and set the id/pk
        stmt = conn.prepareStatement(readSTObjSQL);         // create the statement
        stmt.setInt(1, id);                                 // set the first parameter to id/pk

        // execute the statement and store the results
        ResultSet rs = stmt.executeQuery();

        // grab the first result, if nothing, do nothing
        if (rs.next()) {
            // read the object, create the input stream, and store it
            newArray = rs.getBytes(2);
            ByteArrayInputStream bais = new ByteArrayInputStream(newArray);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = (SuffixTrieNode) ois.readObject();
            ois.close();
        }

        // close the statement and return the object (SuffixTrieNode)
        stmt.close();
        return obj;
    }
    
    /**
     * Reads in the entire suffix trie from the database
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void readAllST() throws SQLException, IOException, ClassNotFoundException {
        // initialize the variables
        this.haveCommited = true;       // need this to use the read in sequences
        SuffixTrieNode obj = null;
        PreparedStatement stmt = null;
        byte [] newArray = null;
        int id = 0;

        // set the statement to readSTObjSQL and set the id/pk
        stmt = conn.prepareStatement(readAllSTSQL);         // create the statement

        // execute the statement and store the results
        ResultSet rs = stmt.executeQuery();

        // grab the results
        while (rs.next()) {
            // get the id
            id = rs.getInt(1);
            // read the object, create the input stream, and store it
            newArray = rs.getBytes(2);
            ByteArrayInputStream bais = new ByteArrayInputStream(newArray);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = (SuffixTrieNode) ois.readObject();
            ois.close();

            // add to list
            this.nodesInDB_st.add(id);

            // set max id
            this.maxNodeID = (id > this.maxNodeID) ? id : this.maxNodeID;

            // add node contents to the C++ trie
            List<Integer> l = obj.getValues();
            for(int i=0; i<l.size(); i++){
                // jni call
                st.setNode(String.valueOf(id), String.valueOf(l.get(i)));
            }
        }

        // close the statement and return the object (SuffixTrieNode)
        stmt.close();
    }

    /**
     * Reads an accession entry from the database
     * @param id    the id of the object from the database
     * @return      return the [accession, sequence] entry
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public String[] readAObj(int id) throws SQLException, IOException, ClassNotFoundException {
        // initialize the variables
        PreparedStatement stmt = null;
        String[] results = null;

        // set the statement to readObjSQL and set the id/pk
        stmt = conn.prepareStatement(readAObjSQL);  // create the statement
        stmt.setInt(1, id);                         // set the first parameter to id/pk

        // execute the statement and store the results
        ResultSet rs = stmt.executeQuery();

        // grab the first result, if nothing, do nothing
        if (rs.next()) {
            results = new String[2];
            results[0] = rs.getString(1);
            results[1] = rs.getString(2);
        }

        // close the statement and return the array
        stmt.close();
        return results;
    }

    /**
     * Reads in all of the accession numbers
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public void readAllA() throws SQLException, IOException, ClassNotFoundException {
        // initialize the variables
        this.haveCommited = true;       // need this to use the read in sequences
        PreparedStatement stmt = null;

        // set the statement to readObjSQL and set the id/pk
        stmt = conn.prepareStatement(readAllASQL);  // create the statement

        // execute the statement and store the results
        ResultSet rs = stmt.executeQuery();

        // grab the results
        while (rs.next()) {
            int id = rs.getInt(1);
            String accession = rs.getString(2);
            String sequence = rs.getString(3);
            this.nodesInDB_a.add(accession);

            // set the neccessary maps
            st.setAccessionTableByID(id, accession);
            st.setAccessionTableByName(accession, id);
            st.setSequenceByID(id, sequence);
        }

        // close the statement and return the array
        stmt.close();
    }

    /**
     * Replaced by checkObj above using the hashmap. Significant time improvement.
     * 
     * This method returns true if the object is found in the database
     * @param id    the id/pk of the object to be searched for
     * @return      true if found, else false
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    /*public boolean checkObj(int id) throws SQLException, IOException, ClassNotFoundException {
        // initialize the variables
        boolean found = false;
        PreparedStatement stmt = conn.prepareStatement(checkObjSQL);	// create the statement
        stmt.setInt(1, id);						// set the first parameter to id/pk

        // execute the statement and store the results
        ResultSet rs = stmt.executeQuery();

        // grab the first result, if exists, set found = true
        if (rs.next()) {
            found = true;
        }

        // close the statement and return the boolean found
        stmt.close();
        return found;
    }*/

    /****************************************************************************
     *                      GARBAGE COLLECTION METTHOD                          *
     ****************************************************************************/
    /**
     * Override Method: finalize - when the garbage collector is called, a DBConnect
     * that is to be deleted has the method finalize() called on it to wrap
     * up anything needed before it is "deleted".  This simply prints out the
     * object that this being deleted as a visual check, but additional code
     * can be added for additional finalization functionality
     *
     * Original method: java.lang.Object.finalize() - empty method
     */
    /*@Override protected void finalize(){
        DBConnect.logger.debug("Deleting DBConnect: " + this);
    }*/
}
