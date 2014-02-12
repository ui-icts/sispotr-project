/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uiowa.icts.safeseed;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import edu.uiowa.icts.safeseed.core.Fragment;
import edu.uiowa.icts.safeseed.core.Seq;
import edu.uiowa.icts.safeseed.core.SeqManager;
import edu.uiowa.icts.safeseed.core.SeqRef;
import edu.uiowa.icts.safeseed.core.SeqRefDefaultImpl;
import edu.uiowa.icts.safeseed.core.SeqRefFactory;
import edu.uiowa.icts.safeseed.core.XmerType;
import edu.uiowa.icts.safeseed.core.Xmers;
import edu.uiowa.icts.safeseed.core.XmersParams;
import edu.uiowa.icts.safeseed.domain.SearchResult;
import edu.uiowa.icts.safeseed.domain.SearchStatus;

/**
 * @author Brandyn
 */
public class MainTest2 extends TestCase {


	private static final Log log = LogFactory.getLog(MainTest2.class);

	/**
	 * 
	 * Tests to see if the sequence entered is properly fragmented
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Test
	public void testMain() throws FileNotFoundException, IOException{

		Fragment.DEBUG=true;
		Xmers.DEBUG=true;
		
		System.out.println("Started Test");
		SeqRef humanSeqRef = new SeqRefDefaultImpl("human","pots.txt", "miRNA.txt","seedsps.tab");
		SeqRefFactory.add(humanSeqRef);

		String inputSeqString = "GAGGAAGAGTAGCTCGCCGAGG";
		SeqManager seqManager = new SeqManager();

		XmersParams paramsSHRNA = new XmersParams( 7, 1/100.0, 99/100.0, 2, true, true, true, true, 3, true,XmerType.SHRNA);

		Seq seq = new Seq();
		seq.setLabel("TestSeq1");
		seq.setSeqString(inputSeqString);
		seq.setSpecies("human");
		seq.setSeqId(1l);
		System.out.println("    adding seq");
		
		seqManager.addSeq(seq,22,paramsSHRNA);
		//	seqManager.addSeq(new File("sequence3.txt"),"human",22,paramsSHRNA);
		System.out.println("    getting seq");

		Seq outputseq = seqManager.getSeq("TestSeq1");
		
		assertEquals(inputSeqString,outputseq.getSeqString());
		System.out.println("seq = "+outputseq.getSeqString());

		Xmers x = seq.getXmers(22);
		for(Fragment f: x.getPassedFragments()){
			log.debug("passenger:"+f.getPassengerStrand());
			assertEquals("GAGGAAGAGTAGCTCGCCGAGG", f.getStartSequence().toString());
		}

		seqManager.printSeqLog();

		
	}


}
