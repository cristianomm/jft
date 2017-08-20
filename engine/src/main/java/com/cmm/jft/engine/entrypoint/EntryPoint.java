/**
 * 
 */
package com.cmm.jft.engine.entrypoint;

import java.util.HashMap;

import com.cmm.jft.engine.BookRepository;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.message.EngineHandler;
import com.cmm.jft.messaging.MessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.enums.FIXProtocols;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.messaging.handlers.MessageHandler;
import com.cmm.jft.trading.enums.StreamTypes;

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
 * <p>
 * <code>EntryPoint.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 17/06/2015 17:00:55
 *
 */
public class EntryPoint extends MessageCracker implements Application {

    private HashMap<FIXProtocols, MessageHandler> handlers;
    private HashMap<FIXProtocols, MessageEncoder> encoders;

    /**
     * http://www.codeproject.com/Articles/757708/Mock-FIX-Trading-Server
     * 
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

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onCreate(quickfix.SessionID)
     */
    public void onCreate(SessionID sessionId) {
	System.out.println("onCreate: Entry Point ");
    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    public void onLogon(SessionID sessionId) {
	if (verifyLogon(sessionId)) {
	    SessionRepository.getInstance().addSession(StreamTypes.ENTRYPOINT, sessionId);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    public void onLogout(SessionID sessionId) {
	SessionRepository.getInstance().removeSession(StreamTypes.ENTRYPOINT, sessionId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
     */
    public void toAdmin(Message message, SessionID sessionId) {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
     */
    public void fromAdmin(Message message, SessionID sessionId)
	    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
     */
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {
	// TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
     */
    public void fromApp(Message message, SessionID sessionId)
	    throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

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
	this.encoders.put(FIXProtocols.FIX44, Fix44EngineMessageEncoder.getInstance());
    }

    public void initHandlers(SessionSettings settings) {
	this.handlers = new HashMap<>();

	MessageHandler handler = new EngineHandler();
	initialize(handler);
	this.handlers.put(FIXProtocols.FIX44, handler);
    }

    public void initRepositories() {
	MessageRepository.getInstance();
	SessionRepository.getInstance();
    }

    public void initOrderBooks(SessionSettings settings) {
	BookRepository.getInstance();
    }

}
