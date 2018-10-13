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
 * <code>FormulaField.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 10/08/2013 03:52:05
 *
 */
@Target(value = ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FormulaField {
	FormatterTypes type();

	String name();
}