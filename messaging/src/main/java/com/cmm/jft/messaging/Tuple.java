/**
 * 
 */
package com.cmm.jft.messaging;

import quickfix.Message;
import quickfix.SessionID;

/**
 * <p><code>Tuple.java</code></p>
 * @author Cristiano
 * @version 10 de ago de 2015 01:14:07
 *
 */
public class Tuple {

	private Message message;
	private SessionID sessionID;
	
	
	/**
	 * @param message
	 * @param sessionID
	 */
	public Tuple(Message message, SessionID sessionID) {
		super();
		this.message = message;
		this.sessionID = sessionID;
	}
	
	/**
	 * @return the message
	 */
	public Message getMessage() {
		return this.message;
	}
	
	/**
	 * @return the sessionID
	 */
	public SessionID getSessionID() {
		return this.sessionID;
	}
	
}
