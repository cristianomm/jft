package com.cmm.jft.engine.message;

import quickfix.Message.Header;
import quickfix.fix44.AllocationInstruction;
import quickfix.fix44.AllocationReport;
import quickfix.fix44.BusinessMessageReject;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.Logon;
import quickfix.fix44.Logout;
import quickfix.fix44.NewOrderCross;
import quickfix.fix44.NewOrderSingle;
import quickfix.fix44.OrderCancelReject;
import quickfix.fix44.OrderCancelReplaceRequest;
import quickfix.fix44.OrderCancelRequest;
import quickfix.fix44.PositionMaintenanceReport;
import quickfix.fix44.PositionMaintenanceRequest;
import quickfix.fix44.Quote;
import quickfix.fix44.QuoteCancel;
import quickfix.fix44.QuoteRequest;
import quickfix.fix44.QuoteRequestReject;
import quickfix.fix44.QuoteStatusReport;
import quickfix.fix44.Reject;
import quickfix.fix44.ResendRequest;
import quickfix.fix44.SecurityDefinition;
import quickfix.fix44.SecurityDefinitionRequest;
import quickfix.fix44.SequenceReset;
import quickfix.fix44.TestRequest;
//import quickfix.fix50sp1.ApplicationMessageReport;
//import quickfix.fix50sp1.ApplicationMessageRequest;
//import quickfix.fix50sp1.ApplicationMessageRequestAck;

public interface MessageEncoder {

	Header buildHeader();

	//[start]-------------------------------------------Session Specific
	Heartbeat heartbeat();

	Logon logon(String authData, boolean resetSeqNum,
			String newPassword);

	
	Logout logout(String text);

	Reject reject(int refMsgSeqNum);

	ResendRequest resendRequest(int beginSeqNum, int endSeqNum);

	SequenceReset sequenceReset();

	TestRequest testRequest();

	//[end]

	
	//[start]-------------------------------------------Application Specific
	AllocationInstruction allocationInstruction();

	AllocationReport allocationReport();
//
//	ApplicationMessageReport applicationMessageReport();
//
//	ApplicationMessageRequest applicationMessageRequest();
//
//	ApplicationMessageRequestAck applicationMessageRequestAck();

	BusinessMessageReject businessMessageReject();

	ExecutionReport executionReport(String orderID,
			String execID, char execType, char orderStatus, String symbol,
			char side, double lastQty, double orderQty, double leavesQty,
			double cumQty, double price, double stopPx, double lastPx);

	NewOrderCross newOrderCross();

	NewOrderSingle newOrderSingle();

	OrderCancelReject orderCancelReject();

	OrderCancelReplaceRequest orderCancelReplaceRequest();

	OrderCancelRequest orderCancelRequest();

	PositionMaintenanceReport positionMaintenanceReport();

	PositionMaintenanceRequest positionMaintenanceRequest();

	Quote quote();

	QuoteCancel quoteCancel();

	QuoteRequest quoteRequest();

	QuoteRequestReject quoteRequestReject();

	QuoteStatusReport quoteStatusReport();

	SecurityDefinition securityDefinition();

	SecurityDefinitionRequest securityDefinitionRequest();
	//[end]

}