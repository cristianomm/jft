/**
 * 
 */
package com.cmm.jft.messaging;

import com.cmm.jft.security.Security;

import quickfix.Message;

/**
 * <p><code>MarketDataMessageEncoder.java</code></p>
 * @author cristiano
 * @version Feb 26, 2016 4:24:04 PM
 *
 */
public interface MarketDataMessageEncoder extends MessageEncoder {

	Message sequenceReset();
	
	Message securityList(Security security);
	
	Message mdIncrementalRefresh();
	
	Message mdSnapShotFullRefresh();
	
	Message securityStatus();
	
	Message nonFixData();
	
	
	
}
