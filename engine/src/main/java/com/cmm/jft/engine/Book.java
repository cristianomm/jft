/**
 * 
 */
package com.cmm.jft.engine;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.SortedMap;

import org.apache.log4j.Level;

import com.cmm.jft.engine.marketdata.incrementals.MarketDataStream;
import com.cmm.jft.engine.marketdata.recovery.SnapshotRecoveryStream;
import com.cmm.jft.engine.match.OrderMatcher;
import com.cmm.jft.engine.match.OrdersTable;
import com.cmm.jft.engine.match.Summary;
import com.cmm.jft.messaging.MessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.MessageSender;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.model.marketdata.BandLimits;
import com.cmm.jft.model.marketdata.MDSnapshot;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.trading.OrderEvent;
import com.cmm.jft.model.trading.Orders;
import com.cmm.jft.model.trading.enums.CancelTypes;
import com.cmm.jft.model.trading.enums.ExecutionTypes;
import com.cmm.jft.model.trading.enums.MarketPhase;
import com.cmm.jft.model.trading.enums.OrderStatus;
import com.cmm.jft.model.trading.enums.OrderTypes;
import com.cmm.jft.model.trading.enums.OrderValidityTypes;
import com.cmm.jft.model.trading.enums.RejectTypes;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.model.trading.enums.WorkingIndicator;
import com.cmm.jft.model.trading.exceptions.OrderException;
import com.cmm.jft.security.service.SecurityService;
import com.cmm.logging.Logging;

import quickfix.FixVersions;
import quickfix.Message;
import quickfix.SessionID;

/**
 * <p>
 * <code>Book.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 26 de jul de 2015 02:23:19
 *
 */
public class Book implements MessageSender {

	private MDSnapshot snapshot;
	private double protectionLevel;
	private double adjustPrice;
	private Security security;
	private OrderMatcher orderMatcher;
	private MarketDataStream umdf;
	private SnapshotRecoveryStream snpr;

	private BandLimits bandLimits;

	private IdGenerator orderIds;
	private IdGenerator eventIds;

	private OrdersTable buyTable;
	private OrdersTable sellTable;

	private MarketPhase phase;
	private ErrorCodes errCodes = ErrorCodes.getInstance();

