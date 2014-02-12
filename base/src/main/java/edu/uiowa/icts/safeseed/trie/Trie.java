/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uiowa.icts.safeseed.trie;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 *
 * @author Ray
 */
public class Trie implements ITrie {

    private static int maxID = 0;
    private static Node root = null;
    private static Node current = null;
    private static Node previous = null;
    private static Logger logger = Logger.getLogger(Trie.class);
    private static SimpleLayout layout = new SimpleLayout();
    private static Appender appender = new ConsoleAppender(layout);
    private static Level logLevel = Level.INFO;
    private LinkedHashMap<String, Integer> searchResults = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String, Integer> sortedSearchResults = new LinkedHashMap<String, Integer>();

    // timer variables
    private double generateStart;
    private double generateEnd;
    private double generateTotal;
    private double searchStart;
    private double searchEnd;
    private double searchTotal;

    private class Node {

        private int id = -1;
        private String value;
        private int depth = -1;
        private List<String> accessions = new ArrayList<String>();
        private List<Integer> nextIDs = new ArrayList<Integer>();
        private List<Node> nextNodes = new ArrayList<Node>();

        public Node() {
            // does nothing at this point
        }

        public void setID(int id) {
            this.id = id;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public void setDepth(int depth) {
            this.depth = depth;
        }

        public void addAccession(String accession) {
            this.accessions.add(accession);
        }

        public void addNextID(int id) {
            this.nextIDs.add(id);
        }

        public void addNextNode(Node node) {
            this.nextNodes.add(node);
        }

        public int getID() {
            return this.id;
        }

        public String getValue() {
            return this.value;
        }

        public int getDepth() {
            return this.depth;
        }

        public List<String> getAccessions() {
            return this.accessions;
        }

        public List<Integer> getNextIDs() {
            return this.nextIDs;
        }

        public List<Node> getNextNodes() {
            return this.nextNodes;
        }
    }

    public Trie() {
        // setup logger
        Trie.logger.setLevel(logLevel);
        Trie.logger.addAppender(appender);

        // create root node
        Trie.root = new Node();
        Trie.root.setID(this.incrementID());
        Trie.root.setValue("root");
        Trie.root.setDepth(0);
    }

    private int incrementID() {
        // return the maxID THEN increment it
        return maxID++;
    }

