package com.cmm.jft.marketdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.cmm.jft.trading.enums.MDEntryTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.UpdateActions;
import com.cmm.jft.vo.Extractable;

public class MDEntry implements Extractable {

	/**
	 * Market Data fields
	 */
	private long mdEntryID;
	private UpdateActions mdUpdateAction;
	private MDEntryTypes mdEntryType;
	private Side side;
	private double mdEntryPx;
	private int mdEntrySize;
	private int numberOfOrders;
	private LocalDate entryDate;
	private LocalDateTime mdEntryDateTime;
	private char securityTradingStatus;
	private long orderID;
	private String tradeID;
	private String mdEntrySeller;
	private String mdEntryBuyer;
	private int mdEntryPosNo;
	private int tradeVolume;
	private int orderEvent;
	private int orderStatus;
	private LocalDateTime orderDate;
	private int orderCondition;
	private char agressor;

	private long buyOrdID;
	private long buySecOrdID;
	private long sellOrdID;
	private long sellSecOrdID;

	/**
	 * Instrument identification block
	 */
	private String symbol;
	private String securityID;
	private String securityIdSrc;
	private String securityExchange;

	/**
	 * 
	 */
	public MDEntry() {
		this.mdEntryDateTime = LocalDateTime.now();
	}

	public long getMdEntryID() {
		return mdEntryID;
	}

	public void setMdEntryID(long mdEntryID) {
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

	/**
	 * @return the mdEntryDateTime
	 */
	public LocalDateTime getMdEntryDateTime() {
		return mdEntryDateTime;
	}

	/**
	 * @param mdEntryDateTime the mdEntryDateTime to set
	 */
	public void setMdEntryDateTime(LocalDateTime mdEntryTime) {
		this.mdEntryDateTime = mdEntryTime;
	}

	public char getSecurityTradingStatus() {
		return securityTradingStatus;
	}

	public void setSecurityTradingStatus(char securityTradingStatus) {
		this.securityTradingStatus = securityTradingStatus;
	}

	public long getOrderID() {
		return orderID;
	}

	public void setOrderID(long orderID) {
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

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the side
	 */
	public Side getSide() {
		return side;
	}

	/**
	 * @param side the side to set
	 */
	public void setSide(Side side) {
		this.side = side;
	}

	/**
	 * @return the orderEvent
	 */
	public int getOrderEvent() {
		return orderEvent;
	}

	/**
	 * @param orderEvent the orderEvent to set
	 */
	public void setOrderEvent(int orderEvent) {
		this.orderEvent = orderEvent;
	}

	/**
	 * @return the orderStatus
	 */
	public int getOrderStatus() {
		return orderStatus;
	}

	/**
	 * @param orderStatus the orderStatus to set
	 */
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the orderDate
	 */
	public LocalDateTime getOrderDate() {
		return orderDate;
	}

	/**
	 * @param orderDate the orderDate to set
	 */
	public void setOrderDate(LocalDateTime orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the orderCondition
	 */
	public int getOrderCondition() {
		return orderCondition;
	}

	/**
	 * @param orderCondition the orderCondition to set
	 */
	public void setOrderCondition(int orderCondition) {
		this.orderCondition = orderCondition;
	}

	/**
	 * @return the buyOrdID
	 */
	public long getBuyOrdID() {
		return buyOrdID;
	}

	/**
	 * @return the buySecOrdID
	 */
	public long getBuySecOrdID() {
		return buySecOrdID;
	}

	/**
	 * @return the sellOrdID
	 */
	public long getSellOrdID() {
		return sellOrdID;
	}

	/**
	 * @return the sellSecOrdID
	 */
	public long getSellSecOrdID() {
		return sellSecOrdID;
	}

	/**
	 * @param buyOrdID the buyOrdID to set
	 */
	public void setBuyOrdID(long buyOrdID) {
		this.buyOrdID = buyOrdID;
	}

	/**
	 * @param buySecOrdID the buySecOrdID to set
	 */
	public void setBuySecOrdID(long buySecOrdID) {
		this.buySecOrdID = buySecOrdID;
	}

	/**
	 * @param sellOrdID the sellOrdID to set
	 */
	public void setSellOrdID(long sellOrdID) {
		this.sellOrdID = sellOrdID;
	}

	/**
	 * @param sellSecOrdID the sellSecOrdID to set
	 */
	public void setSellSecOrdID(long sellSecOrdID) {
		this.sellSecOrdID = sellSecOrdID;
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

	/**
	 * @return the entryDate
	 */
	public LocalDate getEntryDate() {
		return entryDate;
	}

	/**
	 * @param entryDate the entryDate to set
	 */
	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}

}
