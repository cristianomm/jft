/**
 * 
 */
package com.cmm.jft.engine;

import com.cmm.logging.ILog;
import com.cmm.logging.Logging;

import quickfix.Application;
import quickfix.MessageCracker;
import quickfix.SessionSettings;

/**
 * <p>
 * <code>Stream.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 25/07/2018 11:54:34
 *
 */
public abstract class Stream extends MessageCracker implements Application, Runnable {

	protected boolean started;
	protected int port;
	protected String address;
	protected ILog logger;
	protected SessionSettings sessionSettings;

	protected Stream() {
		logger = Logging.getInstance();
		createSessionSettings();
	}

	/**
	 * @return the started
	 */
	public boolean isStarted() {
		return started;
	}

	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @return the sessionSettings
	 */
	public SessionSettings getSessionSettings() {
		return sessionSettings;
	}

	public void start() {
		started = true;
	}

	public void stop() {
		started = false;
	}

	public abstract void createSessionSettings();

}
