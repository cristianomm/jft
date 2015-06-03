/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.financial;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.*;

import com.cmm.jft.db.DBObject;

/**
 *
 * <p>
 * <code>CurrencyQuote</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "CurrencyQuote")
@NamedQueries({
		@NamedQuery(name = "CurrencyQuote.findAll", query = "SELECT c FROM CurrencyQuote c"),
		@NamedQuery(name = "CurrencyQuote.findByCurrencyQuoteID", query = "SELECT c FROM CurrencyQuote c WHERE c.currencyQuoteID = :currencyQuoteID"),
		@NamedQuery(name = "CurrencyQuote.findByQDateTime", query = "SELECT c FROM CurrencyQuote c WHERE c.qDateTime = :qDateTime"),
		@NamedQuery(name = "CurrencyQuote.findByValue", query = "SELECT c FROM CurrencyQuote c WHERE c.value = :value") })
public class CurrencyQuote implements Serializable, DBObject<CurrencyQuote> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "CURRENCY_SEQ", sequenceName = "CURRENCY_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "CURRENCY_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "currencyQuoteID", nullable = false)
	private Long currencyQuoteID;
	@Basic(optional = false)
	@Column(name = "QDateTime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date qDateTime;
	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Basic(optional = false)
	@Column(name = "Value", nullable = false, precision = 19, scale = 6)
	private BigDecimal value;
	@JoinColumn(name = "currencyID", referencedColumnName = "currencyID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Currency currencyID;

	public CurrencyQuote() {
	}

	public CurrencyQuote(Long currencyQuoteID, Date qDateTime, BigDecimal value) {
		this.currencyQuoteID = currencyQuoteID;
		this.qDateTime = qDateTime;
		this.value = value;
	}

	public Long getCurrencyQuoteID() {
		return currencyQuoteID;
	}

	public void setCurrencyQuoteID(Long currencyQuoteID) {
		this.currencyQuoteID = currencyQuoteID;
	}

	public Date getQDateTime() {
		return qDateTime;
	}

	public void setQDateTime(Date qDateTime) {
		this.qDateTime = qDateTime;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	public Currency getCurrencyID() {
		return currencyID;
	}

	public void setCurrencyID(Currency currencyID) {
		this.currencyID = currencyID;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (currencyQuoteID != null ? currencyQuoteID.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof CurrencyQuote)) {
			return false;
		}
		CurrencyQuote other = (CurrencyQuote) object;
		if ((this.currencyQuoteID == null && other.currencyQuoteID != null)
				|| (this.currencyQuoteID != null && !this.currencyQuoteID
						.equals(other.currencyQuoteID))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.cmm.jft_core.CurrencyQuote[ currencyQuoteID="
				+ currencyQuoteID + " ]";
	}

	// /* (non-Javadoc)
	// * @see com.cmm.jft_core.DBObject#add()
	// */
	// @Override
	// public CurrencyQuote add() throws DataBaseException {
	// return (CurrencyQuote) DBFacade.getInstance()._persist(this);
	// }
	//
	// /* (non-Javadoc)
	// * @see com.cmm.jft_core.DBObject#update()
	// */
	// @Override
	// public CurrencyQuote update() throws DataBaseException {
	// return (CurrencyQuote) DBFacade.getInstance()._update(this);
	// }
	//
	// /* (non-Javadoc)
	// * @see com.cmm.jft_core.DBObject#remove()
	// */
	// @Override
	// public CurrencyQuote remove() throws DataBaseException {
	// return (CurrencyQuote) DBFacade.getInstance()._remove(this);
	// }
	//
	// /* (non-Javadoc)
	// * @see com.cmm.jft_core.DBObject#loadByKey(java.lang.Object)
	// */
	// @Override
	// public CurrencyQuote loadByKey(Object key) throws DataBaseException {
	// return (CurrencyQuote) DBFacade.getInstance()._findByKey(getClass(),
	// key);
	// }

}
