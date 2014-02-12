/**
 * Institute for Clinical and Translational Science
 * University of Iowa
 * www.icts.uiowa.edu
 *
 */
package edu.uiowa.icts.safeseed.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author Brandyn Kusenda
 * @since Apr 19, 2012
 */
public class RNACart implements Serializable {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3084372154304143825L;
	
	private List<CartItem> itemList;
	
	public RNACart(){
		itemList = new ArrayList<CartItem>();
	}

	public List<CartItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<CartItem> itemList) {
		this.itemList = itemList;
	}
	
	public void addItem(CartItem item){
		if(contains(item)==false){
			itemList.add(item);
		}
	}
	
	public boolean contains(CartItem item){
		
		for(CartItem tempItem: itemList){
			if(item.equals(tempItem)){
				return true;
			}
		}
		return false;
	}
	
	public int getCartSize(){
		return itemList.size();
	}

	
	

	
	
}
