/**
 * 
 */
package com.cmm.jft.core.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;


/**
 * <p>
 * <code>DateTimeFormatter.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 16:47:18
 *
 */
public class DateTimeFormatter implements Formatter<Date> {

	private SimpleDateFormat formatter;

	public DateTimeFormatter(String format) {
		this.formatter = new SimpleDateFormat(format);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#format(java.lang.Object)
	 */
	@Override
	public String format(Date t) {
		String ret = "";
		try {
			ret = formatter.format(t);
		} catch (Exception e) {
			// e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.core.format.Formatter#parse(java.lang.String)
	 */
	@Override
	public Date parse(String st) {
		Date ret = null;
		try {
			ret = formatter.parse(st);
		} catch (ParseException e) {
			//e.printStackTrace();
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
		return "DateTime";
	}

}
