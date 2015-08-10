/**
 * 
 */
package com.cmm.jft.engine;

import java.util.HashSet;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.cmm.jft.engine.message.MessageSender;
import com.cmm.jft.trading.marketdata.MarketOrder;
import com.cmm.jft.trading.securities.Security;
import com.cmm.jft.trading.services.SecurityService;

import quickfix.Message;
import quickfix.SessionID;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;

/**
 * <p><code>Book.java</code></p>
 * @author Cristiano M Martins
 * @version 26 de jul de 2015 02:23:19
 *
 */
public class Book implements MessageSender {
	
	private Security security;
	private HashSet<String> validOrderTypes;
	private ConcurrentLinkedQueue<MarketOrder> buyQueue;
	private ConcurrentLinkedQueue<MarketOrder> sellQueue;
	
	
	public Book(String symbol, HashSet<String> orderTypes){
		this.security = SecurityService.getInstance().provideSecurity(symbol);
		this.validOrderTypes = orderTypes;
		this.buyQueue = new ConcurrentLinkedQueue<>();
		this.sellQueue = new ConcurrentLinkedQueue<>();
	}
	
	
	/**
	 * @return the buyOrders
	 */
	public ConcurrentLinkedQueue<MarketOrder> getBuyOrders() {
		return this.buyQueue;
	}
	
	/**
	 * @return the sellOrders
	 */
	public ConcurrentLinkedQueue<MarketOrder> getSellOrders() {
		return this.sellQueue;
	}
	
	public Security getSymbol() {
		return security;
	}
	
	public void getBookInfo() {
		
	}
	
	private void validateOrder(MarketOrder orders) {
		
	}
	
	public boolean addOrder(MarketOrder order) {
		boolean added = false;
		
		
		
		return added;
	}
	
	public void removeOrder() {
		
	}


	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageSender#sendMessage(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		
		return MessageRepository.getInstance().addMessage(message, sessionID);
	}
	
	
	
}
