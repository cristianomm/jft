package com.cmm.jft.vo;

import java.util.Date;


public class TradeVO implements Extractable, Comparable<TradeVO> {
	
	public String tradeID;
	public String securityID;
	
	public Date sessionDate;
	
	public String buyOrderID;
	public String buySecOrderID;

	public String sellOrderID;
	public String sellSecOrderID;
	
	public String buyBroker;
	public String sellBroker;
	
	public Date tradeDate;
	public Date tradeTime;
	public double price;
	public double volume;
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(TradeVO o) {
		int compDt = this.tradeDate.compareTo(o.tradeDate);
		int compTm = this.tradeTime.compareTo(o.tradeTime);
		int ret = 0;
		if (compDt == 0) {
			ret = compTm;
		} else if (compDt < 0) {
			ret = compDt;
		} else {
			ret = 1;
		}

		return ret;
	}
	
	
	

}
