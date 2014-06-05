package edu.uiowa.icts.spring.ldap;

import java.util.List;


public interface LDAPService {
	public List getAllPersonNames();
	public List<LDAPPerson> findByUsername(String username);
	  public List<LDAPPerson> getByUsername(String username);
	public List search(String value);
	
	/**
	 * @param firstname
	 * @param lastname
	 * @return
	 */
	public List<LDAPPerson> findByFullname(String firstname, String lastname);
}
