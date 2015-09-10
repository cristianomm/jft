/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.trading.marketdata;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.cmm.jft.security.Security;

/**
 *
 * <p>
 * <code>Tick</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Tick")
// @DiscriminatorValue(value="2")
@NamedQueries({
		@NamedQuery(name = "Tick.findAll", query = "SELECT m FROM Tick m"),
		@NamedQuery(name = "Tick.findByTickID", query = "SELECT m FROM Tick m WHERE m.marketDataEventID = :marketDataEventID"),
		@NamedQuery(name = "Tick.findByLastDateTime", query = "SELECT m FROM Tick m WHERE m.lastDateTime = :lastDateTime"),
		@NamedQuery(name = "Tick.findByBid", query = "SELECT m FROM Tick m WHERE m.bid = :bid"),
		@NamedQuery(name = "Tick.findByAsk", query = "SELECT m FROM Tick m WHERE m.ask = :ask"),
		@NamedQuery(name = "Tick.findByBidVolume", query = "SELECT m FROM Tick m WHERE m.bidVolume = :bidVolume"),
		@NamedQuery(name = "Tick.findByAskVolume", query = "SELECT m FROM Tick m WHERE m.askVolume = :askVolume") })
public class Tick extends MarketEvent {

	// @Id
	// @TableGenerator(name = "TICK_SEQ", table = "SEQUENCE", allocationSize =
	// 1, initialValue = 1)
	// @GeneratedValue(generator = "TICK_SEQ", strategy = GenerationType.TABLE)
	// @Basic(optional = false)
	// @Column(name = "tickID", nullable = false)
	// private Long tickID;

	@Column(name = "LastPrice", precision = 19, scale = 6, nullable = false)
	private BigDecimal lastPrice;

	@Column(name = "LastDateTime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastDateTime;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "Bid", precision = 19, scale = 6, nullable = false)
	private BigDecimal bid;

	@Column(name = "Ask", precision = 19, scale = 6, nullable = false)
	private BigDecimal ask;

	@Column(name = "BidVolume", nullable = false)
	private Long bidVolume;

	@Column(name = "AskVolume", nullable = false)
	private Long askVolume;

	public Tick() {
	}

	/**
	 * @param eventDateTime
	 * @param volume
	 * @param lastPrice
	 * @param lastDateTime
	 * @param bid
	 * @param ask
	 * @param bidVolume
	 * @param askVolume
	 */
	public Tick(Security security, Date eventDateTime, long volume,
			BigDecimal lastPrice, Date lastDateTime, BigDecimal bid,
			BigDecimal ask, Long bidVolume, Long askVolume) {
		super();
		// this.securityID = security;
		// this.eventDateTime = eventDateTime;
		// this.volume = volume;
		this.lastPrice = lastPrice;
		this.lastDateTime = lastDateTime;
		this.bid = bid;
		this.ask = ask;
		this.bidVolume = bidVolume;
		this.askVolume = askVolume;
	}

	// /**
	// * @return the tickID
	// */
	// public Long getTickID() {
	// return tickID;
	// }

	/**
	 * @return the lastPrice
	 */
	public BigDecimal getLastPrice() {
		return this.lastPrice;
	}

	/**
	 * @param lastPrice
	 *            the lastPrice to set
	 */
	public void setLastPrice(BigDecimal lastPrice) {
		this.lastPrice = lastPrice;
	}

	/**
	 * @return the lastDateTime
	 */
	public Date getLastDateTime() {
		return this.lastDateTime;
	}

	/**
	 * @param lastDateTime
	 *            the lastDateTime to set
	 */
	public void setLastDateTime(Date lastDateTime) {
		this.lastDateTime = lastDateTime;
	}

	/**
	 * @return the bid
	 */
	public BigDecimal getBid() {
		return this.bid;
	}

	/**
	 * @param bid
	 *            the bid to set
	 */
	public void setBid(BigDecimal bid) {
		this.bid = bid;
	}

	/**
	 * @return the ask
	 */
	public BigDecimal getAsk() {
		return this.ask;
	}

	/**
	 * @param ask
	 *            the ask to set
	 */
	public void setAsk(BigDecimal ask) {
		this.ask = ask;
	}

	/**
	 * @return the bidVolume
	 */
	public Long getBidVolume() {
		return this.bidVolume;
	}

	/**
	 * @param bidVolume
	 *            the bidVolume to set
	 */
	public void setBidVolume(Long bidVolume) {
		this.bidVolume = bidVolume;
	}

	/**
	 * @return the askVolume
	 */
	public Long getAskVolume() {
		return this.askVolume;
	}

	/**
	 * @param askVolume
	 *            the askVolume to set
	 */
	public void setAskVolume(Long askVolume) {
		this.askVolume = askVolume;
	}

}
