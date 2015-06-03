/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.trading.securities;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.db.DBObject;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

/**
 *
 * <p>
 * <code>Company</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Company")
@NamedQueries({
		@NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
		@NamedQuery(name = "Company.findByCompanyID", query = "SELECT c FROM Company c WHERE c.companyID = :companyID"),
		@NamedQuery(name = "Company.findByCompanyName", query = "SELECT c FROM Company c WHERE c.companyName = :companyName"),
		@NamedQuery(name = "Company.findByOnShares", query = "SELECT c FROM Company c WHERE c.onShares = :onShares"),
		@NamedQuery(name = "Company.findByPnShares", query = "SELECT c FROM Company c WHERE c.pnShares = :pnShares") })
public class Company implements DBObject<Company> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "COMPANY_SEQ", sequenceName = "COMPANY_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "COMPANY_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "companyID", nullable = false)
	private Long companyID;

	// @Column(name="MarketCode", length=5)
	// private String marketCode;

	@Column(name = "MarketName", length = 50)
	private String marketName;

	@Basic(optional = false)
	@Column(name = "CompanyName", nullable = false, length = 100)
	private String companyName;

	@Column(name = "OnShares")
	private Long onShares;

	@Column(name = "PnShares")
	private Long pnShares;

	@Column(name = "CNPJ", length = 14)
	private String cnpj;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status")
	private GeneralStatus status;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CompanyDate")
	private Date companyDate;

	@JoinTable(name = "CompanySegment", joinColumns = { @JoinColumn(name = "companyID", referencedColumnName = "companyID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "segmentID", referencedColumnName = "segmentID", nullable = false) })
	@ManyToMany(fetch = FetchType.LAZY)
	private Set<Segment> segmentSet;
	
	@JoinColumn(name = "stockExchangeID", referencedColumnName = "stockExchangeID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private StockExchange stockExchangeID;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "companyID", fetch = FetchType.LAZY)
	private Set<Isin> isinSet;


	public Company() {
		this.isinSet = new HashSet<Isin>();
		this.segmentSet = new HashSet<Segment>();
	}

	/**
	 * @param name
	 * @param onShares
	 * @param pnShares
	 * @param stockExchangeID
	 */
	public Company(String companyName, Long onShares, Long pnShares,
			StockExchange stockExchangeID) {
		super();
		this.companyName = companyName;
		this.onShares = onShares;
		this.pnShares = pnShares;
		this.stockExchangeID = stockExchangeID;
		this.isinSet = new HashSet<Isin>();
		this.segmentSet = new HashSet<Segment>();
	}

	// /**
	// * @return the marketCode
	// */
	// public String getMarketCode() {
	// return this.marketCode;
	// }
	//
	// /**
	// * @param marketCode the marketCode to set
	// */
	// public void setMarketCode(String marketCode) {
	// this.marketCode = marketCode;
	// }

	/**
	 * @return the marketName
	 */
	public String getMarketName() {
		return this.marketName;
	}

	/**
	 * @param marketName
	 *            the marketName to set
	 */
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	/**
	 * @return the status
	 */
	public GeneralStatus getStatus() {
		return this.status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(GeneralStatus status) {
		this.status = status;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return this.companyName;
	}

	/**
	 * @param companyName
	 *            the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the onShares
	 */
	public Long getOnShares() {
		return this.onShares;
	}

	/**
	 * @param onShares
	 *            the onShares to set
	 */
	public void setOnShares(Long onShares) {
		this.onShares = onShares;
	}

	/**
	 * @return the pnShares
	 */
	public Long getPnShares() {
		return this.pnShares;
	}

	/**
	 * @param pnShares
	 *            the pnShares to set
	 */
	public void setPnShares(Long pnShares) {
		this.pnShares = pnShares;
	}

	/**
	 * @return the stockExchangeID
	 */
	public StockExchange getStockExchangeID() {
		return this.stockExchangeID;
	}

	/**
	 * @param stockExchangeID
	 *            the stockExchangeID to set
	 */
	public void setStockExchangeID(StockExchange stockExchangeID) {
		this.stockExchangeID = stockExchangeID;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the companyID
	 */
	public Long getCompanyID() {
		return this.companyID;
	}

	/**
	 * @return the segmentSet
	 */
	public Set<Segment> getSegmentSet() {
		return this.segmentSet;
	}

	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return this.cnpj;
	}

	/**
	 * @param cnpj
	 *            the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the companyDate
	 */
	public Date getCompanyDate() {
		return this.companyDate;
	}

	/**
	 * @param companyDate
	 *            the companyDate to set
	 */
	public void setCompanyDate(Date companyDate) {
		this.companyDate = companyDate;
	}

	/**
	 * @return the isinSet
	 */
	public Set<Isin> getIsinSet() {
		return this.isinSet;
	}

}
