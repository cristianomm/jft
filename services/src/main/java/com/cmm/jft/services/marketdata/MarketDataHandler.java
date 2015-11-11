/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.concurrent.LinkedBlockingQueue;

import com.cmm.jft.connector.message.ClientMarketDataMessageHandler;

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

/**
 * <p><code>MarketDataHandler.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 9:46:53 PM
 *
 */
public class MarketDataHandler extends ClientMarketDataMessageHandler {
	
	
	
	
	public MarketDataHandler() {
		this.messageQueue = new LinkedBlockingQueue<Message>();
	}
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix44.SequenceReset, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SequenceReset message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
		messageQueue.add(message);
		
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix44.Heartbeat, quickfix.SessionID)
	 */
	@Override
	public void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messageQueue.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.SecurityList, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityList message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messageQueue.add(message);
		
		//este metodo trata as mensagens provenientes do stream instrument definition
		//processa a mensagem, recuperando os instrumentos presentes na mensagem
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.MarketDataIncrementalRefresh, quickfix.SessionID)
	 */
	@Override
	public void onMessage(MarketDataIncrementalRefresh message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messageQueue.add(message);
		
		/*
		 * Este metodo eh responsavel pelo recebimento das mensagens que contem informacao sobre
		 * negociacao, book de ofertas
		 */
		
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.MarketDataSnapshotFullRefresh, quickfix.SessionID)
	 */
	@Override
	public void onMessage(MarketDataSnapshotFullRefresh message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messageQueue.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.SecurityStatus, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityStatus message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messageQueue.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.News, quickfix.SessionID)
	 */
	@Override
	public void onMessage(News message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messageQueue.add(message);
	}

}
