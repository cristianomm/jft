/**
 * 
 */
package com.cmm.jft.data.dde;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Level;

import com.cmm.jft.data.DataStream;
import com.cmm.jft.data.enums.DataFields;
import com.cmm.jft.data.exceptions.StreamException;
import com.cmm.logging.Logging;
import com.pretty_tools.dde.DDEException;
import com.pretty_tools.dde.client.DDEClientConversation;

/**
 * <p>
 * <code>DDEStream.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 30/07/2013 01:32:48
 * 
 */
public class DDEStream implements DataStream {

	private boolean open;
	private String service;
	private String topic;
	private DDEClientConversation ddeConv;
	private ArrayList<String> symbols;
	private LinkedBlockingQueue<QuoteData> data;
	private HashMap<String, QuoteData> lastQuotes;
	private DDEMapping ddeMapping;
	private FileOutputStream fos;

	public DDEStream(String service, String topic) {
		this.service = service;
		this.topic = topic;
		this.ddeMapping = new DDEMapping();
		this.ddeConv = new DDEClientConversation();
		this.symbols = new ArrayList<String>();
		this.data = new LinkedBlockingQueue<QuoteData>();
		this.lastQuotes = new HashMap<String, QuoteData>();
		try {
			this.fos = new FileOutputStream(System.currentTimeMillis() + "quotes.csv");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void addSymbol(String symbol) {
		symbols.add(symbol);
		ddeMapping.addSymbol(symbol);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.DataStream#open()
	 */
	public void open() {
		open = true;
		try {
			ddeConv.connect(service, topic);
			ddeMapping.map(ddeConv);
		} catch (DDEException e) {
			e.printStackTrace();
			Logging.getInstance().log(DDEStream.class, e, Level.ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.DataStream#close()
	 */
	public void close() {
		open = false;
		try {
			ddeConv.disconnect();
		} catch (DDEException e) {
			e.printStackTrace();
			Logging.getInstance().log(DDEStream.class, e, Level.ERROR);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.DataStream#acquireData(long)
	 */
	public void acquireData(long time) {

		while (open) {
			try {

				for (String sbl : symbols) {
					acquire(sbl);
				}

				Thread.sleep(time);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Logging.getInstance().log(DDEStream.class, e, Level.ERROR);
			}
		}

	}

	private void acquire(String symbol) {

		if (ddeMapping.contains(symbol)) {
			QuoteData qd = new QuoteData();

			// busca o mapeamento para o simbolo
			HashMap<DataFields, Map> map = ddeMapping.getMaps(symbol);
			for (DataFields code : map.keySet()) {
				String st;
				try {
					st = ddeConv.request(map.get(code).getPosition(
							ddeMapping.getMapLanguage()));
					qd.addValue(code, st);
				} catch (DDEException e) {
					e.printStackTrace();
					Logging.getInstance().log(DDEStream.class, e, Level.ERROR);
				} catch (StreamException e) {
					e.printStackTrace();
					Logging.getInstance().log(DDEStream.class, e, Level.ERROR);
				}

			}
			qd.updateDateTime();

			// verifica se nao eh cotacao repetida
			if (lastQuotes.containsKey(symbol)	&& !lastQuotes.get(symbol).equals(qd)) {
				// caso nao seja, adiciona na fila
				data.add(qd);
				lastQuotes.put(symbol, qd);
//				try {
//					fos.write((qd.toString()+"\n").getBytes());
//				} catch (IOException e) {
//					e.printStackTrace();
//					Logging.getInstance().log(DDEStream.class, e, Level.ERROR);
//				}
			} else if (!lastQuotes.containsKey(symbol)) {
				data.add(qd);
				lastQuotes.put(symbol, qd);
				// System.out.println(qd);
				// try {
				// fos.write((qd.toString()+"\n").getBytes());
				// } catch (IOException e) {
				// e.printStackTrace();
				// Logging.getInstance().log(DDEStream.class, e, Level.ERROR);
				// }
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.DataStream#getData()
	 */
	public LinkedBlockingQueue<QuoteData> getData() {
		return data;
	}

}
