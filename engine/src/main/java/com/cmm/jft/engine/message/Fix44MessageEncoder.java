/**
 * 
 */
package com.cmm.jft.engine.message;

import java.util.Date;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.trading.enums.OrderTypes;

import quickfix.Message.Header;
import quickfix.field.AvgPx;
import quickfix.field.BeginSeqNo;
import quickfix.field.ClOrdID;
import quickfix.field.CumQty;
import quickfix.field.DeliverToCompID;
import quickfix.field.EncryptMethod;
import quickfix.field.EndSeqNo;
import quickfix.field.ExecID;
import quickfix.field.ExecType;
import quickfix.field.ExpireDate;
import quickfix.field.HeartBtInt;
import quickfix.field.LastPx;
import quickfix.field.LastQty;
import quickfix.field.LeavesQty;
import quickfix.field.MsgSeqNum;
import quickfix.field.NewPassword;
import quickfix.field.NewSeqNo;
import quickfix.field.NoPartyIDs;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.OrigSendingTime;
import quickfix.field.PossDupFlag;
import quickfix.field.PossResend;
import quickfix.field.Price;
import quickfix.field.RawData;
import quickfix.field.RawDataLength;
import quickfix.field.RefSeqNum;
import quickfix.field.ResetSeqNumFlag;
import quickfix.field.SecurityExchange;
import quickfix.field.SenderCompID;
import quickfix.field.SendingTime;
import quickfix.field.Side;
import quickfix.field.StopPx;
import quickfix.field.Symbol;
import quickfix.field.TargetCompID;
import quickfix.field.TestReqID;
import quickfix.field.Text;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
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
 * <p><code>Fix44MessageEncoder.java</code></p>
 * @author Cristiano
 * @version 17/06/2015 17:31:27
 *
 */
