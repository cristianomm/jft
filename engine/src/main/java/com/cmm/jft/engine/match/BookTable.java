/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.PriorityBlockingQueue;

import org.junit.runner.notification.RunNotifier;

import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.security.Security;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.MDEntryTypes;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.enums.UpdateActions;
import com.cmm.jft.trading.enums.WorkingIndicator;
import com.cmm.jft.trading.exceptions.OrderException;

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
    private SortedMap<Double, SortedMap<Date, Orders>> stopQueue;


    /**
     * @param buy 
     * @param capacity
     */
    public BookTable(Side side) {
	this.side = side;

	this.orders = new OrdersTable(side);

	this.stopQueue = Collections.synchronizedSortedMap(
		new TreeMap<Double, SortedMap<Date, Orders>>());
	//this.orders = new PriorityBlockingQueue<>(10000, new PriceTimeComparator(side));
    }

    /**
     * @return the orders
     */
    public SortedMap<Double, SortedMap<Date, Orders>> getOrders() {
	return orders.getOrders();
    }

    private MDEntry createMBOEntry(Orders order, UpdateActions updtAction){
	MDEntry mboEntry = new MDEntry();

	mboEntry.setOrderID(order.getOrderID().toString());
	if(side == Side.BUY){
	    mboEntry.setMdEntryBuyer(order.getBrokerID());
	}else{
	    mboEntry.setMdEntrySeller(order.getBrokerID());
	}
	mboEntry.setMdEntryDate(order.getInsertDate());
	mboEntry.setMdEntryTime(order.getInsertTime());
	mboEntry.setMdEntryType(side == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);

	if(updtAction != null) {
	    mboEntry.setMdUpdateAction(updtAction);
	}

	mboEntry.setMdEntryPx(order.getPrice());
	mboEntry.setMdEntrySize((int) order.getVolume());
	mboEntry.setMdEntryPosNo(orders.getOrderPosition(order.getOrderID()));

	return mboEntry;
    }


    public void addStop(Orders ordr) throws Exception{
	if(ordr.getOrderType() == OrderTypes.Stop || ordr.getOrderType() == OrderTypes.StopLimit){
	    if(!stopQueue.containsKey(ordr.getStopPrice())){
		stopQueue.put(ordr.getStopPrice(), (TreeMap<Date, Orders>) Collections.synchronizedMap(new TreeMap<Date, Orders>()));
	    }
	    stopQueue.get(ordr.getStopPrice()).put(ordr.getOrderDateTime(), ordr);

	}else{
	    throw new Exception("Invalid order type: " + ordr.getOrderType());
	}

    }


    public MDEntry[] add(Orders order){
	MDEntry[] entries = new MDEntry[2];
	if(order.getSide() == side){
	    //adiciona a ordem
	    orders.add(order);

	    entries[0] = createMBOEntry(order, UpdateActions.New);
	    
	    Summary sum = orders.findSummary(order.getPrice());
	    MDEntry mbpEntry = new MDEntry();
	    mbpEntry.setMdEntryDate(order.getInsertDate());
	    mbpEntry.setMdEntryTime(order.getInsertTime());
	    mbpEntry.setMdEntryType(side == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);
	    mbpEntry.setMdUpdateAction(UpdateActions.New);
	    mbpEntry.setMdEntryPx(order.getPrice());
	    mbpEntry.setMdEntrySize(sum.getOrderVolume());
	    mbpEntry.setNumberOfOrders(sum.getOrderCount());
	    mbpEntry.setMdEntryPosNo(orders.getPricePosition(order.getPrice()));
	    entries[1] = mbpEntry;
	}

	return entries;

    }


    public MDEntry[] remove(Orders order){
	MDEntry[] entries = new MDEntry[2];
	Summary sum = orders.findSummary(order.getPrice());
	if(sum != null){
	    //--------------------------------------------------MBO
	    entries[0] = createMBOEntry(order, UpdateActions.Delete);

	    //remove a ordem
	    orders.remove(order.getOrderID());


	    //--------------------------------------------------MBP
	    MDEntry mbpEntry = new MDEntry();
	    mbpEntry.setMdEntryDate(order.getInsertDate());
	    mbpEntry.setMdEntryTime(order.getInsertTime());
	    mbpEntry.setMdEntryType(side == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);
	    mbpEntry.setMdUpdateAction(UpdateActions.Delete);
	    mbpEntry.setMdEntryPosNo(orders.getPricePosition(order.getPrice()));
	    entries[1] = mbpEntry;
	}
	return entries;
    }

    public MDEntry[] update(Orders order){

	MDEntry[] entries = new MDEntry[2];
	if(order.getSide() == side){
	    entries[0] = createMBOEntry(order, UpdateActions.Change);

	    Summary sum = orders.findSummary(order.getPrice());

	    MDEntry mbpEntry = new MDEntry();
	    mbpEntry.setMdEntryPx(sum.getPrice());
	    mbpEntry.setMdEntrySize(sum.getOrderVolume());
	    mbpEntry.setNumberOfOrders(sum.getOrderCount());
	    mbpEntry.setMdEntryDate(order.getInsertDate());
	    mbpEntry.setMdEntryTime(order.getInsertTime());
	    mbpEntry.setMdEntryType(side == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);
	    mbpEntry.setMdUpdateAction(UpdateActions.Change);
	    mbpEntry.setMdEntryPosNo(orders.getPricePosition(order.getPrice()));
	    entries[1] = mbpEntry;

	}
	return entries;
    }

    /**
     * Retorna uma lista com as provaveis execucoes para o preco e quantidade informados.
     * @param limitPrice preco limite para a execucao da ordem.
     * @param leavesVolume quantidade necessaria para executar a ordem.
     * @return
     */
    public List<OrderEvent> listExecutions(double limitPrice, double leavesVolume){

	List<OrderEvent> events = null;
	if(side == Side.BUY) {
	    events = listBuyExecutions(limitPrice, leavesVolume);
	}
	else {
	    events = listSellExecutions(limitPrice, leavesVolume);
	}
	return events;
    }

    
    private List<OrderEvent> listBuyExecutions(double limitPrice, double leavesVolume){
	ArrayList<OrderEvent> events = new ArrayList<>();
	
	int cumVolume = 0;
	for(SortedMap<Date, Orders> tm : orders.getOrders().values()) {
	    for(Orders bookOrder : tm.values()) {
		double priceToFill = bookOrder.getPrice();
		
		if(cumVolume < leavesVolume && priceToFill >= limitPrice){
		    //calcula o volume que falta para completar na ordem agressora
		    double vol = leavesVolume - cumVolume;
		    double volumeToFill = 0;
		    
		    if(vol >= bookOrder.getLeavesVolume()){
			volumeToFill = bookOrder.getLeavesVolume();
		    }
		    else if(vol < bookOrder.getLeavesVolume()){
			volumeToFill = vol;
		    }

		    //cria a execucao para a ordem do book
		    OrderEvent fill = new OrderEvent(ExecutionTypes.TRADE, volumeToFill, priceToFill);
		    fill.setOrderID(bookOrder);
		    events.add(fill);
		}

	    }
	}
	
	return events;
    }

    private List<OrderEvent> listSellExecutions(double limitPrice, double leavesVolume){
	ArrayList<OrderEvent> events = new ArrayList<>();
	
	int cumVolume = 0;
	for(SortedMap<Date, Orders> tm : orders.getOrders().values()) {
	    for(Orders bookOrder : tm.values()) {
		double priceToFill = bookOrder.getPrice();
		
		if(cumVolume < leavesVolume && priceToFill <= limitPrice){
		    //calcula o volume que falta para completar na ordem agressora
		    double vol = leavesVolume - cumVolume;
		    double volumeToFill = 0;
		    if(vol >= bookOrder.getLeavesVolume()){
			volumeToFill = bookOrder.getLeavesVolume();
		    }
		    else if(vol < bookOrder.getLeavesVolume()){
			volumeToFill = vol;
		    }

		    //cria a execucao para a ordem do book
		    OrderEvent fill = new OrderEvent(ExecutionTypes.TRADE, volumeToFill, priceToFill);
		    fill.setOrderID(bookOrder);
		    events.add(fill);
		}

	    }
	}
	
	return events;
    }
    
    
    /**
     * @return the stopQueue
     */
    public SortedMap<Double, SortedMap<Date, Orders>> getStopQueue() {
	return stopQueue;
    }

    public SortedMap<Date, Orders> getStops(double stopPrice){

	SortedMap<Date, Orders> stops = null;
	if(stopQueue.containsKey(stopPrice)) {
	    stops = stopQueue.get(stopPrice);
	    stopQueue.put(stopPrice, Collections.synchronizedSortedMap(new TreeMap<Date, Orders>()));
	}

	return stops;
    }

    
    public List<MDEntry> takeSnapshot(){
	ArrayList<MDEntry> mds = new ArrayList<>(500);
	synchronized(orders) {
	    for(SortedMap<Date, Orders> tm: orders.getOrders().values()) {
		for(Orders o : tm.values()) {
		    mds.add(createMBOEntry(o, null));
		}
	    }
	}
	//orders.forEach(o -> mds.add(createMBOEntry(o)));

	return mds;
    }


    public static void main(String[] args) throws OrderException{
	try {
	    Side s = Side.SELL;
	    BookTable bt = new BookTable(s);
	    Security sec = new Security("WDOJ17");
	    String clOrdID = "123456";
	    double price = 3272.5;
	    long orderID = 1;

	    bt.add(new Orders(orderID++, clOrdID+"a", sec, s, price, 1, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	    Thread.sleep(500);
	    bt.add(new Orders(orderID++, clOrdID+"b", sec, s, price, 1, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	    Thread.sleep(10);
	    bt.add(new Orders(orderID++, clOrdID+"c", sec, s, price-.5, 2, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	    Thread.sleep(5);
	    bt.add(new Orders(orderID++, clOrdID+"d", sec, s, price-1, 5, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	    Thread.sleep(10);
	    bt.add(new Orders(orderID++, clOrdID+"e", sec, s, price-1, 10, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	    Thread.sleep(50);
	    bt.add(new Orders(orderID++, clOrdID+"f", sec, s, price+2, 3, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	    Thread.sleep(50);
	    bt.add(new Orders(orderID++, clOrdID+"g", sec, s, price-1.5, 3, OrderTypes.Limit, TradeTypes.DAY_TRADE));

	    //bt.getOrders().forEach(o -> System.out.println(o));
	    int num = 100;
	    while(num-- >=0) {
		Thread.sleep(5);
		bt.add(new Orders(orderID++, clOrdID+num, sec, s, price-1.5, 3, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	    }


	    bt.getOrders().forEach(
		    (d,tm) -> 
		    tm.forEach(
			    (dt,ord) -> 
			    System.out.println(bt.orders.getOrderPosition(ord.getOrderID()) + " - " + ord)));

	    bt.orders.getOrdersSummary().values().stream().forEach(sm -> System.out.println(sm));
	}
	catch(InterruptedException e) {
	    e.printStackTrace();
	}
    }

}
