/**
 * Institute for Clinical and Translation Science (ICTS)
 * University of Iowa
 * 
 * @author bkusenda
 * @date Jan 5, 2011
 */
package edu.uiowa.icts.spring;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.orm.hibernate3.annotation.AnnotationSessionFactoryBean;

/**
 * @author bkusenda
 *
 */
public class HsqlAnnotationSessionFactoryBean extends AnnotationSessionFactoryBean
{
	
	
	private static final Log log =LogFactory.getLog(HsqlAnnotationSessionFactoryBean.class);
	private List<String> schemas;
	private String url="jdbc:hsqldb:file:testdb";
	
	
	public void create()
	{
		log.debug("Creating schemas :"+schemas.size());
			
	  	      
	         try {
				Class.forName("org.hsqldb.jdbcDriver" );

	         Connection conn = DriverManager.getConnection(url,"sa","");
	         for(String schema: schemas)
	         {
	         Statement st = conn.createStatement();    // statements
	         String expression = "CREATE SCHEMA "+schema+" AUTHORIZATION DBA";
	         st.executeUpdate(expression);    // run the query
	           st.close();
	         }
	         //Statement st = conn.createStatement();    // statements
	         //st.execute("SHUTDOWN");
	         //st.close();
	         
	          conn.close();
	           
				} catch (Exception e) {
					// TODO Auto-generated catch block
					log.debug("Exception occured");
					e.printStackTrace();
				}
	         
	}


	public List<String> getSchemas() {
		return schemas;
	}


	public void setSchemas(List<String> schemas) {
		this.schemas = schemas;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}
	
	

}
