package com.cmm.jft.model.trading.enums;

/**
 * <p>
 * <code>ExecutionTypes.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 10/08/2015 15:57:55
 *
 */
public enum ExecutionTypes {
	
	/**
	 * 0 - NEW
	 */
	NEW('0'),
	
	/**
	 * 4 - CANCELED
	 */
	CANCELED('4'),
	
	/**
	 * 5 - REPLACE
	 */
	REPLACE('5'),
	
	/**
	 * 8 - REJECTED
	 */
	REJECTED('8'),
	
	/**
	 * 9 - SUSPENDED
	 */
	SUSPENDED('9'),
	
	/**
	 * C - EXPIRED
	 */
	EXPIRED('C'),
	
	/**
	 * D - RESTATED
	 */
	RESTATED('D'),
	
	/**
	 * F - TRADE
	 */
	TRADE('F'),
	
	/**
	 * H - TRADE CANCEL
	 */
	TRADE_CANCEL('H');	

	
	char value;
	
	private ExecutionTypes(char value) {
		this.value = value;
	}
	
	public char getValue() {
		return value;
	}
	
	public static ExecutionTypes getByValue(char value) {
		ExecutionTypes ret = null;
		
		for(ExecutionTypes et:ExecutionTypes.values()) {
			if(et.value == value) {
				ret = et;
				break;
			}
		}
		
		return ret;
	}
	
	
}
