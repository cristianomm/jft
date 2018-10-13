/**
 * 
 */
package com.cmm.jft.db.exceptions;

/**
 * <p><code>DataBaseException.java</code></p>
 * @author Cristiano Martins
 * @version 05/09/2013 14:20:13
 *
 */
public class DataBaseException extends Throwable {

    /**
     * 
     */
    public DataBaseException() {
	super();
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     */
    public DataBaseException(String message, Throwable cause,
	    boolean enableSuppression, boolean writableStackTrace) {
	super(message, cause, enableSuppression, writableStackTrace);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public DataBaseException(String message, Throwable cause) {
	super(message, cause);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param message
     */
    public DataBaseException(String message) {
	super(message);
	// TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public DataBaseException(Throwable cause) {
	super(cause);
	// TODO Auto-generated constructor stub
    }

}
