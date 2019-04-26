/**
 * 
 */
package com.cmm.jft.model.trading.enums;

/**
 * <p><code>WorkingIndicator.java</code></p>
 * @author Cristiano
 * @version 26 de ago de 2015 20:35:10
 *
 */
public enum WorkingIndicator {

	Working('Y'), No_Working('N');
	
	
	char value;
	/**
	 * 
	 */
	private WorkingIndicator(char value) {
		this.value = value;
	}
	
	
	/**
	 * @return the value
	 */
	public char getValue() {
		return this.value;
	}
	
	public static WorkingIndicator getByValue(char value){
		WorkingIndicator ret = No_Working;
		
		switch(value){
		case 'N':
			ret = No_Working;
			break;
			
		case 'Y':
			ret = Working;
		}
		
		return ret;
	}
	
}
