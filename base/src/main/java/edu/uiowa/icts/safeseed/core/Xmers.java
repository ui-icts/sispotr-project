/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uiowa.icts.safeseed.core;



import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Ray Hylock, updated by Brandyn Kusenda
 * 
 */
public class Xmers implements Serializable{

	
	private static final long serialVersionUID = 2653683517359520239L;

    private static final Log log = LogFactory.getLog(Xmers.class);

	public static boolean DEBUG=false;


    
    private SeqRef seqRef;
    
    private Seq seq;
    
    private SeqUtils seqUtils;
    
    private List<String> columns;
    private List<List<String>> data;
    private List<String> columnsOff;
    private List<List<String>> dataOff;
    private int length = 0;
    
    /**
     * common prarams
     */
    private int searchLength = 0;
    private double gcMin = 0.0d;
    private double gcMax = 0.0d;
    private int gcRequired = 0;
    private boolean gAt2 = false;
    private boolean gAt3 = false;
    private boolean pol3 = false;
    private boolean trxStart = false;
    private boolean ensureBestPots = false;
    private int numberOfReturnElements = 0;
	private boolean replaceUsWithTsOnAntisense = false;
	private boolean replaceUsWithTsOnPassenger = false;
	private boolean appendNNtoPassenger = true;
    
    
    /**
     * Moves to here after of proper length in current fragments
     */
    private List<Fragment> allFragments = new ArrayList<Fragment>();
    
    /**
     * fragments that meet criteria
     */
    private List<Fragment> passedFragments = new ArrayList<Fragment>();
    
    /**
     * Working storage for partial fragments before moved to allfragments
     */
    private List<Fragment> currentFragments = new ArrayList<Fragment>();

    /**
     * Currently not in use
     * TODO: check if need
     */
    private List<Fragment> tempFragments = new ArrayList<Fragment>();
    private List<Fragment> oneOffFragments = new ArrayList<Fragment>();
    private RankFragments rankFragments = new RankFragments();

	private XmerType xmerType;



    public Xmers(Seq seq, int length, XmersParams params)
    {

    	this(	seq,
    			length,
    			params.getSearchLength(),
    			params.getGcMin(),
    			params.getGcMax(),
    			params.getGcRequired(),
    			params.isgAt2(),
    			params.isgAt3(),
    			params.isEnsureBestPots(),
    			params.isPol3(),
    			params.getNumberOfReturnElements(),
    			params.isTrxStart(),
    			params.isReplaceUsWithTsOnAntisense(),
    			params.isReplaceUsWithTsOnPassenger(),
    			params.isAppendNNtoPassenger(),    	
    			params.getXmerType()
    			);
    }
    
    public Xmers(
    		Seq seq,
    		int length, 
    		int searchLength, 
    		double gcMin, 
    		double gcMax,
            int gcRequired, 
            boolean gAt2, 
            boolean gAt3, 
            boolean ensureBestPots,
            boolean pol3, 
            int numberOfReturnElements,
            boolean trxStart,
            boolean replaceUsWithTsOnAntisense,
            boolean replaceUsWithTsOnPassenger,
            boolean appendNNtoPassenger,
            XmerType xmerType
            ) 
    {
       

    	this.seq = seq;
    	this.seqRef=SeqRefFactory.getSeqRefBySpecies(seq.getSpecies());
    	seqUtils = new SeqUtils();
    	
        this.length = length;
        this.searchLength = searchLength-1;
        this.gcMin = gcMin;
        this.gcMax = gcMax;
        this.gcRequired = gcRequired;
        this.gAt2 = gAt2;
        this.gAt3 = gAt3;
        this.ensureBestPots = ensureBestPots;
        this.numberOfReturnElements = numberOfReturnElements;
        this.pol3=pol3;
        this.trxStart=trxStart;
        this.xmerType = xmerType;
        this.replaceUsWithTsOnAntisense =replaceUsWithTsOnAntisense;
        this.replaceUsWithTsOnPassenger =replaceUsWithTsOnPassenger;
        this.appendNNtoPassenger = appendNNtoPassenger;
         
        /**
         * Process sequence and convert into fragments
         */
        fragment(); 
       
        
    }
    

