/**
 * 
 */
package com.cmm.jft.engine;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.log4j.Level;

import com.cmm.jft.engine.enums.MatchTypes;
import com.cmm.jft.engine.marketdata.UMDF;
import com.cmm.jft.engine.match.OrderMatcher;
import com.cmm.jft.messaging.MessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.MessageSender;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.WorkingIndicator;
import com.cmm.jft.trading.exceptions.OrderException;
import com.cmm.logging.Logging;

import quickfix.Message;
import quickfix.SessionID;

/**
 * <p><code>Book.java</code></p>
 * @author Cristiano M Martins
 * @version 26 de jul de 2015 02:23:19
 *
 */
public class Book implements MessageSender {
	
	
	private int orderCount;
	private double protectionLevel;
	private Security security;
	private OrderMatcher orderMatcher;
	private HashSet<String> validOrderTypes;
	private PriorityBlockingQueue<Orders> buyQueue;
	private PriorityBlockingQueue<Orders> sellQueue;
	
	/**
	 * Stop orders will remain in this queue while stop price
	 * has not triggered. 
	 */
	private PriorityBlockingQueue<Orders> stopQueue;


	public Book(String symbol, HashSet<String> orderTypes, MatchTypes matchType, double protectionLevel){
		this.orderCount = 0;
		this.protectionLevel = protectionLevel;
		this.security = SecurityService.getInstance().provideSecurity(symbol);
		this.validOrderTypes = orderTypes;
		this.orderMatcher = new OrderMatcher(matchType, this.protectionLevel);
		this.buyQueue = orderMatcher.getBuyQueue();
		this.sellQueue = orderMatcher.getSellQueue();
	}

	public Security getSecurity() {
		return security;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public Security getSymbol() {
		return security;
	}

	public void getBookInfo() {

	}

	private boolean validateOrder(Orders order) {

		boolean valid = false;

		valid = isValidType(order);
		
		valid = valid && !order.getClOrdID().isEmpty();
		
		//verifica se a quantidade para o instrumento eh valida
		valid = valid && (order.getVolume() % security.getSecurityInfoID().getMinVolume()) == 0;

		//verifica se o preco esta correto
		if(order.getPrice() == 0) {
			valid = valid 
					&& order.getOrderType() != OrderTypes.Market 
					&& order.getOrderType() != OrderTypes.MarketWithLeftOverAsLimit;
			
		}else if(order.getPrice() > 0) {
			valid = valid && true;
		}
		
		return valid;
	}

	private boolean isValidType(Orders order) {

		boolean isValid = false;
		switch(order.getOrderType()) {

		case Market:
			isValid = true;
			break;

		case Limit:
			isValid = true;
			break;

		case Stop:
			isValid = true;
			break;

		case StopLimit:
			isValid = true;
			break;

		case MarketWithLeftOverAsLimit:
			isValid = true;
			break;

		default: isValid = false;
		}

		return isValid;
	}



	public boolean addOrder(Orders order, SessionID sessionID) {
		boolean added = false;

		try {
			if(added = validateOrder(order)) {
				order.setWorkingIndicator(WorkingIndicator.No_Working);
				order.setOrderStatus(OrderStatus.SUSPENDED);
			}
			
			//ainda nao adicionou no book, deve primeiro verificar se pode executar 
			//antes de inserir no book
			if(added) {
				//envia mensagem informando que a ordem foi aceita
				OrderEvent oe = new OrderEvent(ExecutionTypes.NEW, new Date(), order.getVolume(), order.getPrice());
				oe.setMessage("Order received");
				oe.setOrderID(order);
				order.addExecution(oe);
				
				sendMessage(((Fix44EngineMessageEncoder)MessageEncoder.getEncoder(sessionID)).executionReport(oe), sessionID);
				orderCount++;
				
				//adiciona a ordem no match engine
				added = added && orderMatcher.addOrder(order);
				
			}

		}catch(OrderException e) {
			added = false;
			OrderEvent oe = new OrderEvent(ExecutionTypes.REJECTED, new Date(), order.getVolume(), order.getPrice());
			oe.setMessage("Order rejected: " + e.getMessage());
			oe.setOrderID(order);
			sendMessage(((Fix44EngineMessageEncoder)MessageEncoder.getEncoder(sessionID)).executionReport(oe), sessionID);
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}

		return added;
	}

	
	
	public void cancelOrder(Orders ordr) {
		
	}
	
	public void replaceOrder(Orders ordr){
		if(ordr.getSide() == Side.BUY){
			buyQueue.forEach(o -> o.getClOrdID().equals(ordr.getClOrdID()));
			
		}
		else{
			sellQueue.forEach(o -> o.getClOrdID().equals(ordr.getClOrdID()));
		}
		
		
		
		
		
		//TERMINAR:
		//enviar o estado atual do book com o que mudou do estado anterior
		
		
	}
	
	
	
	public void closeBook() {
		
		buyQueue.forEach(o -> cancelOrder(o));
		sellQueue.forEach(o -> cancelOrder(o));
		
	}	
	


	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageSender#sendMessage(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		return MessageRepository.getInstance().addMessage(message, sessionID);
	}	


}
