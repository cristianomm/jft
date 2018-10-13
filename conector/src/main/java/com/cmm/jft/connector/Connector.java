package com.cmm.jft.connector;

import java.util.concurrent.ConcurrentLinkedQueue;

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


/**
 * 
 * @author Cristiano M Martins
 * @version 30-10-2015 15:14:17
 * 
 */
public class Connector extends MessageCracker implements Application {

    protected SessionID sessionID;
    protected ConcurrentLinkedQueue<Message> inMessages;
    protected ConcurrentLinkedQueue<Message> outMessages;

    public Connector() {
	super();
    }

    @Override
    public void onCreate(SessionID sessionId) {
	System.out.println("Connector-client create: " + sessionId.toString());
    }

    @Override
    public void onLogon(SessionID sessionId) {
	this.sessionID  = sessionId;
	System.out.println("Connector-logon " + sessionID);
    }

    @Override
    public void onLogout(SessionID sessionId) {

    }

    @Override
    public void toAdmin(Message message, SessionID sessionId) {

    }

    @Override
    public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound,
    IncorrectDataFormat, IncorrectTagValue, RejectLogon {

    }

    @Override
    public void toApp(Message message, SessionID sessionId) throws DoNotSend {

    }

    @Override
    public void fromApp(Message message, SessionID sessionId) throws FieldNotFound,
    IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
	System.out.println("Connector-message: " + message);
	crack(message, sessionId);
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

}