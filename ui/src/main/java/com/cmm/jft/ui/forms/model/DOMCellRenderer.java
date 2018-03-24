package com.cmm.jft.ui.forms.model;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class DOMCellRenderer extends DefaultTableCellRenderer {
    
    public DOMCellRenderer() {
        super();
    }
    
    @Override
    public Component getTableCellRendererComponent(JTable jTable, Object value, 
                                                   boolean isSelected, boolean hasFocus, 
                                                   int row, int column) {
        
	
        Component cp = super.getTableCellRendererComponent(jTable, value, isSelected, hasFocus, row, column);
        
        if(isSelected || hasFocus){
            switch(column){
            case DOMTableModel.ORDBUY:
            case DOMTableModel.VOLBUY:
            case DOMTableModel.QTBUY:
                cp.setForeground(Color.black);
                cp.setBackground(Color.decode("#81d7dc"));
                break;
            case DOMTableModel.PRICE:
                cp.setForeground(Color.black);
                cp.setBackground(Color.decode("#feea9e"));
                break;
            
            case DOMTableModel.QTSELL:
            case DOMTableModel.VOLSELL:
            case DOMTableModel.ORDSELL:
                cp.setForeground(Color.black);
                cp.setBackground(Color.decode("#d36169"));
                break;
            }
        }
        else{
            switch(column){
            case DOMTableModel.ORDBUY:
                cp.setBackground(Color.black);
                cp.setForeground(Color.white);
                break;
            case DOMTableModel.VOLBUY:
            case DOMTableModel.QTBUY:
                if(value == null){
                    cp.setBackground(Color.decode("#0917a0"));
                }else{
                    cp.setBackground(Color.decode("#4169E1"));
                }
                break;
            case DOMTableModel.PRICE:
                cp.setBackground(Color.white);
                break;
            case DOMTableModel.QTSELL:
            case DOMTableModel.VOLSELL:
                if(value == null){
                    cp.setBackground(Color.decode("#df2d2d"));
                }else{
                    cp.setBackground(Color.decode("#ff7f50"));
                }
                break;
            case DOMTableModel.ORDSELL:
                cp.setBackground(Color.black);
                cp.setForeground(Color.white);
                break;
            }    
        }
        
        return cp;
    }
    
    
}
