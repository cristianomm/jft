/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Level;

import quickfix.FieldException;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.MsgType;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.SequenceReset;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.News;
import quickfix.fix44.SecurityList;
import quickfix.fix44.SecurityStatus;

import com.cmm.jft.connector.enums.MDSynchronizationSteps;
import com.cmm.jft.connector.marketdata.MarketDataConnector;
import com.cmm.jft.connector.marketdata.MarketDataStarter;
import com.cmm.jft.marketdata.MDNews;
import com.cmm.jft.security.Security;
import com.cmm.jft.security.SecurityInfo;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.enums.MarketPhase;
import com.cmm.jft.trading.enums.StreamTypes;
import com.cmm.logging.Logging;

/**
 * <p><code>MarketDataService.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 4:42:53 PM
 * @updated 04-nov-2015 13:32:49
 */
public class MarketDataService {

    private MDSynchronizationSteps step;

    private boolean started;
    private boolean connected;
    private boolean receivingInstruments;

    private long lstSnapshotHbt;
    private long lstRecoveryHbt;
    private long lstNewsHbt;
    private long lstMarketDataHbt;
    private long lstInstrumentHbt;

    private int lstSnapshotMsgSecNum;
    private int lstRecoveryMsgSecNum;
    private int lstNewsMsgSecNum;
    private int lstMarketDataMsgSecNum;
    private int lstInstrumentMsgSecNum;

    private MarketDataConnector connector;
    private LinkedHashMap<Integer, Market> markets;

    private LinkedList<MDNews> newsFeed;
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
	this.receivingInstruments = false;

	this.lstMarketDataMsgSecNum = -1;
	this.lstInstrumentMsgSecNum = -1;
	this.lstNewsMsgSecNum = -1;
	this.lstRecoveryMsgSecNum = -1;
	this.lstSnapshotMsgSecNum = -1;

