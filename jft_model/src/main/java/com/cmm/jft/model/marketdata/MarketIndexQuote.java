/**
 * 
 */
package com.cmm.jft.model.marketdata;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.security.MarketIndex;
import com.cmm.jft.model.util.JpaConverters;

/**
 * <p>
 * <code>MarketIndexQuote.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 29, 2019
 * 
 */
@Entity
@Table(name="MarketIndexQuote", schema="MarketData")
public class MarketIndexQuote implements DBObject<MarketIndexQuote> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long marketIndexQuoteId;
	
	@OrderBy(value="Date")
	@ManyToOne(cascade = CascadeType.ALL, fetch=FetchType.LAZY)
	private MarketIndex marketIndexId;
	
	@Convert(converter=JpaConverters.LocalDateConverter.class)
	@Column(name="Date")
	private LocalDate date;
	
	@Column(name="Value", scale=19, precision=8)
	private double value;
	
	
	/**
	 * 
	 */
	public MarketIndexQuote() {
		
	}
	
	/**
	 * @return the date
	 */
	public LocalDate getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(LocalDate date) {
		this.date = date;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(double value) {
		this.value = value;
	}

	/**
	 * @return the marketIndexQuoteId
	 */
	public long getMarketIndexQuoteId() {
		return marketIndexQuoteId;
	}
	
	/**
	 * @return the marketIndexId
	 */
	public MarketIndex getMarketIndexId() {
		return marketIndexId;
	}
	
	/**
	 * @param marketIndexId the marketIndexId to set
	 */
	public void setMarketIndexId(MarketIndex marketIndexId) {
		this.marketIndexId = marketIndexId;
	}
	
}
