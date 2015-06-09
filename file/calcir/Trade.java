/**
 * 
 */
package com.cmm.jft.core.calcir;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * <p>
 * <code>Trade.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 27/04/2014 17:25:43
 *
 */
public class Trade {

	private static final double TAX_EMOLUMENTOS = .00005;
	private static final double TAX_LIQUIDACAO = .0002;
	private static final double TAX_ISS = .05;
	private static final double TAX_IRRF_PD = 0.01;
	private static final double TAX_IRRF_PS = 0.0005;
	private static final double TAX_IRRF_D = 0.2;
	private static final double TAX_IRRF_S = 0.15;

	public int codTrade;
	public String security;
	public Date date;
	public TradeTypes type;
	private Resumo resumo;
	public double profit;
	public double comissionTotal;
	private List<Operation> buy;
	private List<Operation> sell;

	/**
     * 
     */
	public Trade(int codTrade, String security, TradeTypes type, Resumo resumo) {
		this.codTrade = codTrade;
		this.security = security;
		this.type = type;
		if (resumo != null) {
			this.resumo = resumo;
			this.profit = resumo.profit;
			this.comissionTotal = resumo.comission;
		}
		this.buy = new ArrayList<Operation>();
		this.sell = new ArrayList<Operation>();
	}

	public void addOperation(Operation op) {
		if (op != null && op.tradeID == codTrade) {
			switch (op.side) {
			case BUY:
				buy.add(op);
				break;
			case SELL:
				sell.add(op);
				break;
			}
			this.date = op.date;
		}
	}

	public double getTotalBuy() {
		double tot = 0;
		for (Operation op : buy) {
			tot += op.total;
		}
		return tot;
	}

	public double getTotalSell() {
		double tot = 0;
		for (Operation op : sell) {
			tot += op.total;
		}
		return tot;
	}

	public double getTotalBuyComission() {
		double tot = 0;
		for (Operation op : buy) {
			tot += op.comission;
		}
		return tot;
	}

	public double getTotalSellComission() {
		double tot = 0;
		for (Operation op : sell) {
			tot += op.comission;
		}
		return tot;
	}

	public double getTotalOperation() {
		return getTotalBuy() + getTotalSell();
	}

	public double getProfit() {
		if (resumo == null) {
			profit = getTotalSell() - getTotalBuy();
		}
		return profit;
	}

	public double getTotalComission() {
		if (resumo == null) {
			comissionTotal = getTotalBuyComission() + getTotalSellComission();
		}
		return comissionTotal;
	}

	public double getEmolumentos() {

		String val = "" + (getTotalOperation() * TAX_EMOLUMENTOS);
		val = val.substring(0, val.indexOf(".") + 3);
		return Double.parseDouble(val);
	}

	public double getTaxLiquidacao() {
		String val = "" + (getTotalOperation() * TAX_LIQUIDACAO);
		val = val.substring(0, val.indexOf(".") + 3);
		return Double.parseDouble(val);
	}

	public double getISS() {
		String val = "" + (getTotalComission() * TAX_ISS);
		val = val.substring(0, val.indexOf(".") + 3);
		return Double.parseDouble(val);
	}

	public double getValorLiquido() {
		return getProfit()
				- (getTotalComission() + getEmolumentos() + getTaxLiquidacao() + getISS());
	}

	public double getIRBase() {
		double irBase = (getValorLiquido() + getISS());
		return irBase > 0 ? irBase : 0;
	}

	public double getIRProvisao() {
		double taxIRRF = (type == TradeTypes.DAY_TRADE ? TAX_IRRF_PD
				: TAX_IRRF_PS);
		return getIRBase() * taxIRRF;
	}

	public double getIR() {
		double taxIRRF = (type == TradeTypes.DAY_TRADE ? TAX_IRRF_D
				: TAX_IRRF_S);
		return getIRBase() * taxIRRF;
	}

}
