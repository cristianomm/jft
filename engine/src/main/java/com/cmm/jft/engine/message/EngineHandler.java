package com.cmm.jft.engine.message;

import com.cmm.jft.engine.Book;
import com.cmm.jft.engine.BookRepository;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.entrypoint.EntryPoint;
import com.cmm.jft.messaging.MessageDecoder;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageDecoder;
import com.cmm.jft.messaging.handlers.EngineMessageHandler;
import com.cmm.jft.model.trading.Orders;

import quickfix.Application;
import quickfix.FieldNotFound;
import quickfix.IncorrectTagValue;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.Symbol;
import quickfix.fix44.AllocationInstruction;
import quickfix.fix44.MessageCracker;
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

public class EngineHandler implements EngineMessageHandler {

	private Fix44EngineMessageDecoder decoder;

	public EngineHandler() {
		// initialize(this);
		this.decoder = new Fix44EngineMessageDecoder();
		System.out.println(this.getClass() + " initialized.");
	}

	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		// System.out.println("MH: " + message);
		Book book = null;
		if ((book = BookRepository.getInstance().getBook(message.getString(Symbol.FIELD))) != null) {
			Orders order = decoder.newOrderSingle(message);
			SessionRepository.getInstance().addTraderSession(order.getTraderId(), sessionID);
			// try to add the order in the book
			book.addOrder(order);
		}

	}

	public void onMessage(OrderCancelReplaceRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		Book book = null;
		Orders ordr = MessageDecoder.getDecoder(sessionID).orderCancelReplaceRequest(message);
		if ((book = BookRepository.getInstance().getBook(message.getString(Symbol.FIELD))) != null) {

			book.replaceOrder(ordr);
		}

	}

	public void onMessage(OrderCancelRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		Book book = null;
		Orders ordr = MessageDecoder.getDecoder(sessionID).orderCancelRequest(message);
		if ((book = BookRepository.getInstance().getBook(message.getString(Symbol.FIELD))) != null) {
			book.cancelOrder(ordr);
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
