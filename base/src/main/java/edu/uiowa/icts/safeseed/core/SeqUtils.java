package edu.uiowa.icts.safeseed.core;

import java.util.HashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SeqUtils {

	
	private static final Log log = LogFactory.getLog(SeqUtils.class);
    /**
     * Computes the reverse complement from DNA to RNA
     * @param s
     * @return
     */
    public String reverseComplement(String s) {
        StringBuilder r = new StringBuilder();
        char c;
        for (int i = s.length() - 1; i >= 0; i--) {
            c = s.charAt(i);
            if (c == 'A') {
                r.append('U');
            } else if (c == 'C') {
                r.append('G');
            } else if (c == 'G') {
                r.append('C');
            } else if (c == 'T') {
                r.append('A');
            } else if (c == 'N'){
            	r.append('N');
            } else if (c == 'a'){
            	r.append('u');
            } else if (c == 'c'){
            	r.append('g');
            } else if (c == 'g'){
            	r.append('c');
            } else if (c == 't'){
            	r.append('a');
            } else if(c == 'n'){
            	r.append('n');
            }
            else{
            	//r.append(c);
            }
        }
        return r.toString();
    }


 
    /*
     * Used for designing for Pol III expression
     * counts consecutive characters and keeps track of the max in a row
     * 
     */
    public HashMap<Character,Integer> calculateConsecutiveMatches(String seq)
    {
    	HashMap<Character,Integer> maxConsecutiveMatchesMap = new HashMap<Character,Integer>();
    	char lastChar=' ';
    	int inrow=0;
    	for(int i=0;i<seq.length();i++)
    	{
    		char c = seq.charAt(i);

    		if(c==lastChar && i>0)
    		{
    			inrow++;
    		}
    		else
    		{
    			inrow=1;
    			if(maxConsecutiveMatchesMap.containsKey(c)==false)
    			{
    				maxConsecutiveMatchesMap.put(c,0 );
    			}       				
    		}

    		Integer curmax = maxConsecutiveMatchesMap.get(c);
    		if(inrow>curmax)
    			maxConsecutiveMatchesMap.put(c,inrow);

    		lastChar=c;
    	}
    	return maxConsecutiveMatchesMap;
    }
    
	public String reverseDNAComplement(String s){
		StringBuilder r = new StringBuilder();
		char c;
		for (int j = s.length() - 1; j >= 0; j--) {
			c = s.charAt(j);
			if (c == 'A') {
				r.append('T');
			} else if (c == 'C') {
				r.append('G');
			} else if (c == 'G') {
				r.append('C');
			} else if (c == 'T') {
				r.append('A');
			} else if (c == 'N'){
				r.append('N');
			} else if (c == 'a'){
				r.append('t');
			} else if (c == 'c'){
				r.append('g');
			} else if (c == 'g'){
				r.append('c');
			} else if (c == 't'){
				r.append('a');
			} else if(c == 'n'){
				r.append('n');
			}
		}
		return r.toString();
	}

}
