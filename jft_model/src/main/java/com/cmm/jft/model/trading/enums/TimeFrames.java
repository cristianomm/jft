/**
 * 
 */
package com.cmm.jft.model.trading.enums;

/**
 * <p>
 * <code>TimeFrames.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Apr 6, 2019
 * 
 */
public enum TimeFrames {
	One_min(1),
	Five_min(5),
	Ten_min(10),
	Day(1440),
	Week(7200),
	Month(30240),
	Year(362880);
	
	int value;
	TimeFrames(int value) {
		this.value = value;
	}
	
}
