/**
 * 
 */
package com.cmm.jft.model.marketdata;

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
import com.cmm.jft.model.trading.enums.MarketEvents;

/**
 * <p>
 * <code>MarketData.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 11/08/2013 23:37:44
 *
 */
@Entity
@Table(name = "MarketEvent", schema="MarketData")
public class MarketEvent implements DBObject<MarketEvent> {

	@Id
	@SequenceGenerator(name = "MARKETEVENT_SEQ", sequenceName = "MARKETEVENT_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "MARKETEVENT_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "marketEventId", nullable = false)
	private Long marketEventId;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "EventType")
	private MarketEvents eventType;

	@Column(name = "Message", length = 255)
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EventDateTime")
	private Date eventDateTime;

	@JoinColumn(name = "marketOrderId", referencedColumnName = "marketOrderId")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private MarketOrder marketOrderId;

	/**
     * 
     */
	public MarketEvent() {
		super();
	}

	/**
	 * @param eventType
	 * @param message
	 * @param eventDateTime
	 * @param marketOrderId
	 */
	public MarketEvent(MarketEvents eventType, String message,
			Date eventDateTime, MarketOrder marketOrderId) {
		super();
		this.eventType = eventType;
		this.message = message;
		this.eventDateTime = eventDateTime;
		this.marketOrderId = marketOrderId;
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
	 * @return the marketOrderId
	 */
	public MarketOrder getMarketOrderId() {
		return this.marketOrderId;
	}

	/**
	 * @param marketOrderId
	 *            the marketOrderId to set
	 */
	public void setMarketOrderId(MarketOrder marketOrderId) {
		this.marketOrderId = marketOrderId;
	}

	/**
	 * @return the marketEventId
	 */
	public Long getMarketEventId() {
		return this.marketEventId;
	}

}
