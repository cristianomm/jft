/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>Side.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Jul 21, 2013 2:05:24 AM
 *
 */
public enum Side {
	BUY("B"), SELL("S");

	String value;

	Side(String value) {
		this.value = value;
	}

	public static Side getByValue(String value) {

		Side s = null;
		if (value.equalsIgnoreCase("b") || value.equalsIgnoreCase("c")) {
			s = BUY;
		} else if (value.equalsIgnoreCase("s") || value.equalsIgnoreCase("v")) {
			s = SELL;
		}
		return s;
	}

}
