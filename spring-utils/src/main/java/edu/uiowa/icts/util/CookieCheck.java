package edu.uiowa.icts.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class CookieCheck extends BodyTagSupport {

	private static final long serialVersionUID = 3786884329856237339L;
	
	static Log log = LogFactory.getLog(CookieCheck.class);
	
	private String cookieName = null;
	
	public int doStartTag(){
		
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		Cookie[] cookies = request.getCookies();
		
		String value = ServletUtilities.getCookieValue(cookies,cookieName,null);
		
		log.debug(value);
		
		if(value != null){
			
		}else{
			
		}
		
		return SKIP_BODY;
	}
	
	public int doAfterBody(){
		clear();
		return SKIP_BODY;
	}
	
	public int doEndTag() throws JspException {
		return super.doEndTag();
	}
	
	private void clear() {
		this.cookieName = null;
	}

	public String getCookieName() {
		return cookieName;
	}

	public void setCookieName(String cookieName) {
		this.cookieName = cookieName;
	}
	
}