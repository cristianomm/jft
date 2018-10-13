package com.cmm.jft.engine;

import java.util.HashMap;
import java.util.TreeMap;

import com.cmm.jft.trading.enums.StreamTypes;

import quickfix.SessionID;

public class SessionRepository {

	private static SessionRepository instance;
	private TreeMap<String, SessionID> traderSessions;
	private HashMap<StreamTypes, HashMap<String, SessionID>> sessions;

	private SessionRepository() {
		this.traderSessions = new TreeMap<>();
		this.sessions = new HashMap<StreamTypes, HashMap<String, SessionID>>();
		for (StreamTypes st : StreamTypes.values()) {
			sessions.put(st, new HashMap<>());
		}

	}

	/**
	 * @return the instance
	 */
	public static synchronized SessionRepository getInstance() {
		if (instance == null) {
			instance = new SessionRepository();
		}
		return instance;
	}

	public void addSession(StreamTypes stream, SessionID sessionID) {
		if (sessionID != null) {
			sessions.get(stream).put(sessionID.getSenderCompID(), sessionID);
		}
	}

	public void removeSession(StreamTypes stream, SessionID sessionID) {
		sessions.get(stream).remove(sessionID.getSenderCompID());
	}

	public HashMap<String, SessionID> getSessions(StreamTypes stream) {
		return sessions.get(stream);
	}

	public void addTraderSession(String traderID, SessionID sessionID) {
		traderSessions.put(traderID, sessionID);
	}

	public SessionID getTraderSession(String traderID) {
		return traderSessions.get(traderID);
	}

}
