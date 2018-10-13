/**
 * 
 */
package com.cmm.jft.core.format;

/**
 * <p>
 * <code>Formatter.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 15:48:53
 *
 */
public interface Formatter<T> {

	String format(T t);

	T parse(String st);

	String getName();

}
