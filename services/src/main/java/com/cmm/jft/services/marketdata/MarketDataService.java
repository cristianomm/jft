/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.LinkedBlockingQueue;

import quickfix.Message;

import com.cmm.jft.trading.enums.MarketPhase;
import com.cmm.jft.vo.NewsVO;

/**
 * <p><code>MarketDataService.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 4:42:53 PM
 * @updated 04-nov-2015 13:32:49
 */
public class MarketDataService {
	
	
	private LinkedHashMap<String, Market> markets;
	private MarketDataHandler marketDataHandler;
	
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
	
	
	private void dooo() {
		
		
		
		/*
		String text="";
		int lines = message.getNoLinesOfText().getValue();
		while(lines-- > 0){
			text += message.getString(58);
		}
		
		Date time = message.getOrigTime().getValue();
		NewsVO news = new NewsVO(
				message.getString(1472), time, 
				message.getHeadline().getValue(), text
				);*/
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
		
		//cria lista de instrumentos para pedir
		requestSecurityList();
		
		
		
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
	
	
	private void requestSecurityList() {
		
	}
	
	
}
