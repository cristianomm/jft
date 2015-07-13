/**
 * 
 */
package com.cmm.jft.engine.message;

import quickfix.Field;
import quickfix.FieldMap;
import quickfix.Message.Header;
import quickfix.Message.Trailer;
import quickfix.MessageUtils;
import quickfix.field.AvgPx;
import quickfix.field.BeginSeqNo;
import quickfix.field.BeginString;
import quickfix.field.BodyLength;
import quickfix.field.CheckSum;
import quickfix.field.CumQty;
import quickfix.field.DeliverToCompID;
import quickfix.field.EncryptMethod;
import quickfix.field.EndSeqNo;
import quickfix.field.ExecID;
import quickfix.field.ExecType;
import quickfix.field.HeartBtInt;
import quickfix.field.LastPx;
import quickfix.field.LastQty;
import quickfix.field.LeavesQty;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.field.NewPassword;
import quickfix.field.NewSeqNo;
import quickfix.field.NextExpectedMsgSeqNum;
import quickfix.field.OrdStatus;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigSendingTime;
import quickfix.field.PossDupFlag;
import quickfix.field.PossResend;
import quickfix.field.Price;
import quickfix.field.RawData;
import quickfix.field.RawDataLength;
import quickfix.field.RefSeqNum;
import quickfix.field.ResetSeqNumFlag;
import quickfix.field.SenderCompID;
import quickfix.field.SendingTime;
import quickfix.field.Side;
import quickfix.field.StopPx;
import quickfix.field.TargetCompID;
import quickfix.field.TestReqID;
import quickfix.field.Text;
import quickfix.fix44.AllocationInstruction;
import quickfix.fix44.AllocationReport;
import quickfix.fix44.BusinessMessageReject;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.Logon;
import quickfix.fix44.Logout;
import quickfix.fix44.Reject;
import quickfix.fix44.ResendRequest;
import quickfix.fix44.SequenceReset;
import quickfix.fix44.TestRequest;
import quickfix.fix50sp1.ApplicationMessageReport;
import quickfix.fix50sp1.ApplicationMessageRequest;
import quickfix.fix50sp1.ApplicationMessageRequestAck;
import quickfix.fix44.ExecutionReport;
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
import quickfix.fix44.SecurityDefinition;
import quickfix.fix44.SecurityDefinitionRequest;


/**
 * <p><code>MessageEncoder.java</code></p>
 * @author Cristiano
 * @version 17/06/2015 17:31:27
 *
 */
public class MessageEncoder {
	
	
	public static void main(String[] args){
		
		System.out.println(new MessageEncoder().testRequest());
	}
	
	
	
	public Header buildHeader(){
		Header header = new Header();
		header.setField(new SenderCompID(""));
		header.setField(new TargetCompID(""));
		header.setField(new DeliverToCompID(""));
		header.setField(new MsgSeqNum(MessageCounter.getInstance().getMessageCount()));
		header.setField(new PossDupFlag(false));
		header.setField(new PossResend(false));
		header.setField(new SendingTime());
		header.setField(new OrigSendingTime());
		
		return header;
	}
	
	
	
	
	
	//[start]-------------------------------------------Session Specific
	public Heartbeat heartbeat(){
		Heartbeat heartbeat = new Heartbeat();
		heartbeat.setFields(buildHeader());
		heartbeat.setField(new TestReqID());
		return heartbeat;
	}
	
	public Logon logon(String authData, boolean resetSeqNum, String newPassword){
		Logon message = new Logon(new EncryptMethod(0), new HeartBtInt(30));
		message.setFields(buildHeader());
		
		if(authData != null){
			message.setField(new RawDataLength(authData.length()));
			message.setField(new RawData(authData));
		}
		
		message.setField(new ResetSeqNumFlag(resetSeqNum));
		//message.setField(new TestMessageIndicator(false));
		if(newPassword!=null){
			message.setField(new NewPassword(newPassword));
		}
		//message.setInt(35002, 0); 
		//message.setInt(35003, 1000);
		
		return message;
	}
	
	public Logout logout(String text){
		Logout logout = new Logout();
		if(text!=null){
			logout.setField(new Text(text));
		}
		
		return logout;
	}
	
	public Reject reject(int refMsgSeqNum){
		Reject reject = new Reject(new RefSeqNum(refMsgSeqNum));
		
		return reject;
	}
	
	public ResendRequest resendRequest(int beginSeqNum, int endSeqNum){
		ResendRequest resendRequest = new ResendRequest(new BeginSeqNo(beginSeqNum), new EndSeqNo(endSeqNum));
		return resendRequest;
	}
	
