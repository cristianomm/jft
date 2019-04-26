/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import quickfix.FieldNotFound;
import quickfix.Group;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

import com.cmm.jft.model.marketdata.MDEntry;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.trading.enums.MDEntryTypes;
import com.cmm.jft.model.trading.enums.MarketPhase;
import com.cmm.jft.vo.TradeVO;

/**
 * <p>
 * <code>Market.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Nov 3, 2015 6:57:10 PM
 *
 */
public class Market {

    private class MBP {
	private List<MDEntry> buyOrders;
	private List<MDEntry> sellOrders;

	/**
	 * 
	 */
	public MBP() {
	    this.buyOrders = Collections.synchronizedList(new ArrayList<>(500));
	    this.sellOrders = Collections.synchronizedList(new ArrayList<>(500));
	}

    }

    private class MBO {

	private List<MDEntry> buyOrders;
	private List<MDEntry> sellOrders;

	public MBO() {
	    this.buyOrders = Collections.synchronizedList(new ArrayList<>(500));
	    this.sellOrders = Collections.synchronizedList(new ArrayList<>(500));
	}

	/**
	 * 
	 * @param message
	 */
	public void newEntry(Group message) {
	    try {
		if (message.getChar(269) == '0') {
		    newBidEnty(message);
		} else if (message.getChar(269) == '1') {
		    newOfferEnty(message);
		}
	    } catch (FieldNotFound e) {
		e.printStackTrace();
	    }

	}

	private synchronized void newBidEnty(Group message) {
	    try {
		MDEntry entry = new MDEntry();
		entry.setMdEntryPx(message.getDouble(270));// MDEntryPx
		entry.setMdEntrySize(message.getInt(271));// MDEntrySize
		entry.setOrderId(Long.parseLong(message.getString(37)));// OrderID
		entry.setMdEntryPosNo(message.getInt(290));// MDEntryPosNo

		entry.setMdEntryType(MDEntryTypes.BID);
		entry.setMdEntryBuyer(message.getString(288));

		buyOrders.add(entry.getMdEntryPosNo(), entry);
	    } catch (FieldNotFound e) {
		e.printStackTrace();
	    }
	}

	private synchronized void newOfferEnty(Group message) {
	    try {
		MDEntry entry = new MDEntry();
		entry.setMdEntryPx(message.getDouble(270));// MDEntryPx
		entry.setMdEntrySize(message.getInt(271));// MDEntrySize
		entry.setOrderId(Long.parseLong(message.getString(37)));// OrderID
		entry.setMdEntryPosNo(message.getInt(290));// MDEntryPosNo

		entry.setMdEntryType(MDEntryTypes.OFFER);
		entry.setMdEntrySeller(message.getString(289));

		sellOrders.add(entry.getMdEntryPosNo(), entry);
	    } catch (FieldNotFound e) {
		e.printStackTrace();
	    }
	}

	/**
	 * 
	 * @param message
	 */
	public synchronized void change(Group message) {
	    try {
		char side = message.getChar(269);
		int size = message.getInt(271);
		int pos = message.getInt(290) - 1;
		if (side == '0') {
		    buyOrders.get(pos).setMdEntrySize(size);
		} else if (message.getChar(269) == '1') {
		    sellOrders.get(pos).setMdEntrySize(size);
		}
	    } catch (FieldNotFound e) {
		e.printStackTrace();
	    }
	}

	/**
	 * 
	 * @param message
	 */
	public synchronized void delete(Group message) {

	    try {
		char side = message.getChar(269);
		int pos = message.getInt(290) - 1;
		if (side == '0') {
		    buyOrders.remove(pos);
		} else if (message.getChar(269) == '1') {
		    sellOrders.get(pos);
		}
	    } catch (FieldNotFound e) {
		e.printStackTrace();
	    }
	}

