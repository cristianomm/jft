/**
 * 
 */
package com.cmm.jft.model.trading.exceptions;

/**
 * <p>
 * <code>OrderException.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 23/08/2013 17:00:20
 *
 */
public class OrderException extends Exception {

	/**
     * 
     */
	public OrderException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public OrderException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public OrderException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public OrderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public OrderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
