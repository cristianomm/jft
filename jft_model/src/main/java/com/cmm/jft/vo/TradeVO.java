package com.cmm.jft.vo;

import java.util.Date;


public class TradeVO implements Extractable, Comparable<TradeVO> {
	
	public String tradeID;
	public String securityID;
	
	public char tradeCondition;
	
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
	public int volume;
	public char agressor;
	
	
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

	public String getTradeID() {
	    return tradeID;
	}

	public void setTradeID(String tradeID) {
	    this.tradeID = tradeID;
	}

	public String getSecurityID() {
	    return securityID;
	}

	public void setSecurityID(String securityID) {
	    this.securityID = securityID;
	}

	public char getTradeCondition() {
	    return tradeCondition;
	}

	public void setTradeCondition(char tradeCondition) {
	    this.tradeCondition = tradeCondition;
	}

	public Date getSessionDate() {
	    return sessionDate;
	}

	public void setSessionDate(Date sessionDate) {
	    this.sessionDate = sessionDate;
	}

	public String getBuyOrderID() {
	    return buyOrderID;
	}

	public void setBuyOrderID(String buyOrderID) {
	    this.buyOrderID = buyOrderID;
	}

	public String getBuySecOrderID() {
	    return buySecOrderID;
	}

	public void setBuySecOrderID(String buySecOrderID) {
	    this.buySecOrderID = buySecOrderID;
	}

	public String getSellOrderID() {
	    return sellOrderID;
	}

	public void setSellOrderID(String sellOrderID) {
	    this.sellOrderID = sellOrderID;
	}

	public String getSellSecOrderID() {
	    return sellSecOrderID;
	}

	public void setSellSecOrderID(String sellSecOrderID) {
	    this.sellSecOrderID = sellSecOrderID;
	}

	public String getBuyBroker() {
	    return buyBroker;
	}

	public void setBuyBroker(String buyBroker) {
	    this.buyBroker = buyBroker;
	}

	public String getSellBroker() {
	    return sellBroker;
	}

	public void setSellBroker(String sellBroker) {
	    this.sellBroker = sellBroker;
	}

	public Date getTradeDate() {
	    return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
	    this.tradeDate = tradeDate;
	}

	public Date getTradeTime() {
	    return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
	    this.tradeTime = tradeTime;
	}

	public double getPrice() {
	    return price;
	}

	public void setPrice(double price) {
	    this.price = price;
	}

	public int getVolume() {
	    return volume;
	}

	public void setVolume(int volume) {
	    this.volume = volume;
	}
	
	/**
	 * @return the agressor
	 */
	public char getAgressor() {
	    return agressor;
	}
	
	/**
	 * @param agressor the agressor to set
	 */
	public void setAgressor(char agressor) {
	    this.agressor = agressor;
	}

}
