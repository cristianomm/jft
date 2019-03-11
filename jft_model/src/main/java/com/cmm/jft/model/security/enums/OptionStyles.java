/**
 * 
 */
package com.cmm.jft.model.security.enums;

/**
 * <p>
 * <code>OptionStyles.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 09/10/2013 02:48:16
 *
 */
public enum OptionStyles {

	AMERICAN("A"), EUROPEAN("E");

	String value;

	OptionStyles(String value) {
		this.value = value;
	}

	public static OptionStyles getByValue(String value) {
		OptionStyles ret = null;

		for (OptionStyles ct : OptionStyles.values()) {
			if (ct.value.equalsIgnoreCase(value)) {
				ret = ct;
				break;
			}
		}

		return ret;
	}

}
