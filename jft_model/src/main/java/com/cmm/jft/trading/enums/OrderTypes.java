/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>OrderTypes.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 07/08/2013 02:31:10
 *
 */
public enum OrderTypes {
	// http://usequities.nyx.com/markets/nyse-arca-equities/order-types
	//https://www.nyse.com/markets/nyse-arca/trading-info
	MARKET, LIMIT, STOP, STOP_LIMIT, STOP_GAIN;

	public static OrderTypes getByValue(String value) {
		OrderTypes ot = null;

		for (OrderTypes ots : OrderTypes.values()) {
			if (ots.name().equalsIgnoreCase(value)) {
				ot = ots;
			}
		}

		return ot;
	}

}
