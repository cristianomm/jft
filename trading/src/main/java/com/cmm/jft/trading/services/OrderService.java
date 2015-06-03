/**
 * 
 */
package com.cmm.jft.trading.services;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.OrdersPrices;
import com.cmm.jft.trading.Trade;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.exceptions.InvalidOrderException;
import com.cmm.jft.trading.securities.Security;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>OrderService.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 22/08/2013 17:41:12
 *
 */
public class OrderService {
	
	private static OrderService instance;
	

	private OrderService() {
		
	}

	public synchronized static OrderService getInstance() {
		if (instance == null) {
			instance = new OrderService();
		}
		return instance;
	}

	
	public Orders newLimitOrder(Security security, Side side, int volume, double limitPrice){
		
		return null;
	}
	
	public Orders newStopOrder(Security security, Side side, int volume, double stopPrice){
		return null;
	}
	
	public Orders newStopLimitOrder(Security security, Side side, int volume, double limitPrice, double stopPrice){
		return null;
	}
	
	public Orders newTrailingStopOrder(Security security, Side side, int volume, double stopPrice){
		return null;
	}
	
	public Orders newMarketOrder(Security security, Side side, int volume){
		return null;
	}
	
	
	public Orders getOrder(OrderTypes orderType, Side side, String symbol,
			int volume, OrdersPrices prices, Trade tradeID)
			throws InvalidOrderException, DataBaseException {

		Orders ordr = new Orders();

		try {

			// verifica se o simbolo existe, caso n exista, lanca exception
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("symbol", symbol);
			List<Security> l = (List<Security>) DBFacade.getInstance()
					.queryNamed("Security.findBySymbol", params);
			if (l == null || l.size() == 0) {
				throw new InvalidOrderException("Symbol not exists:" + symbol);
			}

			// verifica se o volume esta de acordo com o lote padrao do simbolo,
			// caso n esteja, lanca exception
			Security exItem = l.get(0);

			if ((volume % exItem.getSecurityInfoID().getMinimalVolume()) != 0) {
				throw new InvalidOrderException("Invalid Volume:" + volume);
			}

			ordr.changeVolume(volume);
			ordr.setSecurityID(exItem);
			ordr.setSide(side);
			ordr.setOrderType(orderType);
			ordr.changePrice(prices);
			ordr.setTradeID(tradeID);

			ordr = (Orders) DBFacade.getInstance()._persist(ordr);

		} catch (Exception e) {
			Logging.getInstance().log(OrderService.class,
					"Erro ao retornar Ordem.", e, Level.ERROR, false);
			throw new InvalidOrderException(e);
		}

		return ordr;
	}

	
//	/**
//	 * Classe responsavel pelo recebimento de respostas do mercado
//	 * <p>
//	 * <code>MarketListener.java</code>
//	 * </p>
//	 * 
//	 * @author Cristiano Martins
//	 * @version 22/08/2013 17:54:47
//	 *
//	 */
//	private class MarketListener implements Runnable {
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see java.lang.Runnable#run()
//		 */
//		@Override
//		public void run() {
//			while (startListener) {
//				try {
//					// recebe o evento da conexao
//					Event ev = connection.receiveEvent();
//
//					if (ev != null) {
//						addEvent(ev);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//					Logging.getInstance().log(OrderService.class, e,
//							Level.ERROR);
//				} catch (DataBaseException e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//	}
//
//	/**
//	 * Classe responsavel pelo envio de ordens atraves de uma conexao para o
//	 * mercado.
//	 * <p>
//	 * <code>Sender.java</code>
//	 * </p>
//	 * 
//	 * @author Cristiano Martins
//	 * @version 22/08/2013 17:55:59
//	 *
//	 */
//	private class Sender implements Runnable {
//
//		/*
//		 * (non-Javadoc)
//		 * 
//		 * @see java.lang.Runnable#run()
//		 */
//		@Override
//		public void run() {
//			while (startSender) {
//				try {
//					// envia para uma conexao um evento
//					Event event = eventsQueue.poll();
//					if (event != null) {
//						Event ev = connection.sendEvent(event);
//						addEvent(ev);
//					}
//				} catch (NullPointerException e) {
//					e.printStackTrace();
//					Logging.getInstance().log(OrderService.class, e,
//							Level.ERROR);
//				} catch (DataBaseException e) {
//					e.printStackTrace();
//					Logging.getInstance().log(OrderService.class, e,
//							Level.ERROR);
//				} catch (Exception e) {
//					e.printStackTrace();
//					Logging.getInstance().log(OrderService.class, e,
//							Level.ERROR);
//				}
//			}
//		}
//
//	}
//
//	private boolean started;
//	private boolean running;
//	private volatile boolean startSender;
//	private volatile boolean startListener;
//	private Queue<Event> eventsQueue;
	
//	public void sendEvent(Event event) {
//		try {
//			Event revent = connection.sendEvent(event);
//			addEvent(revent);
//		} catch (OrderException e) {
//			Logging.getInstance().log(OrderService.class,
//					"Erro ao enviar Evento", e, Level.ERROR, false);
//		} catch (DataBaseException e) {
//			Logging.getInstance().log(OrderService.class,
//					"Erro ao enviar Evento", e, Level.ERROR, false);
//		}
//
//	}
//
//	private void addEvent(Event event) throws OrderException, DataBaseException {
//
//		switch (event.getEventType()) {
//		case CANCEL:
//			cancelOrder(event.getOrderSerial());
//			break;
//		case EXPIRE:
//			cancelOrder(event.getOrderSerial());
//			break;
//		case NEW:
//			newOrder(event.getOrderSerial());
//			break;
//		case NEW_STOP_PRICE:
//			break;
//		case REENTRY:
//			break;
//		case REJECT:
//			cancelOrder(event.getOrderSerial());
//			break;
//		case REMOVE:
//			cancelOrder(event.getOrderSerial());
//			break;
//		case STOP_PRICE_TRIGGERED:
//			tradeOrder(event);
//			break;
//		case TRADE:
//			tradeOrder(event);
//			break;
//		case UPDATE:
//			changeOrder(event);
//			break;
//		}
//
//		registerEvent(event);
//
//	}
//
//	public void sendOrder(Orders order) {
//		try {
//			Event event = new Event();
//			event.setEventType(MarketEvents.NEW);
//			event.setMessage("Ordem Enviada.");
//			event.setOrderSerial(order.getOrderSerial());
//			event.setPrices(order.getOrdersPrices());
//			event.setVolume(order.getVolume());
//
//			orders.put(order.getOrderSerial(), order);
//
//			Event rev = connection.sendEvent(event);
//			addEvent(rev);
//			// eventsQueue.offer(event);
//
//		} catch (DataBaseException e) {
//			Logging.getInstance().log(OrderService.class,
//					"Erro ao enviar Ordem", e, Level.ERROR, false);
//		} catch (Exception e) {
//			Logging.getInstance().log(OrderService.class,
//					"Erro ao enviar Ordem", e, Level.ERROR, false);
//		}
//
//	}
//
//	private void registerEvent(Event event) {
//
//		try {
//			OrderEvent oe = new OrderEvent();
//			oe.setEventDateTime(event.getEventDateTime());
//			oe.setEventType(event.getEventType());
//			oe.setMessage(event.getMessage());
//			oe.setOrderID(orders.get(event.getOrderSerial()));
//			oe = (OrderEvent) DBFacade.getInstance()._persist(oe);
//		} catch (DataBaseException e) {
//			Logging.getInstance().log(OrderService.class,
//					"Erro ao registrar Evento", e, Level.ERROR, false);
//		}
//	}
//
//	private void newOrder(String orderSerial) {
//		Orders order = orders.get(orderSerial);
//		order.setOrderStatus(OrderStatus.OPEN);
//		order = updateOrder(order);
//		orders.put(order.getOrderSerial(), order);
//	}
//
//	private void tradeOrder(Event event) {
//
//		Date executionDateTime = event.getEventDateTime();
//		int execVolume = event.getVolume();
//		BigDecimal execPrice = new BigDecimal("" + event.getPrices().price);
//		Orders order = orders.get(event.getOrderSerial());
//
//		try {
//			DBFacade.getInstance().beginTransaction();
//
//			order = (Orders) DBFacade.getInstance().attachToSession(order, order.getOrderID());
//
//			// adiciona a execucao
//			order = addExecution(order, executionDateTime, execVolume, execPrice);
//
//			// caso a ordem tenha sido totalmente executada, adiciona entrada no
//			// journal
//			if (order.getOrderStatus() == OrderStatus.FILLED) {
//				TradingService.getInstance().registerTrade(order.getTradeID());
//			}
//
//			// TODO Ajustar maps de trade e ordens está persistindo objetos
//			// desatualizados
//			orders.put(order.getOrderSerial(), order);
//
//			DBFacade.getInstance().commit();
//
//			// fecha a sessao
//			DBFacade.getInstance().closeSession();
//
//		} catch (DataBaseException e) {
//			Logging.getInstance().log(OrderService.class,
//					"Erro ao registrar execucao de ordem.", e, Level.ERROR,
//					false);
//		}
//
//	}
//	
//	public void listenMarket() {
//		if (!startListener) {
//			this.startListener = true;
//			new Thread(new MarketListener()).start();
//		}
//	}
//
//	public void sendOrders() {
//		if (!startSender) {
//			this.startSender = true;
//			new Thread(new Sender()).start();
//		}
//	}

}
