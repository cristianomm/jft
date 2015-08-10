package com.cmm.jft.engine.message;

import com.cmm.jft.trading.OrderExecution;

import quickfix.Message;
import quickfix.SessionID;

public class MessageBuilder {
	
	
	public static Message buildExecutionReport(OrderExecution execution, SessionID sessionId) {
		
		Message message = null;
		
		switch(sessionId.getBeginString()) {
		case "FIX.4.4":
			message = Fix44MessageEncoder.getInstance().executionReport(execution);
			
			break;
		}
		
		return message;
	}
	
	
	
}
