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
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.MarketEvents;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.exceptions.InvalidOrderException;
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
	private Double price;

	@Basic(optional = false)
	@Column(name = "Volume", nullable = false)
	private Integer volume;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "AvgPrice", precision = 19, scale = 6)
	private BigDecimal avgPrice;

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
	@Column(name = "OrderValidityType", nullable = false)
	private OrderValidityTypes validityType;
	
	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "OrderType", nullable = false, updatable = false)
	private OrderTypes orderType;
	
	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "TradeType", nullable = false, updatable = false)
	private TradeTypes tradeType;
	
	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "Side", nullable = false, updatable = false)
	private Side side;
	
	@Column(name="Comment", length=250)
	private String comment;

	@Basic(optional = false)
	@Column(name = "OrderSerial", length = 25, updatable = false, nullable = false)
	private String orderSerial;
	
	@Column(name="Serial", length=25, updatable=false, nullable=false)
	private String Serial;
	
	@JoinColumn(name = "securityID", referencedColumnName = "securityID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Security securityID;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderID")
	private List<OrderExecution> executionsList;

	
	
	public Orders() {
		this.price =  0d;
		this.duration = new Date();
		this.orderDateTime = new Date();
		this.orderStatus = OrderStatus.CREATED;
		this.orderSerial = UUID.randomUUID().toString();
		this.executionsList = new ArrayList<OrderExecution>();
	}
	
	
	

	/**
	 * @param limitPrice
	 * @param volume
	 * @param orderType
	 * @param tradeType
	 * @param side
	 * @throws OrderException 
	 */
	public Orders(Security security,  Side side, double price, Integer volume, 
			OrderTypes orderType, TradeTypes tradeType) throws OrderException {
		super();
		this.price = price;
		this.securityID = security;
		this.volume = volume;
		this.orderType = orderType;
		this.tradeType = tradeType;
		this.side = side;
		init();
		//putPrices(orderPrices);
	}
	

	private void init(){
		this.orderDateTime = new Date();
		this.orderStatus = OrderStatus.CREATED;
		this.orderSerial = UUID.randomUUID().toString();
		this.executionsList = new ArrayList<OrderExecution>();
	}
	
	
	
	private void refreshOrder() throws OrderException {

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
		double sumTotal = 0d;
		for (OrderExecution oe : executionsList) {
			sumVolume += oe.getVolume();
			sumTotal += oe.getPrice() * oe.getVolume();
		}

		// ajusta os valores de execucao da ordem
		executedVolume = sumVolume;
		int sv = executionsList.size() > 0 ? executionsList.size() : 1;
		avgPrice = new BigDecimal(sumTotal/sv);

		return avgPrice;
	}

	public double getOrderValue() {
		return price * volume;
	}

	public BigDecimal getExecutedOrderValue() {
		return avgPrice.multiply(new BigDecimal(executedVolume));
	}

	public Long getOrderID() {
		return orderID;
	}

	/**
	 * @return the price
	 */
	public Double getPrice() {
		return this.price;
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

	public BigDecimal getAvgPrice() {
		return avgPrice;
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
	
	public OrderValidityTypes getValidityType() {
		return validityType;
	}
	
	public void setValidityType(OrderValidityTypes validityType) {
		this.validityType = validityType;
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
	
	public TradeTypes getTradeType() {
		return tradeType;
	}
	
	/**
	 * @param tradeType the tradeType to set
	 */
	public void setTradeType(TradeTypes tradeType) {
		this.tradeType = tradeType;
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
				+ (this.volume != null ? "volume=" + this.volume + ", " : "")
				+ (this.avgPrice != null ? "averagePrice=" + this.avgPrice + ", " : "")
				+ (this.executedVolume != null ? "executedVolume=" + this.executedVolume + ", " : "")
				+ (this.duration != null ? "duration=" + this.duration + ", " : "")
				+ (this.orderDateTime != null ? "orderDateTime=" + this.orderDateTime + ", " : "")
				+ (this.orderStatus != null ? "orderStatus=" + this.orderStatus + ", " : "")
				+ (this.side != null ? "side=" + this.side + ", " : "")
				+ (this.orderType != null ? "orderType=" + this.orderType + ", " : "")
				+ (this.orderSerial != null ? "orderSerial=" + this.orderSerial + ", " : "")
				+ (this.securityID != null ? "securityID=" + this.securityID + ", " : "")
				+ (this.executionsList != null ? "executionsList="+ this.executionsList.subList(0,Math.min(this.executionsList.size(), maxLen))
						: "") + "]";
	}

	
	
	
	public boolean addExecution(Date executionDateTime, int execVolume, double execPrice) throws OrderException{
		boolean ret = false;
		
		if(orderStatus == OrderStatus.NEW || orderStatus == OrderStatus.PARTIALLY_FILLED){
			//volume executado eh menor que o volume total e menor que o volume atual
			if(execVolume<=volume && execVolume <= (volume-executedVolume)){
				// cria a execucao
				OrderExecution oex = new OrderExecution(ExecutionTypes.TRADE, executionDateTime, execVolume, execPrice, this);
				oex.setMessage("Execution of " + execVolume + " at price " + execPrice);
								
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
	
	
	public boolean changePrice(double price) throws OrderException {
		boolean ret = false;
		if(orderStatus == OrderStatus.NEW){
			this.price = price;
			ret = true;
		}else{
			ret = false;
			throw new OrderException("Invalid Order status: " + orderStatus);
		}
		return ret;
	}
		
	public boolean changeVolume(int volume) throws OrderException {
		boolean ret = false;
		if(orderStatus == OrderStatus.NEW){
			
			// verifica se o volume esta de acordo com o lote padrao do simbolo,
			// caso n esteja, lanca exception
			if ((volume % securityID.getSecurityInfoID().getMinimalVolume()) != 0) {
				throw new OrderException("Invalid Volume:" + volume);
			}
			
			this.volume = volume;
			ret = true;
		}else{
			ret = false;
			throw new OrderException("Invalid Order status: " + orderStatus);
		}
		return ret;
	}
	
	
	/*
	private void putPrices(OrdersPrices prices) throws OrderException {

		switch (orderType) {
		case Limit:
			if (prices.price <= 0) {
				throw new OrderException(String.format(
						"Invalid Prices to Limit Orders(price): %f",
						prices.price));
			} else {				
				price = prices.price;
			}

			break;
		case Market:

			break;
		case Stop:
			if (prices.stop <= 0) {
				throw new OrderException(String.format(
						"Invalid Prices to Stop Orders(stop): %f, %f", prices.stop));
			} else {
				stopPrice = prices.stop;
			}
			break;
		case StopLimit:
			if (prices.limit <= 0 || prices.stop <= 0 || prices.gain <= 0) {
				throw new OrderException(
						String.format(
								"Invalid Prices to StopGain Orders(limit, stop, gain): %f, %f, %f",
								prices.limit, prices.stop, prices.gain));
			} else {
				price = prices.limit;
				stopPrice = prices.stop;
			}
			break;
		case StopLimit:
			if (prices.price <= 0 || prices.limit <= 0 || prices.stop <= 0) {
				throw new OrderException(
						String.format(
								"Invalid Prices to StopLimit Orders(price, limit, stop): %f, %f, %f",
								prices.price, prices.limit, prices.stop));
			} else {
				price = prices.price;
				stopPrice = prices.stop;
				
				if(side==Side.BUY){
					if(stopPrice<price){
						throw new OrderException("Stop Price must be greater than Limit Price.");
					}
				}
				else if(side == Side.SELL){
					if(stopPrice>price){
						throw new OrderException("Limit Price must be greater than Stop Price.");
					}
				}
				
			}

		default:
			throw new OrderException("Order type not allowed:" + orderType);
		}

	}*/

}
