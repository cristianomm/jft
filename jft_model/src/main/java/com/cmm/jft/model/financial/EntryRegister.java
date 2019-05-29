/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.financial;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.financial.Account;
import com.cmm.jft.model.financial.enums.EntryType;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * <p>
 * <code>EntryRegister</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "EntryRegister", schema="Financial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "EntryRegister.findAll", query = "SELECT e FROM EntryRegister e"),
		@NamedQuery(name = "EntryRegister.findByEntryRegisterId", query = "SELECT e FROM EntryRegister e WHERE e.entryRegisterId = :entryRegisterId"),
		@NamedQuery(name = "EntryRegister.findByEntryType", query = "SELECT e FROM EntryRegister e WHERE e.entryType = :entryType"),
		@NamedQuery(name = "EntryRegister.findByCredit", query = "SELECT e FROM EntryRegister e WHERE e.credit = :credit"),
		@NamedQuery(name = "EntryRegister.findByDebit", query = "SELECT e FROM EntryRegister e WHERE e.debit = :debit"),
		@NamedQuery(name = "EntryRegister.findByDescription", query = "SELECT e FROM EntryRegister e WHERE e.description = :description"),
		@NamedQuery(name = "EntryRegister.findByOperationId", query = "SELECT e FROM EntryRegister e WHERE e.operationId = :operationId") })
public class EntryRegister implements Serializable, DBObject<EntryRegister> {
	
	private static final long serialVersionUId = 1L;
	
	@Id
	@SequenceGenerator(name = "ENTRYREGISTER_SEQ", sequenceName = "ENTRYREGISTER_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ENTRYREGISTER_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "entryRegisterId", nullable = false)
	private Long entryRegisterId;
	
	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "EntryType", nullable = false)
	private EntryType entryType;
	
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@Column(name = "Credit", nullable = false, precision = 19, scale = 6)
	private double credit;
	
	@Basic(optional = false)
	@Column(name = "Debit", nullable = false, precision = 19, scale = 6)
	private double debit;
	
	@Column(name = "Description", length = 255)
	private String description;
	
	@Column(name = "OperationId", length = 20)
	private String operationId;
	
	@JoinColumn(name = "entryId", referencedColumnName = "entryId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private JournalEntry entryId;
	
	@JoinColumn(name = "currencyId", referencedColumnName = "currencyId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Currency currencyId;
	
	@JoinColumn(name = "debitAccountId", referencedColumnName = "accountId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account debitAccountId;
	
	@JoinColumn(name = "creditAccountId", referencedColumnName = "accountId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account creditAccountId;

	public EntryRegister() {
	}

	/**
	 * @param entryType
	 * @param credit
	 * @param debit
	 * @param description
	 * @param currencyId
	 * @param debitAccountId
	 * @param creditAccountId
	 */
	public EntryRegister(EntryType entryType, double credit,
			double debit, String description, Currency currencyId,
			Account creditAccountId, Account debitAccountId) {
		super();
		this.entryType = entryType;
		this.credit = credit;
		this.debit = debit;
		this.description = description;
		this.currencyId = currencyId;
		this.debitAccountId = debitAccountId;
		this.creditAccountId = creditAccountId;
	}

	/**
	 * @return the entryType
	 */
	public EntryType getEntryType() {
		return this.entryType;
	}

	/**
	 * @param entryType
	 *            the entryType to set
	 */
	public void setEntryType(EntryType entryType) {
		this.entryType = entryType;
	}

	/**
	 * @return the credit
	 */
	public double getCredit() {
		return this.credit;
	}

	/**
	 * @param credit
	 *            the credit to set
	 */
	public void setCredit(double credit) {
		this.credit = credit;
	}

	/**
	 * @return the debit
	 */
	public double getDebit() {
		return this.debit;
	}

	/**
	 * @param debit
	 *            the debit to set
	 */
	public void setDebit(double debit) {
		this.debit = debit;
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
	 * @return the operationId
	 */
	public String getOperationId() {
		return this.operationId;
	}

	/**
	 * @param operationId
	 *            the operationId to set
	 */
	public void setOperationId(String operationId) {
		this.operationId = operationId;
	}

	/**
	 * @return the entryId
	 */
	public JournalEntry getEntryId() {
		return this.entryId;
	}

	/**
	 * @param entryId
	 *            the entryId to set
	 */
	public void setEntryId(JournalEntry entryId) {
		this.entryId = entryId;
	}

	/**
	 * @return the currencyId
	 */
	public Currency getCurrencyId() {
		return this.currencyId;
	}

	/**
	 * @param currencyId
	 *            the currencyId to set
	 */
	public void setCurrencyId(Currency currencyId) {
		this.currencyId = currencyId;
	}

	/**
	 * @return the debitAccountId
	 */
	public Account getDebitAccountId() {
		return this.debitAccountId;
	}

	/**
	 * @param debitAccountId
	 *            the debitAccountId to set
	 */
	public void setDebitAccountId(Account debitAccountId) {
		this.debitAccountId = debitAccountId;
	}

	/**
	 * @return the creditAccountId
	 */
	public Account getCreditAccountId() {
		return this.creditAccountId;
	}

	/**
	 * @param creditAccountId
	 *            the creditAccountId to set
	 */
	public void setCreditAccountId(Account creditAccountId) {
		this.creditAccountId = creditAccountId;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUId;
	}

	/**
	 * @return the entryRegisterId
	 */
	public Long getEntryRegisterId() {
		return this.entryRegisterId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EntryRegister ["
				+ (this.entryRegisterId != null ? "entryRegisterId="
						+ this.entryRegisterId + ", " : "")
				+ (this.entryType != null ? "entryType=" + this.entryType
						+ ", " : "")
				+ "credit=" + this.credit + ", "
				+ "debit=" + this.debit + ", " 
				+ (this.description != null ? "description=" + this.description
						+ ", " : "")
				+ (this.operationId != null ? "operationId=" + this.operationId
						+ ", " : "")
				+ (this.entryId != null ? "entryId="
						+ this.entryId.getEntryId() + ", " : "")
				+ (this.currencyId != null ? "currencyId=" + this.currencyId
						+ ", " : "")
				+ (this.debitAccountId != null ? "debitAccountId="
						+ this.debitAccountId + ", " : "")
				+ (this.creditAccountId != null ? "creditAccountId="
						+ this.creditAccountId : "") + "]";
	}

}
