/**
 * 
 */
package com.cmm.jft.core.exceptions;

/**
 * <p>
 * <code>EntryClosedException.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Jul 21, 2013 2:36:25 AM
 * 
 */
public class EntryClosedException extends Throwable {

	/**
     * 
     */
	private static final long serialVersionUID = -6071532899414617296L;

	/**
     * 
     */
	public EntryClosedException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public EntryClosedException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public EntryClosedException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public EntryClosedException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public EntryClosedException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
