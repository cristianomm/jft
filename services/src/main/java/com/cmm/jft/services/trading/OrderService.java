/**
 * 
 */
package com.cmm.jft.services.trading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;

import com.cmm.jft.core.Configuration;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.security.Security;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.exceptions.InvalidOrderException;
import com.cmm.jft.trading.exceptions.OrderException;
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
	
	
	private long orderID;
	private String brokerID;
	private String traderID;
	private String clOrderIDPrefix;
	
	private static OrderService instance;
	

	private OrderService() {
		this.brokerID = Configuration.getInstance().getConfiguration("brokerID").toString();
		this.traderID = Configuration.getInstance().getConfiguration("traderID").toString();
		this.clOrderIDPrefix = Configuration.getInstance().getConfiguration("clOrderIDPrefix").toString();
		
	}

	public synchronized static OrderService getInstance() {
		if (instance == null) {
			instance = new OrderService();
		}
		return instance;
	}
	
	/**
	 * Cria uma ordem 
	 * @param security
	 * @param side
	 * @param price
	 * @param volume
	 * @param orderType
	 * @param tradeType
	 * @return
	 * @throws OrderException 
	 */
	private Orders createOrder(Security security,  Side side, double price, double volume, 
			OrderTypes orderType, TradeTypes tradeType) throws OrderException{
		
		Orders ordr = null;
		
		String clOrdID = String.format("%1$s.%2$s-%3$06d", clOrderIDPrefix, brokerID, orderID++);
		ordr = new Orders(clOrdID, security, side, price, volume, orderType, tradeType);	
		
		ordr.setBrokerID(brokerID);
		ordr.setTraderID(traderID);
		
		return ordr;
	}

	
	public List<Orders> newOrder(OrderTypes orderType, Security security, Side side, double volume,
			double price, double stopLoss, double stopGain){
		
		List<Orders> ordrs = new ArrayList<Orders>();
		
		boolean createLA = false;
		
		switch (orderType) {
		case CounterOrderSelection:
			break;
			
		case ForexSwap:
			break;
			
		case Funari:
			break;
			
		case Limit:
			ordrs.add(newLimitOrder(security, side, volume, price));
			createLA = true;
			break;
			
		case LimitOrBetter:
			break;
			
		case LimitWithOrWithout:
			break;
			
		case Market:
			ordrs.add(newMarketOrder(security, side, volume));
			createLA = true;
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
			ordrs.add(newStopOrder(security, side, volume, stopLoss));
			createLA = false;
			break;
			
		case StopLimit:
			ordrs.add(newLimitOrder(security, side, volume, price));
			createLA = false;
			break;
			
		case WithOrWithout:
			break;

		default:
			break;
		}
		
		
		if(createLA){
			Side sideExit = side == Side.BUY?Side.SELL:Side.BUY;
			for(Orders ordr:createLossAndGain(security, sideExit, volume, price, stopLoss, stopGain)){
				if(ordr != null){
					ordrs.add(ordr);
				}
			}
		}
		
		for (int i = 0; i < ordrs.size(); i++) {
			if(ordrs.get(i) == null){
				ordrs.remove(i);
			}
		}
		
		return ordrs;
	}
	
	private Orders newLimitOrder(Security security, Side side, double volume, double limitPrice){
		Orders ordr = null;
		try {
			ordr = createOrder(security, side, limitPrice, volume, OrderTypes.Limit, TradeTypes.DAY_TRADE);			
		} catch (OrderException e) {
			e.printStackTrace();
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		
		return ordr;
	}
	
	private Orders newStopOrder(Security security, Side side, double volume, double stopPrice){
		Orders ordr = null;
		try {
			//calcula o valor maximo limite que a ordem podera ser executada
			Object obj = Configuration.getInstance().getConfiguration("MarketDiscount");
			double discount = (stopPrice * (obj == null?0: Double.parseDouble(obj.toString())));
			discount = side == Side.BUY?discount:-discount;
			stopPrice = stopPrice + discount;
			ordr = createOrder(security, side, stopPrice, volume, OrderTypes.Stop, TradeTypes.DAY_TRADE);
		} catch (OrderException e) {
			//e.printStackTrace();
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		
		return ordr;
	}
			
	private Orders newMarketOrder(Security security, Side side, double volume){
		Orders ordrs = null;
		try {
			ordrs = createOrder(security, side, 0, volume, OrderTypes.Market, TradeTypes.DAY_TRADE);
		} catch (OrderException e) {
			e.printStackTrace();
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		
		return ordrs;
	}
	
	
	private Orders newTrailingStopOrder(Security security, Side side, int volume, double stopPrice){
		
		return null;
	}	
	
	
	private Orders[] createLossAndGain(Security security, Side sideExit, double volume, double price, double stopLoss, double stopGain){
		
		Orders[] ordrs = new Orders[2];
		
		try{
			//StopLoss
			if(stopLoss!= price && stopLoss != stopGain && stopLoss > 0){
				ordrs[0] = (createOrder(security, sideExit, stopLoss, volume, OrderTypes.Limit, TradeTypes.DAY_TRADE));
			}
			
			//StopGain
			if(stopGain != price && stopGain != stopLoss && stopGain > 0){
				ordrs[1] = (createOrder(security, sideExit, stopGain, volume, OrderTypes.Limit, TradeTypes.DAY_TRADE));
			}
		} catch(OrderException e){
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		return ordrs;
	}
	
	
	public Orders createAndPersistOrder(OrderTypes orderType, Side side, String symbol, int volume, double price, double stopPrice)
			throws InvalidOrderException, DataBaseException {

		Orders ordr = null;

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
			ordr = createOrder(exItem , side, price, volume, orderType, TradeTypes.DAY_TRADE);
			
			//ordr.changePrice(price);
			//ordr.changeVolume(volume);
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
