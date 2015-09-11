package com.cmm.jft.engine.message;


import com.cmm.jft.engine.Book;
import com.cmm.jft.engine.BookRepository;
import com.cmm.jft.marketdata.MarketOrder;
import com.cmm.jft.messaging.MessageDecoder;
import com.cmm.jft.messaging.MessageHandler;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;

import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.Symbol;
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


public class Fix44EngineHandler implements MessageHandler {
	
	
	//private static Fix44MessageHandler instance;
		
	public Fix44EngineHandler(){
		//initialize(this);
		System.out.println(getClass() + " initialized.");
	}
	
	
	
	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		//System.out.println("MH: " + message);
		
		Book book = null;
		
		if((book = BookRepository.getInstance().getBook(message.getString(Symbol.FIELD))) != null){
			Orders order = new Orders();
			
			//try to add the order in the book
			book.addOrder(order, sessionID);
		}
		
	}
	
	
	public void onMessage(OrderCancelReplaceRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
		Book book = null;
		Orders ordr = MessageDecoder.getDecoder(sessionID).orderCancelReplaceRequest(message);
		if((book = BookRepository.getInstance().getBook(message.getString(Symbol.FIELD))) != null){
			book.replaceOrder(ordr);
		}
		
	}
	
	public void onMessage(OrderCancelRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		
		Book book = null;
		Orders ordr = MessageDecoder.getDecoder(sessionID).orderCancelRequest(message);
		if((book = BookRepository.getInstance().getBook(message.getString(Symbol.FIELD))) != null){
			book.replaceOrder(ordr);
		}
		
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
