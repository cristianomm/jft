/**
 * 
 */
package com.cmm.jft.engine.marketdata;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.management.JMException;

import org.apache.log4j.Level;

import com.cmm.jft.engine.Service;
import com.cmm.jft.engine.Stream;
import com.cmm.jft.engine.enums.MarketDataTypes;
import com.cmm.jft.engine.marketdata.instrument.InstrumentStream;
import com.cmm.jft.engine.marketdata.news.NewsStream;
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
    
    private int channel;
    private ILog logger;
    private List<Service> services;
    private MarketDataTypes marketDataType;
    private ExecutorService executorService;
    
    public MarketDataChannelDefinition(int channel, MarketDataTypes marketDataType) {
	this.channel = channel;
	this.logger = Logging.getInstance();
	this.marketDataType = marketDataType;
	this.services = new LinkedList<>();
	this.executorService = Executors.newFixedThreadPool(4);
    }
    
        
    public void register(Stream stream) {
	try {
	    logger.log(getClass(), "Registering stream " + stream + "at channel " + channel, Level.INFO);
	    services.add(new Service(stream));
	} catch (ConfigError | FieldConvertError | JMException e) {
	    logger.log(getClass(), e, Level.ERROR);
	}
    }
    
    public void startServices() {
	logger.log(getClass(), "Starting streams for channel " + channel, Level.INFO);
	services.forEach(s -> s.start());
	
	services.forEach(s -> executorService.execute(s.getStreamApplication()));
    }
        
    public static void main(String[] args) {
	MarketDataChannelDefinition m051 = new MarketDataChannelDefinition(51, MarketDataTypes.MBO);
	
	m051.register(InstrumentStream.getInstance());
	m051.register(NewsStream.getInstance());
	m051.startServices();
	
    }
    
}
