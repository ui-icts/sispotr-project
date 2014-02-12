package edu.uiowa.icts.ray.cachemap;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * Suffix trie accession node objects. Each node has an id and a hashmap of accession = offsets.
 *
 * @author Ray Hylock
 */
public class SuffixTrieAccessionNode implements Serializable {
    /**
     * Global variables
     */
    private static final long serialVersionUID = 1L;
    private int nodeID;
    private boolean inUse = false;
    private HashMap<Integer, HashSet<Integer>> accessions = new HashMap<Integer, HashSet<Integer>>();

    /**
     * Constructor: SuffixTrieAccessionNode - does nothing, required for serialization
     */
    public SuffixTrieAccessionNode() {
        /**
         * Does Nothing, required for serialization
         */
    }

    /****************************************************************************
     *                           GETTER METTHODS                                *
     ****************************************************************************/
    /**
     * Returns the node id
     * @return 
     */
    public synchronized int getID() {
        return this.nodeID;
    }

    /**
     * Returns true if the node is in use
     * @return 
     */
    public synchronized boolean getInUse(){
        return this.inUse;
    }

    /**
     * Returns the list of accessions and their offsets
     * @return 
     */
    public synchronized HashMap<Integer, HashSet<Integer>> getAccessions() {
        return this.accessions;
    }

    /**
     * Returns the set of offsets for the given accession
     * @param accession
     * @return 
     */
    public synchronized HashSet<Integer> getOffsets(int accession) {
        return this.accessions.get(accession);
    }

    /****************************************************************************
     *                           SETTER METTHODS                                *
     ****************************************************************************/
     /**
      * Adds the information to a blank node
      * @param node
      * @param accessions
      */
    public synchronized void setAll(int node, HashMap<Integer, HashSet<Integer>> accessions) {
        this.inUse = true;
        this.nodeID = node;
        this.accessions.clear();
        this.accessions.putAll(accessions);
    }

    /**
     * Creates a new node from a nodeID, accession number, and offset
     * @param node
     * @param accession
     * @param offset
     */
    public synchronized void createNode(int node, int accession, int offset){
        this.inUse = true;
        this.nodeID = node;
        this.addOffset(accession, offset);
    }

    /**
     * Makes an exact copy of SuffixTrieAccessionNode n
     * @param n
     */
    public synchronized void copyAll(SuffixTrieAccessionNode n) {
        this.inUse = true;
        this.nodeID = n.getID();
        this.accessions.clear();
        this.accessions.putAll(n.getAccessions());
    }

    /**
     * Updates the contents of one node with that of another (id remains)
     * @param n
     */
    public synchronized void updateNode(SuffixTrieAccessionNode n) {
        this.accessions.clear();
        this.accessions.putAll(n.getAccessions());
    }

    /**
     * Set a nodes id
     * @param id 
     */
    public synchronized void setID(int id) {
        this.nodeID = id;
    }


    /**
     * Adds an offset to an accession. If the accession does not exist, then
     * it than entry is created, else the accession offset list is updated.
     * @param accession
     * @param offset 
     */
    public synchronized void addOffset(int accession, int offset) {
        HashSet<Integer> t;
        if(this.accessions.containsKey(accession)){
            t = this.getOffsets(accession);
        } else {
            t = new HashSet<Integer>();
        }
        t.add(offset);
        this.accessions.put(accession, t);
    }

    /****************************************************************************
     *                           CLEARING METTHODS                              *
     ****************************************************************************/
    /**
     * Clear the accessions HashMap
     */
    public synchronized void clearAccessions() {
        this.accessions.clear();
    }

    /**
     * Clears the contents of the node and marks it as unused
     */
    public synchronized void clearAll() {
        this.inUse = false;
        this.nodeID = 0;
        this.accessions.clear();
    }
    /****************************************************************************
     *                      GARBAGE COLLECTION METTHOD                          *
     ****************************************************************************/
    /**
     * Override Method: finalize - when the garbage collector is called, a SuffixTrieAccessionNode
     * that is to be deleted has the method finalize() called on it to wrap
     * up anything needed before it is "deleted".  This simply prints out the
     * object that this being deleted as a visual check, but additional code
     * can be added for additional finalization functionality
     *
     * Original method: java.lang.Object.finalize() - empty method
     */
    /*@Override protected void finalize(){
        System.out.println("Deleting SuffixTrieAccessionNode: " + this);
    }*/
}
