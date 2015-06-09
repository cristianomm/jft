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
	Market,
	
	/**
	 * 2	=	Limit		[Limit]
	 */
	Limit,
	
	/**
	 * 3	=	Stop / Stop Loss		[Stop]
	 */
	Stop,
	
	/**
	 * 4	=	Stop Limit		[StopLimit]
	 */
	StopLimit,
	
	/**
	 * 6	=	With Or Without		[WithOrWithout]
	 */
	WithOrWithout,
	
	/**
	 * 7	=	Limit Or Better	[LimitOrBetter]
	 */
	LimitOrBetter,
	
	/**
	 * 8	=	Limit With Or Without		[LimitWithOrWithout]
	 */
	LimitWithOrWithout,
	
	/**
	 * 9	=	On Basis		[OnBasis]
	 */
	OnBasis,
	
	/**
	 * D	=	Previously Quoted		[PreviouslyQuoted]
	 */
	PreviouslyQuoted,
	
	/**
	 * E	=	Previously Indicated		[PreviouslyIndicated]
	 */
	PreviouslyIndicated,
	
	/**
	 * G	=	Forex Swap		[ForexSwap]
	 */
	ForexSwap,
	
	/**
	 * I	=	Funari (Limit day order with unexecuted portion handles as Market On Close. E.g. Japan)		[Funari]
	 */
	Funari,
	
	/**
	 * J	=	Market If Touched (MIT)		[MarketIfTouched]
	 */
	MarketIfTouched,
	
	/**
	 * K	=	Market With Left Over as Limit (market order with unexecuted quantity becoming limit order at last price)		[MarketWithLeftOverAsLimit]
	 */
	MarketWithLeftOverAsLimit,
	
	/**
	 * L	=	Previous Fund Valuation Point (Historic pricing; for CIV)		[PreviousFundValuationPoint]
	 */
	PreviousFundValuationPoint,
	
	/**
	 * M	=	Next Fund Valuation Point (Forward pricing; for CIV)		[NextFundValuationPoint]
	 */
	NextFundValuationPoint,
	
	/**
	 * P	=	Pegged		[Pegged]
	 */
	Pegged,
	/**
	 * Q	=	Counter-order selection		[CounterOrderSelection]
	 */
	CounterOrderSelection;
	
	
	public static OrderTypes getByValue(String value) {
		OrderTypes ot = null;

		for (OrderTypes ots : OrderTypes.values()) {
			if (ots.name().equalsIgnoreCase(value)) {
				ot = ots;
			}
		}

		return ot;
	}

}
