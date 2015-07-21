package com.cmm.jft.engine.message;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.MessageCracker;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.AllocationInstruction;
import quickfix.fix44.NewOrderCross;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReplaceRequest;
import quickfix.fix44.OrderCancelRequest;
import quickfix.fix44.PositionMaintenanceRequest;
import quickfix.fix44.Quote;
import quickfix.fix44.QuoteCancel;
import quickfix.fix44.QuoteRequest;
import quickfix.fix44.QuoteRequestReject;
import quickfix.fix44.SecurityDefinitionRequest;
import quickfix.fix50sp1.ApplicationMessageRequest;


public class Fix44MessageHandler extends MessageCracker {
	
	
	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(OrderCancelReplaceRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(OrderCancelRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(NewOrderCross message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(SecurityDefinitionRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(QuoteRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(Quote message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(QuoteCancel message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(QuoteRequestReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(PositionMaintenanceRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(AllocationInstruction message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	public void onMessage(ApplicationMessageRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
	}
	
	
}
