/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.util.HashMap;

import com.cmm.jft.core.vo.Extractable;

/**
 * <p><code>Data.java</code></p>
 * @author Cristiano M Martins
 * @version 06/03/2015 15:04:18
 *
 */
public class Data implements Extractable {
	
		
	private HashMap<String, Object> data;
	
	
	public Data(){
		this.data = new HashMap<String, Object>();
	}
	
	
	public void put(String dataID, Object value){
		data.put(dataID, value);
	}
	
	
	public Object getData(String dataID){
		return data.get(dataID);
	}
	
}
