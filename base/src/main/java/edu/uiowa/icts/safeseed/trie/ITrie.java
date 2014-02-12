/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uiowa.icts.safeseed.trie;

import java.util.LinkedHashMap;

/**
 *
 * @author Ray
 */
public interface ITrie {
    public void generateTree(LinkedHashMap<String, String> seq);
    public void search(String seq, boolean sortKey);
}
