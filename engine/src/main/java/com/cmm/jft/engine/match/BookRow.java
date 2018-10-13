/**
 * 
 */
package com.cmm.jft.engine.match;

import com.cmm.jft.trading.Orders;

/**
 * <p>
 * <code>BookRow.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 03/03/2017 11:40:29
 *
 */
public class BookRow {

	private BookRow nextRow;
	private BookRow prevRow;

	private int position;
	private String orderID;
	private double price;
	private Orders order;

	public BookRow(int position, String orderID, double price, Orders order) {
		this.position = position;
		this.orderID = orderID;
		this.price = price;
		this.order = order;
	}

	/**
	 * @return the nextRow
	 */
	public BookRow getNextRow() {
		return nextRow;
	}

	/**
	 * @return the order
	 */
	public Orders getOrder() {
		return order;
	}

	/**
	 * @return the orderID
	 */
	public String getOrderID() {
		return orderID;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @return the prevRow
	 */
	public BookRow getPrevRow() {
		return prevRow;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

	public void changePosition(int position, BookRow prevRow, BookRow nextRow) {
		this.nextRow = nextRow;
		this.prevRow = prevRow;
		this.position = position;
	}

}
