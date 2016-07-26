package com.cmm.jft.connector.marketdata;

import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.SequenceReset;
import quickfix.mina.message.FIXMessageDecoder;
import quickfix.mina.message.FIXMessageEncoder;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.News;
import quickfix.fix44.SecurityList;
import quickfix.fix44.SecurityStatus;

import java.util.concurrent.LinkedBlockingQueue;

import com.cmm.jft.connector.enums.Streams;
import com.cmm.jft.connector.message.ClientMarketDataMessageHandler;


/**
 * @author Cristiano M Martins
 * @version 30-10-2015 11:58:59
 * 
 */

public class MarketDataConnector extends ClientMarketDataMessageHandler {

	
	private static MarketDataConnector instance;

	private MarketDataConnector() {
		this.incrementalStream = new LinkedBlockingQueue<Message>();
		this.instrumentStream = new LinkedBlockingQueue<Message>();
		this.snapshotStream = new LinkedBlockingQueue<Message>();
		this.recoveryStream = new LinkedBlockingQueue<Message>();
	}

	public synchronized static MarketDataConnector getInstance() {
		if(instance == null) {
			instance = new MarketDataConnector();
		}
		return instance;
	}
	
	protected void addOnStream(Message message, SessionID sessionID) {
		if(sessionID.getTargetCompID().equalsIgnoreCase(Streams.INCREMENTAL.name())){
			incrementalStream.add(message);
		}
		else if(sessionID.getTargetCompID().equalsIgnoreCase(Streams.INSTRUMENT.name())){
			instrumentStream.add(message);
		}
		else if(sessionID.getTargetCompID().equalsIgnoreCase(Streams.RECOVERY.name())){
			recoveryStream.add(message);
		}
		else if(sessionID.getTargetCompID().equalsIgnoreCase(Streams.SNAPSHOT.name())){
			snapshotStream.add(message);
		}
		
	}


	public void joinInstrumentDefinition() {
		Session.lookupSession(instrumentDefinitionSID).logon();
	}

	public void joinIncrementalStream() {
		Session.lookupSession(incrementalStreamSID).logon();
	}

	public void joinRecoveryStream() {
		Session.lookupSession(recoveryStreamSID).logon();
	}

	public void joinSnapshotStream() {
		Session.lookupSession(snapshotStreamSID).logon();
	}

	public void exitInstrumentDefinition() {
		Session.lookupSession(instrumentDefinitionSID).logout("user requested");
	}

	public void exitIncrementalStream() {
		Session.lookupSession(incrementalStreamSID).logout("user requested");
	}

	public void exitRecoveryStream() {
		Session.lookupSession(recoveryStreamSID).logout("user requested");
	}
	
	public void exitSnapshotStream() {
		Session.lookupSession(snapshotStreamSID).logout("user requested");
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromAdmin(Message message, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromApp(Message message, SessionID sessionID)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		//System.out.println(message);
		//System.out.println(sessionID);
		//System.out.println("fromApp: " + message);
		crack(message, sessionID);
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	@Override
	public void onCreate(SessionID sessionID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	@Override
	public void onLogon(SessionID sessionID) {
		System.out.println(sessionID);
		
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	@Override
	public void onLogout(SessionID sessionID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toAdmin(Message message, SessionID sessionID) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toApp(Message message, SessionID sessionID) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix44.SequenceReset, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SequenceReset message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		System.out.println("SequenceReset - " + message);
		addOnStream(message, sessionID);
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix44.Heartbeat, quickfix.SessionID)
	 */
	@Override
	public void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		System.out.println("heartbeat - " + message);
		addOnStream(message, sessionID);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50sp2.SecurityList, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityList message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		System.out.println("securityList - " + message);
		addOnStream(message, sessionID);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50sp2.MarketDataIncrementalRefresh, quickfix.SessionID)
	 */
	@Override
	public void onMessage(MarketDataIncrementalRefresh message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		System.out.println("MDIncrRefresh - " + message);
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50sp2.MarketDataSnapshotFullRefresh, quickfix.SessionID)
	 */
	@Override
	public void onMessage(MarketDataSnapshotFullRefresh message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50sp2.SecurityStatus, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityStatus message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50sp2.News, quickfix.SessionID)
	 */
	@Override
	public void onMessage(News message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
		
	}
	

}
