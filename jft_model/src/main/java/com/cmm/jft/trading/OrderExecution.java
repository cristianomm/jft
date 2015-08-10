/**
 * 
 */
package com.cmm.jft.trading;

import java.math.BigDecimal;
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
import com.cmm.jft.trading.enums.ExecutionTypes;

/**
 * <p>
 * <code>OrderExecution.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 13/12/2013 13:48:18
 *
 */

@Entity
@Table(name = "OrderExecution")
public class OrderExecution implements DBObject<OrderExecution> {

	@Id
	@SequenceGenerator(name = "ORDEREXECUTION_SEQ", sequenceName = "ORDEREXECUTION_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ORDEREXECUTION_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "orderExecutionID", nullable = false)
	private Long orderExecutionID;

	@Column(name = "Volume")
	private int volume;
	
	@Column(name="LeavesVolume")
	private int leavesVolume;
	
	@Column(name = "Price", precision = 19, scale = 6)
	private double price;
	
	@Column(name = "LastPrice", precision = 19, scale = 6)
	private double lastPrice;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ExecutionType", length=25)
	private ExecutionTypes executionType;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ExecutionDateTime")
	private Date executionDateTime;
	
	@Column(name = "Message", length = 255)
	private String message;

	@JoinColumn(name = "orderID", referencedColumnName = "orderID", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Orders orderID;

	/**
     * 
     */
	public OrderExecution() {
		super();
	}

	/**
	 * @param executionDateTime
	 * @param volume
	 * @param price
	 * @param orderID
	 */
	public OrderExecution(ExecutionTypes execType, Date executionDateTime, int volume, double price, Orders orderID) {
		super();
		this.executionType = execType;
		this.executionDateTime = executionDateTime;
		this.volume = volume;
		this.price = price;
		this.orderID = orderID;
	}

	/**
	 * @return the executionDateTime
	 */
	public Date getExecutionDateTime() {
		return this.executionDateTime;
	}

	/**
	 * @param executionDateTime
	 *            the executionDateTime to set
	 */
	public void setExecutionDateTime(Date executionDateTime) {
		this.executionDateTime = executionDateTime;
	}

	/**
	 * @return the volume
	 */
	public long getVolume() {
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
	 * @param orderID
	 *            the orderID to set
	 */
	public void setOrderID(Orders orderID) {
		this.orderID = orderID;
	}

	/**
	 * @return the orderExecutionID
	 */
	public Long getOrderExecutionID() {
		return this.orderExecutionID;
	}
	
	public ExecutionTypes getExecutionType() {
		return executionType;
	}
	
	public double getLastPrice() {
		return lastPrice;
	}
	
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public int getLeavesVolume() {
		return leavesVolume;
	}
	
	public void setLeavesVolume(int leavesVolume) {
		this.leavesVolume = leavesVolume;
	}

}
