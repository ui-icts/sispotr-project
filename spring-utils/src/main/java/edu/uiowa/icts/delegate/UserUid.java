package edu.uiowa.icts.delegate;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.switchuser.SwitchUserFilter;
import org.springframework.security.web.authentication.switchuser.SwitchUserGrantedAuthority;

public class UserUid {

	public static String username(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		String user = "";
		if (auth != null){ 
			user = auth.getName();
		}else{
			user = "anonymousUser";
		}
		return user;
	}
	
	public static String previousUsername(){
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
		return "anonymousUser";
	}
}
