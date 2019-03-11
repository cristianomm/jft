/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.security;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.db.DBObject;

import java.time.LocalDate;
import java.util.Date;
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
@Table(name = "Company", schema = "Security")
@NamedQueries({
		@NamedQuery(name = "Company.findAll", query = "SELECT c FROM Company c"),
		@NamedQuery(name = "Company.findByCompanyID", query = "SELECT c FROM Company c WHERE c.companyID = :companyID"),
		@NamedQuery(name = "Company.findByCompanyName", query = "SELECT c FROM Company c WHERE c.companyName = :companyName")
		})
public class Company implements DBObject<Company> {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	@Basic(optional = false)
	@Column(name = "companyID", nullable = false)
	private Long companyID;

	// @Column(name="MarketCode", length=5)
	// private String marketCode;

	@Column(name = "MarketName", length = 100)
	private String marketName;

	@Basic(optional = false)
	@Column(name = "CompanyName", nullable = false, length = 255)
	private String companyName;
	
	@Column(name = "EmitterCode", length = 4, unique = true)
	private String emitterCode;
	
	@Column(name = "CVMCode", length = 10, unique = true)
	private String cvmCode;
	
	@Column(name = "CNPJ", length = 14, unique = true)
	private String cnpj;

	@Enumerated(EnumType.ORDINAL)
	@Column(name = "Status")
	private GeneralStatus status;

	
	@Column(name = "CompanyDate", columnDefinition="DATE")
	private LocalDate companyDate;

	//@JoinTable(name = "CompanySegment", joinColumns = { @JoinColumn(name = "companyID", referencedColumnName = "companyID", nullable = false) }, inverseJoinColumns = { @JoinColumn(name = "segmentID", referencedColumnName = "segmentID", nullable = false) })
	//@ManyToMany(fetch = FetchType.LAZY)
	//private Set<Segment> segmentSet;
	
	@JoinColumn(name = "stockExchangeID", referencedColumnName = "stockExchangeID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private StockExchange stockExchangeID;


	public Company() {
		//this.segmentSet = new HashSet<Segment>();
	}

	/**
	 * @param name
	 * @param onShares
	 * @param pnShares
	 * @param stockExchangeID
	 */
	public Company(String companyName, StockExchange stockExchangeID) {
		super();
		this.companyName = companyName;
		this.stockExchangeID = stockExchangeID;
		//this.segmentSet = new HashSet<Segment>();
	}

	/**
	 * @return the marketName
	 */
	public String getMarketName() {
		return marketName;
	}

	/**
	 * @param marketName the marketName to set
	 */
	public void setMarketName(String marketName) {
		this.marketName = marketName;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return companyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	/**
	 * @return the emitterCode
	 */
	public String getEmitterCode() {
		return emitterCode;
	}

	/**
	 * @param emitterCode the emitterCode to set
	 */
	public void setEmitterCode(String emitterCode) {
		this.emitterCode = emitterCode;
	}

	/**
	 * @return the cnpj
	 */
	public String getCnpj() {
		return cnpj;
	}

	/**
	 * @param cnpj the cnpj to set
	 */
	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	/**
	 * @return the status
	 */
	public GeneralStatus getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(GeneralStatus status) {
		this.status = status;
	}

	/**
	 * @return the companyDate
	 */
	public LocalDate getCompanyDate() {
		return companyDate;
	}

	/**
	 * @param companyDate the companyDate to set
	 */
	public void setCompanyDate(LocalDate companyDate) {
		this.companyDate = companyDate;
	}

	/**
	 * @return the stockExchangeID
	 */
	public StockExchange getStockExchangeID() {
		return stockExchangeID;
	}

	/**
	 * @param stockExchangeID the stockExchangeID to set
	 */
	public void setStockExchangeID(StockExchange stockExchangeID) {
		this.stockExchangeID = stockExchangeID;
	}

	/**
	 * @return the companyID
	 */
	public Long getCompanyID() {
		return companyID;
	}
	
	/**
	 * @return the cvmCode
	 */
	public String getCvmCode() {
		return cvmCode;
	}
	
	/**
	 * @param cvmCode the cvmCode to set
	 */
	public void setCvmCode(String cvmCode) {
		this.cvmCode = cvmCode;
	}
		
}
