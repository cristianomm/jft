/**
 * 
 */
package com.cmm.jft.engine.match;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.PriorityBlockingQueue;

import quickfix.Message;
import quickfix.SessionID;
import quickfix.fix44.ExecutionReport;

import com.cmm.jft.engine.SessionRepository;
import com.cmm.jft.engine.enums.MatchTypes;
import com.cmm.jft.engine.message.MessageEncoder;
import com.cmm.jft.engine.message.MessageSender;
import com.cmm.jft.trading.OrderExecution;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.ExecutionTypes;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.exceptions.OrderException;

/**
 * @author Cristiano M Martins
 * @version 17/08/15 11:17:42
 *
 */
public class OrderMatcher  implements MessageSender {

	private double lastPrice;
	private double lastVolume;
	private double protectionLevel;
	private PriorityBlockingQueue<Orders> buyQueue;
	private PriorityBlockingQueue<Orders> sellQueue;
	private PriorityBlockingQueue<Orders> stopQueue;

	
	public OrderMatcher(MatchTypes matchTypes, double protectionLevel) {
		
		this.protectionLevel = protectionLevel;
		if(matchTypes == MatchTypes.FIFO) {
			this.buyQueue = new PriorityBlockingQueue<>(1000, new PriceTimeComparator());
			this.sellQueue = new PriorityBlockingQueue<>(1000, new PriceTimeComparator());
			this.stopQueue = new PriorityBlockingQueue<>(1000, new PriceTimeComparator());
		}
		
	}
	
	
	public double getLastPrice() {
		return lastPrice;
	}
	
	public double getLastVolume() {
		return lastVolume;
	}
	
	/**
	 * @return the buyQueue
	 */
	public PriorityBlockingQueue<Orders> getBuyQueue() {
		return this.buyQueue;
	}
	
	/**
	 * @return the sellQueue
	 */
	public PriorityBlockingQueue<Orders> getSellQueue() {
		return this.sellQueue;
	}
	
	private void match() {
		
		Orders sell = sellQueue.poll();
		Orders buy = buyQueue.poll();
		
	}
	
	
	public boolean addOrder(Orders buyOrder) throws OrderException {
		boolean add = false;
		
		add = execute(buyOrder);
		
		return add;
	}
	
	private boolean addOnBook(Orders ordr) {
		boolean add = false;
		if(ordr.getSide() == Side.BUY) {
			add = buyQueue.offer(ordr);
		}
		else {
			add = sellQueue.offer(ordr);
		}
		
		return add;
	}
	
	
	private boolean execute(Orders ordr) {
		
		boolean executed = false;
		switch(ordr.getOrderType()) {
		case Market:
			executed = executeMarketWithProtection(ordr);
			break;
		case Limit:
			executed = executeLimit(ordr);
			break;
		case Stop:
			executed = addToStopBook(ordr);//nao executa stop mas sim adiciona no "stop book" e aguarda para ser ativada
			break;
		case StopLimit:
			executed = addToStopBook(ordr);//nao executa stoplimit, deve inserir no stop book e aguardar o gatilho
			break;
		case MarketWithLeftOverAsLimit:
			executed = executeMarketToLimit(ordr);
			break;
		}
		
		return executed;
	}
	
	private boolean fillOrders(Orders newOrder, Orders bookOrder, double qtyToFill, double priceToFill){
		boolean send = false;
		
		OrderExecution orderFill = new OrderExecution(ExecutionTypes.TRADE, qtyToFill, priceToFill);
		OrderExecution bookFill = new OrderExecution(ExecutionTypes.TRADE, qtyToFill, priceToFill);
		try {
			if(newOrder.addExecution(orderFill) && bookOrder.addExecution(bookFill)) {
				
				//ajusta os valores para ultima execucao
				this.lastPrice = priceToFill;
				this.lastVolume = qtyToFill;
				
				//recupera a sessao da ordem recebida
				SessionID orderSession = SessionRepository.getInstance().getSession(newOrder.getPartyID());
				send = sendMessage(MessageEncoder.getEncoder(orderSession).executionReport(orderFill), orderSession);
				
				//recupera a sessao da ordem que estava no book
				SessionID bookOrderSession = SessionRepository.getInstance().getSession(bookOrder.getPartyID());
				send = sendMessage(MessageEncoder.getEncoder(bookOrderSession).executionReport(bookFill), bookOrderSession);
			}else {
				throw new OrderException("Error on add executions.");
			}
			
		} catch (OrderException e) {
			e.printStackTrace();
			send = false;
		}
		
		return send;
	}
	
	private PriorityBlockingQueue<Orders> getCounterpartyBookOrders(Side side) {
		
		if(side == Side.BUY) {
			return sellQueue;
		}
		
		return buyQueue;
	}
	
	private void adjustProtectionPrice(Orders ordr) {
		
		/*
		 * For bids, the protection price calculated is by adding an offset to the last trade price. 
		 * For offers, the offset is subtracted from the last trade. The protection price cannot be 
		 * specified in the incoming order.
		 */
		double offset = (lastPrice * protectionLevel) * (ordr.getSide()==Side.BUY? 1:-1);
		ordr.setProtectionPrice(lastPrice + offset);
		
	}
	
