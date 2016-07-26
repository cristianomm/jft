/**
 * 
 */
package com.cmm.jft.engine.marketdata;

import java.text.SimpleDateFormat;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.enums.StreamTypes;
import com.cmm.jft.messaging.MarketDataMessageEncoder;
import com.cmm.jft.messaging.MessageRepository;
import com.cmm.jft.messaging.MessageSender;
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
import quickfix.field.MDStreamID;
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
import quickfix.fix50sp2.MarketDataIncrementalRefresh;
import quickfix.fix50sp2.MarketDataIncrementalRefresh.NoMDEntries;

/**
 * <p><code>UMDF.java</code></p>
 * @author Cristiano M Martins
 * @version Feb 26, 2016 3:57:55 PM
 *
 */
public class UMDF implements MessageSender {
	
	private static UMDF instance;
	
	private MarketDataMessageEncoder encoder;

	
	
	private UMDF(){
		encoder = Fix50SP2MDMessageEncoder.getInstance();
	}
	
	/**
	 * @return the instance
	 */
	public static synchronized UMDF getInstance() {
		if (instance == null) {
			instance = new UMDF();
		}
		return instance;
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
	
	public void informTrade(OrderEvent orderFill){
		
		//Trade - 2
		
		
		
		MarketDataIncrementalRefresh.NoMDEntries entry = new NoMDEntries();
		entry.setChar(279, MDUpdateAction.NEW);
		entry.setChar(269, MDEntryType.TRADE);
		
		entry.set(new RptSeq(1));
		
		entry.set(new SecurityID(orderFill.getOrderID().getSecurityID().getSecurityID().toString()));
		entry.set(new SecurityIDSource(""+orderFill.getOrderID().getSecurityID().getSecurityIDSrc()));
		entry.set(new SecurityExchange(orderFill.getOrderID().getSecurityID().getStockExchangeID().getStockExchangeID()));
		
		entry.setChar(1500, 'E');
		entry.set(new MDEntryPx());
		entry.set(new MDEntrySize() );
		//(37014, MDEntryType.TRADE);
		entry.set(new MDEntryDate());
		entry.set(new MDEntryTime());
		//37016=2014061037017=61:60:23
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss");
		
		entry.setString(37016, sdf.format(orderFill.getOrderID().getOrderDateTime()));
		entry.setString(37017, stf.format(orderFill.getOrderID().getOrderDateTime()));
		
		entry.set(new TickDirection());
		entry.set(new TradeCondition());
		entry.set(new TradingSessionID() );
		entry.set(new MDEntryBuyer() );
		entry.set(new MDEntrySeller());
		entry.set(new NetChgPrevDay());
		entry.set(new SellerDays());
		entry.set(new TradeVolume());
		entry.set(new TradeID());
		
		
		
		//encoder.mdIncrementalRefresh();
		
		
		
		//Trade Volume - B
		
		//Trading session High/Low/VWAP Price - 7/8/9
		
		//Opening Price - 4
		
		//Closing Price - 5
		
		
	}
	
	public void informNews(String news){
		
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
