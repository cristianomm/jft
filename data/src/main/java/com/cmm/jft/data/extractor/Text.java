/**
 * 
 */
package com.cmm.jft.data.extractor;


/**
 * <p><code>Text.java</code></p>
 * @author Cristiano
 * @version 06/04/2015 21:17:36
 *
 */
public class Text implements Extractable {

	private String text;
	
	/**
	 * 
	 */
	public Text(String text) {
		this.text = text;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return this.text;
	}
	
}
