/**
 * 
 */
package com.cmm.jft.messaging;

import java.util.HashMap;

import com.cmm.jft.security.Security;

import quickfix.Field;
import quickfix.Message;
import quickfix.fix44.MarketDataIncrementalRefresh;

/**
 * <p><code>MarketDataMessageEncoder.java</code></p>
 * @author Cristiano M Martins
 * @version Feb 26, 2016 4:24:04 PM
 *
 */
public interface MarketDataMessageEncoder extends MessageEncoder {

	Message sequenceReset();
	
	Message securityList(Security[] securities);
	
	Message mdIncrementalRefresh(MarketDataIncrementalRefresh.NoMDEntries entries[]);
	
	Message mdSnapShotFullRefresh();
	
	Message securityStatus();
	
	Message news(String title, String message);
	
	Message nonFixData();
	
}
