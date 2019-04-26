/**
 * 
 */
package com.cmm.jft.marketdata.service;

import com.cmm.jft.model.marketdata.MDEntry;
import com.cmm.jft.model.trading.enums.MDEntryTypes;

/**
 * <p>
 * <code>MDEvent.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 14/02/2018 11:28:15
 *
 */
public class MDEvent {
    
    private MDEntryTypes eventType;
    private MDEntry mdEntry;
    
    /**
     * 
     */
    public MDEvent(MDEntry entry) {
	this.mdEntry = entry;
	this.eventType = entry.getMdEntryType();
    }

    /**
     * @return the eventType
     */
    public MDEntryTypes getEventType() {
	return eventType;
    }
    
    /**
     * @return the mdEntry
     */
    public MDEntry getMdEntry() {
	return mdEntry;
    }
    
}
