/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.marketdata;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.financial.Broker;
import com.cmm.jft.security.Security;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.Side;

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
 * @NamedQuery(name = "MarketTrade.findByMarketTradeID", query =
 * "SELECT m FROM MarketTrade m WHERE m.marketTradeID = :marketTradeID"),
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
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "MARKETORDER_SEQ", sequenceName = "MARKETORDER_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "MARKETORDER_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "marketOrderID", nullable = false)
	private Long marketOrderID;

	@Column(name = "ExternalID", length = 30)
	private String externalID;

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

	@JoinColumn(name = "securityID", referencedColumnName = "securityID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Security securityID;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "marketEventID", fetch = FetchType.LAZY)
	private List<MarketEvent> eventsList;
	
	@JoinColumn(name = "brokerID", referencedColumnName = "brokerID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Broker brokerID;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="sellOrderID", cascade=CascadeType.ALL)
	private List<MarketTrade> sellTradeList;
	
	@OneToMany(fetch=FetchType.LAZY, mappedBy="buyOrderID", cascade=CascadeType.ALL)
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
	 * @param securityID
	 */
	public MarketOrder(Side side, BigDecimal price, Integer volume,
			Date expirationDate, OrderStatus orderStatus, Security securityID) {
		super();
		this.side = side;
		this.price = price;
		this.volume = volume;
		this.expirationDate = expirationDate;
		this.orderStatus = orderStatus;
		this.securityID = securityID;
	}

	/**
	 * @return the externalID
	 */
	public String getExternalID() {
		return this.externalID;
	}

	/**
	 * @param externalID
	 *            the externalID to set
	 */
	public void setExternalID(String externalID) {
		this.externalID = externalID;
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
	 * @return the marketOrderID
	 */
	public Long getMarketOrderID() {
		return this.marketOrderID;
	}

	/**
	 * @return the eventsList
	 */
	public List<MarketEvent> getEventsList() {
		return this.eventsList;
	}
	
	/**
	 * @return the brokerID
	 */
	public Broker getBrokerID() {
		return this.brokerID;
	}
	
	/**
	 * @param brokerID the brokerID to set
	 */
	public void setBrokerID(Broker brokerID) {
		this.brokerID = brokerID;
	}

}
