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

import com.cmm.jft.core.services.Service;
import com.cmm.jft.trading.enums.StreamTypes;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>MarketDataStarter.java</code>
 * </p>
 * 
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

    public boolean start() {
	try {
	    init();

	    if (!initiatorStarted) {
		try {
		    initiator.start();
		    initiatorStarted = true;
		} catch (Exception e) {
		    Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	    }

	    if (!System.getProperties().containsKey("openfix")) {
		setChannels();
	    }

	    //shutdownLatch.await();
	    started = true;

	} catch (Exception e) {
	    e.printStackTrace();
	}

	return started;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.core.services.Service#stop()
     */
    @Override
    public boolean stop() {
	shutdownLatch.countDown();
	return (started = false);
    }

    /* (non-Javadoc)
     * @see com.cmm.jft.core.services.Service#isStarted()
     */
    @Override
    public boolean isStarted() {
	return started;
    }

    private void init() throws Exception {
	InputStream inputStream = Thread.currentThread().getContextClassLoader()
		.getResourceAsStream("MDClientConnector.cfg");

	SessionSettings settings = new SessionSettings(inputStream);
	inputStream.close();

	connector = MarketDataConnector.getInstance();
	MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
	LogFactory logFactory = new FileLogFactory(settings);
	MessageFactory messageFactory = new DefaultMessageFactory();

	initiator = new SocketInitiator(connector, messageStoreFactory, settings, logFactory, messageFactory);

	JmxExporter exporter = new JmxExporter();
	exporter.register(initiator);

    }

    private synchronized void setChannels() {

	for (SessionID sessionId : initiator.getSessions()) {
	    if (sessionId.getTargetCompID().equalsIgnoreCase(StreamTypes.MARKET_DATA.name())) {
		connector.setMarketDataStreamSID(sessionId);
		connector.joinMarketDataStream();
	    } else if (sessionId.getTargetCompID().equalsIgnoreCase(StreamTypes.INSTRUMENT.name())) {
		connector.setInstrumentStreamSID(sessionId);
		connector.joinInstrumentStream();
	    } else if (sessionId.getTargetCompID().equalsIgnoreCase(StreamTypes.NEWS.name())) {
		connector.setNewsStreamSID(sessionId);
		connector.joinNewsStream();
	    } else if (sessionId.getTargetCompID().equalsIgnoreCase(StreamTypes.RECOVERY.name())) {
		connector.setRecoveryStreamSID(sessionId);
		connector.joinRecoveryStream();
	    } else if (sessionId.getTargetCompID().equalsIgnoreCase(StreamTypes.SNAPSHOT.name())) {
		connector.setSnapshotStreamSID(sessionId);
		connector.joinSnapshotStream();
	    }
	}

    }

}
