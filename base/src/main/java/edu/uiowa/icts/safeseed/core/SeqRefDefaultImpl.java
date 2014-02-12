/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.uiowa.icts.safeseed.core;

import edu.uiowa.icts.safeseed.linearscan.LinearScan;
import java.io.Serializable;
import edu.uiowa.icts.safeseed.trie.Trie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Appender;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

/**
 *
 * @author Ray
 * Refactored by Brandyn Kusenda
 */
public class SeqRefDefaultImpl implements  SeqRef, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8285488216786415603L;

	private static final Log log = LogFactory.getLog(SeqRefDefaultImpl.class);


	private String species;
	private String potsFilename = "";				
	private String mirnaFilename = "";	
	private String spsFilename = "";

	private boolean init=false;
	public static boolean DEBUG =true;


	// tab file reader
	//private List<String> headerNumbers = new ArrayList<String>();
	private List<String> headerMIRNA = new ArrayList<String>();
	private LinkedHashMap<String, Double> sevenMerPots = new LinkedHashMap<String, Double>();
	private LinkedHashMap<String, String> sevenMerBestSeq = new LinkedHashMap<String, String>();
	private LinkedHashMap<String, Double> sevenMerBestPots = new LinkedHashMap<String, Double>();
	private LinkedHashMap<String, Double> sevenMerPotsPercentiles = new LinkedHashMap<String, Double>();
	private LinkedHashMap<String, Double> spsMap = new LinkedHashMap<String, Double>();

	// xmer = conserved
	private LinkedHashMap<String, String[]> sevenMerMatch = new LinkedHashMap<String, String[]>();
	private LinkedHashMap<String, String[]> twoSevenMerMatch = new LinkedHashMap<String, String[]>();
	private LinkedHashMap<String, String[]> threeEightMerMatch = new LinkedHashMap<String, String[]>();


	public SeqRefDefaultImpl(String species, String potsFilename, String mirnaFilename, String spsFilename) 
	{
		this.species =species; 
		this.potsFilename=potsFilename;
		this.mirnaFilename=mirnaFilename;
		this.spsFilename = spsFilename;


		init();
	}

	public SeqRefDefaultImpl(Properties props) 
	{
		this.species =props.getProperty("species"); 
		this.potsFilename=props.getProperty("pots.filename");
		this.mirnaFilename=props.getProperty("mirna.filename");
		this.mirnaFilename=props.getProperty("sps.filename");


		init();

	}

	public SeqRefDefaultImpl()
	{

	}


	@Override
	public void init()
	{
		//Load in files
		if(init==false)
		{
			try
			{

				readTabFileNumbers(potsFilename);
				readTabSPS(spsFilename);

				if(mirnaFilename!=null)
					readTabFileMIRNA(mirnaFilename);
				SeqRefFactory.add(this);
				init=true;
				logStatus();

			}
			catch ( Exception e)
			{
				log.error("Failed to load files:",e);

			}

		}





	}



	/**
	 * Reads in the 7mer POTS file
	 * NO HEADER IN THIS FILE
	 * only two columns
	 * modified 9/8/2011 by Brandyn Kusenda
	 * @param fileName
	 * @throws IOException
	 */
	private void readTabFileNumbers(String fileName) throws IOException {
		// create a new tab file reader object
		InputStream in = SeqRef.class.getClassLoader().getResourceAsStream(fileName);
		if(in == null){

			try{
				in = new FileInputStream(fileName);
			} catch (IOException e){

				log.error("error could not find:"+fileName,e);
				throw e;

			}

		}

		BufferedReader fin = null;

		fin = new BufferedReader(new InputStreamReader(in));


		String line = null;

		int linenum=0;
		//read each line of text file
		while ((line = fin.readLine()) != null) 
		{
			String[] s = line.split("\t");
			//logger.debug("line:"+line);

			/*
			 * add entry to hashmap
			 */
			try
			{
				sevenMerPots.put(s[0], Double.parseDouble(s[1]));		// 7mer = POTS
			}
			catch(NumberFormatException ne)
			{
				log.error("could not parse pot score from line + "+linenum+" "+ s[1]+"...skipping");

			}


			linenum++;

		}

		// compute percentiles: sort in descending order and divide each by the n
		LinkedHashMap<String, Double> temp = new LinkedHashMap<String, Double>();
		temp.putAll(this.sortLHMDesc(sevenMerPots));
		Iterator<String> it = temp.keySet().iterator();
		int pos = 0;
		while(it.hasNext()){
			String key = it.next();
			Double value = (double) pos / (double) temp.size();
			sevenMerPotsPercentiles.put(key, value);
			pos++;
		}
	}

	/**
	 * 
	 * miRNA	seed+m8	nt 2-7	nt 3-8	conserved
	 * @param fileName
	 * @throws IOException
	 */
	private void readTabFileMIRNA(String fileName) throws IOException {
		if(DEBUG)
			log.debug("loading file:"+fileName);
		// create a new tab file reader object
		InputStream in = SeqRef.class.getClassLoader().getResourceAsStream(fileName);
		if(in == null){

			try{
				in = new FileInputStream(fileName);
			} catch (IOException e){

				log.error("error could not find:"+fileName,e);
				throw e;

			}

		}
		//File file = new File(in.toString());
		BufferedReader fin = null;
		//try {
		//fin = new BufferedReader(new FileReader(file));
		fin = new BufferedReader(new InputStreamReader(in));
		//} catch (FileNotFoundException e) {
		//    logger.error("File " + fileName + " not found.", e);
		//}

		String line = null;
		boolean headerLine = true;

		//read each line of text file
		while ((line = fin.readLine()) != null) {
			String[] s = line.split("\t");
			if(headerLine){
				this.headerMIRNA.add(s[0]);
				this.headerMIRNA.add(s[1]);
				this.headerMIRNA.add(s[2]);
				this.headerMIRNA.add(s[3]);
				this.headerMIRNA.add(s[4]);
			} else {
				String[] mer = {s[0], s[4]};
				this.sevenMerMatch.put(s[1], mer);
				this.twoSevenMerMatch.put(s[2], mer);
				this.threeEightMerMatch.put(s[3], mer);
				//this.sevenMerBestSeq.put(s[0], s[4]);						// 7mer = Best seq
				//	this.sevenMerBestPots.put(s[0], Double.parseDouble(s[5]));	// 7mer = Best POTS
			}
			if (headerLine) {
				headerLine = false;
			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	/**
	 * 
	 * seedsps file
	 * @param fileName
	 * @throws IOException
	 */
	private void readTabSPS(String fileName) throws IOException {
		if(DEBUG)
			log.debug("loading file:"+fileName);
		// create a new tab file reader object
		InputStream in = SeqRef.class.getClassLoader().getResourceAsStream(fileName);
		if(in == null){

			try{
				in = new FileInputStream(fileName);
			} catch (IOException e){

				log.error("error could not find:"+fileName,e);
				throw e;

			}

		}
		BufferedReader fin = null;
		fin = new BufferedReader(new InputStreamReader(in));
		String line = null;
		boolean headerLine = true;
		//read each line of text file
		while ((line = fin.readLine()) != null) {
			String[] s = line.split("\t");
			if(!headerLine){
				spsMap.put(s[0], Double.parseDouble(s[1]));
			}
				headerLine = false;
		}
	}
	
	
	
	
	

	/**
	 * Sort a LinkedHashMap
	 * @param pf
	 * @return
	 */
	private LinkedHashMap<String, Double> sortLHMDesc(LinkedHashMap pf) {
		List list = new LinkedList(pf.entrySet());
		Collections.sort(list, new Comparator() {

			// 2 compare 1 is descending order
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o2)).getValue()).compareTo(((Map.Entry) (o1)).getValue());
			}
		});

		LinkedHashMap result = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	@Override
	public Double get7MerPots(String sevenMer) {
		if(sevenMerPots.get(sevenMer)==null){
			log.debug("7merpots=null..returning 1000");
			return 1000.00;
		}
		return sevenMerPots.get(sevenMer);
	}

	@Override
	public Double get7MerBestPots(String sevenMer) {
		if(sevenMerBestPots.get(sevenMer)==null){
			log.debug("7merbestpots=null...returning 7merpots");
			return get7MerPots(sevenMer);

		}
		return sevenMerBestPots.get(sevenMer);

	}

	@Override
	public String get7MerBestSeq(String sevenMer) {
		return sevenMerBestSeq.get(sevenMer);
	}

	@Override
	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	@Override
	public String[] get7MerMatch(String sevenMer) {
		return sevenMerMatch.get(sevenMer);

	}

	@Override
	public Double get7MerPotsPercentiles(String sevenMer)
	{

		return sevenMerPotsPercentiles.get(sevenMer);
	}


	public String getPotsFilename() {
		return potsFilename;
	}

	public void setPotsFilename(String potsFilename) {
		this.potsFilename = potsFilename;
	}

	public String getMirnaFilename() {
		return mirnaFilename;
	}

	public void setMirnaFilename(String mirnaFilename) {
		this.mirnaFilename = mirnaFilename;
	}

	public Double getSps(String seed) {
		return spsMap.get(seed);
	}

	

	public String getSpsFilename() {
		return spsFilename;
	}

	public void setSpsFilename(String spsFilename) {
		this.spsFilename = spsFilename;
	}

	/* (non-Javadoc)
	 * @see edu.uiowa.icts.safeseed.core.SeqRef#status()
	 */
	@Override
	public void logStatus() {
		if(DEBUG){
			if(init)
				log.debug("SeqRef Initialized");
			else
				log.debug("SeqRef no initialized");
			log.debug("   # of pots scores:   "+sevenMerPots.size());
			log.debug("   # of match results: "+sevenMerMatch.size());
		}

	}

}
