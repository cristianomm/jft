package com.cmm.jft.trading.enums;

public enum RejectTypes {
	
	/**
	 * 1 - ORDER CANCEL REQUEST
	 */
	OrderCancelRequest('1'),
	
	/**
	 * 2 - ORDER CANCEL REPLACE REQUEST
	 */
	OrderCancelReplaceRequest('2');
	
	char value;
	
	private RejectTypes(char value) {
		this.value = value;
	}
	
	public char getValue() {
		return value;
	}
	
	public static RejectTypes getByValue(char value) {
		RejectTypes ret = null;
		if(value == OrderCancelRequest.value) {
			ret = OrderCancelRequest;
		}
		else if(value == OrderCancelReplaceRequest.value) {
			ret = OrderCancelReplaceRequest;
		}
		
		return ret;
	}
	
}
