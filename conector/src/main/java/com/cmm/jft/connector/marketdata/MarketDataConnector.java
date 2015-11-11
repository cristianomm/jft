package com.cmm.jft.connector.marketdata;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix50.MessageCracker;


/**
 * @author Cristiano M Martins
 * @version 30-10-2015 11:58:59
 * 
 */

public class MarketDataConnector extends MessageCracker implements Application {
	
	
	private SessionID instrumentDefinition;
	private SessionID incrementalStream;
	private SessionID recoveryStream;
	
	
	/**
	 * @return the incrementalStream
	 */
	public SessionID getIncrementalStream() {
		return this.incrementalStream;
	}
	
	/**
	 * @return the instrumentDefinition
	 */
	public SessionID getInstrumentDefinition() {
		return this.instrumentDefinition;
	}
	
	/**
	 * @return the recoveryStream
	 */
	public SessionID getRecoveryStream() {
		return this.recoveryStream;
	}
	
	/**
	 * @param incrementalStream the incrementalStream to set
	 */
	public void setIncrementalStream(SessionID incrementalStream) {
		this.incrementalStream = incrementalStream;
	}
	
	/**
	 * @param instrumentDefinition the instrumentDefinition to set
	 */
	public void setInstrumentDefinition(SessionID instrumentDefinition) {
		this.instrumentDefinition = instrumentDefinition;
	}
	
	/**
	 * @param recoveryStream the recoveryStream to set
	 */
	public void setRecoveryStream(SessionID recoveryStream) {
		this.recoveryStream = recoveryStream;
	}
	
	
	@Override
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogon(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLogout(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		// TODO Auto-generated method stub
		
	}

}
