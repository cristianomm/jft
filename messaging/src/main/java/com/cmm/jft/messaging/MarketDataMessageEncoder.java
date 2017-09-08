/**
 * 
 */
package com.cmm.jft.messaging;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import com.cmm.jft.marketdata.MDSnapshot;
import com.cmm.jft.messaging.enums.DepthTypes;
import com.cmm.jft.trading.enums.MarketPhase;
import com.cmm.jft.trading.enums.UpdateActions;
import com.cmm.jft.security.Security;
import com.cmm.jft.trading.Orders;

import quickfix.Message;
import quickfix.field.MDUpdateAction;
import quickfix.fix44.MarketDataIncrementalRefresh;
import quickfix.fix44.MarketDataSnapshotFullRefresh;

/**
 * <p><code>MarketDataMessageEncoder.java</code></p>
 * @author Cristiano M Martins
 * @version Feb 26, 2016 4:24:04 PM
 *
 */
public interface MarketDataMessageEncoder extends MessageEncoder {
    
    static MDUpdateAction newUA = new MDUpdateAction(MDUpdateAction.NEW);
    static MDUpdateAction changeUA = new MDUpdateAction(MDUpdateAction.CHANGE);
    static MDUpdateAction deleteUA = new MDUpdateAction(MDUpdateAction.DELETE);
    static MDUpdateAction del_thruUA = new MDUpdateAction(MDUpdateAction.DELETE_THRU);
    static MDUpdateAction del_fromUA = new MDUpdateAction(MDUpdateAction.DELETE_FROM);
    
    Message sequenceReset();

    Message securityList(Security[] securities);
    
    MarketDataIncrementalRefresh.NoMDEntries bidEntryIncMBO(Orders order, int positionNo, UpdateActions updateAction, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries bidEntryIncMBP(double price, int size, int numOrders, Security security, int positionNo, UpdateActions updateAction, int rptSeq);
    
    MarketDataIncrementalRefresh.NoMDEntries offerEntryIncMBO(Orders order, int positionNo, UpdateActions updateAction, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries offerEntryIncMBP(double price, int size, int numOrders,Security security, int positionNo, UpdateActions updateAction, int rptSeq);
    
    
    MarketDataIncrementalRefresh.NoMDEntries tradeEntryInc(
	    UpdateActions updateAction, Security security, String buyer, String seller, double price,
	    int size, Date tradeDateTime, String tradeID, int tradeVolume, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries openPriceEntryInc(UpdateActions updateAction, Security security, double openPrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries closePriceEntryInc(Security security, double closePrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries highPriceEntryInc(UpdateActions updateAction, Security security, double highPrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries lowPriceEntryInc(UpdateActions updateAction, Security security, double lowPrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries vwapPriceEntryInc(UpdateActions updateAction, Security security, double vwapPrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries tradeVolumeEntryInc(Security security, int numOfTrades, double financialVolume, double tradedVolume, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries emptyBookEntryInc(Security security, int rptSeq);

    Message mdIncrementalRefresh(Queue<MarketDataIncrementalRefresh.NoMDEntries> entries);
    
    
    MarketDataSnapshotFullRefresh.NoMDEntries bidEntrySnp(double price, int volume, Date insertDtTime, String orderID, String brokerID, int positionNo);
    MarketDataSnapshotFullRefresh.NoMDEntries offerEntrySnp(double price, int volume, Date insertDtTime, String orderID, String brokerID, int positionNo);
    MarketDataSnapshotFullRefresh.NoMDEntries tradeEntrySnp(String buyer, String seller, double price, int volume, Date tradeDateTime, String tradeID, int tradeVolume);
    MarketDataSnapshotFullRefresh.NoMDEntries openPriceEntrySnp(double openPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries closePriceEntrySnp(double closePrice);
    MarketDataSnapshotFullRefresh.NoMDEntries highPriceEntrySnp(double highPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries lowPriceEntrySnp(double lowPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries vwapPriceEntrySnp(double vwapPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries tradeVolumeEntrySnp(double financialVolume, double totalVolume);
    MarketDataSnapshotFullRefresh.NoMDEntries securityTradingStateSnp(MarketPhase phase);
    MarketDataSnapshotFullRefresh.NoMDEntries priceBandSnp(double high, double low, int priceBandType, int priceLimitType);
    MarketDataSnapshotFullRefresh.NoMDEntries quantityBandSnp(int quantity);

    Message mdSnapShotFullRefresh(Security security, int rptSeq, int lstMsgSeqNum);
    
    Message securityStatus();

    Message news(String title, String message, String source, int newsID);

    Message nonFixData();

}
