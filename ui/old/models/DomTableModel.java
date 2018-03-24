/**
 * 
 */
package com.cmm.jft.ui.models;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.cmm.jft.marketdata.MDEntry;

/**
 * <p>
 * <code>DomTableModel.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 04/10/2017 12:00:08
 *
 */
public class DomTableModel extends AbstractTableModel {

    private static final int ORDBUY = 0;
    private static final int VOLBUY = 1;
    private static final int QTBUY = 2;
    private static final int PRICE = 3;
    private static final int QTSELL = 4;
    private static final int VOLSELL = 5;
    private static final int ORDSELL = 6;
    
    private String[] headers;
    
    private List<MDEntry> buyList;
    private List<MDEntry> sellList;
    
     
    /**
     * 
     */
    public DomTableModel() {
	this.headers = new String[]{"", "VolCp", "QtCp", "Preço", "QtVd", "VolVd", ""};
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
	return buyList.size() + sellList.size();
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
	
	Object value = null;
	MDEntry entry = null;
	
	switch(columnIndex) {
	case ORDBUY:
	    //value = entry.getOrderBuyQt();
	    break;
	case QTBUY:
	    value = entry.getNumberOfOrders();
	    break;
	case VOLBUY:
	    value = entry.getMdEntrySize();
	    break;
	case PRICE:
	    value = entry.getMdEntryPx();
	    break;
	case VOLSELL:
	    value = entry.getMdEntrySize();
	    break;
	case QTSELL:
	    value = entry.getNumberOfOrders();
	    break;
	case ORDSELL:
	    //value = entry.getOrderSellQt();
	    break;
	}
	
	return value;
    }

}
