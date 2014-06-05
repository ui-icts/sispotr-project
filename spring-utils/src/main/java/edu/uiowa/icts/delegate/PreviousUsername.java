package edu.uiowa.icts.delegate;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.SessionContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;

@SuppressWarnings("serial")
public class PreviousUsername extends TagSupport{

	private static final Log log = LogFactory.getLog(PreviousUsername.class);

	public int doStartTag() throws JspException {
		try {
			log.debug(pageContext == null);
			pageContext.getOut().write(username());
		} catch (IOException e) {
			log.error("problem writing username",e);
		}
		return EVAL_PAGE;
	}

	public String username(){
		try{
			Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
			if(currentAuth != null){
				Collection<GrantedAuthority> currentAuthorities = currentAuth.getAuthorities();
				for(GrantedAuthority ga : currentAuthorities){
					if((ga instanceof SwitchUserGrantedAuthority) && (ga.getAuthority().equals(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR))){
						return ((SwitchUserGrantedAuthority) ga).getSource().getName();
					}
				}
				return currentAuth.getName();
			}
		}catch (Exception e){
			log.error("oops",e);
		}
		return "anonymousUser";
	}
}