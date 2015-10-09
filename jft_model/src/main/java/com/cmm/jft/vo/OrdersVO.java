/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cmm.jft.vo.OrderEventVO;
import com.cmm.jft.vo.OrdersVO;
import com.cmm.jft.db.DBObject;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.enums.WorkingIndicator;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;


/**
 *
 * <p>
 * <code>OrdersVO</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
public class OrdersVO implements DBObject<OrdersVO> {
	
	public SimpleLongProperty orderID;
	
	public SimpleDoubleProperty price;
	
	public SimpleDoubleProperty stopPrice;
	
	public SimpleDoubleProperty volume;
	
	public SimpleDoubleProperty leavesVolume;
	
	public SimpleDoubleProperty lastPrice;
	
	public SimpleDoubleProperty avgPrice;
	
	public SimpleDoubleProperty protectionPrice;
	
	public SimpleDoubleProperty maxFloor;
	
	public SimpleDoubleProperty executedVolume;
	
	public Date duration;
	
	public Date orderDateTime;
	
	public OrderStatus orderStatus;
	
	public WorkingIndicator workingIndicator;
	
	public OrderValidityTypes validityType;
	
	public OrderTypes orderType;	
	
	public TradeTypes tradeType;
	
	public Side side;
	
	public SimpleStringProperty comment;
	
	public SimpleStringProperty clOrdID;
	
	public SimpleStringProperty origClOrdID;
	
	public SimpleStringProperty partyID;
	
	public char partyIdSource;
	
	public SimpleStringProperty partyRole;
	
	public SimpleStringProperty securityID;
	
	public List<OrderEventVO> eventsList;
	
	
	public OrdersVO() {
		this.eventsList = new ArrayList<OrderEventVO>();
	}
	
	
	public SimpleLongProperty orderIDProperty(){
		if(orderID == null) {
			orderID = new SimpleLongProperty();
		}
		return orderID;
	}
	
	public SimpleDoubleProperty priceProperty(){
		if(price == null) {
			price = new SimpleDoubleProperty();
		}
		return price;
	}
	
	public SimpleDoubleProperty stopPriceProperty(){
		if(stopPrice == null) {
			stopPrice = new SimpleDoubleProperty();
		}
		
		return stopPrice;
	}
	
	public SimpleDoubleProperty volumeProperty(){
		if(volume == null) {
			volume = new SimpleDoubleProperty();
		}
		return volume;
	}
	
	public SimpleDoubleProperty leavesVolumeProperty(){
		if(leavesVolume == null) {
			leavesVolume = new SimpleDoubleProperty();
		}
		return leavesVolume;
	}

	public SimpleDoubleProperty executedVolumeProperty(){
		if(executedVolume == null) {
			executedVolume = new SimpleDoubleProperty();
		}
		return executedVolume;
	}
	
	public SimpleDoubleProperty lastPriceProperty(){
		if(lastPrice == null) {
			lastPrice = new SimpleDoubleProperty();
		}
		return lastPrice;
	}
	
	public SimpleDoubleProperty avgPriceProperty(){
		if(avgPrice == null) {
			avgPrice = new SimpleDoubleProperty();
		}
		return avgPrice;
	}
	
	public SimpleDoubleProperty protectionPriceProperty(){
		if(protectionPrice == null) {
			protectionPrice = new SimpleDoubleProperty();
		}
		return protectionPrice;
	}
	
	public SimpleDoubleProperty maxFloorProperty(){
		if(maxFloor == null) {
			maxFloor = new SimpleDoubleProperty();
		}
		return maxFloor;
	}
	
	
	public SimpleStringProperty clOrdIDProperty(){
		if(clOrdID == null) {
			clOrdID = new SimpleStringProperty("");
		}
		return clOrdID;
	}

	public SimpleStringProperty origClOrdIDProperty(){
		if(origClOrdID == null) {
			origClOrdID = new SimpleStringProperty("");
		}
		return origClOrdID;
	}
	public SimpleStringProperty commentProperty(){
		if(comment == null) {
			comment = new SimpleStringProperty("");
		}
		return comment;
	}
	public SimpleStringProperty partyIDProperty(){
		if(partyID == null) {
			partyID = new SimpleStringProperty("");
		}
		return partyID;
	}
	public SimpleStringProperty partyRoleProperty(){
		if(partyRole == null) {
			partyRole = new SimpleStringProperty("");
		}
		return partyRole;
	}

	public SimpleStringProperty securityIDProperty(){
		if(securityID == null) {
			securityID = new SimpleStringProperty("");
		}
		return securityID;
	}
	
	
	
	/**
	 * @return the price
	 */
	public double getPrice() {
		return this.priceProperty().get();
	}
	
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.priceProperty().set(price);
	}

	public long getOrderID() {
		return orderIDProperty().get();
	}

	public void setOrderID(long orderID) {
		this.orderIDProperty().set(orderID);
	}

	public double getStopPrice() {
		return stopPriceProperty().get();
	}

	public void setStopPrice(double stopPrice) {
		this.stopPriceProperty().set(stopPrice);
	}

	public double getVolume() {
		return volumeProperty().get();
	}

	public void setVolume(double volume) {
		this.volumeProperty().set(volume);
	}

	public double getLeavesVolume() {
		return leavesVolumeProperty().get();
	}

	public void setLeavesVolume(double leavesVolume) {
		this.leavesVolumeProperty().set(leavesVolume);
	}

	public double getLastPrice() {
		return lastPriceProperty().get();
	}

	public void setLastPrice(double lastPrice) {
		this.lastPriceProperty().set(lastPrice);
	}

	public double getAvgPrice() {
		return avgPriceProperty().get();
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPriceProperty().set(avgPrice);
	}

	public double getProtectionPrice() {
		return protectionPriceProperty().get();
	}

	public void setProtectionPrice(double protectionPrice) {
		this.protectionPriceProperty().set(protectionPrice);
	}

	public double getMaxFloor() {
		return maxFloorProperty().get();
	}

	public void setMaxFloor(double maxFloor) {
		this.maxFloorProperty().set(maxFloor);
	}

	public double getExecutedVolume() {
		return executedVolumeProperty().get();
	}

	public void setExecutedVolume(double executedVolume) {
		this.executedVolumeProperty().set(executedVolume);
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
		this.duration = (duration);
	}

	public Date getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(Date orderDateTime) {
		this.orderDateTime = (orderDateTime);
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = (orderStatus);
	}

	public WorkingIndicator getWorkingIndicator() {
		return workingIndicator;
	}

	public void setWorkingIndicator(WorkingIndicator workingIndicator) {
		this.workingIndicator = (workingIndicator);
	}

	public OrderValidityTypes getValidityType() {
		return validityType;
	}

	public void setValidityType(OrderValidityTypes validityType) {
		this.validityType = (validityType);
	}

	public OrderTypes getOrderType() {
		return orderType;
	}

	public void setOrderType(OrderTypes orderType) {
		this.orderType = (orderType);
	}

	public TradeTypes getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeTypes tradeType) {
		this.tradeType = (tradeType);
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = (side);
	}

	public String getComment() {
		return commentProperty().get();
	}

	public void setComment(String comment) {
		this.commentProperty().set(comment);
	}

	public String getClOrdID() {
		return clOrdIDProperty().get();
	}

	public void setClOrdID(String clOrdID) {
		this.clOrdIDProperty().set(clOrdID);
	}

	public String getOrigClOrdID() {
		return origClOrdIDProperty().get();
	}

	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdIDProperty().set(origClOrdID);
	}

	public String getPartyID() {
		return partyIDProperty().get();
	}

	public void setPartyID(String partyID) {
		this.partyIDProperty().set(partyID);
	}

	public char getPartyIdSource() {
		return partyIdSource;
	}

	public void setPartyIdSource(char partyIdSource) {
		this.partyIdSource = (partyIdSource);
	}

	public String getPartyRole() {
		return partyRoleProperty().get();
	}

	public void setPartyRole(String partyRole) {
		this.partyRoleProperty().set(partyRole);
	}

	public String getSecurityID() {
		return securityIDProperty().get();
	}

	public void setSecurityID(String securityID) {
		this.securityIDProperty().set(securityID);
	}

	public List<OrderEventVO> getEventsList() {
		return eventsList;
	}

	public void setEventsList(List<OrderEventVO> eventsList) {
		this.eventsList = eventsList;
	}
	
}
