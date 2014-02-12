package edu.uiowa.icts.safeseed.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/*
 * This class
 * 
 */
public class Seq implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1397770233146700509L;
	private static final Log log =LogFactory.getLog(Seq.class);
	private Long seqId;
	private String label;
	private String seqString;	
	private String species;
	
	private HashMap<Integer,Xmers> xmersMap;
	
	
	public Seq() {
		xmersMap = new HashMap<Integer,Xmers>();
	}
	public Long getSeqId() {
		return seqId;
	}
	public void setSeqId(Long seqId) {
		this.seqId = seqId;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getSeqString() {
		return seqString;
	}
	public void setSeqString(String seqString) {
		this.seqString = seqString;
	}
	public String getSpecies() {
		if(species==null)
			return "human";
		return species;
	}
	public void setSpecies(String species) {
		this.species = species;
	}
	
	public void computeXmers(Integer length, XmersParams params)
	{
		/*
		 * if exists update, else create and add to map
		 */
		if(xmersMap.containsKey(length))
		{
			Xmers x = xmersMap.get(length);
			x.updateFragments(params);	
		}
		else
		{
			Xmers x = new Xmers(this,length,params);
			xmersMap.put(length, x);
			
		}
		
	}
	
//	public void computeXmers(int length , XmersParams params)
//	{
//		/*
//		 * if exists update, else create and add to map
//		 */
//		if(xmersMap.containsKey(length))
//		{
//			Xmers f = xmersMap.get(length);
//			f.updateFragments(params);	
//		}
//		else
//		{
//			Xmers f = new Xmers(this,length,params);		
//			xmersMap.put(length, f);
//			
//		}
//		
//	}
	
	public Xmers getXmers(Integer len)
	{
		return xmersMap.get(len);
	}
	
	public Collection<Xmers> getXmersList()
	{
		return xmersMap.values();
	}
	
	public HashMap<Integer,Xmers> getXmersMap()
	{
		return xmersMap;
	}

	
	public int length()
	{
		return seqString.length();
	}
	
	public char charAt(int i)
	{
		return seqString.charAt(i);
	}

	@Override
	public String toString() {
		
		return	"Seq [seqId=" + seqId + ", label=" + label + ", species=" + species + ", xmersMap="
				+ xmersMap+"]";
						
	}
	


}
