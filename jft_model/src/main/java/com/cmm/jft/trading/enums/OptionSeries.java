/**
 * 
 */
package com.cmm.jft.trading.enums;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>
 * <code>OptionSeries.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 18/10/2013 14:45:52
 *
 */
public enum OptionSeries {

	/*
	 * TIPO DA OPÇÃO CALL(OPC) PUT(OPV)
	 */

	/**
	 * JANEIRO
	 */
	A(Calendar.JANUARY, Side.BUY), M(Calendar.JANUARY, Side.SELL),
	/**
	 * FEVEREIRO
	 */
	B(Calendar.FEBRUARY, Side.BUY), N(Calendar.FEBRUARY, Side.SELL),
	/**
	 * MARÇO
	 */
	C(Calendar.MARCH, Side.BUY), O(Calendar.MARCH, Side.SELL),
	/**
	 * ABRIL
	 */
	D(Calendar.APRIL, Side.BUY), P(Calendar.APRIL, Side.SELL),
	/**
	 * MAIO
	 */
	E(Calendar.MAY, Side.BUY), Q(Calendar.MAY, Side.SELL),
	/**
	 * JUNHO
	 */
	F(Calendar.JUNE, Side.BUY), R(Calendar.JUNE, Side.SELL),
	/**
	 * JULHO
	 */
	G(Calendar.JULY, Side.BUY), S(Calendar.JULY, Side.SELL),
	/**
	 * AGOSTO
	 */
	H(Calendar.AUGUST, Side.BUY), T(Calendar.AUGUST, Side.SELL),
	/**
	 * SETEMBRO
	 */
	I(Calendar.SEPTEMBER, Side.BUY), U(Calendar.SEPTEMBER, Side.SELL),
	/**
	 * OUTUBRO
	 */
	J(Calendar.OCTOBER, Side.BUY), V(Calendar.OCTOBER, Side.SELL),
	/**
	 * NOVEMBRO
	 */
	K(Calendar.NOVEMBER, Side.BUY), W(Calendar.NOVEMBER, Side.SELL),
	/**
	 * DEZEMBRO
	 */
	L(Calendar.DECEMBER, Side.BUY), X(Calendar.DECEMBER, Side.SELL);

	int month;
	Side side;

	OptionSeries(int month, Side side) {
		this.month = month;
		this.side = side;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return this.month;
	}

	/**
	 * @return the side
	 */
	public Side getSide() {
		return this.side;
	}

	public static OptionSeries getByVal(Date expirationDate) {
		OptionSeries ret = null;
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(expirationDate);

		for (OptionSeries opt : OptionSeries.values()) {
			if (opt.month == gc.get(Calendar.MONTH)) {
				ret = opt;
				break;
			}
		}
		return ret;
	}

}
