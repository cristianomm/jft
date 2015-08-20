/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import quickfix.Message;
import quickfix.SessionID;
import quickfix.fix44.ExecutionReport;

import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.enums.MatchTypes;
import com.cmm.jft.engine.message.MessageEncoder;
import com.cmm.jft.engine.message.MessageSender;
import com.cmm.jft.trading.OrderExecution;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.exceptions.OrderException;

/**
 * @author Cristiano M Martins
 * @version 17/08/15 11:17:42
 *
 */
public class OrderMatcher  implements MessageSender {

	private double lastPrice;
	private double lastVolume;
	private double protectionLevel;
	private PriorityBlockingQueue<Orders> buyQueue;
	private PriorityBlockingQueue<Orders> sellQueue;

	
	public OrderMatcher(MatchTypes matchTypes, double protectionLevel) {
		
		this.protectionLevel = protectionLevel;
		if(matchTypes == MatchTypes.FIFO) {
			this.buyQueue = new PriorityBlockingQueue<>(1000, new PriceTimeComparator());
			this.sellQueue = new PriorityBlockingQueue<>(1000, new PriceTimeComparator());
		}
		
	}
	
	
	public double getLastPrice() {
		return lastPrice;
	}
	
	public double getLastVolume() {
		return lastVolume;
	}
	
	/**
	 * @return the buyQueue
	 */
	public PriorityBlockingQueue<Orders> getBuyQueue() {
		return this.buyQueue;
	}
	
	/**
	 * @return the sellQueue
	 */
	public PriorityBlockingQueue<Orders> getSellQueue() {
		return this.sellQueue;
	}
	
	private void match() {
		
		Orders sell = sellQueue.poll();
		Orders buy = buyQueue.poll();
		
	}
	
	
	public boolean addOrder(Orders buyOrder) throws OrderException {
		boolean add = false;
		
		add = execute(buyOrder);
		
		return add;
	}
	
	private boolean addOnBook(Orders ordr) {
		boolean add = false;
		if(ordr.getSide() == Side.BUY) {
			add = buyQueue.offer(ordr);
		}
		else {
			add = sellQueue.offer(ordr);
		}
		
		return add;
	}
	
	
	private boolean execute(Orders ordr) {
		
		boolean executed = false;
		switch(ordr.getOrderType()) {
		case Market:
			executed = executeMarketWithProtection(ordr);
			break;
		case Limit:
			executed = executeLimit(ordr);
			break;
		case Stop:
			executed = executeStopWithProtection(ordr);
			break;
		case StopLimit:
			executed = executeStopLimit(ordr);
			break;
		case MarketWithLeftOverAsLimit:
			executed = executeMarketToLimit(ordr);
			break;
		}
		
		return executed;
	}
	
