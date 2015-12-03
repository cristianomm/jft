/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.field.MsgSeqNum;
import quickfix.field.MsgType;
import quickfix.fix50.MarketDataIncrementalRefresh;
import quickfix.fix50.MarketDataSnapshotFullRefresh;

import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.MarketPhase;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.vo.OrderEventVO;
import com.cmm.jft.vo.OrdersVO;

/**
 * <p><code>Market.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 3, 2015 6:57:10 PM
 *
 */
public class Market {

	private int msgSeqNum;

	private Security security;

	/**
	 * MDEntryType=0
	 */
	private ConcurrentLinkedQueue<Orders> buyQueue;

	/**
	 * MDEntryType=1
	 */
	private ConcurrentLinkedQueue<Orders> sellQueue;


	private ConcurrentLinkedQueue<OrderEvent> timeSales;


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



	private Market(String symbol){
		this.phase = MarketPhase.Close;
		this.security = SecurityService.getInstance().provideSecurity(symbol);
		resetMarketData(msgSeqNum);
	}


	public void resetMarketData(int newSeqNum){
		this.msgSeqNum = newSeqNum;
		this.buyQueue = new ConcurrentLinkedQueue<>();
		this.sellQueue = new ConcurrentLinkedQueue<>();
		this.timeSales = new ConcurrentLinkedQueue<OrderEvent>();

	}

	public void addSnapshot(Message message){

		try {
			//snapshot:
			//fila de ordens compra e venda
			//estatisticas do mercado
			char type = message.getChar(MsgType.FIELD);	
			msgSeqNum = message.getInt(MsgSeqNum.FIELD);

			if(type == 'X') {//Incr Refresh
				addIncrementalRefresh((MarketDataIncrementalRefresh) message);
			}
			else if(type == 'W') {//Snapshot Full Refresh
				addSnapshotFullRefresh((MarketDataSnapshotFullRefresh) message);	
			}

		} catch (FieldNotFound e) {
			e.printStackTrace();
		}

	}

	private void addIncrementalRefresh(MarketDataIncrementalRefresh incrementalRefresh) {

	}

	private void addSnapshotFullRefresh(MarketDataSnapshotFullRefresh fullRefresh) {



	}


	//de acordo com MDEntryType
	//Type Market Data entry. Valid values:
	/**
	 * 0 = Bid
	 * 1 = Offer
	 * 
	 */
	private void addOrder(Message message) {
		try {
			
			
			
			OrdersVO order = new OrdersVO();
			/*
			Types of Market Data update action
			0 = New
			1 = Change
			2 = Delete
			3 = Delete Thru
			4 = Delete From
			5 = Overlay
			*/
			order.setQueuePosition(message.getInt(290));
			order.setOrderID(message.getString(37));
			
			
			
			
			/*
			279 MDUpdateAction “0”,”1”,”2”,”3”,”4”
			269 MDEntryType “0”,”1”
			83 RptSeq X
			48 SecurityID X 
			22 SecurityIDSource X 
			207 SecurityExchange X
			1500 MDStreamID C “L” - for BTC book 
			270 MDEntryPx C Not sent for MOA and MOC
			271 MDEntrySize X
			432 ExpireDate C Used for BTC contracts only
			37019 EarlyTermination C Used for BTC contracts only 272 MDEntryDate X
			273 MDEntryTime X
			37016 MDInsertDate X
			37017 MDInsertTime X
			276 QuoteCondition “C”, ”R” “R” – On book retransmission
			288 MDEntryBuyer C Sent on bids, but not on MBP/TOB or FX 289 MDEntrySeller C Sent on offers, but not on MBP/TOB or FX
			290 MDEntryPositionNo X
			37 OrderID X*/

			order.setSide(Side.getByValue(message.getChar(269)));
			order.setSecurityID(message.getString(48));
			order.setPrice(message.getDouble(270));
			order.setVolume(message.getDouble(271));

			//(message.getString(37016) + message.getString(37017));
			//order.setOrderDateTime(orderDateTime);
			
			
		}catch(Exception e) {

		}

	}

	/**
	 * 2 = Trade 
	 */

	/**
	 * 	3 = Index Value
	 */

	/**
	 * 	4 = Opening Price
	 */

	/**
	 * 	5 = Closing Price
	 */

	/**
	 * 	6 = Settlement Price
	 */

	/**
	 * 7 = Session High Price
	 * 8 = Session Low Price
	 * 9 = Session VWAP Price
	 * 
	 */

	/**
	 * A = Imbalance
	 */

	/**
	 * B = Trade Volume
	 */

	/**
	 * C = Open Interest
	 */

	/**
	 * J = Empty Book
	 */

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
	 * @return the buyQueue
	 */
	public ConcurrentLinkedQueue<Orders> getBuyQueue() {
		return buyQueue;
	}


	/**
	 * @return the sellQueue
	 */
	public ConcurrentLinkedQueue<Orders> getSellQueue() {
		return sellQueue;
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
	public double getVwap() {
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
	 * @param security the security to set
	 */
	public void setSecurity(Security security) {
		this.security = security;
	}


	/**
	 * @param buyQueue the buyQueue to set
	 */
	public void setBuyQueue(ConcurrentLinkedQueue<Orders> buyQueue) {
		this.buyQueue = buyQueue;
	}


	/**
	 * @param sellQueue the sellQueue to set
	 */
	public void setSellQueue(ConcurrentLinkedQueue<Orders> sellQueue) {
		this.sellQueue = sellQueue;
	}


	/**
	 * @param tradeCount the tradeCount to set
	 */
	public void setTradeCount(long tradeCount) {
		this.tradeCount = tradeCount;
	}


	/**
	 * @param openPrice the openPrice to set
	 */
	public void setOpenPrice(double openPrice) {
		this.openPrice = openPrice;
	}


	/**
	 * @param closePrice the closePrice to set
	 */
	public void setClosePrice(double closePrice) {
		this.closePrice = closePrice;
	}


	/**
	 * @param maxPrice the maxPrice to set
	 */
	public void setMaxPrice(double maxPrice) {
		this.maxPrice = maxPrice;
	}


	/**
	 * @param minPrice the minPrice to set
	 */
	public void setMinPrice(double minPrice) {
		this.minPrice = minPrice;
	}


	/**
	 * @param lastVolume the lastVolume to set
	 */
	public void setLastVolume(double lastVolume) {
		this.lastVolume = lastVolume;
	}


	/**
	 * @param lastPrice the lastPrice to set
	 */
	public void setLastPrice(double lastPrice) {
		this.lastPrice = lastPrice;
	}


	/**
	 * @param vwap the vwap to set
	 */
	public void setVwap(double vwap) {
		this.vwap = vwap;
	}


	/**
	 * @param tradeVolume the tradeVolume to set
	 */
	public void setTradeVolume(double tradeVolume) {
		this.tradeVolume = tradeVolume;
	}


	/**
	 * @param phase the phase to set
	 */
	public void setPhase(MarketPhase phase) {
		this.phase = phase;
	}


}
