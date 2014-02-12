/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uiowa.icts.safeseed.refseq;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 *
 * @author Ray
 */
public class RefSeq {

    private class Sequence {
        // 0=gi, 1=gi-number, 2=from datasource, 3=accession, 4=locus
        private int gi_number = -1;
        private String from_source = "";
        private String accession = "";
        private String locus = "";
        private StringBuffer sequence = null;
        private BitSet bits = null;

        public Sequence() {
            // does nothing
        }
    }

    // logger variables
    private final Logger logger = Logger.getLogger(RefSeq.class);
    private final SimpleLayout layout = new SimpleLayout();
    private final Appender appender = new ConsoleAppender(layout);
    private final Level logLevel = Level.INFO;      // debug, info, warn, error, fatal

    // operational variables
    private boolean bits = true;
    private boolean test = true;
    private LinkedHashMap<String, Sequence> refSeqSequences = new LinkedHashMap<String, Sequence>();
    private LinkedHashMap<String, List<Double>> matches = null;

    // timer variables
    private double readFileStart;
    private double readFileEnd;
    private double readFileTotal;
    private double matchesStart;
    private double matchesEnd;
    private double matchesTotal;

    public RefSeq(String file, boolean bits, boolean test) throws IOException {
        // setup logger
        this.logger.setLevel(logLevel);
        this.logger.addAppender(appender);
        
        this.bits = bits;
        this.test = test;
        this.readFile(file);
    }

    private void readFile(String file) throws FileNotFoundException, IOException {
        // start the timer
        readFileStart = System.currentTimeMillis();
        
        // retrieve sequence data
        BitSet seqBits = null;
        StringBuffer seqBuff = null;
        if (bits) {
            seqBits = new BitSet();
        } else {
            seqBuff = new StringBuffer();
        }
        boolean first = true;
        Sequence seq = null;

        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String s;
        DecimalFormat df = new DecimalFormat("###,###,###,###");
        int nucleotide = 0;             // the current bit position, new line, but continued bits
        double totalNucleotides = 0;    // total number of nucleotides in the data set
        double total = 0;
        while ((s = br.readLine()) != null) {
            // FASTA header
            if (s.charAt(0) == '>') {
                if (first) {
                    first = false;
                } else {
                    if (bits) {
                        // add closure bit, REQUIRED in case last bit is 0 (e.g., A or G)
                        seqBits.set(nucleotide*2);
                        seq.bits = seqBits;
                        seqBits = new BitSet();
                    } else {
                        seq.sequence = seqBuff;
                        seqBuff = new StringBuffer();
                    }
                }
                seq = new Sequence();
                if(bits) nucleotide = 0;
                // 0=>gi, 1=gi-number, 2=from datasource, 3=accession, 4=locus
                String[] t = s.split("\\|");
                this.logger.debug(t[1] + " - " + this.refSeqSequences.size());
                seq.gi_number = Integer.parseInt(t[1].trim());
                seq.from_source = t[2].trim();
                seq.accession = t[3].trim();
                seq.locus = t[4].trim();
                this.refSeqSequences.put(seq.accession, seq);
                
            // sequence data
            } else {
                if (bits) {
                    total += s.length();
                    for (int i = 0; i < s.length(); i++) {
                        if (s.charAt(i) == 'A') {
                            // A = 00 so nothing to set
                        } else if (s.charAt(i) == 'C') {
                            // C = 01
                            int pos = nucleotide * 2;
                            seqBits.set(pos + 1);
                        } else if (s.charAt(i) == 'G') {
                            // G = 10
                            int pos = nucleotide * 2;
                            seqBits.set(pos);
                        } else if (s.charAt(i) == 'T') {
                            int pos = nucleotide * 2;
                            seqBits.set(pos);
                            seqBits.set(pos + 1);
                        }
                        nucleotide++;
                        if(this.logLevel.equals(Level.INFO)) totalNucleotides++;
                    }
                    this.logger.debug("\t\tcount = " + df.format(nucleotide));
                } else {
                    seqBuff.append(s);
                    if(this.logLevel.equals(Level.DEBUG)) nucleotide += s.length();
                    this.logger.debug("\t\tcount = " + df.format(nucleotide));
                    if(this.logLevel.equals(Level.INFO)) totalNucleotides += s.length();
                }
            }
        }
        this.logger.info("Total nucleotides = " + df.format(totalNucleotides));
        if (bits) {
            // add closure bit, REQUIRED in case last bit is 0 (e.g., A or G)
            seqBits.set(nucleotide*2);

            // set bits
            seq.bits = seqBits;
        } else {
            seq.sequence = seqBuff;
        }
        fr.close();
        
        // stop the timer, compute the difference, and print out the time
        readFileEnd = System.currentTimeMillis();
        readFileTotal = readFileEnd - readFileStart;
        this.logger.info("Loading RefSeq took " + readFileTotal + " ms");
    }

