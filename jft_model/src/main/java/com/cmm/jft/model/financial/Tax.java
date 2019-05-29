/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.financial;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>Tax</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Tax", schema="Financial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t"),
		@NamedQuery(name = "Tax.findByTaxId", query = "SELECT t FROM Tax t WHERE t.taxId = :taxId"),
		@NamedQuery(name = "Tax.findByTaxName", query = "SELECT t FROM Tax t WHERE t.taxName = :taxName") })
public class Tax implements DBObject<Tax> {
	private static final long serialVersionUId = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "taxId", nullable = false, length = 10)
	private String taxId;
	@Basic(optional = false)
	@Column(name = "TaxName", nullable = false, length = 100)
	private String taxName;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "taxId", fetch = FetchType.LAZY)
	private Set<TaxSetup> taxSetupSet;

	public Tax() {
		this.taxSetupSet = new HashSet<TaxSetup>();
	}

	public Tax(String taxId, String taxName) {
		this.taxId = taxId;
		this.taxName = taxName;
		this.taxSetupSet = new HashSet<TaxSetup>();
	}

	public String getTaxId() {
		return taxId;
	}

	public String getTaxName() {
		return taxName;
	}

	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	public Set<TaxSetup> getTaxSetupSet() {
		return taxSetupSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Tax ["
				+ (this.taxId != null ? "taxId=" + this.taxId + ", " : "")
				+ (this.taxName != null ? "taxName=" + this.taxName + ", " : "")
				+ (this.taxSetupSet != null ? "taxSetupSet=" + this.taxSetupSet
						: "") + "]";
	}

}
