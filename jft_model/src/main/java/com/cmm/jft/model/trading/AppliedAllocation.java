/**
 * 
 */
package com.cmm.jft.model.trading;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cmm.jft.model.security.Security;

/**
 * <p>
 * <code>AppliedAllocation.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 23, 2019
 * 
 */
@Entity
@Table(name="AppliedAllocation", schema="Trading")
public class AppliedAllocation {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long appliedAllocationId;
	
	@JoinColumn(name="securityId", referencedColumnName = "securityId")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Security securityId;
	
	@JoinColumn(name="portfolioId", referencedColumnName = "portfolioId")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Portfolio portfolioId;
	
	@Column(name="Value", precision=19, scale=8)
	private double value;
	
	/**
	 * 
	 */
	public AppliedAllocation() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the appliedAllocationId
	 */
	public long getAppliedAllocationId() {
		return appliedAllocationId;
	}

	/**
	 * @param appliedAllocationId the appliedAllocationId to set
	 */
	public void setAppliedAllocationId(long appliedAllocationId) {
		this.appliedAllocationId = appliedAllocationId;
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
	 * @return the portfolioId
	 */
	public Portfolio getPortfolioId() {
		return portfolioId;
	}

	/**
	 * @param portfolioId the portfolioId to set
	 */
	public void setPortfolioId(Portfolio portfolioId) {
		this.portfolioId = portfolioId;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}
	
}
