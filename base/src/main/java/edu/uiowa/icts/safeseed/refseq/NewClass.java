/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uiowa.icts.safeseed.refseq;

import edu.uiowa.icts.safeseed.trie.Trie;

import java.io.IOException;
import java.text.DecimalFormat;

/**
 *
 * @author Ray
 */
public class NewClass {
    public static void main(String[] args) throws IOException{
        /**
         * Memory usage variables
         */
        DecimalFormat df = new DecimalFormat("###,##0.00000");
        double freeMemoryBegin = Runtime.getRuntime().freeMemory();
        double freeMemoryEnd, programMemory;

        /**
         * RefSeq
         */
        // new refseq class: filename, use BitSet (else StringBuffer), single sequence search
        RefSeq r = new RefSeq("human.rna.fna", false, false);
        r.findMatches("GACCCCAATGC");

        /**
         * Trie using RefSeq
         */
        /*Trie tr = new Trie();
        RefSeq r = new RefSeq("human.rna.fna", false, true);
        tr.generateTree(r.getRefSeqFromStringBuffer());*/
        //tr.search(demoSearchString(2, 2), false);

        /**
         * Memory usage
         */
        freeMemoryEnd = Runtime.getRuntime().freeMemory();
        //programMemory = totalMemory - freeMemory - baselineMemory;
        programMemory = freeMemoryEnd - freeMemoryBegin;
        System.out.println("Program memory usage without JVM: " + df.format(programMemory / 1024 / 1024) + "MB");

    }
}
