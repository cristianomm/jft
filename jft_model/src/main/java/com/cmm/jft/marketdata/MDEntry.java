package com.cmm.jft.marketdata;

import java.util.Date;


public class MDEntry {
	
	/**
	 * Market Data fields
	 */
	private String mdEntryID;
	private char mdUpdateAction;
	private char mdEntryType;
	private double mdEntryPx;
	private int mdEntrySize;
	private int numberOfOrders;
	private Date mdEntryDate;
	private Date mdEntryTime;
	private char securityTradingStatus;
	private String orderID;
	private String mdEntrySeller;
	private String mdEntryBuyer;
	private int mdEntryPosNo;
	private int tradeVolume;
	
	/**
	 * Instrument identification block
	 */
	private String securityID;
	private String securityIdSrc;
	private String securityExchange;
	
	
	
	
}
