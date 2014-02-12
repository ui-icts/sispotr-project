package edu.uiowa.icts.safeseed.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.common.io.Files;

public class SeqParser {

	private static final Log log =LogFactory.getLog(SeqParser.class);
	public static boolean DEBUG = true;

	private List<Seq> seqList;


	public SeqParser(File file) throws FileNotFoundException, IOException
	{
		seqList = new ArrayList<Seq>();

		read(file);
	}

	public SeqParser(String st) throws IOException
	{
		seqList = new ArrayList<Seq>();

		read(st);
	}
	/**
	 * Copy file text to 'seqText' string variable
	 * @param fileName
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void read(File file) throws FileNotFoundException, IOException {

		//		File file = new File("output.rdf");
		//		String filetext = Files.toString(file,  Charset.forName("UTF-8"));


		String filetext = Files.toString(file,  Charset.forName("UTF-8"));


		read(filetext);

	}

	/**
	 * Reads in sequence input string
	 * 
	 */
	private void read(String sequenceText) throws  IOException {

		if(DEBUG)
			log.debug("reading sequence data");
		String s;
		String sh = null, a = null;
		BufferedReader br = new BufferedReader(new StringReader(sequenceText));
		StringBuffer seq = new StringBuffer();
		if(DEBUG)
			log.debug("before seq="+sequenceText);
		boolean first = true;
		int count =0;
		Seq currentSeq =null;
		while ((s = br.readLine()) != null) {
			if(DEBUG)
				log.info("Length of s:"+s.length());
			/*
			 * if line starts with '>'
			 */
			if (s!=null && s.length() >0 && s.charAt(0) == '>') 
			{
				/*
				 * If first, currentSeq is empty
				 */
				if (first) {
					first = false;
				} else {
					//	this.seqHeaders.put(a, sh);
					//	this.sequences.put(a, seq.toString());
					currentSeq.setSeqString(seq.toString());
					seqList.add(currentSeq);

					sh = a = "";


					seq.setLength(0);			// clear the string buffer
				}
				currentSeq = new Seq();
				sh = s;
				a="";
				s=s.replaceFirst("\\>", "");
				String[] t = s.split("\\|");
				if(t.length==1)
					t = s.split("\\\\");
				if(t.length==1)
					t = s.split("\\/");
				if(t.length==1)
					t = s.split(",");


				if(DEBUG)
					log.debug("header length:"+t.length);
				if(t.length==0)
				{
					a = "seq:"+(count+1);
				}
				if(t.length==1)
				{
					a = t[0];
				}
				else if(t.length==3 || t.length==2)
				{
					a = t[0];


				}
				/*
				 * Standard header
				 *  0=gi, 1=gi-number, 2=from datasource, 3=accession, 4=locus
				 */
				else if(t.length>3)
				{

					a = t[3];
				}

				currentSeq.setLabel(a);
				count++;
			} else if( s!=null &&  s.length() >0 && first )
			{
				first=false;
				a = "seq_"+(count+1);

				currentSeq= new Seq();
				currentSeq.setLabel(a);
				seq.append(s);


				count++;

			}  else {
				seq.append(s);
			}
		}

		currentSeq.setSeqString(seq.toString());
		if(DEBUG)
			log.debug("after seq="+currentSeq.getSeqString());
		seqList.add(currentSeq);
		//	this.seqHeaders.put(a, sh);
		//	this.sequences.put(a, seq.toString());

	}


	public List<Seq> getSeqList()
	{

		return seqList;
	}

	public void clear()
	{
		seqList = new ArrayList<Seq>();
	}
}
