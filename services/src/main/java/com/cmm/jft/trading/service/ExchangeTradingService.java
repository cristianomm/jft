/**
 * 
 */
package com.cmm.jft.trading.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmm.jft.connector.engine.EngineConnector;
import com.cmm.jft.connector.engine.EngineStarter;
import com.cmm.jft.core.Configuration;
import com.cmm.jft.core.enums.Objects;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.financial.service.JournalService;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageDecoder;
import com.cmm.jft.model.financial.Broker;
import com.cmm.jft.model.financial.Brokerage;
import com.cmm.jft.model.financial.Commission;
import com.cmm.jft.model.financial.DistributionRule;
import com.cmm.jft.model.financial.ExchangeTax;
import com.cmm.jft.model.financial.JournalEntry;
import com.cmm.jft.model.financial.Rule;
import com.cmm.jft.model.financial.exceptions.RegistrationException;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.trading.OrderEvent;
import com.cmm.jft.model.trading.Orders;
import com.cmm.jft.model.trading.TradePosition;
import com.cmm.jft.model.trading.enums.OrderTypes;
import com.cmm.jft.model.trading.enums.OrderValidityTypes;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.model.trading.enums.TradeTypes;
import com.cmm.jft.model.trading.exceptions.OrderException;
import com.cmm.jft.security.service.SecurityService;
import com.cmm.logging.Logging;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import quickfix.FieldNotFound;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.OrderCancelReject;

/**
 * <p>
 * <code>ExchangeTradingService.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 10/03/2014 16:11:33
 * 
 */
public class ExchangeTradingService {

	/**
	 * Broker used for trading account.
	 */
	private Broker brokerID;

	private static ExchangeTradingService instance;

	@Autowired
	private SecurityService securityService;

	/**
	 * Market Connection.
	 */
	private EngineConnector connection;

	/**
	 * List for the interface...
	 */
	private ObservableList<Orders> ordersData;

	/**
	 * Map to the existing orders, orders are referenced by TradePosition, the orderkey
	 * are the key.
	 */
	private ConcurrentHashMap<String, Orders> orders;

	/**
	 * Map to get open trades. The String key are the symbol.
	 */
	private ConcurrentHashMap<String, TradePosition> positions;

	private Fix44EngineMessageDecoder decoder;

	/**
	 * 
	 */
	private ExchangeTradingService() {
		decoder = new Fix44EngineMessageDecoder();
		String strBroker = (String) Configuration.getInstance().getConfiguration("brokerID");
		brokerID = (Broker) DBFacade.getInstance().findObject("Broker.findByBrokerCode", "brokerCode", strBroker);

		this.ordersData = FXCollections.observableArrayList();
		this.orders = new ConcurrentHashMap<String, Orders>();
		this.positions = new ConcurrentHashMap<String, TradePosition>();
		this.connection = EngineConnector.getInstance();
	}

	/**
	 * @return the instance
	 */
	public static ExchangeTradingService getInstance() {
		if (instance == null) {
			instance = new ExchangeTradingService();
		}
		return instance;
	}

