package com.cmm.jft.connector.message;

import java.util.concurrent.LinkedBlockingQueue;

import quickfix.Application;
import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.SequenceReset;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.News;
import quickfix.fix44.SecurityList;
import quickfix.fix44.SecurityStatus;


public abstract class ClientMarketDataMessageHandler extends MessageCracker implements Application {
	
	
	/**
	 * Guarda o ultimo tempo de recebimento de mensagens.
	 * Caso o tempo entre recebimento de mensagem seja 
	 * maior que 30 segundos o estado do book devera
	 * ser considerado inconsistente
	 */
	protected volatile long lastTimeMessage;
	
	protected SessionID snapshotStreamSID;
	protected SessionID recoveryStreamSID;
	protected SessionID incrementalStreamSID;
	protected SessionID instrumentDefinitionSID;
	
	protected LinkedBlockingQueue<Message> incrementalStream;
	protected LinkedBlockingQueue<Message> instrumentStream;
	protected LinkedBlockingQueue<Message> snapshotStream;
	protected LinkedBlockingQueue<Message> recoveryStream;
	
	
	/**
	 * @return the snapshotStreamSID
	 */
	public SessionID getSnapshotStreamSID() {
		return this.snapshotStreamSID;
	}


	/**
	 * @param snapshotStreamSID the snapshotStreamSID to set
	 */
	public void setSnapshotStreamSID(SessionID snapshotStreamSID) {
		this.snapshotStreamSID = snapshotStreamSID;
	}


	/**
	 * @return the recoveryStreamSID
	 */
	public SessionID getRecoveryStreamSID() {
		return this.recoveryStreamSID;
	}


	/**
	 * @param recoveryStreamSID the recoveryStreamSID to set
	 */
	public void setRecoveryStreamSID(SessionID recoveryStreamSID) {
		this.recoveryStreamSID = recoveryStreamSID;
	}


	/**
	 * @return the incrementalStreamSID
	 */
	public SessionID getIncrementalStreamSID() {
		return this.incrementalStreamSID;
	}


	/**
	 * @param incrementalStreamSID the incrementalStreamSID to set
	 */
	public void setIncrementalStreamSID(SessionID incrementalStreamSID) {
		this.incrementalStreamSID = incrementalStreamSID;
	}


	/**
	 * @return the instrumentDefinitionSID
	 */
	public SessionID getInstrumentDefinitionSID() {
		return this.instrumentDefinitionSID;
	}


	/**
	 * @param instrumentDefinitionSID the instrumentDefinitionSID to set
	 */
	public void setInstrumentDefinitionSID(SessionID instrumentDefinitionSID) {
		this.instrumentDefinitionSID = instrumentDefinitionSID;
	}


	/**
	 * @return the lastTimeMessage
	 */
	public long getLastTimeMessage() {
		return this.lastTimeMessage;
	}


	/**
	 * @return the incrementalStream
	 */
	public LinkedBlockingQueue<Message> getIncrementalStream() {
		return this.incrementalStream;
	}


	/**
	 * @return the instrumentStream
	 */
	public LinkedBlockingQueue<Message> getInstrumentStream() {
		return this.instrumentStream;
	}


	/**
	 * @return the snapshotStream
	 */
	public LinkedBlockingQueue<Message> getSnapshotStream() {
		return this.snapshotStream;
	}


	/**
	 * @return the recoveryStream
	 */
	public LinkedBlockingQueue<Message> getRecoveryStream() {
		return this.recoveryStream;
	}


	public abstract void onMessage(SequenceReset message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	
	public abstract void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	
	public abstract void onMessage(SecurityList message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	
	public abstract void onMessage(MarketDataIncrementalRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue;

	
	public abstract void onMessage(MarketDataSnapshotFullRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue;

	
	public abstract void onMessage(SecurityStatus message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	
	public abstract void onMessage(News message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

}
