/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.sql.Time;
import java.time.Instant;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import quickfix.DataDictionary;
import quickfix.FieldException;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.SendingTime;
import quickfix.field.TradeDate;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.SequenceReset;
import quickfix.fix50.MarketDataIncrementalRefresh;
import quickfix.fix50.MarketDataSnapshotFullRefresh;
import quickfix.fix50.News;
import quickfix.fix50.SecurityList;
import quickfix.fix50.SecurityStatus;

import com.cmm.jft.connector.enums.Streams;
import com.cmm.jft.connector.marketdata.MarketDataConnector;
import com.cmm.jft.connector.marketdata.MarketDataStarter;
import com.cmm.jft.connector.message.ClientMarketDataMessageHandler;
import com.cmm.jft.messaging.LogFile;
import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.enums.MarketPhase;
import com.cmm.jft.vo.NewsVO;

/**
 * <p><code>MarketDataService.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 4:42:53 PM
 * @updated 04-nov-2015 13:32:49
 */
public class MarketDataService {


	private boolean started;
	private boolean connected;
	
	private long lstSnapshotHbt;
	private long lstRecoveryHbt;
	private long lstIncrementalHbt;
	private long lstInstrumentHbt;
	
	private MarketDataConnector connector;
	private LinkedHashMap<String, Market> markets;
	
