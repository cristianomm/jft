/**
 * 
 */
package com.cmm.jft.engine;

import java.time.Instant;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.cmm.jft.security.Security;

/**
 * <p>
 * <code>IdGenerator.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 3, 2016 10:38:47 AM
 *
 */
public class IdGenerator {
    
    private AtomicInteger seqNumericID;

    
    public IdGenerator(Security security, Date date){
	this.seqNumericID = new AtomicInteger(security.getSecurityID() + (int)date.getTime());
    }
    
    /**
     * @param date
     */
    public IdGenerator(Date date) {
	this.seqNumericID = new AtomicInteger((int)date.getTime());
    }

    /**
     * 
     * @return 
     */
    public int getNextNumeric() {
	return seqNumericID.getAndIncrement();
    }

}
