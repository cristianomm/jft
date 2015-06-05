/**
 * 
 */
package com.cmm.jft.trading.securities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.cmm.jft.db.DBObject;


/**
 * <p>
 * <code>Country.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 10/09/2013 23:41:08
 *
 */
@Entity
@Table(name = "Country")
//@NamedQueries({ @NamedQuery(name = "Country.findAll", query = "select c from Country c") })
public class Country implements DBObject<Country> {

	@Id
	@Column(name = "countryID", length = 3)
	private String countryID;

	@Column(name = "CountryName", length = 100, nullable = false)
	private String countryName;

	@Column(name = "Area", precision = 12, scale = 2)
	private Double area;

	@Column(name = "Population")
	private Long population;

	@Column(name = "Continent", length = 2)
	private String continent;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "countryID", fetch = FetchType.LAZY)
	private List<StockExchange> exchangesList;


	public Country() {
		
	}

	/**
	 * @param countryID
	 * @param countryName
	 * @param area
	 * @param population
	 */
	public Country(String countryID, String countryName, Double area,
			Long population, String continent) {
		
		this.countryID = countryID;
		this.countryName = countryName;
		this.area = area;
		this.population = population;
		this.continent = continent;
	}

	/**
	 * @return the countryName
	 */
	public String getCountryName() {
		return this.countryName;
	}

	/**
	 * @param countryName
	 *            the countryName to set
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * @return the area
	 */
	public Double getArea() {
		return this.area;
	}

	/**
	 * @param area
	 *            the area to set
	 */
	public void setArea(Double area) {
		this.area = area;
	}

	/**
	 * @return the population
	 */
	public Long getPopulation() {
		return this.population;
	}

	/**
	 * @param population
	 *            the population to set
	 */
	public void setPopulation(Long population) {
		this.population = population;
	}

	/**
	 * @return the countryID
	 */
	public String getCountryID() {
		return this.countryID;
	}

	/**
	 * @return the continent
	 */
	public String getContinent() {
		return this.continent;
	}

	/**
	 * @param continent
	 *            the continent to set
	 */
	public void setContinent(String continent) {
		this.continent = continent;
	}

}
