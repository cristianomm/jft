/**
 * 
 */
package com.cmm.jft.core.format;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
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
public class DateTimeFormatter implements Formatter<LocalDateTime> {

    private java.time.format.DateTimeFormatter formatter;

    public DateTimeFormatter(String format) {
	this.formatter = java.time.format.DateTimeFormatter.ofPattern(format);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.core.format.Formatter#format(java.lang.Object)
     */
    @Override
    public String format(LocalDateTime t) {
	String ret = "";
	try {
	    ret = formatter.format(t);
	} catch (Exception e) {
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
    public LocalDateTime parse(String st) {
	LocalDateTime ret = null;
	try {
	    ret = LocalDateTime.parse(st, formatter);
	} catch (DateTimeParseException e) {
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
