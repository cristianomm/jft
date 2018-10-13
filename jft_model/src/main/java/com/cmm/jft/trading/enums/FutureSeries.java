/**
 * 
 */
package com.cmm.jft.trading.enums;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>
 * <code>FutureSeries.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 18/10/2013 03:15:39
 *
 */
public enum FutureSeries {

	/**
	 * F - Contrato com Vencimento em Janeiro
	 */
	F(Calendar.JANUARY),

	/**
	 * G - Contrato com Vencimento em Fevereiro
	 */
	G(Calendar.FEBRUARY),

	/**
	 * H - Contrato com Vencimento em Marco
	 */
	H(Calendar.MARCH),

	/**
	 * J - Contrato com Vencimento em Abril
	 */
	J(Calendar.APRIL),

	/**
	 * K - Contrato com Vencimento em Maio
	 */
	K(Calendar.MAY),

	/**
	 * M - Contrato com Vencimento em Junho
	 */
	M(Calendar.JUNE),

	/**
	 * N - Contrato com Vencimento em Julho
	 */
	N(Calendar.JULY),

	/**
	 * Q - Contrato com Vencimento em Agosto
	 */
	Q(Calendar.AUGUST),

	/**
	 * U - Contrato com Vencimento em Setembro
	 */
	U(Calendar.SEPTEMBER),

	/**
	 * V - Contrato com Vencimento em Outubro
	 */
	V(Calendar.OCTOBER),

	/**
	 * X - Contrato com Vencimento em Novembro
	 */
	X(Calendar.NOVEMBER),

	/**
	 * Z - Contrato com Vencimento em Dezembro
	 */
	Z(Calendar.DECEMBER);

	int month;

	FutureSeries(int month) {
		this.month = month;
	}

	/**
	 * @return the month
	 */
	public int getMonth() {
		return this.month;
	}

	public static FutureSeries getByVal(Date expirationDate) {
		FutureSeries ret = null;
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(expirationDate);

		for (FutureSeries f : FutureSeries.values()) {
			if (f.month == gc.get(Calendar.MONTH)) {
				ret = f;
				break;
			}
		}
		return ret;
	}

}
