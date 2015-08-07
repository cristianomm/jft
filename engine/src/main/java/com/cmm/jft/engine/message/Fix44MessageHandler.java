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


public class Fix44MessageHandler implements MessageHandler {
	
	
	//private static Fix44MessageHandler instance;
		
	public Fix44MessageHandler(){
		//initialize(this);
		System.out.println(getClass() + " initialized.");
	}
	
	/**
	 * @return the instance
	 */
	/*public static Fix44MessageHandler getInstance() {
		if(instance == null){
			instance = new Fix44MessageHandler();
			instance.initialize(instance);
		}
		return instance;
	}*/
	
	
	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		System.out.println("MH: " + message);
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
	
	
}
