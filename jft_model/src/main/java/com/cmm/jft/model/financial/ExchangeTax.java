/**
 * 
 */
package com.cmm.jft.model.financial;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.criteria.Fetch;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.trading.enums.ValueTypes;

/**
 * <p>
 * <code>ExchangeTax.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 09/08/2014 02:17:17
 *
 */
@Entity
@Table(name = "ExchangeTax", schema="Financial")
public class ExchangeTax implements DBObject<ExchangeTax> {

	@Id
	@SequenceGenerator(name = "EXCHANGETAX_SEQ", sequenceName = "EXCHANGETAX_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "EXCHANGETAX_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "exchangeTaxId", nullable = false)
	private Long exchangeTaxId;

	@Column(name = "TaxName", length = 50)
	private String taxName;

	@Column(name = "Tax")
	private double tax;

	@Enumerated(EnumType.STRING)
	@Column(name = "CalcType", length = 30)
	private ValueTypes calcType;

	@JoinColumn(name = "brokerageId", referencedColumnName = "brokerageId", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Brokerage brokerageId;

	
	/**
     * 
     */
	public ExchangeTax() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param taxName
	 * @param aliquota
	 * @param brokerageId
	 */
	public ExchangeTax(String taxName, double tax, ValueTypes calcType,	Brokerage brokerageId) {
		super();
		this.taxName = taxName;
		this.tax = tax;
		this.calcType = calcType;
		this.brokerageId = brokerageId;
	}

	/**
	 * @return the taxName
	 */
	public String getTaxName() {
		return this.taxName;
	}

	/**
	 * @param taxName
	 *            the taxName to set
	 */
	public void setTaxName(String taxName) {
		this.taxName = taxName;
	}

	/**
	 * @return the tax
	 */
	public double getTax() {
		return this.tax;
	}

	/**
	 * @param tax
	 *            the tax to set
	 */
	public void setTax(double tax) {
		this.tax = tax;
	}

	/**
	 * @return the calcType
	 */
	public ValueTypes getCalcType() {
		return this.calcType;
	}

	/**
	 * @return the exchangeTaxId
	 */
	public Long getExchangeTaxId() {
		return this.exchangeTaxId;
	}

	/**
	 * @return the brokerageId
	 */
	public Brokerage getBrokerageId() {
		return this.brokerageId;
	}
	
}
