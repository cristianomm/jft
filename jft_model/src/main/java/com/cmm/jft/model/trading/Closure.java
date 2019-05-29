/**
 * 
 */
package com.cmm.jft.model.trading;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
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

/**
 * @author Cristiano M Martins
 *
 */
@Entity
@Table(name = "Closure", schema = "Trading")
public class Closure {
	
	public static class ClosureComparator implements Comparator<Closure> {
		@Override
		public int compare(Closure o1, Closure o2) {
			return o1.getDate().compareTo(o2.getDate());
		};		
	}
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private long closureId;
	
	@JoinColumn(name = "portfolioId", referencedColumnName = "portfolioId")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Portfolio portfolioId;

	@Column(name = "Date", columnDefinition = "timestamp")
	private LocalDateTime date;
	
	@Column(name = "Value", precision = 19, scale = 8)
	private double value;
	
	@Column(name = "LastValue", precision = 19, scale = 8)
	private double lastValue;
	
	@Column(name = "Variation", precision = 19, scale = 8)
	private double variation;
	
	@Column(name = "Earnings", precision = 19, scale = 8)
	private double earnings;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="appliedAllocationId", fetch=FetchType.LAZY)
	private List<AppliedAllocation> appliedAllocations;
	
	
	public Closure() {
		this.appliedAllocations = new ArrayList<AppliedAllocation>();
	}
	
	public long getClosureId() {
		return closureId;
	}

	public void setClosureId(long closureId) {
		this.closureId = closureId;
	}

	public Portfolio getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(Portfolio portfolioId) {
		this.portfolioId = portfolioId;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public double getValue() {
		return value;
	}

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

	public double getVariation() {
		return variation;
	}

	public void setVariation(double variation) {
		this.variation = variation;
	}

	public double getEarnings() {
		return earnings;
	}

	public void setEarnings(double earnings) {
		this.earnings = earnings;
	}

	/**
	 * @return the appliedAllocations
	 */
	public List<AppliedAllocation> getAppliedAllocations() {
		return appliedAllocations;
	}

	/**
	 * @param appliedAllocations the appliedAllocations to set
	 */
	public void setAppliedAllocations(List<AppliedAllocation> appliedAllocations) {
		this.appliedAllocations = appliedAllocations;
	}
	
	
}
