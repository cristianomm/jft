/**
 * 
 */
package com.cmm.jft.messaging;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;

import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;

/**
 * <p><code>MessageDispatcher.java</code></p>
 * @author Cristiano M Martins
 * @version 30 de jul de 2015 23:25:19
 *
 */
public class MessageDispatcher {
	
	
	private static MessageDispatcher instance;
	
	
	
	private MessageDispatcher(){
		
	}
	
	/**
	 * @return the instance
	 */
	public static synchronized MessageDispatcher getInstance() {
		if(instance == null){
			instance = new MessageDispatcher();
		}
		return instance;
	}
	
	
	public void deliverMessage(Message message, SessionID sessionID){
		try {
			Session.sendToTarget(message, sessionID);
		} catch (SessionNotFound e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}
	
}
