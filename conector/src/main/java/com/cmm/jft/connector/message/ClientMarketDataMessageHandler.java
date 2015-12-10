package com.cmm.jft.connector.message;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.SequenceReset;
import quickfix.fix50.MarketDataIncrementalRefresh;
import quickfix.fix50.MarketDataSnapshotFullRefresh;
import quickfix.fix50.News;
import quickfix.fix50.SecurityList;
import quickfix.fix50.SecurityStatus;

import java.util.concurrent.LinkedBlockingQueue;

import com.cmm.jft.connector.enums.Streams;
import com.cmm.jft.messaging.MarketDataMessageHandler;

public abstract class ClientMarketDataMessageHandler implements MarketDataMessageHandler {
	
	
	/**
	 * Guarda o ultimo tempo de recebimento de mensagens.
	 * Caso o tempo entre recebimento de mensagem seja 
	 * maior que 30 segundos o estado do book devera
	 * ser considerado inconsistente
	 */
	protected volatile long lastTimeMessage;
	
	protected LinkedBlockingQueue<Message> instrumentStream;
	protected LinkedBlockingQueue<Message> incrementalStream;
	protected LinkedBlockingQueue<Message> snapshotStream;
	protected LinkedBlockingQueue<Message> recoveryStream;
	
	
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
	
	
	public LinkedBlockingQueue<Message> getIncrementalStream() {
		return incrementalStream;
	}
	
	public LinkedBlockingQueue<Message> getInstrumentStream() {
		return instrumentStream;
	}
	
	public LinkedBlockingQueue<Message> getRecoveryStream() {
		return recoveryStream;
	}
	
	public LinkedBlockingQueue<Message> getSnapshotStream() {
		return snapshotStream;
	}
	
	
	@Override
	public abstract void onMessage(SequenceReset message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	@Override
	public abstract void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	@Override
	public abstract void onMessage(SecurityList message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	@Override
	public abstract void onMessage(MarketDataIncrementalRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue;

	@Override
	public abstract void onMessage(MarketDataSnapshotFullRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue;

	@Override
	public abstract void onMessage(SecurityStatus message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	@Override
	public abstract void onMessage(News message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

}
