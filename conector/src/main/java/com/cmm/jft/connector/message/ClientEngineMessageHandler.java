package com.cmm.jft.connector.message;


import java.util.concurrent.LinkedBlockingQueue;

import com.cmm.jft.messaging.EngineMessageHandler;
import com.cmm.jft.messaging.MessageDecoder;
import com.cmm.jft.trading.OrderEvent;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.Message;
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


public abstract class ClientEngineMessageHandler extends EngineMessageHandler {
	

	protected LinkedBlockingQueue<Message> messages;
	
	
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.BusinessMessageReject, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(BusinessMessageReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.ExecutionReport, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(ExecutionReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.OrderCancelReject, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(OrderCancelReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.SecurityDefinition, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(SecurityDefinition message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.QuoteRequest, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(QuoteRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.QuoteStatusReport, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(QuoteStatusReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.Quote, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(Quote message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.QuoteCancel, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(QuoteCancel message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.QuoteRequestReject, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(QuoteRequestReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.PositionMaintenanceReport, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(PositionMaintenanceReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.fix44.Fix44MessageHandler#onMessage(quickfix.fix44.AllocationReport, quickfix.SessionID)
	 */
	@Override
	public abstract void onMessage(AllocationReport message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;
	
	
}
