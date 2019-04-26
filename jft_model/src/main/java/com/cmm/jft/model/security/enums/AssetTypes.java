/**
 * 
 */
package com.cmm.jft.model.security.enums;

/**
 * <p>
 * <code>AssetTypes.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 29/09/2013 19:09:33
 *
 */
public enum AssetTypes {

	/**
	 * S – AÇÃO
	 */
	STOCK("S"),
	/**
	 * T – COMMODITIES
	 */
	COMMODITIES("T"),
	/**
	 * I – ÍNDICES
	 */
	INDEX("I"),
	/**
	 * F – FUTURO
	 */
	FUTURE("F"),
	/**
	 * B – CESTA
	 */
	BASKET("B"),
	/**
	 * D - TAXA DE JUROS
	 */
	TAXRATE("D"),
	/**
	 * C – MOEDA
	 */
	CURRENCY("C"),
	/**
	 * 0 – OPÇÃO
	 */
	OPTION("0"),
	/**
	 * W – SWAP
	 */
	SWAP("W"),
	/**
	 * M – OUTROS
	 */
	OTHERS("M");

	String value;

	AssetTypes(String value) {
		this.value = value;
	}

	public static AssetTypes getByValue(String value) {
		AssetTypes ret = null;

		for (AssetTypes tp : AssetTypes.values()) {
			if (tp.value.equalsIgnoreCase(value)) {
				ret = tp;
				break;
			}
		}

		return ret;
	}

}
