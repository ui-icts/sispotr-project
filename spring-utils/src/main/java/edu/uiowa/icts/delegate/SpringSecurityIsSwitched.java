package edu.uiowa.icts.delegate;

import java.util.Collection;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;

@SuppressWarnings("serial")
public class SpringSecurityIsSwitched extends TagSupport {
	public int doStartTag() throws JspException {
		Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
		Collection<GrantedAuthority> currentAuthorities = currentAuth.getAuthorities();
		for(GrantedAuthority ga : currentAuthorities){
			if((ga instanceof SwitchUserGrantedAuthority) && (ga.getAuthority().equals(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR))){
				return EVAL_BODY_INCLUDE;
			}
		}
		return SKIP_BODY;
	}
}