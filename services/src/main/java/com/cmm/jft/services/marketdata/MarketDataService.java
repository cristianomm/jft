/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.cmm.jft.connector.marketdata.MarketDataConnector;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.vo.NewsVO;

import quickfix.FieldException;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.Logout;
import quickfix.fix44.SequenceReset;
import quickfix.fix50.MarketDataIncrementalRefresh;
import quickfix.fix50.MarketDataSnapshotFullRefresh;
import quickfix.fix50.News;
import quickfix.fix50.SecurityList;
import quickfix.fix50.SecurityListRequest;
import quickfix.fix50.SecurityStatus;

/**
 * <p><code>MarketDataService.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 4:42:53 PM
 * @updated 04-nov-2015 13:32:49
 */
public class MarketDataService {

	private boolean started;
	private boolean connected;
	private long lastHeartbeat;
	private MarketDataHandler marketDataHandler;
	private LinkedHashMap<String, Market> markets;
	
	private LinkedList<NewsVO> newsFeed;
	

	private static MarketDataService instance;

	private MarketDataService(){
		this.started = false;
		this.connected = false;
		this.newsFeed = new LinkedList<NewsVO>();
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



	public void start(){

		startConnection();
		requestSecurityList();
		
		started = true;
	}

	/**
	 * Cria a lista de securities e envia o request para o MDS 
	 */
	public void requestSecurityList() {
		
		SecurityListRequest request = new SecurityListRequest();
		
		
		
		MarketDataConnector.getInstance().joinInstrumentDefinition();
		MarketDataConnector.getInstance().send(request, MarketDataConnector.getInstance().getInstrumentDefinition());
		MarketDataConnector.getInstance().exitInstrumentDefinition();
		
	}
	
	
	
	private void consumeMessages(){

		while(started){
			try {
				Message m = marketDataHandler.getMessageQueue().take();
				switch(m.getString(35)){

				case MsgType.HEARTBEAT:
					consumeHeartbeat((Heartbeat) m);
					break;

				case MsgType.MARKET_DATA_INCREMENTAL_REFRESH:
					consumeMDIncrRefresh((MarketDataIncrementalRefresh) m);
					break;

				case MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH:
					consumeMDSnapshotFullRefresh((MarketDataSnapshotFullRefresh) m);
					break;

				case MsgType.NEWS:
					consumeNews((News) m);
					break;

				case MsgType.SECURITY_LIST:
					consumeSecurityList((SecurityList) m);
					break;

				case MsgType.SECURITY_STATUS:
					consumeSecurityStatus((SecurityStatus) m);
					break;

				case MsgType.SEQUENCE_RESET:
					consumeSequenceReset((SequenceReset) m);
					break;

				default:

				}

			} catch (InterruptedException | FieldNotFound e) {
				e.printStackTrace();
			}
		}
	}


	private void consumeHeartbeat(Heartbeat heartbeat) {
		long aux = System.currentTimeMillis();
		
		if(lastHeartbeat > 0) {
			//caso tenha passado o tempo, reset no book 
			if((aux - lastHeartbeat)>30000) {
				
			}
		}
		
	}

	private void consumeMDIncrRefresh(MarketDataIncrementalRefresh incrementalRefresh){
		
	}

	private void consumeMDSnapshotFullRefresh(MarketDataSnapshotFullRefresh snapshotFullRefresh){
		
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
			
			newsFeed.add(newsvo);
			
		}catch(FieldException | FieldNotFound e){
			e.printStackTrace();
		} 

	}

	private void consumeSecurityList(SecurityList securityList){
		
		try {
			SecurityService.getInstance().restartSecurityList();
		
			int totNumRelSym = securityList.getTotNoRelatedSym().getValue();
			
			while((totNumRelSym--) >0) {
				
				
			}
			
		} catch(FieldNotFound e) {
			
		}
		
	}

	private void consumeSecurityStatus(SecurityStatus securityStatus){

		//pra cada security que recebeu o status
		
		
		//ajusta o status da security em securityService
		
		
	}

	private void consumeSequenceReset(SequenceReset sequenceReset) {
		try {
			int newSeqNum = sequenceReset.getNewSeqNo().getValue();
			
			markets.forEach((k,m) -> m.resetMarketData(newSeqNum));
			
		}catch(FieldNotFound e) {
			
		}
		
	}


	/**
	 * Join incremental Stream
	 */
	private void startConnection(){
		
	}
	
	
	private void resetBooks() {
		
	}
	
	private void processSecurityList() {

	}

	private void processSecurityStatus(){

	}


}
