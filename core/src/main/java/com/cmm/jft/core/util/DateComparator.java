/**
 * 
 */
package com.cmm.jft.core.util;

import java.util.Comparator;
import java.util.Date;

/**
 * <p>
 * <code>DateComparator.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 18/10/2013 01:36:14
 *
 */
public class DateComparator implements Comparator<Date> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(Date dt1, Date dt2) {
		return dt1.compareTo(dt2);
	}

}
