package com.cmm.jft.engine;

import java.util.HashMap;

import org.omg.CORBA.portable.Streamable;

import com.cmm.jft.engine.enums.StreamTypes;

import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionStateListener;

public class SessionRepository {
	
	private static SessionRepository instance;
	
	private HashMap<StreamTypes, HashMap<String, SessionID>> sessions;
	
	
	private SessionRepository() {
		this.sessions = new HashMap<StreamTypes, HashMap<String, SessionID>>();
		
		for(StreamTypes st: StreamTypes.values()){
			sessions.put(st, new HashMap<>());
		}
		
	}
	
	
	/**
	 * @return the instance
	 */
	public static synchronized SessionRepository getInstance() {
		if(instance == null){
			instance = new SessionRepository();
		}
		return instance;
	}
	
	public void addSession(StreamTypes stream, SessionID sessionID){
		if(sessionID != null){
			sessions.get(stream).put(sessionID.getSenderCompID(), sessionID);
		}
	}
	
	public void removeSession(StreamTypes stream, SessionID sessionID){
		sessions.get(stream).remove(sessionID.getSenderCompID());
	}
	
	public SessionID getSession(StreamTypes stream, String traderID) {
		return sessions.get(stream).get(traderID);
	}
	
	public HashMap<String, SessionID> getSessions(StreamTypes stream){
		return sessions.get(stream);
	}
	
}
