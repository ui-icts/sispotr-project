package edu.uiowa.icts.safeseed.core;

import java.io.Serializable;
import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * Fragment Class
 * 
 */
public class Fragment implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5829759477197651594L;
	private static final Log log =LogFactory.getLog(Fragment.class);

	public static boolean DEBUG=false;

	private Xmers xmers;
	private StringBuffer startSequence = new StringBuffer();
	private String newTargetSequence = "";
	private String newTargetSequenceOrig = "";
	private String rcSequence = "";
	private String rcSequenceOrig = "";
	private String siRNASeq = "";
	private String passengerStrand = "";
	private String change = "";
	private char guideP1, passengerP19, passengerP18, passengerP17;
	private int startPosition = 0;
	private boolean validForSearch = false;



	// return variables
	private int gcContent, threePlusFour;
	private double gcContentPercentage, pots_double, pots_double_orig;
	private char two, three, four, nineteen, twenty;
	private String pots_nt;
	private boolean gcContent_bool, threePlusFour_bool, nineteen_bool, twenty_bool, pol3_bool, trxStart_bool;
	boolean guide_no_c_at_two_bool=true;



	public Fragment(Xmers xmer, int startPosition) {
		this.xmers = xmer;
		this.startPosition = startPosition;
	}

	public void addToSequence(char c) {

		this.startSequence.append(c);
		if (c == 'C' || c == 'G') {
			this.gcContent++;
		}
	}
	
	public int getLength(){
		return startSequence.length();
	}

	public void setAllPermanentVariables() {
		// percentage, converted to % during output
		this.gcContentPercentage = (double) this.gcContent / (double) xmers.getLength();

		// characters
		this.three = this.startSequence.charAt(2);
		this.four = this.startSequence.charAt(3);
		this.nineteen = this.startSequence.charAt(18);
		this.twenty = this.startSequence.charAt(19);


		// counts
		if (this.three == 'C' || this.three == 'G') {
			this.threePlusFour++;
		}
		if (this.three == 'G') {
			// this.threePlusFour++;  commentong out, i don't think this belongs here as it has been handled by the previous if clause
			// +1 transcription start
			this.trxStart_bool = true;
		}

		if (this.four == 'C' || this.four == 'G') {
			this.threePlusFour++;
		}

		// substring, e.g., l=21mer sl=6mer, 2-8=13-20(reverse compliment match)
		this.pots_nt = this.startSequence.substring(xmers.getLength() - xmers.getSearchLength() - 2, xmers.getLength() - 1);
		//   log.debug("xmers.getLength()"+xmers.getLength());
		//   log.debug("xmers.getSearchLength()"+xmers.getSearchLength());
		//   log.debug("pots_nt:"+pots_nt);
		//logger.debug("nt: " + this.pots_nt + " - " + sevenMerBestSeq.get(this.pots_nt));
	}



	/**
	 * Variables that can be altered based on augmented
	 * gcRequired, gAt3, gAt2, two_seven_nt_hex_curoff, three_eight_nt_hex_cutoff, gcMin, gcMax
	 */
	public void setAllVolatileVariables() {
		// pots and new target sequence
		if(xmers.isEnsureBestPots() && false){




			pots_double = xmers.getSeqRef().get7MerBestPots(pots_nt);

			pots_double_orig = xmers.getSeqRef().get7MerPots(pots_nt);
			// replace the 7mer with the best pots version
			newTargetSequence = startSequence.substring(0, xmers.getLength() - xmers.getSearchLength() - 2) + 
					xmers.getSeqRef().get7MerBestSeq(pots_nt) + startSequence.charAt(xmers.getLength() - 1);
			newTargetSequenceOrig = startSequence.toString();
		//	log.debug("  old: " + this.startSequence + ", new: " + this.newTargetSequence);
		} else {
			//	log.debug("frag species:"+xmers.getSeqRef().getSpecies());
			//	log.debug("pots_nt:"+pots_nt);
			//	xmers.getSeqRef().logStatus();
			//	log.debug("??"+xmers.getSeqRef().get7MerPots(pots_nt));
			pots_double = xmers.getSeqRef().get7MerPots(pots_nt);
			pots_double_orig = pots_double;
			newTargetSequence = startSequence.toString();
			newTargetSequenceOrig = newTargetSequence;
		}

		//log.debug("newTargetSequence length1:"+newTargetSequence.length()+"'"+newTargetSequence+"'");
		this.rcSequence = xmers.getSeqUtils().reverseComplement(this.newTargetSequence);
		//log.debug("rcSequence length1:"+rcSequence.length());
		this.rcSequenceOrig = xmers.getSeqUtils().reverseComplement(this.newTargetSequenceOrig);
		this.setSIRNAData();
		this.threePlusFour_bool = (this.threePlusFour >= xmers.getGcRequired()) ? true : false;
		this.nineteen_bool = (xmers.isgAt3() || this.nineteen != 'G') ? true : false;
		this.twenty_bool = (xmers.isgAt2() || this.twenty != 'G') ? true : false;
		this.trxStart_bool = (xmers.isTrxStart()) ? trxStart_bool : true;	// if we don't care, then the bool is true



		/**
		 * check if designed for pol 3 is required, is so calculate
		 * 
		 * TODO
		 * this could be done faster by skipping subsequence sequences since the length is fixed.
		 * skip the next (searchlength-4) sequences when this is found at the end of a sequence
		 * 
		 */

		if(xmers.isPol3()==false)
		{
			pol3_bool=true;
		}
		else
		{
			pol3_bool=true;
			HashMap<Character,Integer> map = xmers.getSeqUtils().calculateConsecutiveMatches(this.newTargetSequence);
			if(map.get('U')!=null && map.get('U')>=4)  	{
				pol3_bool=false;
			}
			if(map.get('T')!=null && map.get('T')>=4)  	{
				pol3_bool=false;
			}

			map = xmers.getSeqUtils().calculateConsecutiveMatches(this.rcSequence);

			if(map.get('U')!=null && map.get('U')>=4)  	{
				pol3_bool=false;
			}
			if(map.get('T')!=null && map.get('T')>=4) 	{
				pol3_bool=false;
			}

			//        	if(pol3_bool==false)	{
			//        		log.debug("POL3 TEST FAILED:"+this.newTargetSequence + "---"+this.rcSequence);
			//        		
			//        	}

		}
		//  log.debug("gcContentPercentage:"+gcContentPercentage);

		if (this.gcContentPercentage >= xmers.getGcMin() && this.gcContentPercentage <= xmers.getGcMax()) {
			this.gcContent_bool = true;
		} else {
			this.gcContent_bool = false;
		}


		/**
		 * if all boolean variables are true than set as valid for search, otherwise, not
		 */
		if(
				threePlusFour_bool 
				&& nineteen_bool 
				&& twenty_bool 
				&& gcContent_bool 
				&& pol3_bool 
				&& trxStart_bool
				&& guide_no_c_at_two_bool){
			
			this.validForSearch = true;
			
		} else {
			if(DEBUG){
				if(!trxStart_bool){
					log.debug("trxStart_bool:"+ trxStart_bool);
				}
				if(!pol3_bool){
					log.debug("pol3_bool:"+pol3_bool);
				}
				if(!gcContent_bool){
					log.debug("gcContent_bool:"+this.gcContent_bool);
				}
				if(!threePlusFour_bool){
					log.debug("threePlusFour_bool:"+this.threePlusFour_bool);
				}
				if(!nineteen_bool){
					log.debug("nineteen_bool:"+this.nineteen_bool);
				}

				if(!twenty_bool){
					log.debug("twenty_bool:"+this.twenty_bool);
				}

				if(!twenty_bool){
					log.debug("twenty_bool:"+this.twenty_bool);
				}
				if(!guide_no_c_at_two_bool){
					log.debug("has c at :"+this.guide_no_c_at_two_bool);
				}
			}


			this.validForSearch = false;
		}




	}

	public String varsToString()
	{
		return "threePlusFour_bool = "+threePlusFour_bool 
				+ ", nineteen_bool = "+nineteen_bool
				+ ", twenty_bool = "+twenty_bool
				+ ", gcContent_bool = "+  gcContent_bool
				+ ", pol3_bool = "+ pol3_bool
				+ ", trxStrat_bool = "+ trxStart_bool;
	}


	private void setSIRNAData() {
		// on reverse complement
		guideP1 = (rcSequence.charAt(0) == 'C') ? 'u' : rcSequence.charAt(0);

		// on sequence
		char l = newTargetSequence.charAt(xmers.getLength() - 1);
		if (l == 'C') {
			passengerP19 = 'u';
		} else if (l == 'T') {
			passengerP19 = 'U';
		} else {
			passengerP19 = l;
		}

		l = newTargetSequence.charAt(xmers.getLength() - 2);
		if (l == 'C') {
			passengerP18 = 'u';
		} else if (l == 'T') {
			passengerP18 = 'U';
		} else {
			passengerP18 = l;
		}

		l = newTargetSequence.charAt(xmers.getLength() - 3);
		if (l == 'C') {
			passengerP17 = 'u';
		} else if (l == 'T') {
			passengerP17 = 'U';
		} else {
			passengerP17 = l;
		}

		// on reverse complement
//		log.debug("rcSequence length:"+rcSequence.length());
		siRNASeq = guideP1 + rcSequence.substring(1, xmers.getLength());
		


		// on sequence
		passengerStrand = newTargetSequence.substring(2, xmers.getLength() - 3) + passengerP17 + passengerP18 + passengerP19 ;
	
		if(xmers.isAppendNNtoPassenger()){
			passengerStrand += "NN";
		}


		if(xmers.isReplaceUsWithTsOnAntisense()){
			siRNASeq = siRNASeq.replace("U", "T");
		}

		if(xmers.isReplaceUsWithTsOnPassenger()){
			passengerStrand = passengerStrand.replace("U", "T");
		}
		else{
			passengerStrand = passengerStrand.replace("T", "U");
		}
		
		if(siRNASeq.charAt(1) == 'C' || siRNASeq.charAt(1)=='c'){
		//	log.debug("siRNASeq:"+siRNASeq);
			guide_no_c_at_two_bool=false;
		}
		
	}






	@Override
	public String toString() {
		return "Fragment [startSequence=" + startSequence
				+ ", newTargetSequence=" + newTargetSequence
				+ ", newTargetSequenceOrig=" + newTargetSequenceOrig
				+ ", rcSequence=" + rcSequence + ", rcSequenceOrig="
				+ rcSequenceOrig + ", siRNASeq=" + siRNASeq
				+ ", passengerStrand=" + passengerStrand + ", change="
				+ change + ", guideP1=" + guideP1 + ", passengerP19="
				+ passengerP19 + ", passengerP18=" + passengerP18
				+ ", passengerP17=" + passengerP17 + ", startPosition="
				+ startPosition + ", validForSearch=" + validForSearch
				+ ", gcContent=" + gcContent + ", threePlusFour="
				+ threePlusFour + ", gcContentPercentage="
				+ gcContentPercentage + ", pots_double=" + pots_double
				+ ", pots_double_orig=" + pots_double_orig + ", three="
				+ three + ", four=" + four + ", nineteen=" + nineteen
				+ ", twenty=" + twenty + ", pots_nt=" + pots_nt
				+ ", gcContent_bool=" + gcContent_bool
				+ ", threePlusFour_bool=" + threePlusFour_bool
				+ ", nineteen_bool=" + nineteen_bool + ", twenty_bool="
				+ twenty_bool + ", pol3_bool=" + pol3_bool
				+ ", trxStart_bool" + trxStart_bool + "]";
	}

	public StringBuffer getStartSequence() {
		return startSequence;
	}

	public void setStartSequence(StringBuffer startSequence) {
		this.startSequence = startSequence;
	}

	public String getNewTargetSequence() {
		return newTargetSequence;
	}

	public void setNewTargetSequence(String newTargetSequence) {
		this.newTargetSequence = newTargetSequence;
	}

	public String getNewTargetSequenceOrig() {
		return newTargetSequenceOrig;
	}

	public void setNewTargetSequenceOrig(String newTargetSequenceOrig) {
		this.newTargetSequenceOrig = newTargetSequenceOrig;
	}

	public String getRcSequence() {
		return rcSequence;
	}

	public void setRcSequence(String rcSequence) {
		this.rcSequence = rcSequence;
	}

	public String getRcSequenceOrig() {
		return rcSequenceOrig;
	}

	public void setRcSequenceOrig(String rcSequenceOrig) {
		this.rcSequenceOrig = rcSequenceOrig;
	}

	/**
	 * Output seq
	 * @return
	 */

	public String getSiRNASeq() {
		return siRNASeq;
	}

	public void setSiRNASeq(String siRNASeq) {
		this.siRNASeq = siRNASeq;
	}


	/**
	 * Output seq
	 * @return
	 */
	public String getPassengerStrand() {
		return passengerStrand;
	}

	public void setPassengerStrand(String passengerStrand) {
		this.passengerStrand = passengerStrand;
	}

	public char getGuideP1() {
		return guideP1;
	}

	public void setGuideP1(char guideP1) {
		this.guideP1 = guideP1;
	}

	public int getStartPosition() {
		return startPosition;
	}

	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}

	public boolean isValidForSearch() {
		return validForSearch;
	}

	public void setValidForSearch(boolean validForSearch) {
		this.validForSearch = validForSearch;
	}

	public String getPots_nt() {
		return pots_nt;
	}

	public void setPots_nt(String pots_nt) {
		this.pots_nt = pots_nt;
	}

	public double getPots_double() {
		return pots_double;
	}

	public void setPots_double(double pots_double) {
		this.pots_double = pots_double;
	}

	public double getPots_double_orig() {
		return pots_double_orig;
	}

	public void setPots_double_orig(double pots_double_orig) {
		this.pots_double_orig = pots_double_orig;
	}

	public Xmers getXmer() {
		return xmers;
	}

	public void setXmer(Xmers xmer) {
		this.xmers = xmer;
	}

	public String getChange() {
		return change;
	}

	public void setChange(String change) {
		this.change = change;
	}

	public char getPassengerP19() {
		return passengerP19;
	}

	public void setPassengerP19(char passengerP19) {
		this.passengerP19 = passengerP19;
	}

	public char getPassengerP18() {
		return passengerP18;
	}

	public void setPassengerP18(char passengerP18) {
		this.passengerP18 = passengerP18;
	}

	public char getPassengerP17() {
		return passengerP17;
	}

	public void setPassengerP17(char passengerP17) {
		this.passengerP17 = passengerP17;
	}

	public int getGcContent() {
		return gcContent;
	}

	public void setGcContent(int gcContent) {
		this.gcContent = gcContent;
	}

	public int getThreePlusFour() {
		return threePlusFour;
	}

	public void setThreePlusFour(int threePlusFour) {
		this.threePlusFour = threePlusFour;
	}

	public double getGcContentPercentage() {
		return gcContentPercentage;
	}

	public void setGcContentPercentage(double gcContentPercentage) {
		this.gcContentPercentage = gcContentPercentage;
	}

	public char getThree() {
		return three;
	}

	public void setThree(char three) {
		this.three = three;
	}

	public char getFour() {
		return four;
	}

	public void setFour(char four) {
		this.four = four;
	}

	public char getNineteen() {
		return nineteen;
	}

	public void setNineteen(char nineteen) {
		this.nineteen = nineteen;
	}

	public char getTwenty() {
		return twenty;
	}

	public void setTwenty(char twenty) {
		this.twenty = twenty;
	}

	public boolean isGcContent_bool() {
		return gcContent_bool;
	}

	public void setGcContent_bool(boolean gcContent_bool) {
		this.gcContent_bool = gcContent_bool;
	}

	public boolean isThreePlusFour_bool() {
		return threePlusFour_bool;
	}

	public void setThreePlusFour_bool(boolean threePlusFour_bool) {
		this.threePlusFour_bool = threePlusFour_bool;
	}

	public boolean isNineteen_bool() {
		return nineteen_bool;
	}

	public void setNineteen_bool(boolean nineteen_bool) {
		this.nineteen_bool = nineteen_bool;
	}

	public boolean isTwenty_bool() {
		return twenty_bool;
	}

	public void setTwenty_bool(boolean twenty_bool) {
		this.twenty_bool = twenty_bool;
	}

	public boolean isPol3_bool() {
		return pol3_bool;
	}

	public void setPol3_bool(boolean pol3_bool) {
		this.pol3_bool = pol3_bool;
	}




}
