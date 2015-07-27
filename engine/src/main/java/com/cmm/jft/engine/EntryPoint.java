/**
 * 
 */
package com.cmm.jft.engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
import quickfix.field.OrdType;

/**
 * <p><code>EntryPoint.java</code></p>
 * @author Cristiano M Martins
 * @version 17/06/2015 17:00:55
 *
 */
public class EntryPoint extends MessageCracker implements Application {

	
	private HashSet<String> validOrderTypes;
	private static final String VALID_ORDER_TYPES_KEY = "ValidOrderTypes";
		
	
	/**
	 * http://www.codeproject.com/Articles/757708/Mock-FIX-Trading-Server
	 * @param settings 
	 * @throws FieldConvertError 
	 * @throws ConfigError 
	 * 
	 */
	public EntryPoint(SessionSettings settings) throws ConfigError, FieldConvertError {
		this.validOrderTypes = new HashSet<String>();
		initEntryPoint(settings);
	}
	
	private void initEntryPoint(SessionSettings settings) throws ConfigError, FieldConvertError {
		initValidOrderTypes(settings);
		initEncoders(settings);
		initHandlers(settings);
		initRepositories();
		
	}
	
	
	private void initValidOrderTypes(SessionSettings settings) throws ConfigError, FieldConvertError {
        if (settings.isSetting(VALID_ORDER_TYPES_KEY)) {
            List<String> orderTypes = Arrays
                    .asList(settings.getString(VALID_ORDER_TYPES_KEY).trim().split("\\s*,\\s*"));
            validOrderTypes.addAll(orderTypes);
        } else {
            validOrderTypes.add(OrdType.LIMIT + "");
        }
    }
	
	
	/* (non-Javadoc)
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	public void onCreate(SessionID sessionId) {
		
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
		
		logon = true;
		
		return logon;
	}
	
	
	
	public void initEncoders(SessionSettings settings) {
		
	}
	
	public void initHandlers(SessionSettings settings) {
		
	}
	
	public void initRepositories() {
		
		MessageRepository.getInstance();
		SessionRepository.getInstance();
		
	}
	
	public void initOrderBooks(SessionSettings settings) {
		
		
		
	}
	
	
	
}
