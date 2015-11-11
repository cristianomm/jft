/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.Date;
import java.util.LinkedHashMap;

import com.cmm.jft.vo.NewsVO;

import quickfix.FieldException;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.fix50.MarketDataIncrementalRefresh;
import quickfix.fix50.MarketDataSnapshotFullRefresh;
import quickfix.fix50.News;
import quickfix.fix50.SecurityList;
import quickfix.fix50.SecurityStatus;

/**
 * <p><code>MarketDataService.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 4:42:53 PM
 * @updated 04-nov-2015 13:32:49
 */
public class MarketDataService {
	
	private boolean started;
	private LinkedHashMap<String, Market> markets;
	private MarketDataHandler marketDataHandler;
	
	private static MarketDataService instance;
	
	private MarketDataService(){
		this.markets = new LinkedHashMap<>();
		this.marketDataHandler = new MarketDataHandler();
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
	
	
	
	public void consumeMessages(){
		
		while(started){
			try {
				Message m = marketDataHandler.getMessageQueue().take();
				switch(m.getString(35)){
				case MsgType.SECURITY_LIST:
					consumeSecurityList((SecurityList) m);
					break;
					
				case MsgType.MARKET_DATA_INCREMENTAL_REFRESH:
					consumeMDIncrRefresh((MarketDataIncrementalRefresh) m);
					break;
					
				case MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH:
					consumeMDSnapshotFullRefresh((MarketDataSnapshotFullRefresh) m);
					break;
					
				case MsgType.SECURITY_STATUS:
					consumeSecurityStatus((SecurityStatus) m);
					break;
					
				case MsgType.NEWS:
					consumeNews((News) m);
					break;
					
				default:
					
				}
				
			} catch (InterruptedException | FieldNotFound e) {
				e.printStackTrace();
			}
		}
	}
	
	
	private void consumeSecurityList(SecurityList securityList){
		
	}
	
	private void consumeMDIncrRefresh(MarketDataIncrementalRefresh incrementalRefresh){
		
	}

	private void consumeMDSnapshotFullRefresh(MarketDataSnapshotFullRefresh snapshotFullRefresh){
		
	}

	private void consumeSecurityStatus(SecurityStatus securityStatus){
		
		//pra cada security que recebeu o status
		
		//ajusta o status da security em securityService
		
	}

	private void consumeNews(News news){
		try{
		String text="";
		int lines = news.getNoLinesOfText().getValue();
		while(lines-- > 0){
			text += news.getString(58);
		}
		
		Date time = news.getOrigTime().getValue();
		NewsVO newsvo = new NewsVO(
				news.getString(1472), time, 
				news.getHeadline().getValue(), text
				);
		}catch(FieldException | FieldNotFound e){
			e.printStackTrace();
		} 
		
	}
	
	public void start(){
		
		processInitMessages();
		processInstrumentTables();
		processBooks();
		processMessages();
		started = true;
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
	
	private void processSecurityStatus(){
		
	}
	
	
}
