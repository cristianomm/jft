/**
 * 
 */
package com.cmm.jft.marketdata;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import com.cmm.jft.security.Security;

/**
 * <p>
 * <code>HistoricalQuote.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 19/09/2013 17:45:57
 *
 */
@Entity
@Table(name = "HistoricalQuote", schema="MarketData")
public class HistoricalQuote implements DBObject<HistoricalQuote> {

	@Id
	@SequenceGenerator(name = "HISTORICALQUOTE_SEQ", sequenceName = "HISTORICALQUOTE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "HISTORICALQUOTE_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "historicalQuoteID", nullable = false)
	private Long historicalQuoteID;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "QDateTime", nullable = false)
	private Date qDateTime;

	@Column(name = "Open", precision = 19, scale = 6, nullable = false)
	private BigDecimal open;

	@Column(name = "Close", precision = 19, scale = 6, nullable = false)
	private BigDecimal close;

	@Column(name = "AdjClose", precision = 19, scale = 6)
	private BigDecimal adjClose;

	@Column(name = "High", precision = 19, scale = 6, nullable = false)
	private BigDecimal high;

	@Column(name = "Low", precision = 19, scale = 6, nullable = false)
	private BigDecimal low;

	@Column(name = "Bid", precision = 19, scale = 6, nullable = false)
	private BigDecimal bid;

	@Column(name = "Ask", precision = 19, scale = 6, nullable = false)
	private BigDecimal ask;

	@Column(name = "AvgPrice", precision = 19, scale = 6, nullable = false)
	private BigDecimal avgPrice;

	@Column(name = "Volume", precision = 19, scale = 2, nullable = false)
	private BigDecimal volume;

	@Column(name = "TradedUnits")
	private Long tradedUnits;

	@Column(name = "TradedQuantity")
	private Long tradedQuantity;

	@Column(name = "QuoteFactor")
	private int quoteFactor;

	@JoinColumn(name = "securityID", referencedColumnName = "securityID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Security securityID;

	public HistoricalQuote() {

	}

	/**
	 * @return the qDateTime
	 */
	public Date getqDateTime() {
		return this.qDateTime;
	}

	/**
	 * @param qDateTime
	 *            the qDateTime to set
	 */
	public void setqDateTime(Date qDateTime) {
		this.qDateTime = qDateTime;
	}

	/**
	 * @return the open
	 */
	public BigDecimal getOpen() {
		return this.open;
	}

	/**
	 * @param open
	 *            the open to set
	 */
	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	/**
	 * @return the close
	 */
	public BigDecimal getClose() {
		return this.close;
	}

	/**
	 * @param close
	 *            the close to set
	 */
	public void setClose(BigDecimal close) {
		this.close = close;
	}

	/**
	 * @return the adjClose
	 */
	public BigDecimal getAdjClose() {
		return this.adjClose;
	}

	/**
	 * @param adjClose
	 *            the adjClose to set
	 */
	public void setAdjClose(BigDecimal adjClose) {
		this.adjClose = adjClose;
	}

	/**
	 * @return the high
	 */
	public BigDecimal getHigh() {
		return this.high;
	}

	/**
	 * @param high
	 *            the high to set
	 */
	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	/**
	 * @return the low
	 */
	public BigDecimal getLow() {
		return this.low;
	}

	/**
	 * @param low
	 *            the low to set
	 */
	public void setLow(BigDecimal low) {
		this.low = low;
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
	 * @return the avgPrice
	 */
	public BigDecimal getAvgPrice() {
		return this.avgPrice;
	}

	/**
	 * @param avgPrice
	 *            the avgPrice to set
	 */
	public void setAvgPrice(BigDecimal avgPrice) {
		this.avgPrice = avgPrice;
	}

	/**
	 * @return the volume
	 */
	public BigDecimal getVolume() {
		return this.volume;
	}

	/**
	 * @param volume
	 *            the volume to set
	 */
	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	/**
	 * @return the tradedUnits
	 */
	public Long getTradedUnits() {
		return this.tradedUnits;
	}

	/**
	 * @param tradedUnits
	 *            the tradedUnits to set
	 */
	public void setTradedUnits(Long tradedUnits) {
		this.tradedUnits = tradedUnits;
	}

	/**
	 * @return the tradedQuantity
	 */
	public Long getTradedQuantity() {
		return this.tradedQuantity;
	}

	/**
	 * @param tradedQuantity
	 *            the tradedQuantity to set
	 */
	public void setTradedQuantity(Long tradedQuantity) {
		this.tradedQuantity = tradedQuantity;
	}

	/**
	 * @return the securityID
	 */
	public Security getSecurityID() {
		return this.securityID;
	}

	/**
	 * @param securityID
	 *            the securityID to set
	 */
	public void setSecurityID(Security securityID) {
		this.securityID = securityID;
	}

	/**
	 * @return the historicalQuoteID
	 */
	public Long getHistoricalQuoteID() {
		return this.historicalQuoteID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "HistoricalQuote [historicalQuoteID=" + this.historicalQuoteID
				+ ", qDateTime=" + this.qDateTime + ", open=" + this.open
				+ ", close=" + this.close + ", high=" + this.high + ", low="
				+ this.low + ", bid=" + this.bid + ", ask=" + this.ask
				+ ", volume=" + this.volume + ", tradedUnits="
				+ this.tradedUnits + ", tradedQuantity=" + this.tradedQuantity
				+ ", securityID=" + this.securityID + "]";
	}

}
