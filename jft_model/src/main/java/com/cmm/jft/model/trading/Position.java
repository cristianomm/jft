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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.model.security.Security;

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
	
	@JoinColumn(name="securityId", referencedColumnName = "securityId")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Security securityId;
		
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="appliedAllocationId", referencedColumnName="appliedAllocationId")
	private AppliedAllocation appliedAllocationId;
	
	@Column(name="PositionDate", columnDefinition="timestamp")
	private LocalDateTime positionDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name="Status")
	private GeneralStatus status;
			
	@Column(name="Quantity", precision = 19, scale = 8)
	private double quantity;
	
	@Column(name="Price", precision = 19, scale = 8)
	private double price;
	
	@Column(name="Value", precision = 19, scale = 8)
	private double value;

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
	 * @return the positionDate
	 */
	public LocalDateTime getPositionDate() {
		return positionDate;
	}

	/**
	 * @param positionDate the positionDate to set
	 */
	public void setPositionDate(LocalDateTime positionDate) {
		this.positionDate = positionDate;
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

	/**
	 * @return the orders
	 */
	public List<Orders> getOrders() {
		return orders;
	}

	/**
	 * @param orders the orders to set
	 */
	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}
				
}
