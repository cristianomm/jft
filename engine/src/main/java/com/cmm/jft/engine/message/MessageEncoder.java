package com.cmm.jft.engine.message;

import java.util.Date;

import com.cmm.jft.trading.OrderExecution;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderTypes;

import quickfix.Message;

public interface MessageEncoder {

	//[start]-------------------------------------------Session Specific
	Message heartbeat();

	Message logon(String authData, boolean resetSeqNum,
			String newPassword);

	
	Message logout(String text);

	Message reject(int refMsgSeqNum);

	Message resendRequest(int beginSeqNum, int endSeqNum);

	Message sequenceReset();

	Message testRequest();

	//[end]

	
	//[start]-------------------------------------------Application Specific
	Message allocationInstruction();

	Message allocationReport();
//
//	Message applicationMessageReport();
//
//	Message applicationMessageRequest();
//
//	Message applicationMessageRequestAck();

	Message businessMessageReject();

	Message executionReport(OrderExecution execution);

	Message newOrderCross();

	Message newOrderSingle(Orders order);

	Message orderCancelReject();

	Message orderCancelReplaceRequest();

	Message orderCancelRequest(String origClordID, String clOrdID, String symbol, 
			com.cmm.jft.trading.enums.Side side, double ordQty, String memo);

	Message positionMaintenanceReport();

	Message positionMaintenanceRequest();

	Message quote();

	Message quoteCancel();

	Message quoteRequest();

	Message quoteRequestReject();

	Message quoteStatusReport();

	Message securityDefinition();

	Message securityDefinitionRequest();
	//[end]

}