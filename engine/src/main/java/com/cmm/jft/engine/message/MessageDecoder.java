/**
 * 
 */
package com.cmm.jft.engine.message;

import com.cmm.jft.trading.marketdata.MarketOrder;

/**
 * <p><code>MessageDecoder.java</code></p>
 * @author Cristiano M Martins
 * @version 17/06/2015 17:31:40
 *
 */
public interface MessageDecoder {

	
	MarketOrder decodeOrderSingle();
	
}
