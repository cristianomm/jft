/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.marketdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.financial.Broker;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.trading.enums.OrderStatus;
import com.cmm.jft.model.trading.enums.Side;

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
@Table(name = "MarketOrder", schema="MarketData")
/*
 * @NamedQueries({
 * 
 * @NamedQuery(name = "MarketTrade.findAll", query =
 * "SELECT m FROM MarketTrade m"),
 * 
 * @NamedQuery(name = "MarketTrade.findByMarketTradeId", query =
 * "SELECT m FROM MarketTrade m WHERE m.marketTradeId = :marketTradeId"),
 * 
 * @NamedQuery(name = "MarketTrade.findByTDateTime", query =
 * "SELECT m FROM MarketTrade m WHERE m.tradeDateTime = :tradeDateTime"),
 * 
 * @NamedQuery(name = "MarketTrade.findByPrice", query =
 * "SELECT m FROM MarketTrade m WHERE m.price = :price"),
 * 
 * @NamedQuery(name = "MarketTrade.findByVolume", query =
 * "SELECT m FROM MarketTrade m WHERE m.volume = :volume")})
 */
public class MarketOrder implements DBObject<MarketOrder> {
	private static final long serialVersionUId = 1L;
	@Id
	@SequenceGenerator(name = "MARKETORDER_SEQ", sequenceName = "MARKETORDER_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "MARKETORDER_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "marketOrderId", nullable = false)
	private Long marketOrderId;

	@Column(name = "ExternalId", length = 30)
	private String externalId;

	@Enumerated(EnumType.STRING)
	@Column(name = "Side", length = 10)
	private Side side;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "Price", precision = 19, scale = 6)
	private BigDecimal price;

	@Column(name = "Volume")
	private double volume;

	@Column(name = "ExpirationDate")
	@Temporal(TemporalType.TIMESTAMP)
	private Date expirationDate;

	@Column(name = "OrderDateTime")
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDateTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "OrderStatus", length = 20)
	private OrderStatus orderStatus;

	@JoinColumn(name = "securityId", referencedColumnName = "securityId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Security securityId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "marketEventId", fetch = FetchType.LAZY)
	private List<MarketEvent> eventsList;
	
	@JoinColumn(name = "brokerId", referencedColumnName = "brokerId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Broker brokerId;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="sellOrderId", cascade=CascadeType.ALL)
	private List<MarketTrade> sellTradeList;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="buyOrderId", cascade=CascadeType.ALL)
	private List<MarketTrade> buyTradeList;
	
	
	/**
     * 
     */
	public MarketOrder() {
		super();
		this.eventsList = new ArrayList<MarketEvent>();
	}

	/**
	 * @param side
	 * @param price
	 * @param volume
	 * @param expirationDate
	 * @param orderStatus
	 * @param securityId
	 */
	public MarketOrder(Side side, BigDecimal price, Integer volume,
			Date expirationDate, OrderStatus orderStatus, Security securityId) {
		super();
		this.side = side;
		this.price = price;
		this.volume = volume;
		this.expirationDate = expirationDate;
		this.orderStatus = orderStatus;
		this.securityId = securityId;
	}

	/**
	 * @return the externalId
	 */
	public String getExternalId() {
		return this.externalId;
	}

	/**
	 * @param externalId
	 *            the externalId to set
	 */
	public void setExternalId(String externalId) {
		this.externalId = externalId;
	}

	/**
	 * @return the side
	 */
	public Side getSide() {
		return this.side;
	}

	/**
	 * @param side
	 *            the side to set
	 */
	public void setSide(Side side) {
		this.side = side;
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
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return this.expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the orderDateTime
	 */
	public Date getOrderDateTime() {
		return this.orderDateTime;
	}

	/**
	 * @param orderDateTime
	 *            the orderDateTime to set
	 */
	public void setOrderDateTime(Date orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	/**
	 * @return the orderStatus
	 */
	public OrderStatus getOrderStatus() {
		return this.orderStatus;
	}

	/**
	 * @param orderStatus
	 *            the orderStatus to set
	 */
	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	/**
	 * @return the securityId
	 */
	public Security getSecurityId() {
		return this.securityId;
	}

	/**
	 * @param securityId
	 *            the securityId to set
	 */
	public void setSecurityId(Security securityId) {
		this.securityId = securityId;
	}

	/**
	 * @return the marketOrderId
	 */
	public Long getMarketOrderId() {
		return this.marketOrderId;
	}

	/**
	 * @return the eventsList
	 */
	public List<MarketEvent> getEventsList() {
		return this.eventsList;
	}
	
	/**
	 * @return the brokerId
	 */
	public Broker getBrokerId() {
		return this.brokerId;
	}
	
	/**
	 * @param brokerId the brokerId to set
	 */
	public void setBrokerId(Broker brokerId) {
		this.brokerId = brokerId;
	}

}
