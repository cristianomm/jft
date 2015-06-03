/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.trading.securities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>StockExchange</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "StockExchange")
@NamedQueries({
		@NamedQuery(name = "StockExchange.findAll", query = "SELECT e FROM StockExchange e"),
		@NamedQuery(name = "StockExchange.findByStockExchangeID", query = "SELECT e FROM StockExchange e WHERE e.stockExchangeID = :stockExchangeID"),
		@NamedQuery(name = "StockExchange.findByExchangeName", query = "SELECT e FROM StockExchange e WHERE e.exchangeName = :exchangeName"),
// @NamedQuery(name = "StockExchange.findByCountry", query =
// "SELECT e FROM StockExchange e WHERE e.country = :country")
})
public class StockExchange implements DBObject<StockExchange> {
	private static final long serialVersionUID = 1L;

	@Id
	// @SequenceGenerator(name = "STOCKEXCHANGE_SEQ",
	// sequenceName="STOCKEXCHANGE_SEQ", allocationSize = 1, initialValue = 1)
	// @GeneratedValue(generator = "STOCKEXCHANGE_SEQ", strategy =
	// GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "stockExchangeID", nullable = false, length = 12)
	private String stockExchangeID;

	@Column(name = "ExchangeName", length = 100)
	private String exchangeName;

	@JoinColumn(name = "countryID", referencedColumnName = "countryID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Country countryID;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "stockExchangeID", fetch = FetchType.LAZY)
	private Set<Company> companySet;

	// @OneToMany(cascade = CascadeType.ALL, mappedBy = "stockExchangeID", fetch
	// = FetchType.LAZY)
	// private Set<Security> securitySet;

	public StockExchange() {
		this.companySet = new HashSet<Company>();
		// this.securitySet = new HashSet<Security>();
	}

	public StockExchange(String exchangeID, String exchangeName, Country country) {
		this.stockExchangeID = exchangeID;
		this.exchangeName = exchangeName;
		this.countryID = country;
		this.companySet = new HashSet<Company>();
		// this.securitySet = new HashSet<Security>();
	}

	/**
	 * @return the exchangeName
	 */
	public String getExchangeName() {
		return this.exchangeName;
	}

	/**
	 * @param exchangeName
	 *            the exchangeName to set
	 */
	public void setExchangeName(String exchangeName) {
		this.exchangeName = exchangeName;
	}

	/**
	 * @return the country
	 */
	public Country getCountryID() {
		return this.countryID;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountryID(Country country) {
		this.countryID = country;
	}

	/**
	 * @return the stockExchangeID
	 */
	public String getStockExchangeID() {
		return this.stockExchangeID;
	}

	/**
	 * @return the companySet
	 */
	public Set<Company> getCompanySet() {
		return this.companySet;
	}

	// /**
	// * @return the securitySet
	// */
	// public Set<Security> getSecuritySet() {
	// return this.securitySet;
	// }

}
