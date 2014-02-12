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
import edu.uiowa.icts.safeseed.PotsSummary;
import edu.uiowa.icts.safeseed.SearchResultContainer;
import edu.uiowa.icts.safeseed.SearchResultsOutputBuilder;
import edu.uiowa.icts.safeseed.controller.AbstractSafeseedController;
import edu.uiowa.icts.safeseed.core.SeqUtils;
import edu.uiowa.icts.safeseed.domain.SearchResult;
import edu.uiowa.icts.safeseed.domain.SearchStatus;



@Controller
@RequestMapping("/tools/search/*")
public class SearchController extends AbstractSafeseedController
{

	private static final Log log = LogFactory.getLog(SearchController.class);


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
