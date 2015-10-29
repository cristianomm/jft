/**
 * 
 */
package com.cmm.jft.engine;

import static quickfix.Acceptor.SETTING_ACCEPTOR_TEMPLATE;
import static quickfix.Acceptor.SETTING_SOCKET_ACCEPT_ADDRESS;
import static quickfix.Acceptor.SETTING_SOCKET_ACCEPT_PORT;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.management.InstanceNotFoundException;
import javax.management.JMException;
import javax.management.MBeanRegistrationException;
import javax.management.ObjectName;

import org.apache.log4j.Level;
import org.quickfixj.jmx.JmxExporter;

import com.cmm.jft.core.services.Service;
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
 * <p><code>App.java</code></p>
 * @author Cristiano M Martins
 * @version 13 de jul de 2015 00:05:40
 *
 */
public class App implements Service {
	
	
	private EntryPoint entryPoint;
	private SocketAcceptor acceptor;
	private JmxExporter jmxExporter;
	private ObjectName connectorObjectName;
	private HashMap<InetSocketAddress, List<TemplateMapping>> dynamicSessionMappings;
	
	
	public App() {
		this.dynamicSessionMappings = new HashMap<InetSocketAddress, List<TemplateMapping>>();
		initListener();
	}
	
	
	private void initListener(){
		try{
			SessionSettings settings = new SessionSettings(App.class.getResourceAsStream("App.cfg"));
			
			entryPoint = new EntryPoint(settings);
			MessageStoreFactory storeFactory = new FileStoreFactory(settings);
			LogFactory logFactory = new FileLogFactory(settings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			
			acceptor = new SocketAcceptor(entryPoint, storeFactory, settings, logFactory, messageFactory);
			
			configureDynamicSessions(settings, entryPoint, storeFactory, logFactory, messageFactory);
			
			jmxExporter = new JmxExporter();
	        connectorObjectName = jmxExporter.register(acceptor);
	        
		}catch(ConfigError | FieldConvertError | JMException e){
			Logging.getInstance().log(getClass(), e, Level.FATAL);
		}

	}
	

	private void configureDynamicSessions(SessionSettings settings, Application application,
			MessageStoreFactory messageStoreFactory, LogFactory logFactory,
			MessageFactory messageFactory) throws ConfigError, FieldConvertError {
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
			acceptor.setSessionProvider(entry.getKey(), new DynamicAcceptorSessionProvider(
					settings, entry.getValue(), application, messageStoreFactory, logFactory, messageFactory));
		}
	}

	private List<TemplateMapping> getMappings(InetSocketAddress address) {
		List<TemplateMapping> mappings = dynamicSessionMappings.get(address);
		if (mappings == null) {
			mappings = new ArrayList<TemplateMapping>();
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


	/* (non-Javadoc)
	 * @see com.cmm.jft.core.services.Service#start()
	 */
	@Override
	public boolean start() {
		boolean running = false;
		try {
			acceptor.start();
			running = true;
		} catch (RuntimeError | ConfigError e) {
			e.printStackTrace();
		}

		return running;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.core.services.Service#stop()
	 */
	@Override
	public boolean stop() {
		boolean running = false;
		try {
			jmxExporter.getMBeanServer().unregisterMBean(connectorObjectName);
			acceptor.stop();
			running = false;
		} catch (MBeanRegistrationException | InstanceNotFoundException e) {
			e.printStackTrace();
		}

		return running;
	}
	
	public static void main(String[] args) {
		App listener = new App();
		listener.start();
	}
	

}