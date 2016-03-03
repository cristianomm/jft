/**
 * 
 */
package com.cmm.jft.engine;

import javax.management.JMException;

import org.slf4j.LoggerFactory;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionSettings;

/**
 * <p><code>EntryPointService.java</code></p>
 * @author Cristiano M Martins
 * @version 13 de jul de 2015 00:05:40
 *
 */
public class EntryPointService extends EngineService {
	
	
	public EntryPointService() {
		try {
			SessionSettings settings = new SessionSettings(
					EntryPointService.class.getResourceAsStream("EntryPointService.cfg"));
			log = LoggerFactory.getLogger(EntryPointService.class);
			init(settings, new EntryPoint(settings));
		} catch (ConfigError | FieldConvertError | JMException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		EntryPointService listener = new EntryPointService();
		listener.start();
	}
	

}
