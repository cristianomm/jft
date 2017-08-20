/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;

import com.cmm.jft.trading.Orders;

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
    
    
    private class Row{
	
	private Row next;
	private Row prev;
	private int position;
	private Orders order;
	
	
	
    }
    
    private Row root;
    
    private SortedMap<Long, Orders> orderIDs;
    private SortedMap<String, Orders> clordIDs;
    private SortedMap<Double, SortedMap<Date,Orders>> orders;
    
    
    public OrdersTable() {
	
	this.clordIDs = Collections.synchronizedSortedMap(new TreeMap<String, Orders>());
	this.orderIDs = Collections.synchronizedSortedMap(new TreeMap<Long, Orders>());
	this.orders = Collections.synchronizedSortedMap(new TreeMap<Double,SortedMap<Date,Orders>>());
    }
    
    /**
     * @return the clordIDs
     */
    public SortedMap<String, Orders> getClordIDs() {
	return clordIDs;
    }
    
    /**
     * @return the orderIDs
     */
    public SortedMap<Long, Orders> getOrderIDs() {
	return orderIDs;
    }
    
    /**
     * @return the orders
     */
    public SortedMap<Double, SortedMap<Date, Orders>> getOrders() {
	return orders;
    }
    
    
    public boolean add(Orders order) {
	boolean added=false;
	if(order != null && order.getOrderID() >0 && order.getClOrdID() != null) {
	    if(!orders.containsKey(order.getPrice())) {
		orders.put(order.getPrice(), 
			Collections.synchronizedSortedMap(new TreeMap<Date, Orders>()));
	    }
	    
	    orders.get(order.getPrice()).put(order.getOrderDateTime(), order); 
	    orderIDs.put(order.getOrderID(), order);
	    clordIDs.put(order.getClOrdID(), order);
	    
	    added = true;
	}
	
	return added;
    }
    
    public Orders remove(long orderID) {
	return remove(findByOrderID(orderID));
    }
    
    public Orders remove(String clOrderID) {
	return remove(findByClOrderID(clOrderID));
    }
    
    private Orders remove(Orders order) {
	Orders ordr = null;
	if(order != null) {
	    order = orderIDs.remove(order.getOrderID());
	    clordIDs.remove(order.getClOrdID());
	    orders.get(order.getPrice()).remove(order.getOrderDateTime());
	}
	return ordr;
    }
    

    public Orders findByClOrderID(String clOrderID) {
	return clordIDs.getOrDefault(clOrderID, null);
    }
    
    public Orders findByOrderID(long orderID) {
	return orderIDs.getOrDefault(orderID, null);
    }
    
    
    
    public int getPosition(long orderID) {
	int pos = -1;
	boolean find = false;
	for(Map.Entry<Double, SortedMap<Date, Orders>> set : orders.entrySet()) {
	    for(Orders ordr : set.getValue().values()){
		pos++;
		if(ordr.getOrderID() == orderID){
		    find = true;
		    break;
		}
	    }
	    if(find) {
		break;
	    }

	}
	
	return pos;
    }
    
    public int getPosition(String clOrderID) {
	int pos = -1;
	
	
	
	return pos;
    }
    
    
}
