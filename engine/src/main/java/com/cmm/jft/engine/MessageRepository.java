package com.cmm.jft.engine;

import java.util.concurrent.ConcurrentLinkedQueue;

import quickfix.Message;



/**
 * <p><code>EntryPointListener.java</code></p>
 * @author Cristiano M Martins
 * @version 22 de jul de 2015 11:14:09
 *
 */
public class MessageRepository {

	
	private static MessageRepository instance;
	private ConcurrentLinkedQueue<Message> messages;
	
	
	
	private MessageRepository() {
		this.messages = new ConcurrentLinkedQueue<Message>();
	}
	
	public static synchronized MessageRepository getInstance() {
		if(instance == null) {
			instance = new MessageRepository();
		}
		
		return instance;
	}
	
	
	public boolean addMessage(Message message) {
		return this.messages.offer(message);
	}
	
	public Message retrieveMessage() {
		return messages.poll();
	}
	
	public int queueSize() {
		return messages.size();
	}
	
	
}
