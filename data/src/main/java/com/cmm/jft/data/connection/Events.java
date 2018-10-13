/**
 * 
 */
package com.cmm.jft.data.connection;

/**
 * <p><code>Events.java</code></p>
 * @author Cristiano
 * @version 19/05/2015 20:59:05
 *
 */
public enum Events {
	EMPTY,
	
	CONNECTION_ERROR,
	CONNECTION_CLOSED,
	CONNECTION_OPEN,
	
	ORDER_SEND, ORDER_ERROR,
	ORDER_UPDATE, ORDER_CANCEL, 
	TRADE, QUOTE
}
