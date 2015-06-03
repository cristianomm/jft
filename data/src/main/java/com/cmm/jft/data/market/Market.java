/**
 * 
 */
package com.cmm.jft.data.market;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * <p>
 * <code>Market.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 20/12/2013 12:44:29
 *
 */
public class Market {

//	private BlockingQueue<Orders> buyOrders;
//
//	private BlockingQueue<Orders> sellOrders;
//
//	private Queue<Event> receivedEvents;
//
//	private Queue<Event> sendEvents;
//
//	public Market() {
//		this.sendEvents = new ConcurrentLinkedQueue<Event>();
//		this.receivedEvents = new ConcurrentLinkedQueue<Event>();
//		this.buyOrders = new PriorityBlockingQueue<>(1000,
//				new OrderPriorityComparator());
//		this.sellOrders = new PriorityBlockingQueue<>(1000,
//				new OrderPriorityComparator());
//	}
//
//	public void trade() {
//
//	}
//
//	public void notifyEvents() {
//
//	}
//
//	public void addOrder(Orders order) {
//		switch (order.getSide()) {
//		case BUY:
//			buyOrders.offer(order);
//			break;
//		case SELL:
//			sellOrders.offer(order);
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	public void addEvent(Event event) {
//		receivedEvents.offer(event);
//	}

}
