package edu.uiowa.icts.ray.jnisuffixtrie;

import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.ObjectIntOpenHashMap;
import com.carrotsearch.hppc.cursors.IntObjectCursor;
import edu.uiowa.icts.ray.cachemap.CacheMap;
import edu.uiowa.icts.ray.cachemap.SuffixTrieAccessionNode;
import edu.uiowa.icts.ray.cachemap.SuffixTrieNode;
import edu.uiowa.icts.ray.database.DBConnect;
import edu.uiowa.icts.ray.ntmanipulation.NTManipulation;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 * This is a SINGLETON method where the first time SuffixTrie_LiteHolder is called,
 * the method will create the SuffixTrie_Lite object; else it will retrieve the object.
 * Cannot make an interface class because we use static methods which aren't allowed
 * in an interface class.
 *
 * clean and build
 * run createH.bat in project
 * @author Ray
 */
public class SuffixTrie_Lite {
    /**
     * SuffixTrie_Lite JNI
     */
    private static int trieDepth = -1;
    private static boolean symmetrical = false;

    // logger variables
    private static Logger logger = Logger.getLogger(SuffixTrie_Lite.class);
    private static SimpleLayout layout = new SimpleLayout();
    private static Appender appender = new ConsoleAppender(layout);
    private static Level logLevel = Level.INFO;

    // timer variables
    private double generateStart;
    private double generateEnd;
    private double generateTotal;

    // accession information
    private static ObjectIntOpenHashMap<String> accessTableByName = new ObjectIntOpenHashMap<String>();
    private static IntObjectOpenHashMap<String> accessTableByID = new IntObjectOpenHashMap<String>();
    private static IntObjectOpenHashMap<StringBuilder> sequenceByID = new IntObjectOpenHashMap<StringBuilder>();
    private static IntObjectOpenHashMap<SuffixTrieNode> suffixTrieNodes = new IntObjectOpenHashMap<SuffixTrieNode>();
    //private final static short MIN_SHORT = -32768;  // min/max is 32768
    private static int accessionCounter = 0;
    private static int currentAccession = 0;

    // class variablees
    private static CacheMap cm = null;
    private static DBConnect db = null;
    private static SuffixTrieAccessionNode tempNode = null;
    private static NTManipulation nt = new NTManipulation();

    // JNI methods
    private native void addSeq(char[] seq, int length);
    private native void newSuffixTrie(int depth);
    private native boolean getInitializationStatus();
    public native void addRoot(char c);
    private native long getTotalNodes();
    private native void begingMemoryCounter();
    private native void endMemoryCounter();
    private native void printMemoryStatus();
    public native void setNodeCounter(String j);
    public native void setNode(String node, String vector);
    public native void writeSuffixTrie();

    /**
     * Simply sets up the loggers
     */
    private SuffixTrie_Lite() {
        // setup logger
        SuffixTrie_Lite.logger.setLevel(logLevel);
        SuffixTrie_Lite.logger.addAppender(appender);
    }

    /**
     * Gets the SuffixTrie_Lite instance and sets the global variables
     * @param td            the depth of the trie
     * @param cm            the cachemap object
     * @param db            the database object
     * @param symmetrical   are we to use symmetrical nucleotide search principles
     * @return              this
     */
    public static SuffixTrie_Lite getInstance(int td, CacheMap cm, DBConnect db, boolean symmetrical) {
        SuffixTrie_Lite.trieDepth = td;
        SuffixTrie_Lite.cm = cm;
        SuffixTrie_Lite.db = db;
        SuffixTrie_Lite.symmetrical = symmetrical;
        return SuffixTrie_LiteHolder.INSTANCE;
    }

    /**
     * Instantiates a new SuffixTrie_Lite object if one does not exist
     */
    private static class SuffixTrie_LiteHolder {
        private static final SuffixTrie_Lite INSTANCE = new SuffixTrie_Lite();
    }

