/**
 * 
 */
package com.cmm.jft.core.util;

/**
 * <p>
 * <code>TimeCounter.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 09/08/2013 04:20:15
 *
 */
public class TimeCounter {

	private long t0;
	private long elapsed;

	public void start() {
		t0 = System.currentTimeMillis();
	}

	public void stop() {
		elapsed = System.currentTimeMillis() - t0;
	}

	public long getElapsedInMilis() {
		return elapsed;
	}

	public double getElapsedInSeconds() {
		return elapsed / 1000d;
	}
}
