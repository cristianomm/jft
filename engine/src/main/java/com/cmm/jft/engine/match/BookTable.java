/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NavigableMap;
import java.util.PriorityQueue;
import java.util.TreeMap;
import java.util.concurrent.PriorityBlockingQueue;

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

    private class Summary{
	double price;
	int orderCount;
	double orderVolume;

	/**
	 * 
	 */
	public Summary(double price, int numOrders, double volume) {
	    this.orderCount = numOrders;
	    this.orderVolume = volume;
	}

	public void incrementOrder(){
	    orderCount++;
	}

	public void incrementVolume(double volume){
	    this.orderVolume += volume;
	}

	public void decrementOrder(){
	    if(orderCount>0){
		orderCount--;
	    }
	}

	public void decrementVolume(double volume){
	    if(orderVolume >= volume){
		orderVolume -= volume;
	    }
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Summary clone() throws CloneNotSupportedException {
	    return new Summary(this.price, this.orderCount, this.orderVolume);
	}
    }

    private Side side;
    private PriorityBlockingQueue<Orders> orders;
    private TreeMap<Double, Summary> ordersSummary;
    private TreeMap<Double, TreeMap<Date, Orders>> stopQueue;


    /**
     * @param buy 
     * @param capacity
     */
    public BookTable(Side side) {
	this.side = side;
	this.stopQueue = new TreeMap<Double, TreeMap<Date, Orders>>();
	this.ordersSummary = new TreeMap<>();
	this.orders = new PriorityBlockingQueue<>(10000, new PriceTimeComparator());
    }
    
    /**
     * @return the orders
     */
    public PriorityBlockingQueue<Orders> getOrders() {
	return orders;
    }
    

    private void saveSnapshot(){


    }

    private void snapshotMBO(){
	MDEntry entry = new MDEntry();

    }

    private void snapshotMBP(){

    }
    
    private MDEntry createMBOEntry(Orders ordr){
	MDEntry entry = new MDEntry();
	
	
	return entry;
    }

    private int getOrderPositionMBO(Orders order){
	//calcula a posicao da ordem
	int position=0;
	boolean find = false;
	for(Orders ordr : orders){
	    position++;
	    if(ordr.getClOrdID().equals(order.getClOrdID())){
		find = true;
	    }
	}
	
	position = find?position:-1;
	
	return position;
    }
    
    private int getOrderPositionMBP(double price){
	int position = 0;
	for(Double level : ordersSummary.keySet()){
	    position++;
	    if(level == price){
		break;
	    }
	}
	return position;
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
//	    if (!ordersQueue.containsKey(order.getPrice())) {
//		ordersQueue.put(order.getPrice(), (TreeMap<Date, Orders>) Collections.synchronizedMap(new TreeMap<Date, Orders>()));
//	    }
	    //adiciona a ordem
	    orders.add(order);//Queue.get(order.getPrice()).put(order.getOrderDateTime(), order);
	    
	    MDEntry mboEntry = new MDEntry();
	    if(side == Side.BUY){
		mboEntry.setMdEntryBuyer(order.getBrokerID());
	    }else{
		mboEntry.setMdEntrySeller(order.getBrokerID());
	    }
	    mboEntry.setMdEntryDate(order.getInsertDate());
	    mboEntry.setMdEntryTime(order.getInsertTime());
	    mboEntry.setMdEntryType(side == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);
	    mboEntry.setMdUpdateAction(UpdateActions.New);
	    mboEntry.setMdEntryPx(order.getPrice());
	    mboEntry.setMdEntrySize((int) order.getVolume());
	    mboEntry.setMdEntryPosNo(getOrderPositionMBO(order));
	    entries[0] = mboEntry;
	    
	    
	    Summary sum = null;
	    if(!ordersSummary.containsKey(order.getPrice())){
		sum = new Summary(order.getPrice(), 1, order.getLeavesVolume());
		ordersSummary.put(order.getPrice(), sum);
	    }else{
		sum = ordersSummary.get(order.getPrice());
		sum.incrementOrder();
		sum.incrementVolume(order.getLeavesVolume());
	    }
	    
	    MDEntry mbpEntry = new MDEntry();
	    mbpEntry.setMdEntryDate(order.getInsertDate());
	    mbpEntry.setMdEntryTime(order.getInsertTime());
	    mbpEntry.setMdEntryType(side == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);
	    mbpEntry.setMdUpdateAction(UpdateActions.New);
	    mbpEntry.setMdEntryPx(order.getPrice());
	    mbpEntry.setMdEntrySize((int) sum.orderVolume);
	    mbpEntry.setNumberOfOrders(sum.orderCount);
	    mbpEntry.setMdEntryPosNo(getOrderPositionMBP(order.getPrice()));
	    entries[1] = mbpEntry;
	}
	
	return entries;
	
    }


    public MDEntry[] remove(Orders order){
	MDEntry[] entries = new MDEntry[2];
	if(ordersSummary.containsKey(order.getPrice())){
	    //--------------------------------------------------MBO
	    MDEntry mboEntry = new MDEntry();
	    if(side == Side.BUY){
		mboEntry.setMdEntryBuyer(order.getBrokerID());
	    }else{
		mboEntry.setMdEntrySeller(order.getBrokerID());
	    }
	    mboEntry.setMdEntryDate(order.getInsertDate());
	    mboEntry.setMdEntryTime(order.getInsertTime());
	    mboEntry.setMdEntryType(side == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);
	    mboEntry.setMdUpdateAction(UpdateActions.Delete);
	    mboEntry.setMdEntryPx(order.getPrice());
	    mboEntry.setMdEntrySize((int) order.getVolume());
	    mboEntry.setMdEntryPosNo(getOrderPositionMBO(order));
	    entries[0] = mboEntry;
	    
	    //remove a ordem
	    orders.remove(order);
	    
	    
	    //--------------------------------------------------MBP
	    Summary sum = ordersSummary.get(order.getPrice());
	    sum.decrementOrder();
	    sum.decrementVolume(order.getLeavesVolume());
	    
	    MDEntry mbpEntry = new MDEntry();
	    mbpEntry.setMdEntryDate(order.getInsertDate());
	    mbpEntry.setMdEntryTime(order.getInsertTime());
	    mbpEntry.setMdEntryType(side == Side.BUY? MDEntryTypes.BID: MDEntryTypes.OFFER);
	    mbpEntry.setMdUpdateAction(UpdateActions.Delete);
	    mbpEntry.setMdEntryPosNo(getOrderPositionMBP(order.getPrice()));
	    entries[1] = mbpEntry;
	}
	return entries;
    }

    public MDEntry[] update(Orders order){
	
	MDEntry[] entries = new MDEntry[2];
	if(order.getSide() == side){
	    
	    
	    
	}
	return entries;
    }

    public List<OrderEvent> getExecutions(Orders aggrOrder){

	ArrayList<OrderEvent> events = new ArrayList<>();

	int cumVolume = 0;
	for(Orders bookOrder :orders){
	    if (bookOrder.getWorkingIndicator() == WorkingIndicator.Working) {
		bookOrder.setWorkingIndicator(WorkingIndicator.No_Working);

		double priceToFill = bookOrder.getPrice();

		if(cumVolume < aggrOrder.getLeavesVolume() && priceToFill <= aggrOrder.getLimitPrice()){
		    //calcula o volume que falta para completar na ordem agressora
		    double vol = aggrOrder.getLeavesVolume() - cumVolume;
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
    public TreeMap<Double, TreeMap<Date, Orders>> getStopQueue() {
	return stopQueue;
    }
    
    public TreeMap<Date, Orders> getStops(double stopPrice){
	
	return stopQueue.get(stopPrice);
    }
    
    public void createSnapshot(){
	
    }

    public static void main(String[] args) throws OrderException{
	Side s = Side.BUY;
	BookTable bt = new BookTable(s);
	Security sec = new Security("WDOJ17");
	String clOrdID = "123456";
	double price = 3272.5;
	bt.add(new Orders(clOrdID, sec, s, price, 1, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	bt.add(new Orders(clOrdID, sec, s, price, 1, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	bt.add(new Orders(clOrdID, sec, s, price-.5, 2, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	bt.add(new Orders(clOrdID, sec, s, price-1, 5, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	bt.add(new Orders(clOrdID, sec, s, price-1, 10, OrderTypes.Limit, TradeTypes.DAY_TRADE));
	bt.add(new Orders(clOrdID, sec, s, price, 3, OrderTypes.Limit, TradeTypes.DAY_TRADE));


    }

}
