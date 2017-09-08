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
    private IdGenerator eventIds;

    private MarketDataChannel umdf;

    private OrdersTable buyTable;
    private OrdersTable sellTable;

    public OrderMatcher(double protectionLevel, MarketDataChannel umdf, 
	    OrdersTable buyTable, OrdersTable sellTable) {
	this.umdf = umdf;
	this.protectionLevel = protectionLevel;
	this.tradeIds = new IdGenerator(new Date());
	this.eventIds = new IdGenerator(new Date());
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


    public void match(Orders order) {

	try {
	    switch (order.getOrderType()) {
	    case Market:
		matchMarketWithProtection(order);
		break;
	    case Limit:
		matchLimit(order);
		break;
		//	    case Stop:
		//	    case StopLimit:
		//		/*
		//		 * Nao executa stop mas adiciona no
		//		 * "stop book" e aguarda gatilho para ser ativada
		//		 */
		//		addStop(order);
		//		break;
	    case MarketWithLeftOverAsLimit:
		matchMarketToLimit(order);
		break;

	    default:
		break;

	    }

	    releaseStopOrders(buyTable.getStopQueue().get(lastPrice));
	    releaseStopOrders(sellTable.getStopQueue().get(lastPrice));

	}catch(OrderException e) {
	    e.printStackTrace();
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

    }


    public void cancelOrder(Orders ordr, CancelTypes cancelType) throws OrderException {

	OrdersTable table = getTable(ordr);

	OrderEvent oe = new OrderEvent(ExecutionTypes.CANCELED, ordr.getVolume(), ordr.getPrice());
	switch(cancelType){
	case Expiration:
	    oe.setOrdRejReason(CancelTypes.Expiration.ordinal());
	    oe.setMessage("Order expired.");
	    sendExecutionReport(oe, ordr.getTraderID());
	    break;

	case Invalid:
	    oe.setMessage("Order Canceled due to invalid execution.");
	    oe.setOrdRejReason(CancelTypes.Invalid.ordinal());
	    sendExecutionReport(oe, ordr.getTraderID());
	    break;

	case Trade:
	    oe.setMessage("Order removed from book.");
	    oe.setOrdRejReason(CancelTypes.Trade.ordinal());
	    break;

	case Requested:
	    sendExecutionReport(oe, ordr.getTraderID());
	    break;

	}
	ordr.addExecution(oe);
	int orderPos = table.getOrderPosition(ordr.getOrderID());
	int pricePos = table.getPricePosition(ordr.getPrice());
	
	//remove da tabela do book
	table.remove(ordr.getOrderID());

	Summary sm = table.findSummary(ordr.getPrice());
	UpdateActions mbpAction = sm == null || sm.getOrderCount() == 0? UpdateActions.Delete:UpdateActions.Change;
	
	
	umdf.informDeleteOrder(
		umdf.createMBOEntry(ordr, UpdateActions.Delete, orderPos), 
		umdf.createMBPEntry(mbpAction, sm, pricePos));

    }

    public void changeOrder(Orders bookOrder) {
	//OrdersTable table = getTable(bookOrder);
	//MDEntry[] entries = table. update(bookOrder);
	//umdf.informChangeOrder(entries[0], entries[1]);
    }


    private boolean fillOrders(Orders newOrder, Orders bookOrder, double qtyToFill, double priceToFill) {
	boolean fill = false;
	if(qtyToFill >0 && priceToFill >0 
		&& (newOrder.getOrderStatus() == OrderStatus.NEW || 
		newOrder.getOrderStatus() == OrderStatus.PARTIALLY_FILLED || 
		newOrder.getOrderStatus() == OrderStatus.REPLACED)
		&&(bookOrder.getOrderStatus() == OrderStatus.NEW || 
		bookOrder.getOrderStatus() == OrderStatus.PARTIALLY_FILLED || 
		bookOrder.getOrderStatus() == OrderStatus.REPLACED)
		) {
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

		    //orderFill.setCumQty(newOrder.getExecutedVolume());
		    //orderFill.setLeavesQty(newOrder.getLeavesVolume());
		    orderFill.setLastQty(qtyToFill);


		    //bookFill.setCumQty(bookOrder.getExecutedVolume());
		    //bookFill.setLeavesQty(bookOrder.getLeavesVolume());
		    bookFill.setLastQty(qtyToFill);


		    // envia os executionReport para os participantes
		    // executionreport da ordem agressora
		    sendExecutionReport(orderFill, newOrder.getTraderID());

		    // executionreport da ordem que estava no book
		    sendExecutionReport(bookFill, bookOrder.getTraderID());

		    MDEntry trade = new MDEntry();
		    trade.setTradeID(tradeIds.nextNumericString());
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
		    fill = true;
		} else {
		    fill = false;
		    throw new OrderException("Error on add executions.");
		}

	    } catch (OrderException e) {
		e.printStackTrace();
		fill = false;
		Logging.getInstance().log(getClass(), e, Level.ERROR);
	    }
	}
	return fill;
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


    private OrdersTable getTable(Orders ordr) {
	OrdersTable table = null;
	if(ordr.getSide() == Side.BUY){
	    table = buyTable;
	}else{
	    table = sellTable;
	}
	return table;
    }

    private OrdersTable oppositeTable(Orders ordr) {
	OrdersTable table = null;
	if(ordr.getSide() == Side.BUY){
	    table = sellTable;
	}else{
	    table = buyTable;
	}
	return table;
    }

    private void restateOrder(Orders order) {
	try {
	    if(order.getMaxFloor() > 0  
		    && order.getOrderStatus() == OrderStatus.PARTIALLY_FILLED
		    && (order.getMaxFloor() % order.getLeavesVolume()) == 0) {

		OrderEvent restate = new OrderEvent();
		restate.setOrderID(order);
		restate.setEventDateTime(new Date());
		restate.setExecutionType(ExecutionTypes.RESTATED);

		order.addExecution(restate);
		getTable(order).restate(order);

		sendExecutionReport(restate, order.getTraderID());
	    }
	}
	catch(OrderException e) {

	}
    }


    private void matchDay(Orders aggrOrder, Orders bookOrder, double fillPrice) throws OrderException {

	double qtyToFill = 0;
	if(aggrOrder.getMinVolume()>0) {
	    if((qtyToFill = getMinVolume(aggrOrder, bookOrder))<=0) {
		cancelOrder(aggrOrder, CancelTypes.Expiration);
	    }
	}else {//nao ha qty minima, executa com o que existe no book
	    qtyToFill = bookOrder.getLeavesVolume() < aggrOrder.getLeavesVolume() ? 
		    bookOrder.getLeavesVolume() : 
			aggrOrder.getLeavesVolume();
	}

	fillOrders(aggrOrder, bookOrder, qtyToFill, fillPrice);
    }

    private void matchGTD(Orders aggrOrder, Orders bookOrder) {

    }

    private void matchGTC(Orders aggrOrder, Orders bookOrder) {

    }

    private void matchIOC(Orders aggrOrder, Orders bookOrder, double fillPrice) throws OrderException {
	double qtyToFill = 0;

	if(aggrOrder.getMinVolume()>0) {
	    if((qtyToFill = getMinVolume(aggrOrder, bookOrder))<=0) {
		cancelOrder(aggrOrder, CancelTypes.Expiration);
	    }
	}else {
	    //executa com a quantidade existente no book
	    qtyToFill = bookOrder.getLeavesVolume() < aggrOrder.getLeavesVolume() ? 
		    bookOrder.getLeavesVolume() : 
			aggrOrder.getLeavesVolume();
	}

	fillOrders(aggrOrder, bookOrder, qtyToFill, fillPrice);

	//caso a ordem agressora nao execute completamente, deve entao ser cancelada
	if(aggrOrder.getLeavesVolume() >0) {
	    cancelOrder(aggrOrder, CancelTypes.Expiration);
	}


    }

    private void matchFOK(Orders aggrOrder, Orders bookOrder, double fillPrice) throws OrderException {
	double qtyToFill = 0;
	//caso a ordem no book deve preencher toda a ordem agressora
	if(aggrOrder.getVolume() <= bookOrder.getLeavesVolume()) {
	    if(aggrOrder.getMinVolume()>0) {
		if((qtyToFill = getMinVolume(aggrOrder, bookOrder))<=0) {
		    cancelOrder(aggrOrder, CancelTypes.Expiration);
		}
	    }else {
		/*
		 * nao ha qty minima, executa com a qty da ordem agressora 
		 * pois a ordem que esta no book possui qty suficiente para executar a ordem
		 */
		qtyToFill = aggrOrder.getLeavesVolume();
	    }

	    fillOrders(aggrOrder, bookOrder, qtyToFill, fillPrice);

	}else {
	    //cancela a ordem pois nao ha contraparte com qty suficiente
	    cancelOrder(aggrOrder, CancelTypes.Expiration);
	}

    }

    private void matchMOC(Orders aggrOrder, Orders bookOrder) {

    }

    private void matchMOA(Orders aggrOrder, Orders bookOrder) {

    }


    /**
     * Recupera a quantidade minima de execucao para a ordem agressora. Caso a ordem
     * agressora possua quantidade minima maior que a quantidade existente no book,
     * metodo retornara 0.
     * @param aggrOrder ordem agressora
     * @param bookOrder ordem que esta no book
     * @return a quantidade possivel de ser executada conforme a quantidade existente no book
     * ou zero caso nao possua quantidade minima suficiente para a execucao da ordem agressora.
     * @throws OrderException
     */
    private double getMinVolume(Orders aggrOrder, Orders bookOrder) {
	double qtyToFill = 0;
	if(aggrOrder.getMinVolume() <= bookOrder.getLeavesVolume()) {
	    //caso a ordem do book tenha qty menor que qty restante na ordem agressora
	    if(bookOrder.getLeavesVolume()>aggrOrder.getLeavesVolume()) {
		qtyToFill = bookOrder.getLeavesVolume();
	    }else {
		//caso a ordem do book tenha mais que a ordem agressora
		qtyToFill = aggrOrder.getLeavesVolume();
	    }
	}else {
	    //neste caso nao existe qty minima para a execucao, a ordem deve ser cancelada
	    qtyToFill = 0;
	}
	return qtyToFill;
    }

    /**
     * 
     * @param ordr
     * @return
     * @throws OrderException 
     */
    private boolean matchLimit(Orders order) throws OrderException {
	boolean exec = false;

	OrdersTable table = oppositeTable(order);
	Orders bookOrder = table.getFirst();

	while(bookOrder!=null &&
		(order.getOrderStatus() == OrderStatus.NEW 
		|| order.getOrderStatus() == OrderStatus.PARTIALLY_FILLED
		||order.getOrderStatus() == OrderStatus.REPLACED)
		&& order.getPrice() == bookOrder.getPrice()
		) {

	    //utiliza o preco limite de execucao
	    double fillPrice = order.getPrice();

	    switch(order.getValidityType()) {
	    case DAY:
	    case GTC:
	    case GTD:
		matchDay(order, bookOrder, fillPrice);
		break;
	    case FOK:
		matchFOK(order, bookOrder, fillPrice);
		break;
	    case IOC:
		matchIOC(order, bookOrder, fillPrice);
		break;
	    }

	    //no caso de alguma ordem ser iceberg
	    restateOrder(order);
	    restateOrder(bookOrder);

	    //busca a proxima ordem para tentar executar
	    bookOrder = table.getFirst();
	}

	return exec;
    }

    /**
     * Execute Market type orders received;
     * 
     * @param ordr
     *            order to execute;
     * @throws OrderException 
     */
    private boolean matchMarketWithProtection(Orders aggrOrder) throws OrderException {
	boolean exec = false;

	adjustProtectionPrice(aggrOrder);
	OrdersTable table = oppositeTable(aggrOrder);
	Orders bookOrder = table.getFirst();

	while(
		(aggrOrder.getOrderStatus() == OrderStatus.NEW 
		|| aggrOrder.getOrderStatus() == OrderStatus.PARTIALLY_FILLED
		||aggrOrder.getOrderStatus() == OrderStatus.REPLACED)
		&& bookOrder.getPrice() <= aggrOrder.getProtectionPrice()
		) {
	    //ordem a mercado, utiliza o preco da ordem que esta no book
	    double fillPrice = bookOrder.getPrice();

	    switch(aggrOrder.getValidityType()) {
	    case DAY:
	    case GTC:
	    case GTD:
		matchDay(aggrOrder, bookOrder, fillPrice);
		break;
	    case FOK:
		matchFOK(aggrOrder, bookOrder, fillPrice);
		break;
	    case IOC:
		matchIOC(aggrOrder, bookOrder, fillPrice);
		break;
	    }

	    //no caso de alguma ordem ser iceberg
	    restateOrder(aggrOrder);
	    restateOrder(bookOrder);

	    //busca a proxima ordem para tentar executar
	    bookOrder = table.getFirst();
	}

	return exec;
    }

    /**
     * 
     * @param ordr
     * @return
     * @throws OrderException 
     */
    private boolean matchMarketToLimit(Orders aggrOrder) throws OrderException {

	//utiliza somente a primeira execucao

	/*
	 * apos a primeira execucao, ajusta a ordem para limit 
	 * e ajusta o preco da ordem para o preco do ultimo negocio executado 
	 */
	boolean exec = false;

	adjustProtectionPrice(aggrOrder);
	OrdersTable table = oppositeTable(aggrOrder);
	Orders bookOrder = table.getFirst();

	while(
		(aggrOrder.getOrderStatus() == OrderStatus.NEW 
		|| aggrOrder.getOrderStatus() == OrderStatus.PARTIALLY_FILLED
		||aggrOrder.getOrderStatus() == OrderStatus.REPLACED)
		&& bookOrder.getPrice() <= aggrOrder.getProtectionPrice()
		) {

	    //ordem a mercado, utiliza o preco da ordem que esta no book
	    double fillPrice = bookOrder.getPrice();

	    switch(aggrOrder.getValidityType()) {
	    case DAY:
	    case GTC:
	    case GTD:
		matchDay(aggrOrder, bookOrder, fillPrice);
		break;
	    case FOK:
		matchFOK(aggrOrder, bookOrder, fillPrice);
		break;
	    case IOC:
		matchIOC(aggrOrder, bookOrder, fillPrice);
		break;
	    }



	    //no caso de alguma ordem ser iceberg
	    restateOrder(aggrOrder);
	    restateOrder(bookOrder);

	    //busca a proxima ordem para tentar executar
	    bookOrder = table.getFirst();
	}

	return exec;

    }

    //    private boolean generalExecute(List<OrderEvent>execs, Orders aggrOrdr) {
    //	boolean exec = false;
    //
    //	try {
    //	    // verifica se pode executar a ordem
    //	    if (validateExecution(aggrOrdr, execs)) {
    //		for (OrderEvent ex : execs) {
    //		    // recupera a ordem que esta no book
    //		    Orders bookOrdr = ex.getOrderID();
    //
    //		    // adiciona as execucoes e as informa para os participantes
    //		    exec = fillOrders(aggrOrdr, bookOrdr, ex.getVolume(), ex.getPrice());
    //
    //		    // remove a ordem que estava no book caso ela tenha sido preenchida
    //		    if (bookOrdr.getOrderStatus() == OrderStatus.FILLED) {
    //			cancelOrder(bookOrdr, CancelTypes.Trade);
    //		    } else {//
    //			changeOrder(bookOrdr);
    //		    }
    //
    //		}
    //
    //		// adiciona o restante da ordem recebida no book
    //		if (aggrOrdr.getOrderStatus() == OrderStatus.PARTIALLY_FILLED || aggrOrdr.getOrderStatus() == OrderStatus.NEW) {
    //
    //		    if (aggrOrdr.getValidityType() == OrderValidityTypes.IOC ) {
    //			cancelOrder(aggrOrdr, CancelTypes.Expiration);
    //		    } else {
    //			// ajusta o tipo da ordem para Limit e adiciona o restante no book
    //			aggrOrdr.setOrderType(OrderTypes.Limit);
    //			exec = addOnBook(aggrOrdr);
    //		    }
    //		}
    //
    //		//caso tenha realizado algum negocio, verifica se deve acionar ordens stop
    //		releaseStopOrders(buyTable.getStops(lastPrice));
    //		releaseStopOrders(sellTable.getStops(lastPrice));
    //
    //	    } else {
    //		// cancela a ordem e informa que a ordem nao podera ser executada
    //		exec = false;
    //		cancelOrder(aggrOrdr, CancelTypes.Invalid);
    //	    }
    //
    //	} catch (OrderException e) {
    //	    e.printStackTrace();
    //	    Logging.getInstance().log(getClass(), e, Level.ERROR);
    //	}
    //
    //	return exec;
    //    }


    private void releaseStopOrders(SortedMap<Date, Orders> stops) throws OrderException {
	if(stops != null) {
	    while(!stops.isEmpty()){

		Orders stpOrd = stops.remove(stops.firstKey());

		if(stpOrd.getOrderType() == OrderTypes.Stop) {
		    adjustProtectionPrice(stpOrd);
		}

		//altera para limit e avisa ao participante
		stpOrd.setOrderType(OrderTypes.Limit);
		stpOrd.setWorkingIndicator(WorkingIndicator.Working);
		OrderEvent limEvnt = new OrderEvent(ExecutionTypes.NEW, stpOrd.getVolume(), stpOrd.getPrice());
		limEvnt.setOrderID(stpOrd);

		sendExecutionReport(limEvnt, stpOrd.getTraderID());

		//insere a ordem no book
		getTable(stpOrd).add(stpOrd);
	    }
	}
    }


    private boolean validateExecution(Orders ordr, List<OrderEvent> executions) {
	boolean validExecution = false;

	switch (ordr.getValidityType()) {
	case MOA:
	    validExecution = true;
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

    private void sendExecutionReport(OrderEvent event, String traderID)throws OrderException{
	event.setOrderEventID(eventIds.nextLong());

	SessionID session = SessionRepository.getInstance().getTraderSession(traderID);
	if(session != null ) {
	    Fix44EngineMessageEncoder encoder = (Fix44EngineMessageEncoder) MessageEncoder.getEncoder(session);
	    sendMessage(encoder.executionReport(event), session);
	}
    }

    @Override
    public boolean sendMessage(Message message, SessionID sessionID) {
	return MessageRepository.getInstance().addMessage(message, sessionID);
    }

}
