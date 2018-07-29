/**
 * 
 */
package com.cmm.jft.engine.marketdata.news;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.Level;

import com.cmm.jft.data.files.CSV;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.Stream;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.trading.enums.StreamTypes;
import com.cmm.logging.Logging;

import quickfix.Application;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.field.Headline;
import quickfix.field.Text;
import quickfix.fix44.News;

/**
 * <p>
 * <code>NewsChannel.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 23/02/2017 00:30:17
 *
 */
public class NewsChannel extends Stream {

    private static NewsChannel instance;
    private int newsID;
    private ArrayBlockingQueue<News> newsQueue;

    private NewsChannel() {
	this.newsID = 1;
	newsQueue = new ArrayBlockingQueue<>(1000);
	loadNews();
    }
    
    /**
     * @return the instance
     */
    public static synchronized NewsChannel getInstance() {
	if(instance == null) {
	    instance = new NewsChannel();
	}
	return instance;
    }

    public void addNews(String source, String headLine, String text){
	try {
	    News n = Fix50SP2MDMessageEncoder.getInstance().news(headLine, text, source, newsID++);
	    
	    newsQueue.put(n);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}

    }

    private void loadNews() {
	try{
	CSV csv = new CSV(Thread.currentThread().getContextClassLoader().
		getResource("News.csv").getPath(), ";", "#");
	while (csv.hasNext()) {
	    String[] line = csv.readLine();

	    String newsSrc = "18";
	    String headLine = line[0];
	    String text = line[1];

	    addNews(newsSrc, headLine, text);
	}
	}catch(Exception e){
	    e.printStackTrace();
	}
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
	System.out.println("oncreate: News Channel");
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
	SessionRepository.getInstance().addSession(StreamTypes.NEWS, sessionId);
	MessageRepository.getInstance().addMessage(Fix50SP2MDMessageEncoder.getInstance().sequenceReset(), sessionId);
	
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		while (true) {
		    sendNews();
		    //adiciona mensagem de teste
		    addNews("18", "Test", "test text.");
		    try {
			Thread.sleep(10000);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	}).start();

	
    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    @Override
    public void onLogout(SessionID sessionId) {
	Logging.getInstance().log(getClass(), "onLogout: " + sessionId.getTargetCompID(), Level.INFO);
	SessionRepository.getInstance().removeSession(StreamTypes.NEWS, sessionId);
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
     * @see quickfix.MessageCracker#onMessage(quickfix.Message,
     * quickfix.SessionID)
     */
    @Override
    protected void onMessage(Message message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	System.out.println("foi: " + message);
    }

    private void sendNews() {
	for (SessionID sid : SessionRepository.getInstance().getSessions(StreamTypes.NEWS).values()) {
	    while(!newsQueue.isEmpty()){
		try {
		    MessageRepository.getInstance().addMessage(newsQueue.take() , sid);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	    }
	}
    }

}
