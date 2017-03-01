/**
 * 
 */
package com.cmm.jft.messaging.enums;

import quickfix.field.MDUpdateAction;

/**
 * <p>
 * <code>UpdateActions.java</code>
 * </p>
 *
 * @author cristiano
 * @version 01/03/2017 11:45:04
 *
 */
public enum UpdateActions {
    
    New(new MDUpdateAction('0')), 
    Change(new MDUpdateAction('1')), 
    Delete(new MDUpdateAction('2')), 
    Delete_Thru(new MDUpdateAction('3')), 
    Delete_From(new MDUpdateAction('4')),
    Overlay(new MDUpdateAction('5'));

    MDUpdateAction value;

    private UpdateActions(MDUpdateAction val) {
	this.value = val;
    }

    /**
     * @return the value
     */
    public MDUpdateAction getValue() {
	return value;
    }
    
    public static UpdateActions valueOf(char c) {
	UpdateActions ua = null;

	for (UpdateActions e : UpdateActions.values()) {
	    if (e.value.getValue() == c) {
		ua = e;
		break;
	    }
	}

	return ua;
    }
    
    public static UpdateActions valueOf(MDUpdateAction update) {
	UpdateActions ua = valueOf(update.getValue());
	return ua;
    }
    
}
