/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>Duration.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 11/08/2013 22:48:14
 *
 */
public enum Duration {

	MILI(1L), SEC(1000L), MIN(60000L), HOUR(3600000L), DAY(86400000L), WEEK(
			604800000L), MONTH(2592000000L), YEAR(31536000000L);

	long value;

	Duration(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

}
