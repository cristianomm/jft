/**
 * 
 */
package com.cmm.jft.core.calcir;

import java.util.Date;

import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;

/**
 * <p>
 * <code>Comission.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 27/04/2014 15:32:31
 *
 */
public class Comission {

	public int codComission;
	public String comissionName;
	public TradeTypes type;
	public Date date;
	public double value;

	/**
     * 
     */
	public Comission() {
		// TODO Auto-generated constructor stub
	}

	public void init(String cod, String date, String name, String value) {
		this.codComission = (Integer) (FormatterFactory
				.getFormatter(FormatterTypes.INT).parse(cod));
		this.type = TradeTypes.getByValue(name);
		this.comissionName = type.name();
		this.date = (Date) (FormatterFactory
				.getFormatter(FormatterTypes.DATE_F8).parse(date));
		this.value = (Double) (FormatterFactory
				.getFormatter(FormatterTypes.DOUBLE).parse(value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Comission [codComission="
				+ this.codComission
				+ ", "
				+ (this.comissionName != null ? "comissionName="
						+ this.comissionName + ", " : "")
				+ (this.date != null ? "date=" + this.date + ", " : "")
				+ "value=" + this.value + "]";
	}

}
