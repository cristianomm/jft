/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.financial;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.financial.enums.AccountCategories;
import com.cmm.jft.model.financial.enums.AccountTypes;
import com.cmm.jft.model.financial.exceptions.AccountException;
import com.cmm.jft.model.partner.PartnerAccount;

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
		@NamedQuery(name = "Account.findByAccountId", query = "SELECT a FROM Account a WHERE a.accountId = :accountId"),
		@NamedQuery(name = "Account.findByAccName", query = "SELECT a FROM Account a WHERE a.accName = :accName"),
		@NamedQuery(name = "Account.findByDescription", query = "SELECT a FROM Account a WHERE a.description = :description"),
		@NamedQuery(name = "Account.findByBalance", query = "SELECT a FROM Account a WHERE a.balance = :balance"),
		@NamedQuery(name = "Account.findByOpen", query = "SELECT a FROM Account a WHERE a.open = :open") })
public class Account implements DBObject<Account> {
	
	private static final long serialVersionUId = 1L;
	
	@Id
	@Basic(optional = false)
	@Column(name = "accountId", nullable = false, length = 20)
	private String accountId;
	
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

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "fatherAccountId", fetch = FetchType.LAZY)
	private List<Account> childAccounts;

	@JoinColumn(name = "fatherAccountId", referencedColumnName = "accountId", nullable = true)
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	private Account fatherAccountId;

	@JoinColumn(name = "currencyId", referencedColumnName = "currencyId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Currency currencyId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "depositAccount", fetch = FetchType.LAZY)
	private Set<Deposit> depositSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "debitAccountId", fetch = FetchType.LAZY)
	private Set<EntryRegister> entryRegisterDbtSet;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "creditAccountId", fetch = FetchType.LAZY)
	private Set<EntryRegister> entryRegisterCrdtSet;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "debitAccountId", fetch = FetchType.LAZY)
	private Set<Rule> ruleDbtAccSet;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "creditAccountId", fetch = FetchType.LAZY)
	private Set<Rule> ruleCrdtAccSet;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "debitAccountId", fetch = FetchType.LAZY)
	private Set<PartnerAccount> partnerAccountDbtSet;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "creditAccountId", fetch = FetchType.LAZY)
	private Set<PartnerAccount> partnerAccountCrdtSet;
	

	public Account() {
		defaultInit();
	}

	public Account(String accountId, String accountName, double creditLimit,
			Currency currency, AccountTypes accountTypes, AccountCategories accountCategories) {		
		this.accountId = accountId;
		this.accName = accountName;
		this.currencyId = currency;
		this.accountType = accountTypes;
		this.accountCategory = accountCategories;
		this.creditLimit = new BigDecimal("" + creditLimit);
		defaultInit();
	}
		
	private void defaultInit() {
		this.open = true;
		this.balance = new BigDecimal(0);
		this.depositSet = new HashSet<Deposit>();
		this.entryRegisterCrdtSet = new HashSet<EntryRegister>();
		this.entryRegisterDbtSet = new HashSet<EntryRegister>();
		this.ruleCrdtAccSet = new HashSet<Rule>();
		this.ruleDbtAccSet = new HashSet<Rule>();
		this.partnerAccountCrdtSet = new HashSet<>();
		this.partnerAccountDbtSet = new HashSet<>();
	}
		
	/**
	 * @return the accName
	 */
	public String getAccName() {
		return accName;
	}

	/**
	 * @param accName the accName to set
	 */
	public void setAccName(String accName) {
		this.accName = accName;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the balance
	 */
	public BigDecimal getBalance() {
		return balance;
	}

	/**
	 * @param balance the balance to set
	 */
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	/**
	 * @return the creditLimit
	 */
	public BigDecimal getCreditLimit() {
		return creditLimit;
	}

	/**
	 * @param creditLimit the creditLimit to set
	 */
	public void setCreditLimit(BigDecimal creditLimit) {
		this.creditLimit = creditLimit;
	}

	/**
	 * @return the open
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * @param open the open to set
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	/**
	 * @return the accountType
	 */
	public AccountTypes getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(AccountTypes accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the accountCategory
	 */
	public AccountCategories getAccountCategory() {
		return accountCategory;
	}

	/**
	 * @param accountCategory the accountCategory to set
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
	 * @param childAccounts the childAccounts to set
	 */
	public void setChildAccounts(List<Account> childAccounts) {
		this.childAccounts = childAccounts;
	}

	/**
	 * @return the fatherAccountId
	 */
	public Account getFatherAccountId() {
		return fatherAccountId;
	}

	/**
	 * @param fatherAccountId the fatherAccountId to set
	 */
	public void setFatherAccountId(Account fatherAccountId) {
		this.fatherAccountId = fatherAccountId;
	}

	/**
	 * @return the currencyId
	 */
	public Currency getCurrencyId() {
		return currencyId;
	}

	/**
	 * @param currencyId the currencyId to set
	 */
	public void setCurrencyId(Currency currencyId) {
		this.currencyId = currencyId;
	}

	/**
	 * @return the depositSet
	 */
	public Set<Deposit> getDepositSet() {
		return depositSet;
	}

	/**
	 * @param depositSet the depositSet to set
	 */
	public void setDepositSet(Set<Deposit> depositSet) {
		this.depositSet = depositSet;
	}

	/**
	 * @return the entryRegisterDbtSet
	 */
	public Set<EntryRegister> getEntryRegisterDbtSet() {
		return entryRegisterDbtSet;
	}

	/**
	 * @param entryRegisterDbtSet the entryRegisterDbtSet to set
	 */
	public void setEntryRegisterDbtSet(Set<EntryRegister> entryRegisterDbtSet) {
		this.entryRegisterDbtSet = entryRegisterDbtSet;
	}

	/**
	 * @return the entryRegisterCrdtSet
	 */
	public Set<EntryRegister> getEntryRegisterCrdtSet() {
		return entryRegisterCrdtSet;
	}

	/**
	 * @param entryRegisterCrdtSet the entryRegisterCrdtSet to set
	 */
	public void setEntryRegisterCrdtSet(Set<EntryRegister> entryRegisterCrdtSet) {
		this.entryRegisterCrdtSet = entryRegisterCrdtSet;
	}

	/**
	 * @return the ruleDbtAccSet
	 */
	public Set<Rule> getRuleDbtAccSet() {
		return ruleDbtAccSet;
	}

	/**
	 * @param ruleDbtAccSet the ruleDbtAccSet to set
	 */
	public void setRuleDbtAccSet(Set<Rule> ruleDbtAccSet) {
		this.ruleDbtAccSet = ruleDbtAccSet;
	}

	/**
	 * @return the ruleCrdtAccSet
	 */
	public Set<Rule> getRuleCrdtAccSet() {
		return ruleCrdtAccSet;
	}

	/**
	 * @param ruleCrdtAccSet the ruleCrdtAccSet to set
	 */
	public void setRuleCrdtAccSet(Set<Rule> ruleCrdtAccSet) {
		this.ruleCrdtAccSet = ruleCrdtAccSet;
	}

	/**
	 * @return the partnerAccountDbtSet
	 */
	public Set<PartnerAccount> getPartnerAccountDbtSet() {
		return partnerAccountDbtSet;
	}

	/**
	 * @param partnerAccountDbtSet the partnerAccountDbtSet to set
	 */
	public void setPartnerAccountDbtSet(Set<PartnerAccount> partnerAccountDbtSet) {
		this.partnerAccountDbtSet = partnerAccountDbtSet;
	}

	/**
	 * @return the partnerAccountCrdtSet
	 */
	public Set<PartnerAccount> getPartnerAccountCrdtSet() {
		return partnerAccountCrdtSet;
	}

	/**
	 * @param partnerAccountCrdtSet the partnerAccountCrdtSet to set
	 */
	public void setPartnerAccountCrdtSet(Set<PartnerAccount> partnerAccountCrdtSet) {
		this.partnerAccountCrdtSet = partnerAccountCrdtSet;
	}

	/**
	 * @return the accountId
	 */
	public String getAccountId() {
		return accountId;
	}

	@Override
	public String toString() {
		return "Account ["
				+ (accountId != null ? "accountId=" + accountId + ", " : "")
				+ (accName != null ? "accName=" + accName + ", " : "")
				+ (description != null ? "description=" + description + ", " : "")
				+ (balance != null ? "balance=" + balance + ", " : "")
				+ (creditLimit != null ? "creditLimit=" + creditLimit + ", " : "")
				+ "open=" + open + ", "
				+ (currencyId != null ? "currencyId=" + currencyId + ", " : "")
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
			throw new AccountException("Account " + accountId
					+ " not have funds: " + totalFunds());
		}

		if (!open) {
			throw new AccountException("Account are closed(id): " + accountId);
		}

		balance = balance.subtract(value);
		ret = true;
		
		return ret;
	}

	public boolean deposit(BigDecimal value) throws AccountException {

		boolean ret = false;
		if (!open) {
			throw new AccountException("Account are closed(id): " + accountId);
		}

		balance = balance.add(value);
		ret = true;

		return ret;
	}

	public boolean close() throws AccountException {
		boolean ret = false;

		if (!open) {
			throw new AccountException("Account are closed(id): " + accountId);
		}

		if (hasFunds(new BigDecimal("0"))) {
			open = false;
			ret = true;
		} else {
			throw new AccountException("Account " + accountId
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
