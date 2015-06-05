/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>StockSpecifications.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 15/11/2013 17:50:29
 *
 */
public enum StockSpecifications {

	/**
	 * Ordinárias
	 */
	OR("OR"),

	/**
	 * Preferenciais Classe A
	 */
	PA("PA"),

	/**
	 * Preferenciais Classe B
	 */
	PB("PB"),

	/**
	 * Preferenciais Classe C
	 */
	PC("PC"),

	/**
	 * Preferenciais Classe D
	 */
	PD("PD"),

	/**
	 * Preferenciais Classe E
	 */
	PE("PE"),

	/**
	 * Preferenciais Classe F
	 */
	PF("PF"),

	/**
	 * Preferenciais Classe G
	 */
	PG("PG"),

	/**
	 * Preferenciais Classe H
	 */
	PH("PH"),

	/**
	 * Preferenciais
	 */
	PR("PR");

	String value;

	StockSpecifications(String value) {
		this.value = value;
	}

	public static StockSpecifications getByValue(String value) {
		StockSpecifications ret = null;

		for (StockSpecifications ct : StockSpecifications.values()) {
			if (ct.value.equalsIgnoreCase(value)) {
				ret = ct;
				break;
			}
		}

		return ret;
	}
}
