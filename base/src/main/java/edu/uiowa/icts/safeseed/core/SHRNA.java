/**
 * Institute for Clinical and Translational Science
 * University of Iowa
 * www.icts.uiowa.edu
 *
 */
package edu.uiowa.icts.safeseed.core;

/**
 * 
 * @author Brandyn Kusenda
 * @since Apr 20, 2012
 */
public class SHRNA {
	
	private String start_seq;
	private String loop_seq;
	private String end_seq;
	
	private String fwdPrimer1;
	private String fwdPrimer2;
	private String reversePrimer1;
	private String reversePrimer2;
	private CartItem item;
	public String getFwdPrimer1() {
		return fwdPrimer1;
	}
	public void setFwdPrimer1(String fwdPrimer1) {
		this.fwdPrimer1 = fwdPrimer1;
	}
	public String getFwdPrimer2() {
		return fwdPrimer2;
	}
	public void setFwdPrimer2(String fwdPrimer2) {
		this.fwdPrimer2 = fwdPrimer2;
	}
	public String getReversePrimer1() {
		return reversePrimer1;
	}
	public void setReversePrimer1(String reversePrimer1) {
		this.reversePrimer1 = reversePrimer1;
	}
	public String getReversePrimer2() {
		return reversePrimer2;
	}
	public void setReversePrimer2(String reversePrimer2) {
		this.reversePrimer2 = reversePrimer2;
	}
	public CartItem getItem() {
		return item;
	}
	public void setItem(CartItem item) {
		this.item = item;
	}
	public String getStart_seq() {
		return start_seq;
	}
	public void setStart_seq(String start_seq) {
		this.start_seq = start_seq;
	}
	public String getLoop_seq() {
		return loop_seq;
	}
	public void setLoop_seq(String loop_seq) {
		this.loop_seq = loop_seq;
	}
	public String getEnd_seq() {
		return end_seq;
	}
	public void setEnd_seq(String end_seq) {
		this.end_seq = end_seq;
	}
	@Override
	public String toString() {
		return "SHRNA [start_seq=" + start_seq + ", loop_seq=" + loop_seq
				+ ", end_seq=" + end_seq + ", fwdPrimer1=" + fwdPrimer1
				+ ", fwdPrimer2=" + fwdPrimer2 + ", reversePrimer1="
				+ reversePrimer1 + ", reversePrimer2=" + reversePrimer2 + "]";
	}
	

}
