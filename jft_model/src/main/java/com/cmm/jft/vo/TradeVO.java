package com.cmm.jft.vo;

import java.util.Date;

import com.cmm.jft.data.extractor.Extractable;


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
