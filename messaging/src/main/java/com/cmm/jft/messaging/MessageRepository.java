package com.cmm.jft.messaging;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;

import quickfix.DataDictionaryProvider;
import quickfix.FixVersions;
import quickfix.LogUtil;
import quickfix.Message;
import quickfix.MessageUtils;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.ApplVerID;

/**
 * <p>
 * <code>MessageRepository.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 22 de jul de 2015 11:14:09
 *
 */
public class MessageRepository {

    private static MessageRepository instance;
    private ConcurrentLinkedQueue<Tuple> messages;

    private MessageRepository() {
	this.messages = new ConcurrentLinkedQueue<Tuple>();

	new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (true) {
		    try {
			while (!messages.isEmpty()) {
			    Tuple t = messages.poll();
			    if (t != null) {
				deliverMessage(t.getMessage(), t.getSessionID());
			    }
			}
			Thread.sleep(100);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	}).start();

    }

    public static synchronized MessageRepository getInstance() {
	if (instance == null) {
	    instance = new MessageRepository();
	}

	return instance;
    }

    public boolean addMessage(Message message, SessionID sessionID) {
	return this.messages.offer(new Tuple(message, sessionID));
    }

    public Tuple retrieveMessage() {

	return messages.poll();
    }

    public int queueSize() {
	return messages.size();
    }

    private void deliverMessage(Message message, SessionID sessionID) {

	try {
	    Logging.getInstance().log(getClass(), "Deliver message: " + message.toString(), Level.INFO);

	    Session session = Session.lookupSession(sessionID);
	    if (session == null) {
		throw new SessionNotFound(sessionID.toString());
	    }

	    DataDictionaryProvider dataDictionaryProvider = session.getDataDictionaryProvider();
	    if (dataDictionaryProvider != null) {
//		try {
//		    dataDictionaryProvider.getApplicationDataDictionary(getApplVerID(session, message))
//		    .validate(message, false);
//		} catch (Exception e) {
//		    LogUtil.logThrowable(sessionID, "Outgoing message failed validation: " + e.getMessage(), e);
//		    return;
//		}
	    }

	    session.send(message);
	    // Session.sendToTarget(message, sessionID);
	} catch (SessionNotFound e) {
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	} catch (Exception e) {
	    // e.printStackTrace();
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}
    }

    private ApplVerID getApplVerID(Session session, Message message) {
	String beginString = session.getSessionID().getBeginString();
	if (FixVersions.BEGINSTRING_FIXT11.equals(beginString)) {
	    return new ApplVerID(ApplVerID.FIX50);
	} else {
	    return MessageUtils.toApplVerID(beginString);
	}
    }

}
