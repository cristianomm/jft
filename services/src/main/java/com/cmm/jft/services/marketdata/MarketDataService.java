/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import com.cmm.jft.connector.marketdata.MarketDataConnector;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.vo.NewsVO;

import quickfix.Field;
import quickfix.FieldException;
import quickfix.FieldNotFound;
import quickfix.FieldType;
import quickfix.FixVersions;
import quickfix.Group;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.SequenceReset;
import quickfix.fix50.MarketDataIncrementalRefresh;
import quickfix.fix50.MarketDataSnapshotFullRefresh;
import quickfix.fix50.News;
import quickfix.fix50.SecurityList;
import quickfix.fix50.SecurityStatus;
import quickfix.fix50.component.Instrument;
import quickfix.fix50.component.SecListGrp;

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
	
	
	public static void main(String[] args){
		try{
			String msg = "8=" + FixVersions.BEGINSTRING_FIX44 + "9=134335=X34=28649=TradingEngineDerivativesA52=20071106-19:31:55.50156=FIXGatewayDerivatives_MD10016=BMFBR5000244_8075=20071106268=10279=0269=2278=4355=WDLH0748=BMFBR500024422=8270=2089271=20272=20071106273=19:31:51274=0336=TradingSessionID288=BM000124289=BM000147451=1.000718562874256032=1279=2269=0278=4455=WDLH0748=BMFBR500024422=8272=20071106273=19:31:50336=TradingSessionID37=000001288=BM000124290=1279=0269=2278=4555=WDLH0748=BMFBR500024422=8270=2089271=7272=20071106273=19:31:52274=1336=TradingSessionID288=BM000002289=BM000147451=1.000718562874256032=2279=2269=0278=4655=WDLH0748=BMFBR500024422=8272=20071106273=19:31:51336=TradingSessionID37=000001288=BM000002290=1279=0269=455=WDLH0748=BMFBR500024422=8270=2089272=20071106273=19:31:55336=TradingSessionID279=0269=B55=WDLH0748=BMFBR500024422=8270=56403271=27272=20071106273=19:31:55336=TradingSessionID279=0269=755=WDLH0748=BMFBR500024422=8270=2089272=20071106273=19:31:55336=TradingSessionID279=0269=855=WDLH0748=BMFBR500024422=8270=2089272=20071106273=19:31:55336=TradingSessionID279=0269=955=WDLH0748=BMFBR500024422=8270=2089272=20071106273=19:31:55336=TradingSessionID279=0269=Z55=WDLH0748=BMFBR500024422=8270=2087.5271=20272=20071106273=19:31:55336=TradingSessionID10=201";
			msg = "35=y1128=934=252=20140610132615107393=111893=N146=555=FESA448=380342622=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803426456=8455=BRFESAACNPR5456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=588800015=BRL120=BRL460=5167=PS762=10046937=FESA107=FERBASA     PN      N17595=58880000541=99991231200=999912231=1667=201404461=EPNEFR470=BR225=2014042963=D164=999912316938=99991231-22:59:59.0001300=8037011=N137010=461151=0755=FBMC348=380347522=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803475456=8455=BRFBMCACNOR3456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=2651615=BRL120=BRL460=5167=CS762=10036937=FBMC107=FIBAM       ON7595=265160541=99991231200=999912231=1667=201312461=ESVUFR470=BR225=2013122663=D164=999912316938=99991231-22:59:59.0001300=8037010=1381151=0155=FESA348=380353322=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803533456=8455=BRFESAACNOR8456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=294400015=BRL120=BRL460=5167=CS762=10036937=FESA107=FERBASA     ON      N17595=29440000541=99991231200=999912231=1667=201404461=ESVUFR470=BR225=2014042963=D164=999912316938=99991231-22:59:59.0001300=8037011=N137010=1501151=0155=FBMC448=380357422=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803574456=8455=BRFBMCACNPR0456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=4613515=BRL120=BRL460=5167=PS762=10046937=FBMC107=FIBAM       PN7595=461354541=99991231200=999912231=1667=201312461=EPNEFR470=BR225=2013122663=D164=999912316938=99991231-22:59:59.0001300=8037010=1501151=0155=GGBR348=380388922=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3803889456=8455=BRGGBRACNOR1456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=5736274815=BRL120=BRL460=5167=CS762=10036937=GGBR107=GERDAU      ON      N17595=573627483541=99991231200=999912231=1667=201405461=ESVUFR470=BR225=2014052263=D164=999912316938=99991231-22:59:59.0001300=8037011=N137010=1541151=07";
			
			String[] fds = msg. split("\\S");
			
			
			Message m = new Message(msg);
			
			SecurityList sl = new SecurityList();
			sl.fromString(msg, null, true);
			Iterator<Field<?>> fields = m.iterator();
			while(fields.hasNext()){
				Field f = fields.next();
				
				if(f.getTag() == 55){
					System.out.println(f);
				}
				
			}
			
			System.out.println(sl);
			
		}catch(InvalidMessage e){
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	

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
//		requestSecurityList();

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
				resetBooks();
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

			int mdMsgSeq = securityList.getHeader().getInt(34);

			if(mdMsgSeq == 1){
				SecurityService.getInstance().restartSecurityList();
				//verifica se recebeu todos instrumentos
				int totNumRelSym = securityList.getTotNoRelatedSym().getValue();
				boolean lstFrag = securityList.getLastFragment().getValue();
				if(totNumRelSym > SecurityService.getInstance().getSecurityList().size() && !lstFrag) {
					
					
					//quantidade de simbolos nesta msg
					int related = securityList.getNoRelatedSym().getValue();
					while((related--) > 0){
						//securityList.SecListGrp().//
					}
					
				}
				//se ja recebeu todos instrumentos, sai do stream
				else if(totNumRelSym == SecurityService.getInstance().getSecurityList().size()){
					MarketDataConnector.getInstance().exitIncrementalStream();
				}
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
