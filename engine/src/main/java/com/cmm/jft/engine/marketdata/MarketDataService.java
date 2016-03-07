/**
 * 
 */
package com.cmm.jft.engine.marketdata;

import javax.management.JMException;

import org.slf4j.LoggerFactory;

import com.cmm.jft.core.services.Service;
import com.cmm.jft.engine.EngineService;
import com.cmm.jft.engine.EntryPoint;
import com.cmm.jft.engine.EntryPointService;

import quickfix.ConfigError;
import quickfix.FieldConvertError;
import quickfix.SessionSettings;

/**
 * <p><code>MarketDataService.java</code></p>
 * @author cristiano
 * @version Mar 3, 2016 5:05:45 PM
 *
 */
public class MarketDataService extends EngineService {
	
	
	/**
	 * 
	 */
	public MarketDataService() {
		try {
			SessionSettings settings = new SessionSettings(
					EntryPointService.class.getResourceAsStream("MarketDataService.cfg"));
			log = LoggerFactory.getLogger(MarketDataService.class);
			init(settings, new EntryPoint(settings));
		} catch (ConfigError | FieldConvertError | JMException e) {
			e.printStackTrace();
		}
	}
	
	
}