	 /**
     * Generates the Fragments from the input sequence
     */
    private void fragment() {
        allFragments.clear();
        passedFragments.clear();
        currentFragments.clear();
        
     //   log.debug("seq length:"+seq.length());
     //   log.debug("length:"+length);
        
        /**
         * Create all fragments
         */
        for (int i = 0; i < seq.length(); i++) {
        	
            char c = seq.charAt(i);
            //log.debug("checking:'"+c+"'");

            /*
             * create new fragment for each possible sequence of length this.length
             */
            if (i <= seq.length() - length) {
                currentFragments.add(new Fragment(this,i));
                
                //log.debug("   adding frag");
            }

            /*
             * look at each fragment in list so far. if fragment is complete, add to allFragments list. else append new char
             */
            for (int j = 0; j < currentFragments.size(); j++) {
                Fragment f = currentFragments.get(j);
             //   log.debug("   adding to all");
              //  log.debug("   fragInfo:"+f.toString());
                if (f.getStartSequence().length() == length) {
                    allFragments.add(f);
                    currentFragments.remove(f);
                    j--;
                } else {
                    f.addToSequence(c);
                    
                    /**
                     * if its the last char and the fragment is of the correct length
                     * used when the sequence length mod length = 0  
                     */
                    if(i==seq.length()-1 && f.getStartSequence().length() == length ){
                    	 allFragments.add(f);
                         currentFragments.remove(f);                      
                    }
                    
                }
            }
        }

        
        /**
         * Do post checking on fragments
         */
        for (int i = 0; i < allFragments.size(); i++) {
            Fragment f = allFragments.get(i);
         //   log.debug("processing fragment:"+i);

          //  log.debug("Length:"+f.getLength());
            f.setAllPermanentVariables();
            f.setAllVolatileVariables();
            if(f.isValidForSearch()){
            	passedFragments.add(f);
            }
        }
        
        /**
         * Finaly sort
         */
        Collections.sort(passedFragments, rankFragments);
        /*this.tempFragments.clear();
        this.tempFragments.putAll(this.passedFragments);
        this.passedFragments.clear();
        this.passedFragments.putAll(this.rankPassedAverages(this.tempFragments));*/
    }

	
	/**
	 * Update changeable params
	 * @param params
	 */
	private void updateParams(XmersParams params)
	{
		
		searchLength = params.getSearchLength()-1;
		gcMin = params.getGcMin();
		gcMax = params.getGcMax();
		gcRequired = params.getGcRequired();
		gAt2 = params.isgAt2();
		gAt3 = params.isgAt3();
		ensureBestPots = params.isEnsureBestPots();
		pol3 = params.isPol3();
		numberOfReturnElements = params.getNumberOfReturnElements();
		log.debug("Updated params");
		//trxStart = params.isTrxStart();
		//xmerType = params.getXmerType();
		
	}
			
	/**
	 * Update changeable params
	 * 
	 * @param gcMin
	 * @param gcMax
	 * @param gcRequired
	 * @param gAt2
	 * @param gAt3
	 * @param ensureBestPots
	 * @param pol3
	 * @param numberOfReturnElements
	 */
	public void updateParams(
    		double gcMin, 
    		double gcMax,
            int gcRequired, 
            boolean gAt2, 
            boolean gAt3, 
            boolean ensureBestPots,
            boolean pol3, 
            int numberOfReturnElements
            )
    {
    	log.debug("gcmin: " + gcMin + ", gcmax: " + gcMax + ", gcRequired: " + gcRequired + ", gat2: " + gAt2 + ", gat3: " + gAt3 + "," +
				" ensureBestPots: " + ensureBestPots + ", return: " + numberOfReturnElements + ", pol3:"+pol3 + ", trxStart:"+false);
    	this.gcMin = gcMin;
    	this.gcMax = gcMax;
    	this.gcRequired = gcRequired;
    	this.gAt2 = gAt2;
    	this.gAt3 = gAt2;
    	this.ensureBestPots = ensureBestPots;
    	this.pol3 = pol3;
    	this.numberOfReturnElements = numberOfReturnElements;
    	    	
    
    }
	
//	// +1 transaction start support for shRNA
//	public void updateParams(
//    		double gcMin, 
//    		double gcMax,
//            int gcRequired, 
//            boolean gAt2, 
//            boolean gAt3, 
//            boolean ensureBestPots,
//            boolean pol3, 
//            int numberOfReturnElements,
//            boolean trxStart)
//    {
//    	log.debug("gcmin: " + gcMin + ", gcmax: " + gcMax + ", gcRequired: " + gcRequired + ", gat2: " + gAt2 + ", gat3: " + gAt3 + "," +
//				" ensureBestPots: " + ensureBestPots + ", return: " + numberOfReturnElements + ", pol3:"+pol3 + ", trxStart:"+trxStart);
//    	this.gcMin = gcMin;
//    	this.gcMax = gcMax;
//    	this.gcRequired = gcRequired;
//    	this.gAt2 = gAt2;
//    	this.gAt3 = gAt2;
//    	this.ensureBestPots = ensureBestPots;
//    	this.pol3 = pol3;
//    //	this.numberOfReturnElements = numberOfReturnElements;
//    //	this.trxStart=trxStart;
//  
//    	
//    	
//    
//    }