	/**
	 * Remove todas as ofertas no book
	 * 
	 * @param message
	 */
	public synchronized void deleteThru(Group message) {
	    try {
		char side = message.getChar(269);
		int pos = message.getInt(290) - 1;
		if (side == '0') {
		    buyOrders.clear();
		} else if (message.getChar(269) == '1') {
		    sellOrders.clear();
		}
	    } catch (FieldNotFound e) {
		e.printStackTrace();
	    }
	}

	/**
	 * 
	 * Remove as ordens contidas no book a partir da posicao enviada ate a
	 * primeira posicao.
	 * 
	 * @param message
	 */
	public synchronized void deleteFrom(Group message) {
	    try {
		char side = message.getChar(269);
		int pos = message.getInt(290);
		List<MDEntry> lst = null;
		if (side == '0') {
		    lst = buyOrders;
		} else if (message.getChar(269) == '1') {
		    lst = sellOrders;
		}

		while (pos-- > 0) {
		    lst.remove(0);// remove sempre a ponta
		}

	    } catch (FieldNotFound e) {
		e.printStackTrace();
	    }
	}

	/**
	 * 
	 * @param entry
	 */
	public synchronized void overlay(Group message) {

	}

    }

    private class TOB {

    }

    private int rptSeq;
    private int msgSeqNum;

    private Security security;

    /**
     * MDEntryType=0, 1
     */
    private MBO mbo;

    private MBP mbp;

    private ArrayList<TradeVO> trades;

    /**
     * MDEntryType=2
     */
    private long tradeCount;

    /**
     * MDEntryType=4
     */
    private double openPrice;

    /**
     * MDEntryType=5
     */
    private double closePrice;

    /**
     * MDEntryType=7
     */
    private double maxPrice;

    /**
     * MDEntryType=8
     */
    private double minPrice;

    private double lastVolume;

    private double lastPrice;

    /**
     * MDEntryType=9
     */
    private double vwap;

    /**
     * MDEntryType=B
     */
    private double tradeVolume;

    /**
     * MDEntryType=c
     */
    private MarketPhase phase;

    private SimpleDateFormat dateFormat;

    private static Market instance;


    private ArrayList<MDListener> listeners;

    public Market(Security security) {
	this.dateFormat = new SimpleDateFormat("yyyyMMdd");
	this.phase = MarketPhase.Close;
	this.security = security;
    }

    public void resetMarketData(int newSeqNum, int newRptSeq) {
	this.msgSeqNum = newSeqNum;
	this.rptSeq = newRptSeq;
	this.mbo = new MBO();
	this.mbp = new MBP();
	this.listeners = new ArrayList<>();
	this.trades = (ArrayList<TradeVO>) Collections.synchronizedList(new ArrayList<TradeVO>(100000));
    }
    
    
    public void addListener(MDListener listener) {
	if(listener != null) {
	    listeners.add(listener);
	}
    }

    public void addSnapshot(MarketDataSnapshotFullRefresh fullRefresh) {
	try {
	    if(Integer.parseInt(fullRefresh.getSecurityID().getValue()) == security.getSecurityId()) {
		//ajusta as colecoes e os indices de sequencia
		//MDIncRefresh recebidos sera a partir de msgSeqNum e maior que rptSeq ajustados aqui
		resetMarketData(fullRefresh.getInt(369), fullRefresh.getInt(83));
		for(Group g : fullRefresh.getGroups(268)) {
		    addMDEntry(g);
		}
	    }
	}catch(FieldNotFound e) {

	}
    }