public class Fix44MessageEncoder implements MessageEncoder {
	
	
	private static MessageEncoder instance;
	
	
	private Fix44MessageEncoder() {
		
	}
	
	
	public static synchronized MessageEncoder getInstance() {
		if(instance == null) {
			instance = new Fix44MessageEncoder();
		}
		return instance;
	}
	
	
	public static void main(String[] args){
		
		System.out.println(new Fix44MessageEncoder().testRequest());
	}
	
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#buildHeader()
	 */
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
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#heartbeat()
	 */
	public Heartbeat heartbeat(){
		Heartbeat heartbeat = new Heartbeat();
		heartbeat.setFields(buildHeader());
		heartbeat.setField(new TestReqID());
		return heartbeat;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#logon(java.lang.String, boolean, java.lang.String)
	 */
	public Logon logon(String authData, boolean resetSeqNum, String newPassword){
		Logon message = new Logon(new EncryptMethod(0), new HeartBtInt(30));
		message.setFields(buildHeader());
		
		if(authData != null){
			message.setField(new RawDataLength(authData.length()));
			message.setField(new RawData(authData));
		}
		
		message.setField(new ResetSeqNumFlag(resetSeqNum));
		if(newPassword!=null){
			message.setField(new NewPassword(newPassword));
		}
		
		
		return message;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#logout(java.lang.String)
	 */
	public Logout logout(String text){
		Logout logout = new Logout();
		if(text!=null){
			logout.setField(new Text(text));
		}
		
		return logout;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#reject(int)
	 */
	public Reject reject(int refMsgSeqNum){
		Reject reject = new Reject(new RefSeqNum(refMsgSeqNum));
		
		return reject;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#resendRequest(int, int)
	 */
	public ResendRequest resendRequest(int beginSeqNum, int endSeqNum){
		ResendRequest resendRequest = new ResendRequest(new BeginSeqNo(beginSeqNum), new EndSeqNo(endSeqNum));
		return resendRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#sequenceReset()
	 */
	public SequenceReset sequenceReset(){
		MessageCounter.getInstance().reset();
		SequenceReset reset = new SequenceReset(new NewSeqNo(MessageCounter.getInstance().getMessageCount()));
		return reset;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#testRequest()
	 */
	public TestRequest testRequest(){
		TestRequest testRequest = new TestRequest(new TestReqID());
		return testRequest;
	}
	//[end]
	
	
	//[start]-------------------------------------------Application Specific
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#allocationInstruction()
	 */
	public AllocationInstruction allocationInstruction(){
		AllocationInstruction instruction = new AllocationInstruction();
		
		return instruction;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#allocationReport()
	 */
	public AllocationReport allocationReport(){
		AllocationReport report = new AllocationReport();
		
		return report;
	}
	
	
//	/* (non-Javadoc)
//	 * @see com.cmm.jft.engine.message.MessageEncoder#applicationMessageReport()
//	 */
//	public ApplicationMessageReport applicationMessageReport(){
//		ApplicationMessageReport report = new ApplicationMessageReport();
//		
//		return report;
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.cmm.jft.engine.message.MessageEncoder#applicationMessageRequest()
//	 */
//	public ApplicationMessageRequest applicationMessageRequest(){
//		ApplicationMessageRequest request = new ApplicationMessageRequest();
//		
//		return request;
//	}
//	
//	/* (non-Javadoc)
//	 * @see com.cmm.jft.engine.message.MessageEncoder#applicationMessageRequestAck()
//	 */
//	public ApplicationMessageRequestAck applicationMessageRequestAck(){
//		ApplicationMessageRequestAck ack = new ApplicationMessageRequestAck();
//		
//		return ack;
//	}
//	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#businessMessageReject()
	 */
	public BusinessMessageReject businessMessageReject(){
		BusinessMessageReject messageReject = new BusinessMessageReject();
		
		
		return messageReject;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#executionReport(java.lang.String, java.lang.String, char, char, java.lang.String, char, double, double, double, double, double, double, double)
	 */
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
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#newOrderCross()
	 */
	public NewOrderCross newOrderCross(){
		NewOrderCross orderCross = new NewOrderCross();
		
		return orderCross;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#newOrderSingle()
	 */
	public NewOrderSingle newOrderSingle(String symbol, com.cmm.jft.trading.enums.Side side, 
			double ordrQty, OrderTypes type, double ordrPrice, double stopPx, 
			com.cmm.jft.trading.enums.TimeInForce tif, Date expireDt, String memo){
		
		NewOrderSingle orderSingle = new NewOrderSingle();
		
		orderSingle.set(new ClOrdID("123456")); 
		orderSingle.set(new NoPartyIDs(0));
		
		orderSingle.set(new Symbol(symbol));
		orderSingle.set(new SecurityExchange("BVMF"));
		orderSingle.set(new Side(side. name().charAt(0))); 
		orderSingle.set(new TransactTime());
		
		orderSingle.set(new OrderQty(ordrQty));
		orderSingle.set(new OrdType(type.name().charAt(0)));
		orderSingle.set(new Price(ordrPrice));
		orderSingle.set(new StopPx(stopPx));
		orderSingle.set(new TimeInForce(tif.getValue()));
		
		orderSingle.set(new ExpireDate(((DateTimeFormatter)FormatterFactory.getFormatter(FormatterTypes.DATE_F9)).format(expireDt)));
		orderSingle.set(new Text(memo));
		
		return orderSingle;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#orderCancelReject()
	 */
	public OrderCancelReject orderCancelReject(){
		OrderCancelReject cancelReject = new OrderCancelReject();
		
		return cancelReject;
	}
		
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#orderCancelReplaceRequest()
	 */
	public OrderCancelReplaceRequest orderCancelReplaceRequest(){
		
		OrderCancelReplaceRequest replaceRequest = new OrderCancelReplaceRequest();
		
		
		
		
		return replaceRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#orderCancelRequest()
	 */
	public OrderCancelRequest orderCancelRequest(String origClordID, String clOrdID, String symbol, 
			com.cmm.jft.trading.enums.Side side, double ordQty, String memo){
		
		OrderCancelRequest cancelRequest = new OrderCancelRequest();
		
		cancelRequest.set(new OrigClOrdID(origClordID));
		cancelRequest.set(new ClOrdID(clOrdID));
		cancelRequest.set(new NoPartyIDs(0));
		cancelRequest.set(new Symbol(symbol));
		cancelRequest.set(new Side(side.getValue()));
		cancelRequest.set(new TransactTime(new Date()));
		cancelRequest.set(new OrderQty(ordQty));
		cancelRequest.set(new Text(memo));
		
		return cancelRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#positionMaintenanceReport()
	 */
	public PositionMaintenanceReport positionMaintenanceReport(){
		PositionMaintenanceReport report = new PositionMaintenanceReport();
		
		return report;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#positionMaintenanceRequest()
	 */
	public PositionMaintenanceRequest positionMaintenanceRequest(){
		PositionMaintenanceRequest maintenanceRequest = new PositionMaintenanceRequest();
		
		return maintenanceRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quote()
	 */
	public Quote quote(){
		Quote quote = new Quote();
		
		return quote;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quoteCancel()
	 */
	public QuoteCancel quoteCancel(){
		QuoteCancel quoteCancel = new QuoteCancel();

		return quoteCancel;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quoteRequest()
	 */
	public QuoteRequest quoteRequest(){
		QuoteRequest quoteRequest = new QuoteRequest();

		return quoteRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quoteRequestReject()
	 */
	public QuoteRequestReject quoteRequestReject(){
		QuoteRequestReject requestReject = new QuoteRequestReject();

		return requestReject;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quoteStatusReport()
	 */
	public QuoteStatusReport quoteStatusReport(){
		QuoteStatusReport statusReport = new QuoteStatusReport();

		return statusReport;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#securityDefinition()
	 */
	public SecurityDefinition securityDefinition(){
		
		SecurityDefinition securityDefinition = new SecurityDefinition();
		
		
		
		return securityDefinition;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#securityDefinitionRequest()
	 */
	public SecurityDefinitionRequest securityDefinitionRequest(){
		SecurityDefinitionRequest request = new SecurityDefinitionRequest();
		
		return request;
	}
	//[end]

	
	
}
