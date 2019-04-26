/**
 * 
 */
package com.cmm.jft.model.trading.exceptions;

/**
 * <p>
 * <code>InvalidOrderException.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Jul 20, 2013 3:58:10 AM
 *
 */
public class InvalidOrderException extends Throwable {

	/**
     * 
     */
	private static final long serialVersionUID = 2436412499384789279L;

	/**
     * 
     */
	public InvalidOrderException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public InvalidOrderException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public InvalidOrderException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public InvalidOrderException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public InvalidOrderException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
