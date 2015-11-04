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
	
	

}
