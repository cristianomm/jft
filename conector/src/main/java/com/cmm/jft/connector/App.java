/**
 * 
 */
package com.cmm.jft.connector;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Level;
import org.quickfixj.jmx.JmxExporter;

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
 * <p><code>App.java</code></p>
 * @author Cristiano
 * @version 31 de jul de 2015 00:00:55
 *
 */
public class App {

	//private static final CountDownLatch shutdownLatch = new CountDownLatch(1);

	private boolean initiatorStarted = false;
	private Initiator initiator = null;
	private Connector connector;
	
	
	public void init() throws Exception {
		InputStream inputStream = App.class.getResourceAsStream("ClientConnector.cfg");
		
		SessionSettings settings = new SessionSettings(inputStream);
		inputStream.close();

		connector = new Connector();
		MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();

		initiator = new SocketInitiator(connector, messageStoreFactory, settings, logFactory,	messageFactory);

		JmxExporter exporter = new JmxExporter();
		exporter.register(initiator);
		
	}

	public synchronized void logon() {
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

	public void logout() {
		for (SessionID sessionId : initiator.getSessions()) {
			Session.lookupSession(sessionId).logout("user requested");
		}
	}

	public void stop() {
		//shutdownLatch.countDown();
	}
	public static void main(String[] args) throws Exception {

		App main = new App();
		main.init();
		if (!System.getProperties().containsKey("openfix")) {
			main.logon();
		}
		
		Thread.sleep(1000);
		
		main.connector.sendTestMessage();
		
	}

}
