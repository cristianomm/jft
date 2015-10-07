/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.core.vo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cmm.jft.db.DBObject;

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
	
	public char orderStatus;
	
	public char workingIndicator;
	
	public char validityType;
	
	public char orderType;	
	
	public char tradeType;
	
	public char side;
	
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
		
		this.avgPrice = new SimpleDoubleProperty(0);
		this.executedVolume = new SimpleDoubleProperty(0);
		this.lastPrice = new SimpleDoubleProperty(0);
		this.leavesVolume = new SimpleDoubleProperty(0);
		this.maxFloor = new SimpleDoubleProperty(0);
		this.price = new SimpleDoubleProperty(0);
		this.protectionPrice = new SimpleDoubleProperty(0);
		this.stopPrice = new SimpleDoubleProperty(0);
		this.volume = new SimpleDoubleProperty(0);
				
		this.clOrdID = new SimpleStringProperty("");
		this.comment = new SimpleStringProperty("");
		
		
		
	}
	
	
	public SimpleLongProperty orderIDProperty(){
		return orderID;
	}
	
	public SimpleDoubleProperty priceProperty(){
		return price;
	}
	
	public SimpleDoubleProperty stopPriceProperty(){
		return stopPrice;
	}
	
	public SimpleDoubleProperty volumeProperty(){
		return volume;
	}
	
	public SimpleDoubleProperty leavesVolumeProperty(){
		return leavesVolume;
	}

	public SimpleDoubleProperty executedVolumeProperty(){
		return executedVolume;
	}
	
	public SimpleDoubleProperty lastPriceProperty(){
		return lastPrice;
	}
	
	public SimpleDoubleProperty avgPriceProperty(){
		return avgPrice;
	}
	
	public SimpleDoubleProperty protectionPriceProperty(){
		return protectionPrice;
	}
	
	public SimpleDoubleProperty maxFloorProperty(){
		return maxFloor;
	}
	
	
	
	
	
	
	/**
	 * @return the price
	 */
	public double getPrice() {
		return this.price.get();
	}
	
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price.set(price);
	}

	public long getOrderID() {
		return orderID.get();
	}

	public void setOrderID(long orderID) {
		this.orderID.set(orderID);
	}

	public double getStopPrice() {
		return stopPrice.get();
	}

	public void setStopPrice(double stopPrice) {
		this.stopPrice.set(stopPrice);
	}

	public double getVolume() {
		return volume.get();
	}

	public void setVolume(double volume) {
		this.volume.set(volume);
	}

	public double getLeavesVolume() {
		return leavesVolume.get();
	}

	public void setLeavesVolume(double leavesVolume) {
		this.leavesVolume.set(leavesVolume);
	}

	public double getLastPrice() {
		return lastPrice.get();
	}

	public void setLastPrice(double lastPrice) {
		this.lastPrice.set(lastPrice);
	}

	public double getAvgPrice() {
		return avgPrice.get();
	}

	public void setAvgPrice(double avgPrice) {
		this.avgPrice.set(avgPrice);
	}

	public double getProtectionPrice() {
		return protectionPrice.get();
	}

	public void setProtectionPrice(double protectionPrice) {
		this.protectionPrice.set(protectionPrice);
	}

	public double getMaxFloor() {
		return maxFloor.get();
	}

	public void setMaxFloor(double maxFloor) {
		this.maxFloor.set(maxFloor);
	}

	public double getExecutedVolume() {
		return executedVolume.get();
	}

	public void setExecutedVolume(double executedVolume) {
		this.executedVolume.set(executedVolume);
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

	public char getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(char orderStatus) {
		this.orderStatus = (orderStatus);
	}

	public char getWorkingIndicator() {
		return workingIndicator;
	}

	public void setWorkingIndicator(char workingIndicator) {
		this.workingIndicator = (workingIndicator);
	}

	public char getValidityType() {
		return validityType;
	}

	public void setValidityType(char validityType) {
		this.validityType = (validityType);
	}

	public char getOrderType() {
		return orderType;
	}

	public void setOrderType(char orderType) {
		this.orderType = (orderType);
	}

	public char getTradeType() {
		return tradeType;
	}

	public void setTradeType(char tradeType) {
		this.tradeType = (tradeType);
	}

	public char getSide() {
		return side;
	}

	public void setSide(char side) {
		this.side = (side);
	}

	public String getComment() {
		return comment.get();
	}

	public void setComment(String comment) {
		this.comment.set(comment);
	}

	public String getClOrdID() {
		return clOrdID.get();
	}

	public void setClOrdID(String clOrdID) {
		this.clOrdID.set(clOrdID);
	}

	public String getOrigClOrdID() {
		return origClOrdID.get();
	}

	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdID.set(origClOrdID);
	}

	public String getPartyID() {
		return partyID.get();
	}

	public void setPartyID(String partyID) {
		this.partyID.set(partyID);
	}

	public char getPartyIdSource() {
		return partyIdSource;
	}

	public void setPartyIdSource(char partyIdSource) {
		this.partyIdSource = (partyIdSource);
	}

	public String getPartyRole() {
		return partyRole.get();
	}

	public void setPartyRole(String partyRole) {
		this.partyRole.set(partyRole);
	}

	public String getSecurityID() {
		return securityID.get();
	}

	public void setSecurityID(String securityID) {
		this.securityID.set(securityID);
	}

	public List<OrderEventVO> getEventsList() {
		return eventsList;
	}

	public void setEventsList(List<OrderEventVO> eventsList) {
		this.eventsList = eventsList;
	}
	
}
