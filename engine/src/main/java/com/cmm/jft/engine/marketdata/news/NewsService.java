/**
 * 
 */
package com.cmm.jft.engine.marketdata.news;

import javax.management.JMException;

import org.apache.log4j.Level;
import org.slf4j.LoggerFactory;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionSettings;

import com.cmm.jft.engine.Service;

/**
 * <p>
 * <code>InstrumentDefinitionService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 23/02/2017 00:13:56
 *
 */
public class NewsService extends Stream {

    public NewsService() {
	try {
	    SessionSettings settings = new SessionSettings(Thread.currentThread().getContextClassLoader()
		    .getResourceAsStream("NewsService.cfg"));
	    
	    init(settings, NewsChannel.getInstance());
	} catch (ConfigError | FieldConvertError | JMException e) {
	    logger.log(getClass(), e, Level.ERROR);
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) throws Exception {
	try {
	    NewsService service = new NewsService();
	    service.start();

	    System.out.println("press <enter> to quit");
	    System.in.read();

	    service.stop();
	} catch (Exception e) {
	    e.printStackTrace();
	    // log.error(e.getMessage());
	}
    }

}
