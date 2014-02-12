package edu.uiowa.icts.safeseed.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.uiowa.icts.spring.*;
import edu.uiowa.icts.safeseed.domain.*;
import edu.uiowa.icts.safeseed.dao.*;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/* Generated by Protogen
 *
Wed Apr 27 09:37:08 CDT 2011 *
*/

@Controller
@RequestMapping("/safeseed/searchstatus/*")
public class SearchStatusController extends AbstractSafeseedController {


    private static final Log log =LogFactory.getLog(SearchStatusController.class);

    @RequestMapping(value = "list.html", method = RequestMethod.GET)
    public ModelAndView list()
    {
        log.debug("in list method for SearchStatus");
        ModelMap model = new ModelMap();
        model.addAttribute("searchStatusList",safeseedDaoService.getSearchStatusService().list());
        return new ModelAndView("/safeseed/searchstatus/list",model);
    }

    @RequestMapping(value = "add.html", method = RequestMethod.GET)
    public ModelAndView add()
    {
        log.debug("in add method for SearchStatus");
        ModelMap model = new ModelMap();
        SearchStatus searchStatus = new SearchStatus();
        model.addAttribute("searchStatus",searchStatus);
        return new ModelAndView("/safeseed/searchstatus/edit",model);
    }

    @RequestMapping(value = "edit.html", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam("searchSeq") String searchStatusId)
    {
        log.debug("in edit method for SearchStatus");
        ModelMap model = new ModelMap();
        SearchStatus searchStatus = safeseedDaoService.getSearchStatusService().findById(searchStatusId);
        model.addAttribute("searchStatus",searchStatus);
        return new ModelAndView("/safeseed/searchstatus/edit",model);
    }

    @RequestMapping(value = "show.html", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam("searchSeq") String searchStatusId)
    {
        log.debug("in show method for SearchStatus");
        ModelMap model = new ModelMap();
        SearchStatus searchStatus = safeseedDaoService.getSearchStatusService().findById(searchStatusId);
        model.addAttribute("searchStatus",searchStatus);
        return new ModelAndView("/safeseed/searchstatus/show",model);
    }

    @RequestMapping(value = "save.html", method = RequestMethod.POST)
    public String save(@ModelAttribute("searchStatus") SearchStatus searchStatus)
    {
        log.debug("in save method for SearchStatus");
        safeseedDaoService.getSearchStatusService().saveOrUpdate(searchStatus);
        return "redirect:/safeseed/searchstatus/list.html";
    }

    @RequestMapping(value = "delete.html", method = RequestMethod.GET)
    public String delete(@RequestParam("searchSeq") String searchStatusId)
    {
        log.debug("in delete method for SearchStatus");
        SearchStatus searchStatus = safeseedDaoService.getSearchStatusService().findById(searchStatusId);
        safeseedDaoService.getSearchStatusService().delete(searchStatus);
        return "redirect:/safeseed/searchstatus/list.html";
    }

}