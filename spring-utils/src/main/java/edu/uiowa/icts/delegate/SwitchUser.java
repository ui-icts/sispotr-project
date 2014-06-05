package edu.uiowa.icts.delegate;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;

public class SwitchUser extends SwitchUserFilter {
	
	@Autowired
	private DelegateService delegateService;
	
	@Override
	public Authentication attemptSwitchUser(HttpServletRequest request) throws AuthenticationException {
		if(delegateService.checkAuthentication(request))
			return super.attemptSwitchUser(request);
		return SecurityContextHolder.getContext().getAuthentication();
	}
	
	@Override
	public Authentication attemptExitUser(HttpServletRequest request) throws AuthenticationException {
		if(delegateService.checkExitAuthentication(request))
			return super.attemptExitUser(request);
		return SecurityContextHolder.getContext().getAuthentication();
	}
}
