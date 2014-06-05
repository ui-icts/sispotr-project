package edu.uiowa.icts.delegate;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;

public interface DelegateService {
	boolean checkAuthentication(HttpServletRequest request) throws AuthenticationException;
	boolean checkExitAuthentication(HttpServletRequest request) throws AuthenticationException;
}
