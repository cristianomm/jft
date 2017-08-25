/**
 * 
 */
package com.cmm.jft.engine;

import java.util.TreeMap;

/**
 * <p>
 * <code>ErrorCodes.java</code>
 * </p>
 *
 * @author cristiano
 * @version 25/08/2017 02:58:59
 *
 */
public class ErrorCodes {
    
    private static ErrorCodes instance;
    private TreeMap<Integer, String> messages;
    
    /**
     * 
     */
    private ErrorCodes() {
	this.messages = new TreeMap<>();
    }
    
    /**
     * @return the instance
     */
    public synchronized static ErrorCodes getInstance() {
	if(instance == null) {
	    instance = new ErrorCodes();
	}
	return instance;
    }
    
    public String getMessage(int errorCode) {
	return messages.get(errorCode);
    }
    
}
