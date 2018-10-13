/**
 * 
 */
package com.cmm.jft.messaging;


/**
 * <p><code>MessageCounter.java</code></p>
 * @author Cristiano
 * @version 17/06/2015 20:11:27
 *
 */
public class MessageCounter {
	
	private int messageCount;
	private static MessageCounter instance;
	
	private MessageCounter(){
		this.messageCount = 0;
	}
	
	/**
	 * @return the instance
	 */
	public static synchronized MessageCounter getInstance() {
		if(instance == null){
			instance = new MessageCounter();
		}
		
		return instance;
	}
	
	public void reset(){
		messageCount = 0;
	}
	
	public int getMessageCount(){
		return messageCount++;
	}
	
}
