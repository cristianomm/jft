/**
 * 
 */
package com.cmm.jft.connector.marketdata;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <code>Channels.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 26/09/2017 01:34:19
 *
 */
public class Channels {
    
    private static Channels instance;
    private Map<Integer, ChannelDefinition> channels;
    
    private Channels() {
	this.channels = new HashMap<>();
    }
    
    /**
     * @return the instance
     */
    public synchronized static Channels getInstance() {
	if(instance == null) {
	    instance = new Channels();
	}
	return instance;
    }
    
    
    /**
     * @return the channels
     */
    public ChannelDefinition getChannel(int channelID) {
	return channels.get(channelID);
    }
    
}
