package edu.uiowa.icts.delegate;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;

/**
 * @author rrlorent
 * Basic implementation of edu.uiowa.icts.delegate.DelegateService.
 * Extend this class if you wish to have more information other than username in the switched session.
 */
public class SwitchUserImpl implements DelegateService {
	
	private static final Log log = LogFactory.getLog(SwitchUserImpl.class);

	public boolean checkAuthentication(HttpServletRequest request) throws AuthenticationException {
		Authentication current_authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<GrantedAuthority> current_authorities = current_authentication.getAuthorities();
		
		for(GrantedAuthority ga : current_authorities){
			if((ga instanceof SwitchUserGrantedAuthority) && (ga.getAuthority().equals(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR))){
				log.debug("user already switched");
				return false;
			}
		}
		
		String user = request.getParameter("j_username");
		if(user != null){
			return true;
		}else{
			return false;
		}
	}

	public boolean checkExitAuthentication(HttpServletRequest request) throws AuthenticationException {
		Authentication current_authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<GrantedAuthority> current_authorities = current_authentication.getAuthorities();
		
		for(GrantedAuthority ga : current_authorities){
			if((ga instanceof SwitchUserGrantedAuthority) && (ga.getAuthority().equals(SwitchUserFilter.ROLE_PREVIOUS_ADMINISTRATOR))){
				SwitchUserGrantedAuthority sw = (SwitchUserGrantedAuthority) ga;
				String user = sw.getSource().getName();
				if(user != null){
					return true;
				}else{
					return false;
				}
			}
		}
		return false;
	}
	
}