    public void addMDEntry(Group entry) {
	try {
	    // snapshot:
	    // fila de ordens compra e venda, estatisticas do mercado
	    // tag 264 indica a profundidade de mercado nas mensagens, pois ha
	    // dois canais
	    // MBO e MBP, verificar o canal tambem
	    MDEntryTypes type = MDEntryTypes.valueOf(entry.getChar(269));
	    switch (type) {
	    case BID:
	    case OFFER:
		// -------------------------MBO
		if (entry.isSetField(346)) {
		    treatMBOOrder(entry);
		}
		// -------------------------MBP
		else {
		    treatMBPOrder(entry);
		}
		break;

	    case TRADE:
		treatTrade(entry);
		break;

	    case OPENING_PRICE:
		treatOpeningPrice(entry);
		break;

	    case CLOSING_PRICE:
		treatClosingPrice(entry);
		break;

	    case TRADING_SESSION_HIGH_PRICE:
	    case TRADING_SESSION_LOW_PRICE:
	    case TRADING_SESSION_VWAP_PRICE:
		treatHighLowVWAPPrice(entry);
		break;

	    case TRADE_VOLUME:
		treatTradeVolume(entry);
		break;

	    case EMPTY_BOOK:
		treatEmptyBook(entry);
		break;
	    }

	    notifyListeners();

	} catch (FieldNotFound e) {
	    e.printStackTrace();
	}

    }


    private void notifyListeners() {
	
	for (MDListener mdListener : listeners) {
	    mdListener.marketDataEvent();
	}
	
    }


