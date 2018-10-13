/**
 * 
 */
package com.cmm.jft.core.format;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;

/**
 * <p>
 * <code>DoubleFormatter.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 15:58:20
 *
 */
public class DoubleFormatter implements Formatter<Double> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#format(java.lang.Object)
	 */
	@Override
	public String format(Double t) {
		return t.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#parse(java.lang.String)
	 */
	@Override
	public Double parse(String st) {
		Double ret = 0d;
		try {
			ret = Double.parseDouble(Cleaner.clearNumericString(st));
		} catch (NumberFormatException e) {
			// e.printStackTrace();
			Logging.getInstance().log(DoubleFormatter.class, e, Level.ERROR);
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
		return Double.TYPE.getName();
	}

}
