/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>MarketEvents.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 13/12/2013 16:48:07
 *
 */
public enum MarketEvents {

	/*
	 * Código do Evento da Ordem: 1 - New 2 - Update 3 - Cancel - Solicitado
	 * pelo participante 4 - Trade 5 - Reentry - Processo interno (quantidade
	 * escondida) 6 - New Stop Price 7 - Reject 8 - Remove - Removida pelo
	 * Sistema (final de dia ou quando é totalmente fechada) 9 - Stop Price
	 * Triggered 11 - Expire - Oferta com validade expirada.
	 */
	NEW("1"), UPDATE("2"), CANCEL("3"), TRADE("4"), REENTRY("5"), NEW_STOP_PRICE(
			"6"), REJECT("7"), REMOVE("8"), STOP_PRICE_TRIGGERED("9"), EXPIRE(
			"11");

	String value;

	private MarketEvents(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public static MarketEvents getByValue(String value) {
		MarketEvents ret = null;

		for (MarketEvents me : MarketEvents.values()) {
			if (me.value.equalsIgnoreCase(value)) {
				ret = me;
				break;
			}
		}

		return ret;
	}

}
