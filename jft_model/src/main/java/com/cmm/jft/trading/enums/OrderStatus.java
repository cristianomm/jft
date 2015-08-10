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
	NEW('0'), PARTIALLY_FILLED('1'), FILLED('2'), CANCELED('4'), 
	REPLACED('5'), REJECTED('8'), SUSPENDED('9'), EXPIRED('C'),
	PREVIOUSFS('Z'),
	// JFT
	CREATED('N'), SUBMITTED('S');

	char value;

	OrderStatus(char value) {
		this.value = value;
	}

	public char getValue() {
		return this.value;
	}

	public static OrderStatus getByValue(char value) {
		OrderStatus ret = null;

		if (value == ' ' || value == 'D') {
			value = '0';
		} else if (value == 'E' || value == 'G'
				|| value == 'O' || value == 'A') {
			value = '4';
		} else if (value == 'X') {
			value = '2';
		} else if (value == 'M') {
			value = '5';
		} else if (value == 'R') {
			value = '8';
		}

		for (OrderStatus os : OrderStatus.values()) {
			if (os.value == value) {
				ret = os;
				break;
			}
		}

		return ret;
	}
}
