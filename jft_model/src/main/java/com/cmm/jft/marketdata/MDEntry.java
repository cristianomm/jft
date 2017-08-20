package com.cmm.jft.marketdata;

import java.util.Date;

import com.cmm.jft.trading.enums.MDEntryTypes;
import com.cmm.jft.trading.enums.UpdateActions;


public class MDEntry {

    /**
     * Market Data fields
     */
    private String mdEntryID;
    private UpdateActions mdUpdateAction;
    private MDEntryTypes mdEntryType;
    private double mdEntryPx;
    private int mdEntrySize;
    private int numberOfOrders;
    private Date mdEntryDate;
    private Date mdEntryTime;
    private char securityTradingStatus;
    private String orderID;
    private String tradeID;
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
    
    
    
    /**
     * 
     */
    public MDEntry() {
	Date d = new Date();
	this.mdEntryDate = d;
	this.mdEntryTime = d;
    }
    
    
    public String getMdEntryID() {
	return mdEntryID;
    }
    public void setMdEntryID(String mdEntryID) {
	this.mdEntryID = mdEntryID;
    }
    public UpdateActions getMdUpdateAction() {
	return mdUpdateAction;
    }
    public void setMdUpdateAction(UpdateActions mdUpdateAction) {
	this.mdUpdateAction = mdUpdateAction;
    }
    public MDEntryTypes getMdEntryType() {
	return mdEntryType;
    }
    public void setMdEntryType(MDEntryTypes mdEntryType) {
	this.mdEntryType = mdEntryType;
    }
    public double getMdEntryPx() {
	return mdEntryPx;
    }
    public void setMdEntryPx(double mdEntryPx) {
	this.mdEntryPx = mdEntryPx;
    }
    public int getMdEntrySize() {
	return mdEntrySize;
    }
    public void setMdEntrySize(int mdEntrySize) {
	this.mdEntrySize = mdEntrySize;
    }
    public int getNumberOfOrders() {
	return numberOfOrders;
    }
    public void setNumberOfOrders(int numberOfOrders) {
	this.numberOfOrders = numberOfOrders;
    }
    public Date getMdEntryDate() {
	return mdEntryDate;
    }
    public void setMdEntryDate(Date mdEntryDate) {
	this.mdEntryDate = mdEntryDate;
    }
    public Date getMdEntryTime() {
	return mdEntryTime;
    }
    public void setMdEntryTime(Date mdEntryTime) {
	this.mdEntryTime = mdEntryTime;
    }
    public char getSecurityTradingStatus() {
	return securityTradingStatus;
    }
    public void setSecurityTradingStatus(char securityTradingStatus) {
	this.securityTradingStatus = securityTradingStatus;
    }
    public String getOrderID() {
	return orderID;
    }
    public void setOrderID(String orderID) {
	this.orderID = orderID;
    }
    public String getTradeID() {
	return tradeID;
    }
    public void setTradeID(String tradeID) {
	this.tradeID = tradeID;
    }
    public String getMdEntrySeller() {
	return mdEntrySeller;
    }
    public void setMdEntrySeller(String mdEntrySeller) {
	this.mdEntrySeller = mdEntrySeller;
    }
    public String getMdEntryBuyer() {
	return mdEntryBuyer;
    }
    public void setMdEntryBuyer(String mdEntryBuyer) {
	this.mdEntryBuyer = mdEntryBuyer;
    }
    public int getMdEntryPosNo() {
	return mdEntryPosNo;
    }
    public void setMdEntryPosNo(int mdEntryPosNo) {
	this.mdEntryPosNo = mdEntryPosNo;
    }
    public int getTradeVolume() {
	return tradeVolume;
    }
    public void setTradeVolume(int tradeVolume) {
	this.tradeVolume = tradeVolume;
    }
    public String getSecurityID() {
	return securityID;
    }
    public void setSecurityID(String securityID) {
	this.securityID = securityID;
    }
    public String getSecurityIdSrc() {
	return securityIdSrc;
    }
    public void setSecurityIdSrc(String securityIdSrc) {
	this.securityIdSrc = securityIdSrc;
    }
    public String getSecurityExchange() {
	return securityExchange;
    }
    public void setSecurityExchange(String securityExchange) {
	this.securityExchange = securityExchange;
    }

}
