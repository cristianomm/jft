/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.Date;

import com.cmm.jft.connector.message.ClientMarketDataMessageHandler;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
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
	
	
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix44.SequenceReset, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SequenceReset message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix44.Heartbeat, quickfix.SessionID)
	 */
	@Override
	public void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.SecurityList, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityList message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.MarketDataIncrementalRefresh, quickfix.SessionID)
	 */
	@Override
	public void onMessage(MarketDataIncrementalRefresh message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.MarketDataSnapshotFullRefresh, quickfix.SessionID)
	 */
	@Override
	public void onMessage(MarketDataSnapshotFullRefresh message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.SecurityStatus, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityStatus message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientMarketDataMessageHandler#onMessage(quickfix.fix50.News, quickfix.SessionID)
	 */
	@Override
	public void onMessage(News message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		int lines = message.getNoLinesOfText().getValue();
		while(lines-- > 0){
			Date time = message.getOrigTime().getValue();
			String text = message.getString(58);
			
		}
		
	}

}
