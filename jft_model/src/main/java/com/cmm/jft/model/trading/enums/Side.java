/**
 * 
 */
package com.cmm.jft.model.trading.enums;

/**
 * <p>
 * <code>Side.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Jul 21, 2013 2:05:24 AM
 *
 */
public enum Side {
    BUY('1'), SELL('2');

    char value;

    Side(char value) {
	this.value = value;
    }

    public char getValue(){
	return value;
    }

    public static Side getByValue(char value) {

	Side s = null;
	if (value == 'b' || value == 'c' || value == '1') {
	    s = BUY;
	} else if (value == 's' || value == 'v' || value == '2') {
	    s = SELL;
	}
	return s;
    }
    
    public static Side getByValue(String value) {
	
	
	Side s = null;
	if (value.equalsIgnoreCase("b") || value.equalsIgnoreCase("c") || value.equalsIgnoreCase("1")) {
	    s = BUY;
	} else if (value.equalsIgnoreCase("s") || value.equalsIgnoreCase("v") || value.equalsIgnoreCase("2")) {
	    s = SELL;
	} else if(value.equalsIgnoreCase("compra") || value.equalsIgnoreCase("buy")) {
	    s = BUY;
	} else if(value.equalsIgnoreCase("venda") || value.equalsIgnoreCase("sell")) {
	    s = SELL;
	}
	
	return s;
    }

}
