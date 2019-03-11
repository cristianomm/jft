/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.marketdata;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.security.Security;

/**
 *
 * <p>
 * <code>MarketTrade</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "MarketTrade", schema="MarketData", 
uniqueConstraints = { @UniqueConstraint(columnNames = {"TradeDateTime", "Price", "Volume" }) })
@NamedQueries({
		@NamedQuery(name = "MarketTrade.findAll", query = "SELECT m FROM MarketTrade m"),
		@NamedQuery(name = "MarketTrade.findByMarketTradeId", query = "SELECT m FROM MarketTrade m WHERE m.marketTradeId = :marketTradeId"),
		@NamedQuery(name = "MarketTrade.findByTDateTime", query = "SELECT m FROM MarketTrade m WHERE m.tradeDateTime = :tradeDateTime"),
		@NamedQuery(name = "MarketTrade.findByPrice", query = "SELECT m FROM MarketTrade m WHERE m.price = :price"),
		@NamedQuery(name = "MarketTrade.findByVolume", query = "SELECT m FROM MarketTrade m WHERE m.volume = :volume") })
public class MarketTrade implements DBObject<MarketTrade> {
	private static final long serialVersionUId = 1L;
	@Id
	@SequenceGenerator(name = "MARKETTRADE_SEQ", sequenceName = "MARKETTRADE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "MARKETTRADE_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "marketTradeId", nullable = false)
	private Long marketTradeId;

	@Column(name = "TradeDateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date tradeDateTime;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "Price", precision = 19, scale = 6)
	private BigDecimal price;

	@Column(name = "Volume")
	private double volume;
	
	@JoinColumn(name="buyOrderId", referencedColumnName="marketOrderId", nullable=false)
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	private MarketOrder buyOrderId;
	
	@JoinColumn(name="sellOrderId", referencedColumnName="marketOrderId", nullable=false)
	@ManyToOne(fetch=FetchType.LAZY, optional=false)
	private MarketOrder sellOrderId;
	

	public MarketTrade() {
	}

	/**
	 * @return the tDateTime
	 */
	public Date getTradeDateTime() {
		return this.tradeDateTime;
	}

	/**
	 * @param tDateTime
	 *            the tDateTime to set
	 */
	public void setTradeDateTime(Date tDateTime) {
		this.tradeDateTime = tDateTime;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return this.price;
	}

	/**
	 * @param price
	 *            the price to set
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
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
	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	/**
	 * @return the marketTradeId
	 */
	public Long getMarketTradeId() {
		return this.marketTradeId;
	}

}
