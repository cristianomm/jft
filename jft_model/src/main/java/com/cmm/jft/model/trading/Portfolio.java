/**
 * 
 */
package com.cmm.jft.model.trading;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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

import com.cmm.jft.model.partner.BusinessPartner;

/**
 * <p>
 * <code>Portfolio.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 23, 2019
 * 
*/
@Entity
@Table(name="Portfolio", schema="Trading")
public class Portfolio {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long portfolioId;
		
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name = "partnerId", referencedColumnName = "partnerId", nullable = false)
	private BusinessPartner partnerId;
	
	@Column(name="Name", length=255)
	private String name;
	
	@Column(name="CreateDate", columnDefinition="TIMESTAMP")
	private LocalDateTime createDate;
	
	@Column(name="PositionDate", columnDefinition="TIMESTAMP")
	private LocalDateTime positionDate;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="allocationId", fetch=FetchType.LAZY)
	private List<Allocation> allocations;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="appliedAllocationId", fetch=FetchType.LAZY)
	private List<AppliedAllocation> appliedAllocations;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="operationId")
	private List<Operation> operationList;
	
	
	public Portfolio() {
		this.allocations = new ArrayList<>();
		this.appliedAllocations = new ArrayList<>();
		this.operationList = new ArrayList<>();
	}

	/**
	 * @return the portfolioId
	 */
	public long getPortfolioId() {
		return portfolioId;
	}

	/**
	 * @param portfolioId the portfolioId to set
	 */
	public void setPortfolioId(long portfolioId) {
		this.portfolioId = portfolioId;
	}

	/**
	 * @return the partnerId
	 */
	public BusinessPartner getPartnerId() {
		return partnerId;
	}

	/**
	 * @param partnerId the partnerId to set
	 */
	public void setPartnerId(BusinessPartner partnerId) {
		this.partnerId = partnerId;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the createDate
	 */
	public LocalDateTime getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
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
	 * @return the allocations
	 */
	public List<Allocation> getAllocations() {
		return allocations;
	}
	
	/**
	 * @param allocations the allocations to set
	 */
	public void setAllocations(List<Allocation> allocations) {
		this.allocations = allocations;
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
	
	/**
	 * @return the operationList
	 */
	public List<Operation> getOperationList() {
		return operationList;
	}
	
	/**
	 * @param operationList the operationList to set
	 */
	public void setOperationList(List<Operation> operationList) {
		this.operationList = operationList;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hash(partnerId, portfolioId);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Portfolio other = (Portfolio) obj;
		return Objects.equals(partnerId, other.partnerId) && portfolioId == other.portfolioId;
	}
		
}
