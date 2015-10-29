package com.cmm.jft.engine;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.cmm.jft.messaging.Tuple;

import quickfix.Message;
import quickfix.SessionID;



/**
 * <p><code>App.java</code></p>
 * @author Cristiano M Martins
 * @version 22 de jul de 2015 11:14:09
 *
 */
public class MessageRepository {

	private static MessageRepository instance;
	private ConcurrentLinkedQueue<Tuple> messages;
	
	
	
	private MessageRepository() {
		this.messages = new ConcurrentLinkedQueue<Tuple>();
	}
	
	public static synchronized MessageRepository getInstance() {
		if(instance == null) {
			instance = new MessageRepository();
		}
		
		return instance;
	}
	
	
	public boolean addMessage(Message message, SessionID sessionID) {
		return this.messages.offer(new Tuple(message, sessionID));
	}
	
	public Tuple retrieveMessage() {
		
		return messages.poll();
	}
	
	public int queueSize() {
		return messages.size();
	}
	
	
}
