/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.MarketPhase;

/**
 * <p><code>Market.java</code></p>
 * @author cristiano
 * @version Nov 3, 2015 6:57:10 PM
 *
 */
public class Market {
	
	
	private Security security;
		
	/**
	 * MDEntryType=0
	 */
	private ConcurrentLinkedQueue<Orders> buyQueue;
	
	/**
	 * MDEntryType=1
	 */
	private ConcurrentLinkedQueue<Orders> sellQueue;
	
	/**
	 * MDEntryType=2
	 */
	private long tradeCount;
	
	/**
	 * MDEntryType=4
	 */
	private double openPrice;
	
	/**
	 * MDEntryType=5
	 */
	private double closePrice;
	
	/**
	 * MDEntryType=7
	 */
	private double maxPrice;
	
	/**
	 * MDEntryType=8
	 */
	private double minPrice;
	
	private double lastVolume;
	
	private double lastPrice;
	
	
	/**
	 * MDEntryType=9
	 */
	private double vwap;
	
	/**
	 * MDEntryType=B
	 */
	private double tradeVolume;
	
	/**
	 * MDEntryType=c
	 */
	private MarketPhase phase;
	
	
	
	private Market(String symbol){
		this.phase = MarketPhase.Close;
		this.security = SecurityService.getInstance().provideSecurity(symbol);
		resetMarketData();
	}
	
	
	private void resetMarketData(){
		this.buyQueue = new ConcurrentLinkedQueue<>();
		this.sellQueue = new ConcurrentLinkedQueue<>();
		
	}
	
	
	public void addSnapshot(){
		
		//snapshot:
		//fila de ordens compra e venda
		//estatisticas do mercado
		
		
		
	}
	
	public void addSnapShotFullRefresh(){
		
	}


	/**
	 * @return the security
	 */
	public Security getSecurity() {
		return security;
	}


	/**
	 * @return the buyQueue
	 */
	public ConcurrentLinkedQueue<Orders> getBuyQueue() {
		return buyQueue;
	}


	/**
	 * @return the sellQueue
	 */
	public ConcurrentLinkedQueue<Orders> getSellQueue() {
		return sellQueue;
	}


	/**
	 * @return the tradeCount
	 */
	public long getTradeCount() {
		return tradeCount;
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
	 * @return the maxPrice
	 */
	public double getMaxPrice() {
		return maxPrice;
	}


	/**
	 * @return the minPrice
	 */
	public double getMinPrice() {
		return minPrice;
	}


	/**
	 * @return the lastVolume
	 */
	public double getLastVolume() {
		return lastVolume;
	}


	/**
	 * @return the lastPrice
	 */
	public double getLastPrice() {
		return lastPrice;
	}


	/**
	 * @return the vwap
	 */
	public double getVwap() {
		return vwap;
	}


	/**
	 * @return the tradeVolume
	 */
	public double getTradeVolume() {
		return tradeVolume;
	}


	/**
	 * @return the phase
	 */
	public MarketPhase getPhase() {
		return phase;
	}


	/**
	 * @param security the security to set
	 */
	public void setSecurity(Security security) {
		this.security = security;
	}


	/**
	 * @param buyQueue the buyQueue to set
	 */
	public void setBuyQueue(ConcurrentLinkedQueue<Orders> buyQueue) {
		this.buyQueue = buyQueue;
	}


	/**
	 * @param sellQueue the sellQueue to set
	 */
	public void setSellQueue(ConcurrentLinkedQueue<Orders> sellQueue) {
		this.sellQueue = sellQueue;
	}


	/**
	 * @param tradeCount the tradeCount to set
	 */
	public void setTradeCount(long tradeCount) {
		this.tradeCount = tradeCount;
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
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}


	/**
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}


	/**
	 * @param lastVolume the lastVolume to set
	 */
	public void setLastVolume(double lastVolume) {
		this.lastVolume = lastVolume;
	}


	/**
	 * @param lastPrice the lastPrice to set
	 */
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}


	/**
	 * @param vwap the vwap to set
	 */
	public void setVwap(double vwap) {
		this.vwap = vwap;
	}


	/**
	 * @param tradeVolume the tradeVolume to set
	 */
	public void setTradeVolume(double tradeVolume) {
		this.tradeVolume = tradeVolume;
	}


	/**
	 * @param phase the phase to set
	 */
	public void setPhase(MarketPhase phase) {
		this.phase = phase;
	}
		
	

}
