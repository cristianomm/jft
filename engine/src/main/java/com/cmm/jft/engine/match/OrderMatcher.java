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

    private class StopOrderReleaser implements Runnable {

	private SortedMap<Double, SortedMap<Date, Orders>> queue;

	public StopOrderReleaser(SortedMap<Double, SortedMap<Date, Orders>> queue) {
	    this.queue =  queue;
	}

	@Override
	public void run() {
	    while (verifyStopOrders) {
		//se contem osdens stop no nivel de preco
		if(queue.containsKey(lastPrice) && !queue.get(lastPrice).isEmpty()){
		    SortedMap<Date, Orders> stops = queue.get(lastPrice);
		    while(!stops.isEmpty()){
			try {
			    Orders stpOrd = stops.remove(stops.firstKey());
			    stpOrd.setWorkingIndicator(WorkingIndicator.Working);
			    stpOrd.changeToMarket();
			    //agride o mercado com a ordem stop
			    addOrder(stpOrd);

			} catch (OrderException e) {
			    e.printStackTrace();
			}
		    }
		}
	    }
	}
    }

    private boolean verifyStopOrders;
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

    private BookTable buyTable;
    private BookTable sellTable;

    public OrderMatcher(double protectionLevel, MarketDataChannel umdf, 
	    BookTable buyTable, BookTable sellTable) {
	this.umdf = umdf;
	this.verifyStopOrders = true;
	this.protectionLevel = protectionLevel;
	this.tradeIds = new IdGenerator(new Date());
	this.buyTable = buyTable;
	this.sellTable = sellTable;
	
	// inicializa os verificadores de ordens stop
	//new Thread(new StopOrderReleaser(buyTable.getStopQueue())).start();
	//new Thread(new StopOrderReleaser(sellTable.getStopQueue())).start();
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


    public boolean addOrder(Orders order) throws OrderException {
	boolean add = false;

	umdf.openNewPacket();
	//umdf.informNewOrder(order, positionNo);
	order.setWorkingIndicator(WorkingIndicator.Working);
	add = execute(order);

	umdf.closePacket();
	return add;
    }

    public void cancelOrder(Orders ordr, CancelTypes cancelType) throws OrderException {

	BookTable table = ordr.getSide() == Side.BUY? buyTable : sellTable;

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
	switch (ordr.getOrderType()) {
	case Market:
	    executed = executeMarketWithProtection(ordr);
	    break;
	case Limit:
	    executed = executeLimit(ordr);
	    break;
	case Stop:
	    adjustProtectionPrice(ordr);
	    executed = addOnBook(ordr);// nao executa stop mas sim adiciona no
	    // "stop book" e aguarda para ser ativada
	    break;
	case StopLimit:
	    executed = addOnBook(ordr);// nao executa stoplimit, deve inserir no
	    // stop book e aguardar o gatilho
	    break;
	case MarketWithLeftOverAsLimit:
	    executed = executeMarketToLimit(ordr);
	    break;
	    
	    default:
		break;
	    
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

    //private PriorityBlockingQueue<Orders> getCounterpartyBookOrders(Side side) {
	//return (side == Side.BUY) ? sellTable.getOrders() : buyTable.getOrders();
    //}

    private void adjustProtectionPrice(Orders ordr) {
	/*
	 * For bids, the protection price calculated is by adding an offset to
	 * the last trade price. For offers, the offset is subtracted from the
	 * last trade. The protection price cannot be specified in the incoming
	 * order.
	 */
	double offset = (lastPrice * protectionLevel) * (ordr.getSide() == Side.BUY ? 1 : -1);
	ordr.setProtectionPrice(lastPrice + offset);

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
	exec = generalExecute(ordr, ordr.getProtectionPrice(), ordr.getVolume());

	return exec;
    }

    private boolean executeLimit(Orders ordr) {
	boolean exec = false;

	exec = generalExecute(ordr, ordr.getPrice(), ordr.getVolume());

	return exec;
    }

    private boolean executeMarketToLimit(Orders ordr) {
	boolean exec = false;

	exec = generalExecute(ordr, ordr.getPrice(), ordr.getVolume());

	return exec;
    }

    private boolean generalExecute(Orders aggrOrdr, double orderPrice, double orderVolume) {
	boolean exec = false;

	BookTable table = null;

	if(aggrOrdr.getSide() == Side.BUY){
	    table = sellTable;
	}else{
	    table = buyTable;
	}

	List<OrderEvent> execs = table.getExecutions(aggrOrdr);

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
			bookOrdr.setWorkingIndicator(WorkingIndicator.Working);
			MDEntry[] entries = table.update(bookOrdr);
			umdf.informChangeOrder(entries[0], entries[1]);
		    }

		}

		// adiciona o restante da ordem recebida no book
		if (aggrOrdr.getOrderStatus() == OrderStatus.PARTIALLY_FILLED || aggrOrdr.getOrderStatus() == OrderStatus.NEW) {

		    if (aggrOrdr.getValidityType() == OrderValidityTypes.IOC ) {
			cancelOrder(aggrOrdr, CancelTypes.Expiration);
		    } else {
			// ajusta o tipo da ordem para Limit e adiciona o restante no book
			aggrOrdr.changeToLimit(orderPrice);
			exec = addOnBook(aggrOrdr);
		    }
		}
		
		//caso tenha realizado algum negocio, verifica se deve acionar ordens stop
		if(exec) {
		    releaseStopOrders(buyTable.getStops(lastPrice));
		    releaseStopOrders(sellTable.getStops(lastPrice));
		}
		
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
		    stpOrd.setWorkingIndicator(WorkingIndicator.Working);
		    stpOrd.changeToMarket();
		    //agride o mercado com a ordem stop
		    addOrder(stpOrd);

		} catch (OrderException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

    /**
     * Cria as execucoes para os valores passados por parametro, 
     * referenciando na execucao, a ordem que esta presente no book 
     * e que fara a contra-parte na transacao. Ainda nao removera do book 
     * as ordens que seram completamente executadas.
     * 
     * @param side
     * @param price
     * @param volume
     * @return	
     */
    /*
    private ArrayList<OrderEvent> createExecutions(Side side, double price, double limitPrice, double volume) {

	ArrayList<OrderEvent> lst = new ArrayList<OrderEvent>();
	TreeMap<Double, TreeMap<Date, Orders>> ordrs = getCounterpartyBookOrders(side);

	double cumVolume = 0;

	//busca o primeiro nivel de preco para a contraparte
	//para verificar se vai executar ou nao a ordem
	if((side == Side.BUY && (price <= ordrs.firstKey() && limitPrice <= ordrs.firstKey())) || 
		(side == Side.SELL && (price >= ordrs.firstKey() && limitPrice >= ordrs.firstKey()))){

	    for (TreeMap<Date, Orders> tm : ordrs. values()) {
		double priceLevel = tm.firstEntry().getValue().getPrice();
		//caso o preco da ordem no book esteja dentro do limite especificado
		if((side == Side.BUY && (price <= priceLevel && limitPrice <= priceLevel)) || 
			(side == Side.SELL && (price >= priceLevel && limitPrice >= priceLevel))){
		    //para este nivel de preco, percorre as ordens que estao ordenadas por tempo
		    //a fim de preencher a ordem agressora.
		    //enquanto ha ordens e nao preencheu o volume...
		    Iterator<Orders> ordrIter = tm.values().iterator(); 
		    while (ordrIter.hasNext() && cumVolume < volume) {
			Orders bookOrder = ordrIter.next();
			if (bookOrder.getWorkingIndicator() == WorkingIndicator.Working) {
			    bookOrder.setWorkingIndicator(WorkingIndicator.No_Working);
			    double volumeToFill = 0;
			    double priceToFill = bookOrder.getPrice();


			    //* a ordem que esta no book pode ter um volume maior, menor ou igual 
			    //* ao necessario para executar a ordem agressora.
			    //o volume da ordem agressora eh maior ou igual ao volume da ordem no book
			    if ((volume - cumVolume) >= bookOrder.getLeavesVolume()) {
				volumeToFill = bookOrder.getLeavesVolume();
			    } 
			    //o volume da ordem agressora eh menor que o volume restante da ordem no book
			    else if((volume - cumVolume) < bookOrder.getLeavesVolume()){
				volumeToFill = bookOrder.getLeavesVolume() - (volume - cumVolume);
			    }
			    //atualiza o volume acumulado para a ordem agressora.
			    cumVolume += volumeToFill;

			    //cria a execucao para a ordem do book
			    OrderEvent fill = new OrderEvent(ExecutionTypes.TRADE, volumeToFill, priceToFill);
			    fill.setOrderID(bookOrder);
			    lst.add(fill);
			}
		    }
		}
		else{
		    break;
		}
	    }
	}

	return lst;
    }*/



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
