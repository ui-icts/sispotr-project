/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uiowa.icts.safeseed.linearscan;

import java.util.LinkedHashMap;

/**
 *
 * @author Ray
 */
public interface ILinearScan {
    public void searchMap(LinkedHashMap<String, String> seqs, String seq, boolean sortKey);
    public void searchMapReg(LinkedHashMap<String, String> seqs, String seq, boolean sortKey);
}