	private LinkedList<NewsVO> newsFeed;
	private static MarketDataService instance;
	private ConcurrentLinkedQueue<Message> snapshots;
	
	
	public static void main(String[] args){
		try{
			
			//(\d+)+=([\w|.|,|:|\-|\s]+)
			//String msg = "8=FIX.4.49=43335=X34=249=TradingEngineDerivativesA52=20071106-19:12:48.06356=FIXGatewayDerivatives_MD10016=BMFBR9200444_175=20071106268=3279=1269=c278=155=COTZ0748=BMFBR920044422=8272=20071106273=19:12:19336=TradingSessionID326=101279=1269=b278=255=COTZ0748=BMFBR920044422=8272=20071106273=19:12:23336=TradingSessionID625=S279=0269=555=COTZ0748=BMFBR920044422=8270=0272=20071106273=19:12:38336=TradingSessionID10=161";
			//msg = "35=y1128=934=252=20140610132615107393=111893=N146=555=FESA448=380342622=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803426456=8455=BRFESAACNPR5456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=588800015=BRL120=BRL460=5167=PS762=10046937=FESA107=FERBASA     PN      N17595=58880000541=99991231200=999912231=1667=201404461=EPNEFR470=BR225=2014042963=D164=999912316938=99991231-22:59:59.0001300=8037011=N137010=461151=0755=FBMC348=380347522=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803475456=8455=BRFBMCACNOR3456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=2651615=BRL120=BRL460=5167=CS762=10036937=FBMC107=FIBAM       ON7595=265160541=99991231200=999912231=1667=201312461=ESVUFR470=BR225=2013122663=D164=999912316938=99991231-22:59:59.0001300=8037010=1381151=0155=FESA348=380353322=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803533456=8455=BRFESAACNOR8456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=294400015=BRL120=BRL460=5167=CS762=10036937=FESA107=FERBASA     ON      N17595=29440000541=99991231200=999912231=1667=201404461=ESVUFR470=BR225=2014042963=D164=999912316938=99991231-22:59:59.0001300=8037011=N137010=1501151=0155=FBMC448=380357422=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803574456=8455=BRFBMCACNPR0456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=4613515=BRL120=BRL460=5167=PS762=10046937=FBMC107=FIBAM       PN7595=461354541=99991231200=999912231=1667=201312461=EPNEFR470=BR225=2013122663=D164=999912316938=99991231-22:59:59.0001300=8037010=1501151=0155=GGBR348=380388922=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803889456=8455=BRGGBRACNOR1456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=5736274815=BRL120=BRL460=5167=CS762=10036937=GGBR107=GERDAU      ON      N17595=573627483541=99991231200=999912231=1667=201405461=ESVUFR470=BR225=2014052263=D164=999912316938=99991231-22:59:59.0001300=8037011=N137010=1541151=07";
			
			//LogFile lf = new LogFile(new DataDictionary("FIX44.xml"));
			//Message p = lf.parseTextMessage(msg);
			//lf.parseNewMessages();
			
			//Message m = (Message) lf.getMessages().get(4);// parseTextMessage(msg);
			
			//System.out.println(lf.getGroups(m).size());
			
			
			MarketDataService.getInstance().start();
			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}



	private MarketDataService(){
		this.started = false;
		this.connected = false;
		this.newsFeed = new LinkedList<NewsVO>();
		this.markets = new LinkedHashMap<>();
		this.connector = MarketDataConnector.getInstance();
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

		started = true;
	}

	/**
	 * Cria a lista de securities e envia o request para o MDS 
	 */
	/*
	public void requestSecurityList() {
		SecurityListRequest request = new SecurityListRequest();
		MarketDataConnector.getInstance().joinInstrumentDefinition();
		MarketDataConnector.getInstance().send(request, MarketDataConnector.getInstance().getInstrumentDefinition());
		MarketDataConnector.getInstance().exitInstrumentDefinition();
	}
	 */


	private void consumeInstrumentStream() {
		while(started){
			try {
				Message m = connector.getInstrumentStream().take();
				switch(m.getString(35)){

				case MsgType.HEARTBEAT:
					consumeHeartbeat((Heartbeat) m, Streams.INSTRUMENT);
					break;

				case MsgType.SEQUENCE_RESET:
					consumeSequenceReset((SequenceReset) m, Streams.INSTRUMENT);
					break;

				case MsgType.SECURITY_LIST:
					consumeSecurityList((SecurityList) m, Streams.INSTRUMENT);
					break;
				}

			} catch (InterruptedException | FieldNotFound e) {
				e.printStackTrace();
			}
		}
	}

	private void consumeSnapshotStream() {
		while(started){
			try {
				Message m = connector.getIncrementalStream().take();
				switch(m.getString(35)){

				case MsgType.HEARTBEAT:
					consumeHeartbeat((Heartbeat) m, Streams.SNAPSHOT);
					break;

				case MsgType.SEQUENCE_RESET:
					consumeSequenceReset((SequenceReset) m, Streams.SNAPSHOT);
					break;

				case MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH:
					consumeMDSnapshotFullRefresh((MarketDataSnapshotFullRefresh) m);
					break;
				}

			} catch (InterruptedException | FieldNotFound e) {
				e.printStackTrace();
			}
		}
	}

	private void consumeIncrementalStream() {
		while(started){
			try {
				Message m = connector.getIncrementalStream().take();
				switch(m.getString(35)){

				case MsgType.HEARTBEAT:
					consumeHeartbeat((Heartbeat) m, Streams.INCREMENTAL);
					break;

				case MsgType.SEQUENCE_RESET:
					consumeSequenceReset((SequenceReset) m, Streams.INCREMENTAL);
					break;

				case MsgType.MARKET_DATA_INCREMENTAL_REFRESH:
					consumeMDIncrRefresh((MarketDataIncrementalRefresh) m);
					break;

				case MsgType.NEWS:
					consumeNews((News) m);
					break;

				case MsgType.SECURITY_LIST:
					consumeSecurityList((SecurityList) m, Streams.INCREMENTAL);
					break;

				case MsgType.SECURITY_STATUS:
					consumeSecurityStatus((SecurityStatus) m);
					break;
				}

			} catch (InterruptedException | FieldNotFound e) {
				e.printStackTrace();
			}
		}
	}

	private void consumeRecoveryStream() {
		while(started){
			try {
				Message m = connector.getInstrumentStream().take();
				switch(m.getString(35)){

				case MsgType.HEARTBEAT:
					consumeHeartbeat((Heartbeat) m, Streams.RECOVERY);
					break;
				}

			} catch (InterruptedException | FieldNotFound e) {
				e.printStackTrace();
			}
		}
	}



	private void consumeHeartbeat(Heartbeat heartbeat, Streams stream) {
		long aux = System.currentTimeMillis();

		if(stream == Streams.INSTRUMENT) {
			if((aux - lstInstrumentHbt)>30000) {

			}
			lstInstrumentHbt = aux;
		}
		else if(stream == Streams.SNAPSHOT) {
			if((aux - lstSnapshotHbt)>30000) {

			}
			lstSnapshotHbt = aux;
		}
		else if(stream == Streams.INCREMENTAL) {
			if(lstIncrementalHbt > 0) {
				//caso tenha passado o tempo, reset no book 
				if((aux - lstIncrementalHbt)>30000) {
					resetBooks(-1);
				}
				lstIncrementalHbt = aux;
			}
		}
		else if(stream == Streams.RECOVERY) {
			lstRecoveryHbt = aux;
		}

	}

	private void consumeMDIncrRefresh(MarketDataIncrementalRefresh incrRefresh){

		try {
			incrRefresh.getInt(MsgSeqNum.FIELD);
			incrRefresh.getInt(SendingTime.FIELD);
			incrRefresh.getInt(TradeDate.FIELD);

			//repeating groups
			int gCount = 1;
			while(gCount < incrRefresh.getNoMDEntries().getValue())	{

				Group group = incrRefresh.getGroup(gCount++, 268);
				int securityID = group.getInt(48);
				char secIDSrc = group.getChar(22);
				String securityExchange = group.getString(207);
				Security sec = SecurityService.getInstance().findSecurity(securityID, secIDSrc, securityExchange);
					
				markets.get(sec.getSymbol()).addMDEntry(group);
			}

		}catch(FieldNotFound e) {
			e.printStackTrace();
		}

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

	private void consumeSecurityList(SecurityList securityList, Streams stream){
		try {
			int mdMsgSeq = securityList.getHeader().getInt(34);

			if(mdMsgSeq == 1){
				SecurityService.getInstance().restartSecurityList();
				//verifica se recebeu todos instrumentos
				int totNumRelSym = securityList.getTotNoRelatedSym().getValue();
				boolean lstFrag = securityList.getLastFragment().getValue();
				if(totNumRelSym > SecurityService.getInstance().getSecurityList().size() && !lstFrag) {
					
					//quantidade de simbolos nesta msg
					int related = securityList.getNoRelatedSym().getValue();
					List<Group> groups = securityList.getGroups(55); 
					for(Group g : groups) {
						if(g.isSetField(55) && g.isSetField(107)) {
							String symbol = g.getString(55);
							Security sec = new Security(symbol);
							sec.setDescription(g.getString(107));
							SecurityService.getInstance().loadSecurity(sec);
						}
					}
				}
				//se ja recebeu todos instrumentos, sai do stream
				else if(totNumRelSym == SecurityService.getInstance().getSecurityList().size()){
					MarketDataConnector.getInstance().exitInstrumentDefinition();
				}
			}

		} catch(FieldNotFound e) {
			
		}

	}

	private void consumeSecurityStatus(SecurityStatus securityStatus){
		try {
			//tratamento de status para securityGroup
			if(securityStatus.isSetField(1151) &&
				securityStatus.isSetField(207) && securityStatus.isSetField(625)) {
			}
			//tratamento de status para security
			else if(securityStatus.isSetField(48) && securityStatus.isSetField(22) && 
					securityStatus.isSetField(207) && securityStatus.isSetField(326) && 
					securityStatus.isSetField(1174)) {
				String symbol = securityStatus.getString(55);
				markets.get(symbol).setPhase(MarketPhase.getByValue(securityStatus.getInt(326)));
			}
		}catch(FieldNotFound e) {
			
		}

	}

	private void consumeSequenceReset(SequenceReset sequenceReset, Streams stream) {
		try {

			if(stream == Streams.INSTRUMENT) {
				//instrument definition loop...
				connector.getInstrumentStream().clear();
			}
			else if(stream == Streams.SNAPSHOT) {
				//snapshot recovery is restarting...
				connector.getSnapshotStream().clear();
			}
			else if(stream == Streams.INCREMENTAL) {
				//reset the incremental stream
				connector.getIncrementalStream().clear();
				int newSeqNum = sequenceReset.getNewSeqNo().getValue();
				resetBooks(newSeqNum);
			}

		}catch(FieldNotFound e) {

		}

	}


	/**
	 * Join incremental Stream
	 */
	private void startConnection(){
		MarketDataStarter mds = new MarketDataStarter();
		mds.start();
		connector.joinInstrumentDefinition();
	}


	private void resetBooks(int newSeqNum) {
		markets.forEach((k, m) -> m.resetMarketData(newSeqNum));
	}

	private void processSecurityList() {

	}

	private void processSecurityStatus(){

	}


}