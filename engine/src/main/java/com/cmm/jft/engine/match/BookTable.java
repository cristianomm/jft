/**
 * 
 */
package com.cmm.jft.engine.match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.MDEntryTypes;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.UpdateActions;

/**
 * <p>
 * <code>Table.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 03/03/2017 11:42:43
 *
 */
public class BookTable {

	private Side side;
	private OrdersTable orders;
	private SortedMap<Double, SortedMap<LocalDateTime, Orders>> stopQueue;

	/**
	 * @param buy
	 * @param capacity
	 */
	public BookTable(Side side) {
		this.side = side;

		this.orders = new OrdersTable(side);

		this.stopQueue = Collections.synchronizedSortedMap(new TreeMap<Double, SortedMap<LocalDateTime, Orders>>());
		// this.orders = new PriorityBlockingQueue<>(10000, new
		// PriceTimeComparator(side));
	}

	/**
	 * @return the orders
	 */
	public SortedMap<Double, SortedMap<LocalDateTime, Orders>> getOrders() {
		return orders.getOrders();
	}

	public void addStop(Orders ordr) throws Exception {
		if (ordr.getOrderType() == OrderTypes.Stop || ordr.getOrderType() == OrderTypes.StopLimit) {
			if (!stopQueue.containsKey(ordr.getStopPrice())) {
				stopQueue.put(ordr.getStopPrice(), (TreeMap<LocalDateTime, Orders>) Collections
						.synchronizedMap(new TreeMap<LocalDateTime, Orders>()));
			}
			stopQueue.get(ordr.getStopPrice()).put(ordr.getOrderDateTime(), ordr);

		} else {
			throw new Exception("Invalid order type: " + ordr.getOrderType());
		}

	}

	private MDEntry createMBOEntry(Orders order, UpdateActions updtAction) {
		MDEntry mboEntry = new MDEntry();

		mboEntry.setOrderID(order.getOrderID());
		if (side == Side.BUY) {
			mboEntry.setMdEntryBuyer(order.getBrokerID());
		} else {
			mboEntry.setMdEntrySeller(order.getBrokerID());
		}
		mboEntry.setMdEntryDateTime(order.getInsertDateTime());
		mboEntry.setMdEntryType(side == Side.BUY ? MDEntryTypes.BID : MDEntryTypes.OFFER);

		if (updtAction != null) {
			mboEntry.setMdUpdateAction(updtAction);
		}

		mboEntry.setMdEntryPx(order.getPrice());
		mboEntry.setMdEntrySize((int) order.getVolume());
		mboEntry.setMdEntryPosNo(orders.getOrderPosition(order.getOrderID()));

		return mboEntry;
	}

	public MDEntry[] add(Orders order) {
		MDEntry[] entries = new MDEntry[2];
		if (order.getSide() == side) {
			// adiciona a ordem
			orders.add(order);

			entries[0] = createMBOEntry(order, UpdateActions.New);

			Summary sum = orders.findSummary(order.getPrice());
			MDEntry mbpEntry = new MDEntry();
			mbpEntry.setMdEntryDateTime(order.getInsertDateTime());
			mbpEntry.setMdEntryType(side == Side.BUY ? MDEntryTypes.BID : MDEntryTypes.OFFER);
			mbpEntry.setMdUpdateAction(UpdateActions.New);
			mbpEntry.setMdEntryPx(order.getPrice());
			mbpEntry.setMdEntrySize(sum.getOrderVolume());
			mbpEntry.setNumberOfOrders(sum.getOrderCount());
			mbpEntry.setMdEntryPosNo(orders.getPricePosition(order.getPrice()));
			entries[1] = mbpEntry;
		}

		return entries;

	}

	public MDEntry[] remove(Orders order) {
		MDEntry[] entries = new MDEntry[2];
		Summary sum = orders.findSummary(order.getPrice());
		if (sum != null) {
			// --------------------------------------------------MBO
			entries[0] = createMBOEntry(order, UpdateActions.Delete);

			// remove a ordem
			orders.remove(order.getOrderID());

			// --------------------------------------------------MBP
			MDEntry mbpEntry = new MDEntry();
			mbpEntry.setMdEntryDateTime(order.getInsertDateTime());
			mbpEntry.setMdEntryType(side == Side.BUY ? MDEntryTypes.BID : MDEntryTypes.OFFER);
			mbpEntry.setMdUpdateAction(UpdateActions.Delete);
			mbpEntry.setMdEntryPosNo(orders.getPricePosition(order.getPrice()));
			entries[1] = mbpEntry;
		}
		return entries;
	}

