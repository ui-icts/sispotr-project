package edu.uiowa.icts.ray.cachemap;

import edu.uiowa.icts.ray.database.DBConnect;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.Map;
import java.util.ArrayList;
import java.util.Map.Entry;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 * This uses a linkedHashMap to simulate an access-ordered (LHM with flag true) cache.
 * When the limit is reached (which is defined by the variable cacheSize and is passed
 * to the constructor), the least recently used (LRU) object is removed.
 *
 * @author Ray Hylock
 *
 * @param <K>	the object type of the key
 * @param <V>	the object type of the value
 *
 */
public class CacheMap<K, V> {
    // initialize the global variables
    private LinkedHashMap<K, V> map;    // the linkedHashMap map itself
    private int cacheSize;              // the max size of the cache
    private int cacheMemory;            // the max space used by the entire application before executing removeEldestEntry
    private SuffixTrieAccessionNode n = new SuffixTrieAccessionNode();        // the instantiating SuffixTrieAccessionNode object
    private DBConnect db;               // the instantiating DBConnect object
    private boolean buildTrie = false;  // is this cachemap used for building the trie or searching

    // miscellaneous variables
    private static final long serialVersionUID = 1L;	// the serial number
    private final float hashTableLoadFactor = 0.75f;	// the load factor for the hashtable
    
    // logger variables
    private static Logger logger = Logger.getLogger(CacheMap.class);
    private static SimpleLayout layout = new SimpleLayout();
    private static Appender appender = new ConsoleAppender(layout);
    private static Level logLevel = Level.INFO;    // D, I, W, E, F

    /**
     * Sets the parameters for the caching process
     * @param cs        the maximum number of entries stored (the cacheSize)
     * @param cm        the maximum size of the application before we begin removing elements
     * @param build     is this CM used during the build process (if so then DB calls)
     */
    public CacheMap(int cs, int cm, boolean build) {
        // setup logger
        CacheMap.logger.setLevel(logLevel);
        CacheMap.logger.addAppender(appender);
        
        // initialize the variables
        this.cacheSize = cs;
        this.cacheMemory = cm;
        this.buildTrie = build;
        int hashTableCapacity = (int) Math.ceil(cacheSize / hashTableLoadFactor) + 1;

        /**
         * Create the LinkedHashMap, define the serialVersionUID, and overrides the 
         * method removeEldestEntry which is by default in the LinkedHashMap class set 
         * to expand the size of the LinkedHashMap and not remove the LRU
         * 
         * @param hashTableCapacity	the capacity which is a function of cacheSize and hashTableLoadFactor
         * @param hashTableLoadFactor	the loading factor
         * @param boolean value		true if by access order (LRU), false if by insertion order (FIFO)
         */
        this.map = new LinkedHashMap<K, V>(hashTableCapacity, hashTableLoadFactor, true) {

            private static final long serialVersionUID = 1;
          
            /**
             * If we are building the trie, this stores the node to be removed before
             * it is removed which happens as soon as size() > cacheSize is true and returned
             */
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                /**
                 * If the limit has been reached, store the LRU node before deletion
                 */
                // actual memory in MB used from the MAX possible; JVM might not have reached Xmx yet
                long mem = (Runtime.getRuntime().maxMemory() - 
                        (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()))/1024/1024;
                long allowed = (Runtime.getRuntime().maxMemory()/1024/1024) - cacheMemory;
                //if (map.size() > cacheSize) {
                if (mem < allowed) {
                    if(buildTrie){
                        try {
                            // grab the LRU node
                            db.batchSTAObj((SuffixTrieAccessionNode) eldest.getValue());
                        } catch (Exception ex) {
                            CacheMap.logger.error("Couldn't write Node " + eldest.getKey() + " to the database");
                        }
                    }
                }

                /**
                 * If the limit has been reached, this will cause the LRU node to be removed
                 * from the linkedHashMap
                 */
                //return map.size() > cacheSize;
                return mem < allowed;
            }
        };
    }

    /**
     * Assigns the database object address
     * @param dbc   the database object
     */
    public void setDB(DBConnect dbc) {
        this.db = dbc;
    }

    /**
     * Retrieves an entry from the variable map. The retrieved entry becomes the 
     * most recently used (MRU) entry.
     * @param key 	the key whose associated value is to be returned
     * @return    	the value associated with this key, or null if not present
     */
    public synchronized V get(K key) {
        return map.get(key);
    }

    /**
     * Adds an entry to the variable map. If the cache is full, the LRU entry is 
     * removed by the removeEldestEntry method above.
     * @param key	the key for the specified value
     * @param value	the value
     */
    public synchronized void put(K key, V value) {
        map.put(key, value);
    }

    /**
     * Clears the variable map
     */
    public synchronized void clear() {
        map.clear();
    }

    /**
     * Returns the number of entries in the variable map
     * @return	the number of entries in the variable map
     */
    public synchronized int usedEntries() {
        return map.size();
    }

    /**
     * Returns the entire contents in the map
     * @return	a Collection with a copy of the cache content
     */
    public synchronized Collection<Map.Entry<K, V>> getAll() {
        return new ArrayList<Map.Entry<K, V>>(map.entrySet());
    }

    /**
     * Grabs all of the entries in the variable map, iterates through the list, 
     * and writes it to the database
     * @throws Exception
     */
    public synchronized void writeAll() throws Exception {
        ArrayList<Entry<K, V>> set = new ArrayList<Map.Entry<K, V>>(map.entrySet());
        Iterator<Entry<K, V>> it = set.iterator();
        int count = 0;
        while (it.hasNext()) {
            n = (SuffixTrieAccessionNode) it.next().getValue();
            db.batchSTAObj(n);
            count++;
            if(count%100000 == 0){
                CacheMap.logger.info("Write: " + count);
            }
        }
        //db.executeSTABatchCommands();
        db.executeBatchCommands();
    }

    /****************************************************************************
     *                      GARBAGE COLLECTION METTHOD                          *
     ****************************************************************************/
    /**
     * Override Method: finalize - when the garbage collector is called, a CacheMap
     * that is to be deleted has the method finalize() called on it to wrap
     * up anything needed before it is "deleted".  This simply prints out the
     * object that this being deleted as a visual check, but additional code
     * can be added for additional finalization functionality
     *
     * Original method: java.lang.Object.finalize() - empty method
     */
    /*@Override protected void finalize(){
        CacheMap.logger.debug("Deleting CacheMap: " + this);
    }*/
}
