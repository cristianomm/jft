/**
 * 
 */
package com.cmm.jft.model.trading;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
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
 * <p>
 * <code>Position.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 23, 2019
 * 
 */
@Entity
@Table(name="Position", schema="Trading")
public class Position {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long positionId;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="appliedAllocationId", referencedColumnName="appliedAllocationId")
	private AppliedAllocation appliedAllocationId;
	
	@Column(name="OpenDate", columnDefinition="timestamp")
	private LocalDateTime openDate; 
	
	@Column(name="CloseDate", columnDefinition="timestamp")
	private LocalDateTime closeDate;
	
	@Column(name="Quantity", precision = 19, scale = 8)
	private double quantity;
	
	@Column(name="OpenPrice", precision = 19, scale = 8)
	private double openPrice;
	
	@Column(name="Price", precision = 19, scale = 8)
	private double price;
	
	@Column(name="ClosePrice", precision = 19, scale = 8)
	private double closePrice;
	
	@Column(name="OpenValue", precision = 19, scale = 8)
	private double openValue;
	
	@Column(name="PresentValue", precision = 19, scale = 8)
	private double PresentValue;

	@Column(name="CloseValue", precision = 19, scale = 8)
	private double closeValue;

	@Column(name="Earnings", precision = 19, scale = 8)
	private double earnings;

	@Column(name="Variation", precision = 19, scale = 8)
	private double variation;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="orderId")
	private List<Orders> orders;
	
	/**
	 * 
	 */
	public Position() {
		this.orders = new ArrayList<>();
	}

	/**
	 * @return the positionId
	 */
	public long getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId the positionId to set
	 */
	public void setPositionId(long positionId) {
		this.positionId = positionId;
	}

	/**
	 * @return the appliedAllocationId
	 */
	public AppliedAllocation getAppliedAllocationId() {
		return appliedAllocationId;
	}

	/**
	 * @param appliedAllocationId the appliedAllocationId to set
	 */
	public void setAppliedAllocationId(AppliedAllocation appliedAllocationId) {
		this.appliedAllocationId = appliedAllocationId;
	}

	/**
	 * @return the openDate
	 */
	public LocalDateTime getOpenDate() {
		return openDate;
	}

	/**
	 * @param openDate the openDate to set
	 */
	public void setOpenDate(LocalDateTime openDate) {
		this.openDate = openDate;
	}

	/**
	 * @return the closeDate
	 */
	public LocalDateTime getCloseDate() {
		return closeDate;
	}

	/**
	 * @param closeDate the closeDate to set
	 */
	public void setCloseDate(LocalDateTime closeDate) {
		this.closeDate = closeDate;
	}

	/**
	 * @return the quantity
	 */
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the openPrice
	 */
	public double getOpenPrice() {
		return openPrice;
	}

	/**
	 * @param openPrice the openPrice to set
	 */
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @return the closePrice
	 */
	public double getClosePrice() {
		return closePrice;
	}

	/**
	 * @param closePrice the closePrice to set
	 */
	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}

	/**
	 * @return the openValue
	 */
	public double getOpenValue() {
		return openValue;
	}

	/**
	 * @param openValue the openValue to set
	 */
	public void setOpenValue(double openValue) {
		this.openValue = openValue;
	}

	/**
	 * @return the presentValue
	 */
	public double getPresentValue() {
		return PresentValue;
	}

	/**
	 * @param presentValue the presentValue to set
	 */
	public void setPresentValue(double presentValue) {
		PresentValue = presentValue;
	}

	/**
	 * @return the closeValue
	 */
	public double getCloseValue() {
		return closeValue;
	}

	/**
	 * @param closeValue the closeValue to set
	 */
	public void setCloseValue(double closeValue) {
		this.closeValue = closeValue;
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
	
}
