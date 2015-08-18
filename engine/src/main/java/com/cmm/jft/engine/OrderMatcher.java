/**
 * 
 */
package com.cmm.jft.engine;

import java.util.concurrent.ConcurrentLinkedQueue;

import quickfix.Message;
import quickfix.SessionID;

import com.cmm.jft.engine.message.MessageSender;
import com.cmm.jft.trading.OrderExecution;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.exceptions.OrderException;

/**
 * @author Cristiano M Martins
 * @version 17/08/15 11:17:42
 *
 */
public class OrderMatcher  implements MessageSender {

	private ConcurrentLinkedQueue<Orders> buyQueue;
	private ConcurrentLinkedQueue<Orders> sellQueue;

	
	public OrderMatcher(ConcurrentLinkedQueue<Orders> buyQueue, ConcurrentLinkedQueue<Orders> sellQueue) {
		this.buyQueue = buyQueue;
		this.sellQueue = sellQueue;
	}
	
	
	private void match() {
		
		Orders sell = sellQueue.poll();
		Orders buy = buyQueue.poll();
		
	}
	
	
	public boolean addBuyOrder(Orders buyOrder) throws OrderException {
		boolean add = false;
		Orders sell = sellQueue.peek(); //dont remove this order now!
		if(sell.getPrice() == buyOrder.getPrice() && sell.getVolume() <= buyOrder.getVolume()) {
			sell.setOrderStatus(OrderStatus.SUSPENDED); //block's this order
			
			
			
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
