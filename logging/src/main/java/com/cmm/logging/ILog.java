/**
 * 
 */
package com.cmm.logging;

import org.apache.log4j.Level;

/**
 * <p><code>ILog.java</code></p>
 * @author Cristiano M Martins
 * @version 29/01/2015 20:27:02
 *
 */
public interface ILog {

	abstract void printStackTrace(boolean print);

	abstract void log(Class<?> cls, String message, Level level);

	abstract void log(Class<?> cls, Throwable throww, Level level);

	abstract void log(Class<?> cls, String message, Throwable throww, Level level);
	
	abstract void log(Class<?> cls, String message, Throwable throww, Level level, boolean showMessage);
}