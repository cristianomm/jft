/**
 * 
 */
package com.cmm.jft.messaging.fix44;

import java.util.Date;

import quickfix.Group;
import quickfix.Message;
import quickfix.field.AvgPx;
import quickfix.field.BeginSeqNo;
import quickfix.field.ClOrdID;
import quickfix.field.ContraBroker;
import quickfix.field.CumQty;
import quickfix.field.CxlRejResponseTo;
import quickfix.field.EncryptMethod;
import quickfix.field.EndSeqNo;
import quickfix.field.ExecID;
import quickfix.field.ExecType;
import quickfix.field.ExpireDate;
import quickfix.field.HeartBtInt;
import quickfix.field.LeavesQty;
import quickfix.field.NewPassword;
import quickfix.field.NewSeqNo;
import quickfix.field.OrdStatus;
import quickfix.field.OrdType;
import quickfix.field.OrderID;
import quickfix.field.OrderQty;
import quickfix.field.OrigClOrdID;
import quickfix.field.PartyID;
import quickfix.field.PartyIDSource;
import quickfix.field.PartyRole;
import quickfix.field.Price;
import quickfix.field.RawData;
import quickfix.field.RawDataLength;
import quickfix.field.RefSeqNum;
import quickfix.field.ResetSeqNumFlag;
import quickfix.field.SecurityExchange;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TestReqID;
import quickfix.field.Text;
import quickfix.field.TimeInForce;
import quickfix.field.TransactTime;
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

import com.cmm.jft.core.Configuration;
import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.messaging.MessageCounter;
import com.cmm.jft.messaging.MessageEncoder;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.RejectTypes;


/**
 * <p><code>Fix44EngineMessageEncoder.java</code></p>
 * @author Cristiano
 * @version 17/06/2015 17:31:27
 *
 */
public class Fix44EngineMessageEncoder implements MessageEncoder {
	
	
	private static Fix44EngineMessageEncoder instance;
	
	
	private PartyIDSource partyIDSrc;
	
	private PartyID partyIDSL;
	private PartyRole partyRoleSL;
	
	private PartyID partyIDEF;
	private PartyRole partyRoleEF;
	
	private PartyID partyIDET;
	private PartyRole partyRoleET;
	
	
	private Fix44EngineMessageEncoder() {
		
		this. partyIDSrc = new PartyIDSource('D');
		
		this.partyIDSL = new PartyID(Configuration.getInstance().getConfiguration("senderLocation").toString());
		this.partyRoleSL = new PartyRole(54);
		
		this.partyIDEF = new PartyID(Configuration.getInstance().getConfiguration("brokerID").toString());
		this.partyRoleEF = new PartyRole(7);
		
		this.partyIDET = new PartyID(Configuration.getInstance().getConfiguration("traderID").toString());
		this.partyRoleET = new PartyRole(36);
	}
	
	
	public static synchronized Fix44EngineMessageEncoder getInstance() {
		if(instance == null) {
			instance = new Fix44EngineMessageEncoder();
		}
		return instance;
	}
	
	
	public static void main(String[] args){
		
		System.out.println(new Fix44EngineMessageEncoder().allocationInstruction());
	}
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#addIdFields(quickfix.Message)
	 */
	public void addIdFields(Message message) {
		
		Group ids = new Group(453,453);
		ids.setString(448, partyIDSL.getValue());
		ids.setChar(447, partyIDSrc.getValue());
		ids.setInt(452, partyRoleSL.getValue());
		message.addGroup(ids);
		
		ids = new AllocationInstruction.NoPartyIDs();
		ids.setString(448, partyIDEF.getValue());
		ids.setChar(447, partyIDSrc.getValue());
		ids.setInt(452, partyRoleEF.getValue());
		message.addGroup(ids);
		
		ids = new AllocationInstruction.NoPartyIDs();
		ids.setString(448, partyIDET.getValue());
		ids.setChar(447, partyIDSrc.getValue());
		ids.setInt(452, partyRoleET.getValue());
		message.addGroup(ids);
		
	}
	
	
	
