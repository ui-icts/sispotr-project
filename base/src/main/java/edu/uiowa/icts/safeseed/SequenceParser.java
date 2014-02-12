/**
 * Institute for Clinical and Translation Science (ICTS)
 * University of Iowa
 * 
 * @author Brandyn Kusenda
 * @date Mar 2, 2011
 */
package edu.uiowa.icts.safeseed;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import edu.uiowa.icts.safeseed.domain.Seq;


public class SequenceParser {

    
    private static final Log log =LogFactory.getLog(SequenceParser.class);
    
    /**
     * Reads in sequence input string
     * 
     */
    public LinkedHashMap<String,Seq> getSequenceListFromString(String sequenceText) throws  IOException {
    	
    	LinkedHashMap<String,Seq> sequenceMap = new LinkedHashMap<String,Seq>();
        String s;
        String sh = null, a = null, d = null;
        BufferedReader br = new BufferedReader(new StringReader(sequenceText));
        StringBuffer seq = new StringBuffer();
        boolean first = true;
        int count=0;
        while ((s = br.readLine()) != null) {
        	log.debug("Length of line:"+s.length());
            if (s!=null && s.length() >0 && s.charAt(0) == '>') 
            {
                if (first) {
                    first = false;
                } else {
                	Seq newSeq = new Seq();
                	newSeq.setName(a);
                	newSeq.setDescription(d);
                	newSeq.setSequence(seq.toString());
                	sequenceMap.put(a,newSeq);
                    sh = a = "";
                    seq.setLength(0);	
                    count++;
                    // clear the string buffer
                }
                sh = s;
                // 0=gi, 1=gi-number, 2=from datasource, 3=accession, 4=locus
                d="";
                a="";
                s.replaceFirst(">", "");
                String[] t = s.split("\\|");
                log.debug("header length:"+t.length);
                if(t.length==0)
                {
                	a = "seq:"+(count+1);
                }
                if(t.length==1)
                {
                	a = t[0];
                }
                else if(t.length==3 || t.length==2)
                {
                	a = t[0];
                	d= t[1];
                	
                }
                else if(t.length>3)
                {
                	a = t[3];
                }
               // this.accessions.add(a);
            } else {
                seq.append(s);
            }
        }
        
        
        Seq newSeq = new Seq();
    	newSeq.setName(a);
    	newSeq.setSequence(seq.toString());
    	sequenceMap.put(a,newSeq);
    	
    	return sequenceMap;
     
        
    }
}
