/**
 * 
 */
package com.cmm.jft.data.extractor;


/**
 * <p><code>Invoice.java</code></p>
 * @author Cristiano M Martins
 * @version 19/02/2015 10:15:29
 *
 */
public class Invoice implements Extractable {
	
	public String date;
	public String invoice;
	
	public String sellTotal;
	public String buyTotal;
	public String netOp;
	public String sumOp;
	public String cblcTotal;
	public String exchangeTotal;
	
	public String liquidationTax;
	public String registerTax;
	public String fee;
	public String brokerage;
	public String iss;
		
	public String swingTradeBase;
	public String dayTradeBase;
	
	public String swingTradeIRRF;
	public String dayTradeIRRF;
	
	public String others;
	public String sumExpense;
	public String otherCost;
	
	public String optionSell;
	public String optionBuy;
	public String swingAdjust;
	public String dayTradeAdjust;
	public String brokerageIRRF;
	public String investmentAccTotal;
	public String total;
	public String invoiceTotal;
	
	
	public String csvLine(){
		return this.date + "; " + this.invoice + "; " + this.sellTotal + "; "
				+ this.buyTotal + "; " + this.netOp + "; " + this.sumOp + "; "
				+ this.cblcTotal + "; " + this.exchangeTotal + "; "
				+ this.liquidationTax + "; " + this.registerTax + "; "
				+ this.fee + "; " + this.brokerage + "; " + this.iss + "; "
				+ this.swingTradeBase + "; " + this.dayTradeBase + "; "
				+ this.swingTradeIRRF + "; " + this.dayTradeIRRF + "; "
				+ this.others + "; " + this.sumExpense + "; " + this.otherCost
				+ "; " + this.optionSell + "; " + this.optionBuy + "; "
				+ this.swingAdjust + "; " + this.dayTradeAdjust + "; "
				+ this.brokerageIRRF + "; " + this.investmentAccTotal + "; "
				+ this.total + "; " + this.invoiceTotal;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Invoice [date=" + this.date + ", invoice=" + this.invoice
				+ ", sellTotal=" + this.sellTotal + ", buyTotal="
				+ this.buyTotal + ", netOp=" + this.netOp + ", sumOp="
				+ this.sumOp + ", cblcTotal=" + this.cblcTotal
				+ ", exchangeTotal=" + this.exchangeTotal + ", liquidationTax="
				+ this.liquidationTax + ", registerTax=" + this.registerTax
				+ ", fee=" + this.fee + ", brokerage=" + this.brokerage
				+ ", iss=" + this.iss + ", swingTradeBase="
				+ this.swingTradeBase + ", dayTradeBase=" + this.dayTradeBase
				+ ", swingTradeIRRF=" + this.swingTradeIRRF + ", dayTradeIRRF="
				+ this.dayTradeIRRF + ", others=" + this.others
				+ ", sumExpense=" + this.sumExpense + ", otherCost="
				+ this.otherCost + ", optionSell=" + this.optionSell
				+ ", optionBuy=" + this.optionBuy + ", swingAdjust="
				+ this.swingAdjust + ", dayTradeAdjust=" + this.dayTradeAdjust
				+ ", brokerageIRRF=" + this.brokerageIRRF
				+ ", investmentAccTotal=" + this.investmentAccTotal
				+ ", total=" + this.total + ", invoiceTotal="
				+ this.invoiceTotal + "]";
	}
	
}
