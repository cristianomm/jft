/**
 * 
 */
package com.cmm.jft.trading.enums;

import com.cmm.jft.financial.enums.EntryType;

/**
 * @author cristiano
 *
 */
public enum MDEntryTypes {

	// Field descriptor #14 C
	BID('0'),

	// Field descriptor #14 C
	OFFER('1'),

	// Field descriptor #14 C
	TRADE('2'),

	// Field descriptor #14 C
	//INDEX_VALUE('3'),

	// Field descriptor #14 C
	OPENING_PRICE('4'),

	// Field descriptor #14 C
	CLOSING_PRICE('5'),

	// Field descriptor #14 C
	//SETTLEMENT_PRICE('6'),

	// Field descriptor #14 C
	TRADING_SESSION_HIGH_PRICE('7'),

	// Field descriptor #14 C
	TRADING_SESSION_LOW_PRICE('8'),

	// Field descriptor #14 C
	TRADING_SESSION_VWAP_PRICE('9'),

	// Field descriptor #14 C
	//IMBALANCE('A'),

	// Field descriptor #14 C
	TRADE_VOLUME('B'),

	// Field descriptor #14 C
	//OPEN_INTEREST('C'),

	// Field descriptor #14 C
	//COMPOSITE_UNDERLYING_PRICE('D'),

	// Field descriptor #14 C
	//SIMULATED_SELL_PRICE('E'),

	// Field descriptor #14 C
	//SIMULATED_BUY_PRICE('F'),

	// Field descriptor #14 C
	//MARGIN_RATE('G'),

	// Field descriptor #14 C
	//MID_PRICE('H'),

	// Field descriptor #14 C
	EMPTY_BOOK('J');

	// Field descriptor #14 C
	//SETTLE_HIGH_PRICE('K'),

	// Field descriptor #14 C
	//SETTLE_LOW_PRICE('L'),

	// Field descriptor #14 C
	//PRIOR_SETTLE_PRICE('M'),

	// Field descriptor #14 C
	//SESSION_HIGH_BID('N'),

	// Field descriptor #14 C
	//SESSION_LOW_OFFER('O'),

	// Field descriptor #14 C
	//EARLY_PRICES('P'),

	// Field descriptor #14 C
	//AUCTION_CLEARING_PRICE('Q');

	private char value;

	private MDEntryTypes(char value) {
		this.value = value;
	}
	
	public static MDEntryTypes valueOf(char type){
		MDEntryTypes ret = null;
		for(MDEntryTypes et : MDEntryTypes.values()){
			if(et.value == type){
				ret = et;
				break;
			}
		}
		return ret;
	}

}
