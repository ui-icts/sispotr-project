/*
 * 
 * @author Brandyn Kusenda, Institute for Clinical and Translational Science, University of Iowa
 */
package edu.uiowa.icts.safeseed.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SeqManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7021682755462563L;

	private static final Log log =LogFactory.getLog(SeqManager.class);
	private LinkedHashMap<String,Seq> seqMap;

	public SeqManager()
	{
		seqMap= new LinkedHashMap<String,Seq>();
	}

	public List<Xmers> getShrnaXmerList(){
		List<Xmers> xmerList = new ArrayList<Xmers>();
		for(Seq seq :seqMap.values()){
			for(Xmers x:seq.getXmersList()){
				if(x.getXmerType() == XmerType.SHRNA){
					xmerList.add(x);
				}
			}
		}
		return xmerList;
	}


	public List<Xmers> getSirnaXmerList(){
		List<Xmers> xmerList = new ArrayList<Xmers>();
		for(Seq seq :seqMap.values()){
			for(Xmers x:seq.getXmersList()){
				if(x.getXmerType() == XmerType.SIRNA){
					xmerList.add(x);
				}
			}
		}
		return xmerList;
	}

	public void addSeq(File f,XmersParams p) throws FileNotFoundException, IOException
	{
		SeqParser loader = new SeqParser(f);
		parseSeq(loader);
	}

	public void addSeq(File f,String species, Integer length,XmersParams params) throws FileNotFoundException, IOException
	{
		SeqParser parser = new SeqParser(f);
		List<Seq> sList = parseSeq(parser,species);
		for(Seq s: sList)
		{
			s.computeXmers(length, params);
		}
	}


	public List<String> addSeq(String st,String species, Integer length,XmersParams params) throws IOException
	{
		List<String>  seqNameList = new ArrayList<String>();
		SeqParser parser = new SeqParser(st);
		List<Seq> sList = parseSeq(parser,species);
		for(Seq s: sList)
		{
			seqNameList.add(s.getLabel());
			s.computeXmers(length, params);
		}

		return seqNameList;

	}

	public void addSeq(Seq s,Integer length,XmersParams params) throws IOException{

		seqMap.put(s.getLabel(),s);
		s.computeXmers(length,  params);

	}



	/*
	 * load into manager
	 */

	private List<Seq>  parseSeq(SeqParser parser) throws IOException
	{


		return parseSeq(parser,"human");
	}

	private List<Seq> parseSeq(SeqParser parser,String species) throws IOException
	{

		List<Seq> sList = new ArrayList<Seq>();
		for(Seq s: parser.getSeqList())
		{
			s.setSpecies(species);
			boolean added =false;
			int i = 1;
			while(added==false){
				if(seqMap.containsKey(s.getLabel())==false){
					seqMap.put(s.getLabel(), s);
					added = true;
					sList.add(s);

				}
				else{
					s.setLabel(s.getLabel()+i);
					i++;
				}

			}
		}
		parser.clear();

		return sList;
	}

	public void computeSeqXmers(Integer length ,XmersParams params)
	{
		for(Seq s: seqMap.values())
		{
			s.computeXmers(length,params);

		}
	}

	public void computeSeqXmers(String species, Integer length ,XmersParams params)
	{
		for(Seq s: seqMap.values())
		{
			if(s.getSpecies().equalsIgnoreCase("species"))
				s.computeXmers(length,params);

		}
	}

	public Collection<Seq> getSeqs()
	{
		return seqMap.values();
	}

	public Seq getSeq(String id)
	{
		return seqMap.get(id);
	}

	public void printSeqLog()
	{
		int i = 0;
		for(Seq s: seqMap.values())
		{
			log.debug(""+i+": " + s.toString());
			i++;
		}

	}

}
