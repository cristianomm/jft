/**
 * 
 */
package com.cmm.jft.data.dde;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * <code>Mapped.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 01/08/2013 00:43:16
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Mapped {
	String mapID();
}
