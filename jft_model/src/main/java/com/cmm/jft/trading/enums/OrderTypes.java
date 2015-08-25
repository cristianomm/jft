/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p>
 * <code>OrderTypes.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 07/08/2013 02:31:10
 *
 */
public enum OrderTypes {
	// http://usequities.nyx.com/markets/nyse-arca-equities/order-types
	//https://www.nyse.com/markets/nyse-arca/trading-info
		
	/**
	 * 	1	=	Market		[Market]
	 */
	Market('1'),
	
	/**
	 * 2	=	Limit		[Limit]
	 */
	Limit('2'),
	
	/**
	 * 3	=	Stop / Stop Loss		[Stop]
	 */
	Stop('3'),
	
	/**
	 * 4	=	Stop Limit		[StopLimit]
	 */
	StopLimit('4'),
	
	/**
	 * 6	=	With Or Without		[WithOrWithout]
	 */
	WithOrWithout('6'),
	
	/**
	 * 7	=	Limit Or Better	[LimitOrBetter]
	 */
	LimitOrBetter('7'),
	
	/**
	 * 8	=	Limit With Or Without		[LimitWithOrWithout]
	 */
	LimitWithOrWithout('8'),
	
	/**
	 * 9	=	On Basis		[OnBasis]
	 */
	OnBasis('9'),
	
	/**
	 * D	=	Previously Quoted		[PreviouslyQuoted]
	 */
	PreviouslyQuoted('D'),
	
	/**
	 * E	=	Previously Indicated		[PreviouslyIndicated]
	 */
	PreviouslyIndicated('E'),
	
	/**
	 * G	=	Forex Swap		[ForexSwap]
	 */
	ForexSwap('G'),
	
	/**
	 * I	=	Funari (Limit day order with unexecuted portion handles as Market On Close. E.g. Japan)		[Funari]
	 */
	Funari('I'),
	
	/**
	 * J	=	Market If Touched (MIT)		[MarketIfTouched]
	 */
	MarketIfTouched('J'),
	
	/**
	 * K	=	Market With Left Over as Limit (market order with unexecuted quantity becoming limit order at last price)		[MarketWithLeftOverAsLimit]
	 */
	MarketWithLeftOverAsLimit('K'),
	
	/**
	 * L	=	Previous Fund Valuation Point (Historic pricing; for CIV)		[PreviousFundValuationPoint]
	 */
	PreviousFundValuationPoint('L'),
	
	/**
	 * M	=	Next Fund Valuation Point (Forward pricing; for CIV)		[NextFundValuationPoint]
	 */
	NextFundValuationPoint('M'),
	
	/**
	 * P	=	Pegged		[Pegged]
	 */
	Pegged('P'),
	/**
	 * Q	=	Counter-order selection		[CounterOrderSelection]
	 */
	CounterOrderSelection('Q');
	
	char value;
	
	private OrderTypes(char value) {
		this.value = value;
	}
	
	public char getValue() {
		return value;
	}
	
	public static OrderTypes getByValue(char value) {
		OrderTypes ot = null;

		for (OrderTypes ots : OrderTypes.values()) {
			if (ots.value == value) {
				ot = ots;
			}
		}

		return ot;
	}
	
}
