/**
 * 
 */
package com.cmm.jft.engine.marketdata.instrument;

import javax.management.JMException;

import org.slf4j.LoggerFactory;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionSettings;

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
    				Thread.currentThread().getContextClassLoader().getResourceAsStream("InstrumentDefinitionService.cfg"));
    		log = LoggerFactory.getLogger(InstrumentDefinitionService.class);
			init(settings, new InstrumentDefinition());
		} catch (ConfigError | FieldConvertError | JMException e) {
			log.error(e.getMessage());
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
        	e.printStackTrace();
            //log.error(e.getMessage());
        }
    }

}
