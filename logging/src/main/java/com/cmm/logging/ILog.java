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

	public abstract void printStackTrace(boolean print);

	public abstract void log(Class<?> cls, String message, Level level);

	public abstract void log(Class<?> cls, Throwable throww, Level level);

	public abstract void log(Class<?> cls, String message, Throwable throww,
			Level level, boolean showMessage);

}