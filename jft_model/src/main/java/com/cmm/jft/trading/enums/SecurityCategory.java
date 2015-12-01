/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>SecurityCategory.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 09/10/2013 01:51:20
 *
 */
public enum SecurityCategory {

	/**
	 * E – AÇÃO
	 */
	STOCK("E"),
	/**
	 * D - Renda Fixa
	 */
	FIXED_INCOME("D"),
	/**
	 * R – DIREITOS
	 */
	RIGHTS("R"),
	/**
	 * O – OPÇÃO
	 */
	OPTION("O"),
	/**
	 * F – FUTURO
	 */
	FUTURE("F"),
	/**
	 * I – INDICE
	 */
	INDEX("I"),
	/**
	 * S – SWAP
	 */
	SWAP("S"),
	/**
	 * T – TAXA
	 */
	TAX("T"),
	/**
	 * G – TERMO
	 */
	TERM("G"),
	/**
	 * M – OUTROS
	 */
	OTHERS("M");

	String value;

	SecurityCategory(String value) {
		this.value = value;
	}

	public static SecurityCategory getByValue(String value) {
		SecurityCategory ret = null;

		for (SecurityCategory ct : SecurityCategory.values()) {
			if (ct.value.equalsIgnoreCase(value)) {
				ret = ct;
				break;
			}
		}

		return ret;
	}
	
	public static SecurityCategory getByISIN(String isin){
		SecurityCategory category =  null;
		
		//BRVALEACNPA3
		//BR
		//VALE
		//ACN
		//PA
		//3
		
		return category;
	}

}
