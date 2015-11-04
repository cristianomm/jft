/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.LinkedHashMap;

import com.cmm.jft.trading.enums.MarketPhase;

/**
 * <p><code>MarketDataService.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 4:42:53 PM
 *
 */
public class MarketDataService {
	
	private LinkedHashMap<String, Market> markets;
	
	private static MarketDataService instance;
	
	private MarketDataService(){
		this.markets = new LinkedHashMap<>();
	}
	
	/**
	 * @return the instance
	 */
	public synchronized static MarketDataService getInstance() {
		if(instance == null){
			instance = new MarketDataService();
		}
		return instance;
	}
	
	public void start(){
		
		processInitMessages();
		processInstrumentTables();
		processBooks();
		processMessages();
		
	}
	
	/**
	 * Join incremental Stream
	 */
	private void processInitMessages(){
		
	}
	
	/**
	 * Join Instrument Definition Stream
	 */
	private void processInstrumentTables(){
		
	}
	
	/**
	 * 
	 */
	private void processBooks(){
		
	}
	
	/**
	 * Let app ready
	 */
	private void processMessages(){
		
	}
	
	
}
