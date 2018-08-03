/**
 * 
 */
package com.cmm.jft.engine;

import static quickfix.Acceptor.SETTING_ACCEPTOR_TEMPLATE;
import static quickfix.Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS;
import static quickfix.Acceptor.SETTING_SOCKET_ACCEPT_PORT;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.JMException;
import javax.management.ObjectName;

import org.apache.log4j.Level;
import org.quickfixj.jmx.JmxExporter;

import com.cmm.logging.ILog;
import com.cmm.logging.Logging;

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
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider;
import quickfix.mina.acceptor.DynamicAcceptorSessionProvider.TemplateMapping;

/**
 * <p>
 * <code>Service.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Mar 3, 2016 5:07:35 PM
 *
 */
public class Service {
    
    private ILog logger;
    private Stream streamApplication;
    private SocketAcceptor acceptor;
    private Map<InetSocketAddress, List<TemplateMapping>> dynamicSessionMappings;

    private JmxExporter jmxExporter;
    private ObjectName connectorObjectName;

    /**
     * @throws JMException 
     * @throws FieldConvertError 
     * @throws ConfigError 
     * 
     */
    public Service(Stream stream) throws ConfigError, FieldConvertError, JMException {
	streamApplication = stream;
	logger = Logging.getInstance();
	dynamicSessionMappings = new HashMap<>();
	init();
    }

    private void init() throws ConfigError, FieldConvertError, JMException {
	
	MessageStoreFactory messageStoreFactory = new FileStoreFactory(streamApplication.getSessionSettings());
	LogFactory logFactory = new FileLogFactory(streamApplication.getSessionSettings());
	MessageFactory messageFactory = new DefaultMessageFactory();
	
	acceptor = new SocketAcceptor(streamApplication, messageStoreFactory, streamApplication.getSessionSettings(), logFactory, messageFactory);
	
	configureDynamicSessions(streamApplication.getSessionSettings(), streamApplication, messageStoreFactory, logFactory, messageFactory);
	
	jmxExporter = new JmxExporter();
	connectorObjectName = jmxExporter.register(acceptor);
	logger.log(getClass(), "Acceptor registered with JMX, name=" + connectorObjectName, Level.INFO);
    }

    private void configureDynamicSessions(SessionSettings settings, Application application,
	    MessageStoreFactory messageStoreFactory, LogFactory logFactory, MessageFactory messageFactory)
		    throws ConfigError, FieldConvertError {
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

    private List<TemplateMapping> getMappings(InetSocketAddress address) {
	List<TemplateMapping> mappings = dynamicSessionMappings.get(address);
	if (mappings == null) {
	    mappings = new ArrayList<>();
	    dynamicSessionMappings.put(address, mappings);
	}
	return mappings;
    }

    private InetSocketAddress getAcceptorSocketAddress(SessionSettings settings, SessionID sessionID)
	    throws ConfigError, FieldConvertError {
	String acceptorHost = "0.0.0.0";
	if (settings.isSetting(sessionID, SETTING_SOCKET_ACCEPT_ADDRESS)) {
	    acceptorHost = settings.getString(sessionID, SETTING_SOCKET_ACCEPT_ADDRESS);
	}
	int acceptorPort = (int) settings.getLong(sessionID, SETTING_SOCKET_ACCEPT_PORT);

	return new InetSocketAddress(acceptorHost, acceptorPort);
    }

    private boolean isSessionTemplate(SessionSettings settings, SessionID sessionID)
	    throws ConfigError, FieldConvertError {
	return settings.isSetting(sessionID, SETTING_ACCEPTOR_TEMPLATE)
		&& settings.getBool(sessionID, SETTING_ACCEPTOR_TEMPLATE);
    }
    
    public void start() {
	try {
	    streamApplication.start();
	    acceptor.start();
	} catch (RuntimeError | ConfigError e) {
	    e.printStackTrace();
	}
    }
    
    public void stop() {
	try {
	    streamApplication.stop();
	    jmxExporter.getMBeanServer().unregisterMBean(connectorObjectName);
	    acceptor.stop();
	} catch (Exception e) {
	    logger.log(getClass(), "Failed to unregister acceptor from JMX", e, Level.ERROR, false);
	}
    }
    
    /**
     * @return the streamApplication
     */
    public Stream getStreamApplication() {
	return streamApplication;
    }

}
