/**
 * 
 */
package com.cmm.jft.connector.engine;


import java.util.concurrent.ConcurrentLinkedQueue;

import com.cmm.jft.connector.Connector;
import com.cmm.jft.connector.message.ClientEngineMessageHandler;

import quickfix.Application;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.Session;
import quickfix.field.ClOrdID;
import quickfix.field.NoPartyIDs;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;
import sun.util.resources.cldr.kk.CalendarData_kk_Cyrl_KZ;

/**
 * <p><code>EngineConnector.java</code></p>
 * @author Cristiano M Martins
 * @version 30-07-2015 23:54:28
 *
 */
public class EngineConnector extends Connector {
	
	public EngineConnector() {
		this.inMessages = new ConcurrentLinkedQueue<Message>();
		this.outMessages = new ConcurrentLinkedQueue<Message>();
		new ClientEngineMessageHandler();
	}
	
	
	
	public boolean sendTestMessage() {
		boolean ret = false;
		
		NewOrderSingle message = new NewOrderSingle();
		message.set(new ClOrdID("123456")); 
		message.set(new NoPartyIDs(0));
		
		message.set(new Symbol("WINV15"));		
		message.set(new Side('1')); 
		message.set(new TransactTime());
		
		message.set(new OrderQty(1));
		message.set(new OrdType('1'));
				
		if(Session.doesSessionExist(sessionID)) {
			System.out.println("Sending test message: " + message);
			ret = send(message, this.sessionID);
			ret = send(message, this.sessionID);
			System.out.println("Send status: " + ret);
		}
		
		
		return ret;
	}
	

}
