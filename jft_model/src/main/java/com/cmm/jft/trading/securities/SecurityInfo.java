/**
 * 
 */
package com.cmm.jft.trading.securities;

import java.util.Date;

import javax.persistence.Basic;
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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.financial.Currency;
import com.cmm.jft.trading.enums.AssetTypes;
import com.cmm.jft.trading.enums.FutureSeries;
import com.cmm.jft.trading.enums.OptionSeries;
import com.cmm.jft.trading.enums.OptionStyles;
import com.cmm.jft.trading.enums.SecurityCategory;

/**
 * <p>
 * <code>Option.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 12/08/2013 01:03:22
 *
 */
@Entity
@Table(name = "SecurityInfo")
public class SecurityInfo implements DBObject<SecurityInfo> {

	@Id
	@SequenceGenerator(name = "SECURITYINFO_SEQ", sequenceName = "SECURITYINFO_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "SECURITYINFO_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "securityInfoID", nullable = false)
	private Long securityInfoID;

	@OneToOne(mappedBy = "securityInfoID")
	private Security securityID;

	@Temporal(TemporalType.DATE)
	@Column(name = "EmissionDate")
	private Date emissionDate;

	@Temporal(TemporalType.DATE)
	@Column(name = "ExpirationDate")
	private Date expirationDate;

	@Column(name = "ExcercisePrice", precision = 19, scale = 6)
	private double exercisePrice;

	@Enumerated(EnumType.STRING)
	@Column(name = "OptionStyle", length = 25)
	private OptionStyles optionStyle;

	@Enumerated(EnumType.STRING)
	@Column(name = "ObjectAsset", length = 1)
	private AssetTypes objectAsset;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "OptionSerie", length = 1)
	private OptionSeries optionSerie;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "FutureSerie", length = 1)
	private FutureSeries futureSerie;
	
	@Column(name = "MinimalVolume")
	private int minimalVolume;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SymbolDate")
	private Date symbolDate;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "Category", length = 30)
	private SecurityCategory category;
	
	@Column(name = "QuoteFactor")
	private int quoteFactor;
	

	public SecurityInfo() {

	}
		
	
	/**
	 * @param securityID
	 * @param category
	 */
	public SecurityInfo(Security securityID, SecurityCategory category) {
		super();
		this.securityID = securityID;
		this.category = category;
	}


	/**
	 * @param securityID
	 * @param emissionDate
	 * @param expirationDate
	 * @param exercisePrice
	 * @param optionStyle
	 * @param objectAsset
	 * @param optionSerie
	 */
	public SecurityInfo(Security securityID, Date emissionDate,
			Date expirationDate, double exercisePrice,
			OptionStyles optionStyle, AssetTypes objectAsset,
			OptionSeries optionSerie) {
		super();
		this.securityID = securityID;
		this.emissionDate = emissionDate;
		this.expirationDate = expirationDate;
		this.exercisePrice = exercisePrice;
		this.optionStyle = optionStyle;
		this.objectAsset = objectAsset;
		this.optionSerie = optionSerie;
	}

	/**
	 * @param securityID
	 * @param expirationDate
	 * @param objectAsset
	 * @param futureSerie
	 */
	public SecurityInfo(Security securityID, Date expirationDate,
			AssetTypes objectAsset, FutureSeries futureSerie) {
		super();
		this.securityID = securityID;
		this.expirationDate = expirationDate;
		this.objectAsset = objectAsset;
		this.futureSerie = futureSerie;
	}


	/**
	 * @return the emissionDate
	 */
	public Date getEmissionDate() {
		return this.emissionDate;
	}

	/**
	 * @param emissionDate
	 *            the emissionDate to set
	 */
	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	/**
	 * @return the expirationDate
	 */
	public Date getExpirationDate() {
		return this.expirationDate;
	}

	/**
	 * @param expirationDate
	 *            the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return the exercisePrice
	 */
	public double getExercisePrice() {
		return this.exercisePrice;
	}

	/**
	 * @param exercisePrice
	 *            the exercisePrice to set
	 */
	public void setExercisePrice(double exercisePrice) {
		this.exercisePrice = exercisePrice;
	}

	/**
	 * @return the optionStyle
	 */
	public OptionStyles getOptionStyle() {
		return this.optionStyle;
	}

	/**
	 * @param optionStyle
	 *            the optionStyle to set
	 */
	public void setOptionStyle(OptionStyles optionStyle) {
		this.optionStyle = optionStyle;
	}
	
	/**
	 * @return the futureSeries
	 */
	public FutureSeries getFutureSeries() {
		return this.futureSerie;
	}
	
	/**
	 * @return the objectAsset
	 */
	public AssetTypes getObjectAsset() {
		return this.objectAsset;
	}
	
	/**
	 * @return the optionSeries
	 */
	public OptionSeries getOptionSeries() {
		return this.optionSerie;
	}
	/**
	 * @return the securityInfoID
	 */
	public Long getSecurityInfoID() {
		return this.securityInfoID;
	}
	
	/**
	 * @return the securityID
	 */
	public Security getSecurityID() {
		return this.securityID;
	}
	
	/**
	 * @return the futureSerie
	 */
	public FutureSeries getFutureSerie() {
		return this.futureSerie;
	}
	
	/**
	 * @return the optionSerie
	 */
	public OptionSeries getOptionSerie() {
		return this.optionSerie;
	}
	
	/**
	 * @return the quoteFactor
	 */
	public int getQuoteFactor() {
		return this.quoteFactor;
	}
	
	/**
	 * @return the symbolDate
	 */
	public Date getSymbolDate() {
		return this.symbolDate;
	}
		
	/**
	 * @param quoteFactor the quoteFactor to set
	 */
	public void setQuoteFactor(int quoteFactor) {
		this.quoteFactor = quoteFactor;
	}
	
	/**
	 * @param symbolDate the symbolDate to set
	 */
	public void setSymbolDate(Date symbolDate) {
		this.symbolDate = symbolDate;
	}
	
	/**
	 * @return the minimalVolume
	 */
	public int getMinimalVolume() {
		return this.minimalVolume;
	}
	
	/**
	 * @param minimalVolume the minimalVolume to set
	 */
	public void setMinimalVolume(int minimalVolume) {
		this.minimalVolume = minimalVolume;
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
