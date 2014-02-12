package edu.uiowa.icts.safeseed.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import edu.uiowa.icts.safeseed.DataTablesProcessor;
import edu.uiowa.icts.safeseed.controller.AbstractSafeseedController;
import edu.uiowa.icts.safeseed.core.CartItem;
import edu.uiowa.icts.safeseed.core.Fragment;
import edu.uiowa.icts.safeseed.core.RNACart;
import edu.uiowa.icts.safeseed.core.SHRNA;
import edu.uiowa.icts.safeseed.core.SHRNABuilder;
import edu.uiowa.icts.safeseed.core.Seq;
import edu.uiowa.icts.safeseed.core.SeqManager;
import edu.uiowa.icts.safeseed.core.XmerType;
import edu.uiowa.icts.safeseed.core.Xmers;
import edu.uiowa.icts.safeseed.core.XmersParams;

@Controller
@RequestMapping("/tools/sispotr/*")
public class SispotrController extends AbstractSafeseedController
{
	private static final Log log = LogFactory.getLog(SispotrController.class);


	/**
	 * displays primary design/sequence input page
	 * 
	 * @param errorMessage
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "design.html", method = RequestMethod.GET)
	public ModelAndView safeseed(
			@RequestParam(value="errorMessage",required=false) String errorMessage,
			HttpServletRequest req,
			ModelMap model)	{
		log.debug("In DefaultContoller..safeseed");

		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager == null)
			seqManager = new SeqManager();

		/*
		 * If an error message exists,pass it along to the jsp page
		 */
		if(errorMessage!=null)
			model.addAttribute("errorMessage", errorMessage);

		model.addAttribute("seqManager",seqManager);

