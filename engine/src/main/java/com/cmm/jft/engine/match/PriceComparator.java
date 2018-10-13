/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.Comparator;

import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.Side;

/**
 * <p>
 * <code>PriceComparator.java</code>
 * </p>
 *
 * @author cristiano
 * @version 03/08/2017 10:22:13
 *
 */
public class PriceComparator implements Comparator<Double> {

	private Side side;

	/**
	 * 
	 */
	public PriceComparator(Side side) {
		this.side = side;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Double price1, Double price2) {
		int comp = 0;

		// a==b
		if (price1 == price2) {
			comp = 0;
		}
		// a<b
		else if (price1 < price2) {
			comp = side == Side.SELL ? -1 : 1;
		}

		// a>b
		else if (price1 > price2) {
			comp = side == Side.SELL ? 1 : -1;
		}

		return comp;
	}

}
