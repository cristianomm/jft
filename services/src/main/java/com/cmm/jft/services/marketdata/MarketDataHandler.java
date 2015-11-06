/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.Date;
import java.util.concurrent.LinkedBlockingQueue;

import com.cmm.jft.connector.message.ClientMarketDataMessageHandler;
import com.cmm.jft.vo.NewsVO;

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
 * @author cristiano
 * @version Nov 3, 2015 9:46:53 PM
 *
 */
public class MarketDataHandler extends ClientMarketDataMessageHandler {
	
	private LinkedBlockingQueue<Message> messages;
	
	
	public MarketDataHandler() {
		this.messages = new LinkedBlockingQueue<Message>();
	}
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix44.SequenceReset, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SequenceReset message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix44.Heartbeat, quickfix.SessionID)
	 */
	@Override
	public void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.SecurityList, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityList message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.MarketDataIncrementalRefresh, quickfix.SessionID)
	 */
	@Override
	public void onMessage(MarketDataIncrementalRefresh message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.MarketDataSnapshotFullRefresh, quickfix.SessionID)
	 */
	@Override
	public void onMessage(MarketDataSnapshotFullRefresh message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.SecurityStatus, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityStatus message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.News, quickfix.SessionID)
	 */
	@Override
	public void onMessage(News message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);
	}

}
