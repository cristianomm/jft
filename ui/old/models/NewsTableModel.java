/**
 * 
 */
package com.cmm.jft.ui.models;

import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import com.cmm.jft.marketdata.MDNews;

/**
 * <p>
 * <code>NewsTableModel.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 03/04/2017 12:07:15
 *
 */
public class NewsTableModel extends AbstractTableModel {

    public static final int SOURCE = 0;
    public static final int TIME = 1;
    public static final int HEADLINE = 2;

    private String[] headers;
    private ArrayList<MDNews> news;

    public NewsTableModel() {
	this.news = new ArrayList<>();
	this.headers = new String[]{
		"Fonte", "Hora", "Notícia"};
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
	return news.size();
    }
    
    
    public MDNews getMDNews(int row){
	MDNews ret = null;
	
	if(row >= 0 && row < news.size()) {
	    ret = news.get(row);
	}
	
	return ret;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIdx, int colIdx) {

	Object value = null;
	MDNews mkt = news.get(rowIdx);
	if(mkt != null) {
	    switch(colIdx){
	    case SOURCE:
		value = mkt.getNewsSrc();
		break;
	    case TIME:
		value = mkt.getOrigTime();
		break;
	    case HEADLINE:
		value = mkt.getHeadLine();
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
	case SOURCE:
	    clss = String.class;
	    break;
	case TIME:
	    clss = Date.class;
	    break;
	case HEADLINE:
	    clss = String.class;
	    break;
	}

	return clss;
    }


    public void addNews(MDNews mdnews){
	news.add(mdnews);
	int lastRow = getRowCount()-1;
	fireTableRowsInserted(lastRow, lastRow);
    }


}
