/**
 * 
 */
package com.cmm.jft.engine.marketdata.incrementals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

import org.apache.log4j.Level;

import com.cmm.jft.engine.IdGenerator;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.Stream;
import com.cmm.jft.engine.match.Summary;
import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.marketdata.MDSnapshot;
import com.cmm.jft.messaging.MarketDataMessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.MessageSender;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.MDEntryTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.StreamTypes;
import com.cmm.jft.trading.enums.UpdateActions;
import com.cmm.logging.Logging;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;
import quickfix.field.NoMDEntries;
import quickfix.field.SecurityExchange;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import sun.security.jca.GetInstance;

/**
 * <p>
 * <code>MarketDataStream.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Feb 26, 2016 3:57:55 PM
 *
 */
public class MarketDataStream extends Stream {


    private enum IncrementalStates{
	OPEN, CLOSED, PACKING
    }

    private int openCont;
    private int sequence;
//    private IdGenerator idGen;
//    private SecurityID securityID;
//    private SecurityIDSource securityIDSrc;
//    private SecurityExchange exchangeID;

    private IncrementalStates packState;

    private static String stream = "E";
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");

    private MarketDataMessageEncoder encoder;
    
    private HashMap<String, IdGenerator> generators;
    
    private LinkedBlockingQueue<MarketDataIncrementalRefresh.NoMDEntries> mbpPackets;
    private LinkedBlockingQueue<MarketDataIncrementalRefresh.NoMDEntries> mboPackets;

    private static MarketDataStream instance;
    
    private MarketDataStream() {
	encoder = Fix50SP2MDMessageEncoder.getInstance();
//	this.security = security;
//	securityID = new SecurityID(security.getSecurityID()+"");
//	securityIDSrc = new SecurityIDSource(security.getSecurityIDSrc()+"");
//	exchangeID = new SecurityExchange(security.getSecurityExchange());

	packState = IncrementalStates.OPEN;
	mbpPackets = new LinkedBlockingQueue<>(1000);
	mboPackets = new LinkedBlockingQueue<>(1000);

	generators = new HashMap<>();
	
	for(Security security : SecurityService.getInstance().getSecurityList()) {
	    generators.put(security.getSymbol(), new IdGenerator(security, new Date()));
	}
	
    }
    
    public static synchronized MarketDataStream getInstance() {
	if(instance == null) {
	    instance = new MarketDataStream();
	}
	return instance;
    }


    public void openNewPacket(){
	if(packState == IncrementalStates.CLOSED || packState == null){
	    packState = IncrementalStates.OPEN;
	    mbpPackets.clear();
	    mboPackets.clear();
	}
	openCont++;
    }

    public void packing(){
	if(packState ==IncrementalStates.OPEN || packState == IncrementalStates.PACKING){
	    packState = IncrementalStates.PACKING;
	}
    }

    /**
     * Close last open packet and send market data to listeners
     */
    public void closePacket(){
	if(packState == IncrementalStates.PACKING && openCont == 1){
	    packState = IncrementalStates.CLOSED;
	    Message msgMBO = encoder.mdIncrementalRefresh(mboPackets);
	    Message msgMBP = encoder.mdIncrementalRefresh(mbpPackets);
	    sendUMDF(msgMBO);
	    sendUMDF(msgMBP);
	}
	openCont--;
    }

    
    private int getRptSeq(String symbol) {
	if(generators.containsKey(symbol)) {
	    return generators.get(symbol).nextInt();
	}
	return -1;
    }

    public MDEntry createMBOEntry(Orders order, UpdateActions updtAction, int position){
	MDEntry mboEntry = new MDEntry();

	if(position >0) {
	    mboEntry.setOrderID(order.getOrderID());
	    if(order.getSide() == Side.BUY){
		mboEntry.setMdEntryBuyer(order.getBrokerID());
	    }else{
		mboEntry.setMdEntrySeller(order.getBrokerID());
	    }
	    mboEntry.setMdEntryDateTime(order.getInsertDateTime());
	    mboEntry.setMdEntryType(order.getSide() == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);

	    //para MD Inc refresh
	    if(updtAction != null) {
		mboEntry.setMdUpdateAction(updtAction);
	    }

	    mboEntry.setMdEntryPx(order.getPrice());
	    mboEntry.setMdEntrySize((int) order.getVolume());
	    mboEntry.setMdEntryPosNo(position);
	}
	return mboEntry;
    }

    public MDEntry createMBPEntry(UpdateActions updtAction, Summary summary, int position) {
	MDEntry mbpEntry = new MDEntry();
	if(summary != null) {

	}

	return mbpEntry;
    }


