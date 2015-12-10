package com.cmm.jft.connector.marketdata;

import quickfix.Session;
import quickfix.SessionID;

import com.cmm.jft.connector.Connector;


/**
 * @author Cristiano M Martins
 * @version 30-10-2015 11:58:59
 * 
 */

public class MarketDataConnector extends Connector {

	private SessionID snapshotStream;
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
	 * 
	 * @return the snapshotStream
	 */
	public SessionID getSnapshotStream() {
		return snapshotStream;
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
	
	/**
	 * 
	 * @param snapshotStream
	 */
	public void setSnapshotStream(SessionID snapshotStream) {
		this.snapshotStream = snapshotStream;
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

	public void joinSnapshotStream() {
		Session.lookupSession(snapshotStream).logon();
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
	
	public void exitSnapshotStream() {
		Session.lookupSession(snapshotStream).logout("user requested");
	}
	

}
