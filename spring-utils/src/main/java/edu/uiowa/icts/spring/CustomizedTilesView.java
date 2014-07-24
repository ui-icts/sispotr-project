package edu.uiowa.icts.spring;

import java.io.File;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tiles.Attribute;
import org.apache.tiles.AttributeContext;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.TilesException;
import org.apache.tiles.servlet.context.ServletUtil;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.util.WebUtils;

import edu.uiowa.icts.exception.MappingNotFoundException;


/**
 * @author bkusenda
 *
 * CustomizedTilesView.java

Usage: Define a default template with the name "mainTemplate" as well as any other templates you want

A view can be referenced in three ways:
1. Use a defined template name. eg "myTemplate"
2. Use the default template and specify only the path to the jsp file eg "path/to/myfile"
3. Define both the template name and the path to the file using a "|" as a delimiter eg "myTemplate|path/to/myfile"

NOTE: the view will first check if a tiles view is defined

Generated JSPs support: 
If the specified file does not exist in "/WEB-INF/jsp", 
the "/WEB-INF/jsp-generated" will be checked for a file with the same relative path

See README.txt for view configuration
 *
 *
 */
public class CustomizedTilesView extends AbstractUrlBasedView {


	private String tilesDefinitionName = "mainTemplate";
	private String tilesBodyAttributeName = "body";
	private String tilesDefinitionDelimiter = "\\|";
	private boolean fallbackToGeneratedJSP = true;

	protected void renderMergedOutputModel(
			Map<String,Object> model,
			HttpServletRequest request, 
			HttpServletResponse response)
	throws Exception {

		ServletContext servletContext = getServletContext();
		TilesContainer container =  ServletUtil.getContainer(servletContext);
		if (container == null) {	
			throw new ServletException("Tiles container is not initialized. " +    
			"Have you added a TilesConfigurer to your web application context?");
		}

		exposeModelAsRequestAttributes(model, request);
		String defName=tilesDefinitionName;
		String inputString=getBeanName();
		String path=null;


		if (!response.isCommitted()) {
			// Tiles is going to use a forward, but some web containers (e.g.
			// OC4J 10.1.3)
			// do not properly expose the Servlet 2.4 forward request
			// attributes... However,
			// must not do this on Servlet 2.5 or above, mainly for GlassFish
			// compatibility.
			if (servletContext.getMajorVersion() == 2 && servletContext.getMinorVersion() < 5) {
				WebUtils.exposeForwardRequestAttributes(request);
			}
		}

		if(container.isValidDefinition(inputString, request, response))
		{
			logger.debug("Provided input string maps to a valid tiles definition");			
			defName=inputString;
		}
		else
		{
			/*
			 * Split the provided string on the tilesDefinitionDelimiter 
			 * into the filepath (path) and definition (defName). 
			 * 
			 * 
			 */
			String[] inputArray = inputString.split(tilesDefinitionDelimiter);
			if(inputArray.length>1) {

				defName= inputArray[0];

				if (!container.isValidDefinition(defName, request, response)) {
					throw new TilesException("No definition for '" + defName +"' in tiles template definitions" );
				}
				
				path = getUrl().replaceFirst(defName+tilesDefinitionDelimiter, "");
				logger.debug("Using specified template "+defName+ " with body "+path);
				if (path.contains("//")) {
				//	logger.info("1 matched: " + path);
					path = path.replace("//", "/");
					//logger.info("new path: " + path);
				} 

			}
			/*
			 * If no definition is specified, use the default definition 
			 * 
			 */
			else {
				path=getUrl();
       				if (path.contains("//")) {
                                //      logger.info("1 matched: " + path);
                                        path = path.replace("//", "/");
                                        //logger.info("new path: " + path);
                                }
				logger.debug("Using default template "+defName+ " with body "+path);
			}
			
			if(fallbackToGeneratedJSP) {
				logger.debug("Use fall back, enabled");
				String root = servletContext.getRealPath("/");
				File f = new File(root+path);

				if (!f.exists()) {
					logger.debug("File does not exist, checking for generated");
					String temppath = path.replaceFirst("/WEB-INF/jsp", "/WEB-INF/jsp-generated");
					f = new File(root+temppath);

					if (f.exists()) {
						logger.debug("Generated file exists, using as fall back");
						path = temppath;
					} else {
						logger.error("File not found, sending 404");
						request.setAttribute("javax.servlet.error.request_uri", path);
						request.setAttribute("javax.servlet.error.exception", new MappingNotFoundException("Mapping not found "+path));
						return;
					}
				}
			} else {
				logger.debug("File Exists");
			}

			//create tiles body attribute  
			Attribute attr = new Attribute();
			attr.setValue(path);
			AttributeContext attributeContext = container.startContext(request, response);
			attributeContext.putAttribute(tilesBodyAttributeName, attr);
			
		}
		
		container.render(defName, request, response);
		container.endContext(request, response);
		
	}
}
