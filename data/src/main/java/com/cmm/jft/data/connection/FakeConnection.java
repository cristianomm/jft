/**
 * 
 */
package com.cmm.jft.data.connection;

import java.util.concurrent.LinkedBlockingQueue;

import com.cmm.jft.data.exceptions.ConnectionException;

/**
 * <p>
 * <code>FakeConnection.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 04/09/2013 00:32:43
 *
 */
public class FakeConnection {

//	LinkedBlockingQueue<Event> events;
//
//	public FakeConnection() {
//		this.events = new LinkedBlockingQueue<Event>();
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.cmm.jft_data.MarketConnection#receive()
//	 */
//	public Event receiveEvent() throws ConnectionException {
//
//		Event o = null;
//		Event event = new Event();
////		try {
////			o = events.take();
////
////			event.setEventType(MarketEvents.TRADE);
////			event.setMessage("Ordem executada.");
////			event.setOrderSerial(o.getOrderSerial());
////			event.setGain(o.getGain());
////			event.setLimit(o.getLimit());
////			event.setPrice(o.getPrice());
////			event.setStart(o.getStart());
////			event.setStop(o.getStop());
////			event.setVolume(o.getVolume());
////		} catch (InterruptedException e) {
////			e.printStackTrace();
////		}
//
//		return event;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * com.cmm.jft_data.MarketConnection#sendOrder(com.cmm.jft_trading.Orders)
//	 */
//	
//	public Event sendEvent(Event event) throws ConnectionException {
//
//		return addEvent(event);
//	}
//
//	private Event addEvent(Event event) {
//
//		Event ret = new Event();
////		switch (event.getEventType()) {
////		case CANCEL:
////			ret = cancel(event);
////			break;
////		case EXPIRE:
////			ret = cancel(event);
////			break;
////		case NEW:
////			ret = placed(event);
////			break;
////		case NEW_STOP_PRICE:
////			ret = change(event);
////			break;
////		case REENTRY:
////			break;
////		case REJECT:
////			ret = cancel(event);
////			break;
////		case REMOVE:
////			ret = cancel(event);
////			break;
////		case STOP_PRICE_TRIGGERED:
////			ret = trade(event);
////			break;
////		case TRADE:
////			ret = trade(event);
////			break;
////		case UPDATE:
////			ret = change(event);
////			break;
////		}
//
//		return ret;
//	}
//
//	private Event placed(Event event) {
//		Event ret = new Event();
////		ret.setEventType(MarketEvents.NEW);
////		ret.setMessage("Ordem acatada.");
////		ret.setOrderSerial(event.getOrderSerial());
////		events.offer(event);
//		return ret;
//	}
//
//	private Event cancel(Event event) {
//		Event ret = new Event();
////		ret.setEventType(MarketEvents.CANCEL);
////		ret.setMessage("Ordem cancelada.");
////		ret.setOrderSerial(event.getOrderSerial());
//		return ret;
//	}
//
//	private Event change(Event event) {
//		Event ret = new Event();
////		ret.setEventType(MarketEvents.UPDATE);
////		ret.setMessage("Ordem alterada.");
////		ret.setOrderSerial(event.getOrderSerial());
//		return ret;
//	}
//
//	private Event trade(Event event) {
//		Event ret = new Event();
////		ret.setEventType(MarketEvents.TRADE);
////		ret.setMessage("Ordem executada.");
////		ret.setOrderSerial(event.getOrderSerial());
//		return ret;
//	}
//
//	/* (non-Javadoc)
//	 * @see com.cmm.jft.data.connection.MarketConnection#changeOrder(java.lang.String)
//	 */
//	@Override
//	public Event changeOrder(String orderSerial)throws ConnectionException {
////		order.setOrderStatus(OrderStatus.REPLACED);
//
//		Event event = new Event();
////		event.setEventType(MarketEvents.UPDATE);
////		event.setMessage("Ordem alterada.");
////		event.setOrderSerial(orderSerial);
//
//		return event;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * com.cmm.jft_data.MarketConnection#cancelOrder(com.cmm.jft_trading.Orders)
//	 */
//	@Override
//	public Event cancelOrder(String orderSerial) throws ConnectionException {
//
//		Event event = new Event();
////		event.setEventType(MarketEvents.CANCEL);
////		event.setMessage("Ordem cancelada.");
////		event.setOrderSerial(orderSerial);
//
//		return event;
//	}

	
}
