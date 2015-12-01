/**
 * 
 */
package com.cmm.jft.financial.exceptions;

/**
 * <p>
 * <code>AccountException.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Jul 25, 2013 4:46:01 AM
 * 
 */
public class AccountException extends Throwable {

	/**
     * 
     */
	private static final long serialVersionUID = -3890865275136505431L;

	/**
     * 
     */
	public AccountException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public AccountException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public AccountException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public AccountException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public AccountException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

}
