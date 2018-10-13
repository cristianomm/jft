/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p><code>OptionRights.java</code></p>
 * @author Cristiano
 * @version 10/06/2015 20:44:48
 *
 */
public enum OptionRights {
	
	Call("C"), Put("P");
	
	String value;

	OptionRights(String value) {
		this.value = value;
	}

	public static OptionRights getByValue(String value) {
		OptionRights ret = null;

		for (OptionRights ct : OptionRights.values()) {
			if (ct.value.equalsIgnoreCase(value)) {
				ret = ct;
				break;
			}
		}

		return ret;
	}
	
}
