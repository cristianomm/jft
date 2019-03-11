/**
 * 
 */
package com.cmm.jft.engine.match;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import com.cmm.jft.engine.ErrorCodes;
import com.cmm.jft.engine.OrderValidationException;
import com.cmm.jft.model.trading.Orders;
import com.cmm.jft.model.trading.enums.OrderTypes;
import com.cmm.jft.model.trading.enums.Side;

/**
 * <p>
 * <code>OrdersTable.java</code>
 * </p>
 *
 * @author Cristiano
 * @version 14/08/2017 11:41:06
 *
 */
public class OrdersTable {

	private class Row {
		private Row next;
		private Row prev;
		private int position;
		private Orders order;
	}

	private Row root;

	private ErrorCodes errCodes = ErrorCodes.getInstance();
	private SortedMap<Double, Summary> ordersSummary;
	private SortedMap<Long, Orders> orderIds;
	private SortedMap<String, Orders> clordIds;
	private SortedMap<Double, SortedMap<LocalDateTime, Orders>> orders;
	private SortedMap<Double, SortedMap<LocalDateTime, Orders>> stopQueue;

	public OrdersTable(Side side) {

		this.clordIds = Collections.synchronizedSortedMap(new TreeMap<String, Orders>());
		this.orderIds = Collections.synchronizedSortedMap(new TreeMap<Long, Orders>());
		this.orders = Collections.synchronizedSortedMap(new TreeMap<Double, SortedMap<LocalDateTime, Orders>>());
		this.ordersSummary = Collections.synchronizedSortedMap(new TreeMap<Double, Summary>(new PriceComparator(side)));
		this.stopQueue = Collections.synchronizedSortedMap(new TreeMap<Double, SortedMap<LocalDateTime, Orders>>());
	}

	/**
	 * @return the clordIds
	 */
	public SortedMap<String, Orders> getClordIds() {
		return clordIds;
	}

	/**
	 * @return the orderIds
	 */
	public SortedMap<Long, Orders> getOrderIds() {
		return orderIds;
	}

	/**
	 * @return the orders
	 */
	public SortedMap<Double, SortedMap<LocalDateTime, Orders>> getOrders() {
		return orders;
	}

	/**
	 * @return the ordersSummary
	 */
	public SortedMap<Double, Summary> getOrdersSummary() {
		return ordersSummary;
	}

	/**
	 * @return the stopQueue
	 */
	public SortedMap<Double, SortedMap<LocalDateTime, Orders>> getStopQueue() {
		return stopQueue;
	}

	public boolean add(Orders order) {
		boolean added = false;
		if (order != null && order.getOrderId() > 0 && order.getClOrdId() != null) {
			if (!orders.containsKey(order.getPrice())) {
				orders.put(order.getPrice(), Collections.synchronizedSortedMap(new TreeMap<LocalDateTime, Orders>()));
			}

			orders.get(order.getPrice()).put(order.getOrderDateTime(), order);
			orderIds.put(order.getOrderId(), order);
			clordIds.put(order.getClOrdId(), order);

			Summary sum = null;
			if (!ordersSummary.containsKey(order.getPrice())) {
				sum = new Summary(order.getPrice(), 1, (int) order.getLeavesVolume());
				ordersSummary.put(order.getPrice(), sum);
			} else {
				sum = ordersSummary.get(order.getPrice());
				sum.incrementOrder();
				sum.incrementVolume(order.getLeavesVolume());
			}

			added = true;
		}

		return added;
	}

	public Orders remove(long orderId) {
		return remove(findByOrderId(orderId));
	}

	public Orders remove(String clOrderId) {
		return remove(findByClOrderId(clOrderId));
	}

	private Orders remove(Orders order) {
		Orders ordr = null;
		if (order != null) {
			order = orderIds.remove(order.getOrderId());
			clordIds.remove(order.getClOrdId());
			orders.get(order.getPrice()).remove(order.getOrderDateTime());

			Summary sum = findSummary(order.getPrice());
			sum.decrementOrder();
			sum.decrementVolume(order.getLeavesVolume());

			// remove o summary
			if (sum.getOrderCount() == 0) {
				ordersSummary.remove(sum.getPrice());
			}

		}
		return ordr;
	}

	public void restate(Orders order) {
		remove(order.getOrderId());
		add(order);
	}

	public void addStop(Orders ordr) throws OrderValidationException {
		if (ordr.getOrderType() == OrderTypes.Stop || ordr.getOrderType() == OrderTypes.StopLimit) {
			if (!stopQueue.containsKey(ordr.getStopPrice())) {
				stopQueue.put(ordr.getStopPrice(), (TreeMap<LocalDateTime, Orders>) Collections
						.synchronizedMap(new TreeMap<LocalDateTime, Orders>()));
			}
			stopQueue.get(ordr.getStopPrice()).put(ordr.getOrderDateTime(), ordr);

		} else {
			throw new OrderValidationException(2045,
					errCodes.getMessage(2045) + ":Invalid order type: " + ordr.getOrderType());
		}

	}

	public Orders findByClOrderId(String clOrderId) {
		return clordIds.getOrDefault(clOrderId, null);
	}

	public Orders findByOrderId(long orderId) {
		return orderIds.getOrDefault(orderId, null);
	}

	public Summary findSummary(double price) {
		return ordersSummary.get(price);
	}

	public Orders getFirst() {
		Orders ret = null;
		for (SortedMap<LocalDateTime, Orders> sm : orders.values()) {
			for (Orders o : sm.values()) {
				return ret = o;
			}
		}
		return ret;
	}

	public int getOrderPosition(long orderId) {
		int pos = 0;
		for (Map.Entry<Double, SortedMap<LocalDateTime, Orders>> set : orders.entrySet()) {
			for (Orders ordr : set.getValue().values()) {
				pos++;
				if (ordr.getOrderId() == orderId) {
					return pos;
				}
			}
		}
		pos = -1;
		return pos;
	}

	public int getOrderPosition(String clOrderId) {
		int pos = 0;

		for (Map.Entry<Double, SortedMap<LocalDateTime, Orders>> set : orders.entrySet()) {
			for (Orders ordr : set.getValue().values()) {
				pos++;
				if (ordr.getClOrdId() == clOrderId) {
					return pos;
				}
			}
		}
		pos = -1;
		return pos;
	}

	public int getPricePosition(double price) {
		int pos = -1;
		for (Double level : ordersSummary.keySet()) {
			pos++;
			if (level == price) {
				break;
			}
		}
		return pos;
	}

}
