/**
 * 
 */
package com.cmm.jft.ui.models;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.cmm.jft.services.marketdata.Market;

/**
 * <p>
 * <code>MarketResumeTableModel.java</code>
 * </p>
 *
 * @author cristiano
 * @version 02/04/2017 03:35:05
 *
 */
public class MarketResumeTableModel extends AbstractTableModel {

    public static final int SECURITY = 0;
    public static final int LAST = 1;
    public static final int VARIATION = 2;
    public static final int BUY = 3;
    public static final int SELL = 4;
    public static final int MIN = 5;
    public static final int MAX = 6;
    public static final int MEAN = 7;
    public static final int OPEN = 8;
    public static final int CLOSE = 9;
    public static final int VOLUME = 10;
    public static final int TRADES = 11;
    public static final int VWAP = 12;

    private String[] headers;
    private ArrayList<Market> markets;

    public MarketResumeTableModel() {
	this.markets = new ArrayList<>();
	this.headers = new String[]{
		"", "Último", "Variação", "Compra", "Venda", "Min", "Max", 
		"Médio", "Abertura", "Fechamento", "Volume", "Negócios", "VWAP"};
	markets.add(null);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
	return columnIndex == 0? true : false;
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
	return markets.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIdx, int colIdx) {

	Object value = null;
	Market mkt = markets.get(rowIdx);
	if(mkt != null) {
	    switch(colIdx){
	    case SECURITY:
		value = mkt.getSecurity().getSymbol();
		break;
	    case LAST:
		value = mkt.getLastPrice();
		break;
	    case VARIATION:

		break;
	    case BUY:
		value = mkt.getBidMBP().get(0).getMdEntryPx();
		break;
	    case SELL:
		value = mkt.getAskMBP().get(0).getMdEntryPx();
		break;
	    case MIN:
		value = mkt.getMinPrice();
		break;
	    case MAX:
		value = mkt.getMaxPrice();
		break;
	    case MEAN:

		break;
	    case OPEN:
		value = mkt.getOpenPrice();
		break;
	    case CLOSE:
		value = mkt.getClosePrice();
		break;
	    case VOLUME:
		value = mkt.getTradeVolume();
		break;
	    case TRADES:
		value = mkt.getTradeCount();
		break;
	    case VWAP:
		value = mkt.getVWAP();
		break;
	    }
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
	case SECURITY:
	    clss = String.class;
	    break;
	case LAST:
	case VARIATION:
	case BUY:
	case SELL:
	case MIN:
	case MAX:
	case MEAN:
	case OPEN:
	case CLOSE:
	case VOLUME:
	case TRADES:
	case VWAP:
	    clss = Double.class;
	    break;
	}

	return clss;
    }


    public void addMarket(Market market){
	markets.add(market);
	int lastRow = getRowCount()-1;
	fireTableRowsInserted(lastRow, lastRow);

    }


}
