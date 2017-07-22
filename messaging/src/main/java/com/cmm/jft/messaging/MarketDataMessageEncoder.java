/**
 * 
 */
package com.cmm.jft.messaging;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;

import com.cmm.jft.messaging.enums.DepthTypes;
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
	    int size, Date tradeDate, Date tradeTime, String tradeID, int tradeVolume, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries openPriceEntryInc(UpdateActions updateAction, Security security, double openPrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries closePriceEntryInc(Security security, double closePrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries highPriceEntryInc(UpdateActions updateAction, Security security, double highPrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries lowPriceEntryInc(UpdateActions updateAction, Security security, double lowPrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries vwapPriceEntryInc(UpdateActions updateAction, Security security, double vwapPrice, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries tradeVolumeEntryInc(Security security, int numOfTrades, double financialVolume, double tradedVolume, int rptSeq);
    MarketDataIncrementalRefresh.NoMDEntries emptyBookEntryInc(Security security, int rptSeq);

    Message mdIncrementalRefresh(Queue<MarketDataIncrementalRefresh.NoMDEntries> entries);
    
    
    MarketDataSnapshotFullRefresh.NoMDEntries bidEntrySnp(Orders order, UpdateActions updateAction, DepthTypes depth);
    MarketDataSnapshotFullRefresh.NoMDEntries offerEntrySnp(Orders order, UpdateActions updateAction, DepthTypes depth);
    MarketDataSnapshotFullRefresh.NoMDEntries tradeEntrySnp(UpdateActions updateAction, Orders buyOrder, Orders sellOrder, int volume, double price);
    MarketDataSnapshotFullRefresh.NoMDEntries openPriceEntrySnp(UpdateActions updateAction, Security security, double openPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries closePriceEntrySnp(UpdateActions updateAction, Security security, double closePrice);
    MarketDataSnapshotFullRefresh.NoMDEntries highPriceEntrySnp(UpdateActions updateAction, Security security, double highPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries lowPriceEntrySnp(UpdateActions updateAction, Security security, double lowPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries vwapPriceEntrySnp(UpdateActions updateAction, Security security, double vwapPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries tradeVolumeEntrySnp(Security security, double vwapPrice);
    MarketDataSnapshotFullRefresh.NoMDEntries emptyBookEntrySnp(Security security);

    Message mdSnapShotFullRefresh(Queue<MarketDataSnapshotFullRefresh.NoMDEntries> entries);

    Message securityStatus();

    Message news(String title, String message, String source, int newsID);

    Message nonFixData();

}
