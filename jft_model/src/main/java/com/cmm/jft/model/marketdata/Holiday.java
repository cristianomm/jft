/**
 * 
 */
package com.cmm.jft.model.marketdata;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.util.JpaConverters;

/**
 * <p>
 * <code>Holiday.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 28, 2019
 * 
 */

@Entity
@Table(name="Holiday", schema="MarketData")
public class Holiday implements DBObject<Holiday> {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long holidayId;
	
	@Convert(converter=JpaConverters.LocalDateConverter.class)
	@Column(name="Date")
	private LocalDate date;
	
	@Column(name="Description", length=100)
	private String description;
	
	/**
	 * 
	 */
	public Holiday() {
		// TODO Auto-generated constructor stub
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the holidayId
	 */
	public long getHolidayId() {
		return holidayId;
	}
		
}
