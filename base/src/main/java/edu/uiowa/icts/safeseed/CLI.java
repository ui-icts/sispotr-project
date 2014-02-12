/**
 * Institute for Clinical and Translational Science
 * University of Iowa
 * www.icts.uiowa.edu
 *
 */
package edu.uiowa.icts.safeseed;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import edu.uiowa.icts.safeseed.core.CartItem;
import edu.uiowa.icts.safeseed.core.Fragment;
import edu.uiowa.icts.safeseed.core.SHRNA;
import edu.uiowa.icts.safeseed.core.SHRNABuilder;
import edu.uiowa.icts.safeseed.core.Seq;
import edu.uiowa.icts.safeseed.core.SeqManager;
import edu.uiowa.icts.safeseed.core.SeqParser;
import edu.uiowa.icts.safeseed.core.SeqRef;
import edu.uiowa.icts.safeseed.core.SeqRefDefaultImpl;
import edu.uiowa.icts.safeseed.core.SeqRefFactory;
import edu.uiowa.icts.safeseed.core.XmerType;
import edu.uiowa.icts.safeseed.core.Xmers;
import edu.uiowa.icts.safeseed.core.XmersParams;


/**
 * 
 * @author Brandyn Kusenda
 * @since May 18, 2012
 */
public class CLI {

	public static Properties props=new Properties();


	/**
	 * Primary parameters without defaults
	 */
	private static Integer searchLength = null;
	private static String potsFile = null;
	private static String mirnaFile = null;
	private static String sequencesFile = null;
	private static String spsFile = null;
	private static String outputDir = null;
	
	private static String species = null;
	private static Integer gcMin = null;
	private static Integer gcMax = null;
	private static Integer gcRequired = null;
	private static Boolean gAt2 = null;
	private static Boolean gAt3 = null;
	private static Boolean ensureBestPots = null;
	private static Boolean pol3 = null;
	private static Integer numberOfReturnElements = null;
	private static Boolean trxStart = null;
	private static XmerType xtype = null;

	private static String shrnaStartSeq = null;
	private static String shrnaLoopSeq = null;
	private static String shrnaEndSeq = null;


	public static SeqManager seqManager = new SeqManager();

	/**
	 * Current directory
	 */
	public static String cwd = "";

	public static void usage(){

		
		System.out.println("");
		System.out.println("Usage:");
		System.out.println("  java sispotr  -help: this message            ");
		System.out.println("  java sispotr  -settingsfile [settingsfile]   ");
		System.out.println("  java sispotr   #uses default settings file settings.properties");
	}


