/**
 * 
 */
package com.cmm.jft.ui.models;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.vo.TradeVO;

/**
 * <p>
 * <code>TimesTradesTableModel.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 30/03/2017 12:04:49
 *
 */
public class TimesTradesTableModel extends AbstractTableModel {
    
    public static final int ID = 0;
    public static final int TIME = 1;
    public static final int VOLUME = 2;
    public static final int PRICE = 3;
    public static final int BUY = 4;
    public static final int SELL = 5;
    public static final int AGRE = 6;
    
    private String[] headers;
    private ArrayList<TradeVO> trades;
    
    
    public TimesTradesTableModel() {
	this.trades = new ArrayList<>();
	this.headers = new String[]{"#", "Time", "Volume", "Price", "Buy", "Sell", "Agr"};
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    
    
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column) {
	if(column >=0 && column < headers.length){
	    return headers[column];
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
	return trades.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIdx, int colIdx) {
	
	Object value = null;
	TradeVO tvo = trades.get(rowIdx);
	switch(colIdx){
	case ID:
	    value = tvo.getTradeID();
	    break;
	case TIME:
	    value = tvo.getTradeTime();
	    break;
	case VOLUME:
	    value = tvo.getVolume();
	    break;
	case PRICE:
	    value = tvo.getPrice();
	    break;
	case BUY:
	    value = tvo.getBuyBroker();
	    break;
	case SELL:
	    value = tvo.getSellBroker();
	    break;    
	case AGRE:
	    value = tvo.getAgressor();
	    break;
	}
	
	return value;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
	Class clss = null;
	switch(columnIndex){
	case ID:
	    clss = String.class;
	    break;
	case TIME:
	    clss = Date.class;
	    break;
	case VOLUME:
	    clss = Integer.class;
	    break;
	case PRICE:
	    clss = Double.class;
	    break;
	case BUY:
	    clss = String.class;
	    break;
	case SELL:
	    clss = String.class;
	    break;    
	case AGRE:
	    clss = String.class;
	    break;
	}
	
	return clss;
    }
    
    
    public void addTrade(TradeVO trade){
	trades.add(trade);
	int lastRow = getRowCount()-1;
	fireTableRowsInserted(lastRow, lastRow);
	
    }
    

}
