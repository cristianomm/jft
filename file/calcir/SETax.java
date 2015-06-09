/**
 * 
 */
package com.cmm.jft.core.calcir;

import java.util.Date;

import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;

/**
 * <p>
 * <code>SETax.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 27/04/2014 15:41:17
 *
 */
public class SETax {

	public int codSETax;
	public String taxName;
	public double tax;
	public int taxType;

	/**
     * 
     */
	public SETax() {
	}

	public void init(String cod, String name, String tax, String taxType) {
		this.codSETax = (Integer) (FormatterFactory
				.getFormatter(FormatterTypes.INT).parse(cod));
		this.taxName = name;
		this.tax = (Double) (FormatterFactory
				.getFormatter(FormatterTypes.DOUBLE).parse(tax));
		this.taxType = (Integer) (FormatterFactory
				.getFormatter(FormatterTypes.INT).parse(taxType));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SETax [codSETax="
				+ this.codSETax
				+ ", "
				+ (this.taxName != null ? "taxName=" + this.taxName + ", " : "")
				+ "tax=" + this.tax + "]";
	}

}
