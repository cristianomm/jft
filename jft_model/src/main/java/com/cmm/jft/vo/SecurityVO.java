/**
 * 
 */
package com.cmm.jft.vo;

/**
 * <p><code>SecurityVO.java</code></p>
 * @author cristiano
 * @version Nov 2, 2015 2:26:40 PM
 *
 */
public class SecurityVO {
	
	private String symbol;
	
	private String description;
	
	
	public SecurityVO(String symbol, String descr){
		this.description = descr;
		this.symbol = symbol;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return this.description;
	}
	
	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}
	
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}
	
}
