/**
 * University of Iowa
 * Institute for Clinical and Translational Science
 * @author Brandyn Kusenda
 */

package edu.uiowa.icts.safeseed;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import edu.uiowa.icts.service.PropertyLoader;

public class OffTargetIndexBuilder {


	private static final Log log =LogFactory.getLog(OffTargetIndexBuilder.class);
	
	private static String propFileName = "offtargetIndex.properties";

	/*
	 * Stores loaded index information to quickly retrieve 
	 * existing indexes without reading from config files
	 * 
	 */
	private static Map<String,IndexInfo> indexMap = new ConcurrentHashMap<String,IndexInfo>();
	private static boolean loaded=false;

	private static Version ver =Version.LUCENE_33;


	/*
	 * Loads index settings from properties file 
	 * 
	 */
	public OffTargetIndexBuilder()
	{


	}
	
	
	/*
	 * Loads all index settings from properties file, does not build
	 * 
	 */
	public synchronized void init()
	{

		
		Properties props = PropertyLoader.loadProperties(propFileName);

		int indexCount = Integer.parseInt(props.getProperty("indexCount","0"));
		
		log.debug("count:"+indexCount);

		for(int i = 0;i<indexCount;i++)
		{

		
			String name = props.getProperty("index"+i+"Name");
			String importFileName = props.getProperty("index"+i+"ImportFile");
			String dirName = props.getProperty("index"+i+"Dir");
			log.debug("name:"+name);
			log.debug("importFileName:"+importFileName);
			log.debug("dirName:"+dirName);
			try {
				loadIndex(name,importFileName,dirName);
			} catch (IOException e) {
				log.error("io exception when building index",e);
			}


		}
		loaded = true;
		

	}
	
	
	
	/*
	 * Builds each index (if doesn't exist), then loads it
	 * 
	 */
	public synchronized void buildAllIndexes()
	{

		Properties props = PropertyLoader.loadProperties(propFileName);

		int indexCount = Integer.parseInt(props.getProperty("indexCount","0"));
		
		log.debug("count:"+indexCount);

		for(int i = 0;i<indexCount;i++)
		{

		
			
			String name = props.getProperty("index"+i+"Name");
			String importFileName = props.getProperty("index"+i+"ImportFile");
			String dirName = props.getProperty("index"+i+"Dir");
			log.debug("name:"+name);
			log.debug("importFileName:"+importFileName);
			log.debug("dirName:"+dirName);
			try {
				if(indexMap.containsKey(name)==false)
					loadAndBuildIndex(name,importFileName,dirName);
			} catch (IOException e) {
				log.error("io exception when building index",e);
			}


		}

	}
	
	/*
	 * builds index even if does NOT exist
	 * 
	 */
	
	public void loadAndBuildIndex(String indexName, String importFilename, String indexDirName) throws IOException
	{
		IndexInfo index = new IndexInfo(indexName,importFilename,indexDirName,false);

		File indexDir = new File(indexDirName);
		Directory fsDir = FSDirectory.open(indexDir);
		index.created= IndexReader.indexExists(fsDir);

		if(index.created==false)
		{
			buildIndex(index);
		}
		index.created= IndexReader.indexExists(fsDir);
		indexMap.put(indexName, index);
	}
	

	/*
	 * builds index even if does NOT exist
	 * 
	 */
	
	public synchronized void loadIndex(String indexName, String importFilename, String indexDirName) throws IOException
	{
		IndexInfo index = new IndexInfo(indexName,importFilename,indexDirName,false);

		File indexDir = new File(indexDirName);
		Directory fsDir = FSDirectory.open(indexDir);
		index.created= IndexReader.indexExists(fsDir);

		if(index.created==true)
		{
			
		index.created= IndexReader.indexExists(fsDir);
		indexMap.put(indexName, index);
		
		}
		else
			log.error("Error Index does NOT exist...not adding to indexMap");
	}

	/*
	 * force builds index even if exists
	 * 
	 */
	public synchronized void forceBuildIndex(String indexName, String importFilename, String indexDirName) throws IOException
	{
		IndexInfo index = new IndexInfo(indexName,importFilename,indexDirName,false);
		buildIndex(index);
		index.created= true;
		indexMap.put(indexName, index);
	}


