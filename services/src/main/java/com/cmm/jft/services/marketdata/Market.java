/**
 * 
 */
package com.cmm.jft.services.marketdata;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import quickfix.FieldMap;
import quickfix.FieldNotFound;
import quickfix.Group;
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
import com.cmm.jft.vo.TimeSalesVO;

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
	private ConcurrentLinkedQueue<OrdersVO> buyQueue;

	/**
	 * MDEntryType=1
	 */
	private ConcurrentLinkedQueue<OrdersVO> sellQueue;
	
	private ConcurrentHashMap<String, OrdersVO> orders;
	
	private ConcurrentHashMap<String, TimeSalesVO> timeSales;
	//private ConcurrentLinkedQueue<OrderEvent> timeSales;


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
		this.orders = new ConcurrentHashMap<String, OrdersVO>();
		this.timeSales = new ConcurrentHashMap<String, TimeSalesVO>();

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
		
		//tag 264 indica a profundidade de mercado nas mensagens, pois ha dois canais 
		//MBO e MBP, verificar o canal tambem
		
	}

	/**
	 * MDUpdateAction 0 e 1
	 * @param order
	 */
	private void newOrder(OrdersVO order) {
		//adiciona ou sobrescreve...
		orders.put(order.getOrderID(), order);
	}
	
	
	/**
	 * Para MDUpdateAction 2, 3 e 4
	 */
	private void deleteOrder(String orderID) {
		orders.remove(orderID);
	}
	
	/**
	 * Remove as ordens contidas no book a partir da posicao 
	 * passada por parametro ate a primeira posicao. 
	 * @param side lado que devera ser removida a ordem.
	 * @param position posicao a partir da qual as ordens serao removidas.
	 */
	private void deleteFrom(Side side, int position) {
		ConcurrentLinkedQueue<OrdersVO> queue = null;
		if(side == Side.BUY) {
			queue = buyQueue;
			
		}
		else {
			queue = sellQueue;
		}
		
		queue.removeIf(
				ord -> 
				ord.getQueuePosition() < position
				);
		queue.parallelStream()
		.forEach(
				ord -> 
				ord.setQueuePosition(ord.getQueuePosition() - (position))
				);
		
	}
	
	/**
	 * Remove todas as ofertas no book a partir da posicao enviada
	 * 
	 * @param side lado ao qual deve-se remover as ofertas do book.
	 * @param position posicao a partir da qual deve-se remover as ofertas.
	 */
	private void deleteThru(Side side, int position) {
		
		//como a bvmf emvia fixo 1. 
		//sera feita a selecao do lado e a remocao de todas ofertas
		if(side == Side.BUY) {
			buyQueue.clear();
		}
		else {
			sellQueue.clear();
		}
		
		
	}
	
	
	
	private void overlay(Side side) {
		
		
		
	}
	 
	
	
	/**
	 * Type Market Data entry. Valid values:
	 * 0 = Bid
	 * 1 = Offer
	 * 
	 */
	private void treatOrder(Group message) {
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

//			tag 279-MDUpdateAction
//			tag 269-MDEntryType
//			tag 48-SecurityID
//			tag 83-RptSeq
//			tag 270-MDEntryPx
//			tag 271-MDEntrySize
//			tag 1023-MDPriceLevel
//			tag 346-NumberOfOrders
			
			int mdUpdtAction = message.getChar(279);//item dentro de grupo mdEntryType
			Side side = Side.getByValue(message.getChar(269));
			int queuePos = message.getInt(290);
			String ordrID = message.getString(37);
			
			String securityID = message.getString(48);
			String secIdSource = message.getString(22);
			String securityExchange = message.getString(207);
			
			order.setQueuePosition(queuePos);
			order.setOrderID(ordrID);
			order.setSide(side);
			order.setPrice(message.getDouble(270));
			order.setVolume(message.getDouble(271));
			
			switch(mdUpdtAction) {
			case 0:
				newOrder(order);
				break;
			case 1:
				newOrder(order);
				break;
			case 2:
				deleteOrder(ordrID);
				break;
			case 3:
				deleteFrom(side, queuePos);
				break;
			case 4:
				deleteThru(side, queuePos);
				break;
			case 5:
				overlay(side);
				break;
			}
						
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
			37019 EarlyTermination C Used for BTC contracts only 
			272 MDEntryDate X
			273 MDEntryTime X
			37016 MDInsertDate X
			37017 MDInsertTime X
			276 QuoteCondition “C”, ”R” “R” – On book retransmission
			288 MDEntryBuyer C Sent on bids, but not on MBP/TOB or FX 289 MDEntrySeller C Sent on offers, but not on MBP/TOB or FX
			290 MDEntryPositionNo X
			37 OrderID X*/

			//(message.getString(37016) + message.getString(37017));
			//order.setOrderDateTime(orderDateTime);
			
		}catch(Exception e) {

		}

	}


	/**
	 * 2 = Trade 
	 */
	private void treatTrade(Group message) {
		try {

			if(message.getChar(269) == '2') {

				int mdUpdtAction = message.getInt(279);
				String tradeID = message.getString(1003);
				if(mdUpdtAction == 0) {

					//String security = message.getString(48);
					//String secIdSource = message.getString(22);
					//String securityExchange = message.getString(207);

					TimeSalesVO ts = new TimeSalesVO();
					ts.setPrice(message.getDouble(270));
					ts.setVolume(message.getDouble(271));

					String date = message.getString(272);
					String time = message.getString(273);
					Instant i = Instant.from(DateTimeFormatter.ofPattern("yyyyMMdd hh:mm:ss").parse(date + " " + time));
					ts.setDateTime(Date.from(i));

					ts.setBuyer(message.getString(288));
					ts.setSeller(message.getString(289));

					timeSales.put(tradeID, ts);

				}else if(mdUpdtAction == 2) {
					timeSales.remove(tradeID);
				}

			}

		}catch(FieldNotFound e) {

		}
	}


	/**
	 * 	3 = Index Value
	 */

	/**
	 * 	4 = Opening Price
	 */
	private void treatOpeningPrice(Group message) {
		try {

			if(message.getChar(269) == '4') {
				//String security = message.getString(48);
				//String secIdSource = message.getString(22);
				//String securityExchange = message.getString(207);

				double price = message.getDouble(270);
				if(message.getInt(279) == 2) {
					price = 0;
				}

				openPrice = price;
			}

		}catch(FieldNotFound e) {

		}
	}


	/**
	 * 	5 = Closing Price
	 */
	private void treatClosingPrice(Group message) {
		try {

			if(message.getChar(269) == '5') {
				//String security = message.getString(48);
				//String secIdSource = message.getString(22);
				//String securityExchange = message.getString(207);

				//sempre sobrescreve
				closePrice = message.getDouble(270);
			}

		}catch(FieldNotFound e) {

		}
	}



	/**
	 * 	6 = Settlement Price
	 */

	/**
	 * 7 = Session High Price
	 * 8 = Session Low Price
	 * 9 = Session VWAP Price
	 * 
	 */
	private void treatHighLowVWAPPrice(Group message) {
		try {

			int type = message.getInt(269);
			if(type >=7 && type <= 9 ) {
				double price = message.getDouble(270);
				if(message.getInt(279) == 2) {
					price = 0;
				}			

				switch(type) {
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

		}catch(FieldNotFound e) {

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
			if(message.getChar(269) == 'B') {
				tradeCount = message.getInt(271);
				tradeVolume = message.getDouble(270);

			}

		}catch(FieldNotFound e) {

		}

	}



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
	public ConcurrentLinkedQueue<OrdersVO> getBuyQueue() {
		return buyQueue;
	}


	/**
	 * @return the sellQueue
	 */
	public ConcurrentLinkedQueue<OrdersVO> getSellQueue() {
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
	public void setBuyQueue(ConcurrentLinkedQueue<OrdersVO> buyQueue) {
		this.buyQueue = buyQueue;
	}


	/**
	 * @param sellQueue the sellQueue to set
	 */
	public void setSellQueue(ConcurrentLinkedQueue<OrdersVO> sellQueue) {
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
