/**
 * 
 */
package com.cmm.jft.connector.engine;


import java.util.concurrent.ConcurrentLinkedQueue;

import com.cmm.jft.connector.Connector;
import com.cmm.jft.connector.message.ClientEngineMessageHandler;
import com.cmm.jft.messaging.fix44.Fix44MessageEncoder;
import com.cmm.jft.trading.Orders;

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
	
	
	private ConcurrentLinkedQueue<Message> messages;
	
	private static EngineConnector instance;
	
	private EngineConnector() {
		this.inMessages = new ConcurrentLinkedQueue<Message>();
		this.outMessages = new ConcurrentLinkedQueue<Message>();
		this.messages = new ConcurrentLinkedQueue<Message>();
	}
	
	
	public synchronized static EngineConnector getInstance() {
		if(instance == null) {
			instance = new EngineConnector();
		}
		return instance;
	}
	
	public void newOrderSingle(Orders ordr) {
		Message m = Fix44MessageEncoder.getInstance().newOrderSingle(ordr);
		send(m, sessionID);
	}
	
	public void cancelReplaceRequest(Orders ordr) {
		Message m = Fix44MessageEncoder.getInstance().orderCancelReplaceRequest(ordr);
		send(m, sessionID);
	}
	
	public void cancelRequest(Orders ordr) {
		Message m = Fix44MessageEncoder.getInstance().orderCancelRequest(ordr);
		send(m, sessionID);
	}
	
	public void newOrderCross(Orders ordr) {
		Message m = Fix44MessageEncoder.getInstance().newOrderCross(ordr);
		send(m, sessionID);
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
