/**
 * 
 */
package com.cmm.jft.engine;

import quickfix.Message;
import quickfix.SessionID;

import com.cmm.jft.engine.message.MessageSender;
import com.cmm.jft.trading.OrderExecution;
import com.cmm.jft.trading.enums.ExecutionTypes;

/**
 * @author Cristiano M Martins
 * @version 17/08/15 11:17:42
 *
 */
public class OrderMatcher  implements MessageSender {

	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		
		// TODO Auto-generated method stub
		
		
		return false;
	}
	
	// creates the order execution
	//	OrderExecution oex = new OrderExecution(ExecutionTypes.TRADE, executionDateTime, execVolume, execPrice);
	//	oex.setMessage("Execution of " + execution.getVolume() + " at price " + execution.getPrice());
	//	oex.setLeavesVolume(volume - oex.getVolume());
	
}
