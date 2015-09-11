/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>TaxSetup</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "TaxSetup", schema="Financial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "TaxSetup.findAll", query = "SELECT t FROM TaxSetup t"),
		@NamedQuery(name = "TaxSetup.findByTaxSetupID", query = "SELECT t FROM TaxSetup t WHERE t.taxSetupID = :taxSetupID"),
		@NamedQuery(name = "TaxSetup.findBySetupName", query = "SELECT t FROM TaxSetup t WHERE t.setupName = :setupName"),
		@NamedQuery(name = "TaxSetup.findByAliquota", query = "SELECT t FROM TaxSetup t WHERE t.aliquota = :aliquota"),
		@NamedQuery(name = "TaxSetup.findByTaxValue", query = "SELECT t FROM TaxSetup t WHERE t.taxValue = :taxValue") })
public class TaxSetup implements DBObject<TaxSetup> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "TAXSETUP_SEQ", sequenceName = "TAXSETUP_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "TAXSETUP_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "taxSetupID", nullable = false)
	private Long taxSetupID;
	@Column(name = "SetupName", length = 100)
	private String setupName;
	@Basic(optional = false)
	@Column(name = "Aliquota", nullable = false)
	private float aliquota;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@Column(name = "TaxValue", nullable = false, precision = 19, scale = 6)
	private BigDecimal taxValue;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "taxSetupID", fetch = FetchType.LAZY)
	private Set<Rule> ruleSet;
	@JoinColumn(name = "taxID", referencedColumnName = "taxID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Tax taxID;

	public TaxSetup() {
		this.ruleSet = new HashSet<Rule>();
	}

	/**
	 * @param setupName
	 * @param aliquota
	 * @param taxValue
	 */
	public TaxSetup(String setupName, float aliquota, BigDecimal taxValue) {
		super();
		this.setupName = setupName;
		this.aliquota = aliquota;
		this.taxValue = taxValue;
	}

	/**
	 * @return the setupName
	 */
	public String getSetupName() {
		return this.setupName;
	}

	/**
	 * @param setupName
	 *            the setupName to set
	 */
	public void setSetupName(String setupName) {
		this.setupName = setupName;
	}

	/**
	 * @return the aliquota
	 */
	public float getAliquota() {
		return this.aliquota;
	}

	/**
	 * @param aliquota
	 *            the aliquota to set
	 */
	public void setAliquota(float aliquota) {
		this.aliquota = aliquota;
	}

	/**
	 * @return the taxValue
	 */
	public BigDecimal getTaxValue() {
		return this.taxValue;
	}

	/**
	 * @param taxValue
	 *            the taxValue to set
	 */
	public void setTaxValue(BigDecimal taxValue) {
		this.taxValue = taxValue;
	}

	/**
	 * @return the taxID
	 */
	public Tax getTaxID() {
		return this.taxID;
	}

	/**
	 * @param taxID
	 *            the taxID to set
	 */
	public void setTaxID(Tax taxID) {
		this.taxID = taxID;
	}

	/**
	 * @return the taxSetupID
	 */
	public Long getTaxSetupID() {
		return this.taxSetupID;
	}

	/**
	 * @return the ruleSet
	 */
	public Set<Rule> getRuleSet() {
		return this.ruleSet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "TaxSetup ["
				+ (this.taxSetupID != null ? "taxSetupID=" + this.taxSetupID
						+ ", " : "")
				+ (this.setupName != null ? "setupName=" + this.setupName
						+ ", " : "")
				+ "aliquota="
				+ this.aliquota
				+ ", "
				+ (this.taxValue != null ? "taxValue=" + this.taxValue + ", "
						: "")
				+ (this.ruleSet != null ? "ruleSet=" + this.ruleSet + ", " : "")
				+ (this.taxID != null ? "taxID=" + this.taxID : "") + "]";
	}

}
