/**
 * 
 */
package com.cmm.jft.messaging.fix44;

import com.cmm.jft.messaging.MessageHandler;

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

/**
 * <p><code>Fix44ClientHandler.java</code></p>
 * @author Cristiano
 * @version 24 de ago de 2015 22:58:23
 *
 */
public abstract class Fix44ClientHandler {

	/**
	 * 
	 */
	public Fix44ClientHandler() {
		super();
	}

	public abstract void onMessage(BusinessMessageReject message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue; 

	public abstract void onMessage(ExecutionReport message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(OrderCancelReject message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(SecurityDefinition message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(QuoteRequest message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(QuoteStatusReport message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(Quote message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(QuoteCancel message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(QuoteRequestReject message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(PositionMaintenanceReport message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

	public abstract void onMessage(AllocationReport message, SessionID sessionID) throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue;

}