	public static void main(String args[]) {

		long orderId = 1;
		String symbol = "WDOV17";
		Security security = SecurityService.getInstance().provideSecurity(symbol);
		SessionID sessionID = new SessionID(FixVersions.BEGINSTRING_FIX44, "SENDER", "TARGET");

		String traderIdB = "";
		String traderIdS = "";
		String brokerId = "";
		String sLct = "";

		Book book = new Book(symbol, .20);

		long t0 = System.currentTimeMillis();
		try {

			Orders ord = new Orders(orderId++, "123456", security, Side.BUY, 3321.5, 2, OrderTypes.Limit, traderIdB,
					brokerId, sLct);
			ord.setBrokerId("308");
			ord.setTraderId("123456");

			boolean added = book.addOrder(ord);

			ord = new Orders(orderId++, "123455", security, Side.SELL, 3321.5, 1, OrderTypes.Limit, traderIdS, brokerId,
					sLct);
			ord.setBrokerId("154");
			ord.setTraderId("654321");

			added = book.addOrder(ord);

			System.out.println(System.currentTimeMillis() - t0);

			double qt = -1000;
			for (int i = 0; i < qt; i++) {
				ord = new Orders(orderId++, "123457" + i, security, Side.BUY, 3321.5, 2, OrderTypes.Limit, traderIdB,
						brokerId, sLct);
				ord.setBrokerId("308");
				ord.setTraderId("3" + i);

				added = added && book.addOrder(ord);
			}
			long t1 = System.currentTimeMillis() - t0;
			System.out.println("Tempo total: " + t1);
			System.out.println("Tempo por ordem: " + t1 / qt);

			assertTrue(added);

		} catch (OrderException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * @param symbol
	 * @param orderTypes
	 * @param matchType
	 * @param protectionLevel
	 */
	public Book(String symbol, double protectionLevel) {
		this.protectionLevel = protectionLevel;
		this.security = SecurityService.getInstance().provideSecurity(symbol);
		this.phase = MarketPhase.Pause;

		this.orderIds = new IdGenerator(LocalDateTime.now());
		this.eventIds = new IdGenerator(LocalDateTime.now());

		this.buyTable = new OrdersTable(Side.BUY);
		this.sellTable = new OrdersTable(Side.SELL);
		this.snapshot = new MDSnapshot(security);

		double auctionBand = security.getSecurityInfoId().getAuctionBand();
		double rejectHiBand = security.getSecurityInfoId().getRejectHiBand();
		double rejectLoBand = security.getSecurityInfoId().getRejectLoBand();
		int qtyLimit = security.getSecurityInfoId().getMaxVolume();

		this.bandLimits = new BandLimits(0, auctionBand, rejectHiBand, rejectLoBand, qtyLimit);

		this.umdf = MarketDataStream.getInstance();
		this.snpr = SnapshotRecoveryStream.getInstance();

		this.orderMatcher = new OrderMatcher(this.protectionLevel, umdf, buyTable, sellTable);

		// realiza um snapshot inicial
		takeSnapshot();
	}

	public Security getSecurity() {
		return security;
	}

	public void calculateAdjPrice() {

	}

	private boolean checkOrder(Orders order) throws OrderValidationException {

		boolean valid = true;
		try {
			switch (security.getSecurityInfoId().getObjectAsset()) {
			case COMMODITIES:
			case STOCK:
				checkEquity(order);
				break;

			case BASKET:
			case CURRENCY:
			case FUTURE:
			case INDEX:
			case OPTION:
			case SWAP:
			case TAXRATE:
			case OTHERS:
				checkDerivative(order);
				break;
			}

			checkCommon(order);

		} catch (OrderValidationException e) {
			valid = false;
			throw new OrderValidationException(e.getErrorCode(), e.getErrorMsg(), e);
		}

		return valid;
	}

	private void checkCommon(Orders order) throws OrderValidationException {
		if (order.getClOrdId().isEmpty()) {
			throw new OrderValidationException(1010, errCodes.getMessage(1010));
		}

		// verifica se a quantidade para o instrumento eh valida
		if ((order.getVolume() % security.getSecurityInfoId().getMinVolume()) != 0d) {
			throw new OrderValidationException(993502, errCodes.getMessage(993502));
		}

		switch (order.getOrderType()) {
		case Market:
		case MarketWithLeftOverAsLimit:
		case Stop:
			if (order.getPrice() != 0) {
				throw new OrderValidationException(992138, errCodes.getMessage(992138));
			}
			break;
		case Limit:
		case StopLimit:
			if (order.getPrice() <= 0) {
				throw new OrderValidationException(992071, errCodes.getMessage(992071));
			}
		}

	}

	private void checkEquity(Orders order) throws OrderValidationException {

		switch (order.getOrderType()) {
		case Market:
			break;
		case Limit:
		case Stop:
		case StopLimit:
		case MarketWithLeftOverAsLimit:
			if (order.getValidityType() == OrderValidityTypes.MOC
					|| order.getValidityType() == OrderValidityTypes.MOA) {
				throw new OrderValidationException(7047, errCodes.getMessage(7047));
			}
			break;
		default:
			throw new OrderValidationException(7047, errCodes.getMessage(7047));
		}

	}

	private void checkDerivative(Orders order) throws OrderValidationException {
		switch (order.getOrderType()) {
		case Market:
			break;

		case Stop:
		case StopLimit:
			if (order.getValidityType() != OrderValidityTypes.DAY) {
				throw new OrderValidationException(7047, errCodes.getMessage(7047));
			}
			if (order.getMaxFloor() > 0) {
				throw new OrderValidationException(7050, errCodes.getMessage(7050));
			}
			if (order.getMinVolume() > 0) {
				throw new OrderValidationException(992129, errCodes.getMessage(992129));
			}

			break;

		case Limit:
		case MarketWithLeftOverAsLimit:
			if (order.getValidityType() == OrderValidityTypes.MOC
					|| order.getValidityType() == OrderValidityTypes.MOA) {
				throw new OrderValidationException(7047, errCodes.getMessage(7047));
			}
			break;

		default:
			throw new OrderValidationException(7047, errCodes.getMessage(7047));
		}

	}

	private void adjustOrderParameters(Orders order) throws OrderException {

		switch (order.getOrderType()) {
		case Limit:
		case Market:
		case MarketWithLeftOverAsLimit:
			order.setWorkingIndicator(WorkingIndicator.Working);
			break;
		case Stop:
		case StopLimit:
			order.setWorkingIndicator(WorkingIndicator.No_Working);
			break;
		}

		order.setOrderStatus(OrderStatus.SUSPENDED);

		// ajusta parametros da ordem no recebimento da oferta
		order.setInsertDateTime(LocalDateTime.now());

		order.setOrderId(orderIds.nextLong());
	}

	public boolean addOrder(Orders order) {
		boolean added = false;
		try {
			umdf.openNewPacket();
			if (added = checkOrder(order)) {

				// ajusta os parametros da ordem
				adjustOrderParameters(order);

				// envia o execution report de ordem recebida
				sendExecutionReport(order, ExecutionTypes.NEW, "Order received.", 0);

				// adiciona a ordem na tabela correspondente
				OrdersTable table = order.getSide() == Side.BUY ? buyTable : sellTable;

				if (order.getOrderType() == OrderTypes.Stop || order.getOrderType() == OrderTypes.StopLimit) {
					table.addStop(order);
				} else {
					table.add(order);
				}

				// informa ao match engine a nova ordem
				orderMatcher.match(order);

				// envia market data
				int mboPos = table.getOrderPosition(order.getOrderId());
				int mbpPos = table.getPricePosition(order.getPrice());
				Summary sm = table.findSummary(order.getPrice());

				umdf.informNewOrder(order, mboPos, sm, mbpPos);

			} else {
				sendExecutionReport(order, ExecutionTypes.REJECTED, errCodes.getMessage(7000), 7000);
			}

		}

		catch (OrderException e) {
			added = false;
			sendExecutionReport(order, ExecutionTypes.REJECTED, errCodes.getMessage(7000), 7000);
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (OrderValidationException e) {
			added = false;
			sendExecutionReport(order, ExecutionTypes.REJECTED, e.getErrorMsg(), e.getErrorCode());
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} finally {
			umdf.closePacket();
		}

		return added;
	}

	public void cancelOrder(Orders order) {
		try {
			umdf.openNewPacket();

			Orders bookOrder = null;
			OrdersTable table = order.getSide() == Side.BUY ? buyTable : sellTable;

			if (order.getOrderId() > 0) {
				bookOrder = table.findByOrderId(order.getOrderId());
			} else if (order.getOrigClOrdId() != null) {
				bookOrder = table.findByClOrderId(order.getOrigClOrdId());
			}

			if (bookOrder != null) {
				orderMatcher.cancelOrder(bookOrder, CancelTypes.Requested);
			} else {
				sendMessage(
						((Fix44EngineMessageEncoder) MessageEncoder.getEncoder(null)).orderCancelReject(order,
								RejectTypes.OrderCancelRequest, 989001, ErrorCodes.getInstance().getMessage(989001)),
						SessionRepository.getInstance().getTraderSession(order.getTraderId()));
			}

		} catch (OrderException e) {
			e.printStackTrace();
		} finally {
			umdf.closePacket();
		}

	}

	public void replaceOrder(Orders order) {
		try {
			umdf.openNewPacket();

			Orders bookOrder = null;
			OrdersTable table = order.getSide() == Side.BUY ? buyTable : sellTable;

			if (order.getOrderId() > 0) {
				bookOrder = table.findByOrderId(order.getOrderId());
			} else if (order.getOrigClOrdId() != null) {
				bookOrder = table.findByClOrderId(order.getOrigClOrdId());
			}

			if (bookOrder != null) {
				// verifica a mudanca a realizar
				/*
				 * Caso a alteracao cause perda de prioridade: -remove a ordem do book -gera
				 * umdf -ajusta os valores -adiciona novamente no book -gera umdf
				 */

				/*
				 * Caso a alteracao mantenha a prioridade -ajusta os valores -gera umdf da ordem
				 * alterada
				 */

				OrderEvent change = new OrderEvent();
				change.setExecutionType(ExecutionTypes.REPLACE);

				// price and stopLimitPrice
				if (order.getPrice() != bookOrder.getPrice()) {
					change.setPrice(order.getPrice());
				}
				// stopPrice
				if (order.getStopPrice() != bookOrder.getStopPrice()) {
					change.setPrice(order.getStopPrice());
				}

				// increase Qty
				if (order.getVolume() > bookOrder.getVolume()) {
					change.setVolume(order.getVolume());
				}
				// decrease Qty
				else if (order.getVolume() < bookOrder.getVolume()) {
					change.setVolume(order.getVolume());
				}

				// min Qty
				if (order.getMinVolume() >= 0 && order.getMinVolume() != bookOrder.getMinVolume()) {
					change.setMinQty(order.getMinVolume());
				}
				// order type
				if (order.getOrderType() != null && order.getOrderType() != bookOrder.getOrderType()) {
					change.setOrderType(order.getOrderType());
				}
				// validity
				if (order.getValidityType() != null && order.getValidityType() != bookOrder.getValidityType()) {
					change.setValidity(order.getValidityType());
				}
				LocalDateTime priority = bookOrder.getOrderDateTime();
				bookOrder.addExecution(change);

				// caso a ordem tenha a prioridade alterada
				if (bookOrder.getOrderDateTime().isAfter(priority)) {
					// altera a prioridade da ordem, remove do book e re-insere
					umdf.informDeleteOrder(bookOrder, table.getOrderPosition(bookOrder.getOrderId()));
					table.remove(bookOrder.getOrderId());

					table.add(bookOrder);
					umdf.informNewOrder(bookOrder, table.getOrderPosition(bookOrder.getOrderId()), null, 0);

				} else {
					// envia a alteracao da ordem
					umdf.informChangeOrder(bookOrder, table.getOrderPosition(bookOrder.getOrderId()));
				}

			} else {// ordem desconhecida
				sendMessage(((Fix44EngineMessageEncoder) MessageEncoder.getEncoder(null)).orderCancelReject(order,
						RejectTypes.OrderCancelReplaceRequest, 989001, ErrorCodes.getInstance().getMessage(989001)),
						SessionRepository.getInstance().getTraderSession(order.getTraderId()));
			}

		} catch (OrderException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} finally {
			umdf.closePacket();
		}

	}

	public void closeBook() {

		// buyQueue.forEach(o -> cancelOrder(o));
		// sellQueue.forEach(o -> cancelOrder(o));

		// cancela as ordens conforme validade

		// realiza um novo snapshot
		takeSnapshot();

	}

	public void takeSnapshot() {
		snapshot.resetSnapshot(0);

		// add current orders in the snapshot
		for (SortedMap<LocalDateTime, Orders> orders : buyTable.getOrders().values()) {
			for (Orders ordr : orders.values()) {
				int position = buyTable.getOrderPosition(ordr.getOrderId());
				snapshot.addOffer(umdf.createMBOEntry(ordr, null, position));
			}
		}

		for (SortedMap<LocalDateTime, Orders> orders : sellTable.getOrders().values()) {
			for (Orders ordr : orders.values()) {
				int position = sellTable.getOrderPosition(ordr.getOrderId());
				snapshot.addOffer(umdf.createMBOEntry(ordr, null, position));
			}
		}

		// add information about this market
		snapshot.setOpenPrice(orderMatcher.getOpenPrice());
		snapshot.setClosePrice(orderMatcher.getClosePrice());
		snapshot.setHighPrice(orderMatcher.getHighPrice());
		snapshot.setLowPrice(orderMatcher.getLowPrice());
		snapshot.setVwapPrice(orderMatcher.getVwapPrice());
		snapshot.setTradeVolume(orderMatcher.getTotalVolume());
		snapshot.setFinancialVolume(orderMatcher.getFinancialVolume());

		snapshot.setLimits(bandLimits);

		// snapshot.(bandLimits.hardLimitHigh, bandLimits.hardLimitLow, 1, 0));
		// snapshot.(bandLimits.rejectionBandHigh, bandLimits.rejectionBandLow, 3, 2));
		// snapshot.(bandLimits.auctionBandHigh, bandLimits.auctionBandLow, 2, 2));
		// snapshot.(bandLimits.staticLimitHigh, bandLimits.staticLimitLow, 4, 0));
		// snapshot.(quantityLimit));
		snapshot.setPhase(phase);

		snpr.updateSnapshot(snapshot);

	}

	private void sendExecutionReport(Orders order, ExecutionTypes exec, String message, int ordRejReason) {

		try {
			OrderEvent oe = new OrderEvent(exec, LocalDateTime.now(), order.getVolume(), order.getPrice());
			oe.setOrderEventId(eventIds.nextLong());
			oe.setOrderId(order);
			oe.setMessage(message);
			oe.setOrdRejReason(ordRejReason);
			order.addExecution(oe);

			SessionID sessionID = SessionRepository.getInstance().getTraderSession(order.getTraderId());
			sendMessage(((Fix44EngineMessageEncoder) MessageEncoder.getEncoder(sessionID)).executionReport(oe),
					sessionID);
		} catch (OrderException e) {
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.engine.message.MessageSender#sendMessage(quickfix.Message,
	 * quickfix.SessionID)
	 */
	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		return MessageRepository.getInstance().addMessage(message, sessionID);
	}

}
