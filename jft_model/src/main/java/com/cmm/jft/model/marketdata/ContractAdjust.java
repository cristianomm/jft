/**
 * 
 */
package com.cmm.jft.model.marketdata;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.util.JpaConverters;

/**
 * <p>
 * <code>ContractAdjust.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Apr 1, 2019
 * 
 */
@Entity
@Table(name="ContractAdjust", schema="MarketData")
public class ContractAdjust implements DBObject<ContractAdjust> {

	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE)
	private Long contractAdjustId;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="securityId", referencedColumnName="securityId")
	private Security securityId;
	
	@Convert(converter=JpaConverters.LocalDateConverter.class)
	@Column(name="Date")
	private LocalDate date;
	
	@Column(name="Variation", scale=19, precision=8)
	private double variation;
	
	@Column(name="AdjPrice", scale=19, precision=8)
	private double adjPrice;
	
	@Column(name="AdjContractPrice", scale=19, precision=8)
	private double adjContractPrice;
	
	@Column(name="ExpirationCode", length=5)
	private String expirationCode;
	
	/**
	 * 
	 */
	public ContractAdjust() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the securityId
	 */
	public Security getSecurityId() {
		return securityId;
	}

	/**
	 * @param securityId the securityId to set
	 */
	public void setSecurityId(Security securityId) {
		this.securityId = securityId;
	}

	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * @return the variation
	 */
	public double getVariation() {
		return variation;
	}

	/**
	 * @param variation the variation to set
	 */
	public void setVariation(double variation) {
		this.variation = variation;
	}

	/**
	 * @return the adjPrice
	 */
	public double getAdjPrice() {
		return adjPrice;
	}

	/**
	 * @param adjPrice the adjPrice to set
	 */
	public void setAdjPrice(double adjPrice) {
		this.adjPrice = adjPrice;
	}

	/**
	 * @return the adjContractPrice
	 */
	public double getAdjContractPrice() {
		return adjContractPrice;
	}

	/**
	 * @param adjContractPrice the adjContractPrice to set
	 */
	public void setAdjContractPrice(double adjContractPrice) {
		this.adjContractPrice = adjContractPrice;
	}

	/**
	 * @return the expirationCode
	 */
	public String getExpirationCode() {
		return expirationCode;
	}

	/**
	 * @param expirationCode the expirationCode to set
	 */
	public void setExpirationCode(String expirationCode) {
		this.expirationCode = expirationCode;
	}

	/**
	 * @return the contractAdjustId
	 */
	public Long getContractAdjustId() {
		return contractAdjustId;
	}
	
}
