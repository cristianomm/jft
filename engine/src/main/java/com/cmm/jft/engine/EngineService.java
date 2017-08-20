/**
 * 
 */
package com.cmm.jft.engine;

import static quickfix.Acceptor.SETTING_ACCEPTOR_TEMPLATE;
import static quickfix.Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS;
import static quickfix.Acceptor.SETTING_SOCKET_ACCEPT_PORT;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.JMException;
import javax.management.ObjectName;

import org.quickfixj.jmx.JmxExporter;
import org.slf4j.Logger;

import com.cmm.jft.core.services.Service;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldConvertError;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.RuntimeError;
import quickfix.ScreenLogFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider.TemplateMapping;

/**
 * <p>
 * <code>EngineService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Mar 3, 2016 5:07:35 PM
 *
 */
public class EngineService implements Service {

    protected boolean started;
    protected Logger log;
    protected SocketAcceptor acceptor;
    protected Map<InetSocketAddress, List<TemplateMapping>> dynamicSessionMappings = new HashMap<InetSocketAddress, List<TemplateMapping>>();

    protected JmxExporter jmxExporter;
    protected ObjectName connectorObjectName;

    protected void init(SessionSettings settings, Application application)
	    throws ConfigError, FieldConvertError, JMException {
	
	MessageStoreFactory messageStoreFactory = new FileStoreFactory(settings);
	LogFactory logFactory = new FileLogFactory(settings);// ScreenLogFactory(true, true, true);
	MessageFactory messageFactory = new DefaultMessageFactory();

	acceptor = new SocketAcceptor(application, messageStoreFactory, settings, logFactory, messageFactory);

	configureDynamicSessions(settings, application, messageStoreFactory, logFactory, messageFactory);

	jmxExporter = new JmxExporter();
	connectorObjectName = jmxExporter.register(acceptor);
	log.info("Acceptor registered with JMX, name=" + connectorObjectName);

    }

    private void configureDynamicSessions(SessionSettings settings, Application application,
	    MessageStoreFactory messageStoreFactory, LogFactory logFactory, MessageFactory messageFactory)
		    throws ConfigError, FieldConvertError {
	//
	// If a session template is detected in the settings, then
	// set up a dynamic session provider.
	Iterator<SessionID> sectionIterator = settings.sectionIterator();
	while (sectionIterator.hasNext()) {
	    SessionID sessionID = sectionIterator.next();
	    if (isSessionTemplate(settings, sessionID)) {
		InetSocketAddress address = getAcceptorSocketAddress(settings, sessionID);
		getMappings(address).add(new TemplateMapping(sessionID, sessionID));
	    }
	}

	for (Map.Entry<InetSocketAddress, List<TemplateMapping>> entry : dynamicSessionMappings.entrySet()) {
	    acceptor.setSessionProvider(entry.getKey(), new DynamicAcceptorSessionProvider(settings, entry.getValue(),
		    application, messageStoreFactory, logFactory, messageFactory));
	}
    }

    protected List<TemplateMapping> getMappings(InetSocketAddress address) {
	List<TemplateMapping> mappings = dynamicSessionMappings.get(address);
	if (mappings == null) {
	    mappings = new ArrayList<TemplateMapping>();
	    dynamicSessionMappings.put(address, mappings);
	}
	return mappings;
    }

    protected InetSocketAddress getAcceptorSocketAddress(SessionSettings settings, SessionID sessionID)
	    throws ConfigError, FieldConvertError {
	String acceptorHost = "0.0.0.0";
	if (settings.isSetting(sessionID, SETTING_SOCKET_ACCEPT_ADDRESS)) {
	    acceptorHost = settings.getString(sessionID, SETTING_SOCKET_ACCEPT_ADDRESS);
	}
	int acceptorPort = (int) settings.getLong(sessionID, SETTING_SOCKET_ACCEPT_PORT);

	InetSocketAddress address = new InetSocketAddress(acceptorHost, acceptorPort);
	return address;
    }

    protected boolean isSessionTemplate(SessionSettings settings, SessionID sessionID)
	    throws ConfigError, FieldConvertError {
	return settings.isSetting(sessionID, SETTING_ACCEPTOR_TEMPLATE)
		&& settings.getBool(sessionID, SETTING_ACCEPTOR_TEMPLATE);
    }

    // protected static InputStream getSettingsInputStream(String[] args) throws
    // FileNotFoundException {
    // InputStream inputStream = null;
    // if (args.length == 0) {
    // inputStream = Thread.currentThread().getContextClassLoader()
    // .getResourceAsStream("InstrumentDefinitionService.cfg");
    // } else if (args.length == 1) {
    // inputStream = new FileInputStream(args[0]);
    // }
    // if (inputStream == null) {
    // System.out.println("usage: " +
    // InstrumentDefinitionService.class.getName() + " [configFile].");
    // System.exit(1);
    // }
    // return inputStream;
    // }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.core.services.Service#start()
     */
    @Override
    public boolean start() {
	try {
	    acceptor.start();
	    started = true;
	} catch (RuntimeError | ConfigError e) {
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
	try {
	    jmxExporter.getMBeanServer().unregisterMBean(connectorObjectName);
	    started = false;
	} catch (Exception e) {
	    log.error("Failed to unregister acceptor from JMX", e);
	}
	acceptor.stop();
	return started;
    }

    /**
     * @return the started
     */
    public boolean isStarted() {
	return started;
    }
}
