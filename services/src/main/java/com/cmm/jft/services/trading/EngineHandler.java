/**
 * 
 */
package com.cmm.jft.services.trading;

import java.util.concurrent.LinkedBlockingQueue;

import quickfix.Message;
import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.AllocationReport;
import quickfix.fix44.BusinessMessageReject;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.OrderCancelReject;
import quickfix.fix44.PositionMaintenanceReport;
import quickfix.fix44.Quote;
import quickfix.fix44.QuoteCancel;
import quickfix.fix44.QuoteRequest;
import quickfix.fix44.QuoteRequestReject;
import quickfix.fix44.QuoteStatusReport;
import quickfix.fix44.SecurityDefinition;

import com.cmm.jft.connector.message.ClientEngineMessageHandler;

/**
 * @author Cristiano M Martins
 * @version 2015-12-16
 *
 */
public class EngineHandler extends ClientEngineMessageHandler {

	
	public EngineHandler() {
		this.messages = new LinkedBlockingQueue<Message>();
	}
	
	
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.BusinessMessageReject, quickfix.SessionID)
	 */
	@Override
	public void onMessage(BusinessMessageReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.ExecutionReport, quickfix.SessionID)
	 */
	@Override
	public void onMessage(ExecutionReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.OrderCancelReject, quickfix.SessionID)
	 */
	@Override
	public void onMessage(OrderCancelReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.SecurityDefinition, quickfix.SessionID)
	 */
	@Override
	public void onMessage(SecurityDefinition message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.QuoteRequest, quickfix.SessionID)
	 */
	@Override
	public void onMessage(QuoteRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		messages.add(message);

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.QuoteStatusReport, quickfix.SessionID)
	 */
	@Override
	public void onMessage(QuoteStatusReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.Quote, quickfix.SessionID)
	 */
	@Override
	public void onMessage(Quote message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.QuoteCancel, quickfix.SessionID)
	 */
	@Override
	public void onMessage(QuoteCancel message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.QuoteRequestReject, quickfix.SessionID)
	 */
	@Override
	public void onMessage(QuoteRequestReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.PositionMaintenanceReport, quickfix.SessionID)
	 */
	@Override
	public void onMessage(PositionMaintenanceReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.connector.message.ClientEngineMessageHandler#onMessage(quickfix.fix44.AllocationReport, quickfix.SessionID)
	 */
	@Override
	public void onMessage(AllocationReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// TODO Auto-generated method stub

	}

}
