/**
 * 
 */
package com.cmm.jft.services.trading;

import com.cmm.jft.connector.engine.EngineConnector;

/**
 * <p><code>EngineService.java</code></p>
 * @author cristiano
 * @version Feb 8, 2017 2:15:51 PM
 *
 */
public class EngineService {
	
	private boolean started;
	private boolean connected;
	
	private static EngineService instance;
	
	private EngineConnector connector;
	
	
	private EngineService(){
		
	}
	
	/**
	 * @return the instance
	 */
	public synchronized static EngineService getInstance() {
		if(instance == null){
			instance = new EngineService();
		}
		
		return instance;
	}
	
	
	
	
}
