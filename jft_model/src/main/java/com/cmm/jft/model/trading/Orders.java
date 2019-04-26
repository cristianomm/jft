/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.cmm.jft.model.trading;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.apache.log4j.Level;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.trading.enums.ExecutionTypes;
import com.cmm.jft.model.trading.enums.OrderStatus;
import com.cmm.jft.model.trading.enums.OrderTypes;
import com.cmm.jft.model.trading.enums.OrderValidityTypes;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.model.trading.enums.TradeTypes;
import com.cmm.jft.model.trading.enums.WorkingIndicator;
import com.cmm.jft.model.trading.exceptions.OrderException;
import com.cmm.logging.Logging;

/**
 *
 * <p>
 * <code>Orders</code>
 * </p>
 * Any Order is identified by three identifiers when it is created:
 * <code>brokerId</code> Entering Firm: <code>traderId</code> Entering Trader:
 * <code>senderLocation</code> this fields are required and are
 * used to identify market participants.
 * 
 * After that, orders can be identified as a group of orders related to an
 * trader. This fields are used for grouping:
 * 
 * <code>clOrdId</code> <code>origClOrdId</code>
 * 
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
@Entity
@Table(name = "Orders", schema = "Trading"/*
											 * , uniqueConstraints = { @UniqueConstraint(columnNames = { "OrderSerial"
											 * }) }
											 */)
@NamedQueries({ @NamedQuery(name = "Orders.findAll", query = "SELECT o FROM Orders o"),
		@NamedQuery(name = "Orders.findByOrderId", query = "SELECT o FROM Orders o WHERE o.orderId = :orderId"), })
