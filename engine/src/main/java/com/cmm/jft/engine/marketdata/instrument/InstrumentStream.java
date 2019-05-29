/**
 * 
 */
package com.cmm.jft.engine.marketdata.instrument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Level;

import com.cmm.jft.data.files.CSV;
import com.cmm.jft.engine.Stream;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.security.SecurityInfo;
import com.cmm.jft.model.trading.enums.StreamTypes;
import com.cmm.logging.Logging;

import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.SecurityList;

/**
 * <p>
 * <code>InstrumentStream.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Feb 29, 2016 10:10:30 AM
 *
 */
public class InstrumentStream extends Stream {

	private static InstrumentStream instance;
	private ArrayList<Security> securities;
	private ArrayList<SecurityList> instruments;

	private InstrumentStream() {
		super();
		securities = new ArrayList<>();
	}

	/**
	 * @return the instance
	 */
	public static synchronized InstrumentStream getInstance() {
		if (instance == null) {
			instance = new InstrumentStream();
		}

		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		crack(message, sessionId);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	@Override
	public void onCreate(SessionID sessionId) {
		System.out.println("onCreate: Instrument Channel");
		// Session.lookupSession(sessionId).generateHeartbeat();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	@Override
	public void onLogon(SessionID sessionId) {
		System.out.println("onLogon: " + sessionId.getTargetCompID());
		logger.log(getClass(), "onLogon: " + sessionId.getTargetCompID(), Level.INFO);
		SessionRepository.getInstance().addSession(StreamTypes.INSTRUMENT, sessionId);
		// MessageRepository.getInstance().addMessage(Fix50SP2MDMessageEncoder.getInstance().sequenceReset(),
		// sessionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	@Override
	public void onLogout(SessionID sessionId) {
		logger.log(getClass(), "onLogout: " + sessionId.getTargetCompID(), Level.INFO);
		SessionRepository.getInstance().removeSession(StreamTypes.INSTRUMENT, sessionId);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see quickfix.MessageCracker#onMessage(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	protected void onMessage(Message message, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		System.out.println("foi: " + message);
		logger.log(getClass(), "onMessage: " + sessionID.getSenderCompID(), Level.INFO);
	}

	private void loadSecurities() {
		logger.log(getClass(), "Loading Security list...", Level.INFO);
		CSV csv = new CSV(Thread.currentThread().getContextClassLoader().getResource("Symbols.csv").getPath(), ";",
				"#");
		int id = 5000000;
		while (csv.hasNext()) {
			String[] line = csv.readLine();

			Security s = new Security(line[0]);
			s.setIsin(line[2]);
			s.setDescription(line[1]);
			s.setSecurityId(id++);
			s.setSecurityInfoId(new SecurityInfo(s, null));
			s.getSecurityInfoId();

			securities.add(s);
		}

		logger.log(getClass(), "Securities loaded: " + securities.size(), Level.INFO);
	}

	/**
	 * Cria as listas com 10 instrumentos cada.
	 */
	private void createSecurityList() {

		logger.log(getClass(), "Building SecurityList messages...", Level.INFO);
		instruments = new ArrayList<>();

		for (int i = 0; i < securities.size(); i += 10) {
			int lastSeg = i;
			Security[] secsTemp = new Security[10];
			for (int j = 0; j < 10 && (lastSeg) < securities.size();) {
				secsTemp[j] = securities.get(lastSeg);

				lastSeg = i + ++j;
			}

			SecurityList list = Fix50SP2MDMessageEncoder.getInstance().securityList(secsTemp);
			list.setInt(393, securities.size());
			if (lastSeg + 1 >= securities.size()) {
				list.setBoolean(893, true);
			} else {
				list.setBoolean(893, false);
			}

			instruments.add(list);
		}

		logger.log(getClass(), "Security list created: " + instruments.size(), Level.INFO);

	}

	public void sendInstruments() {

		Collection<SessionID> connections = SessionRepository.getInstance().getSessions(StreamTypes.INSTRUMENT)
				.values();
		logger.log(getClass(), "Sending SecurityList to " + connections.size() + " connections.", Level.INFO);

		for (SessionID sid : connections) {
			for (SecurityList sl : instruments) {
				MessageRepository.getInstance().addMessage(sl, sid);
			}
		}

		for (SessionID sid : connections) {
			try {
				Session.lookupSession(sid).setNextSenderMsgSeqNum(1);
			} catch (IOException e) {
				logger.log(getClass(), e, Level.ERROR);
				e.printStackTrace();
			}
			MessageRepository.getInstance().addMessage(Fix50SP2MDMessageEncoder.getInstance().sequenceReset(), sid);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.engine.marketdata.Service#createSessionSettings()
	 */
	@Override
	public void createSessionSettings() {
		try {
			sessionSettings = new SessionSettings(Thread.currentThread().getContextClassLoader()
					.getResourceAsStream("InstrumentDefinitionService.cfg"));
		} catch (ConfigError e) {
			logger.log(getClass(), e, Level.ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.engine.marketdata.Service#start()
	 */
	@Override
	public void start() {
		logger.log(getClass(), "Starting Instruments stream", Level.INFO);
		loadSecurities();
		createSecurityList();
		super.start();
		logger.log(getClass(), "Instruments Stream started successfully", Level.INFO);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		while (started) {
			sendInstruments();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
