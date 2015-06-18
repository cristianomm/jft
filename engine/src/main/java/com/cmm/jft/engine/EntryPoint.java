/**
 * 
 */
package com.cmm.jft.engine;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;

/**
 * <p><code>EntryPoint.java</code></p>
 * @author Cristiano
 * @version 17/06/2015 17:00:55
 *
 */
public class EntryPoint extends MessageCracker implements Application {

	/* (non-Javadoc)
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	public void onLogon(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	public void onLogout(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
	 */
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
	 */
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
	 */
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
	 */
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		// TODO Auto-generated method stub
		
	}

	
	
}