	public void updateFragments(XmersParams params)
	{
		updateParams(params);
		updateFragments();
	}
	
	
	public void updateFragments()
    {
		currentFragments.clear();
	  	passedFragments.clear();
    	log.debug("gcmin: " + gcMin + ", gcmax: " + gcMax + ", gcRequired: " + gcRequired + ", gat2: " + gAt2 + ", gat3: " + gAt3 + "," +
				" ensureBestPots: " + ensureBestPots + ", return: " + numberOfReturnElements + ", pol3:"+pol3+ ", trxStart:"+trxStart);

    	
    	log.debug("Total number of fragments:"+allFragments.size());
    	for (int i = 0; i < allFragments.size(); i++) {
            Fragment f = this.allFragments.get(i);
            
            f.setAllVolatileVariables();
            if(f.isValidForSearch()){
            	this.passedFragments.add(f);
            }
        }
    	log.debug("...number of Passed fragments:"+passedFragments.size());
    	Collections.sort(this.passedFragments, rankFragments);
        /*this.tempFragments.clear();
        this.tempFragments.putAll(this.passedFragments);
        this.passedFragments.clear();
        this.passedFragments.putAll(this.rankPassedAverages(this.tempFragments));*/
    }
	
   
    public void computeOneOff(){
        List<Integer> positions = new ArrayList<Integer>();
        int r = length - searchLength - 2;
        positions.add(1);
        positions.add(2);
        for(int i=r; i<length - 1; i++){
            positions.add(i);
        }
        
        for(int i=numberOfReturnElements-1; i<allFragments.size(); i++){
            computeOneOffValues(this.allFragments.get(i), positions);
        }
    }
    /**
     *
     * @param f
     * gcRequired, gAt3, gAt2, two_seven_nt_hex_curoff, three_eight_nt_hex_cutoff, gcMin, gcMax
     */
    private void computeOneOffValues(Fragment f, List<Integer> positions) {
        Fragment t = new Fragment(this,0);
        // for every position in the sequence
        for (int z=0; z<positions.size(); z++){
            int pos = positions.get(z);
            char c = f.getNewTargetSequence().charAt(pos);
            // for all four nucleotides
            for (int k = 0; k < 4; k++) {
                char p = 0;
                if (k == 0) {
                    p = 'A';
                } else if (k == 1) {
                    p = 'C';
                } else if (k == 2) {
                    p = 'G';
                } else if (k == 3) {
                    p = 'T';
                }
                if (c != p) {
                    for (int j = 0; j < length; j++) {
                        if (j == pos) {
                            t.addToSequence(p);
                        } else {
                            t.addToSequence(c);
                        }
                    }
                    t.setAllPermanentVariables();
                    t.setAllVolatileVariables();
                    t.setStartPosition(f.getStartPosition());          // duplicate start position
                    t.setChange("" + c + pos + p);                // from @pos to
                    this.oneOffFragments.add(t);     			// add the fragment to one off
                    t = new Fragment(this,0);                        // create a new temporary fragment
                }
            }
        }
        /*this.tempFragments.clear();
        this.tempFragments.putAll(this.oneOffFragments);
        this.oneOffFragments.clear();
        this.oneOffFragments.putAll(this.rankPassedAverages(this.tempFragments));*/
    }
    
    /**
     * Sort a List of fragments by their POTS values
     * @param pf
     * @return
     */
    class RankFragments implements Comparator<Fragment> {
        public int compare(Fragment f1, Fragment f2) {
        	// 1 compare 2 = ascending order
        	Double d1 = f1.getPots_double();
        	Double d2 = f2.getPots_double();
        	return d1.compareTo(d2);
        }
    }

