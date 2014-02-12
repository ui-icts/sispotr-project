package edu.uiowa.icts.safeseed.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.ModelAndView;

import edu.uiowa.icts.exception.MappingNotFoundException;
import edu.uiowa.icts.safeseed.controller.AbstractSafeseedController;
import edu.uiowa.icts.safeseed.domain.Person;



@Controller
@RequestMapping("/*")
public class DefaultController extends AbstractSafeseedController
{
  
	//not used right now
	public static String passwordSalt = "in1ins878";
	private static final Log log = LogFactory.getLog(DefaultController.class);

	private ProviderManager authenticationManager;


	public void setAuthenticationManager(ProviderManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}



	@RequestMapping("/**")
	public void mappingNotFound(HttpServletRequest request, HttpServletResponse response) throws MappingNotFoundException {
		throw new MappingNotFoundException(request.getRequestURL().toString());
	}

	@RequestMapping(value = "{page}.html", method = RequestMethod.GET)
	public ModelAndView displayDefault(@PathVariable String page,HttpServletRequest req,HttpServletResponse res)
	{
		ModelMap model = new ModelMap();
		return new ModelAndView(page,model);

	}


	@RequestMapping(value = "index.html", method = RequestMethod.GET)
	public ModelAndView index(ModelMap model)
	{
		log.debug("In DefaultContoller..index");
		if("anonymousUser".equalsIgnoreCase(getUsername())==false)
		{
			log.debug("Logged in as: " +getUsername());
			Person user = safeseedDaoService.getPersonService().findByUsername(getUsername());
			if(user==null)
			{
				user = new Person(getUsername(),"","","");
				safeseedDaoService.getPersonService().save(user);
				log.debug("...new user added");
			}
			else
			{
				log.debug("...user exists");
			}
		}
		else 
			log.debug("not logged in");


		return new ModelAndView("index",model);

	}

	@RequestMapping(value = "help.html", method = RequestMethod.GET)
	public ModelAndView help(ModelMap model)
	{
		log.debug("In DefaultContoller..help");

		return new ModelAndView("help",model);

	}




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



	@RequestMapping(value = "admin/home.html", method = RequestMethod.GET)
	public ModelAndView adminHome(ModelMap model)
	{
		log.debug("In DefaultContoller..index");

		return new ModelAndView("admin/home",model);

	}


	@RequestMapping(value = "login.html", method = RequestMethod.GET)
	public ModelAndView login(@RequestParam(value = "login_error", required=false) String login_error,
			HttpServletRequest req,
			ModelMap model)
	{

		model.addAttribute("login_error",login_error);
		return new ModelAndView("baseTemplateWide|login",model);

	}
	@RequestMapping(value = "register.html", method = RequestMethod.GET)
	public ModelAndView register(HttpServletRequest req,
			ModelMap model)
	{


		return new ModelAndView("baseTemplateWide|register",model);

	}

	@RequestMapping(value = "register.html", method = RequestMethod.POST)
	public ModelAndView registerSubmit(
			@RequestParam(value = "username") String username,
			@RequestParam(value = "email") String email,
			@RequestParam(value = "organization") String organization,
			@RequestParam(value = "industry") String industry,
			@RequestParam(value = "password1") String password1,
			@RequestParam(value = "password2") String password2,
			HttpServletRequest req,
			ModelMap model)
	{
		username = username.toLowerCase();
		model.addAttribute("regusername",username.toLowerCase());
		model.addAttribute("regemail",email);
		model.addAttribute("regorg",organization);

		if(username.length()<5 && username.equals(username.replace(" ",""))==false){
			log.debug("login failed, bad username");
			return new ModelAndView("baseTemplateWide|register",model);
		}

		if(email.length()<3 || email.contains("@")==false){
			log.debug("login failed, bad email");
			model.addAttribute("error","registration failed: bad email address");
			return new ModelAndView("baseTemplateWide|register",model);
		}
		if(password1.length()<5 || password1.equals(password2)==false){
			log.debug("login failed, bad password");
			model.addAttribute("error","registration failed: passwords must be 6 or more characters and must match");
			return new ModelAndView("baseTemplateWide|register",model);
		}

		Person person1 = safeseedDaoService.getPersonService().findByUsername(username);

		if(person1 != null){
			log.debug("login failed, username taken");
			model.addAttribute("error","registration failed: username is already used");
			return new ModelAndView("baseTemplateWide|register",model);
		}

		Person person2 = safeseedDaoService.getPersonService().findByEmail(email);

		if(person2 != null){
			log.debug("login failed, email exists");
			model.addAttribute("error","registration failed: email is already used");
			return new ModelAndView("baseTemplateWide|register",model);
		}

		Person newPerson = new Person();
		newPerson.setUsername(username.toLowerCase());
		newPerson.setEmail(email);
		newPerson.setDomain("public");
		newPerson.setGuid(username);
		newPerson.setOrganization(organization);
		newPerson.setIndustry(industry);
		newPerson.setPassword(hashPassword(password1));
		//newPerson.setPassword(password1);


		safeseedDaoService.getPersonService().save(newPerson);



		return new ModelAndView("baseTemplateWide|registerationSuccess",model);

	}



	public void autoLogin(HttpServletRequest request, String username, String password) {
		try {
			// Must be called from request filtered by Spring Security, otherwise SecurityContextHolder is not updated
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
			token.setDetails(new WebAuthenticationDetails(request));
			Authentication authentication = authenticationManager.authenticate(token);
			log.debug("Logging in with {}"+ authentication.getPrincipal());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			SecurityContextHolder.getContext().setAuthentication(null);
			log.error("Failure in autoLogin", e);
		}
	}


	/*
	 * Reviewer login...has no identifying information 
	 * 
	 */
	@RequestMapping(value = "reviewerlogin.html", method = RequestMethod.GET)
	public ModelAndView reviewerlogin(@RequestParam(value = "login_error", required=false) String login_error,
			HttpServletRequest req,
			ModelMap model)
	{

		model.addAttribute("login_error",login_error);
		return new ModelAndView("noFrameTemplate|reviewerlogin",model);

	}


	@RequestMapping(value = "logout.html", method = RequestMethod.GET)
	public ModelAndView logout(
			HttpServletRequest req,
			HttpServletResponse res
			)
	{
		ModelMap model = new ModelMap();

		req.getSession().invalidate();


		return new ModelAndView("redirect:index.html",model);


	}

	public String hashPassword(String plainText)
	{
		//Salt
		//plainText = passwordSalt+plainText;
		MessageDigest mdAlgorithm =null ;
		try {
			mdAlgorithm = MessageDigest.getInstance("MD5");
			mdAlgorithm.update(plainText.getBytes());

			byte[] digest = mdAlgorithm.digest();
			StringBuffer hexString = new StringBuffer();

			for (int i = 0; i < digest.length; i++) {
				plainText = Integer.toHexString(0xFF & digest[i]);

				if (plainText.length() < 2) {
					plainText = "0" + plainText;
				}

				hexString.append(plainText);
			}

			return hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			log.error("error hashing text",e);;
			return null;
		}


	}

}
