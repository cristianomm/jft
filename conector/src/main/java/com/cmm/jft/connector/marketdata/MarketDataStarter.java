package com.cmm.jft.connector.marketdata;

import java.io.InputStream;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Level;
import org.quickfixj.jmx.JmxExporter;

import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.Initiator;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;
import quickfix.mina.SessionConnector;

import com.cmm.jft.connector.enums.Streams;
import com.cmm.jft.core.services.Service;
import com.cmm.logging.Logging;


/**
 * <p><code>MarketDataStarter.java</code></p>
 * @author Cristiano M Martins
 * @version 30-10-2015 12:08:50
 *
 */
public class MarketDataStarter implements Service {

	private MarketDataConnector connector;
	private Initiator initiator = null;
	private boolean started;
	private boolean initiatorStarted = false;
	private static final CountDownLatch shutdownLatch = new CountDownLatch(1);
	
	public static void main(String[] args) {
		try {
			MarketDataStarter mds = new MarketDataStarter();
			mds.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean start() {
		try {
			init();
			
			if (!System.getProperties().containsKey("openfix")) {
				setChannels();
			}
			
			shutdownLatch.await();
			started = true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return started;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.core.services.Service#stop()
	 */
	@Override
	public boolean stop() {
		shutdownLatch.countDown();
		return (started = false);
	}


	private void init() throws Exception {
		InputStream inputStream = MarketDataStarter.class.getResourceAsStream("MDClientConnector.cfg");

		SessionSettings settings = new SessionSettings(inputStream);
		inputStream.close();

		connector = MarketDataConnector.getInstance();
		MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
		LogFactory logFactory = new FileLogFactory(settings);
		MessageFactory messageFactory = new DefaultMessageFactory();

		initiator = new SocketInitiator(connector, messageStoreFactory, settings, logFactory,	messageFactory);

		JmxExporter exporter = new JmxExporter();
		exporter.register(initiator);

	}

	private synchronized void setChannels() {
		if (!initiatorStarted) {
			try {
				initiator.start();
				initiatorStarted = true;

			} catch (Exception e) {
				Logging.getInstance().log(getClass(), e, Level.ERROR);
			}
		} 

		for (SessionID sessionId : initiator.getSessions()) {
			if(sessionId.getTargetCompID().equalsIgnoreCase(Streams.INCREMENTAL.name())){
				connector.setIncrementalStreamSID(sessionId);
			}
			else if(sessionId.getTargetCompID().equalsIgnoreCase(Streams.INSTRUMENT.name())){
				connector.setInstrumentDefinitionSID(sessionId);
			}
			else if(sessionId.getTargetCompID().equalsIgnoreCase(Streams.RECOVERY.name())){
				connector.setRecoveryStreamSID(sessionId);
			}
			else if(sessionId.getTargetCompID().equalsIgnoreCase(Streams.SNAPSHOT.name())){
				connector.setSnapshotStreamSID(sessionId);
			}
		}

	}

}
