/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.Date;
import java.util.List;
import java.util.SortedMap;

import org.apache.log4j.Level;

import quickfix.Message;
import quickfix.SessionID;

import com.cmm.jft.engine.IdGenerator;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.marketdata.MarketDataChannel;
import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.messaging.MessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.MessageSender;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.CancelTypes;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.StreamTypes;
import com.cmm.jft.trading.enums.UpdateActions;
import com.cmm.jft.trading.enums.WorkingIndicator;
import com.cmm.jft.trading.exceptions.OrderException;
import com.cmm.logging.Logging;

/**
 * @author Cristiano M Martins
 * @version 17/08/15 11:17:42
 *
 */
public class OrderMatcher implements MessageSender {

    private double openPrice;
    private double closePrice;
    private double highPrice;
    private double lowPrice;
    private double lastPrice;
    private double vwapPrice;
    private double lastVolume;
    private int totalVolume;
    private int totalTrades;
    private double financialVolume;
    private double protectionLevel;

    private IdGenerator tradeIds;

    private MarketDataChannel umdf;

    private OrdersTable buyTable;
    private OrdersTable sellTable;

    public OrderMatcher(double protectionLevel, MarketDataChannel umdf, 
	    OrdersTable buyTable, OrdersTable sellTable) {
	this.umdf = umdf;
	this.protectionLevel = protectionLevel;
	this.tradeIds = new IdGenerator(new Date());
	this.buyTable = buyTable;
	this.sellTable = sellTable;
    }



    /**
     * @return the openPrice
     */
    public double getOpenPrice() {
	return openPrice;
    }

    /**
     * @return the closePrice
     */
    public double getClosePrice() {
	return closePrice;
    }

    /**
     * @return the highPrice
     */
    public double getHighPrice() {
	return this.highPrice;
    }

    /**
     * @return the lowPrice
     */
    public double getLowPrice() {
	return this.lowPrice;
    }

    public double getLastPrice() {
	return lastPrice;
    }

    /**
     * @return the vwapPrice
     */
    public double getVwapPrice() {
	return this.vwapPrice;
    }

    public double getLastVolume() {
	return lastVolume;
    }

    /**
     * @return the totalVolume
     */
    public int getTotalVolume() {
	return this.totalVolume;
    }

    /**
     * @return the financialVolume
     */
    public double getFinancialVolume() {
	return financialVolume;
    }

    /**
     * @return the totalTrades
     */
    public int getTotalTrades() {
	return this.totalTrades;
    }
    
    private void checkExecution() {
	
    }
    


    public boolean addOrder(Orders order) throws OrderException {
	boolean add = false;

	//umdf.openNewPacket();
	//umdf.informNewOrder(order, positionNo);
	//order.setWorkingIndicator(WorkingIndicator.Working);
	add = execute(order);

	//umdf.closePacket();
	return add;
    }

    public void cancelOrder(Orders ordr, CancelTypes cancelType) throws OrderException {

	OrdersTable table = ordr.getSide() == Side.BUY? buyTable : sellTable;

	OrderEvent oe = new OrderEvent(ExecutionTypes.CANCELED, ordr.getVolume(), ordr.getPrice());
	switch(cancelType){
	case Expiration:
	    oe.setOrdRejReason(CancelTypes.Expiration.ordinal());
	    oe.setMessage("Order expired.");
	    sendExecutionReport(ordr.getTraderID(), oe);
	    break;

	case Invalid:
	    oe.setMessage("Order Canceled due to invalid execution.");
	    oe.setOrdRejReason(CancelTypes.Invalid.ordinal());
	    sendExecutionReport(ordr.getTraderID(), oe);
	    break;

	case Trade:
	    oe.setMessage("Order removed from book.");
	    oe.setOrdRejReason(CancelTypes.Trade.ordinal());
	    break;

	case Requested:
	    sendExecutionReport(ordr.getTraderID(), oe);
	    break;

	}
	ordr.addExecution(oe);
	MDEntry[] entries = table.remove(ordr);
	umdf.informDeleteOrder(entries[0], entries[1]);

    }

    public void changeOrder(Orders bookOrder) {
	OrdersTable table = bookOrder.getSide() == Side.BUY? buyTable : sellTable;
	MDEntry[] entries = table.update(bookOrder);
	umdf.informChangeOrder(entries[0], entries[1]);
    }



    private boolean addStop(Orders order) throws Exception {
	boolean ret = false;
	order.setWorkingIndicator(WorkingIndicator.No_Working);
	if(order.getSide() == Side.BUY) {
	    buyTable.addStop(order);
	    ret = true;
	}else {
	    sellTable.addStop(order);
	    ret = true;
	}
	return ret;
    }

