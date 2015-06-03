/**
 * 
 */
package com.cmm.jft.data.connection;

/**
 * <p><code>EventFields.java</code></p>
 * @author Cristiano
 * @version 19/05/2015 20:59:36
 *
 */
public enum EventFields {
	EventType, 
	
	//--------------------------------------------GENERAL
	Message, ReturnCode,
	
	
	//--------------------------------------------ORDER
	OrderID, OrderSequence,
	OrderLimitPrice, OrderStopPrice, 
	OrderGainPrice, OrderAvgPrice, OrderStartPrice,
	OrderVolume, OrderSide, OrderStatus, OrderDate, OrderExpireDate,
	OrderType
}
