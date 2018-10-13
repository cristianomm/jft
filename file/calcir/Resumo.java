/**
 * 
 */
package com.cmm.jft.core.calcir;

import java.util.Date;

import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;

/**
 * <p>
 * <code>Resumo.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 28/04/2014 23:30:01
 *
 */
public class Resumo {

	public Date date;
	public double vBovespa;
	public double vBMF;
	public int orders;
	public double profit;
	public double comission;
	public int ativos;

	public Resumo() {
		// TODO Auto-generated constructor stub
	}

	public void init(String date, String vBovespa, String vBMF, String orders,
			String profit, String comission, String ativos) {
		this.date = (Date) (FormatterFactory
				.getFormatter(FormatterTypes.DATE_F10).parse(date));
		this.vBovespa = (Double) (FormatterFactory
				.getFormatter(FormatterTypes.DOUBLE).parse(vBovespa.replace(
				".", ",")));
		this.vBMF = (Double) (FormatterFactory
				.getFormatter(FormatterTypes.DOUBLE).parse(vBMF.replace(".",
				",")));
		this.orders = (Integer) (FormatterFactory
				.getFormatter(FormatterTypes.INT).parse(orders));
		this.profit = (Double) (FormatterFactory
				.getFormatter(FormatterTypes.DOUBLE).parse(profit.replace(".",
				",")));
		this.comission = (Double) (FormatterFactory
				.getFormatter(FormatterTypes.DOUBLE).parse(comission.replace(
				".", ",")));
		this.ativos = (Integer) (FormatterFactory
				.getFormatter(FormatterTypes.INT).parse(ativos));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Resumo ["
				+ (this.date != null ? "date=" + this.date + ", " : "")
				+ "vBovespa=" + this.vBovespa + ", vBMF=" + this.vBMF
				+ ", orders=" + this.orders + ", profit=" + this.profit
				+ ", comission=" + this.comission + ", ativos=" + this.ativos
				+ "]";
	}

}
