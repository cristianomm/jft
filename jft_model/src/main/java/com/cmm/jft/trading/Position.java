
package com.cmm.jft.trading;

import com.cmm.jft.db.DBObject;
import com.cmm.jft.financial.Brokerage;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.Side;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;


/**
 * 
 * <p>
 * <code>Position</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 6, 2013 2:00:40 AM
 */
public class Position implements DBObject<Position> {

    private String positionSerial;

    private String symbol;

    private Date openDate;

    private Date closeDate;

    private double profit;

    private boolean open;

    private List<Orders> ordersList;


    /**
     * @param symbol
     */
    public Position(String symbol) {
	this.open = false;
	this.symbol = symbol;
	this.openDate = new Date();
	this.positionSerial = UUID.randomUUID().toString();
	this.ordersList = new ArrayList<Orders>();
    }

    public void refreshTrade() {

	// calcula o lucro
	getProfit();

    }

    public double getProfit() {
	double buy = 0;
	double sell = 0;

	Iterator<Orders> itOrd = ordersList.iterator();
	while (itOrd.hasNext()) {
	    Orders o = itOrd.next();
	    if (o.getOrderStatus() == OrderStatus.FILLED) {
		if (o.getSide() == Side.BUY) {
		    buy = buy + o.getExecutedOrderValue();
		} else {
		    sell = sell + o.getExecutedOrderValue();
		}
		profit = sell - buy;
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
    public double getTradeValue() {
	double val = 0;

	Iterator<Orders> itOrd = ordersList.iterator();
	while (itOrd.hasNext()) {
	    Orders o = itOrd.next();
	    if (o.getOrderStatus() == OrderStatus.FILLED) {
		if (o.getSide() == Side.SELL) {
		    val = val + o.getExecutedOrderValue();
		}
	    }
	}
	return val;
    }

    /**
     * @return the symbol
     */
    public String getSymbol() {
	return this.symbol;
    }

    public void addOrder(Orders orders) {
	if (symbol.isEmpty()) {
	    symbol = orders.getSecurityID().getSymbol();
	}
	open = true;
	ordersList.add(orders);
    }

    public Orders getOrder(String orderSerial) {
	for (Orders o : ordersList) {
	    if (o.getClOrdID().equalsIgnoreCase(orderSerial)) {
		return o;
	    }
	}
	return null;
    }


    /**
     * Calcula a posicao atual do volume executado para as ordens ativas
     * @return Posicao atual do volume executado
     */
    public int getPosition() {
	int buy = 0, sell = 0;
	for (Orders o : ordersList) {
	    if (o.getOrderStatus() == OrderStatus.NEW || o.getOrderStatus() == OrderStatus.FILLED || o.getOrderStatus() == OrderStatus.PARTIALLY_FILLED) {
		buy += o.getSide() == Side.BUY ? o.getExecutedVolume() : 0;
		sell += o.getSide() == Side.SELL ? o.getExecutedVolume() : 0;
	    }
	}

	return buy - sell;
    }


    //	/**
    //	 * Calcula a posicao atual das ordens que ainda nao foram totalmente executadas
    //	 * @return
    //	 */
    //	public int getOpenPosition() {
    //		int buy = 0, sell = 0;
    //		for (Orders o : ordersList) {
    //			if (o.getOrderStatus() == OrderStatus.NEW || o.getOrderStatus() == OrderStatus.PARTIALLY_FILLED) {
    //				buy += o.getSide() == Side.BUY ? o.getExecutedVolume() : 0;
    //				sell += o.getSide() == Side.SELL ? o.getExecutedVolume() : 0;
    //			}
    //		}
    //
    //		return buy - sell;
    //	}

    public Brokerage getBrokerage() {

	Brokerage brokerage = null;
	//		Orders ordr = ordersList.iterator().next();
	//		for (Brokerage brkg : brokerID.getBrokerageList()) {
	//			if (tradeType == brkg.getTradeType()) {
	//				if (ordr.getSecurityID().getSecurityInfoID().getCategory() == brkg.getCategory()) {
	//					brokerage = brkg;
	//					break;
	//				}
	//			}
	//		}

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

    public boolean isOpen() {
	return open;
    }


    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	final int maxLen = 10;
	return "Position [positionSerial=" + this.positionSerial
		+ ", ordersSet="
		+ (this.ordersList != null ? this.toString(this.ordersList, maxLen) : null) 
		+ ", symbol=" + this.symbol
		+ ", openDate=" + this.openDate + ", closeDate="
		+ this.closeDate + "]";
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
