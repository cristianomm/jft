/**
 * 
 */
package com.cmm.jft.data.exceptions;

/**
 * <p>
 * <code>StreamException.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 02/08/2013 11:54:31
 *
 */
public class StreamException extends Throwable {

	/**
     * 
     */
	private static final long serialVersionUID = 1L;

	/**
     * 
     */
	public StreamException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public StreamException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public StreamException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public StreamException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public StreamException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
