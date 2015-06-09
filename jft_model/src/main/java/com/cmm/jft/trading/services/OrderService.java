/**
 * 
 */
package com.cmm.jft.trading.services;

import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;

import quickfix.examples.banzai.OrderSide;
import quickfix.examples.banzai.OrderType;
import quickfix.examples.ordermatch.OrderMatcher;
import quickfix.field.OrderCategory;
import quickfix.field.SecurityType;
import quickfix.fix50.OrderStatusRequest;

import com.cmm.jft.core.Configuration;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.OrdersPrices;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.exceptions.InvalidOrderException;
import com.cmm.jft.trading.exceptions.OrderException;
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

	
	public Orders[] newOrder(OrderTypes orderType, Security security, Side side, double price, double stopPrice, int volume){
		
		Orders[] ordrs = null;
		switch (orderType) {
		case CounterOrderSelection:
			break;
			
		case ForexSwap:
			break;
			
		case Funari:
			break;
			
		case Limit:
			ordrs = newLimitOrder(security, side, volume, price);
			break;
			
		case LimitOrBetter:
			break;
			
		case LimitWithOrWithout:
			break;
			
		case Market:
			ordrs = newMarketOrder(security, side, volume, price, stopPrice);
			break;
			
		case MarketIfTouched:
			break;
			
		case MarketWithLeftOverAsLimit:
			break;
			
		case NextFundValuationPoint:
			break;
			
		case OnBasis:
			break;
			
		case Pegged:
			break;
		
		case PreviousFundValuationPoint:
			break;
			
		case PreviouslyIndicated:
			break;
			
		case PreviouslyQuoted:
			break;
			
		case Stop:
			ordrs = newStopOrder(security, side, volume, stopPrice);
			break;
			
		case StopLimit:
			ordrs = newStopLimitOrder(security, side, volume, price, stopPrice);
			break;
			
		case WithOrWithout:
			break;

		default:
			break;
		}
		
		return ordrs;
	}
	
	public Orders[] newLimitOrder(Security security, Side side, int volume, double limitPrice){
		Orders[] ordr = new Orders[1];
		try {
			ordr[0] = new Orders(security, side, limitPrice, 0d, volume, OrderTypes.Limit, TradeTypes.DAY_TRADE);
		} catch (OrderException e) {
			e.printStackTrace();
		}
		
		return ordr;
	}
	
	public Orders[] newStopOrder(Security security, Side side, int volume, double stopPrice){
		Orders[] ordr = new Orders[1];
		try {
			double discount = (stopPrice * (Double)Configuration.getInstance().getConfiguration("MarketDiscount"));
			discount = side == Side.BUY?discount:-discount;
			discount = stopPrice + discount;
			ordr[0] = new Orders(security, side, discount, stopPrice, volume, OrderTypes.Stop, TradeTypes.DAY_TRADE);
		} catch (OrderException e) {
			e.printStackTrace();
		}
		
		return ordr;
	}
	
	public Orders[] newStopLimitOrder(Security security, Side side, int volume, double limitPrice, double stopPrice){
		Orders[] ordrs = new Orders[2];
		try {
			Side stopSide = side==Side.BUY?Side.SELL:Side.BUY;
			
			ordrs[0] = newLimitOrder(security, side, volume, limitPrice)[0];
			ordrs[1] = newStopOrder(security, stopSide, volume, stopPrice)[0];
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ordrs;
	}
	
	public Orders[] newMarketOrder(Security security, Side side, int volume, double gainPrice, double stopPrice){
		Orders[] ordrs = new Orders[1];
		try {
			ordrs[0] = new Orders(security, side, 0, 0, volume, OrderTypes.Market, TradeTypes.DAY_TRADE);
		} catch (OrderException e) {
			e.printStackTrace();
		}
		
		return ordrs;
	}
	
	public Orders newTrailingStopOrder(Security security, Side side, int volume, double stopPrice){
		
		return null;
	}
	
	public Orders[] newOTOOrder(Security security, Side side, int volume, double limitPrice, double gainPrice, double stopPrice){
		Orders[] ordrs = new Orders[3];
		try {
			Side stopSide = side==Side.BUY?Side.SELL:Side.BUY;
			
			ordrs[0] = newLimitOrder(security, side, volume, limitPrice)[0];//ordem de entrada
			ordrs[1] = newLimitOrder(security, side, volume, gainPrice)[0];//ordem de ganho
			ordrs[2] = newStopOrder(security, stopSide, volume, stopPrice)[0];//stop loss
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return ordrs;
	}
	
	
	
	
	public Orders createAndPersistOrder(OrderTypes orderType, Side side, String symbol, int volume, double price, double stopPrice)
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
			
			Security exItem = l.get(0);

			ordr.setSecurityID(exItem);
			ordr.setSide(side);
			ordr.setOrderType(orderType);
			ordr.changePrice(price);
			ordr.changeStopPrice(stopPrice);
			ordr.changeVolume(volume);
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
