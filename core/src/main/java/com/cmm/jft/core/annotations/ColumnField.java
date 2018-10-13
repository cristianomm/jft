/**
 * 
 */
package com.cmm.jft.core.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.cmm.jft.core.format.FormatterTypes;

/**
 * <p>
 * <code>ColumnField.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 28/01/2014 00:17:32
 *
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnField {

	String name();

	FormatterTypes formatter();

}
