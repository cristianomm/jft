/**
 * 
 */
package com.cmm.jft.marketdata;

import java.util.ArrayList;

import com.cmm.jft.security.Security;
import com.cmm.jft.trading.enums.MDEntryTypes;
import com.cmm.jft.trading.enums.MarketPhase;

/**
 * <p>
 * <code>MDSnapshot.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 27/07/2017 11:39:41
 *
 */
public class MDSnapshot {
    
    private int rptSeqNum;
    private int lstMsgSeqNum;
    private Security security;
    
    private double openPrice;
    private double closePrice;
    private double highPrice;
    private double lowPrice;
    private double vwapPrice;
    private int tradeVolume;
    private double financialVolume;
    private MarketPhase phase;
    private BandLimits limits;
    
    private ArrayList<MDEntry> buyEntries;
    private ArrayList<MDEntry> sellEntries;
    
    /**
     * 
     */
    public MDSnapshot(Security security) {
	this.security = security;
	this.buyEntries = new ArrayList<>(1000);
	this.sellEntries = new ArrayList<>(1000);
    }
    
    
    public void addOffer(MDEntry entry) {
	
	if(entry.getMdEntryType() == MDEntryTypes.BID) {
	    buyEntries.add(entry);
	}else if(entry.getMdEntryType() == MDEntryTypes.OFFER) {
	    sellEntries.add(entry);
	}
	
    }
    
    /**
     * Clear entries in the current snapshot, and update MsgSeqNum for the next SeqNum.
     * @param lstMsgSeqNum Last MsgSeqNum processed.
     */
    public void resetSnapshot(int lstMsgSeqNum) {
	this.lstMsgSeqNum = lstMsgSeqNum;
	buyEntries.clear();
	sellEntries.clear();
    }

    /**
     * @return the rptSeqNum
     */
    public int getRptSeqNum() {
        return rptSeqNum;
    }

    /**
     * @return the lstMsgSeqNum
     */
    public int getLstMsgSeqNum() {
        return lstMsgSeqNum;
    }

    /**
     * @return the security
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * @return the openPrice
     */
    public double getOpenPrice() {
        return openPrice;
    }

    /**
     * @return the closePrice
     */
    public double getClosePrice() {
        return closePrice;
    }

    /**
     * @return the highPrice
     */
    public double getHighPrice() {
        return highPrice;
    }

    /**
     * @return the lowPrice
     */
    public double getLowPrice() {
        return lowPrice;
    }

    /**
     * @return the vwapPrice
     */
    public double getVwapPrice() {
        return vwapPrice;
    }

    /**
     * @return the tradeVolume
     */
    public int getTradeVolume() {
        return tradeVolume;
    }
    
    /**
     * @return the financialVolume
     */
    public double getFinancialVolume() {
	return financialVolume;
    }

    /**
     * @return the phase
     */
    public MarketPhase getPhase() {
        return phase;
    }

    /**
     * @return the buyEntries
     */
    public ArrayList<MDEntry> getBuyEntries() {
        return buyEntries;
    }

    /**
     * @return the sellEntries
     */
    public ArrayList<MDEntry> getSellEntries() {
        return sellEntries;
    }
    
    /**
     * @return the limits
     */
    public BandLimits getLimits() {
	return limits;
    }

    /**
     * @param rptSeqNum the rptSeqNum to set
     */
    public void setRptSeqNum(int rptSeqNum) {
        this.rptSeqNum = rptSeqNum;
    }

    /**
     * @param lstMsgSeqNum the lstMsgSeqNum to set
     */
    public void setLstMsgSeqNum(int lstMsgSeqNum) {
        this.lstMsgSeqNum = lstMsgSeqNum;
    }

    /**
     * @param security the security to set
     */
    public void setSecurity(Security security) {
        this.security = security;
    }

    /**
     * @param openPrice the openPrice to set
     */
    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    /**
     * @param closePrice the closePrice to set
     */
    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    /**
     * @param highPrice the highPrice to set
     */
    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    /**
     * @param lowPrice the lowPrice to set
     */
    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    /**
     * @param vwapPrice the vwapPrice to set
     */
    public void setVwapPrice(double vwapPrice) {
        this.vwapPrice = vwapPrice;
    }

    /**
     * @param tradeVolume the tradeVolume to set
     */
    public void setTradeVolume(int tradeVolume) {
        this.tradeVolume = tradeVolume;
    }
    
    /**
     * @param financialVolume the financialVolume to set
     */
    public void setFinancialVolume(double financialVolume) {
	this.financialVolume = financialVolume;
    }

    /**
     * @param phase the phase to set
     */
    public void setPhase(MarketPhase phase) {
        this.phase = phase;
    }
    
    /**
     * @param limits the limits to set
     */
    public void setLimits(BandLimits limits) {
	this.limits = limits;
    }
    
}
