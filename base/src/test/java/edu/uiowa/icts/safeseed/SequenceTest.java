package edu.uiowa.icts.safeseed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.Before;
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

public class SequenceTest extends TestCase {

	private static final Log log = LogFactory.getLog(SequenceTest.class);
	
	private static final String SEQUENCE = "GAGGAAGAGTAGCTCGCCGAGG";
	private static final String PASSENGER = "GGAAGAGUAGCUCGCCGAGNN"; // to verify everything works while changing parameters
	
	private static final String DESIGN_TYPE = "shrna";
	private static final String SPECIES = "human";
	
	private static final Integer LENGTH_OF_XMERS = 21;
	private static final Integer GC_REQUIRED = 2;
	private static final Integer NUMBER_OF_RETURN_ELEMENTS = 0;
	
	private static final Double GC_MIN = 20.0;
	private static final Double GC_MAX = 70.0;

	private static final Boolean ENSURE_BEST_POTS = false;
	private static final Boolean GAT2 = true;
	private static final Boolean GAT3 = true;
	private static final Boolean POL3 = true;

	private static final Long SEQUENCE_ID = 1l;
	
	@Before
	public void setup(){
		
	}
	
	@After
	public void teardown(){
		
	}
	
	@Test
	public void testSequenceText(){
		
		log.debug("running testSequenceText");
		
		/*
		 * bootstrap
		 */
		SeqRef humanSeqRef = new SeqRefDefaultImpl("human","pots_human_7.txt", "mirna_human_7.txt","seedsps.tab");
		SeqRefFactory.add(humanSeqRef);
		
		try {

			Seq seq = new Seq();
			seq.setLabel("seq1");
			seq.setSeqString(SEQUENCE);
			seq.setSpecies(SPECIES);
			seq.setSeqId(SEQUENCE_ID);
			
			SeqManager seqManager = addSequence(seq);
			
			Seq outputSeq = seqManager.getSeq("seq1");
			
			assertEquals(SEQUENCE, outputSeq.getSeqString());
			
			Xmers x = seq.getXmers(LENGTH_OF_XMERS);
			for(Fragment f : x.getPassedFragments()){
				log.debug("passenger:"+f.getPassengerStrand());
				assertEquals(PASSENGER, f.getPassengerStrand());
			}
			
		} catch (IOException e) {
			log.error("error adding sequence to sequence manager",e);
			fail("error adding sequence to sequence manager");
		}
		
		log.debug("testSequenceText complete");
	}
	
	@Test
	public void testSequenceParamsFile(){
		log.debug("running testSequenceParamsFile");
		
		/*
		 * bootstrap
		 */
		SeqRef humanSeqRef = new SeqRefDefaultImpl("human","pots_human_7.txt", "mirna_human_7.txt","seedsps.tab");
		SeqRefFactory.add(humanSeqRef);
		
		URL absoluteUrl = getClass().getClassLoader().getResource("seqeunce.txt");
		
		if(absoluteUrl != null){

			String fileName = absoluteUrl.getFile();
			File f = new File(fileName);
			
			if(f != null && f.exists()){
				try {

					SeqManager seqManager = addSequence(f);
					
					Collection<Seq> seqs = seqManager.getSeqs();
					for(Seq seq : seqs){
						Xmers x = seq.getXmers(LENGTH_OF_XMERS);
						for(Fragment frag : x.getPassedFragments()){
							log.debug("passenger:"+frag.getPassengerStrand());
							assertEquals(PASSENGER, frag.getPassengerStrand());
						}
					}
					
				} catch (FileNotFoundException e) {
					log.error("error attmepting to read "+fileName,e);
					fail("error attmepting to read "+fileName);
				} catch (IOException e) {
					log.error("error attmepting to read "+fileName,e);
					fail("error attmepting to read "+fileName);
				}
			}else{
				fail("file is null or does not exist: "+fileName);
			}			
		}else{
			fail("file is null or does not exist: sequence.txt");
		}
		
		
		log.debug("testSequenceParamsFile complete");
	}
	
	
	private SeqManager addSequence(Seq seq) throws IOException {
		SeqManager seqManager = new SeqManager();

		XmersParams params = null;

		if("shrna".equalsIgnoreCase(DESIGN_TYPE)){
			params = new XmersParams(7, GC_MIN/100.0, GC_MAX/100.0, GC_REQUIRED, GAT2, GAT3, ENSURE_BEST_POTS, POL3, NUMBER_OF_RETURN_ELEMENTS,true,XmerType.SHRNA);
		}else{
			params = new XmersParams(7, GC_MIN/100.0, GC_MAX/100.0, GC_REQUIRED, GAT2, GAT3, ENSURE_BEST_POTS, POL3, NUMBER_OF_RETURN_ELEMENTS);	
		}
		
		log.debug(params.toString());
		
		seqManager.addSeq(seq, LENGTH_OF_XMERS, params);
		
		return seqManager;
	}
	
	private SeqManager addSequence(File sequenceFile) throws IOException {
		SeqManager seqManager = new SeqManager();

		XmersParams params = null;

		if("shrna".equalsIgnoreCase(DESIGN_TYPE)){
			params = new XmersParams(7, GC_MIN/100.0, GC_MAX/100.0, GC_REQUIRED, GAT2, GAT3, ENSURE_BEST_POTS, POL3, NUMBER_OF_RETURN_ELEMENTS,true,XmerType.SHRNA);
		}else{
			params = new XmersParams(7, GC_MIN/100.0, GC_MAX/100.0, GC_REQUIRED, GAT2, GAT3, ENSURE_BEST_POTS, POL3, NUMBER_OF_RETURN_ELEMENTS);	
		}
		
		log.debug(params.toString());
		
		seqManager.addSeq(sequenceFile, SPECIES, LENGTH_OF_XMERS, params);
		
		return seqManager;
	}
	
}