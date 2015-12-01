/**
 * 
 */
package com.cmm.jft.data.connection;

import java.util.HashMap;


/**
 * <p>
 * <code>Event.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 14/12/2013 01:42:16
 *
 */
public class Event {
	
	
	private HashMap<EventFields, Object> values;
	

	public Event() {
		this.values = new HashMap<EventFields, Object>();
	}
	
	public void addValue(EventFields field, Object value){
		values.put(field, value);
	}
	
	public Object getValue(EventFields field){
		return values.getOrDefault(field, Events.EMPTY);
	}
	
}
