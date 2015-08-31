/**
 * 
 */
package com.cmm.jft.core.format;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * <code>FormatterTypes.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 16:06:59
 *
 */
public enum FormatterTypes {

	INT("", Integer.class), BOOLEAN("", Boolean.class), BIGDECIMAL("",
			BigDecimal.class), DOUBLE("", Double.class), LONG("", Long.class),

	/**
	 * d-M-y
	 */
	DATE_F1("d-M-y", Date.class),

	/**
	 * M-d-y
	 */
	DATE_F2("M-d-y", Date.class),

	/**
	 * y-M-d
	 */
	DATE_F3("y-M-d", Date.class),

	/**
	 * dd-MM-yy
	 */
	DATE_F4("dd-MM-yy", Date.class),

	/**
	 * MM-dd-yy
	 */
	DATE_F5("MM-dd-yy", Date.class),

	/**
	 * yy-MM-dd
	 */
	DATE_F6("yy-MM-dd", Date.class),

	/**
	 * dd-MM-yyyy
	 */
	DATE_F7("dd-MM-yyyy", Date.class),

	/**
	 * yyyy-MM-dd
	 */
	DATE_F8("yyyy-MM-dd", Date.class),

	/**
	 * yyyyMMdd
	 */
	DATE_F9("yyyyMMdd", Date.class),

	/**
	 * dd/MM/yyyy
	 */
	DATE_F10("dd/MM/yyyy", Date.class),

	/**
	 * hh:mm
	 */
	TIME_F1("hh:mm", Date.class),

	/**
	 * hh:mm:ss
	 */
	TIME_F2("hh:mm:ss", Date.class),

	/**
	 * hh:mm:ss,S
	 */
	TIME_F3("hh:mm:ss,S", Date.class),

	/**
	 * dd-MM-yy hh:mm
	 */
	DATE_TIME_F1("dd-MM-yy hh:mm", Date.class),

	/**
	 * dd-MM-yy hh:mm:ss
	 */
	DATE_TIME_F2("dd-MM-yy hh:mm:ss", Date.class),

	/**
	 * dd-MM-yy hh:mm:ss,S
	 */
	DATE_TIME_F3("dd-MM-yy hh:mm:ss,S", Date.class),

	/**
	 * dd-MM-yyyy hh:mm
	 */
	DATE_TIME_F4("dd-MM-yyyy hh:mm", Date.class),

	/**
	 * dd-MM-yyyy hh:mm:ss
	 */
	DATE_TIME_F5("dd-MM-yyyy hh:mm:ss", Date.class),

	/**
	 * dd-MM-yyyy hh:mm:ss,S
	 */
	DATE_TIME_F6("dd-MM-yyyy hh:mm:ss,S", Date.class),

	/**
	 * yyyy-MM-dd hh:mm
	 */
	DATE_TIME_F7("yyyy-MM-dd hh:mm", Date.class),

	/**
	 * yyyy-MM-dd hh:mm:ss
	 */
	DATE_TIME_F8("yyyy-MM-dd hh:mm:ss", Date.class),

	/**
	 * yyyy-MM-dd hh:mm:ss,S
	 */
	DATE_TIME_F9("yyyy-MM-dd hh:mm:ss,S", Date.class),
	
	/**
	 * yyyy.MM.dd hh:mm
	 */
	DATE_TIME_F10("yyyy.MM.dd hh:mm", Date.class),
	
	/**
	 * dd/MM/yyyy hh:mm:ss
	 */
	DATE_TIME_F11("dd/MM/yyyy hh:mm:ss", Date.class);
	
	
	
	Class classType;
	String format;

	FormatterTypes(String format, Class classType) {
		this.format = format;
	}

	public String getFormat() {
		return format;
	}

	public Class getClassType() {
		return classType;
	}

}
