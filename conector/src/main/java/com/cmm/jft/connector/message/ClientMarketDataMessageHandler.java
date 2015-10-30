package com.cmm.jft.connector.message;

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

import com.cmm.jft.messaging.MarketDataMessageHandler;

public class ClientMarketDataMessageHandler implements MarketDataMessageHandler {

	@Override
	public void onMessage(SequenceReset message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(Heartbeat message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(SecurityList message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(MarketDataIncrementalRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(MarketDataSnapshotFullRefresh message,
			SessionID sessionID) throws FieldNotFound, UnsupportedMessageType,
			IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(SecurityStatus message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	@Override
	public void onMessage(News message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

}
