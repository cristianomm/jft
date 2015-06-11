/**
 * 
 */
package com.cmm.jft.trading.securities;

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
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.financial.Currency;
import com.cmm.jft.trading.enums.AssetTypes;
import com.cmm.jft.trading.enums.FutureSeries;
import com.cmm.jft.trading.enums.OptionRights;
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
	
	@JoinColumn(name="currencyID", referencedColumnName="currencyID")
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private Currency currencyID;
	
	
	//SYMBOL;DESCRIPTION;ISIN;CURRENCY_BASE;CONTRACT_SIZE;TICK_SIZE;TICK_VALUE;DIGITS;MINIMAL_VOLUME;STEP_VOLUME
	@Column(name="ISIN", length=12, nullable=false)
	private String isin;
	
	/**
	 * Trade contract size
	 */
	@Column(name="ContractSize")
	private int contractSize;
	
	/**
	 * Minimal price change
	 */
	@Column(name="TickSize", precision = 19, scale = 6)
	private double tickSize;
	
	/**
	 * 
	 */
	@Column(name="TickValue", precision = 19, scale = 6)
	private double tickValue;
	
	/**
	 * 
	 */
	@Column(name="Digits")
	private int digits;
	
	/**
	 * Minimal volume for a deal
	 */
	@Column(name="MinimalVolume")
	private int minimalVolume;
	
	/**
	 * Minimal volume change step for deal execution
	 */
	@Column(name="StepVolume")
	private int stepVolume;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "ObjectAsset", length = 1)
	private AssetTypes objectAsset;	

	@Enumerated(EnumType.STRING)
	@Column(name = "Category", length = 30)
	private SecurityCategory category;	
	
	/**
	 * Date of the symbol trade beginning (usually used for futures)
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "EmissionDate")
	private Date emissionDate;
	
	/**
	 * Date of the symbol trade end (usually used for futures)
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "ExpirationDate")
	private Date expirationDate;
	
	//-------------------------------OptionSpecific
	@Enumerated(EnumType.STRING)
	@Column(name = "OptionStyle", length = 25)
	private OptionStyles optionStyle;
	
	/**
	 * Option right (Call/Put)
	 */
	@Enumerated(EnumType.STRING)
	@Column(name="OptionRight")
	private OptionRights optionRight;
	
	/**
	 * The strike price of an option. 
	 * The price at which an option buyer can buy (in a Call option) or sell (in a Put option) 
	 * the underlying asset, and the option seller is obliged to sell or buy 
	 * the appropriate amount of the underlying asset.
	 */
	@Column(name = "StrikePrice", precision = 19, scale = 6)
	private double strikePrice;
	



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
	 * @return the currencyID
	 */
	public Currency getCurrencyID() {
		return this.currencyID;
	}


	/**
	 * @param currencyID the currencyID to set
	 */
	public void setCurrencyID(Currency currencyID) {
		this.currencyID = currencyID;
	}


	/**
	 * @return the isin
	 */
	public String getIsin() {
		return this.isin;
	}


	/**
	 * @param isin the isin to set
	 */
	public void setIsin(String isin) {
		this.isin = isin;
	}


	/**
	 * @return the contractSize
	 */
	public int getContractSize() {
		return this.contractSize;
	}


	/**
	 * @param contractSize the contractSize to set
	 */
	public void setContractSize(int contractSize) {
		this.contractSize = contractSize;
	}


	/**
	 * @return the tickSize
	 */
	public double getTickSize() {
		return this.tickSize;
	}


	/**
	 * @param tickSize the tickSize to set
	 */
	public void setTickSize(double tickSize) {
		this.tickSize = tickSize;
	}


	/**
	 * @return the tickValue
	 */
	public double getTickValue() {
		return this.tickValue;
	}


	/**
	 * @param tickValue the tickValue to set
	 */
	public void setTickValue(double tickValue) {
		this.tickValue = tickValue;
	}


	/**
	 * @return the digits
	 */
	public int getDigits() {
		return this.digits;
	}


	/**
	 * @param digits the digits to set
	 */
	public void setDigits(int digits) {
		this.digits = digits;
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
	 * @return the stepVolume
	 */
	public int getStepVolume() {
		return this.stepVolume;
	}


	/**
	 * @param stepVolume the stepVolume to set
	 */
	public void setStepVolume(int stepVolume) {
		this.stepVolume = stepVolume;
	}


	/**
	 * @return the objectAsset
	 */
	public AssetTypes getObjectAsset() {
		return this.objectAsset;
	}


	/**
	 * @param objectAsset the objectAsset to set
	 */
	public void setObjectAsset(AssetTypes objectAsset) {
		this.objectAsset = objectAsset;
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


	/**
	 * @return the emissionDate
	 */
	public Date getEmissionDate() {
		return this.emissionDate;
	}


	/**
	 * @param emissionDate the emissionDate to set
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
	 * @param expirationDate the expirationDate to set
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}


	/**
	 * @return the optionStyle
	 */
	public OptionStyles getOptionStyle() {
		return this.optionStyle;
	}


	/**
	 * @param optionStyle the optionStyle to set
	 */
	public void setOptionStyle(OptionStyles optionStyle) {
		this.optionStyle = optionStyle;
	}


	/**
	 * @return the optionRight
	 */
	public OptionRights getOptionRight() {
		return this.optionRight;
	}


	/**
	 * @param optionRight the optionRight to set
	 */
	public void setOptionRight(OptionRights optionRight) {
		this.optionRight = optionRight;
	}


	/**
	 * @return the strikePrice
	 */
	public double getStrikePrice() {
		return this.strikePrice;
	}


	/**
	 * @param strikePrice the strikePrice to set
	 */
	public void setStrikePrice(double strikePrice) {
		this.strikePrice = strikePrice;
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
	
	
}
