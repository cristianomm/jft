/**
 * 
 */
package com.cmm.jft.model.security.enums;

/**
 * <p>
 * <code>SecurityEventTypes.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 17, 2019
 * 
 */
public enum SecurityEventTypes {

	Bonus(5), Split(10), Group(15), Dividend(20), Interest(25), Warrant(30), Adjustment(35);
	
	int value;
	
	private SecurityEventTypes(int value) {
		this.value = value;
	}
	
}