	/**
	 * Execute Market type orders received;
	 * @param ordr order to execute;
	 */
	private boolean executeMarketWithProtection(Orders ordr) {
		boolean exec = false;
				
		adjustProtectionPrice(ordr);
		exec = generalExecute(ordr, ordr.getProtectionPrice(), ordr.getVolume());
		
		return exec;
	}
	
	private boolean executeLimit(Orders ordr) {
		boolean exec = false;
		
		exec = generalExecute(ordr, ordr.getPrice(), ordr.getVolume());
		
		return exec;
	}
	
	private boolean addToStopBook(Orders ordr) {
		boolean exec = false;
		
		
		
		return exec;
	}
	
	private boolean executeMarketToLimit(Orders ordr) {
		boolean exec = false;
		
		exec = generalExecute(ordr, ordr.getPrice(), ordr.getVolume());
		
		return exec;
	}
	
	
	private boolean generalExecute(Orders ordr, double orderPrice, double orderVolume) {
		boolean exec = false;
		
		PriorityBlockingQueue<Orders> orders = getCounterpartyBookOrders(ordr.getSide());
		List<OrderExecution> execs = createExecutions(ordr.getSide(), orderPrice, orderVolume);
		try {
			//verifica se pode executar a ordem 
			if(validateExecution(ordr, execs)) {
				
				for(OrderExecution ex:execs) {
					//adiciona as execucoes e as informa para os participantes
					exec = fillOrders(ordr, ex.getOrderID(), ex.getVolume(), ex.getPrice());
					
					//remove the filled order from book
					if(ex.getOrderID().getOrderStatus() != OrderStatus.PARTIALLY_FILLED) {
						orders.remove(ex.getOrderID());
					}
					
				}
				
				//adiciona o restante da ordem recebida no book
				if(ordr.getOrderStatus() == OrderStatus.PARTIALLY_FILLED || ordr.getOrderStatus() == OrderStatus.NEW) {
					
					//ajusta o tipo da ordem para Limit
					ordr.changeToLimit(orderPrice);
					exec = addOnBook(ordr);
				}
				
			}else {
				//cancela a ordem e informa que a ordem nao podera ser executada
				exec = false;
				
				OrderExecution oe = new OrderExecution(ExecutionTypes.CANCELED, ordr.getVolume(), ordr.getPrice());
				oe.setMessage("Order Canceled due to invalid execution.");
				ordr.addExecution(oe);
				
				SessionID bookOrderSession = SessionRepository.getInstance().getSession(ordr.getPartyID());
				sendMessage(MessageEncoder.getEncoder(bookOrderSession).executionReport(oe), bookOrderSession);
			}
			
		}catch(OrderException e) {
			
		}
		
		
		return exec;
	}
	
	/**
	 * Cria as execucoes para os valores passados por parametro, referenciando
	 * na execucao a ordem que esta presente no book e fara a contra-parte na transacao.
	 * @param side
	 * @param price 
	 * @param volume
	 * @return
	 */
	private ArrayList<OrderExecution> createExecutions(Side side, double price, double volume) {
		
		ArrayList<OrderExecution> lst = new ArrayList<OrderExecution>();
		PriorityBlockingQueue<Orders> ordrs = getCounterpartyBookOrders(side);
				
		double cumVolume = 0;
		while(ordrs.iterator().hasNext() && cumVolume < volume) {
			
			Orders bookOrder = ordrs.iterator().next(); 
			
			double qtyToFill = 0;
			double priceToFill = bookOrder.getPrice();
			
			if(volume >= bookOrder.getLeavesVolume()) {
				qtyToFill = bookOrder.getLeavesVolume();
			}
			else {
				qtyToFill = volume;
			}
			
			if(cumVolume < volume && priceToFill <= price) {
				cumVolume += qtyToFill;
				OrderExecution fill = new OrderExecution(ExecutionTypes.TRADE, qtyToFill, priceToFill);
				fill.setOrderID(bookOrder);
				lst.add(fill);
			}
		}
		
		return lst;
	}
	
	private boolean validateExecution(Orders ordr, List<OrderExecution> executions) {
		boolean validExecution = false;
		
		switch(ordr.getValidityType()) {
		case GFA:
			break;
		case DAY:
			break;
		case GTC:
			break;
		case IMMEDIATE_OR_CANCEL:
			break;
		case FILL_OR_KILL:
			break;
		case GTD:
			break;
		case ATC:
			break;
		
		}
		
		
		
		return validExecution;
	}
	
	
	@Override
	public boolean sendMessage(Message message, SessionID sessionID) {
		
		
				
		return false;
	}
	
	// creates the order execution
	//	OrderExecution oex = new OrderExecution(ExecutionTypes.TRADE, executionDateTime, execVolume, execPrice);
	//	oex.setMessage("Execution of " + execution.getVolume() + " at price " + execution.getPrice());
	//	oex.setLeavesVolume(volume - oex.getVolume());
	
}
