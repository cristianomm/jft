/**
 * 
 */
package com.cmm.jft.trading;

import java.util.Comparator;


/**
 * <p>
 * <code>OrderPriorityComparator.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 20/12/2013 12:54:24
 *
 */
public class OrderPriorityComparator implements Comparator<Orders> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Orders ordr1, Orders ordr2) {

		if (ordr1 != null && ordr2 != null) {
			return ordr1.getOrderDateTime().compareTo(ordr2.getOrderDateTime());
		}
		throw new NullPointerException("Invalid parameter: ordr1= " + ordr1
				+ " ordr2= " + ordr2);
	}

}
