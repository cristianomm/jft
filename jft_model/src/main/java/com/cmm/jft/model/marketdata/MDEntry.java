package com.cmm.jft.model.marketdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.cmm.jft.model.trading.enums.MDEntryTypes;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.model.trading.enums.UpdateActions;
import com.cmm.jft.vo.Extractable;

public class MDEntry implements Extractable {

	/**
	 * Market Data fields
	 */
	private long mdEntryId;
	private UpdateActions mdUpdateAction;
	private MDEntryTypes mdEntryType;
	private Side side;
	private double mdEntryPx;
	private int mdEntrySize;
	private int numberOfOrders;
	private LocalDate entryDate;
	private LocalDateTime mdEntryDateTime;
	private char securityTradingStatus;
	private long orderId;
	private long tradeId;
	private String mdEntrySeller;
	private String mdEntryBuyer;
	private int mdEntryPosNo;
	private int tradeVolume;
	private int orderEvent;
	private int orderStatus;
	private LocalDateTime orderDate;
	private int orderCondition;
	private char agressor;

	private long buyOrdId;
	private long buySecOrdId;
	private long sellOrdId;
	private long sellSecOrdId;

	/**
	 * Instrument identification block
	 */
	private String symbol;
	private String securityId;
	private String securityIdSrc;
	private String securityExchange;

	/**
	 * 
	 */
	public MDEntry() {
		this.mdEntryDateTime = LocalDateTime.now();
	}

	public long getMdEntryId() {
		return mdEntryId;
	}

	public void setMdEntryId(long mdEntryId) {
		this.mdEntryId = mdEntryId;
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

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getTradeId() {
		return tradeId;
	}

	public void setTradeId(long tradeId) {
		this.tradeId = tradeId;
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

	public String getSecurityId() {
		return securityId;
	}

	public void setSecurityId(String securityId) {
		this.securityId = securityId;
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
	 * @return the buyOrdId
	 */
	public long getBuyOrdId() {
		return buyOrdId;
	}

	/**
	 * @return the buySecOrdId
	 */
	public long getBuySecOrdId() {
		return buySecOrdId;
	}

	/**
	 * @return the sellOrdId
	 */
	public long getSellOrdId() {
		return sellOrdId;
	}

	/**
	 * @return the sellSecOrdId
	 */
	public long getSellSecOrdId() {
		return sellSecOrdId;
	}

	/**
	 * @param buyOrdId the buyOrdId to set
	 */
	public void setBuyOrdId(long buyOrdId) {
		this.buyOrdId = buyOrdId;
	}

	/**
	 * @param buySecOrdId the buySecOrdId to set
	 */
	public void setBuySecOrdId(long buySecOrdId) {
		this.buySecOrdId = buySecOrdId;
	}

	/**
	 * @param sellOrdId the sellOrdId to set
	 */
	public void setSellOrdId(long sellOrdId) {
		this.sellOrdId = sellOrdId;
	}

	/**
	 * @param sellSecOrdId the sellSecOrdId to set
	 */
	public void setSellSecOrdId(long sellSecOrdId) {
		this.sellSecOrdId = sellSecOrdId;
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
