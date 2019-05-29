/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.financial;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.financial.Account;
import com.cmm.jft.model.financial.EntryRegister;
import com.cmm.jft.model.security.SecurityInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

/**
 *
 * <p>
 * <code>Currency</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Currency", schema="Financial")
@NamedQueries({
		@NamedQuery(name = "Currency.findAll", query = "SELECT c FROM Currency c"),
		@NamedQuery(name = "Currency.findByCurrencyId", query = "SELECT c FROM Currency c WHERE c.currencyId = :currencyId"),
		@NamedQuery(name = "Currency.findByCurrSymbol", query = "SELECT c FROM Currency c WHERE c.currSymbol = :currSymbol"),
		@NamedQuery(name = "Currency.findByDescription", query = "SELECT c FROM Currency c WHERE c.description = :description") })
public class Currency implements Serializable, DBObject<Currency> {
	private static final long serialVersionUId = 1L;

	@Id
	@Basic(optional = false)
	@Column(name = "currencyId", nullable = false, length = 5)
	private String currencyId;

	@Column(name = "CurrSymbol", length = 5)
	private String currSymbol;

	@Column(name = "Sign")
	private String sign;

	@Column(name = "FractionalUnit", length = 50)
	private String fractionalUnit;

	@Column(name = "Description", length = 255)
	private String description;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "currencyId", fetch = FetchType.LAZY)
	private Set<Account> accountSet;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "currencyId", fetch = FetchType.LAZY)
	private Set<EntryRegister> entryRegisterSet;
	
	@OneToMany(cascade=CascadeType.ALL, mappedBy="currencyId", fetch=FetchType.LAZY)
	private List<SecurityInfo> securityInfoList;
	
	
	public Currency() {
		this.accountSet = new HashSet<Account>();
		this.entryRegisterSet = new HashSet<EntryRegister>();
		this.securityInfoList = new ArrayList<SecurityInfo>();
	}

	public Currency(String currencyId) {
		this.currencyId = currencyId;
		this.accountSet = new HashSet<Account>();
		this.entryRegisterSet = new HashSet<EntryRegister>();
		this.securityInfoList = new ArrayList<SecurityInfo>();
	}

	/**
	 * @return the currSymbol
	 */
	public String getCurrSymbol() {
		return this.currSymbol;
	}

	/**
	 * @param currSymbol
	 *            the currSymbol to set
	 */
	public void setCurrSymbol(String currSymbol) {
		this.currSymbol = currSymbol;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return this.sign;
	}

	/**
	 * @param sign
	 *            the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the fractionalUnit
	 */
	public String getFractionalUnit() {
		return this.fractionalUnit;
	}

	/**
	 * @param fractionalUnit
	 *            the fractionalUnit to set
	 */
	public void setFractionalUnit(String fractionalUnit) {
		this.fractionalUnit = fractionalUnit;
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
	 * @return the currencyId
	 */
	public String getCurrencyId() {
		return this.currencyId;
	}

	/**
	 * @return the accountSet
	 */
	public Set<Account> getAccountSet() {
		return this.accountSet;
	}

	/**
	 * @return the entryRegisterSet
	 */
	public Set<EntryRegister> getEntryRegisterSet() {
		return this.entryRegisterSet;
	}
	
	/**
	 * @return the securityInfoList
	 */
	public List<SecurityInfo> getSecurityInfoList() {
		return this.securityInfoList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Currency [currencyId=" + this.currencyId + ", currSymbol="
				+ this.currSymbol + ", sign=" + this.sign + ", fractionalUnit="
				+ this.fractionalUnit + ", description=" + this.description
				+ ", accountSet=" + this.accountSet + ", entryRegisterSet="
				+ this.entryRegisterSet + ", securityInfoList="
				+ this.securityInfoList + "]";
	}

}