	private boolean sendExecutions(Orders newOrder, Orders bookOrder, double qtyToFill, double priceToFill){
		boolean send = false;
		
		OrderExecution orderFill = new OrderExecution(ExecutionTypes.TRADE, qtyToFill, priceToFill);
		OrderExecution bookFill = new OrderExecution(ExecutionTypes.TRADE, qtyToFill, priceToFill);
		try {
			if(newOrder.addExecution(orderFill) && bookOrder.addExecution(bookFill)) {
				
				//ajusta os valores para ultima execucao
				this.lastPrice = priceToFill;
				this.lastVolume = qtyToFill;
				
				//recupera a sessao da ordem recebida
				SessionID orderSession = SessionRepository.getInstance().getSession(newOrder.getPartyID());
				send = sendMessage(MessageEncoder.getEncoder(orderSession).executionReport(orderFill), orderSession);
				
				//recupera a sessao da ordem que estava no book
				SessionID bookOrderSession = SessionRepository.getInstance().getSession(bookOrder.getPartyID());
				send = sendMessage(MessageEncoder.getEncoder(bookOrderSession).executionReport(bookFill), bookOrderSession);
			}else {
				throw new OrderException("Error on add executions.");
			}
			
		} catch (OrderException e) {
			e.printStackTrace();
			send = false;
		}
		
		return send;
	}
	
	
	/**
	 * Execute Market type orders received;
	 * @param ordr order to execute;
	 */
	private boolean executeMarketWithProtection(Orders ordr) {
		boolean exec = false;
		PriorityBlockingQueue<Orders> orders = getCounterpartyBookOrders(ordr.getSide());
		
		
		boolean inExec = true; 
		while(inExec) {
			
			Orders bookOrder = orders.peek();
			try {
				if(bookOrder != null) {
					double qtyToFill = 0;
					double priceToFill = bookOrder.getPrice();
					
					if(ordr.getLeavesVolume() <= bookOrder.getLeavesVolume()) {
						qtyToFill = ordr.getLeavesVolume();
					}
					else {// if(ordr.getLeavesVolume() > bookOrder.getLeavesVolume()) {
						qtyToFill = bookOrder.getLeavesVolume();
					}
						
					/*
					 * For bids, the protection price calculated is by adding an offset to the last trade price. 
					 * For offers, the offset is subtracted from the last trade. The protection price cannot be 
					 * specified in the incoming order.
					 */
					double offset = (lastPrice * protectionLevel) * (ordr.getSide()==Side.BUY? 1:-1);
					ordr.setProtectionPrice(lastPrice + offset);
					
					if(ordr.getSide() == Side.BUY && ordr.getProtectionPrice() <= bookOrder.getPrice()) {
						exec = sendExecutions(ordr, bookOrder, qtyToFill, priceToFill);
						inExec = ordr.getOrderStatus() == OrderStatus.PARTIALLY_FILLED;
					}
					else if(ordr.getSide() == Side.SELL && ordr.getProtectionPrice() >= bookOrder.getPrice()) {
						exec = sendExecutions(ordr, bookOrder, qtyToFill, priceToFill);
						inExec = ordr.getOrderStatus() == OrderStatus.PARTIALLY_FILLED;
					}
					//caso o preco esteja fora dos limites, 
					//adiciona no book correspondente como se fosse uma ordem limite
					else {
						ordr.addExecution(new OrderExecution(ExecutionTypes.REPLACE, ordr.getVolume(), ordr.getProtectionPrice()));
						exec = addOnBook(ordr);
						inExec = false;
					}
					
					//remove the filled order from book
					if(bookOrder.getOrderStatus() != OrderStatus.PARTIALLY_FILLED) {
						bookOrder = orders.poll();
					}			
					
				}
				else {
					ordr.addExecution(new OrderExecution(ExecutionTypes.REPLACE, ordr.getVolume(), ordr.getProtectionPrice()));
					exec = addOnBook(ordr);
					inExec = false;
				}
			}catch(OrderException e) {
				inExec = false;
			}catch(NullPointerException e) {
				inExec = false;
			}catch(Exception e){
				inExec = false;
			}
			
		}
		
		return exec;
		
	}
	
	private boolean executeLimit(Orders ordr) {
		
	}
	
	private boolean executeStopWithProtection(Orders ordr) {
		
	}
	
	private boolean executeStopLimit(Orders ordr) {
		
	}
	
	private boolean executeMarketToLimit(Orders ordr) {
		
	}
	
	
	private PriorityBlockingQueue<Orders> getCounterpartyBookOrders(Side side) {
		
		if(side == Side.BUY) {
			return sellQueue;
		}
		
		return buyQueue;
	}
	
	
	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		
		
				
		return false;
	}
	
	// creates the order execution
	//	OrderExecution oex = new OrderExecution(ExecutionTypes.TRADE, executionDateTime, execVolume, execPrice);
	//	oex.setMessage("Execution of " + execution.getVolume() + " at price " + execution.getPrice());
	//	oex.setLeavesVolume(volume - oex.getVolume());
	
}