	/**
	 * Primary function for building index
	 * TODO: could be generalized more
	 * @param index : object that stores index info
	 * @throws IOException
	 */
	public synchronized void buildIndex(IndexInfo index) throws IOException
	{
		String importfilename = index.importFileName;
		String indexDirName = index.indexDirName;
		StandardAnalyzer analyzer = new StandardAnalyzer(ver);

		//index location
		File indexDir = new File(indexDirName);


		Directory fsDir = FSDirectory.open(indexDir);

		IndexWriterConfig indconfig = new IndexWriterConfig(ver,analyzer);
		IndexWriter indexWriter = new IndexWriter(fsDir, indconfig);


		//read input text file
		FileInputStream fstream = new FileInputStream(importfilename);
		DataInputStream in = new DataInputStream(fstream);
		BufferedReader br = new BufferedReader(new InputStreamReader(in));

		/*
		 * Split each line into cols and add to index
		 */
		int count=0;
		String line = "";
		while((line = br.readLine()) !=null)
		{



			String[] cols = line.split("\t");

			if(cols.length !=7)
				continue;

			if(count %100000==0)
				log.debug("indexing:"+line);
			Document doc = new Document();
			
			/*
			 * 
			 * These fields could be extracted and provided as paramters
			 */
			doc.add(new Field("tpots",""+cols[0],Field.Store.YES,Field.Index.NO));
			doc.add(new Field("seed",""+cols[1],Field.Store.YES,Field.Index.ANALYZED));
			doc.add(new Field("geneid",""+cols[2],Field.Store.YES	,Field.Index.ANALYZED));
			doc.add(new Field("8mer",""+cols[3],Field.Store.YES,Field.Index.NO));
			doc.add(new Field("7merm8",""+cols[4],Field.Store.YES,Field.Index.NO));
			doc.add(new Field("7merm1a",""+cols[5],Field.Store.YES,Field.Index.NO));
			doc.add(new Field("6mer",""+cols[6],Field.Store.YES,Field.Index.NO));


			indexWriter.addDocument(doc);
			count ++;


		}
		indexWriter.optimize();
		indexWriter.close();

		log.debug("done indexing");
	}

	

	public static String getPropFileName() {
		return propFileName;
	}


	public static void setPropFileName(String propFileName) {
		OffTargetIndexBuilder.propFileName = propFileName;
	}


	public static Map<String, IndexInfo> getIndexMap() {
		return indexMap;
	}


	public static void setIndexMap(Map<String, IndexInfo> indexMap) {
		OffTargetIndexBuilder.indexMap = indexMap;
	}


	public static Version getVer() {
		return ver;
	}


	public static void setVer(Version ver) {
		OffTargetIndexBuilder.ver = ver;
	}



	public static boolean isLoaded() {
		return loaded;
	}


	public static void setLoaded(boolean loaded) {
		OffTargetIndexBuilder.loaded = loaded;
	}



	/*
	 * Class to store indexInfo in static map of loaded indexes
	 */
 	 public class IndexInfo
	{

		String indexName;
		String importFileName;
		String indexDirName;
		boolean created;

		public IndexInfo(String indexName, String importFileName,String indexDirName, boolean created) {
			super();
			this.indexName = indexName;
			this.importFileName = importFileName;
			this.indexDirName = indexDirName;
			this.created = created;
		}

		public String getIndexName() {
			return indexName;
		}

		public void setIndexName(String indexName) {
			this.indexName = indexName;
		}

		public String getImportFileName() {
			return importFileName;
		}

		public void setImportFileName(String importFileName) {
			this.importFileName = importFileName;
		}

		public String getIndexDirName() {
			return indexDirName;
		}

		public void setIndexDirName(String indexDirName) {
			this.indexDirName = indexDirName;
		}

		public boolean isCreated() {
			return created;
		}

		public void setCreated(boolean created) {
			this.created = created;
		}



	}
}
