/**
 * 
 */
package com.cmm.jft.engine.marketdata;

import com.cmm.jft.messaging.MarketDataMessageEncoder;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;

import quickfix.Group;

/**
 * <p><code>UMDF.java</code></p>
 * @author Cristiano M Martins
 * @version Feb 26, 2016 3:57:55 PM
 *
 */
public class UMDF {
	
	private static UMDF instance;
	
	private MarketDataMessageEncoder encoder;

	
	
	private UMDF(){
		
	}
	
	/**
	 * @return the instance
	 */
	public static synchronized UMDF getInstance() {
		if (instance == null) {
			instance = new UMDF();
		}
		return instance;
	}
	
	
	/**
	 * @param order
	 */
	public void informNewOrder(Orders order) {
		 
		//Group g = new Group(269, delim);
		informMarket();
	}
	
	public void informUpdateOrder(Orders order){
		
	}
	
	
	
	public void informTrade(OrderEvent orderFill){
		
	}
	
	public void informNews(String news){
		
	}
	
	
	
	
	public void mbo(){
		
	}
	
	public void mbp(){
		
	}
	
	public void tob(){
		
	}
	
	
	/**
	 * Informa ao mercado os eventos ocorridos
	 */
	public void informMarket(){
		
		mbo();
		mbp();
		tob();
		
	}
	
	
}
