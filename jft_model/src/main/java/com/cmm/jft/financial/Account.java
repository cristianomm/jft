/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

import com.cmm.jft.financial.enums.AccountCategories;
import com.cmm.jft.financial.enums.AccountTypes;
import com.cmm.jft.financial.exceptions.AccountException;
import com.cmm.jft.db.DBObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * <p>
 * <code>Account</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "Account", schema="Financial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a"),
		@NamedQuery(name = "Account.findByAccountID", query = "SELECT a FROM Account a WHERE a.accountID = :accountID"),
		@NamedQuery(name = "Account.findByAccName", query = "SELECT a FROM Account a WHERE a.accName = :accName"),
		@NamedQuery(name = "Account.findByDescription", query = "SELECT a FROM Account a WHERE a.description = :description"),
		@NamedQuery(name = "Account.findByBalance", query = "SELECT a FROM Account a WHERE a.balance = :balance"),
		@NamedQuery(name = "Account.findByOpen", query = "SELECT a FROM Account a WHERE a.open = :open") })
public class Account implements Serializable, DBObject<Account> {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "accountID", nullable = false, length = 20)
	private String accountID;
	
	@Column(name = "AccName", length = 100)
	private String accName;
	
	@Column(name = "Description", length = 255)
	private String description;
	
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "Balance", precision = 19, scale = 6)
	private BigDecimal balance;
	
	@Column(name = "CreditLimit", precision = 19, scale = 6)
	private BigDecimal creditLimit;

	@Column(name = "Open")
	private boolean open;

	@Enumerated(EnumType.STRING)
	@Column(name = "AccountType")
	private AccountTypes accountType;

	@Enumerated(EnumType.STRING)
	@Column(name = "AccountCategory")
	private AccountCategories accountCategory;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fatherAccountID", fetch = FetchType.LAZY)
	private List<Account> childAccounts;

	@JoinColumn(name = "fatherAccountID", referencedColumnName = "accountID", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Account fatherAccountID;

	@JoinColumn(name = "currencyID", referencedColumnName = "currencyID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Currency currencyID;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "depositAccount", fetch = FetchType.LAZY)
	private Set<Deposit> depositSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "debitAccountID", fetch = FetchType.LAZY)
	private Set<EntryRegister> entryRegisterDbtSet;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "creditAccountID", fetch = FetchType.LAZY)
	private Set<EntryRegister> entryRegisterCrdtSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "debitAccountID", fetch = FetchType.LAZY)
	private Set<Rule> ruleDbtAccSet;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "creditAccountID", fetch = FetchType.LAZY)
	private Set<Rule> ruleCrdtAccSet;

	public Account() {
		this.open = true;
		this.balance = new BigDecimal(0);
		this.depositSet = new HashSet<Deposit>();
		this.entryRegisterCrdtSet = new HashSet<EntryRegister>();
		this.entryRegisterDbtSet = new HashSet<EntryRegister>();
		this.ruleCrdtAccSet = new HashSet<Rule>();
		this.ruleDbtAccSet = new HashSet<Rule>();
	}

	public Account(String accountID, String accountName, double creditLimit,
			Currency currency, AccountTypes accountTypes,
			AccountCategories accountCategories) {
		this.open = true;
		this.balance = new BigDecimal(0);
		this.accountID = accountID;
		this.accName = accountName;
		this.currencyID = currency;
		this.accountType = accountTypes;
		this.accountCategory = accountCategories;
		this.creditLimit = new BigDecimal("" + creditLimit);
		this.depositSet = new HashSet<Deposit>();
		this.entryRegisterCrdtSet = new HashSet<EntryRegister>();
		this.entryRegisterDbtSet = new HashSet<EntryRegister>();
		this.ruleCrdtAccSet = new HashSet<Rule>();
		this.ruleDbtAccSet = new HashSet<Rule>();
	}

	/**
	 * @return the accName
	 */
	public String getAccName() {
		return this.accName;
	}

	/**
	 * @param accName
	 *            the accName to set
	 */
	public void setAccName(String accName) {
		this.accName = accName;
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
	 * @return the creditLimit
	 */
	public BigDecimal getCreditLimit() {
		return this.creditLimit;
	}

	/**
	 * @param creditLimit
	 *            the creditLimit to set
	 */
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
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
	 * @return the accountTypesID
	 */
	public AccountTypes getAccountType() {
		return this.accountType;
	}

	/**
	 * @param accountTypesID
	 *            the accountTypesID to set
	 */
	public void setAccountType(AccountTypes accountTypesID) {
		this.accountType = accountTypesID;
	}

	/**
	 * @return the accountCategory
	 */
	public AccountCategories getAccountCategory() {
		return accountCategory;
	}

	/**
	 * @param accountCategory
	 *            the accountCategory to set
	 */
	public void setAccountCategory(AccountCategories accountCategory) {
		this.accountCategory = accountCategory;
	}

	/**
	 * @return the childAccounts
	 */
	public List<Account> getChildAccounts() {
		return childAccounts;
	}

	/**
	 * @return the fatherAccountID
	 */
	public Account getFatherAccountID() {
		return fatherAccountID;
	}

	/**
	 * @param fatherAccountID
	 *            the fatherAccountID to set
	 */
	public void setFatherAccountID(Account fatherAccountID) {
		this.fatherAccountID = fatherAccountID;
	}

	/**
	 * @return the accountID
	 */
	public String getAccountID() {
		return this.accountID;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		this.balance = computeBalance();
		return this.balance;
	}

	/**
	 * @return the open
	 */
	public Boolean isOpen() {
		return this.open;
	}

	/**
	 * @return the depositSet
	 */
	public Set<Deposit> getDepositSet() {
		return this.depositSet;
	}

	/**
	 * @return the entryRegisterDbtSet
	 */
	public Set<EntryRegister> getEntryRegisterDbtSet() {
		return this.entryRegisterDbtSet;
	}

	/**
	 * @return the entryRegisterCrdtSet
	 */
	public Set<EntryRegister> getEntryRegisterCrdtSet() {
		return this.entryRegisterCrdtSet;
	}

	/**
	 * @return the ruleDbtAccSet
	 */
	public Set<Rule> getRuleDbtAccSet() {
		return this.ruleDbtAccSet;
	}

	/**
	 * @return the ruleCrdtAccSet
	 */
	public Set<Rule> getRuleCrdtAccSet() {
		return this.ruleCrdtAccSet;
	}

	@Override
	public String toString() {
		return "Account ["
				+ (accountID != null ? "accountID=" + accountID + ", " : "")
				+ (accName != null ? "accName=" + accName + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (balance != null ? "balance=" + balance + ", " : "")
				+ (creditLimit != null ? "creditLimit=" + creditLimit + ", " : "")
				+ "open=" + open + ", "
				+ (currencyID != null ? "currencyID=" + currencyID + ", " : "")
				+ (accountType != null ? "accountType=" + accountType + ", " : "")
				+ (depositSet != null ? "depositSet=" + depositSet + ", " : "")
				+ (entryRegisterDbtSet != null ? "entryRegisterDbtSet=" + entryRegisterDbtSet + ", " : "")
				+ (entryRegisterCrdtSet != null ? "entryRegisterCrdtSet=" + entryRegisterCrdtSet + ", " : "")
				+ (ruleDbtAccSet != null ? "ruleDbtAccSet=" + ruleDbtAccSet + ", " : "")
				+ (ruleCrdtAccSet != null ? "ruleCrdtAccSet=" + ruleCrdtAccSet : "") + "]";
	}

	public boolean hasFunds(BigDecimal value) {
		// maior que 0 no compareTo!!
		return balance.add(creditLimit).compareTo(value) > 0;
	}

	public BigDecimal totalFunds() {
		return balance.add(creditLimit);
	}

	public boolean debit(BigDecimal value) throws AccountException {
		boolean ret = false;
		BigDecimal aux = balance.subtract(value);
		if (aux.add(creditLimit).doubleValue() < 0) {
			throw new AccountException("Account " + accountID
					+ " not have funds: " + totalFunds());
		}

		if (!open) {
			throw new AccountException("Account are closed(id): " + accountID);
		}

		balance = balance.subtract(value);
		ret = true;
		
		return ret;
	}

	public boolean deposit(BigDecimal value) throws AccountException {

		boolean ret = false;
		if (!open) {
			throw new AccountException("Account are closed(id): " + accountID);
		}

		balance = balance.add(value);
		ret = true;

		return ret;
	}

	public boolean close() throws AccountException {
		boolean ret = false;

		if (!open) {
			throw new AccountException("Account are closed(id): " + accountID);
		}

		if (hasFunds(new BigDecimal("0"))) {
			open = false;
			ret = true;
		} else {
			throw new AccountException("Account " + accountID
					+ " dont have funds");
		}
		return ret;
	}
	
	private BigDecimal computeBalance(){
		BigDecimal blnc = new BigDecimal("0");
		if(accountCategory == AccountCategories.SYNTHETIC){
			for(Account cacc:childAccounts){
				//computa apenas as contas 'folha'
				//if(cacc.accountCategory==AccountCategories.ANALYTICAL){
				blnc.add(cacc.getBalance());
			}
			this.balance = blnc;
		}
		else{
			blnc = this.balance;
		}
		
		return blnc;
	}

}
