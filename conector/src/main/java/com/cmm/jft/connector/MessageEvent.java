/**
 * 
 */
package com.cmm.jft.connector;

import quickfix.fix44.Message;

/**
 * <p>
 * <code>MessageEvent.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 02/04/2017 11:45:47
 *
 */
public class MessageEvent {
    
    private Message message;
    
    /**
     * 
     */
    public MessageEvent(Message message) {
	this.message = message;
    }
    
    /**
     * @return the message
     */
    public Message getMessage() {
	return message;
    }
    
    
}
