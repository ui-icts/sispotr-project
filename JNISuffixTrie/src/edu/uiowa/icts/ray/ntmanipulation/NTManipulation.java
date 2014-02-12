/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uiowa.icts.ray.ntmanipulation;

import com.carrotsearch.hppc.CharCharOpenHashMap;
import com.carrotsearch.hppc.IntObjectOpenHashMap;
import com.carrotsearch.hppc.IntOpenHashSet;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 *
 * @author ray
 */
public class NTManipulation {
    // logger variables
    private static Logger logger = Logger.getLogger(NTManipulation.class);
    private static SimpleLayout layout = new SimpleLayout();
    private static Appender appender = new ConsoleAppender(layout);
    private static Level logLevel = Level.INFO;
    
    // storage
    private static CharCharOpenHashMap complements = new CharCharOpenHashMap();
    private static IntObjectOpenHashMap<IntOpenHashSet> matches = new IntObjectOpenHashMap<IntOpenHashSet>();
    
    
    public NTManipulation(){
        // setup logger
        NTManipulation.logger.setLevel(logLevel);
        NTManipulation.logger.addAppender(appender);
        
        /**
         * Set complements
         */
        complements.put('A', 'T');
        complements.put('T', 'A');
        complements.put('U', 'A');
        complements.put('C', 'G');
        complements.put('G', 'C');
        complements.put('R', 'Y');
        complements.put('Y', 'R');
        complements.put('K', 'M');
        complements.put('M', 'K');
        complements.put('S', 'S');
        complements.put('W', 'W');
        complements.put('B', 'V');
        complements.put('V', 'B');
        complements.put('D', 'H');
        complements.put('H', 'D');
        complements.put('N', 'N');
        
        /**
         * Set matches
         */
        // A = A
        IntOpenHashSet c = new IntOpenHashSet();
        c.add('A');
        matches.put('A', c);
        
        // T = T or U
        c = new IntOpenHashSet();
        c.add('T');
        c.add('U');
        matches.put('T', c);
        
        // U = U or T
        c = new IntOpenHashSet();
        c.add('U');
        c.add('T');
        matches.put('U', c);
        
        // C = C
        c = new IntOpenHashSet();
        c.add('C');
        matches.put('C', c);
        
        // G = G
        c = new IntOpenHashSet();
        c.add('G');
        matches.put('G', c);
        
        // R = A, G, or R
        c = new IntOpenHashSet();
        c.add('A');
        c.add('G');
        c.add('R');
        matches.put('R', c);
        
        // Y = C, T, or Y
        c = new IntOpenHashSet();
        c.add('C');
        c.add('T');
        c.add('Y');
        matches.put('Y', c);
        
        // K = G, T, or K
        c = new IntOpenHashSet();
        c.add('G');
        c.add('T');
        c.add('K');
        matches.put('K', c);
        
        // M = A, C, or M
        c = new IntOpenHashSet();
        c.add('A');
        c.add('C');
        c.add('M');
        matches.put('M', c);
        
        // S = C, G, or S
        c = new IntOpenHashSet();
        c.add('C');
        c.add('G');
        c.add('S');
        matches.put('S', c);
        
        // W = A, T, or W
        c = new IntOpenHashSet();
        c.add('A');
        c.add('T');
        c.add('W');
        matches.put('W', c);
        
        // B = C, G, T, or B
        c = new IntOpenHashSet();
        c.add('C');
        c.add('G');
        c.add('T');
        c.add('B');
        matches.put('B', c);
        
        // D = A, G, T, or D
        c = new IntOpenHashSet();
        c.add('A');
        c.add('G');
        c.add('T');
        c.add('D');
        matches.put('D', c);
        
        // H = A, C, T, or H
        c = new IntOpenHashSet();
        c.add('A');
        c.add('C');
        c.add('T');
        c.add('H');
        matches.put('H', c);
        
        // V = A, C, G, or V
        c = new IntOpenHashSet();
        c.add('A');
        c.add('C');
        c.add('G');
        c.add('V');
        matches.put('V', c);
        
        // N = A, C, G, T, or N
        c = new IntOpenHashSet();
        c.add('A');
        c.add('C');
        c.add('G');
        c.add('T');
        c.add('N');
        matches.put('N', c);
    }
    
    public String reverseComplement(String seq){
        StringBuilder rc = new StringBuilder();
        for(int i=seq.length()-1; i>=0; i--){
            rc.append(complements.get(seq.charAt(i)));
        }

        return rc.toString();
    }
    

    /**
     * Returns true if they two ints are a match, false otherwise using the
     * standard nucleotide code table as the basis for comparison.
     * Matches are based on symmetry. If matches are defined as symmetrical,
     * then a match in either direction counts as a match. Otherwise, c1 must
     * equal c2.
     * @param c1            character 1 as int
     * @param c2            character 2 as ints
     * @param symmetrical   is this a symmetrical search
     * @return 
     */
    public boolean compareNTIntInt(int c1, int c2, boolean symmetrical){
        boolean match = false;
        //NTManipulation.logger.warn(c2 + ", " + matches.get(c2));
        // compare c1 to c2
        if(matches.get(c1).contains(c2)){
            match = true;

        // compare n2 to n1
        } else if(symmetrical && matches.get(c2).contains(c1)) {
            match = true;
        }
        
        return match;
    }
    public boolean compareNTCharChar(char c1, char c2, boolean symmetrical){
        int n1 = Character.toUpperCase(c1);
        int n2 = Character.toUpperCase(c2);
        return compareNTIntInt(n1, n2, symmetrical);
    }
    public boolean comapreNTCharInt(char c1, int c2, boolean symmetrical){
        int n1 = Character.toUpperCase(c1);
        return compareNTIntInt(n1, c2, symmetrical);
    }
    public boolean compareNTIntChar(int c1, char c2, boolean symmetrical){
        int n2 = Character.toUpperCase(c2);
        return compareNTIntInt(c1, n2, symmetrical);
    }
}
