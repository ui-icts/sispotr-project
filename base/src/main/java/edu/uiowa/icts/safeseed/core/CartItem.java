/**
 * Institute for Clinical and Translational Science
 * University of Iowa
 * www.icts.uiowa.edu
 *
 */
package edu.uiowa.icts.safeseed.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Brandyn Kusenda
 * @since Apr 19, 2012
 */
public class CartItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String,String> attribMap;
	
	public CartItem(){
		attribMap = new HashMap<String,String>();
	}
	
	public void addAttr(String key, String val){
		
		attribMap.put(key, val);
		
	}
	
	public String getAttr(String key){
		return attribMap.get(key);
	}

	public Map<String, String> getAttribMap() {
		return attribMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attribMap == null) ? 0 : attribMap.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CartItem other = (CartItem) obj;
		if (attribMap == null) {
			if (other.attribMap != null)
				return false;
		} else if (!attribMap.equals(other.attribMap))
			return false;
		return true;
	}
	
	
	

}