    // -----------------------------------------------------MBO
    /**
     * Type Market Data entry. Valid values: 0 = Bid 1 = Offer
     * 
     */
    private void treatMBOOrder(Group message) {
	try {
	    /*
	     * Types of Market Data update action 0 = New 1 = Change 2 = Delete
	     * 3 = Delete Thru 4 = Delete From 5 = Overlay
	     */
	    switch (message.getChar(279)) {
	    case '0':
		mbo.newEntry(message);
	    case '1':
		mbo.change(message);
		break;
	    case '2':
		mbo.delete(message);
		break;
	    case '3':
		mbo.deleteThru(message);
		break;
	    case '4':
		mbo.deleteFrom(message);
		break;
	    case '5':
		mbo.overlay(message);
		break;
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    // -----------------------------------------------------MBP
    private void treatMBPOrder(Group message) {

    }

    /**
     * 2 = Trade
     */
    private void treatTrade(Group message) {
	try {
	    if (message.getChar(269) == '2') {
		String tradeID = message.getString(1003);

		TradeVO ts = new TradeVO();

		ts.tradeID = tradeID;
		ts.tradeCondition = message.getChar(277);
		ts.price = message.getDouble(270);
		ts.volume = (int) message.getDouble(271);

		ts.buyBroker = message.getString(288);
		ts.sellBroker = message.getString(289);

		Instant time = Instant.ofEpochMilli(Long.parseLong(message.getString(273)));

		ts.tradeDate = dateFormat.parse(message.getString(272));
		ts.tradeTime = Date.from(time);

		trades.add(ts);
	    }

	} catch (FieldNotFound | ParseException e) {
	    e.printStackTrace();
	}
    }

    /**
     * 3 = Index Value
     */

    /**
     * 4 = Opening Price
     */
    private void treatOpeningPrice(Group message) {
	try {

	    if (message.getChar(269) == '4') {
		double price = message.getDouble(270);
		openPrice = price;
	    }

	} catch (FieldNotFound e) {
	    e.printStackTrace();
	}
    }

    /**
     * 5 = Closing Price
     */
    private void treatClosingPrice(Group message) {
	try {

	    if (message.getChar(269) == '5') {
		// sempre sobrescreve
		closePrice = message.getDouble(270);
	    }

	} catch (FieldNotFound e) {
	    e.printStackTrace();
	}
    }

    /**
     * 6 = Settlement Price
     */

    /**
     * 7 = Session High Price 8 = Session Low Price 9 = Session VWAP Price
     * 
     */
    private void treatHighLowVWAPPrice(Group message) {
	try {

	    int type = message.getInt(269);
	    if (type >= 7 && type <= 9) {
		double price = message.getDouble(270);
		switch (type) {
		case '7':
		    maxPrice = price;
		    break;

		case '8':
		    minPrice = price;
		    break;

		case '9':
		    vwap = price;
		    break;
		}
	    }

	} catch (FieldNotFound e) {
	    e.printStackTrace();
	}

    }

    /**
     * A = Imbalance
     */

    /**
     * B = Trade Volume
     */
    private void treatTradeVolume(Group message) {
	try {
	    if (message.getChar(269) == 'B') {
		tradeCount = message.getInt(271);
		tradeVolume = message.getDouble(270);

	    }

	} catch (FieldNotFound e) {
	    e.printStackTrace();
	}
    }

    /**
     * C = Open Interest
     */

    /**
     * J = Empty Book
     */
    private void treatEmptyBook(Group message) {
	try {
	    if (message.getChar(269) == 'J') {
		resetMarketData(0, 0);
	    }
	} catch (FieldNotFound e) {
	    e.printStackTrace();
	}
    }

    /**
     * g = Price band
     */

    /**
     * h = Quantity band
     */

    /**
     * D = Composite Underlying Price (future use)
     */

    /**
     * @return the security
     */
    public Security getSecurity() {
	return security;
    }

    /**
     * @return the tradeCount
     */
    public long getTradeCount() {
	return tradeCount;
    }

    /**
     * @return the openPrice
     */
    public double getOpenPrice() {
	return openPrice;
    }

    /**
     * @return the closePrice
     */
    public double getClosePrice() {
	return closePrice;
    }

    /**
     * @return the maxPrice
     */
    public double getMaxPrice() {
	return maxPrice;
    }

    /**
     * @return the minPrice
     */
    public double getMinPrice() {
	return minPrice;
    }

    /**
     * @return the lastVolume
     */
    public double getLastVolume() {
	return lastVolume;
    }

    /**
     * @return the lastPrice
     */
    public double getLastPrice() {
	return lastPrice;
    }

    /**
     * @return the vwap
     */
    public double getVWAP() {
	return vwap;
    }

    /**
     * @return the tradeVolume
     */
    public double getTradeVolume() {
	return tradeVolume;
    }

    /**
     * @return the phase
     */
    public MarketPhase getPhase() {
	return phase;
    }

    /**
     * @param security
     *            the security to set
     */
    public void setSecurity(Security security) {
	this.security = security;
    }

    /**
     * @param tradeCount
     *            the tradeCount to set
     */
    public void setTradeCount(long tradeCount) {
	this.tradeCount = tradeCount;
    }

    /**
     * @param openPrice
     *            the openPrice to set
     */
    public void setOpenPrice(double openPrice) {
	this.openPrice = openPrice;
    }

    /**
     * @param closePrice
     *            the closePrice to set
     */
    public void setClosePrice(double closePrice) {
	this.closePrice = closePrice;
    }

    /**
     * @param maxPrice
     *            the maxPrice to set
     */
    public void setMaxPrice(double maxPrice) {
	this.maxPrice = maxPrice;
    }

    /**
     * @param minPrice
     *            the minPrice to set
     */
    public void setMinPrice(double minPrice) {
	this.minPrice = minPrice;
    }

    /**
     * @param lastVolume
     *            the lastVolume to set
     */
    public void setLastVolume(double lastVolume) {
	this.lastVolume = lastVolume;
    }

    /**
     * @param lastPrice
     *            the lastPrice to set
     */
    public void setLastPrice(double lastPrice) {
	this.lastPrice = lastPrice;
    }

    /**
     * @param vwap
     *            the vwap to set
     */
    public void setVwap(double vwap) {
	this.vwap = vwap;
    }

    /**
     * @param tradeVolume
     *            the tradeVolume to set
     */
    public void setTradeVolume(double tradeVolume) {
	this.tradeVolume = tradeVolume;
    }

    /**
     * @param phase
     *            the phase to set
     */
    public void setPhase(MarketPhase phase) {
	this.phase = phase;
    }

    public List<MDEntry> getBidMBO(){
	return mbo.buyOrders;
    }

    public List<MDEntry> getAskMBO(){
	return mbo.sellOrders;
    }

    public List<MDEntry> getBidMBP(){
	return mbp.buyOrders;
    }

    public List<MDEntry> getAskMBP(){
	return mbp.sellOrders;
    }

    /**
     * @return the trades
     */
    public ArrayList<TradeVO> getTrades() {
	return trades;
    }


}
