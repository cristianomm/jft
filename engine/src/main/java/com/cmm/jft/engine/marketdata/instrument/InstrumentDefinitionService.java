/**
 * 
 */
package com.cmm.jft.engine.marketdata.instrument;

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
import org.slf4j.LoggerFactory;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FieldConvertError;
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

import com.cmm.jft.core.services.Service;
import com.cmm.jft.engine.EngineService;
import com.cmm.jft.engine.EntryPointService;

/**
 * <p><code>InstrumentDefinitionService.java</code></p>
 * @author Cristiano M Martins
 * @version 13 de jul de 2015 00:05:40
 *
 */
public class InstrumentDefinitionService extends EngineService {
    
    public InstrumentDefinitionService() {
    	try {
    		SessionSettings settings = new SessionSettings(
    				EntryPointService.class.getResourceAsStream("InstrumentDefinitionService.cfg"));
    		log = LoggerFactory.getLogger(InstrumentDefinitionService.class);
			init(settings, new InstrumentDefinition());
		} catch (ConfigError | FieldConvertError | JMException e) {
			e.printStackTrace();
		}
    }
    
    
    public static void main(String args[]) throws Exception {
        try {
            InstrumentDefinitionService service = new InstrumentDefinitionService();
            service.start();

            System.out.println("press <enter> to quit");
            System.in.read();

            service.stop();
        } catch (Exception e) {
            //log.error(e.getMessage(), e);
        }
    }

    
	

}
