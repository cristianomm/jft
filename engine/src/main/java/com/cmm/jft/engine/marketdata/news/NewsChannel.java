/**
 * 
 */
package com.cmm.jft.engine.marketdata.news;

import java.util.ArrayList;

import com.cmm.jft.data.files.CSV;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.enums.StreamTypes;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.security.Security;
import com.cmm.jft.security.SecurityInfo;

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
import quickfix.fix44.SecurityList;

/**
 * <p>
 * <code>InstrumentDefinition.java</code>
 * </p>
 * 
 * @author cristiano
 * @version 23/02/2017 00:30:17
 *
 */
public class News extends MessageCracker implements Application {

    private ArrayList<Security> securities;

    private ArrayList<SecurityList> instruments;

    public News() {
	securities = new ArrayList<>();
	loadSecurities();

	createInstrumentList();
	new Thread(new Runnable() {

	    @Override
	    public void run() {
		while (true) {
		    sendInstruments();
		    try {
			Thread.sleep(1000);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }
		}
	    }
	}).start();

    }

    private void loadSecurities() {
	loadFile();
    }

    private void loadFile() {

	CSV csv = new CSV(Thread.currentThread().getContextClassLoader().getResource("SecuritiesMT.csv").getPath(), ";",
		"#");
	int id = 5000000;
	while (csv.hasNext()) {
	    String[] line = csv.readLine();

	    Security s = new Security(line[0]);
	    s.setDescription(line[1]);
	    s.setSecurityID(id++);
	    s.setSecurityInfoID(new SecurityInfo(s, null));
	    s.getSecurityInfoID().setIsin(line[2]);

	    securities.add(s);
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
	System.out.println("oncreate");
	// Session.lookupSession(sessionId).generateHeartbeat();
    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onLogon(quickfix.SessionID)
     */
    @Override
    public void onLogon(SessionID sessionId) {
	System.out.println("onLogon: " + sessionId.getSenderCompID());
	SessionRepository.getInstance().addSession(StreamTypes.INSTRUMENT, sessionId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    @Override
    public void onLogout(SessionID sessionId) {
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
     * @see quickfix.MessageCracker#onMessage(quickfix.Message,
     * quickfix.SessionID)
     */
    @Override
    protected void onMessage(Message message, SessionID sessionID)
	    throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
	System.out.println("foi: " + message);
    }

    /**
     * Cria as listas com 10 instrumentos cada.
     */
    private void createInstrumentList() {

	instruments = new ArrayList<>();

	// 35=y1128=934=152=20140610132615075393=111893=N146=5
	// 55=APTI448=380024022=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3800240456=8455=BRAPTIACNPR3456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=1234615=BRL120=BRL460=5167=PS762=10046937=APTI107=ALIPERTI
	// PN7595=123455541=99991231200=999912231=1667=201404461=EPNEFR470=BR225=2014043063=D164=999912316938=99991231-22:59:59.0001300=8037010=1261151=01
	// 55=APTI348=380026522=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3800265456=8455=BRAPTIACNOR6456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=625015=BRL120=BRL460=5167=CS762=10036937=APTI107=ALIPERTI
	// ON7595=62500541=99991231200=999912231=1667=201404461=ESVUFR470=BR225=2014043063=D164=999912316938=99991231-22:59:59.0001300=8037010=1261151=01
	// 55=CSNA348=380249322=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3802493456=8455=BRCSNAACNOR6456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=14579701115=BRL120=BRL460=5167=CS762=10036937=CSNA107=SID
	// NACIONALON7595=1457970108541=99991231200=999912231=1667=201403461=ESVUFR470=BR225=2014030563=D164=999912316938=99991231-22:59:59.0001300=8037010=2451151=04
	// 55=DUQE348=380275822=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3802758456=8455=BRDUQEACNOR5456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=10777515=BRL120=BRL460=5167=CS762=10036937=DUQE107=MET
	// DUQUE
	// ON7595=1077753541=99991231200=999912231=1667=201205461=ESVUFR470=BR225=2012050263=D164=999912316938=99991231-22:59:59.0001300=8037010=401151=01
	// 55=DUQE448=380296422=8207=BVMF1351=31180=MBO511180=MBP1511141=11022=STD264=101180=TOB2511141=11022=STD264=1454=2455=BMFBR3802964456=8455=BRDUQEACNPR2456=4870=2871=34872=1871=24872=1980=M1234=11093=21231=100969=0.015151=29749=1009748=17382515=BRL120=BRL460=5167=PS762=10046937=DUQE107=MET
	// DUQUE
	// PN7595=1738251541=99991231200=999912231=1667=201205461=EPNEFR470=BR225=2012050263=D164=999912316938=99991231-22:59:59.0001300=8037010=401151=01

	for (int i = 0; i < securities.size(); i += 10) {

	    Security[] secsTemp = new Security[10];
	    for (int j = 0; j < 10 && (i + j) < securities.size(); j++) {
		secsTemp[j] = securities.get(i + j);
	    }

	    SecurityList list = Fix50SP2MDMessageEncoder.getInstance().securityList(secsTemp);
	    list.setInt(393, securities.size());
	    if (i + 1 >= securities.size()) {
		list.setBoolean(893, true);
	    } else {
		list.setBoolean(893, false);
	    }

	    instruments.add(list);

	}

    }

    private void sendInstruments() {
	for (SessionID sid : SessionRepository.getInstance().getSessions(StreamTypes.INSTRUMENT).values()) {
	    MessageRepository.getInstance().addMessage(Fix50SP2MDMessageEncoder.getInstance().sequenceReset(), sid);
	    for (SecurityList sl : instruments) {
		MessageRepository.getInstance().addMessage(sl, sid);
	    }
	}
    }

}
