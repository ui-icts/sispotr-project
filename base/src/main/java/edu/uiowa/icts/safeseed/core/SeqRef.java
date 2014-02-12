/*
 * 
 * 
 */
package edu.uiowa.icts.safeseed.core;

/*
 * Interface for class that contains lookup information 
 * 
 */
public interface SeqRef {
	
	public String getSpecies();
	
	public Double get7MerPots(String sevenMer);
	
	public Double get7MerBestPots(String sevenMer);
	
	public String get7MerBestSeq(String sevenMer);
	
	public String[] get7MerMatch(String sevenMer);

	Double get7MerPotsPercentiles(String sevenMer);
	
	public Double getSps(String seed);
	
	public void init();

	public void logStatus();

}
