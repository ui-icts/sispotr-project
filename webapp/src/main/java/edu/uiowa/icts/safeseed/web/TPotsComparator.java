package edu.uiowa.icts.safeseed.web;

import java.io.Serializable;
import java.util.Comparator;

public class TPotsComparator implements Comparator<String[]>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8134128967465662103L;

	@Override
	public int compare(String[] arg0, String[] arg1) {
		
		
		if(arg0.length>2 && arg1.length>2)
		{
			float f0 = Float.parseFloat(arg0[1]);
			float f1 = Float.parseFloat(arg1[1]);
			//System.out.println("comparing:"+f0+" & "+f1);
			if(f0>f1)
				return 1;
			else if(f0<f1)
				return -1;			
		}
		else
		{
			//System.out.println("error sorting");
		}
		
		return 0;
	}
	
	

}
