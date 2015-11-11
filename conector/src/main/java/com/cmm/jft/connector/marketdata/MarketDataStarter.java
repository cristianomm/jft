package com.cmm.jft.connector.marketdata;

import java.io.InputStream;

import org.apache.log4j.Level;
import org.quickfixj.jmx.JmxExporter;

import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

import com.cmm.logging.Logging;


/**
 * <p><code>MarketDataStarter.java</code></p>
 * @author Cristiano M Martins
 * @version 30-10-2015 12:08:50
 *
 */
public class MarketDataStarter {

	private MarketDataConnector connector;
	private Initiator initiator = null;
	private boolean initiatorStarted = false;


	public void start() throws Exception {
		init();
		if (!System.getProperties().containsKey("openfix")) {
			logon();
		}

	}


	private void init() throws Exception {
		InputStream inputStream = MarketDataStarter.class.getResourceAsStream("MDClientConnector.cfg");

		SessionSettings settings = new SessionSettings(inputStream);
		inputStream.close();

		connector = new MarketDataConnector();
		MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();

		initiator = new SocketInitiator(connector, messageStoreFactory, settings, logFactory,	messageFactory);

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
