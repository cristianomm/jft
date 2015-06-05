/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.trading;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.*;

import org.apache.log4j.Level;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.trading.enums.MarketEvents;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.exceptions.OrderException;
import com.cmm.jft.trading.securities.Security;
import com.cmm.logging.Logging;

/**
 *
 * <p>
 * <code>Orders</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "Orders", uniqueConstraints = { @UniqueConstraint(columnNames = { "OrderSerial" }) })
@NamedQueries({
		@NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o"),
		@NamedQuery(name = "Orders.findByOrderID", query = "SELECT o FROM Orders o WHERE o.orderID = :orderID"),
		@NamedQuery(name = "Orders.findByVolume", query = "SELECT o FROM Orders o WHERE o.volume = :volume"),
		@NamedQuery(name = "Orders.findByAveragePrice", query = "SELECT o FROM Orders o WHERE o.averagePrice = :averagePrice"),
		@NamedQuery(name = "Orders.findByExecutedVolume", query = "SELECT o FROM Orders o WHERE o.executedVolume = :executedVolume"),
		@NamedQuery(name = "Orders.findByDuration", query = "SELECT o FROM Orders o WHERE o.duration = :duration"),
		@NamedQuery(name = "Orders.findByOrderStatus", query = "SELECT o FROM Orders o WHERE o.orderStatus = :orderStatus"),
		@NamedQuery(name = "Orders.findBySide", query = "SELECT o FROM Orders o WHERE o.side = :side") })
public class Orders implements DBObject<Orders> {
	private static final long serialVersionUID = 1L;
	@Id
	@SequenceGenerator(name = "ORDERS_SEQ", sequenceName = "ORDERS_SEQ", allocationSize = 1, initialValue = 1)
	@GeneratedValue(generator = "ORDERS_SEQ", strategy = GenerationType.AUTO)
	@Basic(optional = false)
	@Column(name = "orderID", nullable = false)
	private Long orderID;

	@Column(name = "Price", precision = 19, scale = 6)
	private BigDecimal price;
		
	@Column(name = "LimitPrice", precision = 19, scale = 6)
	private BigDecimal limitPrice;

	@Column(name = "StopPrice", precision = 19, scale = 6)
	private BigDecimal stopPrice;

	@Column(name = "StopGain", precision = 19, scale = 6)
	private BigDecimal stopGain;

	@Basic(optional = false)
	@Column(name = "Volume", nullable = false)
	private Integer volume;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "AveragePrice", precision = 19, scale = 6)
	private BigDecimal averagePrice;

	@Column(name = "ExecutedVolume")
	private Integer executedVolume;

	@Column(name = "Duration", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date duration;

	@Column(name = "OrderDateTime", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date orderDateTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "OrderStatus", nullable = false)
	private OrderStatus orderStatus;

	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "Side", nullable = false, updatable = false)
	private Side side;
	
	@Column(name="Comment", length=250)
	private String comment;

	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "OrderType", nullable = false, updatable = false)
	private OrderTypes orderType;

	@Basic(optional = false)
	@Column(name = "OrderSerial", length = 25, updatable = false, nullable = false)
	private String orderSerial;

	@JoinColumn(name = " tradeID", referencedColumnName = "tradeID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Trade tradeID;

	@JoinColumn(name = "securityID", referencedColumnName = "securityID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Security securityID;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderID")
	private List<OrderEvent> eventsList;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderID")
	private List<OrderExecution> executionsList;

	public Orders() {
		this.limitPrice = new BigDecimal(0);
		this.price = new BigDecimal(0);
		this.stopGain = new BigDecimal(0);
		this.stopPrice = new BigDecimal(0);
		this.duration = new Date();
		this.orderDateTime = new Date();
		this.orderStatus = OrderStatus.CREATED;
		this.orderSerial = UUID.randomUUID().toString();
		this.eventsList = new ArrayList<OrderEvent>();
		this.executionsList = new ArrayList<OrderExecution>();
	}

	/**
	 * @param volume
	 * @param duration
	 * @param operation
	 * @param orderType
	 * @param tradeID
	 * @param securityID
	 * @throws OrderException 
	 */
	public Orders(OrdersPrices prices, Integer volume, Date duration, Side side,
			OrderTypes orderType, Trade tradeID, Security securityID) throws OrderException {
		super();
		putPrices(prices);
//		this.limitPrice = new BigDecimal(0);
//		this.price = new BigDecimal(0);
//		this.startPrice = new BigDecimal(0);
//		this.stopGain = new BigDecimal(0);
//		this.stopPrice = new BigDecimal(0);
		this.volume = volume;
		this.duration = duration;
		this.orderDateTime = new Date();
		this.side = side;
		this.orderType = orderType;
		this.tradeID = tradeID;
		this.securityID = securityID;
		this.orderStatus = OrderStatus.CREATED;
		this.orderSerial = UUID.randomUUID().toString();
		this.eventsList = new ArrayList<OrderEvent>();
		this.executionsList = new ArrayList<OrderExecution>();
	}

	public void refreshOrder() throws OrderException {

		try {
			// calcula o preco medio
			calculateAveragePrice();

			// ajusta o status
			int cv = volume - executedVolume;

			// ordem ainda nao foi totalmente executada
			if (cv > 0 && cv != volume) {
				orderStatus = OrderStatus.PARTIALLY_FILLED;
			} else if (cv == 0) {
				orderStatus = OrderStatus.FILLED;
			} else if (cv < 0) {// erro!!!!!
				throw new OrderException("ExecutedVolume is greater than order volume: " + executedVolume);
			}

		} catch (Exception e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
			throw new OrderException(e);
		}

	}

	public BigDecimal calculateAveragePrice() {
		int sumVolume = 0;
		BigDecimal sumTotal = new BigDecimal(0);
		for (OrderExecution oe : executionsList) {
			sumVolume += oe.getVolume();
			sumTotal = sumTotal.add(oe.getPrice().multiply(
					new BigDecimal(oe.getVolume())));
		}

		// ajusta os valores de execucao da ordem
		executedVolume = sumVolume;
		int sv = executionsList.size() > 0 ? executionsList.size() : 1;
		averagePrice = sumTotal.divide(new BigDecimal(sv));

		return averagePrice;
	}

	public BigDecimal getOrderValue() {
		return price.multiply(new BigDecimal(volume));
	}

	public BigDecimal getExecutedOrderValue() {
		return averagePrice.multiply(new BigDecimal(executedVolume));
	}

	public Long getOrderID() {
		return orderID;
	}

	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return this.price;
	}

	/**
	 * @return the limitPrice
	 */
	public BigDecimal getLimitPrice() {
		return this.limitPrice;
	}
	/**
	 * @return the stopPrice
	 */
	public BigDecimal getStopPrice() {
		return this.stopPrice;
	}

	/**
	 * @return the stopGain
	 */
	public BigDecimal getStopGain() {
		return this.stopGain;
	}

	/**
	 * @return the orderType
	 */
	public OrderTypes getOrderType() {
		return this.orderType;
	}

	/**
	 * @param orderType
	 *            the orderType to set
	 */
	public void setOrderType(OrderTypes orderType) {
		this.orderType = orderType;
	}

	public Integer getVolume() {
		return volume;
	}

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public Integer getExecutedVolume() {
		return executedVolume;
	}

	public Date getDuration() {
		return duration;
	}

	public void setDuration(Date duration) {
		this.duration = duration;
	}

	/**
	 * @return the orderDateTime
	 */
	public Date getOrderDateTime() {
		return this.orderDateTime;
	}

	/**
	 * @param orderDateTime
	 *            the orderDateTime to set
	 */
	public void setOrderDateTime(Date orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Side getSide() {
		return side;
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public Trade getTradeID() {
		return tradeID;
	}

	public void setTradeID(Trade tradeID) {
		this.tradeID = tradeID;
	}

	public Security getSecurityID() {
		return securityID;
	}

	public void setSecurityID(Security securityID) {
		this.securityID = securityID;
	}

	/**
	 * @return the orderSerial
	 */
	public String getOrderSerial() {
		return orderSerial;
	}

	/**
	 * @return the eventsList
	 */
	public List<OrderEvent> getEventsList() {
		return this.eventsList;
	}

	/**
	 * @return the executionsList
	 */
	public List<OrderExecution> getExecutionsList() {
		return this.executionsList;
	}
	
	/**
	 * @return the comment
	 */
	public String getComment() {
		return this.comment;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final int maxLen = 10;
		return "Orders ["
				+ (this.orderID != null ? "orderID=" + this.orderID + ", " : "")
				+ (this.price != null ? "price=" + this.price + ", " : "")
				+ (this.limitPrice != null ? "limitPrice=" + this.limitPrice + ", " : "")
				+ (this.stopPrice != null ? "stopPrice=" + this.stopPrice + ", " : "")
				+ (this.stopGain != null ? "stopGain=" + this.stopGain + ", " : "")
				+ (this.volume != null ? "volume=" + this.volume + ", " : "")
				+ (this.averagePrice != null ? "averagePrice=" + this.averagePrice + ", " : "")
				+ (this.executedVolume != null ? "executedVolume=" + this.executedVolume + ", " : "")
				+ (this.duration != null ? "duration=" + this.duration + ", " : "")
				+ (this.orderDateTime != null ? "orderDateTime=" + this.orderDateTime + ", " : "")
				+ (this.orderStatus != null ? "orderStatus=" + this.orderStatus + ", " : "")
				+ (this.side != null ? "side=" + this.side + ", " : "")
				+ (this.orderType != null ? "orderType=" + this.orderType + ", " : "")
				+ (this.orderSerial != null ? "orderSerial=" + this.orderSerial + ", " : "")
				+ (this.tradeID != null ? "tradeID=" + this.tradeID + ", " : "")
				+ (this.securityID != null ? "securityID=" + this.securityID + ", " : "")
				+ (this.eventsList != null ? "eventsList="
						+ this.eventsList.subList(0,
								Math.min(this.eventsList.size(), maxLen))
						+ ", " : "")
				+ (this.executionsList != null ? "executionsList="
						+ this.executionsList.subList(0,
								Math.min(this.executionsList.size(), maxLen))
						: "") + "]";
	}

	
	
	
	public boolean addExecution(Date executionDateTime, int execVolume, BigDecimal execPrice) throws OrderException{
		boolean ret = false;
		
		if(orderStatus == OrderStatus.OPEN || orderStatus == OrderStatus.PARTIALLY_FILLED){
			//volume executado eh menor que o volume total e menor que o volume atual
			if(execVolume<=volume && execVolume <= (volume-executedVolume)){
				// cria o evento
				OrderEvent oev = new OrderEvent(MarketEvents.TRADE, "Execution of "
				+ execVolume + " at price " + execPrice, executionDateTime, this);
				
				// cria a execucao
				OrderExecution oex = new OrderExecution(executionDateTime, execVolume, execPrice, this);

				eventsList.add(oev);
				executionsList.add(oex);
				
				//ajusta o estado da ordem
				refreshOrder();
			}
		}else{
			throw new OrderException("Order status is invalid to add execution: " + orderStatus);
		}
		
		return ret;
	}
	
	public void cancel() {
		if (orderStatus != OrderStatus.CANCELED) {
			orderStatus = OrderStatus.CANCELED;
		}
	}
	
	
	public boolean changePrice(OrdersPrices prices) throws OrderException {
		boolean ret = false;
		if(orderStatus == OrderStatus.OPEN){
			putPrices(prices);
			ret = true;
		}else{
			ret = false;
			throw new OrderException("Invalid Order status: " + orderStatus);
		}
		return ret;
	}
	
	public boolean changeVolume(int volume) throws OrderException {
		boolean ret = false;
		if(orderStatus == OrderStatus.OPEN){
			this.volume = volume;
			ret = true;
		}else{
			ret = false;
			throw new OrderException("Invalid Order status: " + orderStatus);
		}
		return ret;
	}
	
	private void putPrices(OrdersPrices prices) throws OrderException {

		switch (orderType) {
		case LIMIT:
			if (prices.price <= 0) {
				throw new OrderException(String.format(
						"Invalid Prices to Limit Orders(price): %f",
						prices.price));
			} else {
				price = new BigDecimal("" + prices.price);
			}

			break;
		case MARKET:

			break;
		case STOP:
			if (prices.limit <= 0 || prices.stop <= 0) {
				throw new OrderException(String.format(
						"Invalid Prices to Stop Orders(limit, stop): %f, %f",
						prices.limit, prices.stop));
			} else {
				limitPrice = new BigDecimal("" + prices.limit);
				stopPrice = new BigDecimal("" + prices.stop);
			}
			break;
		case STOP_GAIN:
			if (prices.limit <= 0 || prices.stop <= 0 || prices.gain <= 0) {
				throw new OrderException(
						String.format(
								"Invalid Prices to StopGain Orders(limit, stop, gain): %f, %f, %f",
								prices.limit, prices.stop, prices.gain));
			} else {
				limitPrice = new BigDecimal("" + prices.limit);
				stopPrice = new BigDecimal("" + prices.stop);
				stopGain = new BigDecimal("" + prices.gain);
			}
			break;
		case STOP_LIMIT:
			if (prices.price <= 0 || prices.limit <= 0 || prices.stop <= 0) {
				throw new OrderException(
						String.format(
								"Invalid Prices to StopLimit Orders(price, limit, stop): %f, %f, %f",
								prices.price, prices.limit, prices.stop));
			} else {
				price = new BigDecimal("" + prices.price);
				limitPrice = new BigDecimal("" + prices.limit);
				stopPrice = new BigDecimal("" + prices.stop);
			}

		default:
			throw new OrderException("Order type not allowed:" + orderType);
		}

	}

	public OrdersPrices getOrdersPrices() {
		OrdersPrices prices = new OrdersPrices();
		prices.gain = getStopGain().doubleValue();
		prices.limit = getLimitPrice().doubleValue();
		prices.price = getPrice().doubleValue();
		prices.stop = getStopPrice().doubleValue();
		return prices;
	}
	
	

}