	this.newsFeed = new LinkedList<MDNews>();
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
	startConnector();
	mdSynchronizationProcedure();
	started = true;
    }

    private void startConnector(){
	try {
	    MarketDataStarter mds = new MarketDataStarter();
	    boolean bol = mds.start();
	    System.out.println("startConector: " + bol);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * startup process for MD synchronization
     */
    private void mdSynchronizationProcedure() {

	step = MDSynchronizationSteps.INSTRUMENT_STEP;

	System.out.println("mdSynchronizationProcedure: " + step);

	new Thread(new Runnable() {
	    @Override
	    public void run() {
//		try {
		    while(true) {
//			Thread.sleep(500);
			consumeInstrumentStream();
		    }
//		} catch (InterruptedException e) {
//		    e.printStackTrace();
//		}
	    }
	}).start();

	new Thread(new Runnable() {
	    @Override
	    public void run() {
		//try {
		    while(true) {
			//Thread.sleep(500);
			consumeSnapshotStream();
		    }
		//} catch (InterruptedException e) {
		    // TODO Auto-generated catch block
		    //e.printStackTrace();
		//}
	    }
	}).start();

	new Thread(new Runnable() {
	    @Override
	    public void run() {
//		try {
		    while(true) {
//			Thread.sleep(500);
			consumeMarketDataStream();
		    }
//		} catch (InterruptedException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
	    }
	}).start();

	new Thread(new Runnable() {
	    @Override
	    public void run() {
//		try {
		    while(true) {
//			Thread.sleep(500);
			consumeNewsStream();
		    }
//		} catch (InterruptedException e) {
//		    // TODO Auto-generated catch block
//		    e.printStackTrace();
//		}
	    }
	}).start();

    }


    private void consumeInstrumentStream() {
	while(started){
	    try {
		if(step == MDSynchronizationSteps.INSTRUMENT_STEP){
		    //Logging.getInstance().log(getClass(), "retrieving instrument messages: " + step, Level.INFO);
		    //System.out.println("retrieving instrument messages: " + step);
		    Message m = connector.getInstrumentStream().take();
		    
		    switch(m.getHeader().getString(35)){

		    case MsgType.HEARTBEAT:
			consumeHeartbeat((Heartbeat) m, StreamTypes.INSTRUMENT);
			break;

		    case MsgType.SEQUENCE_RESET:
			consumeSequenceReset((SequenceReset) m, StreamTypes.INSTRUMENT);
			break;

		    case MsgType.SECURITY_LIST:
			consumeSecurityList((SecurityList) m, StreamTypes.INSTRUMENT);
			break;
		    }
		}
		Thread.sleep(1000);

	    } catch (InterruptedException | FieldNotFound e) {
		e.printStackTrace();
		Logging.getInstance().log(getClass(), e, Level.ERROR);
	    }
	}
    }

    private void consumeSnapshotStream() {
	while(started){
	    try {
		if(step == MDSynchronizationSteps.MARKETRECOVERY_STEP){
		    System.out.println("retrieving recovery messages: " + step);
		    Message m = connector.getSnapshotStream().take();
		    switch(m.getHeader().getString(35)){

		    case MsgType.HEARTBEAT:
			consumeHeartbeat((Heartbeat) m, StreamTypes.SNAPSHOT);
			break;

		    case MsgType.SEQUENCE_RESET:
			consumeSequenceReset((SequenceReset) m, StreamTypes.SNAPSHOT);
			break;

		    case MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH:
			consumeMDSnapshotFullRefresh((MarketDataSnapshotFullRefresh) m);
			break;
		    }
		}
		Thread.sleep(1000);

	    } catch (InterruptedException | FieldNotFound e) {
		e.printStackTrace();
	    }
	}
    }

    private void consumeMarketDataStream() {
	while(started){
	    try {
		if(step == MDSynchronizationSteps.INCREMENTAL_STEP){
		    System.out.println("retrieving incremental messages: " + step);
		    Message m = connector.getMarketDataStream().take();
		    switch(m.getHeader().getString(35)){

		    case MsgType.HEARTBEAT:
			consumeHeartbeat((Heartbeat) m, StreamTypes.MARKET_DATA);
			break;

		    case MsgType.SEQUENCE_RESET:
			consumeSequenceReset((SequenceReset) m, StreamTypes.MARKET_DATA);
			break;

		    case MsgType.MARKET_DATA_INCREMENTAL_REFRESH:
			consumeMDIncrRefresh((MarketDataIncrementalRefresh) m);
			break;

		    case MsgType.SECURITY_LIST:
			consumeSecurityList((SecurityList) m, StreamTypes.MARKET_DATA);
			break;

		    case MsgType.SECURITY_STATUS:
			consumeSecurityStatus((SecurityStatus) m);
			break;
		    }	
		}
		Thread.sleep(1000);

	    } catch (InterruptedException | FieldNotFound e) {
		e.printStackTrace();
	    }

	}
    }

    private void consumeNewsStream() {
	while(started){
	    try {
		if(step == MDSynchronizationSteps.INCREMENTAL_STEP){
		    System.out.println("retrieving news messages: " + step);
		    Message m = connector.getNewsStream().take();
		    switch(m.getHeader().getString(35)){

		    case MsgType.HEARTBEAT:
			consumeHeartbeat((Heartbeat) m, StreamTypes.NEWS);
			break;

		    case MsgType.SEQUENCE_RESET:
			consumeSequenceReset((SequenceReset) m, StreamTypes.NEWS);
			break;

		    case MsgType.NEWS:
			consumeNews((News) m);
			break;
		    }	
		}
		Thread.sleep(1000);

	    } catch (InterruptedException | FieldNotFound e) {
		e.printStackTrace();
	    }

	}
    }

    private void consumeRecoveryStream() {
	while(started){
	    try {
		if(step == MDSynchronizationSteps.RECOVERY_STEP){
		    System.out.println("retrieving recovery messages: " + step);
		    Message m = connector.getRecoveryStream().take();
		    switch(m.getString(35)){

		    case MsgType.HEARTBEAT:
			consumeHeartbeat((Heartbeat) m, StreamTypes.RECOVERY);
			break;
		    }
		}
		Thread.sleep(1000);

	    } catch (InterruptedException | FieldNotFound e) {
		e.printStackTrace();
	    }
	}
    }



    private void consumeHeartbeat(Heartbeat heartbeat, StreamTypes stream) {
	long aux = System.currentTimeMillis();

	if(stream == StreamTypes.INSTRUMENT) {
	    if((aux - lstInstrumentHbt)>30000) {

	    }
	    lstInstrumentHbt = aux;
	} else if(stream == StreamTypes.MARKET_DATA) {
	    if(lstMarketDataHbt > 0) {
		//caso tenha passado o tempo, reset no book 
		if((aux - lstMarketDataHbt)>30000) {
		    resetBooks(-1);
		}
		lstMarketDataHbt = aux;
	    }
	} else if(stream == StreamTypes.NEWS) {
	    if((aux - lstNewsHbt)>30000) {

	    }
	    lstNewsHbt = aux;
	} else if(stream == StreamTypes.SNAPSHOT) {
	    if((aux - lstSnapshotHbt)>30000) {

	    }
	    lstSnapshotHbt = aux;
	} else if(stream == StreamTypes.RECOVERY) {
	    lstRecoveryHbt = aux;
	}

    }

    /**
     * Realiza o tratamento da atualizacao incremental. Cada mensagem corresponda a 
     * atualizacao de multiplos instrumentos.
     * @param incrRefresh
     */
    private void consumeMDIncrRefresh(MarketDataIncrementalRefresh incrRefresh){

	try {
	    //repeating groups
	    for(Group group : incrRefresh.getGroups(268)) {
		int securityID = group.getInt(48);
		char secIDSrc = group.getChar(22);
		String securityExchange = group.getString(207);
		//Security sec = SecurityService.getInstance().findSecurity(securityID, secIDSrc, securityExchange);

		markets.get(securityID).addMDEntry(group);
	    }

	}catch(FieldNotFound e) {
	    e.printStackTrace();
	}

    }

    /**
     * Realiza o tratamento do snapshot full refresh. O snapshot eh enviado por 
     * instrumento. Cada mensagem independente coresponde a atualizacao completa de um instrumento.
     * @param snapshotFullRefresh
     */
    private void consumeMDSnapshotFullRefresh(MarketDataSnapshotFullRefresh snapshotFullRefresh){
	try{
	    int msgSecNum = snapshotFullRefresh.getHeader().getInt(34);
	    if(msgSecNum == (lstSnapshotMsgSecNum+1)){
		lstSnapshotMsgSecNum = msgSecNum;
		int totNumReports = snapshotFullRefresh.getInt(911);

		int secID = snapshotFullRefresh.getInt(48);
		char secIDSrc = snapshotFullRefresh.getSecurityIDSource().getValue().charAt(0);
		String securityExchange = snapshotFullRefresh.getSecurityExchange().getValue();

		//Security sec = SecurityService.getInstance().findSecurity(secID, secIDSrc, securityExchange);
		//buscar o market por SecurityID
		List<Group> grps = snapshotFullRefresh.getGroups(268);
		for(Group g : grps){
		    markets.get(secID).addMDEntry(g);
		}

		//caso seja a ultima msg fecha a stream
		if(totNumReports == lstSnapshotMsgSecNum){
		    connector.exitSnapshotStream();
		    //avanca para o recebimento da stream incremental
		    step = MDSynchronizationSteps.INCREMENTAL_STEP;
		}
	    }
	    else if(msgSecNum == 1){
		lstSnapshotMsgSecNum = msgSecNum;
	    }

	}catch(FieldNotFound e){
	    e.printStackTrace();
	}

    }

    private void consumeNews(News news){
	try{
	    String text="";
	    int lines = news.getInt(33);
	    while(lines-- > 0){
		text += news.getString(58);
	    }

	    Date time = news.getOrigTime().getValue();
	    MDNews mdNews = new MDNews(
		    news.getString(1472), time, news.getString(6940),
		    news.getHeadline().getValue(), text
		    );

	    newsFeed.add(mdNews);

	}catch(FieldException | FieldNotFound e){
	    e.printStackTrace();
	} 

    }

    private void consumeSecurityList(SecurityList securityList, StreamTypes stream){
	try {
	    int mdMsgSeq = securityList.getHeader().getInt(34);
	    boolean lstFrag = securityList.getLastFragment().getValue();
	    
	    if(mdMsgSeq == 1) {
		receivingInstruments = true;
		SecurityService.getInstance().restartSecurityList();
	    }
	    
	    if(mdMsgSeq >= 1 && receivingInstruments){
		
		//verifica se recebeu todos instrumentos
		int totNumRelSym = securityList.getTotNoRelatedSym().getValue();
		if(totNumRelSym > SecurityService.getInstance().getSecurityList().size()) {

		    //quantidade de simbolos nesta msg
		    //int related = securityList.getNoRelatedSym().getValue();
		    List<Group> groups = securityList.getGroups(146); 
		    for(Group g : groups) {
			if(g.isSetField(55) && g.isSetField(107)) {
			    String symbol = g.getString(55);
			    Security sec = new Security(symbol);
			    sec.setDescription(g.getString(107));

			    sec.setSecurityID(g.getInt(48));
			    sec.setSecurityIDSrc(g.getChar(22));
			    sec.setSecurityExchange(g.getString(207));

			    SecurityInfo si = new SecurityInfo();
			    si.setTickSize(g.getDouble(969));
			    si.setMinVolume(g.getInt(9749));
			    si.setMaxVolume(g.getInt(9748));
			    si.setDigits(g.getInt(5151));
			    sec.setSecurityInfoID(si);

			    SecurityService.getInstance().loadSecurity(sec);
			}
		    }
		}
		//se ja recebeu todos instrumentos, sai do stream
		if(lstFrag && totNumRelSym == SecurityService.getInstance().getSecurityList().size()){
		    step = MDSynchronizationSteps.MARKETRECOVERY_STEP;
		    connector.exitInstrumentDefinition();
		    receivingInstruments = false;
		}
		
		System.out.println("SecurityList size: " + SecurityService.getInstance().getSecurityList().size());
	    }

	} catch(FieldNotFound e) {
	    e.printStackTrace();
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
		markets.get(symbol).setPhase(
			MarketPhase.getByValue(securityStatus.getSecurityTradingStatus().getValue()));
	    }
	}catch(FieldNotFound e) {
	    e.printStackTrace();
	}

    }

    private void consumeSequenceReset(SequenceReset sequenceReset, StreamTypes stream) {
	try {
	    if(stream == StreamTypes.INSTRUMENT) {
		//instrument definition loop...
		lstInstrumentMsgSecNum = -1;
		connector.getInstrumentStream().clear();
	    }
	    else if(stream == StreamTypes.SNAPSHOT) {
		//snapshot recovery is restarting...
		lstSnapshotMsgSecNum = -1;
		connector.getSnapshotStream().clear();
	    }
	    else if(stream == StreamTypes.MARKET_DATA) {
		//reset the incremental stream
		connector.getMarketDataStream().clear();
		int newSeqNum = sequenceReset.getNewSeqNo().getValue();
		lstMarketDataMsgSecNum = newSeqNum;
		resetBooks(newSeqNum);
	    }
	    else if(stream == StreamTypes.NEWS) {
		//reset the incremental stream
		connector.getNewsStream().clear();
		int newSeqNum = sequenceReset.getNewSeqNo().getValue();
		lstNewsMsgSecNum = newSeqNum;
	    }

	}catch(FieldNotFound e) {
	    e.printStackTrace();
	}

    }


    /**
     * Adiciona na tabela de instrumentos um instrumento para receber a 
     * atualizacao de market data.
     * @param securityID ID do instrumento
     * @param secIDSrc 
     * @param securityExchange 
     * @return <code>int</code> indicando o status, <code>-1</code> erro ao adicionar o instrumento, 
     * <code>0</code> caso adicione o instrumento sem erros. 
     */
    public int subscribeInstrument(int securityID, char secIDSrc, String securityExchange){

	int ret = -1;
	Security sec = SecurityService.getInstance().findSecurity(securityID, secIDSrc, securityExchange);

	if(sec != null){
	    Market mrkt = new Market(sec);
	    markets.put(securityID, mrkt);
	    ret = 0;
	}

	return ret;
    }

    /**
     * @return the newsFeed
     */
    public LinkedList<MDNews> getNewsFeed() {
	return newsFeed;
    }

    public Market getMarket(int securityID){
	return markets.get(securityID);
    }

    public Market hasMarketSymbol(String symbol) {

	for(Market m:markets.values()) {
	    if(symbol.equalsIgnoreCase(m.getSecurity().getSymbol())) {
		return m;
	    }
	}
	return null;

    }


    private void resetBooks(int newSeqNum) {
	markets.forEach((k, m) -> m.resetMarketData(newSeqNum));
    }

}