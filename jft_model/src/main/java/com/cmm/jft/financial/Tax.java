/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

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
@Table(name = "Tax")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "Tax.findAll", query = "SELECT t FROM Tax t"),
		@NamedQuery(name = "Tax.findByTaxID", query = "SELECT t FROM Tax t WHERE t.taxID = :taxID"),
		@NamedQuery(name = "Tax.findByTaxName", query = "SELECT t FROM Tax t WHERE t.taxName = :taxName") })
public class Tax implements DBObject<Tax> {
	private static final long serialVersionUID = 1L;
	@Id
	@Basic(optional = false)
	@Column(name = "taxID", nullable = false, length = 10)
	private String taxID;
	@Basic(optional = false)
	@Column(name = "TaxName", nullable = false, length = 100)
	private String taxName;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "taxID", fetch = FetchType.LAZY)
	private Set<TaxSetup> taxSetupSet;

	public Tax() {
		this.taxSetupSet = new HashSet<TaxSetup>();
	}

	public Tax(String taxID, String taxName) {
		this.taxID = taxID;
		this.taxName = taxName;
		this.taxSetupSet = new HashSet<TaxSetup>();
	}

	public String getTaxID() {
		return taxID;
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
				+ (this.taxID != null ? "taxID=" + this.taxID + ", " : "")
				+ (this.taxName != null ? "taxName=" + this.taxName + ", " : "")
				+ (this.taxSetupSet != null ? "taxSetupSet=" + this.taxSetupSet
						: "") + "]";
	}

}
