/**
 * 
 */
package com.cmm.jft.model.security.enums;

/**
 * <p>
 * <code>MarketTypes.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 26/09/2013 00:45:29
 *
 */
public enum MarketTypes {

	// EQUITIES,
	// COMMODITIES_AND_FUTURES,
	// FOREIGN_EXCHANGES,
	// EXCHANGE_TRADED_FUNDS,

	/**
	 * 010 VISTA
	 */
	EQUITIES(10),

	/**
	 * 012 EXERCÍCIO DE OPÇÕES DE COMPRA
	 */
	CALL_EXERCISE(12),

	/**
	 * 013 EXERCÍCIO DE OPÇÕES DE VENDA
	 */
	PUT_EXERCISE(13),

	/**
	 * 017 LEILÃO
	 */
	AUCTION(17),

	/**
	 * 020 FRACIONÁRIO
	 */
	FRACTIONARY(20),

	/**
	 * 030 TERMO
	 */
	TERM(30),

	/**
	 * 050 FUTURO COM RETENÇÃO DE GANHO
	 */
	FUTURE_GAIN_RETENTION(50),

	/**
	 * 060 FUTURO COM MOVIMENTAÇÃO CONTÍNUA
	 */
	FUTURE_CONTINUOS_MOVING(60),

	/**
	 * 070 OPÇÕES DE COMPRA
	 */
	CALL(70),

	/**
	 * 080 OPÇÕES DE VENDA
	 */
	PUT(80);

	int value;

	MarketTypes(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public static MarketTypes getByValue(int value) {

		MarketTypes ret = null;

		switch (value) {
		case 10:
			ret = EQUITIES;
			break;
		case 12:
			ret = CALL_EXERCISE;
			break;
		case 13:
			ret = PUT_EXERCISE;
			break;
		case 17:
			ret = AUCTION;
			break;
		case 20:
			ret = FRACTIONARY;
			break;
		case 30:
			ret = TERM;
			break;
		case 50:
			ret = FUTURE_GAIN_RETENTION;
			break;
		case 60:
			ret = FUTURE_CONTINUOS_MOVING;
			break;
		case 70:
			ret = CALL;
			break;
		case 80:
			ret = PUT;
			break;

		}

		return ret;
	}
}