    private boolean addOnBook(Orders ordr) {
	boolean add = false;
	MDEntry[] entries = null;
	if (ordr.getSide() == Side.BUY) {
	    entries = buyTable.add(ordr);

	} else {
	    entries = sellTable.add(ordr);
	}

	if(entries[0] != null && entries[1] != null) {
	    add = true;
	    umdf.informNewOrder(entries[0], entries[1]);
	}

	return add;
    }

    private boolean execute(Orders ordr) {

	boolean executed = false;

	try {
	    switch (ordr.getOrderType()) {
	    case Market:
		executed = executeMarketWithProtection(ordr);
		break;
	    case Limit:
		executed = executeLimit(ordr);
		break;
	    case Stop:
	    case StopLimit:
		/*
		 * Nao executa stop mas adiciona no
		 * "stop book" e aguarda gatilho para ser ativada
		 */
		executed = addStop(ordr);
		break;
	    case MarketWithLeftOverAsLimit:
		executed = executeMarketToLimit(ordr);
		break;

	    default:
		break;

	    }
	}catch(Exception e) {
	    e.printStackTrace();
	}
	return executed;
    }

    private boolean fillOrders(Orders newOrder, Orders bookOrder, double qtyToFill, double priceToFill) {
	boolean send = false;

	OrderEvent orderFill = new OrderEvent(ExecutionTypes.TRADE, qtyToFill, priceToFill);
	OrderEvent bookFill = new OrderEvent(ExecutionTypes.TRADE, qtyToFill, priceToFill);
	try {
	    if (newOrder.addExecution(orderFill) && bookOrder.addExecution(bookFill)) {

		// ajusta os valores para ultima execucao
		lastPrice = priceToFill;
		lastVolume = qtyToFill;
		totalVolume += qtyToFill;
		totalTrades++;

		financialVolume += lastPrice * lastVolume;
		vwapPrice = financialVolume / totalVolume;

		if(highPrice < priceToFill){
		    highPrice = priceToFill;
		    umdf.informHighPrice(highPrice);
		}

		if(lowPrice < priceToFill){
		    lowPrice = priceToFill;
		    umdf.informLowPrice(lowPrice);
		}


		// envia os executionReport para os participantes
		// executionreport da ordem agressora
		sendExecutionReport(newOrder.getTraderID(), orderFill);

		// executionreport da ordem que estava no book
		sendExecutionReport(bookOrder.getTraderID(), bookFill);


		MDEntry trade = new MDEntry();

		trade.setTradeID(tradeIds.getNextNumeric()+"");
		if(newOrder.getSide() == Side.BUY){
		    trade.setMdEntryBuyer(newOrder.getBrokerID());
		    trade.setMdEntrySeller(bookOrder.getBrokerID());
		}else{
		    trade.setMdEntryBuyer(bookOrder.getBrokerID());
		    trade.setMdEntrySeller(newOrder.getBrokerID());
		}
		trade.setMdEntryPx(priceToFill);
		trade.setMdEntrySize((int)qtyToFill);
		trade.setMdUpdateAction(UpdateActions.New);
		trade.setTradeVolume((int) totalVolume);

		// cria um evento de trade e um de vwap para enviar o MD
		umdf.informTrade(trade);
		umdf.informVWAPPrice(vwapPrice);
		umdf.informTradeVolume(totalTrades, financialVolume, totalVolume);

	    } else {
		throw new OrderException("Error on add executions.");
	    }

	} catch (OrderException e) {
	    e.printStackTrace();
	    send = false;
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

	return send;
    }

    /**
     * For bids, the protection price calculated is by adding an offset to
     * the last trade price. For offers, the offset is subtracted from the
     * last trade. The protection price cannot be specified in the incoming
     * order.
     * @param ordr
     */
    private void adjustProtectionPrice(Orders ordr) {

	double protectPx = lastPrice + ((lastPrice * protectionLevel) * (ordr.getSide() == Side.BUY ? 1 : -1));

	ordr.setPrice(protectPx);
	ordr.setProtectionPrice(protectPx);
    }



    private BookTable getTable(Orders ordr) {
	BookTable table = null;
	if(ordr.getSide() == Side.BUY){
	    table = sellTable;
	}else{
	    table = buyTable;
	}
	return table;
    }

    /**
     * Execute Market type orders received;
     * 
     * @param ordr
     *            order to execute;
     */
    private boolean executeMarketWithProtection(Orders ordr) {
	boolean exec = false;

	adjustProtectionPrice(ordr);

	List<OrderEvent> execs = getTable(ordr).listExecutions(ordr.getProtectionPrice(), ordr.getLeavesVolume());
	exec = generalExecute(execs, ordr);

	return exec;
    }

    private boolean executeLimit(Orders ordr) {
	boolean exec = false;

	List<OrderEvent> execs = getTable(ordr).listExecutions(ordr.getPrice(), ordr.getLeavesVolume());
	exec = generalExecute(execs, ordr);

	return exec;
    }

    private boolean executeMarketToLimit(Orders ordr) {
	boolean exec = false;

	List<OrderEvent> execs = getTable(ordr).listExecutions(ordr.getPrice(), ordr.getLeavesVolume());
	//utiliza somente a primeira execucao
	execs = execs.subList(0, 1);
	exec = generalExecute(execs, ordr);
	
	/*
	 * apos a primeira execucao, ajusta a ordem para limit 
	 * e ajusta o preco da ordem para o preco do ultimo negocio executado 
	 */
	ordr.setOrderType(OrderTypes.Limit);
	ordr.setPrice(lastPrice);
	execs = getTable(ordr).listExecutions(ordr.getPrice(), ordr.getLeavesVolume());
	exec = generalExecute(execs, ordr);
	
	return exec;
    }

    private boolean generalExecute(List<OrderEvent>execs, Orders aggrOrdr) {
	boolean exec = false;

	try {
	    // verifica se pode executar a ordem
	    if (validateExecution(aggrOrdr, execs)) {
		for (OrderEvent ex : execs) {
		    // recupera a ordem que esta no book
		    Orders bookOrdr = ex.getOrderID();

		    // adiciona as execucoes e as informa para os participantes
		    exec = fillOrders(aggrOrdr, bookOrdr, ex.getVolume(), ex.getPrice());

		    // remove a ordem que estava no book caso ela tenha sido preenchida
		    if (bookOrdr.getOrderStatus() == OrderStatus.FILLED) {
			cancelOrder(bookOrdr, CancelTypes.Trade);
		    } else {//
			changeOrder(bookOrdr);
		    }

		}

		// adiciona o restante da ordem recebida no book
		if (aggrOrdr.getOrderStatus() == OrderStatus.PARTIALLY_FILLED || aggrOrdr.getOrderStatus() == OrderStatus.NEW) {

		    if (aggrOrdr.getValidityType() == OrderValidityTypes.IOC ) {
			cancelOrder(aggrOrdr, CancelTypes.Expiration);
		    } else {
			// ajusta o tipo da ordem para Limit e adiciona o restante no book
			aggrOrdr.setOrderType(OrderTypes.Limit);
			exec = addOnBook(aggrOrdr);
		    }
		}

		//caso tenha realizado algum negocio, verifica se deve acionar ordens stop
		releaseStopOrders(buyTable.getStops(lastPrice));
		releaseStopOrders(sellTable.getStops(lastPrice));

	    } else {
		// cancela a ordem e informa que a ordem nao podera ser executada
		exec = false;
		cancelOrder(aggrOrdr, CancelTypes.Invalid);
	    }

	} catch (OrderException e) {
	    e.printStackTrace();
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

	return exec;
    }


    private void releaseStopOrders(SortedMap<Date, Orders> stops) {
	if(stops != null) {
	    while(!stops.isEmpty()){
		try {
		    Orders stpOrd = stops.remove(stops.firstKey());

		    if(stpOrd.getOrderType() == OrderTypes.Stop) {
			adjustProtectionPrice(stpOrd);
		    }

		    stpOrd.setOrderType(OrderTypes.Limit);

		    //insere a ordem no book
		    addOrder(stpOrd);

		} catch (OrderException e) {
		    e.printStackTrace();
		}
	    }
	}
    }


    private boolean validateExecution(Orders ordr, List<OrderEvent> executions) {
	boolean validExecution = false;

	switch (ordr.getValidityType()) {
	case MOA:
	    break;
	case DAY:
	    validExecution = true;
	    break;
	case GTC:
	    validExecution = true;
	    break;
	case IOC:
	    double c = 0;
	    for (OrderEvent oe : executions) {
		c += oe.getVolume();
	    }
	    validExecution = (c <= ordr.getVolume());
	    break;
	case FOK:
	    c = 0;
	    for (OrderEvent oe : executions) {
		c += oe.getVolume();
	    }
	    validExecution = (c == ordr.getVolume());
	    break;
	case GTD:
	    validExecution = new Date().compareTo(ordr.getDuration()) <= 0;
	    break;
	case MOC:
	    validExecution = true;
	    break;
	}

	return validExecution;
    }

    private void sendExecutionReport(String traderID, OrderEvent event){
	//TODO: Faltou TraderID
	SessionID bookOrderSession = SessionRepository.getInstance().getSession(
		StreamTypes.ENTRYPOINT, traderID);
	Fix44EngineMessageEncoder encoder = (Fix44EngineMessageEncoder) MessageEncoder.getEncoder(bookOrderSession);
	sendMessage(encoder.executionReport(event), bookOrderSession);
    }

    @Override
    public boolean sendMessage(Message message, SessionID sessionID) {
	return MessageRepository.getInstance().addMessage(message, sessionID);
    }

}
