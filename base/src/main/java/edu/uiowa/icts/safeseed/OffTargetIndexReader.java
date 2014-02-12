/**
 * University of Iowa
 * Institute for Clinical and Translational Science
 * @author Brandyn Kusenda
 */

package edu.uiowa.icts.safeseed;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import edu.uiowa.icts.safeseed.OffTargetIndexBuilder.IndexInfo;

public class OffTargetIndexReader {

	private static final Log log =LogFactory.getLog(OffTargetIndexReader.class);
	
	/**
	 * 
	 * Runs query against a given index
	 * 
	 * @param indexName : unique name used to identify index
	 * @param queryfield : what column to query
	 * @param query : query string
	 * @param resultfields : what columns to return in results
	 * @param resultMax : max number of results 
	 * @return
	 * @throws IOException
	 */
	public  List<String[]> query(String indexName,String queryfield, String query,String[] resultfields,  int resultMax) throws IOException
	{
		if(OffTargetIndexBuilder.isLoaded()==false)
		{
			log.error("Index builder has not be initialized");
			return null;
		}
		
		List<String[]> resultsList = new ArrayList<String[]>();
		IndexInfo index = OffTargetIndexBuilder.getIndexMap().get(indexName);
		if(index == null)
		{
			log.debug("no such index exists index="+indexName);
			return resultsList;
		}

		File indexDir = new File(index.indexDirName);
		Directory fsDir = FSDirectory.open(indexDir);
		IndexSearcher searcher = new IndexSearcher(fsDir, true);
		StandardAnalyzer analyzer = new StandardAnalyzer(OffTargetIndexBuilder.getVer());
		String querystr = query;
		try {
			Query q = new QueryParser(OffTargetIndexBuilder.getVer(), queryfield, analyzer).parse(querystr);

			TopDocs docs = searcher.search(q,resultMax);
			ScoreDoc[] hits = docs.scoreDocs;

			log.debug("Found " + hits.length + " hits.");

			for(int i=0;i<hits.length;++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				String[] results = new String[resultfields.length];
				//System.out.print(""+i+" : ");
				for(int j=0;j<resultfields.length;j++)
				{
					results[j]=d.get(resultfields[j]);
				//	System.out.print( results[j] + "\t");
				}
				//System.out.println();

				resultsList.add(results);
			}


		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultsList;
	}
	

}
