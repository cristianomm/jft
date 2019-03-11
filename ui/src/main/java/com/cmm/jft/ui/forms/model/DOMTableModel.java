/**
 * 
 */
package com.cmm.jft.ui.forms.model;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.cmm.jft.model.marketdata.MDEntry;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.services.trading.TradingService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;
import java.util.TreeMap;

/**
 * <p>
 * <code>DomTableModel.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 04/10/2017 12:00:08
 *
 */
public class DOMTableModel extends AbstractTableModel {

    private class EntryComparator implements Comparator {
        @Override
        public int compare(Object object, Object object2) {

            Double d1 = (Double) object;
            Double d2 = (Double) object2;
            if (d1.compareTo(d2) == 1) {
                return -1;
            } else if (d1.compareTo(d2) == -1) {
                return 1;
            }

            return 0;
        }
    }

    
    public static final int ORDBUY = 0;
    public static final int VOLBUY = 1;
    public static final int QTBUY = 2;
    public static final int PRICE = 3;
    public static final int QTSELL = 4;
    public static final int VOLSELL = 5;
    public static final int ORDSELL = 6;

    private String[] headers;
    
    class Summary{
	
	double price;
	int volume;
	int ordCnt;
	
    }

    private TreeMap<Double, Summary> buyOrders;
    private TreeMap<Double, Summary> sellOrders;
    private TreeMap<Double, MDEntry> entries;


    /**
     *
     */
    public DOMTableModel() {
        this.buyOrders = new TreeMap();
        this.sellOrders = new TreeMap();
        this.entries = new TreeMap(new EntryComparator());
        this.headers = new String[] { "OrdCp", "VolCp", "QtCp", "Px", "QtVd", "VolVd", "OrdVd" };

        new Thread(new Runnable() {
            @Override
            public void run() {
                Random rand = new Random();

                MDEntry mde = new MDEntry();
                double min = 3200;
                double max = 3500;
                ArrayList<Double> prices = new ArrayList((int) (max - min) * 2);
                while (min <= max) {
                    prices.add(min += .5);
                }

                addPriceList(prices);
                prices = null;
                
                while (true) {
                    try {
                        mde = new MDEntry();
                        mde.setMdEntryPx(3300 + rand.nextInt(31) + (.5 * rand.nextInt(2)));
                        if (mde.getMdEntryPx() < 3315.5) {
                            mde.setSide(Side.BUY);
                        } else {
                            mde.setSide(Side.SELL);
                        }

                        mde.setMdEntrySize(rand.nextInt(250));
                        mde.setNumberOfOrders(rand.nextInt(200));

                        addEntry(mde.getMdEntryPx(), mde);

                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }).start();


    }

    public void addPriceList(List<Double> prices) {
        for(double px:prices){
            MDEntry mde = new MDEntry();
            mde.setMdEntryPx(px);
            entries.put(px, mde);
        }
        //atualiza a tabela
        fireTableDataChanged();   
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
        return entries.size();
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {

        if (columnIndex == ORDBUY || columnIndex == ORDSELL) {
            return true;
        }
        return false;
    }
    
    @Override
    public Class getColumnClass(int colIdx) {
        Class clss = null;
        switch (colIdx) {
        case ORDBUY:
        case QTBUY:
        case VOLBUY:
            clss = Integer.class;
            break;
        case PRICE:
            clss = Double.class;
            break;
        case VOLSELL:
        case QTSELL:
        case ORDSELL:
            clss = Integer.class;
            break;
        }
        return clss;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Object value = null;
        MDEntry entry = null;
        int cnt = 0;
        for (MDEntry e : entries.values()) {
            entry = e;
            if (cnt++ == rowIndex) {
                break;
            }
        }

        switch (columnIndex) {
        case ORDBUY:
            value = entry.getSide() == Side.BUY ? getOrderCount(entry.getMdEntryPx(), Side.BUY) : null;
            break;
        case QTBUY:
            value = entry.getSide() == Side.BUY ? entry.getNumberOfOrders() : null;
            break;
        case VOLBUY:
            value = entry.getSide() == Side.BUY ? entry.getMdEntrySize() : null;
            break;
        case PRICE:
            value = entry.getMdEntryPx();
            break;
        case VOLSELL:
            value = entry.getSide() == Side.SELL ? entry.getMdEntrySize() : null;
            break;
        case QTSELL:
            value = entry.getSide() == Side.SELL ? entry.getNumberOfOrders() : null;
            break;
        case ORDSELL:
            value = entry.getSide() == Side.SELL ? getOrderCount(entry.getMdEntryPx(), Side.SELL) : null;
            break;
        }

        return value;
    }

    public void addEntry(double price, MDEntry entry) {

        if (entries.containsKey(price)) {
            entries.put(price, entry);
            int pos = 0;
            for (MDEntry e : entries.values()) {
                pos++;
                if (e.getMdEntryPx() == price) {
                    break;
                }
            }
            fireTableRowsUpdated(pos, pos);
        } else {
            entries.put(price, entry);
            int pos = 0;
            for (MDEntry e : entries.values()) {
                pos++;
                if (e.getMdEntryPx() == price) {
                    break;
                }
            }
            fireTableRowsInserted(pos, pos);
        }
    }


    public MDEntry getEntry(double price){
        return entries.get(price);
    }
    
    public void addPendingOrder(double price, Side side, int volume){
        Summary pend = getPendingOrder(price, side);
        if(pend == null){
            pend = new Summary();
            
            if(side == Side.BUY){
                buyOrders.put(price, pend);
            }else{
                sellOrders.put(price, pend);
            }
        }
        
        pend.ordCnt += 1;
        pend.price = price;
        pend.volume += volume;
        
    }
    
    private Summary getPendingOrder(double price, Side side){
        return side == Side.BUY? buyOrders.get(price): sellOrders.get(price);
    }
    
    public int getOrderCount(double price, Side side){
        int qty = 0;
        
        TradingService.getInstance().getOrderCount(price);
        
        return qty;
    }
    

}
