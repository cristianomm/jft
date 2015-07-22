package com.cmm.jft.engine;

import java.util.HashMap;

import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionStateListener;

public class SessionRepository {
	
	
	
	private class SessionListener implements SessionStateListener{

		@Override
		public void onConnect() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onDisconnect() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onHeartBeatTimeout() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLogon() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onLogout() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onMissedHeartBeat() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onRefresh() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onReset() {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	
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
