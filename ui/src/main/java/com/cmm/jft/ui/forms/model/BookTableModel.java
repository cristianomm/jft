/**
 * 
 */
package com.cmm.jft.ui.forms.model;

import java.util.List;
import java.util.TreeMap;

import javax.swing.table.AbstractTableModel;

import com.cmm.jft.marketdata.service.MDListener;
import com.cmm.jft.marketdata.service.Market;
import com.cmm.jft.marketdata.service.MarketDataService;
import com.cmm.jft.model.marketdata.MDEntry;

/**
 * <p>
 * <code>BookTableModel.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 14/02/2018 10:31:51
 *
 */
public class BookTableModel extends AbstractTableModel implements MDListener {

    private static final int BUYBROKER=0; 
    private static final int BUYORDERS=1;
    private static final int BUYQTY=2;
    private static final int BUYPRICE=3;
    private static final int SELLPRICE=4;
    private static final int SELLQTY=5;
    private static final int SELLORDERS=6;
    private static final int SELLBROKER=7;
    
    private boolean mboBookType;
    private String[] headers = new String[] {"Broker", "Orders", "Qty", "Cpa", "Vda", "Qty", "Orders", "Broker"};
    
    private Market market;
    
    
    /**
     * 
     */
    public BookTableModel(String symbol) {
	if(symbol != null && MarketDataService.getInstance().hasMarketSymbol(symbol) != null) {
	    setSymbol(symbol);
	}
	
    }
    
    public void setSymbol(String symbol) {
	this.market = MarketDataService.getInstance().hasMarketSymbol(symbol);
	this.market.addListener(this);
	this.mboBookType = true;
    }
    
    
    
    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    @Override
    public int getRowCount() {
	
	int rCount = 0;
	
	if(mboBookType) {
	    rCount = market.getAskMBO().size() > market.getBidMBO().size()?
		    market.getAskMBO().size() : market.getBidMBO().size();
	}else {
	    rCount = market.getAskMBP().size() > market.getBidMBP().size()?
		    market.getAskMBP().size() : market.getBidMBP().size();
	}
	
	return rCount;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    @Override
    public int getColumnCount() {
	return headers.length-1;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
	Object obj = null;
	List<MDEntry> mktList = null;
	
	if(columnIndex >= BUYBROKER && columnIndex <= BUYPRICE) {
	    
	    mktList = mboBookType? market.getBidMBO():market.getBidMBP();
	    
	    if(rowIndex < mktList.size()) {
		switch(columnIndex) {
		case BUYBROKER:
		    obj = mktList.get(rowIndex).getMdEntryBuyer();
		    break;
		case BUYORDERS:
		    obj = mktList.get(rowIndex).getNumberOfOrders();
		    break;
		case BUYQTY:
		    obj = mktList.get(rowIndex).getMdEntrySize();
		    break;
		case BUYPRICE:
		    obj = mktList.get(rowIndex).getMdEntryPx();
		    break;
		}
	    }
	}else if(columnIndex >= SELLPRICE && columnIndex <= SELLBROKER) {
	    mktList = mboBookType?market.getAskMBO():market.getAskMBP();
	    
	    if(rowIndex < mktList.size()) {
		switch(columnIndex) {
		case SELLPRICE:
		    obj = mktList.get(rowIndex).getMdEntryPx();
		    break;
		case SELLQTY:
		    obj = mktList.get(rowIndex).getMdEntrySize();
		    break;
		case SELLORDERS:
		    obj = mktList.get(rowIndex).getNumberOfOrders();
		    break;
		case SELLBROKER:
		    obj = mktList.get(rowIndex).getMdEntrySeller();
		    break;
		}
	    }
	}
	
	return obj;
    }



    /* (non-Javadoc)
     * @see com.cmm.jft.marketdata.service.MDListener#marketDataEvent()
     */
    @Override
    public void marketDataEvent() {
	fireTableDataChanged();
	
    }

}
