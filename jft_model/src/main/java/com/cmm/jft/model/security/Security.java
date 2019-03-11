/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.marketdata.HistoricalQuote;
import com.cmm.jft.model.marketdata.MarketOrder;
import com.cmm.jft.model.trading.Orders;

/**
 *
 * <p>
 * <code>Security</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Security", schema="Security")
//, uniqueConstraints=@UniqueConstraint(columnNames= {"Symbol"}) )
@NamedQueries({
	@NamedQuery(name = "Security.findAll", query = "SELECT e FROM Security e"),
	@NamedQuery(name = "Security.findBySecurityId", query = "SELECT e FROM Security e WHERE e.securityId = :securityId"),
	@NamedQuery(name = "Security.findBySymbol", query = "SELECT e FROM Security e WHERE e.symbol = :symbol"),
	// @NamedQuery(name = "Security.findByIsin", query =
	// "SELECT e FROM Security e WHERE e.isin = :isin"),
	// @NamedQuery(name = "Security.findBySecurityName", query =
	// "SELECT e FROM Security e WHERE e.securityName = :securityName")
})
public class Security implements DBObject<Security> {

	@Id
	@SequenceGenerator(name = "SECURITY_SEQ", sequenceName = "SECURITY_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "SECURITY_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "securityId", nullable = false)
	private int securityId;

	@Column(name = "SecurityIdSrc", nullable = true, length = 1)
	private char securityIdSrc;

	@Basic(optional = false)
	@Column(name = "Symbol", nullable = false, length = 50, unique=true)
	private String symbol;

	@Column(name = "Description", length = 255)
	private String description;
	
	@Column(name="ISIN", length=12, unique=true)
    private String isin;
	
	@Column(name="CETIPCode", length=20, unique=true)
    private String CETIPCode;
	
	@Column(name="CVMCode", length=12, unique=true)
    private String CVMCode;
	
	@Column(name="SELICCode", length=6, unique=true)
    private String SELICCode;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "securityId", fetch = FetchType.LAZY)
	private Set<Orders> ordersSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "securityId", fetch = FetchType.LAZY)
	private Set<MarketOrder> marketOrderSet;

	@OrderBy(value = "QDateTime")
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "securityId", fetch = FetchType.LAZY)
	private Set<HistoricalQuote> historicalQuoteSet;

	@OneToOne(cascade=CascadeType.ALL)
	@JoinColumn(name="securityInfoId", referencedColumnName="securityInfoId")
	private SecurityInfo securityInfoId;

	@JoinColumn(name = "stockExchangeId", referencedColumnName = "stockExchangeId", nullable = false)
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private StockExchange stockExchangeId;

	public Security() {
		this.securityIdSrc = '0';
		this.ordersSet = new HashSet<Orders>();
		this.marketOrderSet = new HashSet<MarketOrder>();
		this.historicalQuoteSet = new HashSet<HistoricalQuote>();
	}

	public Security(String symbol) {
		this.symbol = symbol;
		this.securityIdSrc = '0';
		this.ordersSet = new HashSet<Orders>();
		this.marketOrderSet = new HashSet<MarketOrder>();
		this.historicalQuoteSet = new HashSet<HistoricalQuote>();
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the ordersSet
	 */
	public Set<Orders> getOrdersSet() {
		return this.ordersSet;
	}

	/**
	 * @return the securityId
	 */
	public int getSecurityId() {
		return this.securityId;
	}

	public char getSecurityIdSrc() {
		return securityIdSrc;
	}

	public void setSecurityId(Integer securityId) {
		this.securityId = securityId;
	}

	public void setSecurityIdSrc(char securityIdSrc) {
		this.securityIdSrc = securityIdSrc;
	}

	/**
	 * @return the historicalQuoteSet
	 */
	public Set<HistoricalQuote> getHistoricalQuoteSet() {
		return this.historicalQuoteSet;
	}

	/**
	 * @return the marketOrderSet
	 */
	public Set<MarketOrder> getMarketOrderSet() {
		return this.marketOrderSet;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the securityInfoId
	 */
	public SecurityInfo getSecurityInfoId() {
		return this.securityInfoId;
	}

	/**
	 * @param securityInfoId the securityInfoId to set
	 */
	public void setSecurityInfoId(SecurityInfo securityInfoId) {
		this.securityInfoId = securityInfoId;
	}

	public StockExchange getStockExchangeId() {
		return stockExchangeId;
	}
	
	public void setStockExchangeId(StockExchange stockExchangeId) {
		this.stockExchangeId = stockExchangeId;
	}
	
	public String getCETIPCode() {
		return CETIPCode;
	}
	
	public void setCETIPCode(String cETIPCode) {
		CETIPCode = cETIPCode;
	}
	
	public String getIsin() {
		return isin;
	}
	
	public void setIsin(String isin) {
		this.isin = isin;
	}
	
	public String getCVMCode() {
		return CVMCode;
	}
	
	public void setCVMCode(String cVMCode) {
		CVMCode = cVMCode;
	}
	
	public String getSELICCode() {
		return SELICCode;
	}
	
	public void setSELICCode(String sELICCode) {
		SELICCode = sELICCode;
	}
	
	@Override
	public String toString() {
		return "Security [securityId=" + securityId + ", securityIdSrc=" + securityIdSrc + ", "
				+ (symbol != null ? "symbol=" + symbol + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (ordersSet != null ? "ordersSet=" + ordersSet + ", " : "")
				+ (marketOrderSet != null ? "marketOrderSet=" + marketOrderSet + ", " : "")
				+ (historicalQuoteSet != null ? "historicalQuoteSet=" + historicalQuoteSet + ", " : "")
				+ (securityInfoId != null ? "securityInfoId=" + securityInfoId + ", " : "")
				+ (stockExchangeId != null ? "stockExchangeId=" + stockExchangeId : "") + "]";
	}

}