	public void connect() {
		try {
			EngineStarter es = new EngineStarter();
			es.start();

			System.out.println("start Engine Conector: ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the ordersData
	 */
	public ObservableList<Orders> getOrdersData() {
		return this.ordersData;
	}

	public ConcurrentHashMap<String, TradePosition> getPositions() {
		return positions;
	}

	public int getPosition() {
		int val = 0;
		for (TradePosition p : positions.values()) {
			val += p.getPosition();
		}
		return val;
	}

	public int getOrderCount() {
		return orders.size();
	}

	public int getOrderCount(double price) {
		return (int) orders.values().stream().filter(o -> o.getPrice() == price).count();
	}

	public double getPositionValue() {
		double val = 0;

		for (TradePosition p : positions.values()) {
			val += p.getTradeValue();
		}
		return val;
	}

	public double getProfit() {
		double val = 0;
		for (TradePosition p : positions.values()) {
			val += p.getProfit();
		}
		return val;
	}

	/**
	 * Load trades with OPEN status from database;
	 */
	public void loadOpenTrades() {
		try {
			// searches for open trades with the brokerid
			String query = String.format(
					"select tradeID from TradePosition " + "where tradestatus = 'OPEN' and brokerID = %d",
					brokerID.getBrokerId());
			List<?> rs = DBFacade.getInstance().queryNative(query);
			for (TradePosition tr : (List<TradePosition>) rs) {
				positions.put(tr.getSymbol(), tr);
			}

		} catch (DataBaseException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}

	// /**
	// * Cria as Ordens de compra e venda e registra suas respectivas execucoes
	// * sem passar pelo mercado
	// *
	// * @param symbol Simbolo do instrumento negociado.
	// * @param orderTypes Tipo de ordem.
	// * @param tradeTypes tipo de Negocio
	// * @param volume Quantidade negociada
	// * @param buyPrice preco executado na compra
	// * @param sellPrice preco executado na venda
	// * @param tradeDate Data da negociacao
	// * @param brokerID corretora
	// * @throws DataBaseException
	// */
	// public void addCompleteTrade(String symbol, OrderTypes orderTypes,
	// TradeTypes tradeType, int volume, OrdersPrices buyPrices,
	// OrdersPrices sellPrices, Date tradeDate, Broker brokerID) throws
	// DataBaseException {
	// try {
	//
	// DBFacade.getInstance().beginTransaction();
	// // obtem o trade
	// TradePosition trade = openPositions.get(symbol);
	//
	// // gera as ordens
	// Orders buyOrder = newOrder(orderTypes, Side.BUY, symbol, volume,
	// buyPrices, tradeDate, tradeType);
	// Orders sellOrder = newOrder(orderTypes, Side.SELL, symbol, volume,
	// sellPrices, tradeDate, tradeType);
	//
	// addExecution(buyOrder.getOrderSerial(), tradeDate, volume,
	// buyOrder.getPrice());
	// addExecution(sellOrder.getOrderSerial(), tradeDate, volume,
	// sellOrder.getPrice());
	//
	// registerTrade(trade);
	//
	// DBFacade.getInstance().commit();
	//
	// } catch (Exception e) {
	// Logging.getInstance().log(getClass(),
	// "Erro ao Adicionar ordens: " + e.getMessage(), e,
	// Level.ERROR, false);
	// } finally {
	// DBFacade.getInstance().closeSession();
	// }
	//
	// }

	/**
	 * 
	 * @param orderType
	 * @param side
	 * @param symbol
	 * @param volume
	 * @param price
	 * @param stopLoss
	 * @param stopGain
	 * @param duration
	 * @param tradeType
	 * @param validityType
	 * @param comment
	 * @return
	 */
	public int newOrder(OrderTypes orderType, Side side, String symbol, int volume, double price, double stopLoss,
			double stopGain, LocalDateTime duration, TradeTypes tradeType, OrderValidityTypes validityType,
			String comment) {

		int ret = -1;
		try {
			TradePosition position = null;

			if (!positions.containsKey(symbol)) {
				positions.put(symbol, new TradePosition(symbol));
			}

			position = positions.get(symbol);

			Security securityID = securityService.provideSecurity(symbol);
			List<Orders> ordrs = OrderService.getInstance().newOrder(orderType, securityID, side, volume, price,
					stopLoss, stopGain);
			if (!ordrs.isEmpty()) {

				for (Orders ordr : ordrs) {
					ordr.setDuration(duration);
					ordr.setTradeType(tradeType);
					ordr.setValidityType(validityType);
					ordr.setComment(comment);

					position.addOrder(ordr);
					storeOrder(ordr);

					// Cria o evento e adiciona no mercado
					sendNewOrderEvent(ordr);
				}
				ret = 0;
			}

		} catch (OrderException e) {
			ret = -1;
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (Exception e) {
			ret = -1;
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}

		return ret;
	}

	/**
	 * 
	 * @param trade
	 */
	public int closePosition(TradePosition position) {

		int ret = -1;
		try {
			// cancela as ordens abertas
			for (Orders order : position.getOrdersList()) {
				cancelOrder(order.getClOrdId());
			}

			// cria ordem de acordo com posicao aberta
			int openPosition = position.getPosition();
			if (openPosition != 0) {
				// se ta comprado passa vendido ou vv
				Side side = openPosition < 0 ? Side.BUY : Side.SELL;

				// Ajusta o volume para nao passar negativo
				int volume = openPosition > 0 ? openPosition : openPosition * -1;

				// calcula a duracao da ordem - ate o fim do dia
				LocalDateTime duration = LocalDate.now().atTime(23, 59, 50);

				// lanca ordem inversa(a posicao) a mercado do mesmo tipo do
				// TradePosition
				newOrder(OrderTypes.Market, side, position.getSymbol(), volume, 0, 0, 0, duration, TradeTypes.DAY_TRADE,
						OrderValidityTypes.DAY, "Close position");

			}
			ret = 0;

		} catch (Exception e) {
			ret = -1;
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}

		return ret;
	}

	public int cancelOrder(String clOrderID) {

		int ret = -1;
		Orders order = orders.get(clOrderID);
		try {
			sendCancelOrderEvent(order);
			// order.cancel();
			storeOrder(order);
			ret = 0;
		} catch (OrderException e) {
			ret = -1;
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		return ret;
	}

	public int changePrice(String clOrderID, double price, double stopPrice) {

		int ret = -1;
		// verifica se a ordem pode ser alterada
		Orders order = orders.get(clOrderID);
		try {
			// if (order.changePrice(price)) {
			sendChangeOrderEvent(order);
			storeOrder(order);
			// }
			ret = 0;
		} catch (OrderException e) {
			ret = -1;
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		return ret;
	}

	public int changeVolume(String clOrderID, int volume) {

		int ret = -1;
		// verifica se a ordem pode ser alterada
		Orders order = orders.get(clOrderID);
		try {
			// if (order.changeVolume(volume)) {
			sendChangeOrderEvent(order);
			storeOrder(order);
			// }
			ret = 0;
		} catch (OrderException e) {
			ret = -1;
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		return ret;
	}

	private void sendNewOrderEvent(Orders ordr) throws OrderException {
		connection.newOrderSingle(ordr);
		// verifica o retorno
		// if(retev.getValue(EventFields.EventType) == Events.ORDER_SEND){
		// Logging.getInstance().log(getClass(), "Order " + ordr.getClOrdId() +
		// " has sent to market.", Level.INFO);
		// }else{
		// throw new OrderException("Error sending order " + ordr.getClOrdId() +
		// retev.getValue(EventFields.Message));
		// }

	}

	private void sendCancelOrderEvent(Orders ordr) throws OrderException {
		connection.cancelRequest(ordr);
		// Event ret = null;//connection.sendEvent(event);
		// if(ret.getValue(EventFields.EventType) == Events.ORDER_CANCEL){
		// Logging.getInstance().log(getClass(), "Order " + ordr.getClOrdId() +
		// " has cancelled.", Level.INFO);
		// }else{
		// throw new OrderException("Error sending order " + ordr.getClOrdId() +
		// ret.getValue(EventFields.Message));
		// }

	}

	private void sendChangeOrderEvent(Orders ordr) throws OrderException {
		connection.cancelReplaceRequest(ordr);
		// Event ret = null;//connection.sendEvent(event);
		// if(ret.getValue(EventFields.EventType) == Events.ORDER_UPDATE){
		// Logging.getInstance().log(getClass(), "Order " + ordr.getClOrdId() +
		// " has changed.", Level.INFO);
		// }else{
		// throw new OrderException("Error sending order " + ordr.getClOrdId() +
		// ret.getValue(EventFields.Message));
		// }
	}

	public void onExecutionReport(ExecutionReport report) {
		try {
			String clOrdID = report.getString(11);
			if (orders.containsKey(clOrdID)) {
				OrderEvent oe = decoder.executionReport(report);
				orders.get(clOrdID).addExecution(oe);
			}
		} catch (FieldNotFound | OrderException e) {

		}
	}

	public void onOrderCancelReject(OrderCancelReject cancelReject) {

	}

	// /**
	// * Cria o evento de execucao e a execucao e os adiciona na ordem passada por
	// * parametro.
	// *
	// * @param order
	// * @param executionDateTime
	// * @param execVolume
	// * @param execPrice
	// * @return Ordem adicionada da Execucao e Evento de Execucao
	// */
	// public boolean addExecution(String clOrderID, Date executionDateTime, int
	// execVolume, double execPrice) {
	// boolean ret = false;
	// try {
	// if (orders.contains(clOrderID)) {
	// ret = orders.get(clOrderID)
	// .addExecution(new OrderEvent(ExecutionTypes.TRADE, executionDateTime,
	// execVolume, execPrice));
	// }
	// } catch (Exception e) {
	// Logging.getInstance().log(getClass(), "Erro ao criar execucao de ordem: " +
	// e.getMessage(), e, Level.ERROR,
	// false);
	// }
	// return ret;
	// }

	/**
	 * Realiza a contabilidade de uma posicao zerada
	 * 
	 * @param position
	 */
	public void registerTrade(TradePosition position) {

		try {
			// verifica se o trade esta aberto
			JournalEntry je = JournalService.getInstance().createEntry();

			// verifica se o trade esta fechado, caso esteja, a posicao ja foi
			// zerada e deve adicionar registro de lucro/perda e custos
			if (position.getPosition() == 0) {

				Brokerage brokerage = position.getBrokerage();
				int volume = position.getTradedVolume();
				double value = position.getTradeValue();

				DistributionRule dRule = JournalService.getInstance().getDistributionRule(Objects.Trade);

				for (Rule rule : dRule.getRuleSet()) {

					// -------------------------------------------Registra
					// Comissoes
					if (rule.getObject() == Objects.Commission) {
						for (Commission comm : brokerage.getCommissionList()) {
							double valaux = 0;
							switch (comm.getCalcType()) {
							case VALUE:
								valaux = value;
								break;
							case VOLUME:
								valaux = volume;
								break;
							default:
								valaux = value;
								break;
							}

							if (valaux > comm.getValueMin() && valaux <= comm.getValueMax()) {
								double commValue = comm.getCommValue();
								JournalService.getInstance().registerEntry(je, rule.getCreditAccountId(),
										rule.getDebitAccountId(), commValue, "Commission");
								break;
							}
						}
					}

					// -----------------------------------------------Registra
					// Taxas
					else if (rule.getObject() == Objects.ExchangeTax) {
						double taxValue = 0;
						for (ExchangeTax et : brokerage.getExchangeTaxList()) {
							switch (et.getCalcType()) {
							case TAX:
								taxValue = (et.getTax() * value);
								break;
							case VALUE:
								taxValue = (et.getTax());
								break;
							default:
								taxValue = (et.getTax() * value);
								break;
							}

							JournalService.getInstance().registerEntry(je, rule.getCreditAccountId(),
									rule.getDebitAccountId(), taxValue, et.getTaxName());
						}
					}

					else {
						// -------------------------------------Registra
						// lucro/prejuizo
						double buyPrice = 0;
						double sellPrice = 0;

						for (Orders order : position.getOrdersList()) {
							if (order.getSide() == Side.BUY) {
								volume = order.getExecutedVolume();
								buyPrice = buyPrice + order.getAvgPrice();
							} else if (order.getSide() == Side.SELL) {
								sellPrice = sellPrice + order.getAvgPrice();
							}
						}

						String descr = "Profit register";
						double profit = (buyPrice - sellPrice) * volume;

						JournalService.getInstance().registerEntry(je, rule.getCreditAccountId(),
								rule.getDebitAccountId(), profit, descr);
					}
				}

				// fecha o je
				je = JournalService.getInstance().closeEntry(je);
			}

		} catch (DataBaseException | RegistrationException e) {
			Logging.getInstance().log(ExchangeTradingService.class, "Error in Order Register.", e, Level.ERROR, false);
		}
	}

	private void storeOrder(Orders order) {
		orders.put(order.getClOrdId(), order);
		ordersData.add(order);

		try {
			DBFacade.getInstance()._persist(order);
		} catch (DataBaseException e) {
			Logging.getInstance().log(getClass(), 
					"Erro ao atualizar Ordem: " + order.getClOrdId(), e, Level.ERROR, false);
		}
	}

	// -------------------------------------------------------------------------
	// private Orders updateOrder(Orders order) {
	// try {
	// order = (Orders) DBFacade.getInstance()._update(order);
	// //order.refreshOrder();
	// order = (Orders) DBFacade.getInstance()._update(order);
	// } catch (OrderException e) {
	// Logging.getInstance().log(getClass(),
	// "Erro ao atualizar Ordem: " + order.getOrderSerial(), e,
	// Level.ERROR, false);
	// } catch (DataBaseException e) {
	// Logging.getInstance().log(getClass(),
	// "Erro ao atualizar Ordem: " + order.getOrderSerial(), e,
	// Level.ERROR, false);
	// }
	// return order;
	// }

}
