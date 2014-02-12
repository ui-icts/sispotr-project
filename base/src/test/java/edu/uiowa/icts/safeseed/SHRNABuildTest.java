/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uiowa.icts.safeseed;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import edu.uiowa.icts.safeseed.core.CartItem;
import edu.uiowa.icts.safeseed.core.SHRNA;
import edu.uiowa.icts.safeseed.core.SHRNABuilder;



/**
 *
 * @author Brandyn
 */
public class SHRNABuildTest extends TestCase {
	
	
	private static final Log log = LogFactory.getLog(SHRNABuildTest.class);
			
	
	@Test
    public void testOne() throws FileNotFoundException, IOException{
		String start_seq ="CCCTTGGAGAAAAGCCTTGTTT";
		String end_seq ="TTTTTTctcgag";
		String loop_seq ="CTGTAAAGCCACAGATGGG";
		
		
		CartItem item = new CartItem();
		item.addAttr("aid", "test12");
		item.addAttr("length", "22");
		item.addAttr("start_pos", "123123");
		item.addAttr("pots", "87");
		item.addAttr("guide","TTCGGCGAGCTACTCTTCC" );
		item.addAttr("passenger","GGAAGAGTAGCTCGCCGAG");
		item.addAttr("seed", "");
		
		SHRNABuilder builder = new SHRNABuilder();
		SHRNA result = builder.createSRNAFromItem(start_seq, loop_seq, end_seq, item);

		log.debug(result.toString());
		
		assertEquals("CCCTTGGAGAAAAGCCTTGTTTGGAAGAGTAGCTCGCCGAGCTGTAAAGCCACAG", result.getFwdPrimer1() );
		assertEquals("ATGGGTTCGGCGAGCTACTCTTCCTTTTTTctcgag", result.getFwdPrimer2() );
		assertEquals("AAAActcgagAAAAAAGGAAGAGTAGCTCGCCGAACCCATCTGTGGCTTTACAGCT", result.getReversePrimer1() );
		assertEquals("CGGCGAGCTACTCTTCCAAACAAGGCTTTTCTCCAAGGG",result.getReversePrimer2());
		
     
    }
	
	@Test	
    public void testNN() throws FileNotFoundException, IOException{
		String start_seq ="CCCTTGGAGAAAAGCCTTGTTT";
		String end_seq ="TTTTTTctcgag";
		String loop_seq ="CTGTAAAGCCACAGATGGG";
		
		
		CartItem item = new CartItem();
		item.addAttr("aid", "test12");
		item.addAttr("length", "22");
		item.addAttr("start_pos", "123123");
		item.addAttr("pots", "87");
		item.addAttr("guide","TTCGGCGAGCTACTCTTCC" );
		item.addAttr("passenger","GGAAGAGTAGCTCGCCGAGNN");
		item.addAttr("seed", "");
		
		SHRNABuilder builder = new SHRNABuilder();
		SHRNA result = builder.createSRNAFromItem(start_seq, loop_seq, end_seq, item);

		log.debug(result.toString());
		
		assertEquals("CCCTTGGAGAAAAGCCTTGTTTGGAAGAGTAGCTCGCCGAGCTGTAAAGCCACAG", result.getFwdPrimer1() );
		assertEquals("ATGGGTTCGGCGAGCTACTCTTCCTTTTTTctcgag", result.getFwdPrimer2() );
		assertEquals("AAAActcgagAAAAAAGGAAGAGTAGCTCGCCGAACCCATCTGTGGCTTTACAGCT", result.getReversePrimer1() );
		assertEquals("CGGCGAGCTACTCTTCCAAACAAGGCTTTTCTCCAAGGG",result.getReversePrimer2());
		
     
    }
	
	
	@Test
    public void testLittle_u() throws FileNotFoundException, IOException{
		String start_seq ="CCCTTGGAGAAAAGCCTTGTTT";
		String end_seq ="TTTTTTctcgag";
		String loop_seq ="CTGTAAAGCCACAGATGGG";
		
		
		CartItem item = new CartItem();
		item.addAttr("aid", "test12");
		item.addAttr("length", "22");
		item.addAttr("start_pos", "123123");
		item.addAttr("pots", "87");
		item.addAttr("guide","uTCGGCGAGCTACTCTTCCTC" );
		item.addAttr("passenger","GGAAGAGUAGCUCGCCGAGNN");
		item.addAttr("seed", "");
		
		SHRNABuilder builder = new SHRNABuilder();
		SHRNA result = builder.createSRNAFromItem(start_seq, loop_seq, end_seq, item);

		log.debug(result.toString());
		
		assertEquals("CCCTTGGAGAAAAGCCTTGTTTGGAAGAGTAGCTCGCCGAGCTGTAAAGCCACAG", result.getFwdPrimer1() );
		assertEquals("ATGGGtTCGGCGAGCTACTCTTCCTTTTTTctcgag", result.getFwdPrimer2() );
		assertEquals("AAAActcgagAAAAAAGGAAGAGTAGCTCGCCGAaCCCATCTGTGGCTTTACAGCT", result.getReversePrimer1() );
		assertEquals("CGGCGAGCTACTCTTCCAAACAAGGCTTTTCTCCAAGGG",result.getReversePrimer2());
		
     
    }
	

}
