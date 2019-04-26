/**
 * 
 */
package com.cmm.jft.model.trading.enums;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * <p>
 * <code>EarningType.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 30/10/2013 15:22:14
 *
 */
public enum EarningType {

	DIVIDEND("DIVIDENDO"), JRS_CAP_PROPRIO("JRS CAP PROPRIO"), RENDIMENTO(
			"RENDIMENTO"), REST_CAP_DIN("REST_CAP_DIN");

	String value;

	EarningType(String value) {
		this.value = value;
	}

	public static EarningType getByValue(String value) {
		EarningType ret = null;
		for (EarningType e : EarningType.values()) {
			if (e.value.equalsIgnoreCase(value)) {
				ret = e;
				break;
			}
		}
		return ret;
	}

}
