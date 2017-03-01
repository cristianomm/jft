/**
 * 
 */
package com.cmm.jft.engine.marketdata.news;

import javax.management.JMException;

import org.slf4j.LoggerFactory;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionSettings;

import com.cmm.jft.engine.EngineService;

/**
 * <p>
 * <code>InstrumentDefinitionService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 23/02/2017 00:13:56
 *
 */
public class NewsService extends EngineService {

    public NewsService() {
	try {
	    SessionSettings settings = new SessionSettings(Thread.currentThread().getContextClassLoader()
		    .getResourceAsStream("NewsService.cfg"));
	    log = LoggerFactory.getLogger(NewsService.class);
	    init(settings, new News());
	} catch (ConfigError | FieldConvertError | JMException e) {
	    log.error(e.getMessage());
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
