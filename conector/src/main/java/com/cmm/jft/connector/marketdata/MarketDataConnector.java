package com.cmm.jft.connector.marketdata;

import com.cmm.jft.connector.Connector;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.Logout;
import quickfix.fix50.MessageCracker;


/**
 * @author Cristiano M Martins
 * @version 30-10-2015 11:58:59
 * 
 */

public class MarketDataConnector extends Connector {


	private SessionID recoveryStream;
	private SessionID incrementalStream;
	private SessionID instrumentDefinition;

	private static MarketDataConnector instance;

	private MarketDataConnector() {

	}

	public synchronized static MarketDataConnector getInstance() {
		if(instance == null) {
			instance = new MarketDataConnector();
		}
		return instance;
	}



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



	public void joinInstrumentDefinition() {
		Session.lookupSession(instrumentDefinition).logon();
	}

	public void joinIncrementalStream() {
		Session.lookupSession(incrementalStream).logon();
	}

	public void joinRecoveryStream() {
		Session.lookupSession(recoveryStream).logon();
	}


	public void exitInstrumentDefinition() {
		Session.lookupSession(instrumentDefinition).logout("user requested");
	}

	public void exitIncrementalStream() {
		Session.lookupSession(incrementalStream).logout("user requested");
	}

	public void exitRecoveryStream() {
		Session.lookupSession(recoveryStream).logout("user requested");
	}

}
