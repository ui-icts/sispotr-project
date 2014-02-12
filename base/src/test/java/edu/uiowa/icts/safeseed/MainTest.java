/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.uiowa.icts.safeseed;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;

import edu.uiowa.icts.safeseed.core.SeqManager;
import edu.uiowa.icts.safeseed.core.SeqRef;
import edu.uiowa.icts.safeseed.core.SeqRefDefaultImpl;
import edu.uiowa.icts.safeseed.core.SeqRefFactory;
import edu.uiowa.icts.safeseed.core.XmerType;
import edu.uiowa.icts.safeseed.core.XmersParams;

/**
 * @author Ray
 */
public class MainTest extends TestCase {
	
	/**
	 *    <bean id="seqref" class="edu.uiowa.icts.safeseed.core.SeqRefDefaultImpl" init-method="init">
    <property name="species" value="human" />
    <property name="potsFilename" value="pots_human_7.txt" />
    <property name="mirnaFilename" value="mirna_human_7.txt" />
   </bean>
   
   <bean id="seqrefmouse" class="edu.uiowa.icts.safeseed.core.SeqRefDefaultImpl" init-method="init">
    <property name="species" value="mouse" />
    <property name="potsFilename" value="pots_mouse_7.txt" />
    <property name="mirnaFilename" value="mirna_mouse_7.txt" />
   </bean>
   
	 */
	private static final Log log = LogFactory.getLog(MainTest.class);
			
	@Test
    public void testMain() throws FileNotFoundException, IOException{
		
		long starttime= System.currentTimeMillis();
		/*
		 * bootstrap
		 */
		SeqRef humanSeqRef = new SeqRefDefaultImpl("human","pots_human_7.txt", "mirna_human_7.txt","seedsps.tab");
		SeqRefFactory.add(humanSeqRef);
		

		
		
		
		
		SeqManager seqManager = new SeqManager();
		long currenttime=System.currentTimeMillis();
		log.debug("bootstrap:"+(currenttime-starttime)+"ms");
		
				
		/*
		 * load sequence
		 * 
		 */
		// for siRNA's (size 21 last param ALWAYS equals false)
		XmersParams params = new XmersParams( 7, 20/100.0, 70/100.0, 2,  true, true, true, false, 10000);
		
		seqManager.addSeq(new File("sequence.txt"),"human",21,params);
		seqManager.addSeq(new File("sequence2.txt"),"human",21,params);
		long currenttime2=System.currentTimeMillis();
		log.debug("read sequences:"+(currenttime2-currenttime)+"ms");
		
		currenttime=System.currentTimeMillis();
		log.debug("Compute Xmers:"+(currenttime-currenttime2)+"ms");

		
		
		seqManager.printSeqLog();

        assertEquals(true, true);
     
        
        currenttime=System.currentTimeMillis();
		log.debug("bootstrap:"+(currenttime-starttime)+"ms");
		
        
        // for shRNA's (size 22 & +1 transcription start)
        XmersParams paramsSHRNA = new XmersParams( 7, 20/100.0, 70/100.0, 2, true, true, true, true, 10000, true,XmerType.SHRNA);
		
		seqManager.addSeq(new File("sequence.txt"),"human",22,paramsSHRNA);
		seqManager.addSeq(new File("sequence2.txt"),"human",22,paramsSHRNA);
		currenttime2=System.currentTimeMillis();
		log.debug("read sequences:"+(currenttime2-currenttime)+"ms");
		
		currenttime=System.currentTimeMillis();
		log.debug("Compute Xmers:"+(currenttime-currenttime2)+"ms");

		
		
		seqManager.printSeqLog();

        assertEquals(true, true);
     
    }
	
	
	
//	
//	@Test
//	public void testSearchResultOutput()
//	{
//		log.debug("Search Result Output Test");
//		Set<SearchResult> resultSet = new HashSet<SearchResult>();
//		SearchStatus ss = new SearchStatus();
//		ss.setSearchSeq("GGACGACAGACAUUCGCC");
//		SearchResult sr = new SearchResult();
//		sr.setSearchStatus(ss);
//		sr.setSearchSeqRc("CCGCUUACAGACAGCAGG");
//		sr.setResultOffset(1);
//		sr.setResultAccession("AIDXXXX");
//		sr.setResultAccessionOffset(99999);
//		sr.setResultSeq("CGAUUACAGACAGCAGGC");
//		resultSet.add(sr);
//		
//		SearchResultsOutputBuilder builder = new SearchResultsOutputBuilder();
//		List<SearchResultContainer> finalResults = builder.build(resultSet);
//		
//		for(SearchResultContainer r: finalResults)
//		{
//			
//			log.debug("\n"+r.toString());
//			
//		}
//		
//		   assertEquals(true, true);
//		
//	}
}
