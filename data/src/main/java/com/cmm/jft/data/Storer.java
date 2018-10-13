/**
 * 
 */
package com.cmm.jft.data;

import com.cmm.jft.data.dde.QuoteData;

/**
 * <p>
 * <code>Storer.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 13/09/2013 14:43:34
 *
 */
public interface Storer {

	void store(QuoteData quoteData);

	void init();

	void close();

}
