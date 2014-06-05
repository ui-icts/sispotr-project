package edu.uiowa.icts.spring.ldap;

import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.WhitespaceWildcardsFilter;
import org.springframework.stereotype.Repository;



@Repository
public class LDAPHome implements LDAPService {
	private static final Log log = LogFactory.getLog(LDAPHome.class);   
	
	@Autowired
	private LdapTemplate ldapTemplate;

	   public void setLdapTemplate(LdapTemplate ldapTemplate) {
	      this.ldapTemplate = ldapTemplate;
	   }

	   
	   public List<LDAPPerson> findByUsername(String username) {
		   log.debug("Username: " + username);
//		   AndFilter filter = new AndFilter();
//		   filter.and(new EqualsFilter("objectClass","organizationalPerson"));
//		   filter.and(new WhitespaceWildcardsFilter("cn",username));
		   
		   //String filter = "(&(objectClass=organizationalPerson)(|(displayName=*"+username+"*)(userPrincalName="+username+"*)))";
		   String filter = "(cn="+username+"*)";
		   log.debug("Filter: " + filter);
		   return ldapTemplate.search("", filter, getContextMapper());
		   
		   
	   }
	   
	   public List<LDAPPerson> getByUsername(String username) {
		   log.debug("Username: " + username);
//		   AndFilter filter = new AndFilter();
//		   filter.and(new EqualsFilter("objectClass","organizationalPerson"));
//		   filter.and(new WhitespaceWildcardsFilter("cn",username));
		   
		   //String filter = "(&(objectClass=organizationalPerson)(|(displayName=*"+username+"*)(userPrincalName="+username+"*)))";
		   String filter = "(cn="+username+")";
		   log.debug("Filter: " + filter);
		   return ldapTemplate.search("", filter, getContextMapper());
		   
		   
	   }
	   
	   public List<LDAPPerson> findByFullname(String firstname,String lastname) {
		   log.debug("firstname: " + firstname);
		   log.debug("lastname: " + lastname);
//		   AndFilter filter = new AndFilter();
//		   filter.and(new EqualsFilter("objectClass","organizationalPerson"));
//		   filter.and(new WhitespaceWildcardsFilter("cn",username));
		   
		   //String filter = "(&(objectClass=organizationalPerson)(|(displayName=*"+username+"*)(userPrincalName="+username+"*)))";
		   String filter = "(&(givenName="+firstname+"*)(sn="+lastname+"*))";
		   
		   
		   log.debug("Filter: " + filter);
		   return ldapTemplate.search("", filter, getContextMapper());
		   
		   
	   }

		public List search(String value) {
			   log.debug("Starting Search");
			   //Attributes attrs = new AttributesMapper();
			   
			   
			   List list = ldapTemplate.search(
		         "", "(cn="+value+"*)",
		         new AttributesMapper() {
		            public Object mapFromAttributes(Attributes attrs)
		               throws NamingException {
		               return attrs.get("DN").get();
		            }
		         });
			   
			   log.debug("List Size: " + list.size());
			   return list;
		}

	   
	public List getAllPersonNames() {
		   log.debug("Starting Search");
		   //Attributes attrs = new AttributesMapper();
		   
		   
		   List list = ldapTemplate.search(
	         "", "(cn=*)",
	         new AttributesMapper() {
	            public Object mapFromAttributes(Attributes attrs)
	               throws NamingException {
	               return attrs.get("DN").get();
	            }
	         });
		   
		   log.debug("List Size: " + list.size());
		   return list;
	}
	
	
	   protected ContextMapper getContextMapper() {
		      return new PersonContextMapper();
		   }

	
	
	
	
	private static class PersonContextMapper implements ContextMapper {
		   public Object mapFromContext(Object ctx) {
		      DirContextAdapter context = (DirContextAdapter)ctx;
		      LDAPPerson p = new LDAPPerson();
		      p.setFullName(context.getStringAttribute("displayName"));
		      p.setLastName(context.getStringAttribute("sn"));
		      //p.setDescription(context.getStringAttribute("description"));
		      // The roleNames property of Person is an String array
		      //p.setRoleNames(context.getStringAttributes("roleNames"));
		      p.setTitle(context.getStringAttribute("title"));
		      //p.setCompany(context.getStringAttribute("company"));
		      p.setDepartment(context.getStringAttribute("company") + " " 
		    		  + context.getStringAttribute("department"));
		      p.setTelephoneNumber(context.getStringAttribute("uiowaOfficePhone"));
		      //p.setDn(context.getStringAttribute("distinguishedName"));
		      p.setMail(context.getStringAttribute("mail"));
		      p.setGivenName(context.getStringAttribute("givenName"));
		      p.setUsername(context.getStringAttribute("cn"));
		      
		      
		      return p;
		   }
		}

}
