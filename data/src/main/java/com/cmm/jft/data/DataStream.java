/**
 * 
 */
package com.cmm.jft.data;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import com.cmm.jft.data.dde.QuoteData;

/**
 * <p>
 * <code>DataStream.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 30/07/2013 01:17:12
 * 
 */
public interface DataStream {

	void open();

	void close();

	void addSymbol(String symbol);

	/**
	 * Realiza a captura de dados durante <code>time</code> milisegundos e
	 * coloca os dados capturados em uma fila
	 * 
	 * @param time
	 */
	void acquireData(long time);

	LinkedBlockingQueue<QuoteData> getData();

}
