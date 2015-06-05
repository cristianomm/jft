/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>TradeTypes.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 18/02/2014 01:37:42
 *
 */
public enum TradeTypes {

	DAY_TRADE("D"), NORMAL("S");

	String value;

	TradeTypes(String value) {
		this.value = value;
	}

	public static TradeTypes getByValue(String value) {

		TradeTypes tt = null;
		if (value.equalsIgnoreCase("d")) {
			tt = DAY_TRADE;
		} else if (value.equalsIgnoreCase("s") || value.equalsIgnoreCase("n")) {
			tt = NORMAL;
		}
		return tt;
	}

}
