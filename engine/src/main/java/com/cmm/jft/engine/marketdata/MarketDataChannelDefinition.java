/**
 * 
 */
package com.cmm.jft.engine.marketdata;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.JMException;

import com.cmm.jft.engine.Service;
import com.cmm.jft.engine.Stream;
import com.cmm.jft.engine.enums.MarketDataTypes;
import com.cmm.jft.engine.marketdata.instrument.InstrumentStream;
import com.cmm.logging.ILog;
import com.cmm.logging.Logging;

import quickfix.ConfigError;
import quickfix.FieldConvertError;

/**
 * <p>
 * <code>MarketDataChannelDefinition.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 25/07/2018 11:31:22
 *
 */
public class MarketDataChannelDefinition {
    
    private ILog logger;
    private List<Service> services;
    private MarketDataTypes marketDataType;    
    
    public MarketDataChannelDefinition(MarketDataTypes marketDataType) {
	this.logger = Logging.getInstance();
	this.marketDataType = marketDataType;
	this.services = new LinkedList<>();
    }
    
        
    public void register(Stream stream) {
	try {
	    services.add(new Service(stream));
	} catch (ConfigError | FieldConvertError | JMException e) {
	    e.printStackTrace();
	}
    }
    
    public void startServices() {
	services.forEach(s -> s.start());
    }
    
    public static void main(String[] args) {
	MarketDataChannelDefinition m051 = new MarketDataChannelDefinition(MarketDataTypes.MBO);
	
	m051.register(InstrumentStream.getInstance());
	m051.startServices();
	
    }
    
    
    
    
}
