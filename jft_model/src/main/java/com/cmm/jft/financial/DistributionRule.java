/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.cmm.jft.core.enums.Objects;
import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>DistributionRule</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "DistributionRule", schema="Financial")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@NamedQueries({
		@NamedQuery(name = "DistributionRule.findAll", query = "SELECT d FROM DistributionRule d"),
		@NamedQuery(name = "DistributionRule.findByDistributionRuleID", query = "SELECT d FROM DistributionRule d WHERE d.distributionRuleID = :distributionRuleID"),
		@NamedQuery(name = "DistributionRule.findByRuleName", query = "SELECT d FROM DistributionRule d WHERE d.ruleName = :ruleName"),
		@NamedQuery(name = "DistributionRule.findByObjectRule", query = "SELECT d FROM DistributionRule d WHERE d.objectRule = :objectRule") 
		})
public class DistributionRule implements Serializable,
		DBObject<DistributionRule> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "DISTRIBUTIONRULE_SEQ", sequenceName = "DISTRIBUTIONRULE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "DISTRIBUTIONRULE_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "distributionRuleID", nullable = false)
	private Long distributionRuleID;
	@Column(name = "RuleName", length = 100)
	private String ruleName;
	
	@Enumerated(EnumType.STRING)
	@Column(name="ObjectRule", length=30)
	private Objects objectRule;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "distributionRuleID", fetch = FetchType.EAGER)
	private Set<Rule> ruleSet;

	public DistributionRule() {
		this.ruleSet = new HashSet<Rule>();
	}

	public DistributionRule(String ruleName) {
		this.ruleName = ruleName;
		this.ruleSet = new HashSet<Rule>();
	}

	/**
	 * @return the ruleName
	 */
	public String getRuleName() {
		return this.ruleName;
	}

	/**
	 * @param ruleName
	 *            the ruleName to set
	 */
	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/**
	 * @return the distributionRuleID
	 */
	public Long getDistributionRuleID() {
		return this.distributionRuleID;
	}

	/**
	 * @return the ruleSet
	 */
	public Set<Rule> getRuleSet() {
		return this.ruleSet;
	}
	
	/**
	 * @return the objectRule
	 */
	public Objects getObjectRule() {
		return this.objectRule;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "DistributionRule ["
				+ (this.distributionRuleID != null ? "distributionRuleID="
						+ this.distributionRuleID + ", " : "")
				+ (this.ruleName != null ? "ruleName=" + this.ruleName + ", "
						: "")
				+ (this.ruleSet != null ? "ruleSet=" + this.ruleSet : "") + "]";
	}

}
