/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Level;
import org.openfast.template.MessageTemplate;
import org.openfast.template.loader.MessageTemplateLoader;
import org.openfast.template.loader.XMLMessageTemplateLoader;

import quickfix.DataDictionary;
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
    private boolean receivingSnapshots;

    private long lstSnapshotHbt;
    private long lstRecoveryHbt;
    private long lstNewsHbt;
    private long lstMarketDataHbt;
    private long lstInstrumentHbt;

    private int lstSnapshotMsgSeqNum;
    private int lstRecoveryMsgSeqNum;
    private int lstNewsMsgSeqNum;
    private int lstMarketDataMsgSeqNum;
    private int lstInstrumentMsgSeqNum;

    private MarketDataConnector connector;
    private List<Integer> subscrSecurities;
    private LinkedHashMap<Integer, Market> markets;

    private ArrayList<MDListener> listeners;
    
    private LinkedList<MDNews> newsFeed;
    private static MarketDataService instance;
    private ConcurrentLinkedQueue<MarketDataSnapshotFullRefresh> snapshots;


    public static void main(String[] args){
	try{
	    
	    String path = "D:\\Disco\\Estudo\\Bovespa\\Mensagens\\UMDF Sinal de Difusão\\UMDFPUMA2.0\\UMDF20_FIXFAST-051_052-2013-07-10\\Production\\";
	    
	    byte[] msgHeader = new byte[10];
	    InputStream fst = new FileInputStream(new File(path + "51_Inc_FAST.bin"));
	    boolean loop = true;
	    int offset = 0;
	    while(loop) {
		fst.read(msgHeader, 0, 10);
		offset += 10;
		long msgSeqNum = msgHeader[0] | msgHeader[1] | msgHeader[2] | msgHeader[3];
		int noChunks = msgHeader[4] | msgHeader[5];
		int currChunk = msgHeader[6] | msgHeader[7];;
		int msgLength = msgHeader[8] | msgHeader[9];;
		System.out.println(msgHeader);
	    }
	    
	    
	    
	    
	    //(\d+)+=([\w|.|,|:|\-|\s]+)
	    //"D:\\Disco\\Disco\\Estudo\\Bovespa\\Manuais e kits de desenvolvimento\\UMDF\\FIXFAST\\templates\\Production\\templates-PUMA.xml"
	    String templateURL = "ftp://ftp.bmf.com.br/FIXFAST/templates/Production/templates-PUMA.xml";
	    URL url = new URL(templateURL);
	    InputStream templateSource = url.openStream(); //new FileInputStream(templateURL);
	    MessageTemplateLoader templateLoader = new XMLMessageTemplateLoader();
	    MessageTemplate[] templates = templateLoader.load(templateSource);
	    
	    for (int i = 0; i < templates.length; i++) {
		//templates[i].
		//System.out.println(templates[i].getName() +" " + templates[i].getId() + " "+((Scalar)templates[i].getFieldById("35")).getDefaultValue());
	    }
	    
	    
	    //String path = "D:\\Disco\\Disco\\Estudo\\Bovespa\\Mensagens\\UMDF Sinal de Difusão\\UMDFPUMA2.0\\testMSG\\";
	    File snap = new File(path + "fix.051.snap.log");
	    DataDictionary dd = new DataDictionary("D:\\Disco\\Workspaces\\git\\jft\\engine\\src\\main\\resources\\DICT_FIX44.xml");
	    Scanner sc = new Scanner(snap);
	    while(sc.hasNextLine()) {
		String msg = sc.nextLine();
		System.out.println(Message.identifyType(msg));
		Message fixMsg = new Message();
		
		fixMsg.fromString(msg, dd, false);
		
		System.out.println(fixMsg.getGroupCount(268));
	    }
	    
	}
	catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private MarketDataService(){
	this.started = false;
	this.connected = false;
	this.receivingInstruments = false;

	this.lstMarketDataMsgSeqNum = -1;
	this.lstInstrumentMsgSeqNum = -1;
	this.lstNewsMsgSeqNum = -1;
	this.lstRecoveryMsgSeqNum = -1;
	this.lstSnapshotMsgSeqNum = -1;

	this.listeners = new ArrayList<>();
	this.newsFeed = new LinkedList<MDNews>();
	this.markets = new LinkedHashMap<>();
	this.subscrSecurities = new ArrayList<>();
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

    public void connect() {
	try {
	    MarketDataStarter mds = new MarketDataStarter();
	    boolean bol = mds.start();
	    mdSynchronizationProcedure();
	    System.out.println("start MD Conector: " + bol);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }


    /**
     * startup process for MD synchronization
     */
    private boolean mdSynchronizationProcedure() {

	boolean appReady = false;
	step = MDSynchronizationSteps.RECOVERY_STEP;
	connector.joinRecoveryStream();

	step = MDSynchronizationSteps. INCREMENTAL_STEP;
	connector.joinMarketDataStream();
	connector.joinNewsStream();

	step = MDSynchronizationSteps.INSTRUMENT_STEP;
	connector.joinInstrumentStream();
	consumeInstrumentStream();

	if(step == MDSynchronizationSteps.MARKETRECOVERY_STEP) {
	    connector.joinSnapshotStream();

	    while(step == MDSynchronizationSteps.MARKETRECOVERY_STEP){
		consumeSnapshotStream();
	    }

	    //constroi os books que devem receber atualizacao
	    for(int secid : subscrSecurities) {
		buildBook(secid);
	    }
	    //sincroniza as mensagens conforme o ultimo snapshot salvo e as atualizacoes incrementais
	    synchronizeBooks();

	    //processa as mensagens de noticia
	    while(!connector.getNewsStream().isEmpty()) {
		consumeNewsStream();
	    }

	    appReady = true;
	}

	return appReady;
    }




    private void synchronizeBooks() {
	try {
	    //remove as mensagens desnecessarias da fila de MD incremental
	    int lstMsgSeqNumProc = connector.getSnapshotStream().peek().getInt(369);
	    while(connector.getMarketDataStream().peek().getHeader().getInt(34) <= lstMsgSeqNumProc) {
		connector.getMarketDataStream().poll();
	    }
	    //processa as mensagens restantes
	    while(!connector.getMarketDataStream().isEmpty()) {
		consumeMarketDataStream();
	    }
	}
	catch(FieldNotFound e) {

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
	buildBook(securityID);
	synchronizeBook(securityID);
	ret = 0;
	
	return ret;
    }

    private void buildBook(int securityID) {
	try {
	    Security security = SecurityService.getInstance().findSecurity(securityID, '8', "BVMF");
	    Market mrkt = new Market(security);
	    markets.put(security.getSecurityID(), mrkt);
	    refreshSnapshot();
	    for(Message msg : snapshots) {
		MarketDataSnapshotFullRefresh snapshotFullRefresh = (MarketDataSnapshotFullRefresh) msg;
		if(snapshotFullRefresh.getInt(48) == security.getSecurityID()) {
		    List<Group> grps = snapshotFullRefresh.getGroups(268);
		    
		    markets.get(security.getSecurityID()).resetMarketData(
			    snapshotFullRefresh.getInt(369), snapshotFullRefresh.getInt(83));
		    for(Group g : grps){
			markets.get(security.getSecurityID()).addMDEntry(g);
		    }
		}
	    }
	}catch(FieldNotFound e) {
	    e.printStackTrace();
	}

    }

    public void synchronizeBook(int securityID) {
	try {
	    if(markets.containsKey(securityID)) {
		for(Message mdinc : connector.getRecoveryStream()) {
		    if(mdinc.getInt(48) == securityID) {
			processMDIncrRefresh((MarketDataIncrementalRefresh) mdinc);
			
			//processa as mensagens restantes
			while(!connector.getMarketDataStream().isEmpty()) {
			    consumeMarketDataStream();
			}

		    }
		}
	    }
	}catch(FieldNotFound e) {

	}
    }


    public void refreshSnapshot() {
	try {
	    int lstMsgPrc = snapshots.peek().getInt(369);

	    if((lstMarketDataMsgSeqNum - lstMsgPrc)>=50) {
		connector.joinSnapshotStream();
		consumeSnapshotStream();
		connector.exitSnapshotStream();
	    }

	}catch(FieldNotFound e) {
	    e.printStackTrace();
	}

    }


    /**
     * 
     */
    private void consumeInstrumentStream() {
	while(step == MDSynchronizationSteps.INSTRUMENT_STEP){
	    try {
		Message m = connector.getInstrumentStream().take();

		switch(m.getHeader().getString(35)){
		case MsgType.HEARTBEAT:
		    processHeartbeat((Heartbeat) m, StreamTypes.INSTRUMENT);
		    break;

		case MsgType.SEQUENCE_RESET:
		    processSequenceReset((SequenceReset) m, StreamTypes.INSTRUMENT);
		    break;

		case MsgType.SECURITY_LIST:
		    processSecurityList((SecurityList) m);
		    break;
		}

	    } catch (InterruptedException | FieldNotFound e) {
		e.printStackTrace();
		Logging.getInstance().log(getClass(), e, Level.ERROR);
	    }
	}
    }

    private void consumeSnapshotStream() {
	while(!connector.getSnapshotStream().isEmpty()) {
	    try {
		System.out.println("retrieving snapshot messages: " + step);
		Message m = connector.getSnapshotStream().take();
		switch(m.getHeader().getString(35)){

		case MsgType.HEARTBEAT:
		    processHeartbeat((Heartbeat) m, StreamTypes.SNAPSHOT);
		    break;

		case MsgType.SEQUENCE_RESET:
		    processSequenceReset((SequenceReset) m, StreamTypes.SNAPSHOT);
		    break;

		case MsgType.MARKET_DATA_SNAPSHOT_FULL_REFRESH:
		    processMDSnapshotFullRefresh((MarketDataSnapshotFullRefresh) m);
		    break;
		}

	    } catch (InterruptedException | FieldNotFound e) {
		e.printStackTrace();
	    }
	}
    }

    private void consumeMarketDataStream() {
	try {
	    if(step == MDSynchronizationSteps.MARKETRECOVERY_STEP || step == MDSynchronizationSteps.READY_STEP){
		System.out.println("retrieving incremental messages: " + step);
		Message m = connector.getMarketDataStream().take();
		switch(m.getHeader().getString(35)){

		case MsgType.HEARTBEAT:
		    processHeartbeat((Heartbeat) m, StreamTypes.MARKET_DATA);
		    break;

		case MsgType.SEQUENCE_RESET:
		    processSequenceReset((SequenceReset) m, StreamTypes.MARKET_DATA);
		    break;

		case MsgType.MARKET_DATA_INCREMENTAL_REFRESH:
		    processMDIncrRefresh((MarketDataIncrementalRefresh) m);
		    break;

		case MsgType.SECURITY_STATUS:
		    processSecurityStatus((SecurityStatus) m);
		    break;
		}	
	    }

	} catch (InterruptedException | FieldNotFound e) {
	    e.printStackTrace();
	}
    }

    private void consumeNewsStream() {
	try {
	    if(step == MDSynchronizationSteps.MARKETRECOVERY_STEP || step == MDSynchronizationSteps.READY_STEP){
		System.out.println("retrieving news messages: " + step);
		Message m = connector.getNewsStream().take();
		switch(m.getHeader().getString(35)){

		case MsgType.HEARTBEAT:
		    processHeartbeat((Heartbeat) m, StreamTypes.NEWS);
		    break;

		case MsgType.SEQUENCE_RESET:
		    processSequenceReset((SequenceReset) m, StreamTypes.NEWS);
		    break;

		case MsgType.NEWS:
		    processNews((News) m);
		    break;
		}	
	    }

	} catch (InterruptedException | FieldNotFound e) {
	    e.printStackTrace();
	}
    }

    private void consumeRecoveryStream() {
	try {
	    if(step == MDSynchronizationSteps.RECOVERY_STEP){
		System.out.println("retrieving recovery messages: " + step);
		Message m = connector.getRecoveryStream().take();
		switch(m.getString(35)){

		case MsgType.HEARTBEAT:
		    processHeartbeat((Heartbeat) m, StreamTypes.RECOVERY);
		    break;
		}
	    }

	} catch (InterruptedException | FieldNotFound e) {
	    e.printStackTrace();
	}

    }



    private void processHeartbeat(Heartbeat heartbeat, StreamTypes stream) {
	long aux = System.currentTimeMillis();

	if(stream == StreamTypes.INSTRUMENT) {
	    if((aux - lstInstrumentHbt)>30000) {

	    }
	    lstInstrumentHbt = aux;
	} else if(stream == StreamTypes.MARKET_DATA) {
	    if(lstMarketDataHbt > 0) {
		//caso tenha passado o tempo, reset no book 
		if((aux - lstMarketDataHbt)>30000) {
		    resetBooks(1,0);
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
     * Realiza o tratamento da atualizacao incremental. 
     * Cada mensagem corresponde a atualizacao de multiplos instrumentos.
     * @param incrRefresh
     */
    private void processMDIncrRefresh(MarketDataIncrementalRefresh incrRefresh){
	try {
	    //repeating groups
	    for(Group group : incrRefresh.getGroups(268)) {
		int securityID = group.getInt(48);
		
		
		
		if(markets.containsKey(securityID)) {
		    markets.get(securityID).addMDEntry(group);
		}
	    }

	}catch(FieldNotFound e) {
	    e.printStackTrace();
	}

    }

    /**
     * Realiza o tratamento do snapshot full refresh. O snapshot eh enviado por 
     * instrumento. Cada mensagem independente coresponde a atualizacao completa de um instrumento,
     * bem como a configuracao inicial do Book.
     * @param snapshotFullRefresh
     */
    private void processMDSnapshotFullRefresh(MarketDataSnapshotFullRefresh snapshotFullRefresh){
	try{
	    int msgSeqNum = snapshotFullRefresh.getHeader().getInt(34);
	    int totNumReports = snapshotFullRefresh.getInt(911);
	    if(msgSeqNum == 1) {
		snapshots.clear();
		receivingSnapshots = true;
	    }

	    if(msgSeqNum <= totNumReports && receivingSnapshots) {
		snapshots.offer(snapshotFullRefresh);
	    }

	    if(msgSeqNum == totNumReports && receivingSnapshots) {
		receivingSnapshots = false;
		connector.exitSnapshotStream();
	    }

	}catch(FieldNotFound e){
	    e.printStackTrace();
	}

    }

    private void processNews(News news){
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

    private void processSecurityList(SecurityList securityList){
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

    private void processSecurityStatus(SecurityStatus securityStatus){
	try {
	    //tratamento de status para securityGroup
	    if(securityStatus.isSetField(1151) &&
		    securityStatus.isSetField(207) && securityStatus.isSetField(625)) {
	    }
	    //tratamento de status para security
	    else if(securityStatus.isSetField(48) && securityStatus.isSetField(22) && 
		    securityStatus.isSetField(207) && securityStatus.isSetField(326) && 
		    securityStatus.isSetField(1174)) {
		int securityID = Integer.parseInt(securityStatus.getSecurityID().getValue());
		if(markets.containsKey(securityID)) {
		    markets.get(securityID).setPhase(
			    MarketPhase.getByValue(securityStatus.getSecurityTradingStatus().getValue()));
		}
	    }
	}catch(FieldNotFound e) {
	    e.printStackTrace();
	}

    }

    private void processSequenceReset(SequenceReset sequenceReset, StreamTypes stream) {
	try {
	    if(stream == StreamTypes.INSTRUMENT) {
		//instrument definition loop...
		lstInstrumentMsgSeqNum = -1;
		receivingInstruments = false;
		connector.getInstrumentStream().clear();
	    }
	    else if(stream == StreamTypes.SNAPSHOT) {
		//snapshot recovery is restarting...
		lstSnapshotMsgSeqNum = -1;
		receivingSnapshots = false;
		connector.getSnapshotStream().clear();
	    }
	    else if(stream == StreamTypes.MARKET_DATA) {
		//reset the incremental stream
		connector.getMarketDataStream().clear();
		int newSeqNum = sequenceReset.getNewSeqNo().getValue();
		lstMarketDataMsgSeqNum = newSeqNum;
		resetBooks(newSeqNum, 0);
	    }
	    else if(stream == StreamTypes.NEWS) {
		//reset the incremental stream
		connector.getNewsStream().clear();
		int newSeqNum = sequenceReset.getNewSeqNo().getValue();
		lstNewsMsgSeqNum = newSeqNum;
	    }

	}catch(FieldNotFound e) {
	    e.printStackTrace();
	}

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


    public void addListener(MDListener listener){
	listeners.add(listener);
    }
    
    private void resetBooks(int newSeqNum, int rptSec) {
	markets.forEach((k, m) -> m.resetMarketData(newSeqNum, rptSec));
    }

}