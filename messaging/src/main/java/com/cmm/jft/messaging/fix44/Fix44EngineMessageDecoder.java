/**
 * 
 */
package com.cmm.jft.messaging.fix44;

import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.Message;
import quickfix.field.ClOrdID;
import quickfix.field.ExecType;
import quickfix.field.ExpireDate;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Price;
import quickfix.field.SecurityExchange;
import quickfix.field.Symbol;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReject;
import quickfix.fix44.OrderCancelReplaceRequest;
import quickfix.fix44.OrderCancelRequest;

import java.time.LocalDateTime;
import java.time.ZoneId;

import org.apache.log4j.Level;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.messaging.MessageDecoder;
import com.cmm.jft.security.Security;
//import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.logging.Logging;

/**
 * <p><code>Fix44EngineMessageDecoder.java</code> </p>
 * @author Cristiano M Martins
 * @version 11/08/2015 13:49:32
 */
public class Fix44EngineMessageDecoder implements MessageDecoder {



    private void addPartyIDs(Orders order, Message message) throws FieldNotFound {
	for(Group g: message.getGroups(453)) {
	    String partyID = g.getString(448);
	    char partIDSrc = g.getChar(447);//always 'D'
	    int partyRole = g.getInt(452);
	    /*
	    4 - Clearing Firm
	    5 - Investor Id
	    7 - Entering Firm
	    12 - Executing Trader
	    36 - Entering Trader
	    54 - Sender Location
	    76 - Desk ID
	    1001 - Order Origination Session
	     */
	    switch(partyRole) {
	    case 4:
		break;
	    case 5:
		break;
	    case 7:
		order.setBrokerID(partyID);
		break;
	    case 12:
		break;
	    case 36:
		order.setTraderID(partyID);
		break;
	    case 54:
		order.setSenderLocation(partyID);
		break;
	    case 76:
		break;
	    case 1001:
		break;
	    }

	}
    }







    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#heartbeat()
     */
    @Override
    public Message heartbeat() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#logon(java.lang.String, boolean, java.lang.String)
     */
    @Override
    public Message logon(String authData, boolean resetSeqNum,
	    String newPassword) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#logout(java.lang.String)
     */
    @Override
    public Message logout(String text) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#reject(int)
     */
    @Override
    public Message reject(int refMsgSeqNum) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#resendRequest(int, int)
     */
    @Override
    public Message resendRequest(int beginSeqNum, int endSeqNum) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#sequenceReset()
     */
    @Override
    public Message sequenceReset() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#testRequest()
     */
    @Override
    public Message testRequest() {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#allocationInstruction(quickfix.Message)
     */
    @Override
    public Message allocationInstruction(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#allocationReport(quickfix.Message)
     */
    @Override
    public Message allocationReport(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#businessMessageReject(quickfix.Message)
     */
    @Override
    public Message businessMessageReject(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#executionReport(quickfix.Message)
     */

    public OrderEvent executionReport(Message message) {
	OrderEvent event = null;

	if(message instanceof ExecutionReport) {
	    ExecutionReport er = (ExecutionReport) message;

	    try {
		event = new OrderEvent(
			ExecutionTypes.getByValue(er.getExecType().getValue()), 
			er.getTransactTime().getValue(), 
			er.getOrderQty().getValue(), er.getPrice().getValue()
			);
		event.setOrderStatus(OrderStatus.getByValue(er.getChar(39)));
		
		ExecutionTypes execType = event.getExecutionType();

		switch(execType) {
		case NEW:
		case CANCELED:
		    break;
		case REPLACE:
		    if(er.isSetField(40)) {
			
		    }
		    if(er.isSetField(99)) {
			event.setStopPrice(er.getDouble(99));
		    }
		    
		    if(er.isSetField(110)) {
			event.setMinQty(er.getDouble(110));
		    }
		    if(er.isSetField(111)) {
			event.setMaxFloor(er.getDouble(111));
		    }
		    break;
		case REJECTED:
		    event.setOrdRejReason(er.getInt(103));
		    break;
		case SUSPENDED:
		    break;
		case EXPIRED:
		    break;
		case RESTATED:
		    break;
		case TRADE:
		    event.setContraBroker(er.getString(375));
		    event.setCumQty(er.getDouble(14));
		    event.setLastQty(er.getDouble(32));
		    event.setLeavesQty(er.getDouble(151));
		    event.setTradeID(er.getString(6032));
		    break;
		case TRADE_CANCEL:
		    break;
		}

	    }catch(FieldNotFound e) {

	    }

	}

	return event;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#newOrderCross(quickfix.Message)
     */
    @Override
    public Orders newOrderCross(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#newOrderSingle(quickfix.Message)
     */
    @Override
    public Orders newOrderSingle(Message message) {
	Orders ordr = null;

	try {
	    if(message instanceof NewOrderSingle) {
		ordr = new Orders();
		NewOrderSingle orderSingle = (NewOrderSingle) message;
		ordr.setClOrdID(orderSingle.getClOrdID().getValue());

		addPartyIDs(ordr, orderSingle);

		//ordr.setSecurityID(SecurityService.getInstance().provideSecurity(orderSingle.getSecurityID().getValue()));
		ordr.setSide(Side.getByValue(orderSingle.getSide().getValue()));
		ordr.setOrderDateTime(orderSingle.getTransactTime().getValue());
		ordr.setVolume(orderSingle.getOrderQty().getValue());
		ordr.setOrderType(OrderTypes.getByValue(orderSingle.getOrdType().getValue()));
		ordr.setPrice(orderSingle.getPrice().getValue());
		ordr.setValidityType(OrderValidityTypes.getByValue(orderSingle.getTimeInForce().getValue()));
		ordr.setDuration(((DateTimeFormatter)FormatterFactory.getFormatter(FormatterTypes.DATE_F9)).parse(orderSingle.getExpireDate().getValue()));
		ordr.setComment(orderSingle.getString(5149));
	    }
	    else {
		throw new Exception("Message is not fix44.NewOrderSingle: " + message); 
	    }
	}catch(FieldNotFound e) {
	    ordr = null;
	} catch(Exception e) {
	    ordr = null;
	}

	return ordr;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#orderCancelReject(quickfix.Message)
     */
    @Override
    public OrderEvent orderCancelReject(Message message) {
	OrderCancelReject cancelReject = (OrderCancelReject) message;
	OrderEvent event = new OrderEvent(); 

	// TODO Auto-generated method stub
	return event;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#orderCancelReplaceRequest(quickfix.Message)
     */
    @Override
    public Orders orderCancelReplaceRequest(Message message) {

	OrderCancelReplaceRequest request = (OrderCancelReplaceRequest) message;
	Orders ordr = new Orders();
	try{
	    ordr.setClOrdID(request.getClOrdID().getValue());
	    //ordr.setOrigClOrdID(request.getOrigClOrdID().getValue());
	    ordr.setMaxFloor(request.getMaxFloor().getValue());
	    //ordr.setSecurityID(SecurityService.getInstance().provideSecurity(request.getSymbol().getValue()));
	    ordr.setSide(Side.getByValue(request.getSide().getValue()));
	    ordr.setVolume(request.getOrderQty().getValue());
	    ordr.setOrderType(OrderTypes.getByValue(request.getOrdType().getValue()));
	    ordr.setPrice(request.getPrice().getValue());
	    ordr.setStopPrice(request.getStopPx().getValue());
	}catch(FieldNotFound e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

	return ordr;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#orderCancelRequest(quickfix.Message)
     */
    @Override
    public Orders orderCancelRequest(Message message) {
	OrderCancelRequest request = (OrderCancelRequest) message;
	Orders ordr = new Orders();
	try{
	    ordr.setClOrdID(request.getClOrdID().getValue());
	    ordr.setOrigClOrdID(request.getOrigClOrdID().getValue());

	    addPartyIDs(ordr, request);

	    //ordr.setSecurityID(SecurityService.getInstance().provideSecurity(request.getSymbol().getValue()));
	    ordr.setSide(Side.getByValue(request.getSide().getValue()));
	    ordr.setVolume(request.getOrderQty().getValue());
	}catch(FieldNotFound e){
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

	return ordr;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#positionMaintenanceReport(quickfix.Message)
     */
    @Override
    public Message positionMaintenanceReport(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#positionMaintenanceRequest(quickfix.Message)
     */
    @Override
    public Message positionMaintenanceRequest(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#quote(quickfix.Message)
     */
    @Override
    public Message quote(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#quoteCancel(quickfix.Message)
     */
    @Override
    public Message quoteCancel(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#quoteRequest(quickfix.Message)
     */
    @Override
    public Message quoteRequest(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#quoteRequestReject(quickfix.Message)
     */
    @Override
    public Message quoteRequestReject(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#quoteStatusReport(quickfix.Message)
     */
    @Override
    public Message quoteStatusReport(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#securityDefinition(quickfix.Message)
     */
    @Override
    public Message securityDefinition(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.engine.message.MessageDecoder#securityDefinitionRequest(quickfix.Message)
     */
    @Override
    public Message securityDefinitionRequest(Message message) {
	// TODO Auto-generated method stub
	return null;
    }

}
