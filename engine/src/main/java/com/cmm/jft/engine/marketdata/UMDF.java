/**
 * 
 */
package com.cmm.jft.engine.marketdata;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.enums.StreamTypes;
import com.cmm.jft.messaging.MarketDataMessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.MessageSender;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;

import quickfix.Group;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.MDBookType;
import quickfix.field.MDEntryBuyer;
import quickfix.field.MDEntryDate;
import quickfix.field.MDEntryPx;
import quickfix.field.MDEntrySeller;
import quickfix.field.MDEntrySize;
import quickfix.field.MDEntryTime;
import quickfix.field.MDEntryType;
import quickfix.field.MDUpdateAction;
import quickfix.field.NetChgPrevDay;
import quickfix.field.RptSeq;
import quickfix.field.SecurityExchange;
import quickfix.field.SecurityID;
import quickfix.field.SecurityIDSource;
import quickfix.field.SellerDays;
import quickfix.field.TickDirection;
import quickfix.field.TradeCondition;
import quickfix.field.TradeID;
import quickfix.field.TradeVolume;
import quickfix.field.TradingSessionID;
import quickfix.fix44.SecurityList;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataIncrementalRefresh.NoMDEntries;

/**
 * <p><code>UMDF.java</code></p>
 * @author Cristiano M Martins
 * @version Feb 26, 2016 3:57:55 PM
 *
 */
public class UMDF implements MessageSender {
	
	
	private int sequence;
	private SecurityID securityID;
	private SecurityIDSource securityIDSrc;
	private SecurityExchange exchangeID;
	
	private static String stream ="E";
	private static MDUpdateAction newUA = new MDUpdateAction(MDUpdateAction.NEW);
	private static MDEntryType tradeET = new MDEntryType(MDEntryType.TRADE);
	private static MDEntryType vwapET = new MDEntryType(MDEntryType.TRADING_SESSION_VWAP_PRICE);
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private static SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
	
	private MarketDataMessageEncoder encoder;
	
	
	public UMDF(String  secID, String secIDSrc, String exchID){
		encoder = Fix50SP2MDMessageEncoder.getInstance();
		
		securityID = new SecurityID(secID);
		securityIDSrc = new SecurityIDSource(secIDSrc);
		exchangeID = new SecurityExchange(exchID);
				
	}
		
	
	/**
	 * @param order
	 */
	public void informNewOrder(Orders order) {
		 
		//Group g = new Group(269, delim);
		informMarket();
	}
	
	public void informUpdateOrder(Orders order){
		
	}
	
	public void informTrade(OrderEvent orderFill, OrderEvent bookFill, double vwap, double totalVolume){
		
		
		//----------------------------------------------------------
		//Trade - 2
		MarketDataIncrementalRefresh.NoMDEntries tradeMD = new NoMDEntries();
		MarketDataIncrementalRefresh.NoMDEntries vwapMD = new NoMDEntries();
		MarketDataIncrementalRefresh.NoMDEntries[] entries = new NoMDEntries[2];
		Date dt = new Date();
		MDEntryDate date = new MDEntryDate(dt);
		MDEntryDate time = new MDEntryDate(dt);
		
		tradeMD.set(newUA);
		tradeMD.set(tradeET);
		tradeMD.setInt(83, sequence++);
		tradeMD.set(securityID);
		tradeMD.set(securityIDSrc);
		tradeMD.set(exchangeID);
		tradeMD.setString(1500, stream);
		
		tradeMD.set(new MDEntryPx(orderFill.getPrice()));
		tradeMD.set(new MDEntrySize(orderFill.getVolume()));
		//(37014, MDEntryType.TRADE);
		tradeMD.set(date);
		tradeMD.set(time);
		//37016=2014061037017=61:60:23
		
		tradeMD.setString(37016, sdf.format(orderFill.getOrderID().getOrderDateTime()));
		tradeMD.setString(37017, stf.format(orderFill.getOrderID().getOrderDateTime()));
		
		tradeMD.set(new TickDirection());
		tradeMD.set(new TradeCondition());
		tradeMD.set(new TradingSessionID("1") );
		tradeMD.set(new MDEntryBuyer(orderFill.getContraBroker()));
		tradeMD.set(new MDEntrySeller(bookFill.getContraBroker()));
		//tradeMD.set(new NetChgPrevDay());
		//tradeMD.set(new SellerDays());
		tradeMD.setDouble(1020, totalVolume);//new TradeVolume()
		tradeMD.setString(1003, orderFill.getTradeID());//new TradeID()
		
		
		
		
		//----------------------------------------------------------
		//Trading session High/Low/VWAP Price - 7/8/9
		vwapMD.set(newUA);
		vwapMD.set(vwapET);
		vwapMD.setInt(83, sequence++);
		vwapMD.set(securityID);
		vwapMD.set(securityIDSrc);
		vwapMD.set(exchangeID);
		vwapMD.setString(1500, stream);
		vwapMD.set(new MDEntryPx(vwap));
		vwapMD.set(date);
		vwapMD.set(time);
		
		
		
		encoder.mdIncrementalRefresh(entries);
		
		
		//Trade Volume - B
		
		//Opening Price - 4
		
		//Closing Price - 5
		
	}
	
	public void informNews(String news){
		
		//Fix50SP2MDMessageEncoder.getInstance().
		
		
	}
	
	
	
	
	public void mbo(){
		
	}
	
	public void mbp(){
		
	}
	
	public void tob(){
		
	}
	
	
	/**
	 * Informa ao mercado os eventos ocorridos
	 */
	public void informMarket(){
		
		mbo();
		mbp();
		tob();
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.messaging.MessageSender#sendMessage(quickfix.Message, quickfix.SessionID)
	 */
	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		
		for(SessionID sid: SessionRepository.getInstance().getSessions(StreamTypes.INCREMENTAL).values()){
			//MessageRepository.getInstance().addMessage(sl, sid);
		}
		
		return false;
	}
	
	
}
