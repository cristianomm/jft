/**
 * 
 */
package com.cmm.jft.model.security;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
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
import com.cmm.jft.model.financial.Currency;
import com.cmm.jft.model.security.enums.AssetTypes;
import com.cmm.jft.model.security.enums.MarketTypes;
import com.cmm.jft.model.security.enums.OptionRights;
import com.cmm.jft.model.security.enums.OptionStyles;
import com.cmm.jft.model.security.enums.SecurityCategory;
import com.cmm.jft.model.util.JpaConverters;

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
@Table(name = "SecurityInfo", schema="Security")
public class SecurityInfo implements DBObject<SecurityInfo> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "securityInfoId")
    private Long securityInfoId;

    @OneToOne(mappedBy = "securityInfoId")
    private Security securityId;

    @JoinColumn(name="currencyId", referencedColumnName="currencyId")
    @ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private Currency currencyId;

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
    @Column(name="MinVolume")
    private int minVolume;

    /**
     * Maximal volume for a deal
     */
    @Column(name="MaxVolume")
    private int maxVolume;

    /**
     * Minimal volume change step for deal execution
     */
    @Column(name="StepVolume")
    private int stepVolume;

    @Column(name="RejectHiBand")
    private double rejectHiBand;

    @Column(name="RejectLoBand")
    private double rejectLoBand;

    @Column(name="AuctionBand")
    private double auctionBand;

    @Column(name="ISINAsset")
    private String ISINAsset;

    
    @Enumerated(EnumType.STRING)
    @Column(name = "ObjectAsset")
    private AssetTypes objectAsset;	

    @Enumerated(EnumType.STRING)
    @Column(name = "Category")
    private SecurityCategory category;	
    
    @Enumerated(EnumType.STRING)
    @Column(name = "MarketType")
    private MarketTypes marketType;	
    
    /**
     * Date of the symbol trade beginning (usually used for futures)
     */
    @Convert(converter=JpaConverters.LocalDateConverter.class)
    @Column(name = "EmissionDate")
    private LocalDate emissionDate;

    /**
     * Date of the symbol trade end (usually used for futures)
     */
    @Convert(converter=JpaConverters.LocalDateConverter.class)
    @Column(name = "ExpirationDate")
    private LocalDate expirationDate;

    //-------------------------------OptionSpecific
    @Enumerated(EnumType.STRING)
    @Column(name = "OptionStyle")
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
     * @param securityId
     * @param category
     */
    public SecurityInfo(Security securityId, SecurityCategory category) {
	super();
	this.securityId = securityId;
	this.category = category;
    }


    /**
     * @return the currencyId
     */
    public Currency getCurrencyId() {
	return this.currencyId;
    }


    /**
     * @param currencyId the currencyId to set
     */
    public void setCurrencyId(Currency currencyId) {
	this.currencyId = currencyId;
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
     * @return the minVolume
     */
    public int getMinVolume() {
	return this.minVolume;
    }


    /**
     * @param minVolume the minVolume to set
     */
    public void setMinVolume(int minVolume) {
	this.minVolume = minVolume;
    }

    /**
     * @return the maxVolume
     */
    public int getMaxVolume() {
	return this.maxVolume;
    }

    /**
     * @param maxVolume the maxVolume to set
     */
    public void setMaxVolume(int maxVolume) {
	this.maxVolume = maxVolume;
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
    public LocalDate getEmissionDate() {
	return this.emissionDate;
    }


    /**
     * @param emissionDate the emissionDate to set
     */
    public void setEmissionDate(LocalDate emissionDate) {
	this.emissionDate = emissionDate;
    }


    /**
     * @return the expirationDate
     */
    public LocalDate getExpirationDate() {
	return this.expirationDate;
    }


    /**
     * @param expirationDate the expirationDate to set
     */
    public void setExpirationDate(LocalDate expirationDate) {
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
     * @return the securityInfoId
     */
    public Long getSecurityInfoId() {
	return this.securityInfoId;
    }


    /**
     * @return the securityId
     */
    public Security getSecurityId() {
	return this.securityId;
    }


    /**
     * @return the auctionBand
     */
    public double getAuctionBand() {
	return auctionBand;
    }

    /**
     * @return the rejectHiBand
     */
    public double getRejectHiBand() {
	return rejectHiBand;
    }

    /**
     * @return the rejectLoBand
     */
    public double getRejectLoBand() {
	return rejectLoBand;
    }

    /**
     * @param auctionBand the auctionBand to set
     */
    public void setAuctionBand(double auctionBand) {
	this.auctionBand = auctionBand;
    }

    /**
     * @param rejectHiBand the rejectHiBand to set
     */
    public void setRejectHiBand(double rejectHiBand) {
	this.rejectHiBand = rejectHiBand;
    }

    /**
     * @param rejectLoBand the rejectLoBand to set
     */
    public void setRejectLoBand(double rejectLoBand) {
	this.rejectLoBand = rejectLoBand;
    }

	/**
	 * @return the marketType
	 */
	public MarketTypes getMarketType() {
		return marketType;
	}

	/**
	 * @param marketType the marketType to set
	 */
	public void setMarketType(MarketTypes marketType) {
		this.marketType = marketType;
	}

	/**
	 * @param securityId the securityId to set
	 */
	public void setSecurityId(Security securityId) {
		this.securityId = securityId;
	}
	
	/**
	 * @return the iSINAsset
	 */
	public String getISINAsset() {
		return ISINAsset;
	}
	/**
	 * @param iSINAsset the iSINAsset to set
	 */
	public void setISINAsset(String iSINAsset) {
		ISINAsset = iSINAsset;
	}
}
