/**
 * 
 */
package com.cmm.jft.model.trading;

import java.util.ArrayList;

import javax.annotation.Generated;
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
 * <code>Allocation.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 23, 2019
 * 
 */
@Entity
@Table(name="Allocation", schema="Trading")
public class Allocation {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long allocationId;
	
	@JoinColumn(name="securityId", referencedColumnName = "securityId")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Security securityId;
	
	@JoinColumn(name="portfolioId", referencedColumnName = "portfolioId")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Portfolio portfolioId;
	
	@Column(name="Value", precision=19, scale=8)
	private double value;
	
	@Column(name="PreviousValue", precision=19, scale=8)
	private double previousValue;
	
	/**
	 * 
	 */
	public Allocation() {
		
	}

	/**
	 * @return the allocationId
	 */
	public long getAllocationId() {
		return allocationId;
	}

	/**
	 * @param allocationId the allocationId to set
	 */
	public void setAllocationId(long allocationId) {
		this.allocationId = allocationId;
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
	
	/**
	 * @return the previousValue
	 */
	public double getPreviousValue() {
		return previousValue;
	}
	
	/**
	 * @param previousValue the previousValue to set
	 */
	public void setPreviousValue(double previousValue) {
		this.previousValue = previousValue;
	}
			
}
