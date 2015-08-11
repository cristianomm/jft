/**
 * 
 */
package com.cmm.jft.engine.message;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.ExecType;
import quickfix.fix44.ExecutionReport;

import com.cmm.jft.trading.OrderExecution;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;

/**
 * <p><code>Fix44MessageDecoder.java</code> </p>
 * @author Cristiano M Martins
 * @version 11/08/2015 13:49:32
 */
public class Fix44MessageDecoder implements MessageDecoder {

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
	
	public OrderExecution executionReport(Message message) {
		OrderExecution execution = null;
		
		if(message instanceof ExecutionReport) {
			ExecutionReport er = (ExecutionReport) message;
			
			try {
			execution = new OrderExecution(
					ExecutionTypes.getByValue(er.getExecType().getValue()), 
					er.getTransactTime().getValue(), 
					er.getOrderQty().getValue(), er.getPrice().getValue()
					);
			}catch(FieldNotFound e) {
				
			}
			
		}
		
		return execution;
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
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageDecoder#orderCancelReject(quickfix.Message)
	 */
	@Override
	public Orders orderCancelReject(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageDecoder#orderCancelReplaceRequest(quickfix.Message)
	 */
	@Override
	public Orders orderCancelReplaceRequest(Message message) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.engine.message.MessageDecoder#orderCancelRequest(quickfix.Message)
	 */
	@Override
	public Orders orderCancelRequest(Message message) {
		// TODO Auto-generated method stub
		return null;
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
