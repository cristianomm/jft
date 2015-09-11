package com.cmm.jft.core.vo;

import java.util.Date;

public class TradeVO implements Extractable {
	
	public String buyOrderID;
	public String buySecOrderID;

	public String sellOrderID;
	public String sellSecOrderID;
	
	public int buyBroker;
	public int sellBroker;
	
	public Date tradeDate;
	public Date tradeTime;
	public double price;
	public double volume;	

}
