
package com.cmm.jft.trading;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.db.DBObject;
import com.cmm.jft.financial.JournalEntry;
import com.cmm.jft.financial.services.JournalService;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;


/**
 * 
 * <p>
 * <code>Trade</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "Trade")
@NamedQueries({
		@NamedQuery(name = "Trade.findAll", query = "SELECT t FROM Trade t"),
		@NamedQuery(name = "Trade.findByTradeID", query = "SELECT t FROM Trade t WHERE t.tradeID = :tradeID"),
		@NamedQuery(name = "Trade.findByTradeSerial", query = "SELECT t FROM Trade t WHERE t.tradeSerial = :tradeSerial"),
		//, uniqueConstraints = { @UniqueConstraint(columnNames = { "TradeSerial" }) }
		// @NamedQuery(name = "Trade.findOpenSymbol", query =
// "SELECT t FROM Trade t INNER JOIN journalentry je ON t.entryid = je.entryid WHERE je.journalstatus = 'OPEN' AND je.entryclose IS NULL AND t.symbol = :symbol"),
})
public class Trade implements DBObject<Trade> {

	@Id
	@SequenceGenerator(name = "TRADE_SEQ", sequenceName = "TRADE_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "TRADE_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "tradeID", nullable = false)
	private Long tradeID;

	@Column(name = "TradeSerial", length = 25, updatable = false, nullable = false, unique=true)
	private String tradeSerial;

	@JoinColumn(name = "entryID", referencedColumnName = "entryID", nullable = false)
	@OneToOne(optional = false, fetch = FetchType.LAZY)
	private JournalEntry entryID;
	
	@Column(name = "Symbol", length = 10)
	private String symbol;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "OpenDate")
	private Date openDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CloseDate")
	private Date closeDate;

	@Column(name = "Profit", precision = 19, scale = 6)
	private BigDecimal profit;

	@Enumerated(EnumType.STRING)
	@Column(name = "TradeType", length = 20)
	private TradeTypes tradeType;

	@Enumerated(EnumType.STRING)
	@Column(name = "TradeStatus")
	private GeneralStatus tradeStatus;

	@JoinColumn(name = "brokerID", referencedColumnName = "brokerID", nullable = false)
	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	private Broker brokerID;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "tradeID", fetch = FetchType.LAZY)
	private List<Orders> ordersList;
	
	
	public Trade() {
		this.symbol = "";
		this.tradeType = TradeTypes.NORMAL;
		init();
	}

	/**
	 * @param symbol
	 */
	public Trade(String symbol, TradeTypes type, Broker brokerID) {
		this.symbol = symbol;
		this.tradeType = type;
		this.brokerID = brokerID;
		init();
	}

	private void init() {
		this.openDate = new Date();
		this.entryID = JournalService.getInstance().createEntry();
		this.tradeSerial = UUID.randomUUID().toString();
		this.entryID.setDescription("Created by Trade: " + tradeSerial);
		this.ordersList = new ArrayList<Orders>();
		this.tradeStatus = GeneralStatus.OPEN;
	}

	public void refreshTrade() {

		// calcula o lucro
		getProfit();

		// ajusta o status e a data de fechamento caso tenha sido completado
		if (getPosition() == 0) {
			tradeStatus = GeneralStatus.CLOSE;
			closeDate = new Date();
		}

	}

	public BigDecimal getProfit() {
		BigDecimal buy = new BigDecimal(0);
		BigDecimal sell = new BigDecimal(0);

		Iterator<Orders> itOrd = ordersList.iterator();
		while (itOrd.hasNext()) {
			Orders o = itOrd.next();
			if (o.getOrderStatus() == OrderStatus.FILLED) {
				if (o.getSide() == Side.BUY) {
					buy = buy.add(o.getExecutedOrderValue());
				} else {
					sell = sell.add(o.getExecutedOrderValue());
				}
				profit = sell.subtract(buy);
			}
		}
		return profit;
	}

	/**
	 * Calcula a posicao de ordens de compra executadas. 
	 * @return 
	 */
	public int getBuyTradedVolume() {
		int volume = 0;
		
		Iterator<Orders> itOrd = ordersList.iterator();
		while (itOrd.hasNext()) {
			Orders o = itOrd.next();
			if (o.getOrderStatus() == OrderStatus.FILLED) {
				if (o.getSide() == Side.BUY) {
					volume+= o.getExecutedVolume();
				}
			}
		}
		
		return volume;
	}

	/**
	 * Calcula a posicao de ordens de venda executadas. 
	 * @return 
	 */
	public int getSellTradedVolume() {
		int volume = 0;
		
		Iterator<Orders> itOrd = ordersList.iterator();
		while (itOrd.hasNext()) {
			Orders o = itOrd.next();
			if (o.getOrderStatus() == OrderStatus.FILLED) {
				if (o.getSide() == Side.SELL) {
					volume+= o.getExecutedVolume();
				}
			}
		}
		
		return volume;
	}
	
	
	/**
	 * Calcula o volume negociado das ordens de compra e venda
	 * que foram totalmente executadas. O volume retornado esta relacionado ao ciclo compra - venda
	 * um volume sera considerado somente se existir um volume de compra e um volume de venda. 
	 * - Caso tenha executado mais compras do que vendas, retorna o volume das ordens de venda executadas.
	 * - Caso tenha realizado maior numero de vendas , retorna o numero de compras realizadas.
	 * @return
	 */
	public int getTradedVolume() {
		int volume = 0;
		
		int bt = getBuyTradedVolume();
		int st = getSellTradedVolume();
		
		volume = bt;
		if(bt>st){
			volume = st;
		}
		else{
			volume = bt;
		}
		
		return volume;
	}
	
	
	/**
	 * Calcula o valor das negociacoes, de acordo com todas as ordens de venda
	 * executadas.
	 * 
	 * @return Valor das vendas para o trade.
	 */
	public BigDecimal getTradeValue() {
		BigDecimal val = new BigDecimal(0);

		Iterator<Orders> itOrd = ordersList.iterator();
		while (itOrd.hasNext()) {
			Orders o = itOrd.next();
			if (o.getOrderStatus() == OrderStatus.FILLED) {
				if (o.getSide() == Side.SELL) {
					val = val.add(o.getExecutedOrderValue());
				}
			}
		}
		return val;
	}

	/**
	 * @return the entryID
	 */
	public JournalEntry getEntryID() {
		return this.entryID;
	}

	/**
	 * @param entryID
	 *            the entryID to set
	 */
	public void setEntryID(JournalEntry entryID) {
		this.entryID = entryID;
	}

	/**
	 * @return the tradeID
	 */
	public Long getTradeID() {
		return this.tradeID;
	}

	/**
	 * @return the tradeSerial
	 */
	public String getTradeSerial() {
		return this.tradeSerial;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * @return the tradeStatus
	 */
	public GeneralStatus getTradeStatus() {
		return this.tradeStatus;
	}

	/**
	 * @return the tradeType
	 */
	public TradeTypes getTradeType() {
		return this.tradeType;
	}

	/**
	 * @return the brokerID
	 */
	public Broker getBrokerID() {
		return this.brokerID;
	}

	public boolean isOpen() {
		return tradeStatus==GeneralStatus.OPEN; //entryID. getJournalStatus() == JournalStatus.OPEN ? true : false;
	}

	public void addOrder(Orders orders) {
		if (symbol.isEmpty()) {
			symbol = orders.getSecurityID().getSymbol();
		}
		ordersList.add(orders);
	}

	public Orders getOrder(String orderSerial) {
		for (Orders o : ordersList) {
			if (o.getOrderSerial().equalsIgnoreCase(orderSerial)) {
				return o;
			}
		}
		return null;
	}
	
	
	/**
	 * A partir das ordens totalmente executadas, calcula a posicao atual do trade
	 * @return Posicao atual calculado a partir das ordens totalmente executadas
	 */
	public int getPosition() {
		int buy = 0, sell = 0;
		for (Orders o : ordersList) {
			if (o.getOrderStatus() == OrderStatus.FILLED) {
				buy += o.getSide() == Side.BUY ? o.getExecutedVolume() : 0;
				sell += o.getSide() == Side.SELL ? o.getExecutedVolume() : 0;
			}
		}

		return buy - sell;
	}

	
	/**
	 * Calcula a posicao atual das ordens que ainda nao foram totalmente executadas
	 * @return
	 */
	public int getOpenPosition() {
		int buy = 0, sell = 0;
		for (Orders o : ordersList) {
			if (o.getOrderStatus() == OrderStatus.OPEN || o.getOrderStatus() == OrderStatus.PARTIALLY_FILLED) {
				buy += o.getSide() == Side.BUY ? o.getExecutedVolume() : 0;
				sell += o.getSide() == Side.SELL ? o.getExecutedVolume() : 0;
			}
		}

		return buy - sell;
	}

	
	public Brokerage getBrokerage() {

		Brokerage brokerage = null;
		Orders ordr = ordersList.iterator().next();
		for (Brokerage brkg : brokerID.getBrokerageList()) {
			if (tradeType == brkg.getTradeType()) {
				if (ordr.getSecurityID().getSecurityInfoID().getCategory() == brkg.getCategory()) {
					brokerage = brkg;
					break;
				}
			}
		}

		return brokerage;
	}

	/**
	 * @return the closeDate
	 */
	public Date getCloseDate() {
		return this.closeDate;
	}

	/**
	 * @return the openDate
	 */
	public Date getOpenDate() {
		return this.openDate;
	}

	/**
	 * @param closeDate
	 *            the closeDate to set
	 */
	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	/**
	 * @param openDate
	 *            the openDate to set
	 */
	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}
	
	/**
	 * @return the ordersList
	 */
	public List<Orders> getOrdersList() {
		return this.ordersList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "Trade [tradeID=" + this.tradeID
				+ ", tradeSerial=" + this.tradeSerial
				+ ", entryID=" + this.entryID
				+ ", ordersSet="
				+ (this.ordersList != null ? this.toString(this.ordersList, maxLen) : null) 
				+ ", symbol=" + this.symbol
				+ ", openDate=" + this.openDate + ", closeDate="
				+ this.closeDate + ", tradeType=" + this.tradeType
				+ ", tradeStatus=" + this.tradeStatus + "]";
	}

	private String toString(Collection<?> collection, int maxLen) {
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		int i = 0;
		for (Iterator<?> iterator = collection.iterator(); iterator.hasNext()
				&& i < maxLen; i++) {
			if (i > 0)
				builder.append(", ");
			builder.append(iterator.next());
		}
		builder.append("]");
		return builder.toString();
	}

}
