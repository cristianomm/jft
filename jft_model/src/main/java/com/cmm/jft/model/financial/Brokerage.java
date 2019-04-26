/**
 * 
 */
package com.cmm.jft.model.financial;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.security.enums.SecurityCategory;
import com.cmm.jft.model.trading.enums.TradeTypes;

/**
 * <p>
 * <code>Brokerage.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 08/08/2014 16:36:37
 *
 */
@Entity
@Table(name = "Brokerage", schema="Financial")
public class Brokerage implements DBObject<Brokerage> {

	@Id
	@SequenceGenerator(name = "BROKERAGE_SEQ", sequenceName = "BROKERAGE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "BROKERAGE_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "brokerageId", nullable = false)
	private Long brokerageId;

	@Enumerated(EnumType.STRING)
	@Column(name = "TradeType")
	private TradeTypes tradeType;

	@Enumerated(EnumType.STRING)
	@Column(name = "Category")
	private SecurityCategory category;

	@JoinColumn(name = "brokerId", referencedColumnName = "brokerId", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Broker brokerId;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "brokerageId")
	private List<Commission> commissionList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "brokerageId")
	private List<ExchangeTax> exchangeTaxList;

	/**
     * 
     */
	public Brokerage() {
		this.commissionList = new ArrayList<Commission>();
		this.exchangeTaxList = new ArrayList<ExchangeTax>();
	}

	/**
	 * @param tradeType
	 * @param category
	 * @param accountId
	 */
	public Brokerage(TradeTypes tradeType, SecurityCategory category,
			Broker brokerId) {
		super();
		this.brokerId = brokerId;
		this.tradeType = tradeType;
		this.category = category;
		this.commissionList = new ArrayList<Commission>();
		this.exchangeTaxList = new ArrayList<ExchangeTax>();
	}

	/**
	 * @return the tradeType
	 */
	public TradeTypes getTradeType() {
		return this.tradeType;
	}

	/**
	 * @param tradeType
	 *            the tradeType to set
	 */
	public void setTradeType(TradeTypes tradeType) {
		this.tradeType = tradeType;
	}

	/**
	 * @return the brokerageId
	 */
	public Long getBrokerageId() {
		return this.brokerageId;
	}

	/**
	 * @return the commissionList
	 */
	public List<Commission> getCommissionList() {
		return this.commissionList;
	}

	/**
	 * @return the exchangeTaxList
	 */
	public List<ExchangeTax> getExchangeTaxList() {
		return this.exchangeTaxList;
	}
	
	/**
	 * @return the category
	 */
	public SecurityCategory getCategory() {
		return this.category;
	}
	
	/**
	 * @param category the category to set
	 */
	public void setCategory(SecurityCategory category) {
		this.category = category;
	}

}
