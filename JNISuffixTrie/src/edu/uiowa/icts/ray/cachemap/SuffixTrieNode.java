package edu.uiowa.icts.ray.cachemap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Suffix trie node objects. Each node has an id and a hashmap of accession = offsets.
 *
 * @author Ray Hylock
 */
public class SuffixTrieNode implements Serializable {
    /**
     * Global variables
     */
    private static final long serialVersionUID = 1L;
    private int nodeID;
    private List<Integer> values = new ArrayList<Integer>();

    /**
     * Constructor: SuffixTrieAccessionNode - does nothing, required for serialization
     */
    public SuffixTrieNode() {
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
     * Returns the list of values
     * @return 
     */
    public synchronized List<Integer> getValues() {
        return this.values;
    }

    /****************************************************************************
     *                           SETTER METTHODS                                *
     ****************************************************************************/
     /**
      * Adds the information to a blank node
      * @param node
      * @param values
      */
    public synchronized void setAll(int node, List<Integer> values) {
        this.nodeID = node;
        this.values.clear();
        this.values.addAll(values);
    }

    /**
     * Creates a new node from a nodeID and value
     * @param node
     * @param value
     */
    public synchronized void createNode(int node, int value){
        this.nodeID = node;
        this.addValue(value);
    }

    /**
     * Makes an exact copy of SuffixTrieAccessionNode n
     * @param n
     */
    public synchronized void copyAll(SuffixTrieNode n) {
        this.nodeID = n.getID();
        this.values.clear();
        this.values.addAll(n.getValues());
    }

    /**
     * Updates the contents of one node with that of another (id remains)
     * @param n
     */
    public synchronized void updateNode(SuffixTrieNode n) {
        this.values.clear();
        this.values.addAll(n.getValues());
    }

    /**
     * Set a nodes id
     * @param id 
     */
    public synchronized void setID(int id) {
        this.nodeID = id;
    }

    /**
     * Adds a value to the node
     * @param value
     */
    public synchronized void addValue(int value) {
        this.values.add(value);
    }

    /****************************************************************************
     *                           CLEARING METTHODS                              *
     ****************************************************************************/
    /**
     * Clear the values Vector
     */
    public synchronized void clearValues() {
        this.values.clear();
    }

    /**
     * Clears the contents of the node
     */
    public synchronized void clearAll() {
        this.nodeID = 0;
        this.values.clear();
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
