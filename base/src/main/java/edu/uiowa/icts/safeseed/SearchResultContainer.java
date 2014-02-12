/**
 * Institute for Clinical and Translation Science (ICTS)
 * University of Iowa
 * 
 * @author Brandyn Kusenda
 * @date Apr 27, 2011
 */
package edu.uiowa.icts.safeseed;

import edu.uiowa.icts.safeseed.domain.SearchResult;

public class SearchResultContainer {

	private Boolean[] comp;
	private Character[] searchChars;
	private Character[] resultChars;
	private SearchResult searchResult;
	
	public Boolean[] getComp() {
		return comp;
	}
	public void setComp(Boolean[] comp) {
		this.comp = comp;
	}
	public Character[] getSearchChars() {
		return searchChars;
	}
	public void setSearchChars(Character[] searchChars) {
		this.searchChars = searchChars;
	}
	public Character[] getResultChars() {
		return resultChars;
	}
	public void setResultChars(Character[] resultChars) {
		this.resultChars = resultChars;
	}
	public SearchResult getSearchResult() {
		return searchResult;
	}
	public void setSearchResult(SearchResult searchResult) {
		this.searchResult = searchResult;
	}
	
	public float getMatchAccuracy()
	{
		if(searchChars.length==0)
			return 0.0f;
		
		int count=0;
		for(int i=0;i<searchChars.length;i++)
		{
			if(comp[i])
				count++;
				
		}
		
		return (float)count/(float)searchChars.length;
		
		
	}
	
	public String toString()
	{
		StringBuffer output = new StringBuffer();
		
		for(int i=0;i<searchChars.length;i++)
		{
			output.append(searchChars[i]);
			output.append(resultChars[i]+":");
			output.append(comp[i]+"\n");
			
		}
		
		output.append("Offset          :" + searchResult.getResultOffset()+"\n");
		output.append("Match Accuracy  :" + getMatchAccuracy()+"\n");
		output.append("Accession Id    :" + searchResult.getResultAccession()+"\n");
		output.append("Accession Offset:" + searchResult.getResultAccessionOffset()+"\n");
		output.append("Search String   :"+searchResult.getSearchStatus().getSearchSeq());
		output.append("Search String RC:"+searchResult.getSearchSeqRc());
		output.append("Result String RC:"+searchResult.getResultSeq());
		
		
		return output.toString();
		
		
		
	}
	
	
}
