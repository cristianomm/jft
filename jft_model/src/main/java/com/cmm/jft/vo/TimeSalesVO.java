/**
 * 
 */
package com.cmm.jft.vo;

import java.util.Date;

/**
 * <p><code>TimeSalesVO.java</code></p>
 * @author cristiano
 * @version Oct 29, 2015 10:40:50 PM
 *
 */
public class TimeSalesVO {
	
	public Date dateTime;
	public double price;
	public double volume;
	public String buyer;
	public String seller;
	public char side;
	/**
	 * @return the dateTime
	 */
	public Date getDateTime() {
		return this.dateTime;
	}
	/**
	 * @param dateTime the dateTime to set
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	/**
	 * @return the price
	 */
	public double getPrice() {
		return this.price;
	}
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}
	/**
	 * @return the volume
	 */
	public double getVolume() {
		return this.volume;
	}
	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}
	/**
	 * @return the buyer
	 */
	public String getBuyer() {
		return this.buyer;
	}
	/**
	 * @param buyer the buyer to set
	 */
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	/**
	 * @return the seller
	 */
	public String getSeller() {
		return this.seller;
	}
	/**
	 * @param seller the seller to set
	 */
	public void setSeller(String seller) {
		this.seller = seller;
	}
	/**
	 * @return the side
	 */
	public char getSide() {
		return this.side;
	}
	/**
	 * @param side the side to set
	 */
	public void setSide(char side) {
		this.side = side;
	}
	
}
