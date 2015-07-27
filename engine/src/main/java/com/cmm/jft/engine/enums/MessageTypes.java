/**
 * 
 */
package com.cmm.jft.engine.enums;

/**
 * <p><code>MessageTypes.java</code></p>
 * @author Cristiano
 * @version 26 de jul de 2015 22:45:05
 *
 */
public enum MessageTypes {
	
	Heartbeat,
	Logon,
	Logout,
	Reject,
	ResendRequest,
	SequenceReset,
	TestRequest,
	AllocationInstruction,
	AllocationReport,
	ApplicationMessageReport,
	ApplicationMessageRequest,
	ApplicationMessageRequestAck,
	BusinessMessageReject,
	ExecutionReport,
	NewOrderCross,
	NewOrderSingle,
	OrderCancelReject,
	OrderCancelReplaceRequest,
	OrderCancelRequest,
	PositionMaintenanceReport,
	PositionMaintenanceRequest,
	Quote,
	QuoteCancel,
	QuoteRequest,
	QuoteRequestReject,
	QuoteStatusReport,
	SecurityDefinition,
	SecurityDefinitionRequest
	
}
