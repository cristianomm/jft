/**
 * 
 */
package com.cmm.jft.engine;

import java.util.HashMap;

import com.cmm.jft.messaging.Fix44MessageEncoder;
import com.cmm.jft.engine.message.Fix44MessageHandler;
import com.cmm.jft.messaging.MessageEncoder;
import com.cmm.jft.messaging.MessageHandler;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;

/**
 * <p><code>EntryPoint.java</code></p>
 * @author Cristiano M Martins
 * @version 17/06/2015 17:00:55
 *
 */
public class EntryPoint extends MessageCracker implements Application {
	
	
	private HashMap<String, MessageHandler> handlers;
	private HashMap<String, MessageEncoder> encoders;
	
	
	
	/**
	 * http://www.codeproject.com/Articles/757708/Mock-FIX-Trading-Server
	 * @param settings 
	 * @throws FieldConvertError 
	 * @throws ConfigError 
	 * 
	 */
	public EntryPoint(SessionSettings settings) throws ConfigError, FieldConvertError {
		initEntryPoint(settings);
	}
	
	private void initEntryPoint(SessionSettings settings) throws ConfigError, FieldConvertError {
		initEncoders(settings);
		initHandlers(settings);
		initRepositories();
		
	}
	
	
	/* (non-Javadoc)
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	public void onCreate(SessionID sessionId) {
		System.out.println("create " + sessionId.getTargetCompID());
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	public void onLogon(SessionID sessionId) {
		if(verifyLogon(sessionId)) {
			SessionRepository.getInstance().addSession(sessionId);
			
		}
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	public void onLogout(SessionID sessionId) {
		SessionRepository.getInstance().removeSession(sessionId);
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
		
		crack(message, sessionId);
		
	}
			
	
	private boolean verifyLogon(SessionID sessionId) {
		
		boolean logon = false;
		System.out.println("trying connection: " + sessionId.getSenderCompID());
		logon = true;
		
		return logon;
	}
	
	
	
	public void initEncoders(SessionSettings settings) {
		this.encoders = new HashMap<>();
		this.encoders.put("FIX.4.4", Fix44MessageEncoder.getInstance());
	}
	
	public void initHandlers(SessionSettings settings) {
		this.handlers = new HashMap<>();
		
		MessageHandler handler = new Fix44MessageHandler();
		initialize(handler);
		this.handlers.put("FIX.4.4", handler);
		
	}
	
	public void initRepositories() {
		
		MessageRepository.getInstance();
		SessionRepository.getInstance();
		
	}
	
	public void initOrderBooks(SessionSettings settings) {
		BookRepository.getInstance();
		
	}
	
	
	
}
