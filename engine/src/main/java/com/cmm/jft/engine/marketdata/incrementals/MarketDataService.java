/**
 * 
 */
package com.cmm.jft.engine.marketdata.incrementals;

import javax.management.JMException;

import org.apache.log4j.Level;
import org.slf4j.LoggerFactory;

import com.cmm.jft.engine.Service;
import com.cmm.jft.engine.entrypoint.EntryPoint;
import com.cmm.jft.engine.entrypoint.EntryPointService;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionSettings;

/**
 * <p>
 * <code>MarketDataService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Mar 3, 2016 5:05:45 PM
 *
 */
public class MarketDataService extends Service {

    public MarketDataService() {
	try {
	    SessionSettings settings = new SessionSettings(
		    Thread.currentThread().getContextClassLoader().getResourceAsStream("MarketDataService.cfg"));
	    init(settings, MarketDataChannel.getInstance());
	} catch (ConfigError | FieldConvertError | JMException e) {
	    logger.log(getClass(), e, Level.ERROR);
	    e.printStackTrace();
	}
    }

    public static void main(String[] args) {
	try {
	    MarketDataService service = new MarketDataService();
	    service.start();

	    System.out.println("press <enter> to quit");
	    System.in.read();

	    service.stop();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
