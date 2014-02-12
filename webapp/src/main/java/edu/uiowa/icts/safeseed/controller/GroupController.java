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
Fri Mar 04 07:04:26 CST 2011 *
*/

@Controller
@RequestMapping("/safeseed/group/*")
public class GroupController extends AbstractSafeseedController {


    private static final Log log =LogFactory.getLog(GroupController.class);

    @RequestMapping(value = "list.html", method = RequestMethod.GET)
    public ModelAndView list()
    {
        log.debug("in list method for Group");
        ModelMap model = new ModelMap();
        model.addAttribute("groupList",safeseedDaoService.getGroupService().list());
        return new ModelAndView("/safeseed/group/list",model);
    }

    @RequestMapping(value = "add.html", method = RequestMethod.GET)
    public ModelAndView add()
    {
        log.debug("in add method for Group");
        ModelMap model = new ModelMap();
        Group group = new Group();
        model.addAttribute("group",group);
        return new ModelAndView("/safeseed/group/edit",model);
    }

    @RequestMapping(value = "edit.html", method = RequestMethod.GET)
    public ModelAndView edit(@RequestParam("groupId") Integer groupId)
    {
        log.debug("in edit method for Group");
        ModelMap model = new ModelMap();
        Group group = safeseedDaoService.getGroupService().findById(groupId);
        model.addAttribute("group",group);
        return new ModelAndView("/safeseed/group/edit",model);
    }

    @RequestMapping(value = "show.html", method = RequestMethod.GET)
    public ModelAndView show(@RequestParam("groupId") Integer groupId)
    {
        log.debug("in show method for Group");
        ModelMap model = new ModelMap();
        Group group = safeseedDaoService.getGroupService().findById(groupId);
        model.addAttribute("group",group);
        return new ModelAndView("/safeseed/group/show",model);
    }

    @RequestMapping(value = "save.html", method = RequestMethod.POST)
    public String save(@ModelAttribute("group") Group group)
    {
        log.debug("in save method for Group");
        safeseedDaoService.getGroupService().saveOrUpdate(group);
        return "redirect:/safeseed/group/list.html";
    }

    @RequestMapping(value = "delete.html", method = RequestMethod.GET)
    public String delete(@RequestParam("groupId") Integer groupId)
    {
        log.debug("in delete method for Group");
        Group group = safeseedDaoService.getGroupService().findById(groupId);
        safeseedDaoService.getGroupService().delete(group);
        return "redirect:/safeseed/group/list.html";
    }
    
}