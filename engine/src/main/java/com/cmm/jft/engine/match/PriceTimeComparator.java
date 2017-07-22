/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.Comparator;

import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.Side;

/**
 * <p>
 * <code>PriceTimeComparator.java</code>
 * </p>
 * 
 * @author Cristiano
 * @version 20 de ago de 2015 00:20:35
 *
 */
public class PriceTimeComparator implements Comparator<Orders> {

    
    
    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    @Override
    public int compare(Orders ordr_a, Orders ordr_b) {

	int result = 0;
	if (ordr_a != null && ordr_b != null) {
	    int dtComp = ordr_a.getOrderDateTime().compareTo(ordr_b.getOrderDateTime());
	    if (ordr_a.getSide() == Side.BUY && ordr_b.getSide() == Side.BUY) {
		switch (comparePrice(ordr_a, ordr_b)) {
		case -1:
		    result = 1;
		    break;

		case 0:
		    result = dtComp;
		    break;

		case 1:
		    result = -1;
		    break;
		}

	    } else if (ordr_a.getSide() == Side.SELL && ordr_b.getSide() == Side.SELL) {
		if ((result = comparePrice(ordr_a, ordr_b)) == 0) {
		    result = dtComp;
		}
	    }

	} else {
	    throw new NullPointerException(String.format("Argument null: order_a: %s, order_b: %s.", ordr_a, ordr_b));
	}

	return result;
    }

    private int comparePrice(Orders ordr_a, Orders ordr_b) {
	int comp = 0;
	// a<b
	if (ordr_a.getPrice() < ordr_b.getPrice()) {
	    comp = -1;
	}

	// a==b
	else if (ordr_a.getPrice() == ordr_b.getPrice()) {
	    comp = 0;
	}

	// a>b
	else if (ordr_a.getPrice() > ordr_b.getPrice()) {
	    comp = 1;
	}

	return comp;
    }

}
