/**
 * 
 */
package com.cmm.jft.financial;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.trading.marketdata.MarketOrder;

/**
 * <p>
 * <code>Broker.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 08/08/2014 16:28:25
 *
 */
@Entity
@Table(name = "Broker", schema="Financial")
@NamedQueries({
		@NamedQuery(name = "Broker.findAll", query = "SELECT b FROM Broker b"),
		@NamedQuery(name = "Broker.findByBrokerCode", query = "SELECT b FROM Broker b WHERE b.brokerCode = :brokerCode") })
public class Broker implements DBObject<Broker> {

	@Id
	@SequenceGenerator(name = "BROKER_SEQ", sequenceName = "BROKER_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "BROKER_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "brokerID", nullable = false)
	private Long brokerID;

	@Column(name = "BrokerCode", length = 20)
	private String brokerCode;

	@Column(name = "BrokerName", nullable = false)
	private String brokerName;
	
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "brokerID")
	private List<Brokerage> brokerageList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "brokerID")
	private Set<MarketOrder> marketOrderSet;

	
	/**
     * 
     */
	public Broker() {
		this.marketOrderSet = new HashSet<MarketOrder>();
		this.brokerageList = new ArrayList<Brokerage>();
	}

	/**
	 * @param brokerCode
	 * @param brokerName
	 */
	public Broker(String brokerCode, String brokerName) {
		super();
		this.brokerCode = brokerCode;
		this.brokerName = brokerName;
		this.marketOrderSet = new HashSet<MarketOrder>();
		this.brokerageList = new ArrayList<Brokerage>();
	}

	/**
	 * @return the brokerCode
	 */
	public String getBrokerCode() {
		return this.brokerCode;
	}

	/**
	 * @param brokerCode
	 *            the brokerCode to set
	 */
	public void setBrokerCode(String brokerCode) {
		this.brokerCode = brokerCode;
	}

	/**
	 * @return the brokerName
	 */
	public String getBrokerName() {
		return this.brokerName;
	}

	/**
	 * @param brokerName
	 *            the brokerName to set
	 */
	public void setBrokerName(String brokerName) {
		this.brokerName = brokerName;
	}

	/**
	 * @return the brokerID
	 */
	public Long getBrokerID() {
		return this.brokerID;
	}

	/**
	 * @return the brokerageList
	 */
	public List<Brokerage> getBrokerageList() {
		return this.brokerageList;
	}
	
	/**
	 * @return the marketOrderSet
	 */
	public Set<MarketOrder> getMarketOrderSet() {
		return this.marketOrderSet;
	}

	// @Override
	// public Broker add() throws DataBaseException {
	// return (Broker) DBFacade.getInstance()._persist(this);
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.cmm.jft_core.DBObject#update()
	// */
	// @Override
	// public Broker update() throws DataBaseException {
	// return (Broker) DBFacade.getInstance()._update(this);
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.cmm.jft_core.DBObject#remove()
	// */
	// @Override
	// public Broker remove() throws DataBaseException {
	// return (Broker) DBFacade.getInstance()._remove(this);
	// }
	//
	// /*
	// * (non-Javadoc)
	// *
	// * @see com.cmm.jft_core.DBObject#loadByKey(java.lang.Object)
	// */
	// @Override
	// public Broker loadByKey(Object key) throws DataBaseException {
	// return (Broker) DBFacade.getInstance()._findByKey(getClass(), key);
	// }

}
