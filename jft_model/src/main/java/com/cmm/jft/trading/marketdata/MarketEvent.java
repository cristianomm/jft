/**
 * 
 */
package com.cmm.jft.trading.marketdata;

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
import com.cmm.jft.trading.enums.MarketEvents;

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
	@Column(name = "marketEventID", nullable = false)
	private Long marketEventID;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "EventType")
	private MarketEvents eventType;

	@Column(name = "Message", length = 255)
	private String message;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "EventDateTime")
	private Date eventDateTime;

	@JoinColumn(name = "marketOrderID", referencedColumnName = "marketOrderID")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private MarketOrder marketOrderID;

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
	 * @param marketOrderID
	 */
	public MarketEvent(MarketEvents eventType, String message,
			Date eventDateTime, MarketOrder marketOrderID) {
		super();
		this.eventType = eventType;
		this.message = message;
		this.eventDateTime = eventDateTime;
		this.marketOrderID = marketOrderID;
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
	 * @return the marketOrderID
	 */
	public MarketOrder getMarketOrderID() {
		return this.marketOrderID;
	}

	/**
	 * @param marketOrderID
	 *            the marketOrderID to set
	 */
	public void setMarketOrderID(MarketOrder marketOrderID) {
		this.marketOrderID = marketOrderID;
	}

	/**
	 * @return the marketEventID
	 */
	public Long getMarketEventID() {
		return this.marketEventID;
	}

}
