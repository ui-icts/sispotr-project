package edu.uiowa.icts.safeseed;

public class PotsSummary {

	private String heptamer="";
	private String species="";
	private int totalGenesTargeted;
	private int genes8Mer=0;
	private int genes7merM8=0;
	private int genes7merA1=0;
	private int genes6mer=0;
	private float pots;
	

	
	public PotsSummary(String heptamer, String species, float pots) {
		super();
		this.heptamer = heptamer;
		this.species = species;
		this.pots = pots;
	}

	public void add(int g8mer, int g7merm8, int g7mera1,int g6mer)
	{
	
		totalGenesTargeted++;
		if(g8mer>=1)
			genes8Mer++;
		if(g7merm8>=1)
			genes7merM8++;
		if(g7mera1>=1)
			genes7merA1++;
		if(g6mer>=1)
			genes6mer++;
		
	}

	public String getHeptamer() {
		return heptamer;
	}

	public void setHeptamer(String heptamer) {
		this.heptamer = heptamer;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}

	public int getTotalGenesTargeted() {
		return totalGenesTargeted;
	}

	public void setTotalGenesTargeted(int totalGenesTargeted) {
		this.totalGenesTargeted = totalGenesTargeted;
	}

	public int getGenes8Mer() {
		return genes8Mer;
	}

	public void setGenes8Mer(int genes8Mer) {
		this.genes8Mer = genes8Mer;
	}

	public int getGenes7merM8() {
		return genes7merM8;
	}

	public void setGenes7merM8(int genes7merM8) {
		this.genes7merM8 = genes7merM8;
	}

	public int getGenes7merA1() {
		return genes7merA1;
	}

	public void setGenes7merA1(int genes7merA1) {
		this.genes7merA1 = genes7merA1;
	}

	public int getGenes6mer() {
		return genes6mer;
	}

	public void setGenes6mer(int genes6mer) {
		this.genes6mer = genes6mer;
	}

	public float getPots() {
		return pots;
	}

	public void setPots(float pots) {
		this.pots = pots;
	}
	
}
