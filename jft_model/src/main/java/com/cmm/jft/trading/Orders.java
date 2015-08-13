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
import javax.persistence.criteria.CriteriaBuilder.Case;

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

	@Column(name="ClOrdID", length=25, updatable=false, nullable=false)
	private String clOrdID;

	@Column(name="OrigClOrdID", length=25, updatable=false, nullable=false)
	private String origClOrdID;


	@JoinColumn(name = "securityID", referencedColumnName = "securityID", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Security securityID;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderID")
	private List<OrderExecution> executionsList;



	public Orders() {
		this.price =  0d;
		this.duration = new Date();
		init();
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
	}


	private void init(){
		this.orderDateTime = new Date();
		this.orderStatus = OrderStatus.CREATED;
		this.orderSerial = UUID.randomUUID().toString();
		this.executionsList = new ArrayList<OrderExecution>();
	}



	private void refreshOrder() throws OrderException {

		try {
			// calculate the order avg price
			calculateAveragePrice();
			
			//adjust the state in according to actual order state
			if(orderStatus == OrderStatus.NEW || orderStatus == OrderStatus.PARTIALLY_FILLED || orderStatus == OrderStatus.REPLACED){
				
				int cv = volume - executedVolume;
	
				// order have not yet been executed
				if (cv > 0 && cv != volume) {
					setOrderStatus(OrderStatus.PARTIALLY_FILLED);
				} else if (cv == 0) {//order was totally executed
					setOrderStatus(OrderStatus.FILLED);
				} else if (cv < 0) {// error!!!!!
					throw new OrderException("ExecutedVolume is greater than order volume: " + executedVolume);
				}
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
			if(oe.getExecutionType() == ExecutionTypes.TRADE) {			
				sumVolume += oe.getVolume();
				sumTotal += oe.getPrice() * oe.getVolume();
			}
		}

		// adjusts the execution control values
		executedVolume = sumVolume;
		
		//only TRADE events are relevant
		long execs = executionsList.stream().filter(e -> e.getExecutionType() == ExecutionTypes.TRADE).count();
		execs = execs > 0 ? execs : 1;
		avgPrice = new BigDecimal(sumTotal/execs);

		return avgPrice;
	}	

	public void setOrderStatus(OrderStatus status) throws OrderException {
		
		boolean invalidState = true;
		switch(this.orderStatus) {
		case CREATED:
			if(status == OrderStatus.SUSPENDED || 
			status == OrderStatus.REJECTED) {
				this.orderStatus = status;
				invalidState = false;
			}
			break;

		case SUSPENDED:
			if(status == OrderStatus.NEW || status == OrderStatus.REJECTED) {
				this.orderStatus = status;
				invalidState = false;
			}			

		case NEW:
			if(status == OrderStatus.REPLACED || 
			status == OrderStatus.EXPIRED || 
			status == OrderStatus.FILLED || 
			status == OrderStatus.CANCELED) {
				this.orderStatus = status;
				invalidState = false;
			}
			break;

		case REPLACED:
			if(status == OrderStatus.EXPIRED || 
			status == OrderStatus.FILLED ||
			status == OrderStatus.PARTIALLY_FILLED ||
			status == OrderStatus.CANCELED) {
				this.orderStatus = status;
				invalidState = false;
			}
			break;

		case EXPIRED:
			this.orderStatus = OrderStatus.EXPIRED;
			invalidState = false;
			break;

		case CANCELED:
			this.orderStatus = OrderStatus.CANCELED;
			invalidState = false;
			break;

		case FILLED:
			this.orderStatus = OrderStatus.FILLED;
			invalidState = false;
			break;

		case PARTIALLY_FILLED:
			if(status == OrderStatus.EXPIRED || 
			status == OrderStatus.FILLED ||
			status == OrderStatus.PARTIALLY_FILLED ||
			status == OrderStatus.CANCELED) {
				this.orderStatus = status;
				invalidState = false;
			}
			break;

		default:
			throw new OrderException("Invalid OrderStatus: " + status);

		}
		
		if(invalidState) {
			throw new OrderException("Can't set status from: " + orderStatus + " to: " + status);
		}

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

	public OrderValidityTypes getValidityType() {
		return validityType;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public Side getSide() {
		return side;
	}

	public Security getSecurityID() {
		return securityID;
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
	
	public void setTradeType(TradeTypes tradeType) {
		this.tradeType = tradeType;
	}

	public String getClOrdID() {
		return clOrdID;
	}
	
	public void setClOrdID(String clOrdID) {
		this.clOrdID = clOrdID;
	}
	
	public void setOrigClOrdID(String origClOrdID) {
		this.origClOrdID = origClOrdID;
	}

	public String getOrigClOrdID() {
		return origClOrdID;
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
		+ (this.executionsList != null ? "executionsList="+ this.executionsList.subList(0,Math.min(this.executionsList.size(), maxLen))	: "") + "]";
	}
	
	/**
	 * 
	 * @param execution
	 * @return
	 * @throws OrderException
	 */
	public boolean addExecution(OrderExecution execution) throws OrderException {
		
		boolean added = false;
		
		switch(execution.getExecutionType()) {
		case CANCELED:
			cancelOrder(execution);
			break;
		case EXPIRED:
			expireOrder(execution);
			break;
		case NEW:
			newOrder(execution);
			break;
		case REJECTED:
			rejectOrder(execution);
			break;
		case REPLACE:
			replaceOrder(execution);
			break;
		case RESTATED:
			break;
		case SUSPENDED:
			break;
		case TRADE:
			tradeOrder(execution);
			break;
		case TRADE_CANCEL:
			cancelTrade(execution);
			break;
		}		
		
		return added;
	}
	
	private void cancelOrder(OrderExecution execution) throws OrderException {
		
		setOrderStatus(OrderStatus.CANCELED);
		refreshOrder();
		
	}
	
	private void expireOrder(OrderExecution execution) throws OrderException {
		
		setOrderStatus(OrderStatus.EXPIRED);
		refreshOrder();
		
	}
	
	private void newOrder(OrderExecution execution) throws OrderException {
		
		setOrderStatus(OrderStatus.NEW);
		refreshOrder();
		
	}
	
	private void rejectOrder(OrderExecution execution) throws OrderException {
		
		setOrderStatus(OrderStatus.REJECTED);
		refreshOrder();
		
	}
	
	private void replaceOrder(OrderExecution execution) throws OrderException {
		
		
		setOrderStatus(OrderStatus.SUSPENDED);
		
		
		
		setOrderStatus(OrderStatus.REPLACED);
		refreshOrder();
		
	}
	
	private void tradeOrder(OrderExecution execution) throws OrderException {
		
		if(orderStatus == OrderStatus.NEW || orderStatus == OrderStatus.PARTIALLY_FILLED || orderStatus == OrderStatus.REPLACED){
			//volume executado eh menor que o volume total e menor que o volume atual
			if(execution.getVolume() <= volume && execution.getVolume() <= (volume-executedVolume)){
				executionsList.add(execution);
				//ajusta o estado da ordem
				refreshOrder();
			} else {
				throw new OrderException("Invalid volume: " + execution.getVolume());
			}			
		} else {
			throw new OrderException("");
		}
		
		
	}
	
	private void cancelTrade(OrderExecution execution) throws OrderException {
		setOrderStatus(OrderStatus.NEW);
		refreshOrder();
	}
	
	
	
	
	private boolean addExecution(Date executionDateTime, int execVolume, double execPrice) throws OrderException{
		boolean ret = false;

		if(orderStatus == OrderStatus.NEW || orderStatus == OrderStatus.PARTIALLY_FILLED){
			//volume executado eh menor que o volume total e menor que o volume atual
			if(execVolume<=volume && execVolume <= (volume-executedVolume)){
				// cria a execucao
				OrderExecution oex = new OrderExecution(ExecutionTypes.TRADE, executionDateTime, execVolume, execPrice);
				oex.setMessage("Execution of " + execVolume + " at price " + execPrice);
				oex.setLeavesVolume(volume - oex.getVolume() );
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
		try {
			setOrderStatus(OrderStatus.REPLACED);
			this.price = price;
			ret = true;
		}catch(OrderException e) {
			throw e;
		}

		return ret;
	}

	public boolean changeVolume(int volume) throws OrderException {
		boolean ret = false;
		try {
			// verifica se o volume esta de acordo com o lote padrao do simbolo,
			// caso n esteja, lanca exception
			if ((volume % securityID.getSecurityInfoID().getMinimalVolume()) != 0) {
				throw new OrderException("Invalid Volume:" + volume);
			}

			this.volume = volume;
			ret = true;
		}catch(OrderException e) {
			throw e;
		}

		return ret;
	}

}
