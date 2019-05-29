/**
 * 
 */
package com.cmm.jft.model.trading;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	
	@JoinColumn(name="closureId", referencedColumnName = "closureId")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Closure closureId;
	
	@Column(name="Date", columnDefinition="timestamp")
	private LocalDateTime date;
	
	@Column(name="Percentual", precision=19, scale=8)
	private double percentual;
	
	@Column(name="Value", precision=19, scale=8)
	private double value;
	
	@Column(name="LastValue", precision=19, scale=8)
	private double lastValue;
	
	@Column(name="Earnings", precision=19, scale=8)
	private double earnings;

	@Column(name="Variation", precision=19, scale=8)
	private double variation;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="positionId", fetch=FetchType.LAZY)
	private List<Position> positions;
	
	/**
	 * 
	 */
	public AppliedAllocation() {
		this.positions = new ArrayList<Position>();
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
	 * 
	 * @return the closureId
	 */
	public Closure getClosureId() {
		return closureId;
	}

	/**
	 * 
	 * @param closureId the closure to set
	 */
	public void setClosureId(Closure closureId) {
		this.closureId = closureId;
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
	 * @return the lastValue
	 */
	public double getLastValue() {
		return lastValue;
	}

	/**
	 * @param lastValue the lastValue to set
	 */
	public void setLastValue(double lastValue) {
		this.lastValue = lastValue;
	}

	/**
	 * @return the percentual
	 */
	public double getPercentual() {
		return percentual;
	}

	/**
	 * @param percentual the percentual to set
	 */
	public void setPercentual(double percentual) {
		this.percentual = percentual;
	}

	/**
	 * @return the earnings
	 */
	public double getEarnings() {
		return earnings;
	}

	/**
	 * @param earnings the earnings to set
	 */
	public void setEarnings(double earnings) {
		this.earnings = earnings;
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
	
	public LocalDateTime getDate() {
		return date;
	}
	
	public void setDate(LocalDateTime date) {
		this.date = date;
	}
	
	public List<Position> getPositions() {
		return positions;
	}
	
	public void setPositions(List<Position> positions) {
		this.positions = positions;
	}
}
