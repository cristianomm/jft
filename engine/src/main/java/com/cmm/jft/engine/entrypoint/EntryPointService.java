/**
 * 
 */
package com.cmm.jft.engine.entrypoint;

import javax.management.JMException;

import org.slf4j.LoggerFactory;

import com.cmm.jft.engine.Service;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionSettings;

/**
 * <p>
 * <code>EntryPointService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 13 de jul de 2015 00:05:40
 *
 */
public class EntryPointService extends Service {

    private EntryPoint entryPoint;
    
    public EntryPointService() {
	try {
	    SessionSettings settings = new SessionSettings(
		    Thread.currentThread().getContextClassLoader().getResourceAsStream("EntryPointService.cfg"));
	    	    
	    entryPoint = new EntryPoint(settings);
	    	    
	    init(settings, entryPoint);
	} catch (ConfigError | FieldConvertError | JMException e) {
	    e.printStackTrace();
	}
    }
    
    /**
     * @return the entryPoint
     */
    public EntryPoint getEntryPoint() {
	return entryPoint;
    }

    public static void main(String[] args) {
	EntryPointService listener = new EntryPointService();
	listener.start();
    }

}