    //----------------------------------------BID OFFER packets
    /**
     * @param order
     */
    public void informNewOrder(Orders order, int posMBO, Summary summary, int posMBP) {
	if(posMBO >0) {
	    try {
		MarketDataIncrementalRefresh.NoMDEntries entry = null;
		if(order.getSide() == Side.BUY) {
		    mboPackets.put(encoder.bidEntryIncMBO(order, posMBO, UpdateActions.New, 
			    getRptSeq(order.getSecurityID().getSymbol())));
		    /*
		    mbpPackets.put(encoder.bidEntryIncMBP(
			    summary.getPrice(), summary.getOrderVolume(), summary.getOrderCount(), 
			    order.getSecurityID(), posMBO, UpdateActions.New, idGen.actualInt())
			    );*/
		}else {
		    mboPackets.put(encoder.offerEntryIncMBO(order, posMBO, UpdateActions.New, 
			    getRptSeq(order.getSecurityID().getSymbol())));
		    /*
		    mbpPackets.put(encoder.bidEntryIncMBP(
			    summary.getPrice(), summary.getOrderVolume(), summary.getOrderCount(), 
			    order.getSecurityID(), posMBO, UpdateActions.New, idGen.actualInt())
			    );*/
		}

	    }catch(InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }

    public void informChangeOrder(Orders order, int posMBO) {
	try {
	    if(order.getSide() == Side.BUY) {
		mboPackets.put(encoder.bidEntryIncMBO(order, posMBO, UpdateActions.Change, 
			getRptSeq(order.getSecurityID().getSymbol())));
		/*
	    mbpPackets.put(encoder.bidEntryIncMBP(
		    summary.getPrice(), summary.getOrderVolume(), summary.getOrderCount(), 
		    order.getSecurityID(), posMBO, UpdateActions.New, idGen.actualInt())
		    );*/
	    }else {
		mboPackets.put(encoder.offerEntryIncMBO(order, posMBO, UpdateActions.Change, 
			getRptSeq(order.getSecurityID().getSymbol())));
		/*
	    mbpPackets.put(encoder.bidEntryIncMBP(
		    summary.getPrice(), summary.getOrderVolume(), summary.getOrderCount(), 
		    order.getSecurityID(), posMBO, UpdateActions.New, idGen.actualInt())
		    );*/
	    }
	}catch(InterruptedException e) {

	}
    }


    public void informDeleteOrder(Orders order, int posMBO){
	try {
	    if(order.getSide() == Side.BUY) {
		mboPackets.put(encoder.bidEntryIncMBO(order, posMBO, UpdateActions.Delete, 
			getRptSeq(order.getSecurityID().getSymbol())));
		/*
	    mbpPackets.put(encoder.bidEntryIncMBP(
		    summary.getPrice(), summary.getOrderVolume(), summary.getOrderCount(), 
		    order.getSecurityID(), posMBO, UpdateActions.New, idGen.actualInt())
		    );*/
	    }else {
		mboPackets.put(encoder.offerEntryIncMBO(order, posMBO, UpdateActions.Delete, 
			getRptSeq(order.getSecurityID().getSymbol())));
		/*
	    mbpPackets.put(encoder.bidEntryIncMBP(
		    summary.getPrice(), summary.getOrderVolume(), summary.getOrderCount(), 
		    order.getSecurityID(), posMBO, UpdateActions.New, idGen.actualInt())
		    );*/
	    }
	}catch(InterruptedException e) {

	}
    }

    //---------------------------------------/BID OFFER packets




    //----------------------------------------/MarketData packets
    public void informTrade(MDEntry trade) {
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = encoder.tradeEntryInc(
		    trade.getMdUpdateAction(), 
		    SecurityService.getInstance().provideSecurity(trade.getSymbol()), 
		    trade.getMdEntryBuyer(), trade.getMdEntrySeller(), 
		    trade.getMdEntryPx(), trade.getMdEntrySize(), 
		    trade.getMdEntryDateTime(), trade.getTradeID(), 
		    trade.getTradeVolume(), getRptSeq(trade.getSymbol())
		    );
	    mboPackets.put(entry);
	    mbpPackets.put(entry);

	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

    }

    public void informIndexValue(){
	throw new NotImplementedException();
    }

    public void informOpenPrice(String symbol, double openPrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.openPriceEntryInc(UpdateActions.New, 
			    SecurityService.getInstance().provideSecurity(symbol), openPrice, 
			    getRptSeq(symbol));

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informClosePrice(String symbol, double closePrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.closePriceEntryInc(
			    SecurityService.getInstance().provideSecurity(symbol), 
			    closePrice, getRptSeq(symbol));

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informSettlementPrice(){
	throw new NotImplementedException();
    }

    public void informHighPrice(String symbol, double highPrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.highPriceEntryInc(
			    UpdateActions.New, 
			    SecurityService.getInstance().provideSecurity(symbol), 
			    highPrice, getRptSeq(symbol));

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informLowPrice(String symbol, double lowPrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.lowPriceEntryInc(
			    UpdateActions.New, 
			    SecurityService.getInstance().provideSecurity(symbol), 
			    lowPrice, getRptSeq(symbol));

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informVWAPPrice(String symbol, double vwapPrice){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.vwapPriceEntryInc(
			    UpdateActions.New, 
			    SecurityService.getInstance().provideSecurity(symbol), 
			    vwapPrice, getRptSeq(symbol));

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informImbalance(){
	throw new NotImplementedException();
    }

    public void informTradeVolume(String symbol, int numOfTrades, double financialVolume, double tradedVolume){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.tradeVolumeEntryInc(
			    SecurityService.getInstance().provideSecurity(symbol), 
			    numOfTrades, financialVolume, tradedVolume, getRptSeq(symbol));

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informOpenInterest(){
	throw new NotImplementedException();
    }

    public void informEmptyBook(String symbol){
	try{
	    MarketDataIncrementalRefresh.NoMDEntries entry = 
		    encoder.emptyBookEntryInc(
			    SecurityService.getInstance().provideSecurity(symbol), 
			    getRptSeq(symbol));

	    mboPackets.put(entry);
	    mbpPackets.put(entry);
	}catch(InterruptedException e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    public void informSecurityState(){
	throw new NotImplementedException();
    }

    public void informPriceBand(){
	throw new NotImplementedException();
    }

    public void informQuantityBand(){
	throw new NotImplementedException();
    }

    public void informCompositeUnderlingPrice(){
	throw new NotImplementedException();
    }

    /**
     * Mercado por Oferta, 
     * Esse tipo contem informacoes sobre cada uma das ofertas, com cada registro representando uma unica ordem. 
     * A BM&FBOVESPA sempre enviara a profundidade total para livro agregado por oferta, i.e. o cliente
     * recebera atualizacoes para todas as ofertas contidas no livro, mesmo que seja a ultima (pior preço).
     * Para livros mais “rasos” isso pode nao ser um problema, mas para livros mais profundos
     * atualizacoes completas nem sempre sao relevantes, alem de consumir banda.
     */
    public void mbo() {

    }

    /**
     * Mercado por Preco, 
     * O livro agregado por preço contem as informacoes individuais de preço, em que cada registro
     * representa a agregação de todas as ofertas (e suas quantidades) por aquele preço. 
     * Alem de quantidade e preço, o livro agregado por preço informa o numero de ofertas inseridas por
     * determinado preço. A BM&FBOVESPA preestabelece a profundidade do livro que estara disponivel 
     * por instrumento, com atribuicao automatica de 5. Isso significa que o cliente somente recebera os 5 
     * melhores niveis de preço no livro.
     */
    public void mbp() {

    }

    /**
     * Topo do Book. Apresenta as melhores ofertas de compra e venda
     * agrupadas por preco em somente um nivel
     * 
     */
    public void tob() {

    }

    /**
     * Informa ao mercado os eventos ocorridos
     */
    public void informMarket() {

	mbo();
	mbp();
	tob();

    }
       
    
    /* (non-Javadoc)
     * @see com.cmm.jft.engine.Stream#createSessionSettings()
     */
    @Override
    public void createSessionSettings() {
	try {
	    sessionSettings = new SessionSettings(Thread.currentThread().getContextClassLoader().getResourceAsStream("MarketDataService.cfg"));
	} catch (ConfigError e) {
	    logger.log(getClass(), e, Level.ERROR);
	}
    }
    
    /* (non-Javadoc)
     * @see com.cmm.jft.engine.Stream#start()
     */
    @Override
    public void start() {
        super.start();
    }
    
    /* (non-Javadoc)
     * @see java.lang.Runnable#run()
     */
    @Override
    public void run() {
        // TODO Auto-generated method stub
        
    }

    private void sendUMDF(Message message) {
	for (SessionID sid : SessionRepository.getInstance().getSessions(StreamTypes.MARKET_DATA).values()) {
	    sendMessage(message, sid);
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.messaging.MessageSender#sendMessage(quickfix.Message,
     * quickfix.SessionID)
     */
    public boolean sendMessage(Message message, SessionID sessionID) {
	return MessageRepository.getInstance().addMessage(message, sessionID);
    }
    
    /* (non-Javadoc)
     * @see quickfix.Application#onCreate(quickfix.SessionID)
     */
    public void onCreate(SessionID sessionId) {
	System.out.println("onCreate: MarketDataStream");
	
    }

    /* (non-Javadoc)
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    public void onLogon(SessionID sessionId) {
	System.out.println("onLogon:" + sessionId.getTargetCompID());
	Logging.getInstance().log(getClass(), "onLogon: " + sessionId.getTargetCompID(), Level.INFO);
	SessionRepository.getInstance().addSession(StreamTypes.MARKET_DATA, sessionId);
	
    }

    /* (non-Javadoc)
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    public void onLogout(SessionID sessionId) {
	Logging.getInstance().log(getClass(), "onLogout: " + sessionId.getTargetCompID(), Level.INFO);
	SessionRepository.getInstance().removeSession(StreamTypes.MARKET_DATA, sessionId);
	
    }

    /* (non-Javadoc)
     * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
     */
    public void toAdmin(Message message, SessionID sessionId) {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
     */
    public void fromAdmin(Message message, SessionID sessionId)
	    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
     */
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
	// TODO Auto-generated method stub
	
    }

    /* (non-Javadoc)
     * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
     */
    public void fromApp(Message message, SessionID sessionId)
	    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
	// TODO Auto-generated method stub
	
    }

}
