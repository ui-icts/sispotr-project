/*
 * @author Brandyn Kusenda
 */
package edu.uiowa.icts.safeseed.core;


public class XmersParams {
	
	private int searchLength; 
	private double gcMin; 
	private double gcMax;
    private int gcRequired; 
    private boolean gAt2; 
    private boolean gAt3;
    private boolean ensureBestPots;
    private boolean pol3;
    private boolean trxStart;
    private boolean replaceUsWithTsOnAntisense;
    private boolean replaceUsWithTsOnPassenger;
    private boolean appendNNtoPassenger;
    private int numberOfReturnElements;
    private XmerType xmerType;
    

    public XmersParams(int searchLength, double gcMin, double gcMax,
			int gcRequired, boolean gAt2, boolean gAt3, boolean ensureBestPots,
			boolean pol3, int numberOfReturnElements) {
		super();

		this.searchLength = searchLength;
		this.gcMin = gcMin;
		this.gcMax = gcMax;
		this.gcRequired = gcRequired;
		this.gAt2 = gAt2;
		this.gAt3 = gAt3;
		this.ensureBestPots = ensureBestPots;
		this.pol3 = pol3;
		this.numberOfReturnElements = numberOfReturnElements;
		
		this.trxStart = false;
		this.xmerType = XmerType.SIRNA;
		this.replaceUsWithTsOnAntisense = false;
		this.replaceUsWithTsOnPassenger = false;
		this.appendNNtoPassenger = true;
	}
    
	// added the +1 transcription start for shRNA's
	public XmersParams(int searchLength, double gcMin, double gcMax,
			int gcRequired, boolean gAt2, boolean gAt3, boolean ensureBestPots,
			boolean pol3, int numberOfReturnElements, boolean trxStart,XmerType xmerType) {
		super();

		this.searchLength = searchLength;
		this.gcMin = gcMin;
		this.gcMax = gcMax;
		this.gcRequired = gcRequired;
		this.gAt2 = gAt2;
		this.gAt3 = gAt3;
		this.ensureBestPots = ensureBestPots;
		this.pol3 = pol3;
		this.numberOfReturnElements = numberOfReturnElements;
		this.trxStart = trxStart;
		this.xmerType = xmerType;
		if(xmerType == XmerType.SHRNA){
			this.replaceUsWithTsOnAntisense = false;
			this.replaceUsWithTsOnPassenger = false;
			this.appendNNtoPassenger = true;
		}
		else{
			this.replaceUsWithTsOnAntisense = false;
			this.replaceUsWithTsOnPassenger = false;
			this.appendNNtoPassenger = true;
		}
		
	}
	
	// added the +1 transcription start for shRNA's
	public XmersParams(int searchLength, double gcMin, double gcMax,
			int gcRequired, boolean gAt2, boolean gAt3, boolean ensureBestPots,
			boolean pol3, int numberOfReturnElements, boolean trxStart,boolean replaceUsWithTsOnAntisense,boolean replaceUsWithTsOnPassenger,boolean appendNNtoPassenger, XmerType xmerType) {
		super();

		this.searchLength = searchLength;
		this.gcMin = gcMin;
		this.gcMax = gcMax;
		this.gcRequired = gcRequired;
		this.gAt2 = gAt2;
		this.gAt3 = gAt3;
		this.ensureBestPots = ensureBestPots;
		this.pol3 = pol3;
		this.numberOfReturnElements = numberOfReturnElements;
		this.trxStart = trxStart;
		this.xmerType = xmerType;
		this.replaceUsWithTsOnAntisense = replaceUsWithTsOnAntisense;
		this.replaceUsWithTsOnPassenger = replaceUsWithTsOnPassenger;
		this.appendNNtoPassenger = appendNNtoPassenger;
	}
	
	
	


	public int getSearchLength() {
		return searchLength;
	}
	public void setSearchLength(int searchLength) {
		this.searchLength = searchLength;
	}
	public double getGcMin() {
		return gcMin;
	}
	public void setGcMin(double gcMin) {
		this.gcMin = gcMin;
	}
	public double getGcMax() {
		return gcMax;
	}
	public void setGcMax(double gcMax) {
		this.gcMax = gcMax;
	}
	public int getGcRequired() {
		return gcRequired;
	}
	public void setGcRequired(int gcRequired) {
		this.gcRequired = gcRequired;
	}
	public boolean isgAt2() {
		return gAt2;
	}
	public void setgAt2(boolean gAt2) {
		this.gAt2 = gAt2;
	}
	public boolean isgAt3() {
		return gAt3;
	}
	public void setgAt3(boolean gAt3) {
		this.gAt3 = gAt3;
	}
	public boolean isEnsureBestPots() {
		return ensureBestPots;
	}
	public void setEnsureBestPots(boolean ensureBestPots) {
		this.ensureBestPots = ensureBestPots;
	}
	public boolean isPol3() {
		return pol3;
	}
	public void setPol3(boolean pol3) {
		this.pol3 = pol3;
	}
	public int getNumberOfReturnElements() {
		return numberOfReturnElements;
	}
	public void setNumberOfReturnElements(int numberOfReturnElements) {
		this.numberOfReturnElements = numberOfReturnElements;
	}
	public boolean isTrxStart(){
		return this.trxStart;
	}
	public void setTrxStart(boolean trxStart){
		this.trxStart = trxStart;
	}

	public XmerType getXmerType() {
		return xmerType;
	}

	public void setXmerType(XmerType xmerType) {
		this.xmerType = xmerType;
	}

	public boolean isReplaceUsWithTsOnAntisense() {
		return replaceUsWithTsOnAntisense;
	}

	public void setReplaceUsWithTsOnAntisense(boolean replaceUsWithTsOnAntisense) {
		this.replaceUsWithTsOnAntisense = replaceUsWithTsOnAntisense;
	}

	public boolean isAppendNNtoPassenger() {
		return appendNNtoPassenger;
	}

	public void setAppendNNtoPassenger(boolean appendNNtoPassenger) {
		this.appendNNtoPassenger = appendNNtoPassenger;
	}

	public boolean isReplaceUsWithTsOnPassenger() {
		return replaceUsWithTsOnPassenger;
	}

	public void setReplaceUsWithTsOnPassenger(boolean replaceUsWithTsOnPassenger) {
		this.replaceUsWithTsOnPassenger = replaceUsWithTsOnPassenger;
	}

	@Override
	public String toString() {
		return "XmersParams [searchLength=" + searchLength + ", gcMin=" + gcMin
				+ ", gcMax=" + gcMax + ", gcRequired=" + gcRequired + ", gAt2="
				+ gAt2 + ", gAt3=" + gAt3 + ", ensureBestPots="
				+ ensureBestPots + ", pol3=" + pol3 + ", trxStart=" + trxStart
				+ ", replaceUsWithTsOnAntisense=" + replaceUsWithTsOnAntisense
				+ ", replaceUsWithTsOnPassenger=" + replaceUsWithTsOnPassenger
				+ ", appendNNtoPassenger=" + appendNNtoPassenger
				+ ", numberOfReturnElements=" + numberOfReturnElements
				+ ", xmerType=" + xmerType + "]";
	}


    
    

}
