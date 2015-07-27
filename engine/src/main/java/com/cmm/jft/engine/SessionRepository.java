package com.cmm.jft.engine;

import java.util.HashMap;

import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionStateListener;

public class SessionRepository {
	
		
	private static SessionRepository instance;
	private HashMap<String, SessionID> sessions;
	
	
	private SessionRepository() {
		this.sessions = new HashMap<String, SessionID>();
	}
	
	public synchronized static SessionRepository getInstance() {
		if(instance == null){
			instance = new SessionRepository();
		}
		
		return instance;
	}
	
	
	public void addSession(SessionID sessionID){
		if(sessionID != null){
			//session.addStateListener(new SessionListener());
			sessions.put(sessionID.getSenderCompID(), sessionID);
		}
	}
	
	public void removeSession(SessionID sessionID){
		sessions.remove(sessionID.getSenderCompID());
	}
	
}
