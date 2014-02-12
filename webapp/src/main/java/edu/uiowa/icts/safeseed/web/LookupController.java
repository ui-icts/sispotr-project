package edu.uiowa.icts.safeseed.web;

import java.io.IOException;
import java.util.ArrayList;
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

import edu.uiowa.icts.safeseed.controller.AbstractSafeseedController;
import edu.uiowa.icts.safeseed.core.SeqRefLookup;



@Controller
@RequestMapping("/tools/lookup/*")
public class LookupController extends AbstractSafeseedController
{

	private static final Log log = LogFactory.getLog(LookupController.class);

	
	

	@RequestMapping(value = "evaluate.html", method = RequestMethod.GET)
	public ModelAndView evaluate(	
			ModelMap model)
	{
		log.debug("In evaluate.html..help");

		return new ModelAndView("/tools/lookup/evaluate",model);

	}

	@RequestMapping(value = "runEvaluate.html", method = RequestMethod.POST)
	public ModelAndView evaluate(
			@RequestParam(value = "radioVal") String radioVal,
			@RequestParam(value = "textVal") String textVal,
			@RequestParam(value = "species") String species,
			HttpServletRequest req,
			ModelMap model) throws IOException
			{
		log.debug("In evaluate.html..help");

		SeqRefLookup lookup = new SeqRefLookup();

		
		if(radioVal!=null && textVal != null)
		{
			
			boolean a = (radioVal.equalsIgnoreCase("a")) ? true : false;
			textVal = textVal.replace("\n", "\r");
			String[] s = textVal.split("\\\r");
			List<String> l = new ArrayList<String>();
			// the split produces a blank line in between sequences, this removes those
			for(int i=0; i<s.length; i++){
				String t = s[i];
				if(t.length() > 0){
					l.add(t);
				}
			}
			if(l.size()>20){
				log.debug("Error: too many sequences");
				return new ModelAndView("redirect:/tools/lookup/evaluate.html?errorMessage=Error: too many sequences",model);
			}
			lookup.evaluateSIRNA(species,l, a);
		}

		log.debug("list-size:"+lookup.getEvalSIRNAData().size());
		model.addAttribute("list", lookup);
		


		model.addAttribute("species",species);
		model.addAttribute("radioVal",radioVal);
		model.addAttribute("textVal",textVal);

		return new ModelAndView("/tools/lookup/evaluateResults",model);

	}
	
	@RequestMapping(value = "*.html")
    public ModelAndView FileNotFoundError(HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        model.addAttribute("javax.servlet.error.request_uri",request.getRequestURI());
        model.addAttribute("javax.servlet.error.exception", new Exception("Request mapping not found "+request.getRequestURI()));
        return new ModelAndView("/error",model);
    }




}
