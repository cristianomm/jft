/**
 * 
 */
package com.cmm.jft.engine;

/**
 * <p>
 * <code>OrderValidationException.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 25/08/2017 02:47:43
 *
 */
public class OrderValidationException extends Throwable {

	private int errorCode;
	private String errorMsg;

	/**
	 * 
	 */
	public OrderValidationException(int errorCode, String msg) {
		this.errorCode = errorCode;
		this.errorMsg = msg;
	}

	/**
	 * @param arg0
	 */
	public OrderValidationException(int errorCode, String errorMsg, Throwable thrw) {
		super(thrw);
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}

	/**
	 * @return the errorCode
	 */
	public int getErrorCode() {
		return errorCode;
	}

	/**
	 * @return the errorMsg
	 */
	public String getErrorMsg() {
		return errorMsg;
	}

}
