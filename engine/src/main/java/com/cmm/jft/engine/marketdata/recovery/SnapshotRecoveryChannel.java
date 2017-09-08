/**
 * 
 */
package com.cmm.jft.engine.marketdata.recovery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Level;

import com.cmm.jft.data.files.CSV;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.marketdata.MDSnapshot;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.security.Security;
import com.cmm.jft.security.SecurityInfo;
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
import quickfix.Session;
import quickfix.SessionID;
import quickfix.UnsupportedMessageType;
import quickfix.fix44.MarketDataSnapshotFullRefresh;
import quickfix.fix44.SecurityList;

/**
 * <p>
 * <code>SnapshotRecoveryChannel.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 2017-07-27 09:07:30
 *
 */
public class SnapshotRecoveryChannel extends MessageCracker implements Application {

    private static SnapshotRecoveryChannel instance;
    
    private Fix50SP2MDMessageEncoder encoder;

    private TreeMap<String, MarketDataSnapshotFullRefresh> snapshots;

    private SnapshotRecoveryChannel() {
	encoder = Fix50SP2MDMessageEncoder.getInstance();
	snapshots = new TreeMap<String, MarketDataSnapshotFullRefresh>();
    }
    
    
    
    /**
     * @return the instance
     */
    public static synchronized SnapshotRecoveryChannel getInstance() {
	if(instance == null) {
	    instance = new SnapshotRecoveryChannel();
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
	System.out.println("oncreate: Snapshot Recovery Channel");
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
	Logging.getInstance().log(getClass(), "onLogon: " + sessionId.getSenderCompID(), Level.INFO);
	SessionRepository.getInstance().addSession(StreamTypes.SNAPSHOT, sessionId);
    }

    /*
     * (non-Javadoc)
     * 
     * @see quickfix.Application#onLogout(quickfix.SessionID)
     */
    @Override
    public void onLogout(SessionID sessionId) {
	Logging.getInstance().log(getClass(), "onLogout: " + sessionId.getTargetCompID(), Level.INFO);
	SessionRepository.getInstance().removeSession(StreamTypes.SNAPSHOT, sessionId);
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
	Logging.getInstance().log(getClass(), "onMessage: " + sessionID.getSenderCompID(), Level.INFO);
    }




    public void updateSnapshot(MDSnapshot snap) {
	MarketDataSnapshotFullRefresh snpfr = 
		(MarketDataSnapshotFullRefresh) encoder.mdSnapShotFullRefresh(
			snap.getSecurity(), snap.getRptSeqNum(), snap.getLstMsgSeqNum());

	for(MDEntry et : snap.getBuyEntries()) {
	    snpfr.addGroup(
		    encoder.bidEntrySnp(et.getMdEntryPx(), et.getMdEntrySize(),
			    et.getMdEntryDateTime(),
			    et.getOrderID(),et.getMdEntryBuyer(),et.getMdEntryPosNo()));
	}
	
	for(MDEntry et : snap.getSellEntries()) {
	    snpfr.addGroup(
		    encoder.offerEntrySnp(et.getMdEntryPx(), et.getMdEntrySize(),
			    et.getMdEntryDateTime(),
			    et.getOrderID(),et.getMdEntrySeller(),et.getMdEntryPosNo()));
	}


	//encoder.tradeEntrySnp(buyer, seller, price, volume, tradeDate, tradeTime, tradeID, tradeVolume)
	snpfr.addGroup(encoder.openPriceEntrySnp(snap.getOpenPrice()));
	snpfr.addGroup(encoder.closePriceEntrySnp(snap.getClosePrice()));
	snpfr.addGroup(encoder.highPriceEntrySnp(snap.getHighPrice()));
	snpfr.addGroup(encoder.lowPriceEntrySnp(snap.getLowPrice()));
	snpfr.addGroup(encoder.vwapPriceEntrySnp(snap.getVwapPrice()));
	snpfr.addGroup(encoder.tradeVolumeEntrySnp(snap.getFinancialVolume(), snap.getTradeVolume()));
	snpfr.addGroup(encoder.priceBandSnp(snap.getLimits().getHardLimitHigh(), snap.getLimits().getHardLimitLow(), 1, 0));
	snpfr.addGroup(encoder.priceBandSnp(snap.getLimits().getRejectionBandHigh(), snap.getLimits().getRejectionBandLow(), 3, 2));
	snpfr.addGroup(encoder.priceBandSnp(snap.getLimits().getAuctionBandHigh(), snap.getLimits().getAuctionBandLow(), 2, 2));
	snpfr.addGroup(encoder.priceBandSnp(snap.getLimits().getStaticLimitHigh(), snap.getLimits().getStaticLimitLow(), 4, 0));
	snpfr.addGroup(encoder.quantityBandSnp(snap.getLimits().getQuantityLimit()));
	snpfr.addGroup(encoder.securityTradingStateSnp(snap.getPhase()));

	snapshots.put(snap.getSecurity().getSymbol(), snpfr);

    }

    public void sendSnapshot() {
	for (SessionID sid : SessionRepository.getInstance().getSessions(StreamTypes.SNAPSHOT).values()) {
	    for (MarketDataSnapshotFullRefresh snap : snapshots.values()) {
		MessageRepository.getInstance().addMessage(snap, sid);
	    }
	}

	for (SessionID sid : SessionRepository.getInstance().getSessions(StreamTypes.SNAPSHOT).values()) {
	    try {
		Session.lookupSession(sid).setNextSenderMsgSeqNum(1);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    MessageRepository.getInstance().addMessage(Fix50SP2MDMessageEncoder.getInstance().sequenceReset(), sid);
	}


    }

}
