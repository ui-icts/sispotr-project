/**
 *  @file: OutputBuilder.java
 *  @date: 3:25:19 PM
 *
 *  @author Brandyn Kusenda, Institute for Clinical and Translational Science, University of Iowa
 *
 */
package edu.uiowa.icts.safeseed;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 */
public class DataTablesProcessor {
	
	
	private static final Log log =LogFactory.getLog(DataTablesProcessor.class);
	
	private int totalResults;
	private List<List<String> > data;
	
	
	
	public DataTablesProcessor(int totalResults, List<List<String>> data) {
		super();
		this.totalResults = totalResults;
		this.data = data;
	
	}

	public int getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

	public List<List<String>> getData() {
		return data;
	}

	public void setData(List<List<String>> data) {
		this.data = data;
	}



	/**
	 * 
	 * 		String only!!
	 * @param headerNames : comma separated list...all sortable
	 * @param req
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject buildTable(String headerNames, HttpServletRequest req) throws JSONException
	{
		List<String> colHeaderNames= new ArrayList<String>();

		List<Boolean> sortableList = new ArrayList<Boolean>();

		List<String> colTypes= new ArrayList<String>();
		List<String> celTemplate= new ArrayList<String>();
		for(String c:headerNames.split(","))
		{
			colHeaderNames.add(c);
			colTypes.add("string");
			sortableList.add(true);
		}
		return buildTable(colHeaderNames,sortableList,colTypes,celTemplate,req);


	}

	/**
	 * 
	 * 		
	 * @param headerNames : comma separated list
	 * @param sortableColumns : comma separated list (true,false)
	 * @param req
	 * @return
	 * @throws JSONException 
	 */
	public JSONObject buildTable(String headerNames, String sortableColumns,String colTypesSt, String cellTemplateSt, HttpServletRequest req) throws JSONException
	{
		List<String> colHeaderNames= new ArrayList<String>();

		List<Boolean> sortableList = new ArrayList<Boolean>();
		List<String> colTypes= new ArrayList<String>();
		List<String> celTemplate= new ArrayList<String>();

		for(String c:headerNames.split(","))
			colHeaderNames.add(c);

		for(String c:sortableColumns.split(","))
			sortableList.add(Boolean.parseBoolean(c));

		for(String c:colTypesSt.split(","))
			colTypes.add(c);

		for(String c:cellTemplateSt.split(","))
			celTemplate.add(c);

		while(colHeaderNames.size() > colTypes.size())
		{
			colTypes.add("string");
		}

		while(colHeaderNames.size() > sortableList.size())
		{
			sortableList.add(true);
		}



		return buildTable(colHeaderNames,sortableList,colTypes,celTemplate,req);


	}

	public JSONObject buildTable(
			List<String> colHeaderNames,
			List<Boolean> sortableList,
			List<String> colTypes,
			List<String> celTemplate,
			HttpServletRequest req
	) throws JSONException 
	{

		Integer firstResult = Integer.parseInt(req.getParameter("iDisplayStart"));
		Integer numResults = Integer.parseInt(req.getParameter("iDisplayLength"));
		Integer sEcho =  Integer.parseInt(req.getParameter("sEcho"));
		String search = req.getParameter("sSearch");
		return buildTable(firstResult,numResults,search,sEcho,colHeaderNames,sortableList,colTypes,celTemplate,req);


	}

	public JSONObject buildTable( 
			Integer firstResult, 
			Integer numResults,
			String search,
			Integer sEcho,
			List<String> colHeaderNames,
			List<Boolean> sortableList,
			List<String> colTypes,
			List<String> celTemplate,
			HttpServletRequest req
	) throws JSONException {

		log.debug("firstResult:"+firstResult);
		log.debug("numResults:"+numResults);
		log.debug("search:"+search);


		if(colHeaderNames.size() != sortableList.size())
		{
			log.error("ERROR: col header and sorable list must be the same length");
			return null;
		}

		List<String[]> orderData = new ArrayList<String[]>();
		for(int i =0;i<colHeaderNames.size();i++)
		{
			String col = req.getParameter("iSortCol_"+i);
			String dir = req.getParameter("sSortDir_"+i);
			log.debug("col:"+col+"  dir:"+dir);
			if(col==null || dir == null)
			{

				log.debug("done preparing orderlist");
				break;
			}
			else
			{
				int colNum = Integer.parseInt(col);
				String colName = colHeaderNames.get(colNum);
				if(sortableList.get(colNum))
					orderData.add(new String[]{colName,dir});
			}

		}

		if(orderData.size()==0)
		{
			for(String s:colHeaderNames)
			{
				if(s!=null)
				{
					orderData.add(new String[]{s,"asc"});
					break;
				}
			}
		}


		return buildTable(sEcho,numResults,firstResult,search,orderData,colHeaderNames,colHeaderNames,colTypes,celTemplate);





	}





	public JSONObject buildTable(int echo, int numResults, int firstResult, String search, List<String[]> orderData,List<String>colHeaderNames,List<String> searchableCols,List<String> colTypes,List<String> celTemplate) throws JSONException
	{
		//Returned value of total
		Long totalCount = 0l;

		
		List<Object[]> pList = new ArrayList<Object[]>();// dao.listSearchPaged(numResults,firstResult,search,orderData,colHeaderNames,searchableCols, colTypes);
		//totalCount = dao.countSearch(search,searchableCols,colTypes);

		JSONObject jsonresults = new JSONObject();
		JSONArray jrows = new JSONArray();

		for(Object[] sList:pList)
		{
			JSONArray jrow = new JSONArray();
			int col=0;
			for(Object s: sList)
			{

				if(celTemplate.size()>=col+1)
				{
					String template=celTemplate.get(col);
					if(template==null || template.isEmpty())
						jrow.put(s);
					else{
						String val = template.replaceAll("\\{val\\}",""+s);
						
						jrow.put(val);
					}
						
				}
				else
					jrow.put(s);
				col++;



			}

			jrows.put(jrow);


		}




		/*
		 * Add to return values
		 * 
		 */


		jsonresults.put("sEcho",echo);
		jsonresults.put("iTotalRecords", totalCount);	

		jsonresults.put("iTotalDisplayRecords", totalCount);		
		jsonresults.put("aaData",jrows);




		return jsonresults;
	}


}