    public void findMatches(String seq){
        // start the timer
        matchesStart = System.currentTimeMillis();
        String c = "";
        
        if(bits){
            this.findBitSetMatches(seq);
            c = "BitSet";
        } else {
            this.findStringBufferMatches(seq);
            c = "StringBuffer";
        }

        // stop the timer, compute the difference, and print out the time
        matchesEnd = System.currentTimeMillis();
        matchesTotal = matchesEnd - matchesStart;
        this.logger.info("Matching against RefSeq " + c + " took " + matchesTotal + " ms");
        
        this.printMatches();
    }
    
    private void findStringBufferMatches(String seq){
        Iterator<Sequence> it = this.refSeqSequences.values().iterator();
        matches = new LinkedHashMap<String, List<Double>>();
        boolean c = true;
        while(it.hasNext() && c){
            Sequence temp = it.next();
            for(int i=0; i<temp.sequence.length()-seq.length()+1; i++){
                if(temp.sequence.subSequence(i, i+seq.length()).equals(seq)){
                    List<Double> t = null;
                    if(this.matches.containsKey(temp.accession)){
                        t = this.matches.get(temp.accession);
                    } else {
                        t = new ArrayList<Double>();
                    }
                    t.add(Double.valueOf(i));
                    matches.put(temp.accession, t);
                }
            }
            if(test) c = false;    // if we want to test on one only
        }
    }

    private void findBitSetMatches(String seq){
        // convert seq into a BitSet
        BitSet s = new BitSet(seq.length());
        for (int i = 0; i < seq.length(); i++) {
            if (seq.charAt(i) == 'A') {
                // A = 00 so nothing to set
            } else if (seq.charAt(i) == 'C') {
                // C = 01
                int pos = i * 2;
                s.set(pos + 1);
            } else if (seq.charAt(i) == 'G') {
                // G = 10
                int pos = i * 2;
                s.set(pos);
            } else if (seq.charAt(i) == 'T') {
                int pos = i * 2;
                s.set(pos);
                s.set(pos + 1);
            }
        }
        // add closure bit, REQUIRED in case last bit is 0 (e.g., A or G)
        s.set((seq.length())*2);
        this.logger.info("Search string - " + s.toString());

        Iterator<Sequence> it = this.refSeqSequences.values().iterator();
        matches = new LinkedHashMap<String, List<Double>>();
        boolean c = true;
        while(it.hasNext() && c){
            Sequence temp = it.next();
            // increment by 2 bits
            for(int i=0; i<temp.bits.length()-s.length()+1; i+=2){
                // get bits and add closure bit
                BitSet g = temp.bits.get(i, i+s.length());
                g.set(s.length()-1);
                this.logger.debug("Comparison string - " + g.toString());

                // compare up to and INCLUDING closure bit
                if(this.bitSetCompare(g, s.get(0, s.length()))){
                    List<Double> t = null;
                    if(this.matches.containsKey(temp.accession)){
                        t = this.matches.get(temp.accession);
                    } else {
                        t = new ArrayList<Double>();
                    }
                    t.add(Double.valueOf(i));
                    matches.put(temp.accession, t);
                }
            }
            if(test) c = false;    // if we want to test on one only
        }
    }

    /**
     * The .equal operator in BitSet.java is too complex for our
     * needs and can thus be ignored
     * @param b1
     * @param b2
     * @return
     */
    private boolean bitSetCompare(BitSet b1, BitSet b2){
        for(int i=0; i<b1.length(); i++){
            if(b1.get(i) != b2.get(i)){
                return false;
            }
        }
        return true;
    }

    private void printMatches(){
        Iterator<String> it = this.matches.keySet().iterator();
        while(it.hasNext()){
            String a = it.next();
            List<Double> d = this.matches.get(a);
            System.out.println(a);
            for(int i=0; i<d.size(); i++){
                System.out.println("\t" + d.get(i));
            }
        }
    }

    public LinkedHashMap<String, String> getRefSeqFromStringBuffer(){
        LinkedHashMap<String, String> temp = new LinkedHashMap<String, String>();
        Iterator<String> it = this.refSeqSequences.keySet().iterator();
        while(it.hasNext()){
            String key = it.next();
            Sequence seq = this.refSeqSequences.get(key);
            temp.put(key, seq.sequence.toString());
        }
        return temp;
    }
}
