/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

import com.cmm.jft.financial.Account;
import com.cmm.jft.financial.DistributionRule;
import com.cmm.jft.financial.TaxSetup;
import com.cmm.jft.core.enums.Objects;
import com.cmm.jft.db.DBObject;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * <p>
 * <code>Rule</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:41 AM
 */
@Entity
@Table(name = "Rule", schema="Financial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "Rule.findAll", query = "SELECT r FROM Rule r"),
		@NamedQuery(name = "Rule.findByRuleID", query = "SELECT r FROM Rule r WHERE r.ruleID = :ruleID"),
		@NamedQuery(name = "Rule.findByApplyTax", query = "SELECT r FROM Rule r WHERE r.applyTax = :applyTax") })
public class Rule implements DBObject<Rule> {

	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "RULE_SEQ", sequenceName = "RULE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "RULE_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "ruleID", nullable = false)
	private Long ruleID;
	@Column(name = "ApplyTax")
	private boolean applyTax;

	@Column(name = "ApplyValue")
	private boolean applyValue;
	
	@Enumerated(EnumType.STRING)
	@Column(name="Object", length=30)
	private Objects object;
	
	@JoinColumn(name = "taxSetupID", referencedColumnName = "taxSetupID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TaxSetup taxSetupID;
	@JoinColumn(name = "distributionRuleID", referencedColumnName = "distributionRuleID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private DistributionRule distributionRuleID;

	@JoinColumn(name = "debitAccountID", referencedColumnName = "accountID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account debitAccountID;
	@JoinColumn(name = "creditAccountID", referencedColumnName = "accountID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account creditAccountID;

	@JoinColumn(name = "ruleFormulaID", referencedColumnName = "ruleFormulaID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private RuleFormula ruleFormulaID;
	
	
	public Rule() {
	}
	
	
	/**
	 * @return the applyTax
	 */
	public boolean isApplyTax() {
		return this.applyTax;
	}

	/**
	 * @param applyTax
	 *            the applyTax to set
	 */
	public void setApplyTax(boolean applyTax) {
		this.applyTax = applyTax;
	}

	/**
	 * @return the applyValue
	 */
	public boolean isApplyValue() {
		return this.applyValue;
	}

	/**
	 * @param applyValue
	 *            the applyValue to set
	 */
	public void setApplyValue(boolean applyValue) {
		this.applyValue = applyValue;
	}

	/**
	 * @return the taxSetupID
	 */
	public TaxSetup getTaxSetupID() {
		return this.taxSetupID;
	}

	/**
	 * @param taxSetupID
	 *            the taxSetupID to set
	 */
	public void setTaxSetupID(TaxSetup taxSetupID) {
		this.taxSetupID = taxSetupID;
	}

	/**
	 * @return the distributionRuleID
	 */
	public DistributionRule getDistributionRuleID() {
		return this.distributionRuleID;
	}

	/**
	 * @param distributionRuleID
	 *            the distributionRuleID to set
	 */
	public void setDistributionRuleID(DistributionRule distributionRuleID) {
		this.distributionRuleID = distributionRuleID;
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
	 * @return the ruleID
	 */
	public Long getRuleID() {
		return this.ruleID;
	}

	/**
	 * @return the ruleFormulaID
	 */
	public RuleFormula getMapRegisterID() {
		return ruleFormulaID;
	}

	/**
	 * @param ruleFormulaID
	 *            the ruleFormulaID to set
	 */
	public void setRuleFormulaID(RuleFormula ruleFormulaID) {
		this.ruleFormulaID = ruleFormulaID;
	}
	
	/**
	 * @return the object
	 */
	public Objects getObject() {
		return this.object;
	}
	
	/**
	 * @param object the object to set
	 */
	public void setObject(Objects object) {
		this.object = object;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Rule ["
				+ (this.ruleID != null ? "ruleID=" + this.ruleID + ", " : "")
				+ ("applyTax=" + this.applyTax + ", ")
				+ (this.taxSetupID != null ? "taxSetupID=" + this.taxSetupID
						+ ", " : "")
				+ (this.distributionRuleID != null ? "distributionRuleID="
						+ this.distributionRuleID + ", " : "")
				+ (this.debitAccountID != null ? "debitAccountID="
						+ this.debitAccountID + ", " : "")
				+ (this.creditAccountID != null ? "creditAccountID="
						+ this.creditAccountID + ", " : "")
				+ (this.ruleFormulaID != null ? "mapRegisterID="
						+ this.ruleFormulaID : "") + "]";
	}

}
