/**
 * 
 */
package com.cmm.jft.connector.engine;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Level;
import org.quickfixj.jmx.JmxExporter;

import com.cmm.jft.connector.message.ClientEngineMessageHandler;
import com.cmm.logging.Logging;

import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.field.ClOrdID;
import quickfix.field.OrdType;
import quickfix.field.Side;
import quickfix.field.TransactTime;
import quickfix.fix44.Heartbeat;
import quickfix.fix44.NewOrderSingle;

/**
 * <p><code>EngineStarter.java</code></p>
 * @author Cristiano M Martins
 * @version 31 de jul de 2015 00:00:55
 *
 */
public class EngineStarter {

	//private static final CountDownLatch shutdownLatch = new CountDownLatch(1);	
	private EngineConnector connector;
	private Initiator initiator = null;
	private boolean initiatorStarted = false;
	
	
	public void start() throws Exception {
		init();
		if (!System.getProperties().containsKey("openfix")) {
			logon();
		}
		
	}
	
	
	private void init() throws Exception {
		InputStream inputStream = EngineStarter.class.getResourceAsStream("ClientConnector.cfg");
		
		SessionSettings settings = new SessionSettings(inputStream);
		inputStream.close();
		
		connector = new EngineConnector();
		MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();
		
		initiator = new SocketInitiator(connector, messageStoreFactory, settings, logFactory, messageFactory);
		
		JmxExporter exporter = new JmxExporter();
		exporter.register(initiator);
		
	}

	private synchronized void logon() {
		if (!initiatorStarted) {
			try {
				initiator.start();
				initiatorStarted = true;
				
			} catch (Exception e) {
				Logging.getInstance().log(getClass(), e, Level.ERROR);
			}
		} 
		
		for (SessionID sessionId : initiator.getSessions()) {
			Session.lookupSession(sessionId).logon();
		}
		
	}

	private void logout() {
		for (SessionID sessionId : initiator.getSessions()) {
			Session.lookupSession(sessionId).logout("user requested");
		}
	}
	
}
