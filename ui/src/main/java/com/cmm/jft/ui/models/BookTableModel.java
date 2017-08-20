/**
 * 
 */
package com.cmm.jft.ui.models;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.Side;

/**
 * <p><code>BookTableModel.java</code></p>
 * @author Cristiano M Martins
 * @version 26 de ago de 2015 17:27:32
 *
 */
public class BookTableModel extends AbstractTableModel {

    private static final int VOLUME = 0;
    private static final int PRICE = 1;
    private static final int TIME = 2;
    private static final int BROKER = 3;

    private Side side;
    private String[] headers;

    private HashMap<String, Orders> orders;
    private HashMap<Integer, String> rowToOrder;
    private HashMap<String, Integer> orderToRow;

    /**
     * 
     */
    public BookTableModel(Side side) {
	this.side = side;
	this.orders = new HashMap<>();
	this.rowToOrder = new HashMap<>();
	this.orderToRow = new HashMap<>();
	this.headers = new String[] {"Volume", "Price", "Time", "Broker"};
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
	if(column >=0 && column < headers.length){
	    return side == Side.BUY ? headers[(headers.length-1) - column] : headers[column];
	}
	return "";
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
	return headers.length;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
	return rowToOrder.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIdx, int colIdx) {
	Object value = null;
	Orders ordr = orders.get(rowToOrder.get(rowIdx));
	int adj = side == Side.BUY ? headers.length-1 : 0;
	switch(colIdx - adj){
	case PRICE:
	    value = Double.toString(ordr.getPrice());
	    break;

	case VOLUME:
	    value = Double.valueOf(ordr.getLeavesVolume());
	    break;

	case BROKER:
	    value = ordr.getBrokerID();
	    break;

	case TIME:
	    value = ordr.getOrderDateTime();
	    break;

	}

	return value;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIdx, int colIdx) {
	return false;
    }



    public void addOrder(Orders ordr){

    }

    public void removeOrder(Orders ordr){

    }

    public void replaceOrder(Orders ordr){
	if(orders.get(ordr.getClOrdID()) != null){
	    int row = orderToRow.get(ordr.getClOrdID());

	    orders.put(ordr.getClOrdID(), ordr);

	    fireTableRowsUpdated(row, row);
	}
    }

    public Orders getOrder(String clOrdId){
	return orders.get(clOrdId);
    }

    public Orders getOrder(int row){
	return orders.get(rowToOrder.get(row));
    }



}
