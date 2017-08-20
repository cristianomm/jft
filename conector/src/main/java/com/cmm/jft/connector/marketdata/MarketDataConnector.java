package com.cmm.jft.connector.marketdata;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.SequenceReset;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.News;
import quickfix.fix44.SecurityList;
import quickfix.fix44.SecurityStatus;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import com.cmm.jft.messaging.handlers.MarketDataMessageHandler;
import com.cmm.jft.trading.enums.StreamTypes;

/**
 * @author Cristiano M Martins
 * @version 30-10-2015 11:58:59
 * 
 */

public class MarketDataConnector extends MessageCracker implements Application, MarketDataMessageHandler {

    /**
     * Guarda o ultimo tempo de recebimento de mensagens. Caso o tempo entre
     * recebimento de mensagem seja maior que 30 segundos o estado do book
     * devera ser considerado inconsistente
     */
    private volatile long lastTimeMessage;

    private SessionID snapshotStreamSID;
    private SessionID recoveryStreamSID;
    private SessionID marketDataStreamSID;
    private SessionID instrumentStreamSID;
    private SessionID newsStreamSID;
    
    private LinkedBlockingQueue<Message> marketDataStream;
    private LinkedBlockingQueue<Message> instrumentStream;
    private LinkedBlockingQueue<Message> newsStream;
    private LinkedBlockingQueue<Message> snapshotStream;
    private LinkedBlockingQueue<Message> recoveryStream;

    private static MarketDataConnector instance;

    private MarketDataConnector() {
	this.marketDataStream = new LinkedBlockingQueue<Message>();
	this.instrumentStream = new LinkedBlockingQueue<Message>();
	this.newsStream = new LinkedBlockingQueue<Message>();
	this.snapshotStream = new LinkedBlockingQueue<Message>();
	this.recoveryStream = new LinkedBlockingQueue<Message>();
    }

    public synchronized static MarketDataConnector getInstance() {
	if (instance == null) {
	    instance = new MarketDataConnector();
	}
	return instance;
    }

    protected void addOnStream(Message message, SessionID sessionID) {
	
	//System.out.println(message.toString());
	
	if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.MARKET_DATA.name())) {
	    marketDataStream.add(message);
	} else if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.INSTRUMENT.name())) {
	    //System.out.println("Conector: " + message.toString());
	    instrumentStream.add(message);
	} else if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.NEWS.name())) {
	    //System.out.println("Conector: " + message.toString());
	    newsStream.add(message);
	} else if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.RECOVERY.name())) {
	    recoveryStream.add(message);
	} else if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.SNAPSHOT.name())) {
	    snapshotStream.add(message);
	}

    }

    public void joinInstrumentStream() {
	System.out.println("joinInstrumentStream-logon");
	Session.lookupSession(instrumentStreamSID).logon();
    }

    public void joinMarketDataStream() {
	Session.lookupSession(marketDataStreamSID).logon();
    }
    
    public void joinNewsStream() {
	Session.lookupSession(newsStreamSID).logon();
    }

    public void joinRecoveryStream() {
	Session.lookupSession(recoveryStreamSID).logon();
    }

    public void joinSnapshotStream() {
	Session.lookupSession(snapshotStreamSID).logon();
    }
    

    public void exitInstrumentDefinition() {
	Session.lookupSession(instrumentStreamSID).logout("user requested");
	instrumentStream.clear();
    }

    public void exitMarketDataStream() {
	Session.lookupSession(marketDataStreamSID).logout("user requested");
	marketDataStream.clear();
    }
    
    public void exitNewsStream() {
	Session.lookupSession(newsStreamSID).logout("user requested");
	newsStream.clear();
    }

    public void exitRecoveryStream() {
	Session.lookupSession(recoveryStreamSID).logout("user requested");
	recoveryStream.clear();
    }

    public void exitSnapshotStream() {
	Session.lookupSession(snapshotStreamSID).logout("user requested");
	snapshotStream.clear();
    }

    public long getLastTimeMessage() {
	return lastTimeMessage;
    }

    public SessionID getSnapshotStreamSID() {
	return snapshotStreamSID;
    }

    public SessionID getRecoveryStreamSID() {
	return recoveryStreamSID;
    }

    public SessionID getMarketDataStreamSID() {
	return marketDataStreamSID;
    }

    public SessionID getInstrumentStreamSID() {
	return instrumentStreamSID;
    }
    
    public SessionID getNewsStreamSID() {
	return newsStreamSID;
    }

    public LinkedBlockingQueue<Message> getMarketDataStream() {
	return marketDataStream;
    }

    public LinkedBlockingQueue<Message> getInstrumentStream() {
	return instrumentStream;
    }
    
    public LinkedBlockingQueue<Message> getNewsStream() {
	return newsStream;
    }

    public LinkedBlockingQueue<Message> getSnapshotStream() {
	return snapshotStream;
    }

    public LinkedBlockingQueue<Message> getRecoveryStream() {
	return recoveryStream;
    }

    public void setNewsStreamSID(SessionID newsStreamSID) {
	this.newsStreamSID = newsStreamSID;
    }
    
    public void setMarketDataStreamSID(SessionID marketDataStreamSID) {
	this.marketDataStreamSID = marketDataStreamSID;
    }

    public void setInstrumentStreamSID(SessionID instrumentStreamSID) {
	this.instrumentStreamSID = instrumentStreamSID;
    }

    public void setRecoveryStreamSID(SessionID recoveryStreamSID) {
	this.recoveryStreamSID = recoveryStreamSID;
    }

    public void setSnapshotStreamSID(SessionID snapshotStreamSID) {
	this.snapshotStreamSID = snapshotStreamSID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
     */
    @Override
    public void fromAdmin(Message message, SessionID sessionID)
	    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
     */
    @Override
    public void fromApp(Message message, SessionID sessionID)
	    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
	// System.out.println(message);
	// System.out.println(sessionID);
	 //System.out.println("fromApp: " + message);
	crack(message, sessionID);

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onCreate(quickfix.SessionID)
     */
    @Override
    public void onCreate(SessionID sessionID) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    @Override
    public void onLogon(SessionID sessionID) {
	// System.out.println(sessionID);
		
	if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.MARKET_DATA.name())) {
	    System.out.println("Incremental logon");
	} else if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.NEWS.name())) {
	    System.out.println("News logon");
	} else if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.INSTRUMENT.name())) {
	    System.out.println("Instrument logon");
	} else if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.RECOVERY.name())) {
	    System.out.println("Recovery logon");
	} else if (sessionID.getTargetCompID().equalsIgnoreCase(StreamTypes.SNAPSHOT.name())) {
	    System.out.println("Snapshot logon");
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    @Override
    public void onLogout(SessionID sessionID) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
     */
    @Override
    public void toAdmin(Message message, SessionID sessionID) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
     */
    @Override
    public void toApp(Message message, SessionID sessionID) throws DoNotSend {
	// TODO Auto-generated method stub

    }

    @Override
    public void onMessage(SequenceReset message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	addOnStream(message, sessionID);

    }

    @Override
    public void onMessage(Heartbeat message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	addOnStream(message, sessionID);

    }

    @Override
    public void onMessage(SecurityList message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	addOnStream(message, sessionID);

    }

    @Override
    public void onMessage(MarketDataIncrementalRefresh message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	addOnStream(message, sessionID);

    }

    @Override
    public void onMessage(MarketDataSnapshotFullRefresh message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	addOnStream(message, sessionID);

    }

    @Override
    public void onMessage(SecurityStatus message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	addOnStream(message, sessionID);

    }

    @Override
    public void onMessage(News message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	addOnStream(message, sessionID);

    }

}
