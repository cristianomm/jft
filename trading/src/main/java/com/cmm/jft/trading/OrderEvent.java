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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cmm.jft.trading.enums.MarketEvents;
import com.cmm.jft.db.DBObject;

/**
 * <p>
 * <code>OrderEvent.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 13/12/2013 14:15:19
 *
 */
@Entity
@Table(name = "OrderEvent")
public class OrderEvent implements DBObject<OrderEvent> {

	@Id
	@SequenceGenerator(name = "ORDEREVENT_SEQ", sequenceName = "ORDEREVENT_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ORDEREVENT_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "orderEventID", nullable = false)
	private Long orderEventID;

	@Enumerated(EnumType.STRING)
	@Column(name = "EventType", length = 50)
	private MarketEvents eventType;

	@Column(name = "Message", length = 255)
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EventDateTime")
	private Date eventDateTime;

	@JoinColumn(name = "orderID", referencedColumnName = "orderID", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Orders orderID;

	/**
     * 
     */
	public OrderEvent() {
		super();
	}

	/**
	 * @param eventType
	 * @param message
	 * @param eventDateTime
	 * @param orderID
	 */
	public OrderEvent(MarketEvents eventType, String message,
			Date eventDateTime, Orders orderID) {
		super();
		this.eventType = eventType;
		this.message = message;
		this.eventDateTime = eventDateTime;
		this.orderID = orderID;
	}

	/**
	 * @return the eventType
	 */
	public MarketEvents getEventType() {
		return this.eventType;
	}

	/**
	 * @param eventType
	 *            the eventType to set
	 */
	public void setEventType(MarketEvents eventType) {
		this.eventType = eventType;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return the eventDateTime
	 */
	public Date getEventDateTime() {
		return this.eventDateTime;
	}

	/**
	 * @param eventDateTime
	 *            the eventDateTime to set
	 */
	public void setEventDateTime(Date eventDateTime) {
		this.eventDateTime = eventDateTime;
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
	 * @return the orderEventID
	 */
	public Long getOrderEventID() {
		return this.orderEventID;
	}

}
