/**
 * 
 */
package com.cmm.jft.messaging;

import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.MDEntryType;

import com.cmm.jft.marketdata.MarketOrder;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;

/**
 * <p><code>MessageDecoder.java</code></p>
 * @author Cristiano M Martins
 * @version 17/06/2015 17:31:40
 *
 */
public interface MessageDecoder {


	static MessageDecoder getDecoder(SessionID sessionId) {

		MessageDecoder decoder = null;
		switch(sessionId.getBeginString()) {
		case "FIX.4.4":
			decoder = null;
			break;
		}
		
		return decoder;
	}


	//[start]-------------------------------------------Session Specific
	Message heartbeat();

	Message logon(String authData, boolean resetSeqNum, String newPassword);

	Message logout(String text);

	Message reject(int refMsgSeqNum);

	Message resendRequest(int beginSeqNum, int endSeqNum);

	Message sequenceReset();

	Message testRequest();

	//[end]


	//[start]-------------------------------------------Application Specific
	Message allocationInstruction(Message message);

	Message allocationReport(Message message);
	//
	//	Message applicationMessageReport(Message message);
	//
	//	Message applicationMessageRequest(Message message);
	//
	//	Message applicationMessageRequestAck(Message message);

	Message businessMessageReject(Message message);

	OrderEvent executionReport(Message message);

	Orders newOrderCross(Message message);

	Orders newOrderSingle(Message message);

	OrderEvent orderCancelReject(Message message);

	Orders orderCancelReplaceRequest(Message message);

	Orders orderCancelRequest(Message message);

	Message positionMaintenanceReport(Message message);

	Message positionMaintenanceRequest(Message message);

	Message quote(Message message);

	Message quoteCancel(Message message);

	Message quoteRequest(Message message);

	Message quoteRequestReject(Message message);

	Message quoteStatusReport(Message message);

	Message securityDefinition(Message message);

	Message securityDefinitionRequest(Message message);
	//[end]

}
