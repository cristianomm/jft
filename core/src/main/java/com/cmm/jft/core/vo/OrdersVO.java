/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.core.vo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder.Case;

import org.apache.log4j.Level;

import com.cmm.jft.db.DBObject;

import javafx.beans.property.SimpleDoubleProperty;


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
	
	public long orderID;
	
	public SimpleDoubleProperty price;
	
	public double stopPrice;
	
	public double volume;
	
	public double leavesVolume;
	
	public double lastPrice;
	
	public BigDecimal avgPrice;
	
	public double protectionPrice;
	
	public double maxFloor;
	
	public double executedVolume;
	
	public Date duration;
	
	public Date orderDateTime;
	
	public char orderStatus;
	
	public char workingIndicator;
	
	public char validityType;
	
	public char orderType;	
	
	public char tradeType;
	
	public char side;
	
	public String comment;
	
	public String clOrdID;
	
	public String origClOrdID;
	
	public String partyID;
	
	public char partyIdSource;
	
	public String partyRole;
	
	public String securityID;
	
	public List<OrderEventVO> eventsList;
	
	
	public OrdersVO() {
		this.eventsList = new ArrayList<OrderEventVO>();
	}
	
	public SimpleDoubleProperty priceProperty(){
		return price;
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

}
