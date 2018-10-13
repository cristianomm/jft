/**
 * 
 */
package com.cmm.jft.financial;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Level;

import com.cmm.jft.financial.Rule;
import com.cmm.jft.financial.exceptions.FormulaException;
import com.cmm.jft.db.DBObject;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>RuleFormula.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 06/08/2013 12:43:33
 *
 */
@Entity
@Table(name = "RuleFormula", schema="Financial")
public class RuleFormula implements DBObject<RuleFormula> {

	@Id
	@SequenceGenerator(name = "RULEFORMULA_SEQ", sequenceName = "RULEFORMULA_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "RULEFORMULA_SEQ", strategy = GenerationType.AUTO)
	@Column(name = "ruleFormulaID")
	private Long ruleFormulaID;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "ruleFormulaID", fetch = FetchType.EAGER)
	private Set<Rule> ruleSet;

	@Column(name = "ValueFormula", length = 255)
	private String valueFormula;

	@Transient
	private double value;

	@Column(name = "Description", length = 100)
	private String description;

	@Transient
	private Formula formula;

	/**
     * 
     */
	public RuleFormula() {
		super();
	}

	/**
	 * @param objectID
	 * @param value
	 * @param description
	 */
	public RuleFormula(String valueFormula, String description) {
		super();
		this.valueFormula = valueFormula;
		this.description = description;
	}

	/**
	 * @return the value
	 */
	public String getValueFormula() {
		return this.valueFormula;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String valueFormula) {
		this.valueFormula = valueFormula;
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
	 * @return the ruleFormulaID
	 */
	public Long getRuleFormulaID() {
		return this.ruleFormulaID;
	}

	/**
	 * @return the ruleSet
	 */
	public Set<Rule> getRuleSet() {
		return this.ruleSet;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return this.value;
	}

	/**
	 * @param valueFormula
	 *            the valueFormula to set
	 */
	public void setValueFormula(String valueFormula) {
		this.valueFormula = valueFormula;
	}

	/**
	 * @return the formula
	 */
	public Formula getFormula() {

		if (formula == null) {
			try {
				formula = new Formula(valueFormula);
			} catch (FormulaException e) {
				Logging.getInstance().log(Formula.class, e, Level.ERROR);
			}
		}
		return formula;
	}

	// /* (non-Javadoc)
	// * @see com.cmm.jft_core.DBObject#add()
	// */
	// @Override
	// public RuleFormula add() throws DataBaseException {
	// return (RuleFormula)DBFacade.getInstance()._persist(this);
	// }
	//
	// /* (non-Javadoc)
	// * @see com.cmm.jft_core.DBObject#update()
	// */
	// @Override
	// public RuleFormula update() throws DataBaseException {
	// return (RuleFormula)DBFacade.getInstance()._update(this);
	// }
	//
	// /* (non-Javadoc)
	// * @see com.cmm.jft_core.DBObject#remove()
	// */
	// @Override
	// public RuleFormula remove() throws DataBaseException {
	// return (RuleFormula)DBFacade.getInstance()._remove(this);
	// }
	//
	// /* (non-Javadoc)
	// * @see com.cmm.jft_core.DBObject#loadByKey(java.lang.Object)
	// */
	// @Override
	// public RuleFormula loadByKey(Object key) throws DataBaseException {
	// RuleFormula mr = (RuleFormula)
	// DBFacade.getInstance()._findByKey(getClass(), key);
	// try {
	// mr.formula = new Formula(valueFormula);
	// } catch (FormulaException e) {
	// Logging.getInstance().log(RuleFormula.class, e, Level.ERROR);
	// }
	//
	// return mr;
	// }

}
