/**
 * 
 */
package com.cmm.jft.engine;

import static org.junit.Assert.assertTrue;

import java.time.Instant;
import java.util.Date;
import java.util.HashSet;

import org.apache.log4j.Level;

import com.cmm.jft.engine.marketdata.MarketDataChannel;
import com.cmm.jft.engine.marketdata.recovery.SnapshotRecoveryChannel;
import com.cmm.jft.engine.match.BookTable;
import com.cmm.jft.engine.match.OrderMatcher;
import com.cmm.jft.engine.match.OrdersTable;
import com.cmm.jft.marketdata.BandLimits;
import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.marketdata.MDSnapshot;
import com.cmm.jft.messaging.MessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.MessageSender;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.CancelTypes;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.MarketPhase;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.enums.WorkingIndicator;
import com.cmm.jft.trading.exceptions.OrderException;
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
    private MarketDataChannel umdf;
    private SnapshotRecoveryChannel snpr;

    private BandLimits bandLimits;

    private long orderIDs;

    private OrdersTable buyTable;
    private OrdersTable sellTable;

    private MarketPhase phase;
    private ErrorCodes errCodes = ErrorCodes.getInstance();

    public static void main(String args[]) {

	long orderID = 1;
	String symbol = "WDOV17";
	Security security = SecurityService.getInstance().provideSecurity(symbol);
	SessionID sessionID = new SessionID(FixVersions.BEGINSTRING_FIX44, "SENDER", "TARGET");

	Book book = new Book(symbol, .20);

	long t0 = System.currentTimeMillis();
	try {

	    Orders ord = new Orders(orderID++, "123456", security, Side.BUY, 
		    3321.5, 2, OrderTypes.Limit, TradeTypes.DAY_TRADE);
	    ord.setBrokerID("308");

	    boolean added = book.addOrder(ord, sessionID);

	    ord = new Orders(orderID++, "123455", security, Side.SELL, 
		    3321.5, 1, OrderTypes.Limit, TradeTypes.DAY_TRADE);
	    ord.setBrokerID("154");

	    added = book.addOrder(ord, sessionID);

	    System.out.println(System.currentTimeMillis() - t0);

	    double qt = -1000;
	    for(int i=0;i<qt;i++) {
		ord = new Orders(orderID++, "123457"+i, security, Side.BUY, 
			3321.5, 2, OrderTypes.Limit, TradeTypes.DAY_TRADE);
		ord.setBrokerID("308");

		added = added && book.addOrder(ord, sessionID);
	    }
	    long t1 = System.currentTimeMillis() - t0;
	    System.out.println("Tempo total: " + t1);
	    System.out.println("Tempo por ordem: " + t1/qt);

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

	this.orderIDs = Instant.now().toEpochMilli();

	this.buyTable = new OrdersTable(Side.BUY);
	this.sellTable = new OrdersTable(Side.SELL);
	this.snapshot = new MDSnapshot(security);

	double auctionBand = security.getSecurityInfoID().getAuctionBand();
	double rejectHiBand = security.getSecurityInfoID().getRejectHiBand();
	double rejectLoBand = security.getSecurityInfoID().getRejectLoBand();
	int qtyLimit = security.getSecurityInfoID().getMaxVolume();

	this.bandLimits = new BandLimits(0, auctionBand, rejectHiBand, rejectLoBand, qtyLimit);

	this.umdf = new MarketDataChannel(security);
	this.snpr = SnapshotRecoveryChannel.getInstance();

	this.orderMatcher = new OrderMatcher(this.protectionLevel, umdf, buyTable, sellTable);

	{
	    new Thread(new Runnable() {
		@Override
		public void run() {
		    while(true) {
			try {
			    takeSnapshot();
			    Thread.sleep(15 * 60);
			} catch (InterruptedException e) {
			    e.printStackTrace();
			}
		    }
		}
	    }).start();
	}
    }


    public Security getSecurity() {
	return security;
    }

    public void calculateAdjPrice() {

    }


    private boolean checkOrder(Orders order) throws OrderValidationException {

	boolean valid = true;
	try {
	    switch(security.getSecurityInfoID().getObjectAsset()) {
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
	    
	}catch(OrderValidationException e) {
	    valid = false;
	    throw new OrderValidationException(e.getErrorCode(), e.getErrorMsg(), e);
	}

	return valid;
    }

    private void checkCommon(Orders order) throws OrderValidationException{
	if(order.getClOrdID().isEmpty()) {
	    throw new OrderValidationException(1010, errCodes.getMessage(1010));
	}

	// verifica se a quantidade para o instrumento eh valida
	if((order.getVolume() % security.getSecurityInfoID().getMinVolume()) == 0) {
	    throw new OrderValidationException(993502, errCodes.getMessage(993502));
	}
	
	switch(order.getOrderType()) {
	case Market:
	case MarketWithLeftOverAsLimit:
	case Stop:
	    if(order.getPrice() != 0) {
		throw new OrderValidationException(992138, errCodes.getMessage(992138));
	    }
	    break;
	case Limit:
	case StopLimit:
	    if(order.getPrice() <=0 ) {
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
	    if(order.getValidityType() == OrderValidityTypes.MOC ||
	    order.getValidityType() == OrderValidityTypes.MOA) {
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
	    if(order.getValidityType() != OrderValidityTypes.DAY) {
		throw new OrderValidationException(7047, errCodes.getMessage(7047));
	    }
	    if(order.getMaxFloor() >0) {
		throw new OrderValidationException(7050, errCodes.getMessage(7050));
	    }
	    if(order.getMinVolume()>0) {
		throw new OrderValidationException(992129, errCodes.getMessage(992129));
	    }
	    
	    break;
	    
	case Limit:
	case MarketWithLeftOverAsLimit:
	    if(order.getValidityType() == OrderValidityTypes.MOC ||
	    order.getValidityType() == OrderValidityTypes.MOA) {
		throw new OrderValidationException(7047, errCodes.getMessage(7047));
	    }
	    break;
	    
	default:
	    throw new OrderValidationException(7047, errCodes.getMessage(7047));
	}

    }



    private void adjustOrderParameters(Orders order) throws OrderException {

	switch(order.getOrderType()) {
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

	order.setOrderStatus(OrderStatus.NEW);

	//ajusta parametros da ordem no recebimento da oferta
	Date insertDt = new Date();
	order.setInsertDate(insertDt);
	order.setInsertTime(insertDt);

	order.setOrderID(orderIDs++);
    }

    public boolean addOrder(Orders order, SessionID sessionID) {
	boolean added = false;
	try {
	    if (added = checkOrder(order)) {
		
		//ajusta os parametros da ordem
		adjustOrderParameters(order);
		
		//envia o execution report de ordem recebida
		sendExecutionReport(order, ExecutionTypes.NEW, "Order received.", 0, sessionID);
		
		//adiciona a ordem na tabela correspondente
		if(order.getSide() == Side.BUY) {
		    if(order.getOrderType() == OrderTypes.Stop || order.getOrderType() == OrderTypes.StopLimit) {
			buyTable.addStop(order);
		    }else {
			buyTable.add(order);
		    }
		}else {
		    if(order.getOrderType() == OrderTypes.Stop || order.getOrderType() == OrderTypes.StopLimit) {
			sellTable.addStop(order);
		    }else {
			sellTable.add(order);
		    }
		}
		
		// informa ao match engine a nova ordem
		added = added && orderMatcher.match(order);
	    }
	    else {
		sendExecutionReport(order, ExecutionTypes.REJECTED, errCodes.getMessage(7000), 7000, sessionID);
	    }

	} 
	
	catch (OrderException e) {
	    added = false;
	    sendExecutionReport(order, ExecutionTypes.REJECTED, errCodes.getMessage(7000), 7000, sessionID);
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	} catch(OrderValidationException e) {
	    added = false;
	    sendExecutionReport(order, ExecutionTypes.REJECTED, e.getErrorMsg(), e.getErrorCode(), sessionID);
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

	return added;
    }

    public void cancelOrder(Orders ordr, SessionID sessionID) {

	try {

	    orderMatcher.cancelOrder(ordr, CancelTypes.Requested);
	} catch (OrderException e) {
	    e.printStackTrace();
	}

    }

    public void replaceOrder(Orders ordr, SessionID sessionID) {
	try {

	    adjustOrderParameters(ordr);
	    if (ordr.getSide() == Side.BUY) {
		buyTable.update(ordr);
	    } else {
		entries = sellTable.update(ordr);
	    }

	    sendExecutionReport(ordr, ExecutionTypes.REJECTED, errCodes.getMessage(7000), 7000, sessionID);
	    
	    MDEntry[] entries = null;
	    if (ordr.getSide() == Side.BUY) {
		entries = buyTable.update(ordr);
	    } else {
		entries = sellTable.update(ordr);
	    }

	    if(entries == null) {
		sendExecutionReport(ordr, ExecutionTypes.REJECTED, errCodes.getMessage(7000), 7000, sessionID);
	    }

	    // TODO: enviar o estado atual do book com o que mudou do estado anterior



	}catch(OrderException e) {
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

    }

    public void closeBook() {

	// buyQueue.forEach(o -> cancelOrder(o));
	// sellQueue.forEach(o -> cancelOrder(o));

    }


    public void takeSnapshot() {
	snapshot.resetSnapshot(0);

	//add an snapshot for each order book
	buyTable.takeSnapshot().forEach(md -> snapshot.addOffer(md));
	sellTable.takeSnapshot().forEach(md -> snapshot.addOffer(md));

	//add information about this market
	snapshot.setOpenPrice(orderMatcher.getOpenPrice());
	snapshot.setClosePrice(orderMatcher.getClosePrice());
	snapshot.setHighPrice(orderMatcher.getHighPrice());
	snapshot.setLowPrice(orderMatcher.getLowPrice());
	snapshot.setVwapPrice(orderMatcher.getVwapPrice());
	snapshot.setTradeVolume(orderMatcher.getTotalVolume());
	snapshot.setFinancialVolume(orderMatcher.getFinancialVolume());

	snapshot.setLimits(bandLimits);

	//snapshot.(bandLimits.hardLimitHigh, bandLimits.hardLimitLow, 1, 0));
	//snapshot.(bandLimits.rejectionBandHigh, bandLimits.rejectionBandLow, 3, 2));
	//snapshot.(bandLimits.auctionBandHigh, bandLimits.auctionBandLow, 2, 2));
	//snapshot.(bandLimits.staticLimitHigh, bandLimits.staticLimitLow, 4, 0));
	//snapshot.(quantityLimit));
	snapshot.setPhase(phase);

	snpr.updateSnapshot(snapshot);

    }


    private void sendExecutionReport(Orders order, ExecutionTypes exec, String message, int ordRejReason, SessionID sessionID) {

	try {
	    OrderEvent oe = new OrderEvent(
		    exec, new Date(), order.getVolume(), order.getPrice()
		    );
	    oe.setMessage(message);
	    oe.setOrderID(order);
	    oe.setOrdRejReason(ordRejReason);
	    order.addExecution(oe);

	    sendMessage((
		    (Fix44EngineMessageEncoder) MessageEncoder.getEncoder(sessionID)).
		    executionReport(oe), sessionID);
	}catch(OrderException e) {
	    Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmm.jft.engine.message.MessageSender#sendMessage(quickfix.Message,
     * quickfix.SessionID)
     */
    @Override
    public boolean sendMessage(Message message, SessionID sessionID) {
	return MessageRepository.getInstance().addMessage(message, sessionID);
    }

}
