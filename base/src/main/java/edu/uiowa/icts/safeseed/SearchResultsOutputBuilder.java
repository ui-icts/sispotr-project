/**
 * Institute for Clinical and Translation Science (ICTS)
 * University of Iowa
 * 
 * @author Brandyn Kusenda
 * @date Apr 27, 2011
 */
package edu.uiowa.icts.safeseed;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import edu.uiowa.icts.safeseed.domain.SearchResult;


public class SearchResultsOutputBuilder {

	public List<SearchResultContainer> build(Set<SearchResult> results)
	{
		List< SearchResultContainer > outputList = new ArrayList<SearchResultContainer>();
		
		for(SearchResult result: results)
		{
			int offset = result.getResultOffset();
			String resultseq = result.getResultSeq();
			String seq_rc = result.getSearchSeqRc();
			
			
			/*
			 * Stores the comparison info (true if match)
			 */
			Boolean[] comp = new Boolean[seq_rc.length()];
			
			/*
			 * Stores the adjusted seq_rc characters
			 * 
			 */						
			Character[] seq_rc_array = new Character[seq_rc.length()];
			
			/*
			 * Stores adjuected results characters
			 * 
			 */
			Character[] result_array = new Character[seq_rc.length()];
			for(int i=0;i<seq_rc.length();i++)
			{
				
				//do comparison
				comp[i]=false;					
				if(i>=offset && i<seq_rc.length())
				{
					if(resultseq.charAt(i-offset) == seq_rc.charAt(i))
						comp[i]=true;
				}
				
				//get search characters
				if(i<seq_rc.length())
					seq_rc_array[i]=seq_rc.charAt(i);
				else
					seq_rc_array[i]= ' ';
				
				//get result characters
				if(i>=offset)
					result_array[i]=resultseq.charAt(i-offset);
				else
					result_array[i]=' ';
					
				
			}
			SearchResultContainer r = new SearchResultContainer();
			r.setComp(comp);
			r.setResultChars(result_array);
			r.setSearchChars(seq_rc_array);
			r.setSearchResult(result);
			outputList.add(r);
			
			
		}
		return outputList;
	}
}