    public void generateTree(LinkedHashMap<String, String> seq) {
        // start the timer
        generateStart = System.currentTimeMillis();
        
        Trie.logger.debug("-----generate tree-----");
        // iterate over the sequences
        Iterator<String> it = seq.keySet().iterator();
        int count = 0;
        int countIn = 0;
        while (it.hasNext()) {
            count++;
            Trie.logger.info(count);
            String key = it.next();
            String value = seq.get(key);

            // for each position in the sequence (root -> positions)
            for (int j = 0; j < value.length(); j++) {
                countIn++;
                Trie.logger.info("\t" + countIn);
                // if the position node exists, retrieve it
                // the position nodes are created in order so this works
                if (Trie.root.getNextNodes().size() >= (j + 1)) {
                    for (int k = 0; k < Trie.root.getNextNodes().size(); k++) {
                        if (Trie.root.getNextNodes().get(k).getValue().equals(String.valueOf(j))) {
                            Trie.previous = Trie.root.getNextNodes().get(k);
                            Trie.logger.debug("fp: " + Trie.previous.getValue());
                            break;
                        }
                    }
                    // else create it
                } else {
                    Trie.previous = new Node();
                    Trie.previous.setID(this.incrementID());
                    Trie.previous.setValue(String.valueOf(j));
                    Trie.previous.setDepth((Trie.root.getDepth() + 1));
                    Trie.logger.debug("cp: " + Trie.previous.getValue());
                    Trie.root.addNextID(Trie.previous.getID());
                    Trie.root.addNextNode(Trie.previous);
                }

                // build the tree beginning with the starting position j
                for (int l = j; l < value.length(); l++) {
                    boolean found = false;
                    // four at most (A,C,G,T)
                    for (int k = 0; k < Trie.previous.getNextNodes().size(); k++) {
                        if (Trie.previous.getNextNodes().get(k).getValue().equals(Character.toString(value.charAt(l)))) {
                            Trie.current = Trie.previous.getNextNodes().get(k);
                            Trie.logger.debug("fn: " + Trie.current.getValue());
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        Trie.current = new Node();
                        Trie.current.setID(this.incrementID());
                        Trie.current.setValue(Character.toString(value.charAt(l)));
                        Trie.current.setDepth(Trie.previous.getDepth() + 1);
                        Trie.logger.debug("  cn: " + Trie.current.getValue());
                        Trie.previous.addNextID(Trie.current.getID());
                        Trie.previous.addNextNode(Trie.current);
                    }
                    Trie.current.addAccession(key);
                    Trie.previous = Trie.current;
                }
            }
        }
        // stop the timer, compute the difference, and print out the time
        generateEnd = System.currentTimeMillis();
        generateTotal = generateEnd - generateStart;
        Trie.logger.info("Generating the trie took " + generateTotal + " ms");
    }

    // use a stack because we need a DFS
    public void search(String seq, boolean sortKey) {
        // start the timer
        searchStart = System.currentTimeMillis();

        Trie.logger.debug("-----search-----");
        Stack<Node> s = new Stack();
        // add all of the positions to the stack, treat them as roots
        for (int i = 0; i < Trie.root.getNextNodes().size(); i++) {
            s.push(Trie.root.getNextNodes().get(i));
        }
        while (!s.isEmpty()) {
            Node temp = s.pop();
            Trie.logger.debug("Node: " + temp.getValue());
            for (int i = 0; i < temp.getNextNodes().size(); i++) {
                Node t = temp.getNextNodes().get(i);
                Trie.logger.debug("Depth: " + t.getDepth());
                Trie.logger.debug("compare: " + seq.substring(t.getDepth() - 2, t.getDepth() - 1) + " = " + t.getValue());
                if (t.getValue().equalsIgnoreCase(seq.substring(t.getDepth() - 2, t.getDepth() - 1))) {
                    Trie.logger.debug("  true");
                    if (t.getDepth() - 2 == (seq.length() - 1)) {
                        Trie.logger.debug("found a match: ");
                        for (int j = 0; j < t.getAccessions().size(); j++) {
                            Trie.logger.debug("  " + t.getAccessions().get(j));
                            if (this.searchResults.containsKey(t.getAccessions().get(j))) {
                                Integer n = this.searchResults.get(t.getAccessions().get(j)) + 1;
                                this.searchResults.put(t.getAccessions().get(j), n);
                            } else {
                                this.searchResults.put(t.getAccessions().get(j), 1);
                            }
                        }
                        break;
                    }
                    s.push(t);
                }
            }
        }
        // LinkedHashMap, T/F sort by key (else value)
        this.sortedSearchResults = sortLHMStringInteger(this.searchResults, sortKey);
        if (this.sortedSearchResults.size() > 0) {
            System.out.println("Accession #: # of matches");
            Iterator<String> it = this.sortedSearchResults.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                int value = this.sortedSearchResults.get(key);
                System.out.println("  " + key + ": " + value);
            }
        } else {
            System.out.println("No matches");
        }
        // stop the timer, compute the difference, and print out the time
        searchEnd = System.currentTimeMillis();
        searchTotal = searchEnd - searchStart;
        Trie.logger.info("Searching the trie took " + searchTotal + " ms");
    }

    // use a queue to get a BFS
    private void print() {
        System.out.println("-----print-----");
        Queue<Node> q = new LinkedList<Node>();
        q.offer(Trie.root);
        while (!q.isEmpty()) {
            Node temp = q.remove();
            System.out.println(temp.getValue());
            for (int i = 0; i < temp.getNextNodes().size(); i++) {
                q.offer(temp.getNextNodes().get(i));
                System.out.println("  " + temp.getNextNodes().get(i).getValue() + ", depth: " + temp.getNextNodes().get(i).getDepth());
            }
        }
    }

    /**
     * Sorts a LinkedHashMap<String, Integer> by key or value
     * @param map   the map to sort
     * @param key   true for key false for value
     * @return      the sorted LinkedHashMap
     *
     * usage:
     * LinkedHashMap<String, String> lhm = sortByComparator(this.searchResults, true);
     */
    private static LinkedHashMap<String, Integer> sortLHMStringInteger(LinkedHashMap map, boolean key) {
        List list = new LinkedList(map.entrySet());
        // sort on key
        if (key) {
            // sort list based on comparator
            Collections.sort(list, new Comparator() {

                public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
                }
            });
            // sort on value
        } else {
            // sort list based on comparator
            Collections.sort(list, new Comparator() {

                public int compare(Object o1, Object o2) {
                    int ret = 0;    // default is same value
                    int i1 = (Integer) ((Map.Entry) (o1)).getValue();
                    int i2 = (Integer) ((Map.Entry) (o2)).getValue();

                    // if the number passed is smaller
                    if (i1 > i2) {
                        ret = 1;

                        // if the number passed is larger
                    } else if (i1 < i2) {
                        ret = -1;
                    }
                    return ret;
                }
            });
        }

        // put sorted list into map again
        LinkedHashMap sortedMap = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
