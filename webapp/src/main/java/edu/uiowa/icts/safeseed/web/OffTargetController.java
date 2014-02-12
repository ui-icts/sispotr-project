package edu.uiowa.icts.safeseed.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import au.com.bytecode.opencsv.CSVWriter;
import edu.emory.mathcs.backport.java.util.Collections;
import edu.uiowa.icts.safeseed.OffTargetIndexBuilder;
import edu.uiowa.icts.safeseed.OffTargetIndexReader;
import edu.uiowa.icts.safeseed.PotsSummary;
import edu.uiowa.icts.safeseed.SearchResultContainer;
import edu.uiowa.icts.safeseed.SearchResultsOutputBuilder;
import edu.uiowa.icts.safeseed.controller.AbstractSafeseedController;
import edu.uiowa.icts.safeseed.core.SeqUtils;
import edu.uiowa.icts.safeseed.domain.SearchResult;
import edu.uiowa.icts.safeseed.domain.SearchStatus;



@Controller
@RequestMapping("/tools/offtarget/*")
public class OffTargetController extends AbstractSafeseedController
{

	private static final Log log = LogFactory.getLog(OffTargetController.class);

	@RequestMapping(value = "buildIndexes.html", method = RequestMethod.GET)
	public ModelAndView buildIndexes(	
			ModelMap model)
	{
		log.debug("In buildIndexes method");
		
		OffTargetIndexBuilder builder = new OffTargetIndexBuilder();
		builder.buildAllIndexes();
		model.addAttribute("indexList",OffTargetIndexBuilder.getIndexMap().values());

		return new ModelAndView("/tools/offtarget/buildIndexes",model);

	}

