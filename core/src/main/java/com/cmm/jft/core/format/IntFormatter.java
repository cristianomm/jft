/**
 * 
 */
package com.cmm.jft.core.format;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;

/**
 * <p>
 * <code>IntFormatter.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 15:53:47
 *
 */
public class IntFormatter implements Formatter<Integer> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft_ui.utils.Formatter#format(java.lang.Object)
	 */
	@Override
	public String format(Integer t) {
		return t.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft_ui.utils.Formatter#parse(java.lang.String)
	 */
	@Override
	public Integer parse(String st) {
		Integer ret = 0;
		try {
			ret = Cleaner.formatNumberAsInteger(Cleaner.clearNumericString(st));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft_ui.utils.Formatter#getName()
	 */
	@Override
	public String getName() {
		return Integer.TYPE.getName();
	}

}
