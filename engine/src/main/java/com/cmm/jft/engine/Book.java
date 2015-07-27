/**
 * 
 */
package com.cmm.jft.engine;

import java.util.concurrent.ConcurrentLinkedQueue;

import com.cmm.jft.trading.marketdata.MarketOrder;

/**
 * <p><code>Book.java</code></p>
 * @author Cristiano M Martins
 * @version 26 de jul de 2015 02:23:19
 *
 */
public class Book {
	
	private String symbol;
	private ConcurrentLinkedQueue<MarketOrder> buyOrders;
	private ConcurrentLinkedQueue<MarketOrder> sellOrders;
	
	
	public Book(String symbol){
		this.symbol = symbol;
		this.buyOrders = new ConcurrentLinkedQueue<>();
		this.sellOrders = new ConcurrentLinkedQueue<>();
	}
	
	
	/**
	 * @return the buyOrders
	 */
	public ConcurrentLinkedQueue<MarketOrder> getBuyOrders() {
		return this.buyOrders;
	}
	
	/**
	 * @return the sellOrders
	 */
	public ConcurrentLinkedQueue<MarketOrder> getSellOrders() {
		return this.sellOrders;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
}
