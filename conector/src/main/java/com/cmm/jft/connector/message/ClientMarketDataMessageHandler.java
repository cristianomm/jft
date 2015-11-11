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

import com.cmm.jft.messaging.MarketDataMessageHandler;

public abstract class ClientMarketDataMessageHandler implements MarketDataMessageHandler {
	
	
	/**
	 * Guarda o ultimo tempo de recebimento de mensagens.
	 * Caso o tempo entre recebimento de mensagem seja 
	 * maior que 30 segundos o estado do book devera
	 * ser considerado inconsistente
	 */
	protected volatile long lastTimeMessage;
	
	protected LinkedBlockingQueue<Message> messageQueue;
	
	
	/**
	 * @return the messageQueue
	 */
	public LinkedBlockingQueue<Message> getMessageQueue() {
		return this.messageQueue;
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
