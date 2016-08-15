/**
 * 
 */
package com.cmm.jft.engine;

/**
 * <p><code>IdGenerator.java</code></p>
 * @author Cristiano M Martins
 * @version Aug 3, 2016 10:38:47 AM
 *
 */
public class IdGenerator {
	
	private int orderID;
	private int execID;
	
	
	
	/**
	 * @return the execID
	 */
	public int getExecID() {
		return this.execID++;
	}
	
	/**
	 * @return the orderID
	 */
	public int getOrderID() {
		return this.orderID++;
	}
	
	
	
}
