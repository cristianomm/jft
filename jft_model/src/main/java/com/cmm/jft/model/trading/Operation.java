/**
 * 
 */
package com.cmm.jft.model.trading;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.trading.enums.OperationTypes;
import com.cmm.jft.model.util.JpaConverters;

/**
 * <p>
 * <code>Operation.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 31, 2019
 * 
 */
@Entity
@Table(name="Operation", schema="Trading")
public class Operation implements DBObject<Operation>{

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long operationId;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="portfolioId", referencedColumnName="portfolioId")
	private Portfolio portfolioId;
	
	@Column(name="Value", scale=19, precision=8)
	private double value;
	
	@Convert(converter=JpaConverters.LocalDateConverter.class)
	@Column(name="Date")
	private LocalDate date;
	
	@Enumerated(EnumType.STRING)
	@Column(name="OperationType")
	private OperationTypes operationType;
		
	@Enumerated(EnumType.STRING)
	@Column(name="Status")
	private GeneralStatus status;
		
	/**
	 * 
	 */
	public Operation() {
	
	}

	/**
	 * @return the operationId
	 */
	public long getOperationId() {
		return operationId;
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
	 * @return the operationType
	 */
	public OperationTypes getOperationType() {
		return operationType;
	}

	/**
	 * @param operationType the operationType to set
	 */
	public void setOperationType(OperationTypes operationType) {
		this.operationType = operationType;
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
}
