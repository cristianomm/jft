/**
 * 
 */
package com.cmm.jft.connector.marketdata;

import java.util.HashMap;
import java.util.Map;

import com.cmm.jft.model.trading.enums.StreamTypes;

/**
 * <p>
 * <code>ChannelDefinition.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 26/09/2017 01:12:50
 *
 */
public class ChannelDefinition {
    
    private class Stream{
	StreamTypes type;
	String streamIP;
	int streamPort;
	
	/**
	 * 
	 */
	public Stream(StreamTypes type, String ip, int port) {
	    this.type = type;
	    this.streamIP = ip;
	    this.streamPort = port;
	}
	
    }
    
    
    public int channel;
    public String channelName;
    public Map<StreamTypes, Stream> streams;
    
    /**
     * 
     */
    public ChannelDefinition(int channel, String channelName) {
	this.channel = channel;
	this.channelName = channelName;
	this.streams = new HashMap<>();
    }
    
    
    public void addStream(StreamTypes type, String ip, int port) {
	streams.put(type, new Stream(type, ip, port));
    }
    
    
}
