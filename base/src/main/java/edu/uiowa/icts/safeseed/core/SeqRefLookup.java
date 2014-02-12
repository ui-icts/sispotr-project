package edu.uiowa.icts.safeseed.core;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SeqRefLookup {

	private static final Log log = LogFactory.getLog(SeqRefLookup.class);
	
//	private List<String> evalSIRNACols = new ArrayList<String>();
	private List<List<String>> evalSIRNAData = new ArrayList<List<String>>();
	private SeqUtils seqUtils = new SeqUtils();
	
	public void evaluateSIRNA(String species, List<String> siRNA, boolean antisense) throws IOException{
		

		SeqRef seqRef = SeqRefFactory.getSeqRefBySpecies(species);
		
		DecimalFormat df = new DecimalFormat("#,###.##%");

		
		for(int i=0; i<siRNA.size(); i++){
			String s = siRNA.get(i);
			

			// required to remove blank entries, fix the case, and RNA -> DNA
			s = s.replaceAll(" ", "");
			s = s.toUpperCase().trim();
			String seqRNA = s;
			s = s.replace('U', 'T');

			// add the original strand and its order
			List<String> data = new ArrayList<String>();
			data.add(String.valueOf(i+1));
			data.add(s);

			// convert to guide if necessary
			if(!antisense){	// convert to antisense
				s = seqUtils.reverseDNAComplement(s);
			}

			String xmerRC = seqUtils.reverseDNAComplement(s.substring(1, 8));
			String xmerRNA = seqRNA.substring(1, 8).replace('T', 'U');
			String xmer = s.substring(1, 8).replace('U', 'T');
			log.debug(s + ", " + xmer);
			
            // to upper-case as some characters can be lower, but all in the miRNA file are upper
			data.add(String.valueOf(seqRef.get7MerPots(xmerRC)));
			String percent = (seqRef.get7MerPotsPercentiles(xmerRC) != null) ? 
					df.format(seqRef.get7MerPotsPercentiles(xmerRC)) : "NA";
			data.add(percent);

			/** add other data
			 * Seed Seq column 
			 */
			data.add(xmer);
			
			
			/** 
			 * miRNA seed Column
			 * miRNA conser Column
			 * get miRNA seed match and miRNA conservation
			 * 
			 */
            if (seqRef.get7MerMatch(xmerRNA.toUpperCase()) != null) {
                data.add(seqRef.get7MerMatch(xmerRNA.toUpperCase())[0]);
                data.add(seqRef.get7MerMatch(xmerRNA.toUpperCase())[1]);
            } else {
                data.add("");
                data.add("");
            }
            
            
            /**
             * SPS COLUMN
             * GET SPS VAL
             */
            Double sps = seqRef.getSps(xmer) ;
			if(sps != null){
				DecimalFormat dformat = new DecimalFormat("##.##");
				data.add(dformat.format(sps));
			}
			else{
				data.add("");
			}
			
            
            
            
        	log.debug("xmerRNA2:"+xmerRNA);
			
	

		
			this.evalSIRNAData.add(data);
		}
	}

	public List<List<String>> getEvalSIRNAData() {
		return evalSIRNAData;
	}

	public void setEvalSIRNAData(List<List<String>> evalSIRNAData) {
		this.evalSIRNAData = evalSIRNAData;
	}
	
	
}
