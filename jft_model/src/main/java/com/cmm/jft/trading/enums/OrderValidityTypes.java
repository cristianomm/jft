/**
 * 
 */
package com.cmm.jft.trading.enums;

/**
 * <p><code>OrderValidityTypes.java</code></p>
 * @author Cristiano
 * @version 8 de ago de 2015 00:37:10
 *
 */
public enum OrderValidityTypes {
	
	/**
	 * 	0 - DAY
	 */
	DAY('0'),
	
	/**
	 * 1 - GOOD TILL CANCEL
	 */
	GTC('1'),
	
	/**
	 * 3 - IMMEDIATE OR CANCEL
	 */
	IOC('3'),
	
	/**
	 * 4 - FILL OR KILL
	 */
	FOK('4'),
	
	/**
	 * 6 - GOOD TILL DATE
	 */
	GTD('6'),
	
	/**
	 * 7 - AT THE CLOSE
	 */
	ATC('7'),
	
	/**
	 * A - GOOD FOR AUCTION
	 */
	GFA('A');
	
	
	char value;
	OrderValidityTypes(char c){
		this.value= c;
	}
	
	public char getValue(){
		return value;
	}
	
	
}
