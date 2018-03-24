/**
 * 
 */
package com.cmm.jft.ui.models;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

/**
 * <p>
 * <code>SecurityCellEditor.java</code>
 * </p>
 *
 * @author cristiano
 * @version 02/04/2017 05:27:20
 *
 */
public class SecurityCellEditor extends DefaultCellEditor {

    
    /**
     * @param textField
     */
    public SecurityCellEditor(JTextField textField) {
	super(textField);
    }
    
    
    
    /* (non-Javadoc)
     * @see javax.swing.DefaultCellEditor#stopCellEditing()
     */
    @Override
    public boolean stopCellEditing() {
	if(getCellEditorValue().toString().length() >=3){
	    System.out.println(getCellEditorValue());
            return true;
        }
        return false;
    }
    
    /* (non-Javadoc)
     * @see javax.swing.DefaultCellEditor#getCellEditorValue()
     */
    @Override
    public Object getCellEditorValue() {
        return ((JTextField)editorComponent).getText();
    }
    
    /* (non-Javadoc)
     * @see javax.swing.DefaultCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
     */
    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	Object fieldValue = value;
        if(null == fieldValue)
            fieldValue = ""; 
        
        ((JTextField)editorComponent).setEditable(true);
        ((JTextField)editorComponent).setText(fieldValue.toString());
        return this.editorComponent;
    }
    

}
