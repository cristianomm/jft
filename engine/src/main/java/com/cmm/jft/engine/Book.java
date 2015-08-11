/**
 * 
 */
package com.cmm.jft.engine;

import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cmm.jft.engine.message.MessageEncoder;
import com.cmm.jft.engine.message.MessageSender;
import com.cmm.jft.trading.OrderExecution;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.securities.Security;
import com.cmm.jft.trading.services.SecurityService;

import quickfix.Message;
import quickfix.SessionID;

/**
 * <p><code>Book.java</code></p>
 * @author Cristiano M Martins
 * @version 26 de jul de 2015 02:23:19
 *
 */
public class Book implements MessageSender {
	
	private Security security;
	private HashSet<String> validOrderTypes;
	private ConcurrentLinkedQueue<Orders> buyQueue;
	private ConcurrentLinkedQueue<Orders> sellQueue;
	
	
	public Book(String symbol, HashSet<String> orderTypes){
		this.security = SecurityService.getInstance().provideSecurity(symbol);
		this.validOrderTypes = orderTypes;
		this.buyQueue = new ConcurrentLinkedQueue<>();
		this.sellQueue = new ConcurrentLinkedQueue<>();
	}
	
	
	/**
	 * @return the buyOrders
	 */
	public ConcurrentLinkedQueue<Orders> getBuyOrders() {
		return this.buyQueue;
	}
	
	/**
	 * @return the sellOrders
	 */
	public ConcurrentLinkedQueue<Orders> getSellOrders() {
		return this.sellQueue;
	}
	
	public Security getSymbol() {
		return security;
	}
	
	public void getBookInfo() {
		
	}
	
	private boolean validateOrder(Orders order) {
		
		boolean valid = false;
		
		valid = isValidType(order);
				
		//verifica se a quantidade para o instrumento eh valida
		valid = valid && (order.getVolume() % security.getSecurityInfoID().getMinimalVolume()) == 0;
		
		//verifica se o preco esta correto
		valid = valid && order.getPrice()>0;
		
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
		
		if(added = validateOrder(order)) {
			
			//order.setOrderStatus(OrderStatus.);
			if(order.getSide() == Side.BUY) {
				added = added && buyQueue.add(order);
			}
			else {
				added = added && sellQueue.add(order);
			}
				
		}
		
		if(added) {
			OrderExecution oe = new OrderExecution(ExecutionTypes.NEW, new Date(), order.getVolume(), order.getPrice());
			oe.setMessage("Order received");
			oe.setOrderID(order);
			
			added = sendMessage(MessageEncoder.getEncoder(sessionID).executionReport(oe), sessionID);
		}
		
		return added;
	}
	
	public void calcelOrder(Orders order) {
		
	}


	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageSender#sendMessage(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		return MessageRepository.getInstance().addMessage(message, sessionID);
	}	
	
	
}
