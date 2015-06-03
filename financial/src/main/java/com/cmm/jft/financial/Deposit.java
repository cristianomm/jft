/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>Deposit</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "Deposit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "Deposit.findAll", query = "SELECT d FROM Deposit d"),
		@NamedQuery(name = "Deposit.findByDepositID", query = "SELECT d FROM Deposit d WHERE d.depositID = :depositID"),
		@NamedQuery(name = "Deposit.findByDepositDate", query = "SELECT d FROM Deposit d WHERE d.depositDate = :depositDate"),
		@NamedQuery(name = "Deposit.findByValue", query = "SELECT d FROM Deposit d WHERE d.value = :value"),
		@NamedQuery(name = "Deposit.findByDescription", query = "SELECT d FROM Deposit d WHERE d.description = :description") })
public class Deposit implements Serializable, DBObject<Deposit> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "DEPOSIT_SEQ", sequenceName = "DEPOSIT_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "DEPOSIT_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "depositID", nullable = false)
	private Long depositID;
	@Basic(optional = false)
	@Column(name = "DepositDate", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date depositDate;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@Column(name = "Value", nullable = false, precision = 19, scale = 6)
	private BigDecimal value;
	@Column(name = "Description", length = 255)
	private String description;
	@JoinColumn(name = "DepositAccount", referencedColumnName = "accountID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account depositAccount;

	public Deposit() {
	}

	/**
	 * @param depositDate
	 * @param value
	 * @param description
	 * @param depositAccount
	 */
	public Deposit(Date depositDate, BigDecimal value, String description,
			Account depositAccount) {
		super();
		this.depositDate = depositDate;
		this.value = value;
		this.description = description;
		this.depositAccount = depositAccount;
	}

	/**
	 * @return the depositDate
	 */
	public Date getDepositDate() {
		return this.depositDate;
	}

	/**
	 * @param depositDate
	 *            the depositDate to set
	 */
	public void setDepositDate(Date depositDate) {
		this.depositDate = depositDate;
	}

	/**
	 * @return the value
	 */
	public BigDecimal getValue() {
		return this.value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(BigDecimal value) {
		this.value = value;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the depositAccount
	 */
	public Account getDepositAccount() {
		return this.depositAccount;
	}

	/**
	 * @param depositAccount
	 *            the depositAccount to set
	 */
	public void setDepositAccount(Account depositAccount) {
		this.depositAccount = depositAccount;
	}

	/**
	 * @return the depositID
	 */
	public Long getDepositID() {
		return this.depositID;
	}

}