public class Orders implements Serializable/*DBObject<Orders>*/ {
	private static final long serialVersionUId = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "orderId", nullable = false)
	private Long orderId;

	@Column(name = "secOrderId", nullable = false)
	private Long secOrderId;

	/**
	 * Unique identifier of the order as assigned by the market participant
	 */
	@Column(name = "ClOrdId", length = 50, updatable = false, nullable = false)
	private String clOrdId;

	/**
	 * Contains the ClOrdId of the replacement order. Conditionally required when
	 * ExecType = 5 (Replace).
	 */
	@Column(name = "OrigClOrdId", length = 50, updatable = false)
	private String origClOrdId;

	/**
	 * Identification of trader
	 */
	@Column(name = "traderId", updatable = false, length = 50)
	private String traderId;

	/**
	 * Broker's identification
	 */
	@Column(name = "brokerId", length = 5, updatable = false)
	private String brokerId;

	@JoinColumn(name = "securityId", referencedColumnName = "securityId", nullable = false)
	@ManyToOne(optional = false, fetch = FetchType.LAZY)
	private Security securityId;

	@Column(name = "senderLocation", length = 50, updatable = false)
	private String senderLocation;

	@Column(name = "Account", length = 10, updatable = false)
	private String account;

	@Column(name = "Price", precision = 19, scale = 8)
	private double price;

	@Column(name = "StopPrice", precision = 19, scale = 8)
	private double stopPrice;

	@Basic(optional = false)
	@Column(name = "Volume", nullable = false)
	private double volume;

	@Column(name = "ExecutedVolume")
	private Integer executedVolume;

	@Column(name = "LeavesVolume")
	private double leavesVolume;

	@Column(name = "MaxFloor")
	private double maxFloor;

	@Column(name = "MinVolume")
	private double minVolume;

	// @Max(value=?) @Min(value=?)//if you know range of your decimal fields
	// consider using these annotations to enforce field validation
	@Column(name = "AvgPrice", precision = 19, scale = 8)
	private double avgPrice;

	@Column(name = "ProtectionPrice", precision = 19, scale = 8)
	private double protectionPrice;

	@Column(name = "Duration", nullable = false, columnDefinition = "DATE")
	private LocalDateTime duration;

	@Column(name = "OrderDateTime", nullable = false, columnDefinition = "DATE")
	private LocalDateTime orderDateTime;

	@Column(name = "InsertDateTime", columnDefinition = "TIMESTAMP")
	private LocalDateTime insertDateTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "OrderStatus", nullable = false)
	private OrderStatus orderStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "WorkingIndicator", nullable = false)
	private WorkingIndicator workingIndicator;

	@Enumerated(EnumType.STRING)
	@Column(name = "OrderValidityType", nullable = false)
	private OrderValidityTypes validityType;

	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "OrderType", nullable = false)
	private OrderTypes orderType;

	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "TradeType", nullable = false, updatable = false)
	private TradeTypes tradeType;

	@Enumerated(EnumType.STRING)
	@Basic(optional = false)
	@Column(name = "Side", nullable = false, updatable = false)
	private Side side;

	@Column(name = "Comment", length = 250)
	private String comment;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "orderId")
	private List<OrderEvent> eventsList;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JoinColumn(name="positionId", referencedColumnName="positionId")
	private Position positionId;

	public Orders() {
		this.price = 0d;
		this.duration = LocalDateTime.now();
		init();
	}

	/**
	 * 
	 * @param orderId
	 * @param clOrdId
	 * @param security
	 * @param side
	 * @param price
	 * @param volume
	 * @param orderType
	 * @param traderId
	 * @param brokerId
	 * @param senderLct
	 * @throws OrderException
	 */
	public Orders(long orderId, String clOrdId, Security security, Side side, double price, int volume,
			OrderTypes orderType, String traderId, String brokerId, String senderLct) throws OrderException {

		if (clOrdId == null || security == null || side == null || volume <= 0 || traderId == null || brokerId == null
				|| senderLct == null) {
			throw new OrderException(String.format(
					"Invalid field value: clOrdId: %1$s, security: %2$s, side: %3$s, "
							+ "volume: %7$d, traderId: %4$s, brokerId: %5$s, senderLct: %6$s",
					clOrdId, security, side, traderId, brokerId, senderLct, volume));
		}

		this.orderId = orderId;
		this.secOrderId = orderId;
		this.clOrdId = clOrdId;

		this.traderId = traderId;
		this.brokerId = brokerId;
		this.senderLocation = senderLct;

		switch (orderType) {
		case Limit:
			this.price = price;
			break;
		case Market:
			break;
		case Stop:
		case StopLimit:
			if (price <= 0) {
				throw new OrderException(String.format("Invalid price for type: %1$f %2$s", price, orderType));
			}
			this.stopPrice = price;
		}

		this.securityId = security;
		this.volume = volume;
		this.leavesVolume = volume;
		this.orderType = orderType;
		this.tradeType = tradeType;
		this.side = side;
		init();
	}

	private void init() {

		this.avgPrice = 0;
		this.executedVolume = 0;

		this.comment = "";
		this.validityType = OrderValidityTypes.DAY;
		this.workingIndicator = WorkingIndicator.No_Working;
		this.orderDateTime = LocalDateTime.now();
		this.orderStatus = OrderStatus.CREATED;
		this.eventsList = new ArrayList<OrderEvent>();
	}

	private void refreshOrder() throws OrderException {

		try {
			// calculate the order avg price and adjusts the last executed price
			calculateOrderValues();

			// adjust the state in according to actual order state
			if (orderStatus == OrderStatus.NEW || orderStatus == OrderStatus.PARTIALLY_FILLED
					|| orderStatus == OrderStatus.REPLACED) {
				// order have not yet been executed
				if (leavesVolume > 0 && leavesVolume < volume) {
					setOrderStatus(OrderStatus.PARTIALLY_FILLED);
				} else if (leavesVolume == 0) {// order was totally executed
					setOrderStatus(OrderStatus.FILLED);
				} else if (leavesVolume < 0) {// error!!!!!
					throw new OrderException("ExecutedVolume is greater than order volume: " + executedVolume);
				}
			}

		} catch (Exception e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
			throw new OrderException(e);
		}

	}

	public double calculateOrderValues() {
		int sumVolume = 0;
		double sumTotal = 0d;
		for (OrderEvent oe : eventsList) {
			if (oe.getExecutionType() == ExecutionTypes.TRADE) {
				sumVolume += oe.getVolume();
				sumTotal += oe.getPrice() * oe.getVolume();
			}
		}

		// adjusts the execution control values
		executedVolume = sumVolume;
		leavesVolume = volume - executedVolume;

		// only TRADE events are relevant
		long execs = eventsList.stream().filter(e -> e.getExecutionType() == ExecutionTypes.TRADE).count();
		execs = execs > 0 ? execs : 1;
		avgPrice = (sumTotal / execs);

		return avgPrice;
	}

	public void setOrderStatus(OrderStatus status) throws OrderException {

		boolean invalidState = true;
		switch (this.orderStatus) {
		case CREATED:
			if (status == OrderStatus.SUSPENDED || status == OrderStatus.REJECTED) {
				this.orderStatus = status;
				invalidState = false;
			}
			break;

		case SUSPENDED:
			if (status == OrderStatus.NEW || status == OrderStatus.REJECTED) {
				this.orderStatus = status;
				invalidState = false;
			}
			break;
		case NEW:
			if (status == OrderStatus.REPLACED || status == OrderStatus.EXPIRED || status == OrderStatus.FILLED
					|| status == OrderStatus.PARTIALLY_FILLED || status == OrderStatus.CANCELED) {
				this.orderStatus = status;
				invalidState = false;
			}
			break;

		case REPLACED:
			if (status == OrderStatus.EXPIRED || status == OrderStatus.FILLED || status == OrderStatus.PARTIALLY_FILLED
					|| status == OrderStatus.CANCELED) {
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
			if (status == OrderStatus.EXPIRED || status == OrderStatus.FILLED || status == OrderStatus.PARTIALLY_FILLED
					|| status == OrderStatus.CANCELED) {
				this.orderStatus = status;
				invalidState = false;
			}
			break;

		default:
			throw new OrderException("Invalid OrderStatus: " + status);
		}

		if (invalidState) {
			throw new OrderException("Can't set status from: " + orderStatus + " to: " + status);
		}

	}

	/**
	 * @return the orderId
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * Adjusts orderId only if orderId has not been adjusted yet.
	 * 
	 * @param orderId the orderId to set
	 */
	public void setOrderId(Long orderId) {
		if (this.orderId <= 0 && orderId > 0) {
			this.orderId = orderId;
		}
	}

	/**
	 * @return the secOrderId
	 */
	public Long getSecOrderId() {
		return secOrderId;
	}

	/**
	 * @param secOrderId the secOrderId to set
	 */
	public void setSecOrderId(Long secOrderId) {
		this.secOrderId = secOrderId;
	}

	/**
	 * @return the traderId
	 */
	public String getTraderId() {
		return this.traderId;
	}

	/**
	 * @param traderId the traderId to set
	 */
	public void setTraderId(String traderId) {
		this.traderId = traderId;
	}

	/**
	 * @return the clOrdId
	 */
	public String getClOrdId() {
		return clOrdId;
	}

	/**
	 * @param clOrdId the clOrdId to set
	 */
	public void setClOrdId(String clOrdId) {
		this.clOrdId = clOrdId;
	}

	public void setOrigClOrdId(String origClOrdId) {
		this.origClOrdId = origClOrdId;
	}

	public String getOrigClOrdId() {
		return origClOrdId;
	}

	/**
	 * @return the brokerId
	 */
	public String getBrokerId() {
		return this.brokerId;
	}

	/**
	 * @param brokerId the brokerId to set
	 */
	public void setBrokerId(String brokerId) {
		this.brokerId = brokerId;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	public double getOrderValue() {
		return price * volume;
	}

	public double getExecutedOrderValue() {
		return avgPrice * executedVolume;
	}

	/**
	 * @return the price
	 */
	public double getPrice() {
		return this.price;
	}

	public double getLeavesVolume() {
		return leavesVolume;
	}

	public double getVolume() {
		return volume;
	}

	/**
	 * @return the minVolume
	 */
	public double getMinVolume() {
		return minVolume;
	}

	/**
	 * @param minVolume the minVolume to set
	 */
	public void setMinVolume(double minVolume) {
		this.minVolume = minVolume;
	}

	public double getAvgPrice() {
		return avgPrice;
	}

	public double getStopPrice() {
		return stopPrice;
	}

	public void setStopPrice(double stopPrice) {
		this.stopPrice = stopPrice;
	}

	public double getProtectionPrice() {
		return protectionPrice;
	}

	public void setProtectionPrice(double protectionPrice) {
		this.protectionPrice = protectionPrice;
	}

	public double getMaxFloor() {
		return this.maxFloor;
	}

	public void setMaxFloor(double maxFloor) {
		this.maxFloor = maxFloor;
	}

	/**
	 * @return the orderType
	 */
	public OrderTypes getOrderType() {
		return this.orderType;
	}

	public Integer getExecutedVolume() {
		return executedVolume;
	}

	public LocalDateTime getDuration() {
		return duration;
	}

	public void setDuration(LocalDateTime duration) {
		this.duration = duration;
	}

	/**
	 * @return the orderDateTime
	 */
	public LocalDateTime getOrderDateTime() {
		return this.orderDateTime;
	}

	public void setOrderDateTime(LocalDateTime orderDateTime) {
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

	public WorkingIndicator getWorkingIndicator() {
		return workingIndicator;
	}

	public void setWorkingIndicator(WorkingIndicator workingIndicator) {
		this.workingIndicator = workingIndicator;
	}

	public Side getSide() {
		return side;
	}

	public Security getSecurityId() {
		return securityId;
	}

	/**
	 * @return the eventsList
	 */
	public List<OrderEvent> getEventsList() {
		return this.eventsList;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public TradeTypes getTradeType() {
		return tradeType;
	}

	public void setTradeType(TradeTypes tradeType) {
		this.tradeType = tradeType;
	}

	/**
	 * @return the senderLocation
	 */
	public String getSenderLocation() {
		return this.senderLocation;
	}

	/**
	 * @param senderLocation the senderLocation to set
	 */
	public void setSenderLocation(String senderLocation) {
		this.senderLocation = senderLocation;
	}

	/**
	 * @param orderType the orderType to set
	 */
	public void setOrderType(OrderTypes orderType) {
		this.orderType = orderType;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
	}

	/**
	 * @param securityId the securityId to set
	 */
	public void setSecurityId(Security securityId) {
		this.securityId = securityId;
	}

	/**
	 * @param side the side to set
	 */
	public void setSide(Side side) {
		this.side = side;
	}

	/**
	 * @param volume the volume to set
	 */
	public void setVolume(double volume) {
		this.volume = volume;
	}

	/**
	 * @return the insertDateTime
	 */
	public LocalDateTime getInsertDateTime() {
		return insertDateTime;
	}

	/**
	 * @param insertDateTime the insertDateTime to set
	 */
	public void setInsertDateTime(LocalDateTime insertDateTime) {
		this.insertDateTime = insertDateTime;
	}
		
	/**
	 * @return the positionId
	 */
	public Position getPositionId() {
		return positionId;
	}

	/**
	 * @param positionId the positionId to set
	 */
	public void setPositionId(Position positionId) {
		this.positionId = positionId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int hash = (int) (47 * (price + volume + orderId + orderDateTime.hashCode()));
		return hash;
	}

	@Override
	public boolean equals(Object otherOrder) {
		boolean eq = false;
		if (otherOrder instanceof Orders) {
			eq = this.clOrdId == ((Orders) otherOrder).clOrdId;
		}

		return eq;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Orders [" + (orderId != null ? "orderId=" + orderId + ", " : "")
				+ (secOrderId != null ? "secOrderId=" + secOrderId + ", " : "")
				+ (clOrdId != null ? "clOrdId=" + clOrdId + ", " : "")
				+ (origClOrdId != null ? "origClOrdId=" + origClOrdId + ", " : "")
				+ (traderId != null ? "traderId=" + traderId + ", " : "")
				+ (brokerId != null ? "brokerId=" + brokerId + ", " : "") + (side != null ? "side=" + side + ", " : "")
				+ (account != null ? "account=" + account + ", " : "") + "price=" + price + ", stopPrice=" + stopPrice
				+ ", volume=" + volume + ", "
				+ (executedVolume != null ? "executedVolume=" + executedVolume + ", " : "") + "leavesVolume="
				+ leavesVolume + ", maxFloor=" + maxFloor + ", minVolume=" + minVolume + ", "
				+ (orderDateTime != null ? "orderDateTime=" + orderDateTime + ", " : "")
				+ (insertDateTime != null ? "insertDatetime=" + insertDateTime + ", " : "")
				+ (orderStatus != null ? "orderStatus=" + orderStatus : "") + "]";
	}

	/**
	 * 
	 * @param execution
	 * @return
	 * @throws OrderException
	 */
	public boolean addExecution(OrderEvent execution) throws OrderException {

		boolean added = true;

		try {
			switch (execution.getExecutionType()) {
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
				restateOrder(execution);
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
			eventsList.add(execution);
		} catch (OrderException e) {
			added = false;
			throw e;
		}

		return added;
	}

	private void cancelOrder(OrderEvent execution) throws OrderException {

		setOrderStatus(OrderStatus.CANCELED);
		workingIndicator = WorkingIndicator.No_Working;
		refreshOrder();
	}

	private void expireOrder(OrderEvent execution) throws OrderException {

		setOrderStatus(OrderStatus.EXPIRED);
		workingIndicator = WorkingIndicator.No_Working;
		refreshOrder();
	}

	private void newOrder(OrderEvent execution) throws OrderException {

		setOrderStatus(OrderStatus.NEW);
		refreshOrder();
	}

	private void rejectOrder(OrderEvent execution) throws OrderException {

		setOrderStatus(OrderStatus.REJECTED);
		workingIndicator = WorkingIndicator.No_Working;
		refreshOrder();
	}

	private void replaceOrder(OrderEvent replace) throws OrderException {

		setOrderStatus(OrderStatus.REPLACED);

		if (replace.getOrderType() != null) {
			this.orderType = replace.getOrderType();
		}

		if (replace.getValidity() != null) {
			this.validityType = replace.getValidity();
		}

		if (replace.getPrice() > 0 && replace.getPrice() != price) {
			this.price = replace.getPrice();
			this.orderDateTime = replace.getEventDateTime();
		}

		if (replace.getStopPrice() > 0) {
			this.stopPrice = replace.getStopPrice();
			this.orderDateTime = replace.getEventDateTime();
		}

		if (replace.getVolume() > 0 && replace.getVolume() != volume) {
			// verifica se o volume esta de acordo com o lote padrao do simbolo,
			// caso n esteja, lanca exception
			if (volume < securityId.getSecurityInfoId().getMinVolume()
					|| volume > securityId.getSecurityInfoId().getMaxVolume()) {
				throw new OrderException("Invalid Volume:" + volume);
			} else {
				if (replace.getVolume() > this.volume) {
					this.orderDateTime = replace.getEventDateTime();
				}
				this.volume = replace.getVolume();
			}
		}

		if (replace.getMinQty() > 0 || (replace.getMinQty() != minVolume && minVolume > 0)) {
			this.minVolume = replace.getMinQty();
			this.orderDateTime = replace.getEventDateTime();
		}

		refreshOrder();
	}

	private void restateOrder(OrderEvent restate) {
		this.secOrderId++;
		this.orderDateTime = restate.getExecutionDateTime();
	}

	private void tradeOrder(OrderEvent execution) throws OrderException {

		if (orderStatus == OrderStatus.NEW || orderStatus == OrderStatus.PARTIALLY_FILLED
				|| orderStatus == OrderStatus.REPLACED) {
			// volume executado eh menor que o volume total e menor que o volume atual
			if (execution.getVolume() <= volume && execution.getVolume() <= (volume - executedVolume)) {
				// ajusta o estado da ordem
				refreshOrder();
			} else {
				throw new OrderException("Invalid volume: " + execution.getVolume());
			}
		} else {
			throw new OrderException("");
		}

	}

	private void cancelTrade(OrderEvent execution) throws OrderException {
		setOrderStatus(OrderStatus.CANCELED);
		refreshOrder();
	}

	public void changeToMarket() throws OrderException {
		setOrderType(OrderTypes.Market);
		setPrice(0);
	}

}
