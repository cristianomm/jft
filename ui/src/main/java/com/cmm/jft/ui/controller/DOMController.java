package com.cmm.jft.ui.controller;

import com.cmm.jft.model.marketdata.MDEntry;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.security.SecurityInfo;
import com.cmm.jft.model.trading.Orders;
import com.cmm.jft.model.trading.enums.OrderTypes;
import com.cmm.jft.model.trading.enums.OrderValidityTypes;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.model.trading.enums.TradeTypes;
import com.cmm.jft.services.trading.TradingService;
import com.cmm.jft.ui.forms.model.DOMTableModel;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.LocalDateTime;
import java.util.Date;

public class DOMController {
    
    private class OrderStrategy{
	double price;
	int volume;
	double gain;
	double loss;
	
    }
    
    

    private int openQty;
    private double openFin;
    private double balance;
    private int qty;
    private double price;
    private Security security;

    private DOMTableModel tblModel;
    private PropertyChangeSupport _propertyChangeSupport = new PropertyChangeSupport(this);

    public DOMController() {
        super();
        security = new Security();
        security.setSymbol("WDOX17");
        security.setSecurityInfoID(new SecurityInfo());
        security.getSecurityInfoID().setContractSize(1);
        security.getSecurityInfoID().setDigits(3);
        security.getSecurityInfoID().setMinVolume(1);
        security.getSecurityInfoID().setMaxVolume(270000000);
        security.getSecurityInfoID().setStepVolume(1);
        security.getSecurityInfoID().setTickValue(5);
        security.getSecurityInfoID().setTickSize(.5);
        
    }


    public void setOpenQty(int openQty) {
        int oldOpenQty = this.openQty;
        this.openQty = openQty;
        _propertyChangeSupport.firePropertyChange("openQty", oldOpenQty, openQty);
    }

    public int getOpenQty() {
        return openQty;
    }

    public void setOpenFin(double openFin) {
        double oldOpenFin = this.openFin;
        this.openFin = openFin;
        _propertyChangeSupport.firePropertyChange("openFin", oldOpenFin, openFin);
    }

    public double getOpenFin() {
        return openFin;
    }

    public void setBalance(double balance) {
        double oldBalance = this.balance;
        this.balance = balance;
        _propertyChangeSupport.firePropertyChange("balance", oldBalance, balance);
    }

    public double getBalance() {
        return balance;
    }

    public void setQty(int qty) {
        int oldQty = this.qty;
        this.qty = qty;
        _propertyChangeSupport.firePropertyChange("qty", oldQty, qty);
    }

    public int getQty() {
        return qty;
    }

    public void setPrice(double price) {
        double oldPrice = this.price;
        this.price = price;
        _propertyChangeSupport.firePropertyChange("price", oldPrice, price);
    }

    public double getPrice() {
        return price;
    }

    public void setSecurity(Security security) {
        Security oldSecurity = this.security;
        this.security = security;
        _propertyChangeSupport.firePropertyChange("security", oldSecurity, security);
    }

    public Security getSecurity() {
        return security;
    }

    public void setTblModel(DOMTableModel tblModel) {
        DOMTableModel oldTblModel = this.tblModel;
        this.tblModel = tblModel;
        _propertyChangeSupport.firePropertyChange("tblModel", oldTblModel, tblModel);
    }

    public DOMTableModel getTblModel() {
        return tblModel;
    }


    public void addPropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        _propertyChangeSupport.removePropertyChangeListener(l);
    }


    public void addOrder(int selectedRow, int selectedColumn) {

        double ordPrice = (Double)tblModel.getValueAt(selectedRow, DOMTableModel.PRICE);
        int ordQty = 0;
        MDEntry entry = tblModel.getEntry(ordPrice);

        //a ordem a ser inserida eh de compra ou venda
        Side side = null;
        Integer val = 0;
        OrderTypes ordType = OrderTypes.Limit;
        switch (selectedColumn) {
        case DOMTableModel.ORDBUY:
        case DOMTableModel.QTBUY:
        case DOMTableModel.VOLBUY:
            side = Side.BUY;
            val = (Integer) tblModel.getValueAt(selectedRow, DOMTableModel.VOLBUY);
            ordType = val != null? ordType:OrderTypes.Stop;
            break;

        case DOMTableModel.ORDSELL:
        case DOMTableModel.QTSELL:
        case DOMTableModel.VOLSELL:
            side = Side.SELL;
            val = (Integer) tblModel.getValueAt(selectedRow, DOMTableModel.VOLSELL);
            ordType = val != null? ordType:OrderTypes.Stop;
            break;
        }

        //somente envia a ordem se selecionou um lado do book para colocar a ordem
        //caso seja clicado o preco, nao ira enviar a ordem
        if(side != null) {
            //monta a ordem
            OrderStrategy os = new OrderStrategy();

            //adiciona no dom a indicacao de ordem ativa
            tblModel.addPendingOrder(ordPrice, side, qty);

            //envia a ordem
            int ret = TradingService.getInstance().newOrder(ordType, side, security.getSymbol(), 
        	    qty, ordPrice, os.loss, os.gain, LocalDateTime.now(), 
        	    TradeTypes.DAY_TRADE, OrderValidityTypes.DAY, "");

            setOpenQty(TradingService.getInstance().getPosition());
            setPrice(ordPrice);
        }
        
    }


}
