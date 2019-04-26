/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.marketdata.ContractAdjust;
import com.cmm.jft.model.marketdata.HistoricalQuote;
import com.cmm.jft.model.marketdata.MarketOrder;
import com.cmm.jft.model.trading.Allocation;
import com.cmm.jft.model.trading.AppliedAllocation;
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
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "securityId", nullable = false)
	private Integer securityId;

	@Column(name = "SecurityIdSrc", nullable = true, length = 1)
	private char securityIdSrc;

	@Basic(optional = false)
	@Column(name = "Symbol", nullable = false, length = 50, unique=true)
	private String symbol;

	@Column(name = "MarketName", length = 100)
	private String marketName;
		
	@Column(name = "Description", length = 255)
	private String description;
	
	@Column(name="ISIN", length=12)
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
	
	@OrderBy(value = "Date")
	@OneToMany(cascade=CascadeType.ALL, mappedBy="securityId", fetch=FetchType.LAZY)
	private Set<SecurityEvent> eventSet;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="securityId", fetch=FetchType.LAZY)
	private Set<Allocation> allocationSet;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="securityId", fetch=FetchType.LAZY)
	private Set<AppliedAllocation> appliedAllocationSet;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="securityId", fetch=FetchType.LAZY)
	private List<ContractAdjust> contractAdjustList;

	public Security() {
		init();
	}

	public Security(String symbol) {
		this.symbol = symbol;
		init();
	}
	
	private void init() {
		this.securityIdSrc = '0';
		this.eventSet = new HashSet<>();
		this.allocationSet = new HashSet<>();
		this.ordersSet = new HashSet<Orders>();
		this.contractAdjustList = new ArrayList<>();
		this.appliedAllocationSet = new HashSet<>();
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
	public Integer getSecurityId() {
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
	 * @param marketName the marketName to set
	 */
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}
	
	/**
	 * @return the marketName
	 */
	public String getMarketName() {
		return marketName;
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
	
	/**
	 * @return the eventSet
	 */
	public Set<SecurityEvent> getEventSet() {
		return eventSet;
	}
	
	/**
	 * @return the allocationSet
	 */
	public Set<Allocation> getAllocationSet() {
		return allocationSet;
	}
	
	/**
	 * @return the appliedAllocationSet
	 */
	public Set<AppliedAllocation> getAppliedAllocationSet() {
		return appliedAllocationSet;
	}
	
	/**
	 * @return the contractAdjustList
	 */
	public List<ContractAdjust> getContractAdjustList() {
		return contractAdjustList;
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
