/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uiowa.icts.safeseed.linearscan;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 *
 * @author Ray
 */
public class LinearScan implements ILinearScan {

    private static Logger logger = Logger.getLogger(LinearScan.class);
    private static SimpleLayout layout = new SimpleLayout();
    private static Appender appender = new ConsoleAppender(layout);
    private static Level logLevel = Level.DEBUG;
    private LinkedHashMap<String, Integer> searchResults = new LinkedHashMap<String, Integer>();
    private LinkedHashMap<String, Integer> sortedSearchResults = new LinkedHashMap<String, Integer>();

    public LinearScan() {
        // setup logger
        LinearScan.logger.setLevel(logLevel);
        LinearScan.logger.addAppender(appender);
    }

    public void searchMap(LinkedHashMap<String, String> seqs, String seq, boolean sortKey) {
        // iterate over the sequences
        Iterator<String> it = seqs.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = seqs.get(key);
            boolean loop = true;
            int pos = 0;
            while (loop) {
                String v = value.substring(pos, pos + seq.length());
                if (seq.equalsIgnoreCase(v)) {
                    int val = 1;
                    if (this.searchResults.containsKey(key)) {
                        val = this.searchResults.get(key) + 1;
                    }
                    this.searchResults.put(key, val);
                }

                if (value.length() - seq.length() == pos) {
                    loop = false;
                }
                pos++;
            }
        }
        this.printSoln(sortKey);
    }

    /**
     * Case sensitive:
     *  CAP = direct match (e.g., A = A)
     *  low = transitions (e.g., a = A or G)
     * @param seqs
     * @param seq
     * @param sortKey
     */
    public void searchMapReg(LinkedHashMap<String, String> seqs, String seq, boolean sortKey) {
        // iterate over the sequences
        Iterator<String> it = seqs.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            String value = seqs.get(key);
            boolean loop = true;
            boolean matchFull = false;
            int pos = 0;
            while (loop) {
                String v = value.substring(pos, pos + seq.length());
                matchFull = this.reg(v, seq);

                // if a full match, add it to the results
                if (matchFull) {
                    int val = 1;
                    if (this.searchResults.containsKey(key)) {
                        val = this.searchResults.get(key) + 1;
                    }
                    this.searchResults.put(key, val);
                }

                if (value.length() - seq.length() == pos) {
                    loop = false;
                }
                pos++;
            }
        }
        this.printSoln(sortKey);
    }

    public void searchRS(ResultSet seqs, String seq, boolean sortKey) throws SQLException {
        while (seqs.next()) {
            String key = seqs.getString(1);
            String value = seqs.getString(2);
            boolean loop = true;
            int pos = 0;
            while (loop) {
                String v = value.substring(pos, pos + seq.length());
                if (seq.equalsIgnoreCase(v)) {
                    int val = 1;
                    if (this.searchResults.containsKey(key)) {
                        val = this.searchResults.get(key) + 1;
                    }
                    this.searchResults.put(key, val);
                }

                if (value.length() - seq.length() == pos) {
                    loop = false;
                }
                pos++;
            }
        }
        this.printSoln(sortKey);
    }

    private void printSoln(boolean sortKey) {
        // LinkedHashMap, T/F sort by key (else value)
        this.sortedSearchResults = sortLHMStringInteger(this.searchResults, sortKey);
        if (this.sortedSearchResults.size() > 0) {
            System.out.println("Accession #: # of matches");
            Iterator<String> ite = this.sortedSearchResults.keySet().iterator();
            while (ite.hasNext()) {
                String key = ite.next();
                int value = this.sortedSearchResults.get(key);
                System.out.println("  " + key + ": " + value);
            }
        } else {
            System.out.println("No matches");
        }
    }

    private boolean reg(String v, String seq) {
        boolean matchFull = false;
        // compare each nucleotide
        for (int i = 0; i < v.length(); i++) {
            boolean match = false;
            char n = seq.charAt(i);
            if (n == 'A') {
                if (n == v.charAt(i)) {
                    match = true;
                }
            } else if (n == 'a') {
                if (v.charAt(i) == 'A' || v.charAt(i) == 'G') {
                    match = true;
                }
            } else if (n == 'C') {
                if (n == v.charAt(i)) {
                    match = true;
                }
            } else if (n == 'c') {
                if (v.charAt(i) == 'C' || v.charAt(i) == 'T') {
                    match = true;
                }
            } else if (n == 'G') {
                if (n == v.charAt(i)) {
                    match = true;
                }
            } else if (n == 'g') {
                if (v.charAt(i) == 'G' || v.charAt(i) == 'A') {
                    match = true;
                }
            } else if (n == 'T') {
                if (n == v.charAt(i)) {
                    match = true;
                }
            } else if (n == 't') {
                if (v.charAt(i) == 'T' || v.charAt(i) == 'C') {
                    match = true;
                }
            }

            // if there isn't a match, break the loop
            if (!match) {
                break;

                // if there is a match and this is the final nucleotide
            } else if (i + 1 == v.length()) {
                matchFull = true;
            }
        }
        return matchFull;
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
