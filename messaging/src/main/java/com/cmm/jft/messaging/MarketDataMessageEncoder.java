/**
 * 
 */
package com.cmm.jft.messaging;

import java.util.HashMap;

import com.cmm.jft.security.Security;

import quickfix.Field;
import quickfix.Message;

/**
 * <p><code>MarketDataMessageEncoder.java</code></p>
 * @author Cristiano M Martins
 * @version Feb 26, 2016 4:24:04 PM
 *
 */
public interface MarketDataMessageEncoder extends MessageEncoder {

	Message sequenceReset();
	
	Message securityList(Security[] securities);
	
	Message mdIncrementalRefresh(HashMap<Integer, Object> values[]);
	
	Message mdSnapShotFullRefresh();
	
	Message securityStatus();
	
	Message nonFixData();
	
}
