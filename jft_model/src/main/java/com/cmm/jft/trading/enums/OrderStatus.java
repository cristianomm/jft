/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>OrderStatus.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Jul 21, 2013 2:06:06 AM
 *
 */
public enum OrderStatus {
	/*
	 * Indicador de estado da ordem: " " - aceite "E" - eliminada (EOC) "G" -
	 * congelada "O" - cancelada seguido de uma ação no instrumento (por ex-
	 * Papel Reservado) "X" - totalmente executada "M" - modificada "D" -
	 * disparada "A" - anulada (corretora) "R" - rejeitada pelo Surveillance,
	 * seguido de um congelamento.
	 */

	/*
	 * Após 04/03/2013 devido a migração para o PUMA alguns ativos estarão
	 * valorizados com: Order status: 0 - New 1 - Partially Filled 2 - Filled 4
	 * - Canceled 5 - Replaced 8 - Rejected C - Expired
	 */
	OPEN("0"), PARTIALLY_FILLED("1"), FILLED("2"), CANCELED("4"), 
	REPLACED("5"), REJECTED("8"), EXPIRED("C"),
	// JFT
	CREATED("N"), SUBMITTED("S");

	String value;

	OrderStatus(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static OrderStatus getByValue(String value) {
		OrderStatus ret = null;

		if (value.equalsIgnoreCase(" ") || value.equalsIgnoreCase("D")) {
			value = "0";
		} else if (value.equalsIgnoreCase("E") || value.equalsIgnoreCase("G")
				|| value.equalsIgnoreCase("O") || value.equalsIgnoreCase("A")) {
			value = "4";
		} else if (value.equalsIgnoreCase("X")) {
			value = "2";
		} else if (value.equalsIgnoreCase("M")) {
			value = "5";
		} else if (value.equalsIgnoreCase("R")) {
			value = "8";
		}

		for (OrderStatus os : OrderStatus.values()) {
			if (os.value.equalsIgnoreCase(value)) {
				ret = os;
				break;
			}
		}

		return ret;
	}
}
