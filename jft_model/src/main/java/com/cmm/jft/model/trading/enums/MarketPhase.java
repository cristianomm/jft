/**
 * 
 */
package com.cmm.jft.model.trading.enums;

/**
 * <p><code>MarketPhase.java</code></p>
 * @author cristiano
 * @version Nov 3, 2015 4:52:35 PM
 *
 */
public enum MarketPhase {
	Pause(2),
	Close(4),
	Open(17),
	Pre_Close(18),
	Pre_Open(21),
	Final_Closing_Call(101);
	
	int value;
	
	/**
	 * 
	 */
	private MarketPhase(int value) {
		this.value = value;
	}
	
	/**
	 * @return the value
	 */
	public int getValue() {
		return this.value;
	}
	
	public static MarketPhase getByValue(int value){
		MarketPhase phase = MarketPhase.Close;
		for(MarketPhase mp : MarketPhase.values()){
			if(mp.value == value){
				phase = mp;
				break;
			}
		}
		
		return phase;
	}
	
}
