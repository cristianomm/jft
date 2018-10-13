/**
 * 
 */
package com.cmm.jft.engine;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.concurrent.atomic.AtomicInteger;

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

	public IdGenerator(Security security, LocalDateTime dateTime) {
		this.seqNumericID = new AtomicInteger(security.getSecurityID() + dateTime.get(ChronoField.MILLI_OF_DAY));
	}

	/**
	 * @param localDateTime
	 */
	public IdGenerator(LocalDateTime localDateTime) {
		this.seqNumericID = new AtomicInteger((int) localDateTime.get(ChronoField.MILLI_OF_DAY));
	}

	public int actualInt() {
		return seqNumericID.get();
	}

	public long actualLong() {
		return seqNumericID.get();
	}

	public String actualString() {
		return seqNumericID.toString();
	}

	/**
	 * Incrementa e retorna um <code>int</code>
	 * 
	 * @return
	 */
	public int nextInt() {
		return seqNumericID.getAndIncrement();
	}

	/**
	 * Incrementa e retorna um <code>long</code>
	 * 
	 * @return
	 */
	public long nextLong() {
		nextInt();
		return seqNumericID.longValue();
	}

	public String nextNumericString() {
		nextInt();
		return seqNumericID.toString();
	}

}