	//[start]-------------------------------------------Session Specific
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#heartbeat()
	 */
	public Heartbeat heartbeat(){
		Heartbeat heartbeat = new Heartbeat();
		heartbeat.setField(new TestReqID());
		return heartbeat;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#logon(java.lang.String, boolean, java.lang.String)
	 */
	public Logon logon(String authData, boolean resetSeqNum, String newPassword){
		Logon message = new Logon(new EncryptMethod(0), new HeartBtInt(30));
				
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
		SequenceReset reset = new SequenceReset(new NewSeqNo(1));
		
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
		
		addIdFields(instruction);
		
		return instruction;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#allocationReport()
	 */
	public AllocationReport allocationReport(){
		AllocationReport report = new AllocationReport();
		
		addIdFields(report);
		
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
	 * @see com.cmm.jft.engine.message.MessageEncoder#executionReport(OrderEvent execution)
	 */
	public ExecutionReport executionReport(OrderEvent execution){
		
		ExecutionReport executionReport = new ExecutionReport(
				new OrderID(execution.getOrderID().getClOrdID()), 
				new ExecID(execution.getOrderEventID()+""),
				new ExecType(execution.getExecutionType().getValue()), 
				new OrdStatus(execution.getOrderID().getOrderStatus().getValue()),
				new Side(execution.getOrderID().getSide().getValue()), 
				new LeavesQty(execution.getOrderID().getLeavesVolume()), 
				new CumQty(execution.getOrderID().getExecutedVolume()), 
				new AvgPx(0)
				);
		
		executionReport.set(new Symbol(execution.getOrderID().getSecurityID().getSymbol()));
		executionReport.set(new OrderQty(execution.getOrderID().getVolume()));
		
		if(!execution.getContraBroker().isEmpty()) {
			ExecutionReport.NoContraBrokers cb = new ExecutionReport.NoContraBrokers();
			cb.set(new ContraBroker(execution.getContraBroker()));
		}
		
		addIdFields(executionReport);
		
		
		return executionReport;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#newOrderCross()
	 */
	public NewOrderCross newOrderCross(Orders order){
		NewOrderCross orderCross = new NewOrderCross();
		
		addIdFields(orderCross);
		
		return orderCross;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#newOrderSingle(Orders order)
	 */
	public NewOrderSingle newOrderSingle(Orders order){
		
		NewOrderSingle orderSingle = new NewOrderSingle(
				new ClOrdID(order.getClOrdID()), new Side(order.getSide().getValue()), 
				new TransactTime(), new OrdType(order.getOrderType().getValue())
				);
		
		addIdFields(orderSingle);
		
		orderSingle.set(new Symbol(order.getSecurityID().getSymbol()));
		orderSingle.set(new SecurityExchange("BVMF"));
		
		orderSingle.set(new OrderQty(order.getVolume()));
		orderSingle.set(new Price(order.getPrice()));
		orderSingle.set(new TimeInForce(order.getValidityType().getValue()));
		
		orderSingle.set(new ExpireDate(((DateTimeFormatter)FormatterFactory.getFormatter(FormatterTypes.DATE_F9)).format(order.getDuration())));
		orderSingle.setString(5149, order.getComment());
		
		return orderSingle;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#orderCancelReject(Orders order, RejectTypes reject)
	 */
	public OrderCancelReject orderCancelReject(Orders order, RejectTypes reject){
		
		OrderCancelReject cancelReject = new OrderCancelReject();
		
		if(order!=null) {
			cancelReject.set(new OrderID(order.getOrderID().toString()));
			cancelReject.set(new ClOrdID(order.getClOrdID()));
			cancelReject.set(new OrigClOrdID(order.getOrigClOrdID()));
			cancelReject.set(new OrdStatus(order.getOrderStatus().getValue()));
			cancelReject.set(new CxlRejResponseTo(reject.getValue()));
			cancelReject.setInt(453, 0);
			cancelReject.setString(5149, order.getComment());
		}
		
		return cancelReject;
	}
		
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#orderCancelReplaceRequest(Orders order)
	 */
	public OrderCancelReplaceRequest orderCancelReplaceRequest(Orders order){
		
		OrderCancelReplaceRequest replaceRequest = new OrderCancelReplaceRequest();
		
		addIdFields(replaceRequest);
		
		if(order != null) {
			replaceRequest.set(new OrigClOrdID(order.getOrigClOrdID()));
			replaceRequest.set(new ClOrdID(order.getClOrdID()));
			replaceRequest.set(new Symbol(order.getSecurityID().getSymbol()));
			replaceRequest.set(new Side(order.getSide().getValue()));
			replaceRequest.set(new TransactTime(order.getOrderDateTime()));
			replaceRequest.set(new OrderQty(order.getExecutedVolume()));
			replaceRequest.set(new OrdType(order.getOrderType().getValue()));
			replaceRequest.setString(5149, order.getComment());
		}
		
		return replaceRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#orderCancelRequest()
	 */
	public OrderCancelRequest orderCancelRequest(Orders order){
		
		OrderCancelRequest cancelRequest = new OrderCancelRequest();
		
		addIdFields(cancelRequest);
		
		cancelRequest.set(new OrigClOrdID(order.getOrigClOrdID()));
		cancelRequest.set(new ClOrdID(order.getClOrdID()));
		cancelRequest.set(new Symbol(order.getSecurityID().getSymbol()));
		cancelRequest.set(new Side(order.getSide().getValue()));
		cancelRequest.set(new TransactTime(new Date()));
		cancelRequest.set(new OrderQty(order.getVolume()));
		cancelRequest.setString(5149, order.getComment());
		
		return cancelRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#positionMaintenanceReport()
	 */
	public PositionMaintenanceReport positionMaintenanceReport(){
		PositionMaintenanceReport report = new PositionMaintenanceReport();
		
		addIdFields(report);
		
		return report;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#positionMaintenanceRequest()
	 */
	public PositionMaintenanceRequest positionMaintenanceRequest(){
		PositionMaintenanceRequest maintenanceRequest = new PositionMaintenanceRequest();
		
		addIdFields(maintenanceRequest);
		
		return maintenanceRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quote()
	 */
	public Quote quote(){
		Quote quote = new Quote();
		
		addIdFields(quote);
		
		return quote;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quoteCancel()
	 */
	public QuoteCancel quoteCancel(){
		QuoteCancel quoteCancel = new QuoteCancel();
		
		addIdFields(quoteCancel);
		
		return quoteCancel;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quoteRequest()
	 */
	public QuoteRequest quoteRequest(){
		QuoteRequest quoteRequest = new QuoteRequest();

		addIdFields(quoteRequest);
		
		return quoteRequest;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quoteRequestReject()
	 */
	public QuoteRequestReject quoteRequestReject(){
		QuoteRequestReject requestReject = new QuoteRequestReject();
		
		addIdFields(requestReject);
		
		return requestReject;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#quoteStatusReport()
	 */
	public QuoteStatusReport quoteStatusReport(){
		QuoteStatusReport statusReport = new QuoteStatusReport();

		addIdFields(statusReport);
		
		return statusReport;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#securityDefinition()
	 */
	public SecurityDefinition securityDefinition(){
		SecurityDefinition securityDefinition = new SecurityDefinition();
		
		addIdFields(securityDefinition);
		
		return securityDefinition;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageEncoder#securityDefinitionRequest()
	 */
	public SecurityDefinitionRequest securityDefinitionRequest(){
		SecurityDefinitionRequest request = new SecurityDefinitionRequest();
		
		addIdFields(request);
		
		return request;
	}
	//[end]

	
	
}
