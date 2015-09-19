package com.cmm.jft.ui.controller;

import com.cmm.jft.trading.enums.Side;

/**
 * 
 * <p><code>DOMElement.java</code></p>
 * @author Cristiano M Martins
 * @version 19 de set de 2015 00:47:46
 *
 */
public class DOMElement {

	private Side side;
	private double price;
	private int volume;

	/**
	 * 
	 */
	public DOMElement(Side side, double price, int volume) {
		this.price = price;
		this.side = side;
		this.volume = volume;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return this.price;
	}

	/**
	 * @return the side
	 */
	public Side getSide() {
		return this.side;
	}
	/**
	 * @return the volume
	 */
	public int getVolume() {
		return this.volume;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @param side the side to set
	 */
	public void setSide(Side side) {
		this.side = side;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(int volume) {
		this.volume = volume;
	}

}
