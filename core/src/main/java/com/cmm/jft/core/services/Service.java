/**
 * 
 */
package com.cmm.jft.core.services;

/**
 * 
 * <p><code>Service.java</code></p>
 * @author Cristiano M Martins
 * @version 14 de jul de 2015 02:20:46
 *
 */
public interface Service extends Runnable {
	
	
	boolean start();
	
	boolean stop();
	
	boolean isStarted();
	
	
	
}