	/**
	 * Loads properties from properties file with default values
	 */
	public static void loadProperties(){



		if("SHRNA".equalsIgnoreCase(props.getProperty("xmerType"))){
			xtype = XmerType.SHRNA;
		}
		else{
			xtype = XmerType.SIRNA;
		}

		potsFile = props.getProperty("potsFile","pots.txt");
		if(potsFile.startsWith("/")==false){
			potsFile = cwd+"/" +potsFile;
		}
		mirnaFile = props.getProperty("mirnaFile","miRNA.txt");
		if(mirnaFile.startsWith("/")==false){
			mirnaFile = cwd+"/" +mirnaFile;
		}
		sequencesFile =props.getProperty("sequencesFile","sequences.txt");
		if(sequencesFile.startsWith("/")==false){
			sequencesFile = cwd+"/" +sequencesFile;
		}
		
		spsFile = props.getProperty(spsFile,"seedsps.tab");
		if(spsFile.startsWith("/")==false){
			spsFile = cwd+"/" +spsFile;
		}
		
		outputDir =props.getProperty("outputDir","");
		if(outputDir.startsWith("/")==true){
			if(outputDir.endsWith("/")==false){
				outputDir = outputDir +"/";
			}
			
			if((new File(outputDir)).exists()==false){
				(new File(outputDir)).mkdirs();
				System.out.println("Output directory does not exist...creating");
			}
		}
		else if("".equalsIgnoreCase(outputDir)){
			outputDir = cwd+"/";
			
		}
			
		requireFile(sequencesFile);
		requireFile(potsFile);
		requireFile(mirnaFile);

		searchLength = Integer.parseInt(props.getProperty("searchLength","21"));
		species = props.getProperty("species","human");
		gcMin = Integer.parseInt(props.getProperty("gcMin","30"));
		gcMax = Integer.parseInt(props.getProperty("gcMax","70"));
		gcRequired = Integer.parseInt(props.getProperty("gcRequired","2"));
		gAt2 = Boolean.parseBoolean(props.getProperty("gAt2","true"));
		gAt3 = Boolean.parseBoolean(props.getProperty("gAt3","true"));
		ensureBestPots = Boolean.parseBoolean(props.getProperty("ensureBestPots","true"));
		pol3 = Boolean.parseBoolean(props.getProperty("pol3","false"));
		numberOfReturnElements = Integer.parseInt(props.getProperty("numberOfReturnSIRNAResults","100"));
		trxStart = Boolean.parseBoolean(props.getProperty("trxStart","false"));


		shrnaStartSeq = props.getProperty("shrnaStartSeq","CCCTTGGAGAAAAGCCTTGTTT");
		shrnaLoopSeq = props.getProperty("shrnaLoopSeq","CTGTAAAGCCACAGATGGG");
		shrnaEndSeq = props.getProperty("shrnaEndSeq","TTTTTTctcgag");

	}


	/**
	 * Gets all settings in settingMap and outputs to String
	 * 
	 * @return
	 */
	public static String getCurrentSettingsAsString(String space){
		
List<String> list = new ArrayList<String>();
		
		for(Object k :props.keySet()){
			String key = (String)k;
			list.add(key+"="+props.getProperty(key)+"");
			
		}
		
		Collections.sort(list);
		

		StringBuilder out = new StringBuilder();
		
		for(String v:list){
			
			out.append(space+v+"\n");
			
		}
		

		return out.toString();

	}

	/**
	 * Entry point
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("-----siSPOTR Console Tool--------");
		String settingsFilename = "settings.properties";
		
		try{
			File directory = new File (".");
			cwd = directory.getCanonicalPath();

		} catch(Exception e){
			System.out.println("Error getting cwd");
			return;
		}

		
		System.out.println("Current Directory:"+cwd);
		try {


			if (args.length >=1 ) {

				if("--settingsFile".equalsIgnoreCase(args[0])){

					settingsFilename = args[1];
					

				}else{
					for(String arg: args){
						System.out.println("incorrect arguments:"+arg +" ");
					}
				
					usage();
					return;

				}

			}
			
			if(settingsFilename.startsWith("/")==false){
				settingsFilename = cwd+"/"+settingsFilename;
			}
			requireFile(settingsFilename);
			
			InputStream in = new FileInputStream(settingsFilename);
			
			System.out.println("Loading settings from "+settingsFilename);
			props.load(in);
			in.close();
			
			loadProperties();
			System.out.println("Current Settings:");
			System.out.println(getCurrentSettingsAsString("    "));

			computeResults();

		}
		catch (Throwable t) {
			t.printStackTrace();
			System.exit(-1);
		}
	}

	/**
	 * Creates SIRNA results and outputs to file, also triggers shRNA construction if specified
	 *  
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static void computeResults() throws FileNotFoundException, IOException{

		Fragment.DEBUG=false;
		Xmers.DEBUG=false;
		SeqRefDefaultImpl.DEBUG=false;
		SeqParser.DEBUG = false;

		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-hhmmss");
		Calendar cal = Calendar.getInstance();

		String sessionId = format.format(cal.getTime());
		SHRNABuilder builder = new SHRNABuilder();

		System.out.println("Computing siRNA Results.....");
		SeqRef humanSeqRef = new SeqRefDefaultImpl(species,potsFile,mirnaFile,spsFile);
		SeqRefFactory.add(humanSeqRef);

		String outputFilename="siSPOTER_" +sessionId+"_Results";
		XmersParams params = new XmersParams( 7, gcMin/100.0, gcMax/100.0, gcRequired, gAt2, gAt3, ensureBestPots, pol3, numberOfReturnElements, trxStart,xtype);
		seqManager.addSeq(new File(sequencesFile),species,searchLength, params);

		PrintWriter out = new PrintWriter( new BufferedOutputStream(new FileOutputStream(outputDir + outputFilename+".csv")));

		out.print("seq,rank,position,POTS,seed,guide,passenger,miRNA seed,miRNA conservation,percent gc");
		
		/**
		 * if shRNA create extra columns
		 */
		if(xtype==XmerType.SHRNA){
			out.print(",fwrd_primer1,fwrd_primer2,reverse_primer1,reverse_primer2");
		}
		out.print("\n");

