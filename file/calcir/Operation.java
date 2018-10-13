/**
 * 
 */
package com.cmm.jft.core.calcir;

import java.util.Date;

import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;

/**
 * <p>
 * <code>Operation.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 27/04/2014 15:11:32
 *
 */
public class Operation {

	public int tradeID;
	public String security;
	public Date date;
	public Side side;
	public TradeTypes type;
	public int quantity;
	public double total;
	public double comission;

	/**
     * 
     */
	public Operation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 * @param tradeID
	 * @param type
	 * @param security
	 * @param date
	 * @param side
	 * @param quantity
	 * @param value
	 */
	public void init(String tradeID, String type, String security, String date,
			String side, String quantity, String value) {
		this.tradeID = (Integer) (FormatterFactory
				.getFormatter(FormatterTypes.INT).parse(tradeID));
		this.type = TradeTypes.getByValue(type);
		this.security = security;
		this.date = (Date) (FormatterFactory
				.getFormatter(FormatterTypes.DATE_F8).parse(date));
		this.side = Side.getByValue(side);
		this.quantity = (Integer) (FormatterFactory
				.getFormatter(FormatterTypes.INT).parse(quantity));
		this.total = (Double) (FormatterFactory
				.getFormatter(FormatterTypes.DOUBLE).parse(value));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Operation [tradeID="
				+ this.tradeID
				+ ", "
				+ (this.security != null ? "security=" + this.security + ", "
						: "")
				+ (this.date != null ? "date=" + this.date + ", " : "")
				+ (this.side != null ? "side=" + this.side + ", " : "")
				+ (this.type != null ? "type=" + this.type + ", " : "")
				+ "quantity=" + this.quantity + ", total=" + this.total
				+ ", comission=" + this.comission + "]";
	}

}
