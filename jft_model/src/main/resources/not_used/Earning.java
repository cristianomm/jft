/**
 * 
 */
package com.cmm.jft.security;

import java.beans.ConstructorProperties;
import java.math.BigDecimal;
import java.util.Date;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.trading.enums.EarningType;

/**
 * <p>
 * <code>Earning.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 30/10/2013 15:19:32
 *
 */
@Entity
@Table(name = "Earning")
@NamedQueries({ @NamedQuery(name = "Earning.findAll", query = "select e from Earning e") })
public class Earning implements DBObject<Earning> {

	@Id
	@SequenceGenerator(name = "EARNING_SEQ", sequenceName = "EARNING_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "EARNING_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "earningID", nullable = false)
	private Long earningID;

	/**
	 * Tipo do pprovento
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "EarningType", length = 50)
	private EarningType earningType;

	/**
	 * Valor do provento
	 */
	@Column(name = "EarningValue", precision = 19, scale = 10)
	private BigDecimal earningValue;

	/**
	 * Data do provento
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ExDate")
	private Date exDate;

	/**
	 * Preco de fechamento na data do provento
	 */
	@Column(name = "ExPrice", precision = 19, scale = 6)
	private BigDecimal exPrice;

	@JoinColumn(name = "securityID", referencedColumnName = "securityID")
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Security securityID;

	/**
     * 
     */
	public Earning() {
		super();
	}

	/**
	 * @param earningType
	 * @param earningValue
	 * @param exDate
	 * @param exPrice
	 * @param securityID
	 */
	public Earning(EarningType earningType, BigDecimal earningValue,
			Date exDate, BigDecimal exPrice, Security securityID) {
		super();
		this.earningType = earningType;
		this.earningValue = earningValue;
		this.exDate = exDate;
		this.exPrice = exPrice;
		this.securityID = securityID;
	}

	/**
	 * @return the earningType
	 */
	public EarningType getEarningType() {
		return this.earningType;
	}

	/**
	 * @param earningType
	 *            the earningType to set
	 */
	public void setEarningType(EarningType earningType) {
		this.earningType = earningType;
	}

	/**
	 * @return the earningValue
	 */
	public BigDecimal getEarningValue() {
		return this.earningValue;
	}

	/**
	 * @param earningValue
	 *            the earningValue to set
	 */
	public void setEarningValue(BigDecimal earningValue) {
		this.earningValue = earningValue;
	}

	/**
	 * @return the exDate
	 */
	public Date getExDate() {
		return this.exDate;
	}

	/**
	 * @param exDate
	 *            the exDate to set
	 */
	public void setExDate(Date exDate) {
		this.exDate = exDate;
	}

	/**
	 * @return the exPrice
	 */
	public BigDecimal getExPrice() {
		return this.exPrice;
	}

	/**
	 * @param exPrice
	 *            the exPrice to set
	 */
	public void setExPrice(BigDecimal exPrice) {
		this.exPrice = exPrice;
	}

	/**
	 * @return the earningID
	 */
	public Long getEarningID() {
		return this.earningID;
	}

	/**
	 * @return the securityID
	 */
	public Security getSecurityID() {
		return this.securityID;
	}

}
