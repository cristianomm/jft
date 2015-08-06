/**
 * 
 */
package com.cmm.jft.connector;

import org.apache.mina.util.SessionUtil;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.ClOrdID;
import quickfix.field.NoPartyIDs;
import quickfix.field.OrdType;
import quickfix.field.OrderQty;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.TransactTime;
import quickfix.fix44.NewOrderSingle;

/**
 * <p><code>Connector.java</code></p>
 * @author Cristiano M Martins
 * @version 30 de jul de 2015 23:54:28
 *
 */
public class Connector extends MessageCracker implements Application{
	
	private SessionID sessionID;
	
	/* (non-Javadoc)
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	@Override
	public void onCreate(SessionID sessionId) {
		System.out.println("create " + sessionId.getSenderCompID());
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	@Override
	public void onLogon(SessionID sessionId) {
		System.out.println("logon " + sessionId.getSenderCompID());
		this.sessionID  = sessionId;
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	@Override
	public void onLogout(SessionID sessionId) {
		System.out.println("logout " + sessionId.getSenderCompID());
		this.sessionID = sessionId;
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		// TODO Auto-generated method stub
		
	}
	
	
	public boolean send(quickfix.Message message, SessionID sessionID) {
        boolean ret = false;
		try {
            ret = Session.sendToTarget(message, sessionID);
        } catch (SessionNotFound e) {
            System.out.println(e);
        }
		
		return ret;
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
			System.out.println("Send status: " + ret);
		}
		
		
		return ret;
	}
	

}