    /**
     * Entry point for the suffix trie builder. Creates a root node and then calls
     * the refseq file reader.
     * @param fileName      the name of the refseq file
     * @param create        true if new trie false if append
     * @param maxpartial    the total number of partial sequences to read in (meaningless if create is false)
     */
    public void buildSuffixTrie(String fileName, boolean create, int maxPartial) throws SQLException, ClassNotFoundException{
        // create root node
        // handled by DBConnect now
        //addRoot('Z');

        /**
         * Read in sequences here
         */
        try {
            this.generateRefSeqTree(fileName, create, maxPartial);
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(SuffixTrie_Lite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(SuffixTrie_Lite.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    /**
     * Creates a new C++ suffix trie object of size trieDepth
     */
    public void initializeSuffixTrie(){
        new SuffixTrie_Lite().newSuffixTrie(trieDepth);
    }

    /**
     * Returns the status the C++ suffix trie (true if initialized)
     */
    public void getStatus(){
        System.out.println("SuffixTrie is intiailized: " + getInitializationStatus());
    }

    /**
     * Gets the name of the accession 
     * @param accession accession variables to retrieve name for
     * @return          the accession name
     */
    public static String getAccessionName(int accession){
        return SuffixTrie_Lite.accessTableByID.get(accession);
    }

    /**
     * Prints the C++ memory usage
     */
    public void getMemoryUsage(){
        printMemoryStatus();
    }

    /**
     * Starts the C++ memory counter
     */
    public void startMemoryCunter(){
        begingMemoryCounter();
    }

    /**
     * Ends the C++ memory counter which generates statistics and prints them
     */
    public void stopMemoryCounter(){
        endMemoryCounter();
    }
    
    /**
     * 
     * @param accessionID
     * @return 
     */
    public static int getSeqLength(int accessionID){
        return SuffixTrie_Lite.sequenceByID.get(accessionID).length();
    }
    
    /**
     * 
     * @param accessionID
     * @param start
     * @param end
     * @return 
     */
    public static String getSeqRange(int accessionID, int start, int end){
        return SuffixTrie_Lite.sequenceByID.get(accessionID).substring(start, end).toString();
    }
    
    public static void setAccessionTableByName(String lastAccession, int id){
        SuffixTrie_Lite.accessTableByName.put(lastAccession, id);
    }

    public static void setAccessionTableByID(int id, String lastAccession){
        SuffixTrie_Lite.accessTableByID.put(id, lastAccession);
    }

    public static void setSequenceByID(int id, String sequence){
        StringBuilder sb = new StringBuilder(sequence);
        SuffixTrie_Lite.sequenceByID.put(id, sb);
    }


    /**
     * Reads in the refseq file and calls the C++ addSeq() method
     * @param fileName      the refseq file name
     * @param create        true if create new false if append
     * @param maxpartial    the total number of partial sequences to read in (meaningless if create is false)
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void generateRefSeqTree(String fileName, boolean create, int maxPartial) throws FileNotFoundException, IOException, SQLException, ClassNotFoundException {
        /**
         * Read in the sequences and generate the trie
         */
        // start the timer
        generateStart = System.currentTimeMillis();
        SuffixTrie_Lite.logger.debug("-----Generate RefSeq Tree-----");

        // variables
        StringBuilder seqBuff = new StringBuilder();
        boolean first = true;

        // instantiate the file reader and read in the file
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);
        String s;
        DecimalFormat df = new DecimalFormat("###,###,###,###");
        int nucleotide = 0;             // the current bit position, new line, but continued bits
        double totalNucleotides = 0;    // total number of nucleotides in the data set
        double total = 0;
        String lastAccession = null;
        while ((s = br.readLine()) != null) {
            // FASTA header
            if (s.charAt(0) == '>') {
                s = s.replace(">", "");
                if (first) {
                    first = false;
                } else {
                    char[] ca = seqBuff.toString().toCharArray();
                    int l = seqBuff.length();
                    // get accession id
                    int a = SuffixTrie_Lite.accessionCounter++;
                    SuffixTrie_Lite.accessTableByName.put(lastAccession, a);
                    SuffixTrie_Lite.accessTableByID.put(a, lastAccession);
                    SuffixTrie_Lite.sequenceByID.put(a, seqBuff);
                    currentAccession = a;
                    db.batchAObj(a, lastAccession, seqBuff);
                    addSeq(ca, l);
                    seqBuff = new StringBuilder();
                    total++;
                    // terminate if partial build
                    if(!create && (total >= maxPartial)) break;
                }
                // 0=>gi, 1=gi-number, 2=from datasource, 3=accession, 4=locus
                if(s.contains("|")){
                    String[] t = s.split("\\|");
                    lastAccession = t[3].trim();
                } else {
                    lastAccession = s.trim();
                }

                // if appending and the accession exists, then start on the next one
                if(!create && db.checkADB(lastAccession)){
                    first = true;
                }

            // sequence data
            } else {
                // if we are skipping sequences, then we cannot perform these
                if(!first) {
                    seqBuff.append(s);
                    if(SuffixTrie_Lite.logLevel.equals(Level.DEBUG)) nucleotide += s.length();
                    SuffixTrie_Lite.logger.debug("\t\tcount = " + df.format(nucleotide));
                    if(SuffixTrie_Lite.logLevel.equals(Level.INFO)) totalNucleotides += s.length();
                }
            }
        }
        // run the final sequence if we didn't terminate early
        if(!create && (total >= maxPartial)){
            // do nothing
        } else {
            int a = SuffixTrie_Lite.accessionCounter++;
            SuffixTrie_Lite.accessTableByName.put(lastAccession, a);
            SuffixTrie_Lite.accessTableByID.put(a, lastAccession);
            SuffixTrie_Lite.sequenceByID.put(a, seqBuff);
            currentAccession = a;
            db.batchAObj(a, lastAccession, seqBuff);
            addSeq(seqBuff.toString().toCharArray(), seqBuff.length());
        }
        fr.close();

        SuffixTrie_Lite.logger.info("Total nucleotides = " + df.format(totalNucleotides));
        SuffixTrie_Lite.logger.info("Total nodes = " + df.format(getTotalNodes()));

        // stop the timer, compute the difference, and print out the time
        generateEnd = System.currentTimeMillis();
        generateTotal = generateEnd - generateStart;
        SuffixTrie_Lite.logger.info("Generating the RefSeq trie took " + generateTotal + " ms");
    }

    /**
     * This method is called from C++ after it has created a node. It returns 
     * the node id and the offset for the known accession number.
     * @param values        [0] = nodeid and [1] = offset
     * @throws SQLException
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    public static void addAccession(String values[]) throws SQLException, IOException, ClassNotFoundException{
        // get the data
        int nodeID = (int) Integer.parseInt(values[0]);
        int offset = (int) Integer.parseInt(values[1]);

        tempNode = (SuffixTrieAccessionNode) cm.get(nodeID);
        if(tempNode != null){
            tempNode.addOffset(currentAccession, offset);
        } else {
            // need to commit the batches if we need one of the elements
            db.checkSTABatch(nodeID);
            
            // if A fails, then B isn't checked; saves a considerable amount of time
            if(db.getHasCommited() && db.checkSTADB(nodeID)){
                tempNode = db.readSTAObj(nodeID);
                tempNode.addOffset(currentAccession, offset);
                SuffixTrieAccessionNode n = new SuffixTrieAccessionNode();
                n.copyAll(tempNode);
                cm.put(nodeID, n);
            } else {
                SuffixTrieAccessionNode n = new SuffixTrieAccessionNode();
                n.createNode(nodeID, currentAccession, offset);
                cm.put(nodeID, n);
            }
        }
    }

    /**
     * This method is called from the writeSuffixTrie method in C++. It returns
     * the nodeID and the current value (in order) during an iterative process.
     * @param values    [nodeID, some value]
     */
    public static void addNode(String values[]){
        try {
            int nodeID = (int) Integer.parseInt(values[0]);
            int value = (int) Integer.parseInt(values[1]);
            if(suffixTrieNodes.containsKey(nodeID)){
                suffixTrieNodes.get(nodeID).addValue(value);
            } else {
                SuffixTrieNode s = new SuffixTrieNode();
                s.createNode(nodeID, value);
                suffixTrieNodes.put(nodeID, s);
            }
        } catch (Exception e) {
            logger.warn("null value passed: " + values[0] + ", " + values[1]);
        }
    }

    public static void batchSuffixTrieNodes() throws IOException, SQLException, ClassNotFoundException{
        for(IntObjectCursor c : suffixTrieNodes){
            db.batchSTObj((SuffixTrieNode) c.value);
        }
        //db.executeSTBatchCommands();
        db.executeBatchCommands();
    }
    
    /**
     * This is called from C++ to compare characters.
     * @param c1    character 1
     * @param c2    character 2
     * @return      true if the characters are the "same" based on the nucleotide code table
     */
    public static boolean compareCharacter(int c1, char c2){
        //SuffixTrie_Lite.logger.info(c1 + ", " + c2);
        boolean match = nt.compareNTIntChar(c1, c2, symmetrical);
        return match;
    }
}
