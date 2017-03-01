/**
 * 
 */
package com.cmm.jft.messaging.enums;

import quickfix.field.MDEntryType;

/**
 * @author Cristiano M Martins
 *
 */
public enum EntryTypes {

    Bid(new MDEntryType('0')), 
    Offer(new MDEntryType('1')), 
    Trade(new MDEntryType('2')), 
    OpeningPrice(new MDEntryType('4')), 
    ClosingPrice(new MDEntryType('5')), 
    HighPrice(new MDEntryType('7')), 
    LowPrice(new MDEntryType('8')), 
    VWAPPrice(new MDEntryType('9')), 
    TradeVolume(new MDEntryType('B')),
    EmptyBook(new MDEntryType('J'));

    MDEntryType value;

    private EntryTypes(MDEntryType val) {
	this.value = val;
    }

    /**
     * @return the value
     */
    public MDEntryType getValue() {
	return value;
    }
    
    public static EntryTypes valueOf(char c) {
	EntryTypes et = null;

	for (EntryTypes e : EntryTypes.values()) {
	    if (e.value.getValue() == c) {
		et = e;
		break;
	    }
	}

	return et;
    }
    
    public static EntryTypes valueOf(MDEntryType c) {
	EntryTypes et = valueOf(c.getValue());
	return et;
    }
}
