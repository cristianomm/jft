/**
 * 
 */
package com.cmm.jft.trading;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.OrderTypes;

/**
 * <p>
 * <code>OrderEvent.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 13/12/2013 13:48:18
 *
 */

@Entity
@Table(name = "OrderEvent", schema="Trading")
public class OrderEvent implements DBObject<OrderEvent> {

    @Id
    @SequenceGenerator(name = "ORDEREVENT_SEQ", sequenceName = "ORDEREVENT_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(generator = "ORDEREVENT_SEQ", strategy = GenerationType.AUTO)
    @Basic(optional = false)
    @Column(name = "orderEventID", nullable = false)
    private Long orderEventID;

    @Column(name = "Volume")
    private double volume;

    @Column(name = "Price", precision = 19, scale = 6)
    private double price;

    @Enumerated(EnumType.STRING)
    @Column(name="ExecutionType", length=30)
    private ExecutionTypes executionType;

    @Enumerated(EnumType.STRING)
    @Basic(optional = false)
    @Column(name = "OrderType", length=30)
    private OrderTypes orderType;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "EventDateTime")
    private Date eventDateTime;

    @Column(name = "Message", length = 255)
    private String message;

    @Column(name="OrdRejReason")
    private int ordRejReason;

    @Column(name="tradeID", length=32)
    private String tradeID;


    @Column(name="ContraBroker", length=5)
    private String contraBroker;

    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Orders orderID;


    /**
     * 
     */
    public OrderEvent() {
	super();
    }

    public OrderEvent(ExecutionTypes execType, double volume, double price) {
	super();
	this.executionType = execType;
	this.eventDateTime = new Date();
	this.volume = volume;
	this.price = price;
    }



    /**
     * @param executionDateTime
     * @param volume
     * @param price
     * @param orderID
     */
    public OrderEvent(ExecutionTypes execType, Date eventDateTime, double volume, double price) {
	super();
	this.executionType = execType;
	this.eventDateTime = eventDateTime;
	this.volume = volume;
	this.price = price;
    }

    /**
     * @return the executionDateTime
     */
    public Date getExecutionDateTime() {
	return this.eventDateTime;
    }

    /**
     * @param executionDateTime
     *            the executionDateTime to set
     */
    public void setEventDateTime(Date eventDateTime) {
	this.eventDateTime = eventDateTime;
    }

    /**
     * @return the volume
     */
    public double getVolume() {
	return this.volume;
    }

    /**
     * @param volume
     *            the volume to set
     */
    public void setVolume(int volume) {
	this.volume = volume;
    }

    /**
     * @return the price
     */
    public double getPrice() {
	return this.price;
    }

    /**
     * @param price
     *            the price to set
     */
    public void setPrice(double price) {
	this.price = price;
    }

    /**
     * @return the orderID
     */
    public Orders getOrderID() {
	return this.orderID;
    }

    /**
     * @param orderID the orderID to set
     */
    public void setOrderID(Orders orderID) {
	this.orderID = orderID;
    }

    /**
     * @return the orderEventID
     */
    public Long getOrderEventID() {
	return this.orderEventID;
    }

    public ExecutionTypes getExecutionType() {
	return executionType;
    }

    public String getMessage() {
	return message;
    }

    public void setMessage(String message) {
	this.message = message;
    }

    public OrderTypes getOrderType() {
	return orderType;
    }

    public void setOrderType(OrderTypes orderType) {
	this.orderType = orderType;
    }	

    public String getContraBroker() {
	return contraBroker;
    }

    public int getOrdRejReason() {
	return ordRejReason;
    }

    public void setOrdRejReason(int ordRejReason) {
	this.ordRejReason = ordRejReason;
    }

    public void setContraBroker(String contraBroker) {
	this.contraBroker = contraBroker;
    }

    public void setExecutionType(ExecutionTypes executionType) {
	this.executionType = executionType;
    }

    /**
     * @return the tradeID
     */
    public String getTradeID() {
	return this.tradeID;
    }

    /**
     * @param tradeID the tradeID to set
     */
    public void setTradeID(String tradeID) {
	this.tradeID = tradeID;
    }

}
