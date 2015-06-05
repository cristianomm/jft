/**
 * 
 */
package com.cmm.jft.trading;

/**
 * <p>
 * <code>OrdersPrices.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 08/08/2013 02:37:12
 *
 */
public class OrdersPrices {

	public double limit;
	public double price;
	public double start;
	public double stop;
	public double gain;

	public OrdersPrices() {
		this.limit = -1;
		this.price = -1;
		this.start = -1;
		this.stop = -1;
		this.gain = -1;
	}

}