    /**
     * Sort a LinkedHashMap
     * @param pf
     * @return
     */
    private LinkedHashMap<Fragment, Double> rankPassedAverages(LinkedHashMap pf) {
        List list = new LinkedList(pf.entrySet());
        Collections.sort(list, new Comparator() {

            public int compare(Object o1, Object o2) {
                return ((Comparable) ((Map.Entry) (o1)).getValue()).compareTo(((Map.Entry) (o2)).getValue());
            }
        });

        LinkedHashMap result = new LinkedHashMap();
        for (Iterator it = list.iterator(); it.hasNext();) {
            Map.Entry entry = (Map.Entry) it.next();
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }

   
    /**
     * Creates the lists necessary for JSP table creation
     */
    public void createPassedTableOutput() {
        DecimalFormat df = new DecimalFormat("#,###.##%");
        // header
    	if(DEBUG)
        log.debug("Creating passed table output");
        // data
        this.data = new ArrayList<List<String>>();
        int max = Math.min(passedFragments.size(), numberOfReturnElements);
    	if(DEBUG)
        log.debug("   max:"+max);
        for(int i=0; i<max; i++){
            List<String> d = new ArrayList<String>();
            Fragment f = this.passedFragments.get(i);
            d.add(String.valueOf(i+1));
         //   d.add((f.startPosition + 1) + "-" + (f.startPosition + this.length)); // 0-index to 1-index
            d.add(""+(f.getStartPosition() + 1) ); // 0-index to 1-index
            //d.add(String.valueOf(f.startPosition + 1));

            //d.add(f.newTargetSequence);
            //d.add(f.rcSequence);
            //d.add(String.valueOf(f.guideP1));
            //d.add(String.valueOf(f.passengerP19));
            //d.add(String.valueOf(f.passengerP18));
            //d.add(String.valueOf(f.passengerP17));

            String t = null;
            
            /*
             * add percentage
             */
  			String percent = (seqRef.get7MerPotsPercentiles(f.getPots_nt()) != null) ? 
  					df.format(seqRef.get7MerPotsPercentiles(f.getPots_nt())) : "NA";
  					
  					
  		 
  		    
            /*
             * pots score
             */
            if(this.ensureBestPots){
            	// no wobble
            	d.add(String.valueOf((int)f.getPots_double_orig()));
              
            	// don't need to mod to siRNASeq as we ignore the first character
                t = f.getRcSequenceOrig().substring(1, this.searchLength + 2);
                d.add(t);
                
                // wobble
                d.add(String.valueOf((int)f.getPots_double()));
                t = f.getSiRNASeq().substring(1, this.searchLength + 2);
                d.add(t);
            } else {
            	d.add(String.valueOf((int)f.getPots_double()));
                t = f.getSiRNASeq().substring(1, this.searchLength + 2);
            
            
            }

            d.add(percent);
            //siRNA seq
            d.add(f.getSiRNASeq());
            //pass strand
            d.add(f.getPassengerStrand());
            d.add(t);

  			
            /*
             *  miRNA match and conservation
             *  to upper-case as some characters can be lower, but all in the miRNA file are upper
             */
            if (seqRef.get7MerMatch(t.toUpperCase()) != null) 
            {
                d.add(seqRef.get7MerMatch(t.toUpperCase())[0]);
                d.add(seqRef.get7MerMatch(t.toUpperCase())[1]);
            } else 
            {
                d.add("");
                d.add("");
            }

            /*
             * G/C Content
             */
            d.add(String.valueOf(df.format(f.getGcContentPercentage())));


            /**
             * GET SPS VAL
             */
            Double sps = seqRef.getSps(f.getPots_nt()) ;
			if(sps != null){
				DecimalFormat dformat = new DecimalFormat("##.##");
				d.add(dformat.format(sps));
			}
			else{
				d.add("");
			}
			
			this.data.add(d);
			

        }
    }

    public void createOneOffTableOutput() {
        DecimalFormat df = new DecimalFormat("#,###.##%");
        // header
        this.columnsOff = new ArrayList<String>();
        this.columnsOff.add("Rank");
        this.columnsOff.add("Position");
        this.columnsOff.add("Change");
        this.columnsOff.add("G/C Content");
        this.columnsOff.add("Target Site");
        this.columnsOff.add("unmod siRNA sequence 5'-->3'");
        this.columnsOff.add("guide P1");
        this.columnsOff.add("passenger p19");
        this.columnsOff.add("passenger p18");
        this.columnsOff.add("passenger p17");
        this.columnsOff.add("siRNA Sequence (5'-->3')");
        this.columnsOff.add("passenger strand (5'-->3')");
        this.columnsOff.add("POTS");

        // additional
        this.columnsOff.add("7Mer");
        this.columnsOff.add("7mer match");
        this.columnsOff.add("miRNA conservation");

        // data
        this.dataOff = new ArrayList<List<String>>();
        int max = Math.min(this.allFragments.size(), this.numberOfReturnElements);
        for(int i=0; i<max; i++){
            List<String> d = new ArrayList<String>();
            Fragment f = this.allFragments.get(i);
            d.add(String.valueOf(i+1));
            //d.add((f.startPosition + 1) + "-" + (f.startPosition + this.length)); // 0-index to 1-index
            d.add(String.valueOf(f.getStartPosition() + 1));
            d.add(f.getChange());
            d.add(String.valueOf(df.format(f.getGcContentPercentage())));
            d.add(f.getNewTargetSequence());
            d.add(f.getRcSequence());
            d.add(String.valueOf(f.getGuideP1()));
            d.add(String.valueOf(f.getPassengerP19()));
            d.add(String.valueOf(f.getPassengerP18()));
            d.add(String.valueOf(f.getPassengerP17()));
            d.add(f.getSiRNASeq());
            d.add(f.getPassengerStrand());
            d.add(String.valueOf((int)f.getPots_double()));

         // additional
            String t = f.getSiRNASeq().substring(1, this.searchLength + 2);
            d.add(t);
            // to upper-case as some characters can be lower, but all in the miRNA file are upper
            if (seqRef.get7MerMatch(t.toUpperCase()) != null) {
                d.add(seqRef.get7MerMatch(t.toUpperCase())[0]);
                d.add(seqRef.get7MerMatch(t.toUpperCase())[1]);
            } else {
                d.add("");
                d.add("");
            }

            this.dataOff.add(d);
        }
    }
    
    private void print() {
        for (int i = 0; i < this.allFragments.size(); i++) {
            log.debug(this.allFragments.get(i).getStartSequence() + " - " + this.allFragments.get(i).getNewTargetSequence());
        }
    }
    
    private void printFragments() {
        DecimalFormat df = new DecimalFormat("#,###.##%");
        log.debug("Start Position\t" + "Target Site\t\t" + "G/C Content\t" + "3\t" + "4\t" + "19\t" + "20\t" + "3+4\t"
                + "7mer\t" + "POTS\t" + "G/C Content\t" + "3+4\t"
                + "19\t" + "20\t" + "New Target Site");

        for (int i = 0; i < this.allFragments.size(); i++) {
            Fragment f = this.allFragments.get(i);
            log.debug(f.toString());
        }
    }

    public List<Fragment> getAllFragments() {
		return allFragments;
	}

	public void setAllFragments(List<Fragment> allFragments) {
		this.allFragments = allFragments;
	}

	public List<Fragment> getPassedFragments() {
		return passedFragments;
	}

	public void setPassedFragments(List<Fragment> passedFragments) {
		this.passedFragments = passedFragments;
	}
	

    public List<String> getColumns() {
        return this.columns;
    }

    public List<List<String>> getData() {
        return this.data;
    }
    
    public List<String> getColumnsOneOff() {
        return this.columnsOff;
    }

    public List<List<String>> getDataOneOff() {
        return this.dataOff;
    }
    
    

	
	public int getSearchLength() {
		return searchLength;
	}

	public void setSearchLength(int searchLength) {
		this.searchLength = searchLength;
	}

	public double getGcMin() {
		return gcMin;
	}

	public void setGcMin(double gcMin) {
		this.gcMin = gcMin;
	}

	public double getGcMax() {
		return gcMax;
	}

	public void setGcMax(double gcMax) {
		this.gcMax = gcMax;
	}

	public boolean isPol3() {
		return pol3;
	}

	public void setPol3(boolean pol3) {
		this.pol3 = pol3;
	}
	
	public boolean isTrxStart(){
		return this.trxStart;
	}
	
	public void setTrxStart(boolean trxStart){
		this.trxStart = trxStart;
	}

	public int getGcRequired() {
		return gcRequired;
	}

	public void setGcRequired(int gcRequired) {
		this.gcRequired = gcRequired;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public SeqRef getSeqRef() {
		if(seqRef==null)
		{
			seqRef=SeqRefFactory.getSeqRefBySpecies(seq.getSpecies());
		}
		return seqRef;
	}

	public void setSeqRef(SeqRef seqRef) {
		this.seqRef = seqRef;
	}

	public Seq getSeq() {
		return seq;
	}

	public void setSeq(Seq seq) {
		this.seq = seq;
	}

	public SeqUtils getSeqUtils() {
		return seqUtils;
	}

	public void setSeqUtils(SeqUtils seqUtils) {
		this.seqUtils = seqUtils;
	}

	public List<String> getColumnsOff() {
		return columnsOff;
	}

	public void setColumnsOff(List<String> columnsOff) {
		this.columnsOff = columnsOff;
	}

	public List<List<String>> getDataOff() {
		return dataOff;
	}

	public void setDataOff(List<List<String>> dataOff) {
		this.dataOff = dataOff;
	}

	public boolean isgAt2() {
		return gAt2;
	}

	public void setgAt2(boolean gAt2) {
		this.gAt2 = gAt2;
	}

	public boolean isgAt3() {
		return gAt3;
	}

	public void setgAt3(boolean gAt3) {
		this.gAt3 = gAt3;
	}

	public boolean isEnsureBestPots() {
		return ensureBestPots;
	}

	public void setEnsureBestPots(boolean ensureBestPots) {
		this.ensureBestPots = ensureBestPots;
	}

	public int getNumberOfReturnElements() {
		return numberOfReturnElements;
	}

	public void setNumberOfReturnElements(int numberOfReturnElements) {
		this.numberOfReturnElements = numberOfReturnElements;
	}

	public List<Fragment> getCurrentFragments() {
		return currentFragments;
	}

	public void setCurrentFragments(List<Fragment> currentFragments) {
		this.currentFragments = currentFragments;
	}

	public List<Fragment> getTempFragments() {
		return tempFragments;
	}

	public void setTempFragments(List<Fragment> tempFragments) {
		this.tempFragments = tempFragments;
	}

	public List<Fragment> getOneOffFragments() {
		return oneOffFragments;
	}

	public void setOneOffFragments(List<Fragment> oneOffFragments) {
		this.oneOffFragments = oneOffFragments;
	}

	public RankFragments getRankFragments() {
		return rankFragments;
	}

	public void setRankFragments(RankFragments rankFragments) {
		this.rankFragments = rankFragments;
	}

	public void setColumns(List<String> columns) {
		this.columns = columns;
	}

	public void setData(List<List<String>> data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Xmers [  "
				+ " length=" + length + ", searchLength=" + searchLength
				+ ", gcMin=" + gcMin + ", gcMax=" + gcMax + ", gcRequired="
				+ gcRequired + ", gAt2=" + gAt2 + ", gAt3=" + gAt3 + ", pol3="
				+ pol3 + ", ensureBestPots=" + ensureBestPots
				+ ", \n      numberOfReturnElements=" + numberOfReturnElements 
				+ ", allFragments=" + allFragments.size() + ", passedFragments="
				+ passedFragments.size() + ", \n       currentFragments=" + currentFragments.size()
				+ ", tempFragments=" + tempFragments.size() + ", oneOffFragments="
				+ oneOffFragments.size() + " ]";
	}

	public XmerType getXmerType() {
		return xmerType;
	}

	public void setXmerType(XmerType xmerType) {
		this.xmerType = xmerType;
	}

	public boolean isReplaceUsWithTsOnAntisense() {
		return replaceUsWithTsOnAntisense;
	}

	public void setReplaceUsWithTsOnAntisense(boolean replaceUsWithTsOnAntisense) {
		this.replaceUsWithTsOnAntisense = replaceUsWithTsOnAntisense;
	}

	public boolean isAppendNNtoPassenger() {
		return appendNNtoPassenger;
	}

	public void setAppendNNtoPassenger(boolean appendNNtoPassenger) {
		this.appendNNtoPassenger = appendNNtoPassenger;
	}

	public boolean isReplaceUsWithTsOnPassenger() {
		return replaceUsWithTsOnPassenger;
	}


}
