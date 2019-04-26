/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.security;

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
@Table(name = "StockExchange", schema="Security")
@NamedQueries({
	@NamedQuery(name = "StockExchange.findAll", query = "SELECT e FROM StockExchange e"),
	@NamedQuery(name = "StockExchange.findByStockExchangeId", query = "SELECT e FROM StockExchange e WHERE e.stockExchangeId = :stockExchangeId"),
	@NamedQuery(name = "StockExchange.findByExchangeName", query = "SELECT e FROM StockExchange e WHERE e.exchangeName = :exchangeName"),
	// @NamedQuery(name = "StockExchange.findByCountry", query =
	// "SELECT e FROM StockExchange e WHERE e.country = :country")
})
public class StockExchange implements DBObject<StockExchange> {
	private static final long serialVersionUId = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "stockExchangeId", nullable = false, length = 15)
	private String stockExchangeId;

	@Column(name = "ExchangeName", length = 100)
	private String exchangeName;

	@JoinColumn(name = "countryId", referencedColumnName = "countryId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Country countryId;

	//@OneToMany(cascade = CascadeType.ALL, mappedBy = "stockExchangeId", fetch = FetchType.LAZY)
	//private Set<Company> companySet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "stockExchangeId", fetch = FetchType.LAZY)
	private Set<Security> securitySet;

	public StockExchange() {
		//this.companySet = new HashSet<Company>();
		this.securitySet = new HashSet<Security>();
	}

	public StockExchange(String exchangeId, String exchangeName, Country country) {
		this.stockExchangeId = exchangeId;
		this.exchangeName = exchangeName;
		this.countryId = country;
		//this.companySet = new HashSet<Company>();
		this.securitySet = new HashSet<Security>();
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
	public Country getCountryId() {
		return this.countryId;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountryId(Country country) {
		this.countryId = country;
	}

	/**
	 * @return the stockExchangeId
	 */
	public String getStockExchangeId() {
		return this.stockExchangeId;
	}

	//	/**
	//	 * @return the companySet
	//	 */
	//	public Set<Company> getCompanySet() {
	//		return this.companySet;
	//	}

	/**
	 * @return the securitySet
	 */
	public Set<Security> getSecuritySet() {
		return this.securitySet;
	}

}
