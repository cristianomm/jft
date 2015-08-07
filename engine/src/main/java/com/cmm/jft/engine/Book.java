/**
 * 
 */
package com.cmm.jft.engine;

import java.util.HashSet;
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
	private HashSet<String> validOrderTypes;
	private ConcurrentLinkedQueue<MarketOrder> buyQueue;
	private ConcurrentLinkedQueue<MarketOrder> sellQueue;
	
	
	public Book(String symbol, HashSet<String> orderTypes){
		this.symbol = symbol;
		this.validOrderTypes = orderTypes;
		this.buyQueue = new ConcurrentLinkedQueue<>();
		this.sellQueue = new ConcurrentLinkedQueue<>();
	}
	
	
	/**
	 * @return the buyOrders
	 */
	public ConcurrentLinkedQueue<MarketOrder> getBuyOrders() {
		return this.buyQueue;
	}
	
	/**
	 * @return the sellOrders
	 */
	public ConcurrentLinkedQueue<MarketOrder> getSellOrders() {
		return this.sellQueue;
	}
	
	public String getSymbol() {
		return symbol;
	}
	
	public void getBookInfo() {
		
	}
	
	public void validateOrder() {
		
	}
	
	public void addOrder() {
		
	}
	
	public void removeOrder() {
		
	}
	
	
	
}