	/*
	 * runs offtarget search using lucene index
	 * 
	 */
	@RequestMapping(value = "offTargetSearch.html", method = RequestMethod.GET)
	public ModelAndView offTargetSearch(
			@RequestParam("7mer") String sevenmer,
			@RequestParam("type") String type,
			@RequestParam("species") String species,
		
			HttpServletResponse res,
			ModelMap model)
	{
		log.debug("Target strand:"+sevenmer);
		
		/*
		 * Get search strand
		 */
		SeqUtils seqUtils = new SeqUtils();
		String searchStrand = sevenmer.replace('U','T');;
		if("a".equalsIgnoreCase(type))
		{
		

			searchStrand = seqUtils.reverseDNAComplement(searchStrand);
			log.debug("Using reverse complement strand:"+searchStrand);
		}

		/*
		 * Run Search
		 */
		OffTargetIndexReader offTargetReader = new OffTargetIndexReader();
		String indexName=species;
		
		List<String[]> results= new ArrayList<String[]>();
		String[] columns = new String[]{"geneid","tpots","8mer","7merm8","7merm1a","6mer"};
		try {
			results = offTargetReader.query(indexName,"seed",searchStrand,columns,20000);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		Collections.sort(results, new TPotsComparator());
		Collections.reverse(results);

		/*
		 * Standard output
		 * 
		 */
		model.addAttribute("searchStrand",searchStrand);
		model.addAttribute("sevenmer",sevenmer);
		model.addAttribute("type",type);
		model.addAttribute("results",results);
		model.addAttribute("species",species);

		return new ModelAndView("/tools/offtarget/offTargetSearch",model);

	}
	
	/*
	 * runs offtarget search using lucene index EXPORT
	 * 
	 */
	@RequestMapping(value = "offTargetSearchExport.html", method = RequestMethod.GET)
	public void offTargetSearchExport(
			@RequestParam("7mer") String sevenmer,
			@RequestParam("type") String type,
			@RequestParam("species") String species,
			@RequestParam(value = "format", required=true) String format, 
			HttpServletResponse res,
			ModelMap model)
	{
		log.debug("Target strand:"+sevenmer);
		
		/*
		 * Get search strand
		 */
		SeqUtils seqUtils = new SeqUtils();
		String searchStrand = sevenmer.replace('U','T');;
		if("a".equalsIgnoreCase(type))
		{
		

			searchStrand = seqUtils.reverseDNAComplement(searchStrand);
			log.debug("Using reverse complement strand:"+searchStrand);
		}

	
		/*
		 * Run Search
		 */
		OffTargetIndexReader offtargetReader = new OffTargetIndexReader();
		String indexName=species;
		List<String[]> results= new ArrayList<String[]>();
		String[] columns = new String[]{"geneid","tpots","8mer","7merm8","7merm1a","6mer"};
		try {
			results = offtargetReader.query(indexName,"seed",searchStrand,columns,20000);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Collections.sort(results, new TPotsComparator());
		Collections.reverse(results);
		/*
		 * CSV output
		 * 
		 */
		if("csv".equalsIgnoreCase(format))
		{
			try {
				CSVWriter csvwriter = new CSVWriter(res.getWriter());
				List<String[]> csvresults = new ArrayList<String[]>();
				csvresults.add(columns);
				csvresults.addAll(results);
			
				res.setContentType("text/csv");


				res.setHeader("Pragma", "");
				res.setHeader("Cache-Control", "No-cache"); 
				res.setHeader(
						"Content-Disposition",
						"attachment; filename=\"output.csv\"");
				csvwriter.writeAll(csvresults);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}




	}


	/*
	 * Creates pots summary form lucene index
	 * 
	 * 
	 */
	@RequestMapping(value = "potsSummary.html", method = RequestMethod.GET)
	public ModelAndView potsSummary(
			@RequestParam("7mer") String sevenmer, 
			@RequestParam("pots") float pots,
			@RequestParam("type") String type, 
			@RequestParam("species") String species,
			ModelMap model)
	{
		log.debug("In pots summary..:"+sevenmer);
		
		log.debug("Target strand:"+sevenmer);
		
		/*
		 * Get search strand
		 */
		SeqUtils seqUtils = new SeqUtils();
		String searchStrand = sevenmer.replace('U','T');;
		if("a".equalsIgnoreCase(type))
		{
		

			searchStrand = seqUtils.reverseDNAComplement(searchStrand);
			log.debug("Using reverse complement strand:"+searchStrand);
		}
		
		OffTargetIndexReader offTargetReader = new OffTargetIndexReader();
		String indexName=species;
		List<String[]> results= new ArrayList<String[]>();
		try {
			results = offTargetReader.query(indexName,"seed",searchStrand,new String[]{"8mer","7merm8","7merm1a","6mer"},20000);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PotsSummary potsSum = new PotsSummary(searchStrand,indexName,pots);
		for(String[] result: results)
		{
			potsSum.add(Integer.parseInt(result[0]), Integer.parseInt(result[1]), Integer.parseInt(result[2]), Integer.parseInt(result[3]));
		}



		model.addAttribute("potsSum",potsSum);
		model.addAttribute("sevenmer",sevenmer);

		return new ModelAndView("/tools/offtarget/potsSummary",model);

	}

	@RequestMapping(value = "seqsearch.html", method = RequestMethod.GET)
	public ModelAndView seqsearch(
			@RequestParam("seq") String seq, 
			HttpServletRequest req,
			ModelMap model)
	{
		String searchseq = seq.substring(1, seq.length()-2);


		log.debug("Original Sequence:"+seq);
		log.debug("Search Sequence:"+searchseq);


		SearchStatus ss = safeseedDaoService.getSearchStatusService().findById(searchseq);

		if(ss==null)
		{
			log.debug("adding searchseq to queue");
			ss = new SearchStatus();
			ss.setSearchSeq(searchseq);
			ss.setEntryTime(new Date());
			ss.setStatus(0);
			ss.setReadCount(0);
			safeseedDaoService.getSearchStatusService().save(ss);
		}
		else
		{
			log.debug("searchseq exists with status:");
		}


		model.addAttribute("searchseq_full", seq);

		model.addAttribute("ss",ss);

		return new ModelAndView("/seqsearch",model);

	}

	@RequestMapping(value = "loadSearchResults.html", method = RequestMethod.GET)
	public ModelAndView loadSearchResults(
			@RequestParam("searchSeq") String searchseq, 
			HttpServletRequest req,
			ModelMap model)
	{
		boolean test=true;

		log.debug("in search results method:"+searchseq);
		SearchStatus ss = safeseedDaoService.getSearchStatusService().findById(searchseq);
		if(test)
		{
			log.debug("Testing!!..............");
			ss.setStatus(2);	
			ss.getSearchResults().addAll(safeseedDaoService.getSearchResultService().list());

		}
		if(ss==null)
		{
			log.error("Error: SearchSeq does not exist");
			return new ModelAndView("bodyOnlyTemplate|/error",model);
		}

		if(ss.getStatus()==2 || test)
		{
			//Test
			if(test)
			{

				SearchResult sr = new SearchResult();


				sr.setSearchSeqRc("CCGCUUACAGACAGCAGG");
				sr.setResultOffset(1);
				sr.setResultSeq("CGAUUACAGACAGCAGGC");
				sr.setResultAccession("xxxx");
				sr.setResultAccessionOffset(99999);
				ss.getSearchResults().add(sr);
			}
			//endtest

			log.debug("Search status == search_completed");
			SearchResultsOutputBuilder builder = new SearchResultsOutputBuilder();
			List<SearchResultContainer> resultList = builder.build(ss.getSearchResults());


			log.debug("Size:"+resultList.size());
			model.addAttribute("totalResults",resultList.size());
			model.addAttribute("resultList",resultList);


		}
		else if(ss.getStatus()==1)
		{
			log.debug("Search status == searching");
		}
		else if(ss.getStatus()==0)
		{
			log.debug("Search status == waiting");
		}

		/*
		 * Incrementing read count
		 */
		ss.setReadCount(ss.getReadCount()+1);
		safeseedDaoService.getSearchStatusService().save(ss);


		log.debug("going to page");
		model.addAttribute("ss",ss);
		return new ModelAndView("bodyOnlyTemplate|/searchResults",model);

	}

	@RequestMapping(value = "*.html")
    public ModelAndView FileNotFoundError(HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        model.addAttribute("javax.servlet.error.request_uri",request.getRequestURI());
        model.addAttribute("javax.servlet.error.exception", new Exception("Request mapping not found "+request.getRequestURI()));
        return new ModelAndView("/error",model);
    }

}