	public SequenceReset sequenceReset(){
		MessageCounter.getInstance().reset();
		SequenceReset reset = new SequenceReset(new NewSeqNo(MessageCounter.getInstance().getMessageCount()));
		return reset;
	}
	
	public TestRequest testRequest(){
		TestRequest testRequest = new TestRequest(new TestReqID());
		return testRequest;
	}
	//[end]
	
	
	//[start]-------------------------------------------Application Specific
	
	public AllocationInstruction allocationInstruction(){
		AllocationInstruction instruction = new AllocationInstruction();
		
		return instruction;
	}
	
	public AllocationReport allocationReport(){
		AllocationReport report = new AllocationReport();
		
		return report;
	}
	
	
	public ApplicationMessageReport applicationMessageReport(){
		ApplicationMessageReport report = new ApplicationMessageReport();
		
		return report;
	}
	
	public ApplicationMessageRequest applicationMessageRequest(){
		ApplicationMessageRequest request = new ApplicationMessageRequest();
		
		return request;
	}
	
	public ApplicationMessageRequestAck applicationMessageRequestAck(){
		ApplicationMessageRequestAck ack = new ApplicationMessageRequestAck();
		
		return ack;
	}
	
	public BusinessMessageReject businessMessageReject(){
		BusinessMessageReject messageReject = new BusinessMessageReject();
		
		
		return messageReject;
	}
	
	public ExecutionReport executionReport(String orderID, String execID, 
			char execType, char orderStatus, String symbol, char side, 
			double lastQty, double orderQty , double leavesQty, double cumQty, 
			double price, double stopPx, double lastPx){
		
		ExecutionReport executionReport = new ExecutionReport();
		
		executionReport = new ExecutionReport(
				new OrderID(orderID), new ExecID(execID),
				new ExecType(execType), new OrdStatus(orderStatus),
				new Side(side), new LeavesQty(leavesQty), 
				new CumQty(cumQty), new AvgPx(cumQty)
				);
		
		executionReport.set(new OrderQty(orderQty));
		executionReport.set(new Price(price));
		executionReport.set(new StopPx(stopPx));
		executionReport.set(new LastQty(lastQty));
		executionReport.set(new LastPx(lastPx));
		
		return executionReport;
	}
	
	public NewOrderCross newOrderCross(){
		NewOrderCross orderCross = new NewOrderCross();
		
		return orderCross;
	}
	
	public NewOrderSingle newOrderSingle(){
		NewOrderSingle orderSingle = new NewOrderSingle();
		
		
		return orderSingle;
	}
	
	public OrderCancelReject orderCancelReject(){
		OrderCancelReject cancelReject = new OrderCancelReject();
		
		return cancelReject;
	}
		
	public OrderCancelReplaceRequest orderCancelReplaceRequest(){
		OrderCancelReplaceRequest replaceRequest = new OrderCancelReplaceRequest();
		
		return replaceRequest;
	}
	
	public OrderCancelRequest orderCancelRequest(){
		OrderCancelRequest cancelRequest = new OrderCancelRequest();
		
		
		
		
		return cancelRequest;
	}
	
	public PositionMaintenanceReport positionMaintenanceReport(){
		PositionMaintenanceReport report = new PositionMaintenanceReport();
		
		return report;
	}
	
	public PositionMaintenanceRequest positionMaintenanceRequest(){
		PositionMaintenanceRequest maintenanceRequest = new PositionMaintenanceRequest();
		
		return maintenanceRequest;
	}
	
	public Quote quote(){
		Quote quote = new Quote();
		
		return quote;
	}
	
	public QuoteCancel quoteCancel(){
		QuoteCancel quoteCancel = new QuoteCancel();

		return quoteCancel;
	}
	
	public QuoteRequest quoteRequest(){
		QuoteRequest quoteRequest = new QuoteRequest();

		return quoteRequest;
	}
	
	public QuoteRequestReject quoteRequestReject(){
		QuoteRequestReject requestReject = new QuoteRequestReject();

		return requestReject;
	}
	
	public QuoteStatusReport quoteStatusReport(){
		QuoteStatusReport statusReport = new QuoteStatusReport();

		return statusReport;
	}
	
	public SecurityDefinition securityDefinition(){
		SecurityDefinition securityDefinition = new SecurityDefinition();

		return securityDefinition;
	}
	
	public SecurityDefinitionRequest securityDefinitionRequest(){
		SecurityDefinitionRequest request = new SecurityDefinitionRequest();
		
		return request;
	}
	//[end]

	
	
}
