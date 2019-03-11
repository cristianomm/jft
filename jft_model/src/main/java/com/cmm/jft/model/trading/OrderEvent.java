/**
 * 
 */
package com.cmm.jft.model.trading;

import java.time.LocalDateTime;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.trading.enums.ExecutionTypes;
import com.cmm.jft.model.trading.enums.OrderStatus;
import com.cmm.jft.model.trading.enums.OrderTypes;
import com.cmm.jft.model.trading.enums.OrderValidityTypes;

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
    @Column(name = "orderEventId", nullable = false)
    private Long orderEventId;
    
    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    @JoinColumn(name="orderId", referencedColumnName="orderId")
    private Orders orderId;
    
    @Column(name="tradeId", length=32)
    private String tradeId;

    @Column(name = "Price", precision = 19, scale = 6)
    private double price;
    
    @Column(name = "StopPrice", precision = 19, scale = 6)
    private double stopPrice;
    
    @Column(name = "Volume")
    private double volume;
    
    @Column(name = "MinQty")
    private double minQty;
    
    @Column(name = "MaxFloor")
    private double maxFloor;

    @Column(name = "CumQty")
    private double cumQty;
    
    @Column(name = "LeavesQty")
    private double leavesQty;
    
    @Column(name = "LastQty")
    private double lastQty;

    @Enumerated(EnumType.ORDINAL)
    @Column(name="ExecutionType")
    private ExecutionTypes executionType;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name="OrderStatus")
    private OrderStatus orderStatus;

    @Enumerated(EnumType.ORDINAL)
    @Basic(optional = false)
    @Column(name = "OrderType")
    private OrderTypes orderType;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name="Validity")
    private OrderValidityTypes validity;

    @Column(name = "EventDateTime")
    private LocalDateTime eventDateTime;
    
    @Column(name="ContraBroker", length=5)
    private String contraBroker;
    
    @Column(name = "Message", length = 255)
    private String message;

    @Column(name="OrdRejReason")
    private int ordRejReason;

    /**
     * 
     */
    public OrderEvent() {
	super();
    }

    public OrderEvent(ExecutionTypes execType, double volume, double price) {
	super();
	this.executionType = execType;
	this.eventDateTime = LocalDateTime.now();
	this.volume = volume;
	this.price = price;
    }



    /**
     * @param executionDateTime
     * @param volume
     * @param price
     * @param orderId
     */
    public OrderEvent(ExecutionTypes execType, LocalDateTime eventDateTime, double volume, double price) {
	super();
	this.executionType = execType;
	this.eventDateTime = eventDateTime;
	this.volume = volume;
	this.price = price;
    }
    
    /**
     * @param orderEventId the orderEventId to set
     */
    public void setOrderEventId(Long orderEventId) {
	this.orderEventId = orderEventId;
    }
    
    /**
     * @return the executionDateTime
     */
    public LocalDateTime getExecutionDateTime() {
	return this.eventDateTime;
    }

    /**
     * @param executionDateTime
     *            the executionDateTime to set
     */
    public void setEventDateTime(LocalDateTime eventDateTime) {
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
    public void setVolume(double volume) {
	this.volume = volume;
    }
    
    /**
     * @return the minQty
     */
    public double getMinQty() {
	return minQty;
    }
    
    /**
     * @param minQty the minQty to set
     */
    public void setMinQty(double minQty) {
	this.minQty = minQty;
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
     * @return the stopPrice
     */
    public double getStopPrice() {
	return stopPrice;
    }
    
    /**
     * @param stopPrice the stopPrice to set
     */
    public void setStopPrice(double stopPrice) {
	this.stopPrice = stopPrice;
    }

    /**
     * @return the orderId
     */
    public Orders getOrderId() {
	return this.orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(Orders orderId) {
	this.orderId = orderId;
    }

    /**
     * @return the orderEventId
     */
    public Long getOrderEventId() {
	return this.orderEventId;
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
     * @param validity the validity to set
     */
    public void setValidity(OrderValidityTypes validity) {
	this.validity = validity;
    }
    
    /**
     * @return the validity
     */
    public OrderValidityTypes getValidity() {
	return validity;
    }

    /**
     * @return the tradeId
     */
    public String getTradeId() {
	return this.tradeId;
    }

    /**
     * @param tradeId the tradeId to set
     */
    public void setTradeId(String tradeId) {
	this.tradeId = tradeId;
    }
    

    /**
     * @return the eventDateTime
     */
    public LocalDateTime getEventDateTime() {
	return eventDateTime;
    }
    
    /**
     * @return the cumQty
     */
    public double getCumQty() {
	return cumQty;
    }
    
    /**
     * @return the lastQty
     */
    public double getLastQty() {
	return lastQty;
    }
    
    /**
     * @return the leavesQty
     */
    public double getLeavesQty() {
	return leavesQty;
    }
    
    /**
     * @param cumQty the cumQty to set
     */
    public void setCumQty(double cumQty) {
	this.cumQty = cumQty;
    }
    
    /**
     * @param lastQty the lastQty to set
     */
    public void setLastQty(double lastQty) {
	this.lastQty = lastQty;
    }
    
    /**
     * @param leavesQty the leavesQty to set
     */
    public void setLeavesQty(double leavesQty) {
	this.leavesQty = leavesQty;
    }
    
    /**
     * @return the orderStatus
     */
    public OrderStatus getOrderStatus() {
	return orderStatus;
    }
    
    /**
     * @param orderStatus the orderStatus to set
     */
    public void setOrderStatus(OrderStatus orderStatus) {
	this.orderStatus = orderStatus;
    }
    
    /**
     * @return the maxFloor
     */
    public double getMaxFloor() {
        return maxFloor;
    }

    /**
     * @param maxFloor the maxFloor to set
     */
    public void setMaxFloor(double maxFloor) {
        this.maxFloor = maxFloor;
    }

}
