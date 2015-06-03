/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

import com.cmm.jft.financial.Account;
import com.cmm.jft.financial.enums.EntryType;
import com.cmm.jft.db.DBObject;

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
@Table(name = "EntryRegister")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "EntryRegister.findAll", query = "SELECT e FROM EntryRegister e"),
		@NamedQuery(name = "EntryRegister.findByEntryRegisterID", query = "SELECT e FROM EntryRegister e WHERE e.entryRegisterID = :entryRegisterID"),
		@NamedQuery(name = "EntryRegister.findByEntryType", query = "SELECT e FROM EntryRegister e WHERE e.entryType = :entryType"),
		@NamedQuery(name = "EntryRegister.findByCredit", query = "SELECT e FROM EntryRegister e WHERE e.credit = :credit"),
		@NamedQuery(name = "EntryRegister.findByDebit", query = "SELECT e FROM EntryRegister e WHERE e.debit = :debit"),
		@NamedQuery(name = "EntryRegister.findByDescription", query = "SELECT e FROM EntryRegister e WHERE e.description = :description"),
		@NamedQuery(name = "EntryRegister.findByOperationID", query = "SELECT e FROM EntryRegister e WHERE e.operationID = :operationID") })
public class EntryRegister implements Serializable, DBObject<EntryRegister> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "ENTRYREGISTER_SEQ", sequenceName = "ENTRYREGISTER_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ENTRYREGISTER_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "entryRegisterID", nullable = false)
	private Long entryRegisterID;
	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "EntryType", nullable = false)
	private EntryType entryType;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@Column(name = "Credit", nullable = false, precision = 19, scale = 6)
	private BigDecimal credit;
	@Basic(optional = false)
	@Column(name = "Debit", nullable = false, precision = 19, scale = 6)
	private BigDecimal debit;
	@Column(name = "Description", length = 255)
	private String description;
	@Column(name = "OperationID", length = 20)
	private String operationID;
	@JoinColumn(name = "entryID", referencedColumnName = "entryID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private JournalEntry entryID;
	@JoinColumn(name = "currencyID", referencedColumnName = "currencyID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Currency currencyID;
	@JoinColumn(name = "debitAccountID", referencedColumnName = "accountID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account debitAccountID;
	@JoinColumn(name = "creditAccountID", referencedColumnName = "accountID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account creditAccountID;

	public EntryRegister() {
	}

	/**
	 * @param entryType
	 * @param credit
	 * @param debit
	 * @param description
	 * @param currencyID
	 * @param debitAccountID
	 * @param creditAccountID
	 */
	public EntryRegister(EntryType entryType, BigDecimal credit,
			BigDecimal debit, String description, Currency currencyID,
			Account creditAccountID, Account debitAccountID) {
		super();
		this.entryType = entryType;
		this.credit = credit;
		this.debit = debit;
		this.description = description;
		this.currencyID = currencyID;
		this.debitAccountID = debitAccountID;
		this.creditAccountID = creditAccountID;
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
	public BigDecimal getCredit() {
		return this.credit;
	}

	/**
	 * @param credit
	 *            the credit to set
	 */
	public void setCredit(BigDecimal credit) {
		this.credit = credit;
	}

	/**
	 * @return the debit
	 */
	public BigDecimal getDebit() {
		return this.debit;
	}

	/**
	 * @param debit
	 *            the debit to set
	 */
	public void setDebit(BigDecimal debit) {
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
	 * @return the operationID
	 */
	public String getOperationID() {
		return this.operationID;
	}

	/**
	 * @param operationID
	 *            the operationID to set
	 */
	public void setOperationID(String operationID) {
		this.operationID = operationID;
	}

	/**
	 * @return the entryID
	 */
	public JournalEntry getEntryID() {
		return this.entryID;
	}

	/**
	 * @param entryID
	 *            the entryID to set
	 */
	public void setEntryID(JournalEntry entryID) {
		this.entryID = entryID;
	}

	/**
	 * @return the currencyID
	 */
	public Currency getCurrencyID() {
		return this.currencyID;
	}

	/**
	 * @param currencyID
	 *            the currencyID to set
	 */
	public void setCurrencyID(Currency currencyID) {
		this.currencyID = currencyID;
	}

	/**
	 * @return the debitAccountID
	 */
	public Account getDebitAccountID() {
		return this.debitAccountID;
	}

	/**
	 * @param debitAccountID
	 *            the debitAccountID to set
	 */
	public void setDebitAccountID(Account debitAccountID) {
		this.debitAccountID = debitAccountID;
	}

	/**
	 * @return the creditAccountID
	 */
	public Account getCreditAccountID() {
		return this.creditAccountID;
	}

	/**
	 * @param creditAccountID
	 *            the creditAccountID to set
	 */
	public void setCreditAccountID(Account creditAccountID) {
		this.creditAccountID = creditAccountID;
	}

	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the entryRegisterID
	 */
	public Long getEntryRegisterID() {
		return this.entryRegisterID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EntryRegister ["
				+ (this.entryRegisterID != null ? "entryRegisterID="
						+ this.entryRegisterID + ", " : "")
				+ (this.entryType != null ? "entryType=" + this.entryType
						+ ", " : "")
				+ (this.credit != null ? "credit=" + this.credit + ", " : "")
				+ (this.debit != null ? "debit=" + this.debit + ", " : "")
				+ (this.description != null ? "description=" + this.description
						+ ", " : "")
				+ (this.operationID != null ? "operationID=" + this.operationID
						+ ", " : "")
				+ (this.entryID != null ? "entryID="
						+ this.entryID.getEntryID() + ", " : "")
				+ (this.currencyID != null ? "currencyID=" + this.currencyID
						+ ", " : "")
				+ (this.debitAccountID != null ? "debitAccountID="
						+ this.debitAccountID + ", " : "")
				+ (this.creditAccountID != null ? "creditAccountID="
						+ this.creditAccountID : "") + "]";
	}

}
