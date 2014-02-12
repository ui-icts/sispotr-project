package edu.uiowa.icts.safeseed.core;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SeqRefFactory {
	
	private static final Log log =LogFactory.getLog(SeqRefFactory.class);
	
	private static Map<String,SeqRef> seqRefMap = new ConcurrentHashMap<String,SeqRef>();;
	
	
	public static void add(SeqRef seqref)
	{
		seqRefMap.put(seqref.getSpecies(),seqref);
		
	}
	
	public static SeqRef getSeqRefBySpecies(String sp)
	{
		return seqRefMap.get(sp);
	}
}
