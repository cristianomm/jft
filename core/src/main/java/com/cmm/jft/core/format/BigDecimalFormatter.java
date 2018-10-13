/**
 * 
 */
package com.cmm.jft.core.format;

import java.math.BigDecimal;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;


/**
 * <p>
 * <code>BigDecimalFormatter.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 16:03:14
 *
 */
public class BigDecimalFormatter implements Formatter<BigDecimal> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#format(java.lang.Object)
	 */
	@Override
	public String format(BigDecimal t) {
		return t.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#parse(java.lang.String)
	 */
	@Override
	public BigDecimal parse(String st) {
		BigDecimal ret = new BigDecimal(0);
		try {
			ret = new BigDecimal(st.isEmpty() || st == null ? "0"
					: Cleaner.clearNumericString(st));
		} catch (NumberFormatException e) {
			System.out.println("valor:" + st);
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#getName()
	 */
	@Override
	public String getName() {
		return BigDecimal.class.getName();
	}

}
