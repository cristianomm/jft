/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import quickfix.Message;
import quickfix.SessionID;

import com.cmm.jft.engine.message.MessageSender;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.exceptions.OrderException;

/**
 * @author Cristiano M Martins
 * @version 17/08/15 11:17:42
 *
 */
public class OrderMatcher  implements MessageSender {

	private PriorityBlockingQueue<Orders> buyQueue;
	private PriorityBlockingQueue<Orders> sellQueue;

	
	public OrderMatcher() {
		this.buyQueue = new PriorityBlockingQueue<>(1000, new PriceTimeComparator());
		this.sellQueue = new PriorityBlockingQueue<>(1000, new PriceTimeComparator());
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
	
	
	public boolean addBuyOrder(Orders buyOrder) throws OrderException {
		boolean add = false;
		Orders sell = sellQueue.peek(); //dont remove this order now!
		if(sell.getPrice() == buyOrder.getPrice()) {
			sell.setOrderStatus(OrderStatus.SUSPENDED); //block's this order
			
			//fill buy and sell
			
			//register trade on trade book
			
			//send trade messages to participants
			
			
			if(sell.getOrderStatus() == OrderStatus.FILLED) {
				
			}
			
			
		}
		
		return add;
	}
	
	public boolean addSellOrder(Orders order) {
		boolean add = false;
		
		
		
		return add;
	}
	
	
	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		
		// TODO Auto-generated method stub
		
		
		return false;
	}
	
	// creates the order execution
	//	OrderExecution oex = new OrderExecution(ExecutionTypes.TRADE, executionDateTime, execVolume, execPrice);
	//	oex.setMessage("Execution of " + execution.getVolume() + " at price " + execution.getPrice());
	//	oex.setLeavesVolume(volume - oex.getVolume());
	
}
