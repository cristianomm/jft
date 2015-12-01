/**
 * 
 */
package com.cmm.jft.core.format;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;


/**
 * <p>
 * <code>LongFormatter.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 15:59:53
 *
 */
public class LongFormatter implements Formatter<Long> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#format(java.lang.Object)
	 */
	@Override
	public String format(Long t) {
		return t.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#parse(java.lang.String)
	 */
	@Override
	public Long parse(String st) {
		Long ret = 0L;
		try {
			ret = Double.valueOf(Cleaner.clearNumericString(st)).longValue();// caso
																				// seja
																				// 99.00...
		} catch (NumberFormatException e) {
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
		return Long.TYPE.getName();
	}

}