		for(Seq seq:seqManager.getSeqs()){
		
			for(Xmers xmer: seq.getXmersList()){

				xmer.createPassedTableOutput();


				if(xmer.getData()==null){
					System.out.println("Error no data in Xmer");
					continue;
				}

				//Print data
				for(List<String> row:xmer.getData())
				{


					StringBuilder rowOut = new StringBuilder();
					rowOut.append(seq.getLabel()+",");
					rowOut.append(row.get(0)+",");
					rowOut.append(row.get(1)+",");
					rowOut.append(row.get(2)+",");
					rowOut.append(row.get(3)+",");
					rowOut.append(row.get(4)+",");
					rowOut.append(row.get(8)+",");
					rowOut.append(row.get(9)+",");
					rowOut.append(row.get(10)+",");
					rowOut.append(row.get(12)+"");
					
					
					/**
					 * if shRNA create extra column entries
					 */
					if(XmerType.SHRNA == xtype){
						CartItem item = new CartItem();
						item.addAttr("aid", seq.getLabel());
						item.addAttr("length", ""+xmer.getLength());
						item.addAttr("start_pos", row.get(1));
						item.addAttr("pots", row.get(2));
						item.addAttr("guide", row.get(4));
						item.addAttr("passenger", row.get(5));
						item.addAttr("seed", row.get(6));
						SHRNA shRNA = builder.createSRNAFromItem(shrnaStartSeq, shrnaLoopSeq, shrnaEndSeq, item);
						//System.out.println("FWD:"+shRNA.getFwdPrimer1());
						//String[] rowArray = new String[]{seq.getLabel(),""+xmer.getLength(),row.get(2),row.get(6),shRNA.getFwdPrimer1(),shRNA.getFwdPrimer2(),shRNA.getReversePrimer1(),shRNA.getReversePrimer2()};
						rowOut.append(","+shRNA.getFwdPrimer1()+","+shRNA.getFwdPrimer2()+","+shRNA.getReversePrimer1()+","+shRNA.getReversePrimer2());

					}


					
					out.print(rowOut.toString()+"\n");
				}

			}
		}

		out.close();

		System.out.println("    siRNA Output stored in:"+outputFilename+".csv");

		String detailsFilename = "siSPOTER_" +sessionId+"_Settings";
		PrintWriter outdetails = new PrintWriter( new BufferedOutputStream(new FileOutputStream(outputDir + detailsFilename+".txt")));
		outdetails.print(getCurrentSettingsAsString(""));
		outdetails.close();
		System.out.println("    Session details stored in:"+detailsFilename+".txt");
		System.out.println("done");

	}


	/**
	 * 
	 */
	private static void requireFile(String filename) {
		if((new File(filename)).exists()==false){
			System.out.println("Error file not found:"+filename);
			System.exit(-1);
		}
	}
	



}
