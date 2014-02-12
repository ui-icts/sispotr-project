/**
 * Institute for Clinical and Translational Science
 * University of Iowa
 * www.icts.uiowa.edu
 *
 */
package edu.uiowa.icts.safeseed.core;

import org.apache.commons.lang.StringUtils;

import edu.uiowa.icts.safeseed.core.CartItem;

/**
 * 
 * @author Brandyn Kusenda
 * @since Apr 20, 2012
 */
public class SHRNABuilder {
	
	private	SeqUtils utils = new SeqUtils();
	
	public SHRNA createSRNAFromItem(String start_seq, String loop_seq, String end_seq, CartItem item){
		
		
		
		String guideSeq = item.getAttr("guide");
		
		String passengerSeq =  item.getAttr("passenger");

	
		guideSeq = StringUtils.replace(guideSeq, "u", "f").toUpperCase();
		guideSeq = StringUtils.replace(guideSeq, "F", "t");
		guideSeq = StringUtils.replace(guideSeq, "U", "T");

		passengerSeq = StringUtils.replace(passengerSeq, "u", "f").toUpperCase();
		passengerSeq = StringUtils.replace(passengerSeq, "F", "t");
		passengerSeq = StringUtils.replace(passengerSeq, "U", "T");
		passengerSeq = StringUtils.replace(passengerSeq, "NN", "");
	
		
	//	if(passengerSeq.substring(passengerSeq.length()-2,passengerSeq.length()).equals("NN")){
		if(passengerSeq.length() >= 21){
			passengerSeq = passengerSeq.substring(0,passengerSeq.length()-2);
			
		}
		
		if(guideSeq.length() >= 21){
			guideSeq = guideSeq.substring(0,guideSeq.length()-2);
			
		}
		
		String fwdPrimer1 = start_seq + passengerSeq + loop_seq.substring(0, 14);
		String fwdPrimer2 = loop_seq.substring(14, loop_seq.length()) + guideSeq + end_seq;
		
			
		
		String reversePrimer1= "AAAA"
		+utils.reverseDNAComplement(end_seq)
		+utils.reverseDNAComplement(guideSeq)
		+utils.reverseDNAComplement(passengerSeq+ loop_seq).substring(0, 21);
		
		
		String reversePrimer2= ""
		+utils.reverseDNAComplement(passengerSeq).substring(2, (passengerSeq).length())
		+utils.reverseDNAComplement(start_seq);
		
		SHRNA resultSHRNA = new SHRNA();
		resultSHRNA.setFwdPrimer1(fwdPrimer1);
		resultSHRNA.setFwdPrimer2(fwdPrimer2);
		resultSHRNA.setReversePrimer1(reversePrimer1);
		resultSHRNA.setReversePrimer2(reversePrimer2);
		resultSHRNA.setItem(item);
		resultSHRNA.setEnd_seq(end_seq);
		resultSHRNA.setLoop_seq(loop_seq);
		resultSHRNA.setStart_seq(start_seq);
		return resultSHRNA;
		
	}

}
