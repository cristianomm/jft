/**
 * 
 */
package com.cmm.jft.core.format;

/**
 * <p>
 * <code>FloatFormatter.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 11/09/2013 01:06:13
 *
 */
public class FloatFormatter implements Formatter<Float> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#format(java.lang.Object)
	 */
	@Override
	public String format(Float t) {
		return t.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#parse(java.lang.String)
	 */
	@Override
	public Float parse(String st) {
		Float ret = 0f;
		try {
			ret = Float.parseFloat(Cleaner.clearNumericString(st));
		} catch (NumberFormatException e) {
			e.printStackTrace();
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
		return Float.TYPE.getName();
	}

}
