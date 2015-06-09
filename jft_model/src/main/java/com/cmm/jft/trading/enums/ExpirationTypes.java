/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p><code>ExpirationTypes.java</code></p>
 * @author Cristiano
 * @version 01/06/2015 21:01:56
 *
 */
public enum ExpirationTypes {
	
	Today, Expiration_Date;
	

	public static ExpirationTypes getByValue(String value) {
		ExpirationTypes et = null;

		for (ExpirationTypes ets : ExpirationTypes.values()) {
			if (ets.name().equalsIgnoreCase(value)) {
				et = ets;
			}
		}

		return et;
	}
	
}
