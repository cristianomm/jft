/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial.old;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.cmm.jft.financial.Account;
import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>AccountTypes</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "AccountTypes", uniqueConstraints = { @UniqueConstraint(columnNames = { "TypeName" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "AccountTypes.findAll", query = "SELECT a FROM AccountTypes a"),
		@NamedQuery(name = "AccountTypes.findByAccountTypeID", query = "SELECT a FROM AccountTypes a WHERE a.accountTypeID = :accountTypeID"),
		@NamedQuery(name = "AccountTypes.findByTypeName", query = "SELECT a FROM AccountTypes a WHERE a.typeName = :typeName"),
		@NamedQuery(name = "AccountTypes.findByDescription", query = "SELECT a FROM AccountTypes a WHERE a.description = :description") })
public class AccountTypes implements Serializable, DBObject<AccountTypes> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "ACCOUNTTYPES_SEQ", sequenceName = "ACCOUNTTYPES_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ACCOUNTTYPES_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "accountTypeID", nullable = false)
	private Long accountTypeID;
	@Column(name = "TypeName", length = 30)
	private String typeName;
	@Column(name = "Description", length = 255)
	private String description;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "accountTypesID", fetch = FetchType.LAZY)
	private Set<Account> accountSet;

	public AccountTypes() {
		this.accountSet = new HashSet<Account>();
	}

	public AccountTypes(String typeName) {
		this.typeName = typeName;
		this.accountSet = new HashSet<Account>();
	}

	/**
	 * @return the typeName
	 */
	public String getTypeName() {
		return this.typeName;
	}

	/**
	 * @param typeName
	 *            the typeName to set
	 */
	public void setTypeName(String typeName) {
		this.typeName = typeName;
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
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the accountTypeID
	 */
	public Long getAccountTypeID() {
		return this.accountTypeID;
	}

	/**
	 * @return the accountSet
	 */
	public Set<Account> getAccountSet() {
		return this.accountSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "AccountTypes ["
				+ (this.accountTypeID != null ? "accountTypeID="
						+ this.accountTypeID + ", " : "")
				+ (this.typeName != null ? "typeName=" + this.typeName + ", "
						: "")
				+ (this.description != null ? "description=" + this.description
						+ ", " : "")
				+ (this.accountSet != null ? "accountSet=" + this.accountSet
						: "") + "]";
	}

}