	public MDEntry[] update(Orders order) {

		MDEntry[] entries = new MDEntry[2];
		if (order.getSide() == side) {
			entries[0] = createMBOEntry(order, UpdateActions.Change);

			Summary sum = orders.findSummary(order.getPrice());

			MDEntry mbpEntry = new MDEntry();
			mbpEntry.setMdEntryPx(sum.getPrice());
			mbpEntry.setMdEntrySize(sum.getOrderVolume());
			mbpEntry.setNumberOfOrders(sum.getOrderCount());
			mbpEntry.setMdEntryDateTime(order.getInsertDateTime());
			mbpEntry.setMdEntryType(side == Side.BUY ? MDEntryTypes.BID : MDEntryTypes.OFFER);
			mbpEntry.setMdUpdateAction(UpdateActions.Change);
			mbpEntry.setMdEntryPosNo(orders.getPricePosition(order.getPrice()));
			entries[1] = mbpEntry;

		}
		return entries;
	}

	/**
	 * Retorna uma lista com as provaveis execucoes para o preco e quantidade
	 * informados.
	 * 
	 * @param limitPrice   preco limite para a execucao da ordem.
	 * @param leavesVolume quantidade necessaria para executar a ordem.
	 * @return
	 */
	public List<OrderEvent> listExecutions(double limitPrice, double leavesVolume) {

		List<OrderEvent> events = null;
		if (side == Side.BUY) {
			events = listBuyExecutions(limitPrice, leavesVolume);
		} else {
			events = listSellExecutions(limitPrice, leavesVolume);
		}
		return events;
	}

	private List<OrderEvent> listBuyExecutions(double limitPrice, double leavesVolume) {
		ArrayList<OrderEvent> events = new ArrayList<>();

		int cumVolume = 0;
		for (SortedMap<LocalDateTime, Orders> tm : orders.getOrders().values()) {
			for (Orders bookOrder : tm.values()) {
				double priceToFill = bookOrder.getPrice();

				if (cumVolume < leavesVolume && priceToFill >= limitPrice) {
					// calcula o volume que falta para completar na ordem agressora
					double vol = leavesVolume - cumVolume;
					double volumeToFill = 0;

					if (vol >= bookOrder.getLeavesVolume()) {
						volumeToFill = bookOrder.getLeavesVolume();
					} else if (vol < bookOrder.getLeavesVolume()) {
						volumeToFill = vol;
					}

					// cria a execucao para a ordem do book
					OrderEvent fill = new OrderEvent(ExecutionTypes.TRADE, volumeToFill, priceToFill);
					fill.setOrderID(bookOrder);
					events.add(fill);
				}
			}
		}

		return events;
	}

	private List<OrderEvent> listSellExecutions(double limitPrice, double leavesVolume) {
		ArrayList<OrderEvent> events = new ArrayList<>();

		int cumVolume = 0;
		for (SortedMap<LocalDateTime, Orders> tm : orders.getOrders().values()) {
			for (Orders bookOrder : tm.values()) {
				double priceToFill = bookOrder.getPrice();

				if (cumVolume < leavesVolume && priceToFill <= limitPrice) {
					// calcula o volume que falta para completar na ordem agressora
					double vol = leavesVolume - cumVolume;
					double volumeToFill = 0;
					if (vol >= bookOrder.getLeavesVolume()) {
						volumeToFill = bookOrder.getLeavesVolume();
					} else if (vol < bookOrder.getLeavesVolume()) {
						volumeToFill = vol;
					}

					// cria a execucao para a ordem do book
					OrderEvent fill = new OrderEvent(ExecutionTypes.TRADE, volumeToFill, priceToFill);
					fill.setOrderID(bookOrder);
					events.add(fill);
				}
			}
		}

		return events;
	}

	public List<MDEntry> takeSnapshot() {
		ArrayList<MDEntry> mds = new ArrayList<>(500);
		synchronized (orders) {
			for (SortedMap<LocalDateTime, Orders> tm : orders.getOrders().values()) {
				for (Orders o : tm.values()) {
					mds.add(createMBOEntry(o, null));
				}
			}
		}
		// orders.forEach(o -> mds.add(createMBOEntry(o)));

		return mds;
	}

}