		return new ModelAndView("/tools/sispotr/design",model);

	}



	/**
	 * Reset session data, such as input sequences and shrna cart
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "reset.html", method = RequestMethod.GET)
	public ModelAndView reset(
			HttpServletRequest req,
			ModelMap model)
	{
		log.debug("In reset");

		req.getSession().setAttribute("seqManager", new SeqManager());
		req.getSession().setAttribute("cart", null);

		/*
		 * If an error message exists,pass it along to the jsp page
		 */

		return new ModelAndView("redirect:/tools/sispotr/design.html",model);

	}
	
	@RequestMapping(value = "testSession.html", method = RequestMethod.GET)
	public ModelAndView testSession(ModelMap model, HttpServletRequest req) throws FileNotFoundException, IOException{
		String input = ">VEGF\nCTGACGGACAGACAGACAGACACCGCCCCCAGCCCCAGCTACCACCTCCTCCCCGGCCGGCGGCGGACAGTGGACGCGGCGGCGAGCCGCGGGCAGGGGCCGGAGCCCGCGCCCGGAGGCGGGGTGGAGGGGGTCGGGGCTCGCGGCGTCGCACTGAAACTTTTCGTCCAACTTCTGGGCTGTTCTCGCTTCGGAGGAGCCGTGGTCCGCGCGGGGGAAGCCGAGCCGAGCGGAGCCGCGAGAAGTGCTAGCTCGGGCCGGGAGGAGCCGCAGCCGGAGGAGGGGGAGGAGGAAGAAGAGAAGGAAGAGGAGAGGGGGCCGCAGTGGCGACTCGGCGCTCGGAAGCCGGGCTCATGGACGGGTGAGGCGGCGGTGTGCGCAGACAGTGCTCCAGCCGCGCGCGCTCCCCAGGCCCTGGCCCGGGCCTCGGGCCGGGGAGGAAGAGTAGCTCGCCGAGGCGCCGAGGAGAGCGGGCCGCCCCACAGCCCGAGCCGGAGAGGGAGCGCGAGCCGCGCCGGCCCCGGTCGGGCCTCCGAAACCATGAACTTTCTGCTGTCTTGGGTGCATTGGAGCCTTGCCTTGCTGCTCTACCTCCACCATGCCAAGTGGTCCCAGGCTGCACCCATGGCAGAAGGAGGAGGGCAGAATCATCACGAAGTGGTGAAGTTCATGGATGTCTATCAGCGCAGCTACTGCCATCCAATCGAGACCCTGGTGGACATCTTCCAGGAGTACCCTGATGAGATCGAGTACATCTTCAAGCCATCCTGTGTGCCCCTGATGCGATGCGGGGGCTGCTGCAATGACGAGGGCCTGGAGTGTGTGCCCACTGAGGAGTCCAACATCACCATGCAGATTATGCGGATCAAACCTCACCAAGGCCAGCACATAGGAGAGATGAGCTTCCTACAGCACAACAAATGTGAATGCAGACCAAAGAAAGATAGAGCAAGACAAGAAAAAAAATCAGTTCGAGGAAAGGGAAAGGGGCAAAAACGAAAGCGCAAGAAATCCCGGTATAAGTCCTGGAGCGTGTACGTTGGTGCCCGCTGCTGTCTAATGCCCTGGAGCCTCCCTGGCCCCCATCCCTGTGGGCCTTGCTCAGAGCGGAGAAAGCATTTGTTTGTACAAGATCCGCAGACGTGTAAATGTTCCTGCAAAAACACAGACTCGCGTTGCAAGGCGAGGCAGCTTGAGTTAAACGAACGTACTTGCAGATGTGACAAGCCGAGGCGGTGA";
		input = input + "\n\n"+">EGFR\nATGCGACCCTCCGGGACGGCCGGGGCAGCGCTCCTGGCGCTGCTGGCTGCGCTCTGCCCGGCGAGTCGGGCTCTGGAGGAAAAGAAAGTTTGCCAAGGCACGAGTAACAAGCTCACGCAGTTGGGCACTTTTGAAGATCATTTTCTCAGCCTCCAGAGGATGTTCAATAACTGTGAGGTGGTCCTTGGGAATTTGGAAATTACCTATGTGCAGAGGAATTATGATCTTTCCTTCTTAAAGACCATCCAGGAGGTGGCTGGTTATGTCCTCATTGCCCTCAACACAGTGGAGCGAATTCCTTTGGAAAACCTGCAGATCATCAGAGGAAATATGTACTACGAAAATTCCTATGCCTTAGCAGTCTTATCTAACTATGATGCAAATAAAACCGGACTGAAGGAGCTGCCCATGAGAAATTTACAGGAAATCCTGCATGGCGCCGTGCGGTTCAGCAACAACCCTGCCCTGTGCAACGTGGAGAGCATCCAGTGGCGGGACATAGTCAGCAGTGACTTTCTCAGCAACATGTCGATGGACTTCCAGAACCACCTGGGCAGCTGCCAAAAGTGTGATCCAAGCTGTCCCAATGGGAGCTGCTGGGGTGCAGGAGAGGAGAACTGCCAGAAACTGACCAAAATCATCTGTGCCCAGCAGTGCTCCGGGCGCTGCCGTGGCAAGTCCCCCAGTGACTGCTGCCACAACCAGTGTGCTGCAGGCTGCACAGGCCCCCGGGAGAGCGACTGCCTGGTCTGCCGCAAATTCCGAGACGAAGCCACGTGCAAGGACACCTGCCCCCCACTCATGCTCTACAACCCCACCACGTACCAGATGGATGTGAACCCCGAGGGCAAATACAGCTTTGGTGCCACCTGCGTGAAGAAGTGTCCCCGTAATTATGTGGTGACAGATCACGGCTCGTGCGTCCGAGCCTGTGGGGCCGACAGCTATGAGATGGAGGAAGACGGCGTCCGCAAGTGTAAGAAGTGCGAAGGGCCTTGCCGCAAAGTGTGTAACGGAATAGGTATTGGTGAATTTAAAGACTCACTCTCCATAAATGCTACGAATATTAAACACTTCAAAAACTGCACCTCCATCAGTGGCGATCTCCACATCCTGCCGGTGGCATTTAGGGGTGACTCCTTCACACATACTCCTCCTCTGGATCCACAGGAACTGGATATTCTGAAAACCGTAAAGGAAATCACAGGGTTTTTGCTGATTCAGGCTTGGCCTGAAAACAGGACGGACCTCCATGCCTTTGAGAACCTAGAAATCATACGCGGCAGGACCAAGCAACATGGTCAGTTTTCTCTTGCAGTCGTCAGCCTGAACATAACATCCTTGGGATTACGCTCCCTCAAGGAGATAAGTGATGGAGATGTGATAATTTCAGGAAACAAAAATTTGTGCTATGCAAATACAATAAACTGGAAAAAACTGTTTGGGACCTCCGGTCAGAAAACCAAAATTATAAGCAACAGAGGTGAAAACAGCTGCAAGGCCACAGGCCAGGTCTGCCATGCCTTGTGCTCCCCCGAGGGCTGCTGGGGCCCGGAGCCCAGGGACTGCGTCTCTTGCCGGAATGTCAGCCGAGGCAGGGAATGCGTGGACAAGTGCAACCTTCTGGAGGGTGAGCCAAGGGAGTTTGTGGAGAACTCTGAGTGCATACAGTGCCACCCAGAGTGCCTGCCTCAGGCCATGAACATCACCTGCACAGGACGGGGACCAGACAACTGTATCCAGTGTGCCCACTACATTGACGGCCCCCACTGCGTCAAGACCTGCCCGGCAGGAGTCATGGGAGAAAACAACACCCTGGTCTGGAAGTACGCAGACGCCGGCCATGTGTGCCACCTGTGCCATCCAAACTGCACCTACGGATGCACTGGGCCAGGTCTTGAAGGCTGTCCAACGAATGGGCCTAAGATCCCGTCCATCGCCACTGGGATGGTGGGGGCCCTCCTCTTGCTGCTGGTGGTGGCCCTGGGGATCGGCCTCTTCATGCGAAGGCGCCACATCGTTCGGAAGCGCACGCTGCGGAGGCTGCTGCAGGAGAGGGAGCTTGTGGAGCCTCTTACACCCAGTGGAGAAGCTCCCAACCAAGCTCTCTTGAGGATCTTGAAGGAAACTGAATTCAAAAAGATCAAAGTGCTGGGCTCCGGTGCGTTCGGCACGGTGTATAAGGGACTCTGGATCCCAGAAGGTGAGAAAGTTAAAATTCCCGTCGCTATCAAGGAATTAAGAGAAGCAACATCTCCGAAAGCCAACAAGGAAATCCTCGATGAAGCCTACGTGATGGCCAGCGTGGACAACCCCCACGTGTGCCGCCTGCTGGGCATCTGCCTCACCTCCACCGTGCAGCTCATCACGCAGCTCATGCCCTTCGGCTGCCTCCTGGACTATGTCCGGGAACACAAAGACAATATTGGCTCCCAGTACCTGCTCAACTGGTGTGTGCAGATCGCAAAGGGCATGAACTACTTGGAGGACCGTCGCTTGGTGCACCGCGACCTGGCAGCCAGGAACGTACTGGTGAAAACACCGCAGCATGTCAAGATCACAGATTTTGGGCTGGCCAAACTGCTGGGTGCGGAAGAGAAAGAATACCATGCAGAAGGAGGCAAAGTGCCTATCAAGTGGATGGCATTGGAATCAATTTTACACAGAATCTATACCCACCAGAGTGATGTCTGGAGCTACGGGGTGACCGTTTGGGAGTTGATGACCTTTGGATCCAAGCCATATGACGGAATCCCTGCCAGCGAGATCTCCTCCATCCTGGAGAAAGGAGAACGCCTCCCTCAGCCACCCATATGTACCATCGATGTCTACATGATCATGGTCAAGTGCTGGATGATAGACGCAGATAGTCGCCCAAAGTTCCGTGAGTTGATCATCGAATTCTCCAAAATGGCCCGAGACCCCCAGCGCTACCTTGTCATTCAGGGGGATGAAAGAATGCATTTGCCAAGTCCTACAGACTCCAACTTCTACCGTGCCCTGATGGATGAAGAAGACATGGACGACGTGGTGGATGCCGACGAGTACCTCATCCCACAGCAGGGCTTCTTCAGCAGCCCCTCCACGTCACGGACTCCCCTCCTGAGCTCTCTGAGTGCAACCAGCAACAATTCCACCGTGGCTTGCATTGATAGAAATGGGCTGCAAAGCTGTCCCATCAAGGAAGACAGCTTCTTGCAGCGATACAGCTCAGACCCCACAGGCGCCTTGACTGAGGACAGCATAGACGACACCTTCCTCCCAGTGCCTGAATACATAAACCAGTCCGTTCCCAAAAGGCCCGCTGGCTCTGTGCAGAATCCTGTCTATCACAATCAGCCTCTGAACCCCGCGCCCAGCAGAGACCCACACTACCAGGACCCCCACAGCACTGCAGTGGGCAACCCCGAGTATCTCAACACTGTCCAGCCCACCTGTGTCAACAGCACATTCGACAGCCCTGCCCACTGGGCCCAGAAAGGCAGCCACCAAATTAGCCTGGACAACCCTGACTACCAGCAGGACTTCTTTCCCAAGGAAGCCAAGCCAAATGGCATCTTTAAGGGCTCCACAGCTGAAAATGCAGAATACCTAAGGGTCGCGCCACAAAGCAGTGAATTTATTGGAGCATGA";
		input = input + "\n\n"+">BACE1\nATGGCCCAAGCCCTGCCCTGGCTCCTGCTGTGGATGGGCGCGGGAGTGCTGCCTGCCCACGGCACCCAGCACGGCATCCGGCTGCCCCTGCGCAGCGGCCTGGGGGGCGCCCCCCTGGGGCTGCGGCTGCCCCGGGAGACCGACGAAGAGCCCGAGGAGCCCGGCCGGAGGGGCAGCTTTGTGGAGATGGTGGACAACCTGAGGGGCAAGTCGGGGCAGGGCTACTACGTGGAGATGACCGTGGGCAGCCCCCCGCAGACGCTCAACATCCTGGTGGATACAGGCAGCAGTAACTTTGCAGTGGGTGCTGCCCCCCACCCCTTCCTGCATCGCTACTACCAGAGGCAGCTGTCCAGCACATACCGGGACCTCCGGAAGGGTGTGTATGTGCCCTACACCCAGGGCAAGTGGGAAGGGGAGCTGGGCACCGACCTGGTAAGCATCCCCCATGGCCCCAACGTCACTGTGCGTGCCAACATTGCTGCCATCACTGAATCAGACAAGTTCTTCATCAACGGCTCCAACTGGGAAGGCATCCTGGGGCTGGCCTATGCTGAGATTGCCAGGCCTGACGACTCCCTGGAGCCTTTCTTTGACTCTCTGGTAAAGCAGACCCACGTTCCCAACCTCTTCTCCCTGCAGCTTTGTGGTGCTGGCTTCCCCCTCAACCAGTCTGAAGTGCTGGCCTCTGTCGGAGGGAGCATGATCATTGGAGGTATCGACCACTCGCTGTACACAGGCAGTCTCTGGTATACACCCATCCGGCGGGAGTGGTATTATGAGGTGATCATTGTGCGGGTGGAGATCAATGGACAGGATCTGAAAATGGACTGCAAGGAGTACAACTATGACAAGAGCATTGTGGACAGTGGCACCACCAACCTTCGTTTGCCCAAGAAAGTGTTTGAAGCTGCAGTCAAATCCATCAAGGCAGCCTCCTCCACGGAGAAGTTCCCTGATGGTTTCTGGCTAGGAGAGCAGCTGGTGTGCTGGCAAGCAGGCACCACCCCTTGGAACATTTTCCCAGTCATCTCACTCTACCTAATGGGTGAGGTTACCAACCAGTCCTTCCGCATCACCATCCTTCCGCAGCAATACCTGCGGCCAGTGGAAGATGTGGCCACGTCCCAAGACGACTGTTACAAGTTTGCCATCTCACAGTCATCCACGGGCACTGTTATGGGAGCTGTTATCATGGAGGGCTTCTACGTTGTCTTTGATCGGGCCCGAAAACGAATTGGCTTTGCTGTCAGCGCTTGCCATGTGCACGATGAGTTCAGGACGGCAGCGGTGGAAGGCCCTTTTGTCACCTTGGACATGGAAGACTGTGGCTACAACATTCCACAGACAGATGAGTCAACCCTCATGACCATAGCCTATGTCATGGCTGCCATCTGCGCCCTCTTCATGCTGCCACTCTGCCTCATGGTGTGTCAGTGGCGCTGCCTCCGCTGCCTGCGCCAGCAGCATGATGACTTTGCTGATGACATCTCCCTGCTGAAGTGA";
		log.debug("input size:"+input.length());
		
		
		
		return addNewSequences(21,10.0,90.0,2,false,false,false,100,"shrna","human",true,input,model,req);
	}
	

	/**
	 * Adds sequences to SeqManager and processes them, then forwards to display page

	 * @param lengthOfXmers 
	 * @param gcMin
	 * @param gcMax
	 * @param gcRequired
	 * @param gAt2
	 * @param gAt3
	 * @param ensureBestPots
	 * @param numberOfReturnElements
	 * @param designType
	 * @param species
	 * @param pol3
	 * @param seqText
	 * @param model
	 * @param req
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value = "addNewSequences.html", method = RequestMethod.POST)
	public ModelAndView addNewSequences(
			@RequestParam("xmer") int lengthOfXmers,
			@RequestParam("gcmin") double gcMin,
			@RequestParam("gcmax") double gcMax,
			@RequestParam("required") int gcRequired,
			@RequestParam(value = "gat2",defaultValue = "true") boolean gAt2,
			@RequestParam(value="gat3",defaultValue ="true") boolean gAt3,
			@RequestParam(value= "ensure",defaultValue = "false") boolean ensureBestPots,
			@RequestParam("number") int numberOfReturnElements,
			@RequestParam("designType") String designType,
			@RequestParam(value ="species", defaultValue="human") String species,
			@RequestParam(value ="pol3", defaultValue="false") Boolean pol3,
			@RequestParam(required = false , value = "seqText") String seqText,
			ModelMap model, HttpServletRequest req) throws FileNotFoundException, IOException {		

		/**
		 * Get file from the form element named "fseq"
		 * if its null, then check for pasted sequence text
		 * If neither exist, give an error
		 */
		
		MultipartFile file = null;
		
		if(seqText == null || seqText.length()==0){
			MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
			file = mreq.getFile("fseq");
		}

		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager == null)
			seqManager = new SeqManager();

		if(file != null && file.getBytes().length!=0)
		{
			log.debug("File found...using");
			seqText = new String(file.getBytes());
		}
		else if( seqText!=null)
		{
			log.debug("Pasted Sequence found...using");
		}
		else
		{
			log.debug("No file or text sequence was entered");
			return new ModelAndView("redirect:/tools/sispotr/design.html?errorMessage=Error: No sequence has been entered");
		}

		log.debug("Sequence text length:\n"+seqText.length());
		log.debug("Starting recomputation....");
		
		/**
		 * cap number of bases to be entered at one time
		 */
		if(seqText.length()>=101000){
			return new ModelAndView("redirect:/tools/sispotr/design.html?errorMessage=Error: Sequences cannot contain more than 100,000 bases");
		}

		XmersParams params = null;

		if("shrna".equalsIgnoreCase(designType)){
			pol3 = true;
			params = new XmersParams(7, gcMin/100.0, gcMax/100.0, gcRequired, gAt2, gAt3, ensureBestPots, pol3, numberOfReturnElements,true,XmerType.SHRNA);
		}
		else{
			params = new XmersParams(7, gcMin/100.0, gcMax/100.0, gcRequired, gAt2, gAt3, ensureBestPots, pol3, numberOfReturnElements);	
		}

		log.debug(params.toString());
		List<String> seqNameList = seqManager.addSeq(seqText,species,lengthOfXmers,params);

		
		/**
		 * Cap number of sequences to be entered at one time
		 */
		if(seqNameList.size()>5){
			return new ModelAndView("redirect:/tools/sispotr/design.html?errorMessage=Error: Maximum of 5 sequences allowed.  Found: "+seqNameList.size());
		}
		
		/**
		 * get the tab index for the sequence to load
		 */
		String seqName = "";
		Integer seqIndex = 0;
		if(seqNameList.size()>=1){
			seqName = seqNameList.get(0);
			int i = 0;
			for(Seq s:seqManager.getSeqs()){

				for(Xmers x:s.getXmersList()){

					if(x.getXmerType().toString().toLowerCase().equalsIgnoreCase(designType)){
						if(s.getLabel().equalsIgnoreCase(seqName)){
							seqIndex = i;
						}	
						i++;
					}
				}
			}
		}
		req.getSession().setAttribute("seqManager",seqManager);
		req.getSession().setAttribute("defaultParams",params);
		return new ModelAndView("redirect:displayResults.html?type="+designType+"&seqIndex="+seqIndex);

	}



	/**
	 * Display results for siRNA or shRNA 
	 * 
	 * @param type  used to forward to appropriate jsp page
	 * @param model
	 * @param req
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value = "displayResults.html", method = RequestMethod.GET)
	public ModelAndView displayResults(
			String type,
			@RequestParam(value="clearCart",defaultValue="false") Boolean clearCart,  
			@RequestParam(value="seqIndex",defaultValue="0") Integer seqIndex,

			ModelMap model, 
			HttpServletRequest req) throws FileNotFoundException, IOException{		

		if(clearCart){
			req.getSession().setAttribute("cart", null);
		}

		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		XmersParams params = (XmersParams)req.getSession().getAttribute("defaultParams");
		if(seqManager == null || params == null){
			return new ModelAndView("redirect:design.html");	
		}

		log.debug("In DefaultContoller..displayResults.html");
		/*store in session
		 * 
		 */
		req.getSession().setAttribute("seqManager",seqManager);

		req.getSession().setAttribute("defaultParams",params);
		ModelMap modelMap = new ModelMap();



		modelMap.addAttribute("defaultParams",params);
		modelMap.addAttribute("seqManager",seqManager);
		modelMap.addAttribute("seqIndex",seqIndex);
		//return new ModelAndView("baseTemplateWide|/results",modelMap);
		return new ModelAndView("/tools/sispotr/displayResults"+type.toUpperCase(),modelMap);

	}


	/**
	 * Method to recompute table data with provided params
	 * called via ajax
	 * 
	 * @param aid
	 * @param lengthOfXmers
	 * @param gcMin
	 * @param gcMax
	 * @param gcRequired
	 * @param gAt2
	 * @param gAt3
	 * @param ensureBestPots
	 * @param numberOfReturnElements
	 * @param species
	 * @param pol3
	 * @param res
	 * @param req
	 * @param model
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@RequestMapping(value = "updateTabData.html", method = RequestMethod.GET)
	public void updateTabData(
			@RequestParam("aid") String aid, 
			@RequestParam("lengthOfXmers") int lengthOfXmers,
			@RequestParam("gcMin") double gcMin,
			@RequestParam("gcMax") double gcMax,
			@RequestParam("gcRequired") int gcRequired,
			@RequestParam(value = "gat2",defaultValue = "true") boolean gAt2,
			@RequestParam(value="gat3",defaultValue ="true") boolean gAt3,
			@RequestParam(value= "ensure",defaultValue = "false") boolean ensureBestPots,
			@RequestParam("numberOfReturnElements") int numberOfReturnElements,
			@RequestParam(value ="species",required=false, defaultValue="human") String species,
			@RequestParam(value ="pol3",required=false, defaultValue="false") Boolean pol3,
			HttpServletResponse res,
			HttpServletRequest req,
			ModelMap model) throws FileNotFoundException, IOException{
		log.debug("in updateTab tab method, here:"+aid);
		model.addAttribute("aid", aid);

		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager == null)
			seqManager = new SeqManager();
		log.debug("pol3:"+pol3);

		Xmers xmer =  seqManager.getSeq(aid).getXmers(lengthOfXmers);
		log.debug("Starting computation....");

		xmer.updateParams(gcMin/100.0, gcMax/100.0, gcRequired, gAt2, gAt3, ensureBestPots, pol3, numberOfReturnElements);
		xmer.updateFragments();
		log.debug("Computation complete....");
		PrintWriter out = res.getWriter();
		//	out.print("complete");
		out.close();

	}

	/**
	 * Ajax call that renders data for tab
	 * 
	 * @param aid
	 * @param type
	 * @param length
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "loadTab.html", method = RequestMethod.GET)
	public ModelAndView loadTab(
			@RequestParam("aid") String aid,
			@RequestParam("type") String type,
			@RequestParam("length") int length, 
			HttpServletRequest req,
			ModelMap model)
	{
		log.debug("in results tab method:"+aid);
		model.addAttribute("aid", aid);

		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager==null)
		{
			log.debug("Error: session expired");
			model.addAttribute("errorMessage", "Error loading");
			return new ModelAndView("bodyOnlyTemplate|/error",model);
		}

		Seq seq = seqManager.getSeq(aid);
		Xmers xmers = seq.getXmers(length);

		log.debug("Xmers:"+xmers.toString());
		log.debug("Passed sequences:"+xmers.getPassedFragments().size());
		xmers.createPassedTableOutput();
		if(xmers.getData()==null)
			log.error("error:xmer has no data");
		log.debug("....rows:"+xmers.getData().size());

		model.addAttribute("xmer", xmers);


		return new ModelAndView("bodyOnlyTemplate|/tools/sispotr/tab"+type.toUpperCase(),model);

	}


	/**
	 * Add Item to Cart
	 * ajax call
	 * 
	 * @param aid
	 * @param index
	 * @param res
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "addToCart.html", method = RequestMethod.GET)
	public ModelAndView addToCart(
			@RequestParam("aid") String aid,
			@RequestParam("index") int index,
			HttpServletResponse res,
			HttpServletRequest req,
			ModelMap model)
	{
		log.debug("in results tab method:"+aid);
		model.addAttribute("aid", aid);

		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager==null)
		{
			log.debug("Error: session expired");
			model.addAttribute("errorMessage", "Error loading");
			return new ModelAndView("bodyOnlyTemplate|/error",model);
		}
		RNACart cart = (RNACart)req.getSession().getAttribute("cart");

		if(cart==null){
			cart = new RNACart();
		}

		Seq seq = seqManager.getSeq(aid);
		Xmers xmers = seq.getXmers(22);

		List<String> details = xmers.getData().get(index);
		CartItem item = new CartItem();

		item.addAttr("aid", aid);
		item.addAttr("length", "22");
		item.addAttr("start_pos", details.get(1));
		item.addAttr("pots", details.get(2));
		item.addAttr("guide", details.get(4));
		item.addAttr("passenger", details.get(5));
		item.addAttr("seed", details.get(6));

		cart.addItem(item);

		model.addAttribute("cart", cart);

		req.getSession().setAttribute("cart", cart);

		return new ModelAndView("bodyOnlyTemplate|/tools/sispotr/cart",model);

	}



	/**
	 * Show cart
	 * ajax call
	 * 
	 * @param res
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "showCart.html", method = RequestMethod.GET)
	public ModelAndView showCart(

			HttpServletResponse res,
			HttpServletRequest req,
			ModelMap model)
	{

		RNACart cart = (RNACart)req.getSession().getAttribute("cart");

		if(cart!=null){
			model.addAttribute("cart", cart);
		}

		return new ModelAndView("bodyOnlyTemplate|/tools/sispotr/cart",model);

	}

	/**
	 * Clear shRNA cart
	 * 
	 * @param res
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "clearCart.html", method = RequestMethod.GET)
	public ModelAndView clearCart(

			HttpServletResponse res,
			HttpServletRequest req,
			ModelMap model){

		req.getSession().setAttribute("cart", null);
		return new ModelAndView("bodyOnlyTemplate|/tools/sispotr/cart",model);
	}




	/**
	 * shRNA builder page
	 * 
	 * @param res
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "shRNABuilder.html", method = RequestMethod.GET)
	public ModelAndView shRNABuilder(

			HttpServletResponse res,
			HttpServletRequest req,
			ModelMap model)
	{

		RNACart cart = (RNACart)req.getSession().getAttribute("cart");

		if(cart!=null){
			model.addAttribute("cart", cart);
		}
		
		/**
		 * Skipping building SHRNA
		 */
		return new ModelAndView("redirect:/tools/sispotr/generateSHRNA.html?start_seq=CCCTTGGAGAAAAGCCTTGTTT&loop_seq=CTGTAAAGCCACAGATGGG&end_seq=TTTTTTctcgag",model);
		
		//return new ModelAndView("/tools/sispotr/buildSHRNA",model);

	}

	/**
	 * Generate SHRNA and output to page
	 * called from shRNA builder page
	 * 
	 * @param start_seq
	 * @param loop_seq
	 * @param end_seq
	 * @param res
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "generateSHRNA.html", method = RequestMethod.GET)
	public ModelAndView generateSHRNA(
			@RequestParam("start_seq") String start_seq,
			@RequestParam("loop_seq") String loop_seq,
			@RequestParam("end_seq") String end_seq,
			HttpServletResponse res,
			HttpServletRequest req,
			ModelMap model){
		log.debug("generateSHRNA method");

		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager==null)
		{
			log.debug("Error: session expired");
			model.addAttribute("errorMessage", "Error loading");
			return new ModelAndView("bodyOnlyTemplate|/error",model);
		}
		RNACart cart = (RNACart)req.getSession().getAttribute("cart");

		if(cart==null){
			return new ModelAndView("/error",model);
		}


		List<SHRNA> shrnaOutputList = new ArrayList<SHRNA>();
		SHRNABuilder builder = new SHRNABuilder();

		for(CartItem item : cart.getItemList()){

			shrnaOutputList.add(builder.createSRNAFromItem(start_seq, loop_seq, end_seq, item));

		}

		model.addAttribute("shrnaList", shrnaOutputList);


		return new ModelAndView("/tools/sispotr/SHRNAResultsOutput",model);

	}

	/**
	 * Ajax call for displaying datatables data
	 * 
	 * TODO: not in use...not yet working
	 * 
	 * @param aid
	 * @param req
	 * @param res
	 * @param model
	 */
	@RequestMapping(value = "processTable.html", method = RequestMethod.GET)
	public void processTable(
			@RequestParam("aid") String aid, 
			HttpServletRequest req,
			HttpServletResponse res,
			ModelMap model)
	{

		log.debug("in datatables ajax method");
		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager==null)
		{
			log.debug("Error: session expired");
			model.addAttribute("errorMessage", "Error loading");
			return;
			//	return new ModelAndView("bodyOnlyTemplate|/error",model);
		}

		Seq seq = seqManager.getSeq(aid);
		Xmers xmers = seq.getXmers(21);




		log.debug("Xmers:"+xmers.toString());
		log.debug("Passed sequences:"+xmers.getPassedFragments().size());
		xmers.createPassedTableOutput();
		if(xmers.getData()==null)
			log.error("error:xmer has no data");

		DataTablesProcessor dtProcessor = new DataTablesProcessor(xmers.getNumberOfReturnElements(), xmers.getData());

		List<Boolean> sortableList = new ArrayList<Boolean>();
		List<String> colTypes= new ArrayList<String>();
		List<String> celTemplate= new ArrayList<String>();
		for(String col : xmers.getColumns())
		{
			log.debug(""+col);
			sortableList.add(true);
			colTypes.add("String");

		}

		try {
			dtProcessor.buildTable(xmers.getColumns(),sortableList,colTypes,celTemplate, req);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/**
	 * Ajax call for displaying commonResults tab
	 * 
	 * @param req
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "commonResults.html", method = RequestMethod.GET)
	public ModelAndView commonResults(
			HttpServletRequest req,
			ModelMap model)
	{
		log.debug("in common results method");


		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager==null)
		{
			log.debug("Error: session expired");
			model.addAttribute("errorMessage", "Error loading");
			return new ModelAndView("bodyOnlyTemplate|/error",model);
		}

		HashMap<String,List<String> > fragmentCountMap = new HashMap<String, List<String> >();
		HashMap<String,List< String[]>> fragmentMap = new HashMap<String, List<String[]>>();
		List<String> passList = new ArrayList<String>();



		for(Seq seq: seqManager.getSeqs())
		{
			Xmers x = seq.getXmers(21); 

			log.debug("sequence:"+seq.getLabel());
			/**
			 * WHy? 
			 * TODO
			 */
			if(x == null){
				log.debug("   skipping,,,null length FIX ME");
				continue;
			}
			log.debug("    length:"+x.getLength());
			int i=0;
			for(Fragment f: x.getPassedFragments())
			{


				List<String> xmerList = null;
				List<String[]> fragList = null;

				if(fragmentCountMap.containsKey(f.getSiRNASeq())){
					xmerList = fragmentCountMap.get(f.getSiRNASeq());
				}
				else{
					xmerList = new ArrayList<String>();
				}

				if(fragmentMap.containsKey(f.getSiRNASeq())){
					fragList = fragmentMap.get(f.getSiRNASeq());
				}
				else{
					fragList = new ArrayList<String[]>();
				}

				xmerList.add(seq.getLabel());

				fragmentCountMap.put(f.getSiRNASeq(), xmerList);
				fragList.add(new String[]{seq.getLabel(),f.getPots_nt(),""+f.getStartPosition(),String.valueOf(f.getPots_double())});
				fragmentMap.put(f.getSiRNASeq(), fragList);

				if(xmerList.size()>=2)
				{

					//info.add(f.getSiRNASeq());
					passList.add(f.getSiRNASeq());
				}

				i++;
			}
			log.debug("count:"+i);


		}


		for(String fragSeq: passList)
		{
			log.debug("frag:"+ fragSeq);

			for(String xmer: fragmentCountMap.get(fragSeq) )
			{
				log.debug("   xmer:"+ xmer);

			}

		}
		log.debug("fragCount:"+fragmentMap.size());
		model.addAttribute("fragmentCountMap",fragmentCountMap);
		model.addAttribute("fragmentMap",fragmentMap);;
		model.addAttribute("passList",passList);

		return new ModelAndView("bodyOnlyTemplate|/tools/sispotr/commonResults",model);

	}

	/**
	 * Export to CSV
	 * 
	 * @param aid
	 * @param length
	 * @param req
	 * @param res
	 * @throws IOException
	 */
	@RequestMapping(value = "exportTab.html", method = RequestMethod.GET)
	public void exportTab(
			@RequestParam("aid") String aid,
			@RequestParam("length") int length,
			HttpServletRequest req,
			HttpServletResponse res
			) throws IOException
			{
		log.debug("in results tab method:"+aid);


		SeqManager seqManager = (SeqManager)req.getSession().getAttribute("seqManager");
		if(seqManager==null)
		{
			log.debug("Error: session expired");
			return;
		}

		String[] columns = new String[]{"rank","position","POTS","percentile_worse","siRNA_Seq","passenger_strand_seq","7Mer","miRNA_seed_match","miRNA_conservation","gc_content_percent"};




		Xmers xmer =  seqManager.getSeq(aid).getXmers(length);
		if(xmer.getData()==null)
			log.error("error:xmer has no data");
		log.debug("....rows:"+xmer.getData().size());


		res.setContentType("text/csv");
		//	res.setContentType("application/rdf+xml");
		res.setHeader("Pragma", "");
		res.setHeader("Cache-Control", "No-cache"); 
		res.setHeader(
				"Content-Disposition",
				"attachment; filename=\""+aid+".csv\"");

		PrintWriter out = res.getWriter();

		//print columns
		int i=0;
		int total = columns.length;
		for(String col:columns)
		{

			out.print(col);
			if(i<total-1)
				out.print(",");
			i++;
		}
		out.print("\n");
		//Print data
		for(List<String> rows:xmer.getData())
		{

			i=0;
			for(String cell: rows)
			{
				out.print(cell);
				if(i<total-1)
					out.print(",");
				i++;
			}

			out.print("\n");
		}
		out.close();

			}





	/**
	 * Display error page
	 * 
	 * @param model
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "error.html", method = RequestMethod.GET)
	public ModelAndView error(ModelMap model,HttpServletRequest request)
	{

		log.debug("Error page ");
		log.error("Error URI: " + request.getAttribute("javax.servlet.error.request_uri")) ;
		//log.error("Error User: " + getUsername());
		log.error("Error Message: " + request.getAttribute("javax.servlet.error.message"), (Throwable)request.getAttribute("javax.servlet.error.exception")) ;

		log.debug("Error page ");

		return new ModelAndView("error",model);

	}


	@RequestMapping(value = "*.html")
    public ModelAndView FileNotFoundError(HttpServletRequest request, HttpServletResponse response) {
        ModelMap model = new ModelMap();
        model.addAttribute("javax.servlet.error.request_uri",request.getRequestURI());
        model.addAttribute("javax.servlet.error.exception", new Exception("Request mapping not found "+request.getRequestURI()));
        return new ModelAndView("/error",model);
    }


}
