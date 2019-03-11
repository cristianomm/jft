/**
 * 
 */
package com.cmm.jft.engine.entrypoint;

import java.time.LocalDateTime;
import java.util.HashMap;

import org.apache.log4j.Level;

import com.cmm.jft.engine.Book;
import com.cmm.jft.engine.BookRepository;
import com.cmm.jft.engine.ErrorCodes;
import com.cmm.jft.engine.IdGenerator;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.Stream;
import com.cmm.jft.messaging.MessageDecoder;
import com.cmm.jft.messaging.MessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.enums.FIXProtocols;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageDecoder;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.messaging.handlers.MessageHandler;
import com.cmm.jft.model.trading.OrderEvent;
import com.cmm.jft.model.trading.Orders;
import com.cmm.jft.model.trading.enums.ExecutionTypes;
import com.cmm.jft.model.trading.enums.StreamTypes;
import com.cmm.jft.model.trading.exceptions.OrderException;

import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionSettings;
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

/**
 * <p>
 * <code>EntryPoint.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 17/06/2015 17:00:55
 *
 */
public class EntryPoint extends Stream {

	private IdGenerator messageIDs;
	private Fix44EngineMessageDecoder decoder;
	private HashMap<FIXProtocols, MessageHandler> handlers;
	private HashMap<FIXProtocols, MessageEncoder> encoders;

	/**
	 * http://www.codeproject.com/Articles/757708/Mock-FIX-Trading-Server
	 * 
	 * @param settings
	 * @throws FieldConvertError
	 * @throws ConfigError
	 * 
	 */
	public EntryPoint() {
		this.decoder = new Fix44EngineMessageDecoder();
		this.messageIDs = new IdGenerator(LocalDateTime.now());
	}

	private boolean verifyLogon(SessionID sessionId) {

		boolean logon = false;
		// System.out.println("trying connection: " + sessionId.getTargetCompID());
		logon = true;

		return logon;
	}

	public void initEncoders(SessionSettings settings) {
		this.encoders = new HashMap<>();
		this.encoders.put(FIXProtocols.FIX44, Fix44EngineMessageEncoder.getInstance());
	}

	public void initHandlers(SessionSettings settings) {
		this.handlers = new HashMap<>();

		// MessageHandler handler = new EngineHandler();
		// initialize(handler);
		// this.handlers.put(FIXProtocols.FIX44, handler);
	}

	public void initRepositories() {
		MessageRepository.getInstance();
		SessionRepository.getInstance();
	}

	public void initOrderBooks(SessionSettings settings) {
		BookRepository.getInstance();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.engine.Stream#createSessionSettings()
	 */
	@Override
	public void createSessionSettings() {
		try {
			sessionSettings = new SessionSettings(
					Thread.currentThread().getContextClassLoader().getResourceAsStream("EntryPointService.cfg"));
		} catch (ConfigError e) {
			logger.log(getClass(), e, Level.ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.engine.Stream#start()
	 */
	@Override
	public void start() {
		super.start();
		initEncoders(sessionSettings);
		initHandlers(sessionSettings);
		initRepositories();
		started = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	@Override
	public void onCreate(SessionID sessionId) {
		System.out.println("onCreate: Entry Point ");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	@Override
	public void onLogon(SessionID sessionId) {
		if (verifyLogon(sessionId)) {
			System.out.println("onLogon:" + sessionId.getTargetCompID());
			logger.log(getClass(), "onLogon: " + sessionId.getTargetCompID(), Level.INFO);
			SessionRepository.getInstance().addSession(StreamTypes.ENTRYPOINT, sessionId);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	@Override
	public void onLogout(SessionID sessionId) {
		logger.log(getClass(), "onLogout: " + sessionId.getTargetCompID(), Level.INFO);
		SessionRepository.getInstance().removeSession(StreamTypes.ENTRYPOINT, sessionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub
		System.out.println("fromAdmin:" + message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub
		System.out.println("toApp:" + message);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// System.out.println("fromApp: " + message);
		crack(message, sessionId);

	}

	public void onMessage(NewOrderSingle message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		System.out.println("NewOrderSingle: " + message);
		Book book = null;
		Orders order = decoder.newOrderSingle(message);
		if ((book = BookRepository.getInstance().getBook(message.getString(Symbol.FIELD))) != null) {
			SessionRepository.getInstance().addTraderSession(order.getTraderId(), sessionID);
			// try to add the order in the book
			book.addOrder(order);
		} else {
			sendExecutionReport(order, ExecutionTypes.REJECTED, ErrorCodes.getInstance().getMessage(998013), 998013);
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

		throw new UnsupportedMessageType();
	}

	public void onMessage(SecurityDefinitionRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		throw new UnsupportedMessageType();
	}

	public void onMessage(QuoteRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		throw new UnsupportedMessageType();
	}

	public void onMessage(Quote message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		throw new UnsupportedMessageType();
	}

	public void onMessage(QuoteCancel message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		throw new UnsupportedMessageType();
	}

	public void onMessage(QuoteRequestReject message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		throw new UnsupportedMessageType();
	}

	public void onMessage(PositionMaintenanceRequest message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		throw new UnsupportedMessageType();
	}

	public void onMessage(AllocationInstruction message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {

		sendExecutionReport(null, ExecutionTypes.REJECTED, ErrorCodes.getInstance().getMessage(3013), 3013);
		throw new UnsupportedMessageType();
	}

	private void sendExecutionReport(Orders order, ExecutionTypes exec, String message, int ordRejReason) {

		try {
			OrderEvent oe = new OrderEvent(exec, LocalDateTime.now(), order.getVolume(), order.getPrice());
			oe.setOrderEventId(messageIDs.nextLong());
			oe.setOrderId(order);
			oe.setMessage(message);
			oe.setOrdRejReason(ordRejReason);
			order.addExecution(oe);

			SessionID sessionID = SessionRepository.getInstance().getTraderSession(order.getTraderId());
			sendMessage(((Fix44EngineMessageEncoder) MessageEncoder.getEncoder(sessionID)).executionReport(oe),
					sessionID);
		} catch (OrderException e) {
			e.printStackTrace();
			logger.log(this.getClass(), e, Level.ERROR);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.engine.message.MessageSender#sendMessage(quickfix.Message,
	 * quickfix.SessionID)
	 */
	public boolean sendMessage(Message message, SessionID sessionID) {
		return MessageRepository.getInstance().addMessage(message, sessionID);
	}

}
