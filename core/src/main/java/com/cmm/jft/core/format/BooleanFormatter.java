/**
 * 
 */
package com.cmm.jft.core.format;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;


/**
 * <p>
 * <code>BooleanFormatter.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 16:01:00
 *
 */
public class BooleanFormatter implements Formatter<Boolean> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#format(java.lang.Object)
	 */
	@Override
	public String format(Boolean t) {
		return t.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#parse(java.lang.String)
	 */
	@Override
	public Boolean parse(String st) {
		Boolean ret = false;
		try {
			ret = Boolean.parseBoolean(st);
		} catch (Exception e) {
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
		return Boolean.TYPE.getName();
	}

}
