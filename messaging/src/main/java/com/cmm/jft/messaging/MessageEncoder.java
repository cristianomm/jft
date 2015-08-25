package com.cmm.jft.messaging;

import java.util.Date;

import com.cmm.jft.messaging.fix44.Fix44MessageEncoder;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.RejectTypes;

import quickfix.Message;
import quickfix.SessionID;

public interface MessageEncoder {


	static MessageEncoder getEncoder(SessionID sessionId) {
		
		MessageEncoder encoder = null;
		switch(sessionId.getBeginString()) {
		case "FIX.4.4":
			encoder = Fix44MessageEncoder.getInstance();
			break;
		}
		
		return encoder;
	}
	
	void addIdFields(Message message);
	
	
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

	Message executionReport(OrderEvent execution);

	Message newOrderCross(Orders order);

	Message newOrderSingle(Orders order);

	Message orderCancelReject(Orders order, RejectTypes reject);

	Message orderCancelReplaceRequest(Orders order);

	Message orderCancelRequest(Orders order);

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