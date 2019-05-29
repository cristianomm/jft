/**
 * 
 */
package com.cmm.jft.model.financial.exceptions;

/**
 * <p>
 * <code>FormulaException.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 10/08/2013 03:53:15
 *
 */
@SuppressWarnings("serial")
public class FormulaException extends Throwable {

	/**
	 * 
	 */
	public FormulaException() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 * @param enableSuppression
	 * @param writableStackTrace
	 */
	public FormulaException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 * @param cause
	 */
	public FormulaException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public FormulaException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param cause
	 */
	public FormulaException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}