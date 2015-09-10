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
	
	public Long orderID;
	
	public double price;
	
	public double stopPrice;
	
	public Double volume;
	
	public double leavesVolume;
	
	public double lastPrice;
	
	public BigDecimal avgPrice;
	
	public double protectionPrice;
	
	public double maxFloor;
	
	public Integer executedVolume;
	
	public Date duration;
	
	public Date orderDateTime;
	
	public char orderStatus;
	
	public char workingIndicator;
	
	public char validityType;
	
	public char orderType;	
	
	public char tradeType;
	
	public char side;
	
	public String comment;
	
	public String orderSerial;
	
	public String clOrdID;
	
	public String origClOrdID;
	
	public String partyID;
	
	public char partyIdSource;
	
	public String partyRole;
	
	//public Security securityID;
	
	public List<OrderEventVO> eventsList;

}
