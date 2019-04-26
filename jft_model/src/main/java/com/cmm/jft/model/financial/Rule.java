/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.financial;

import com.cmm.jft.core.enums.Objects;
import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.financial.Account;
import com.cmm.jft.model.financial.DistributionRule;
import com.cmm.jft.model.financial.TaxSetup;

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
		@NamedQuery(name = "Rule.findByRuleId", query = "SELECT r FROM Rule r WHERE r.ruleId = :ruleId"),
		@NamedQuery(name = "Rule.findByApplyTax", query = "SELECT r FROM Rule r WHERE r.applyTax = :applyTax") })
public class Rule implements DBObject<Rule> {

	private static final long serialVersionUId = 1L;
	@Id
	@SequenceGenerator(name = "RULE_SEQ", sequenceName = "RULE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "RULE_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "ruleId", nullable = false)
	private Long ruleId;
	@Column(name = "ApplyTax")
	private boolean applyTax;

	@Column(name = "ApplyValue")
	private boolean applyValue;
	
	@Enumerated(EnumType.STRING)
	@Column(name="Object", length=30)
	private Objects object;
	
	@JoinColumn(name = "taxSetupId", referencedColumnName = "taxSetupId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private TaxSetup taxSetupId;
	@JoinColumn(name = "distributionRuleId", referencedColumnName = "distributionRuleId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private DistributionRule distributionRuleId;

	@JoinColumn(name = "debitAccountId", referencedColumnName = "accountId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account debitAccountId;
	@JoinColumn(name = "creditAccountId", referencedColumnName = "accountId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Account creditAccountId;

	@JoinColumn(name = "ruleFormulaId", referencedColumnName = "ruleFormulaId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private RuleFormula ruleFormulaId;
	
	
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
	 * @return the taxSetupId
	 */
	public TaxSetup getTaxSetupId() {
		return this.taxSetupId;
	}

	/**
	 * @param taxSetupId
	 *            the taxSetupId to set
	 */
	public void setTaxSetupId(TaxSetup taxSetupId) {
		this.taxSetupId = taxSetupId;
	}

	/**
	 * @return the distributionRuleId
	 */
	public DistributionRule getDistributionRuleId() {
		return this.distributionRuleId;
	}

	/**
	 * @param distributionRuleId
	 *            the distributionRuleId to set
	 */
	public void setDistributionRuleId(DistributionRule distributionRuleId) {
		this.distributionRuleId = distributionRuleId;
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
	 * @return the ruleId
	 */
	public Long getRuleId() {
		return this.ruleId;
	}

	/**
	 * @return the ruleFormulaId
	 */
	public RuleFormula getMapRegisterId() {
		return ruleFormulaId;
	}

	/**
	 * @param ruleFormulaId
	 *            the ruleFormulaId to set
	 */
	public void setRuleFormulaId(RuleFormula ruleFormulaId) {
		this.ruleFormulaId = ruleFormulaId;
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
				+ (this.ruleId != null ? "ruleId=" + this.ruleId + ", " : "")
				+ ("applyTax=" + this.applyTax + ", ")
				+ (this.taxSetupId != null ? "taxSetupId=" + this.taxSetupId
						+ ", " : "")
				+ (this.distributionRuleId != null ? "distributionRuleId="
						+ this.distributionRuleId + ", " : "")
				+ (this.debitAccountId != null ? "debitAccountId="
						+ this.debitAccountId + ", " : "")
				+ (this.creditAccountId != null ? "creditAccountId="
						+ this.creditAccountId + ", " : "")
				+ (this.ruleFormulaId != null ? "mapRegisterId="
						+ this.ruleFormulaId : "") + "]";
	}

}
