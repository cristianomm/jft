/**
 * 
 */
package com.cmm.jft.data.loader;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.cmm.jft.data.dde.QuoteData;
import com.cmm.jft.data.enums.DataFields;
import com.cmm.jft.data.exceptions.StreamException;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;

/**
 * <p>
 * <code>TickDataLoader.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 07/08/2013 01:45:08
 *
 */
public class TickDataLoader {

	public static void main(String[] args) {
		String file = "../jft_data/1378324245473quotes.csv";
		new TickDataLoader().storeFileOnDB(file, ";");
	}

	public void storeFileOnDB(String fileName, String colDelimiter) {

		try {
			Scanner sc = new Scanner(new File(fileName));
			sc.useDelimiter("\n");
			HashMap<String, ArrayList<QuoteData>> quotes = new HashMap<String, ArrayList<QuoteData>>();

			while (sc.hasNext()) {
				String[] cols = sc.next().split(colDelimiter);
				QuoteData qd = new QuoteData();
				// 28/08/13; 12:11; -16; 1377702695034; IBOV; IBOVESPA; BOVESPA;
				// #N/D; INDICE;
				// Wed Aug 28 12:11:35 BRT 2013; 50091.55; 50091.0; 50490.74;
				// 49766.91;
				// 182441; 214650600; 485700; 50439.7; 0; 0; 0; 0; 0.7; 0.0;
				// 1454296543.00; 3721431.00
				try {
					qd.addValue(DataFields.DAT, cols[0]);
					qd.addValue(DataFields.TIME, cols[1]);
					// qd.addValue(DataFields., cols[2]);
					qd.addValue(DataFields.ADT, cols[3]);
					qd.addValue(DataFields.SYMBOL, cols[4]);
					qd.addValue(DataFields.NME, cols[5]);
					qd.addValue(DataFields.MKT, cols[6]);
					qd.addValue(DataFields.ISI, cols[7]);
					qd.addValue(DataFields.TYPE, cols[8]);
					// qd.addValue(DataFields.QDT, cols[9]);
					qd.addValue(DataFields.OPN, cols[10]);
					qd.addValue(DataFields.LST_CLOSE, cols[11]);
					qd.addValue(DataFields.HIGH, cols[12]);
					qd.addValue(DataFields.LOW, cols[13]);
					qd.addValue(DataFields.TRADES, cols[14]);
					qd.addValue(DataFields.TRADED_QT, cols[15]);
					qd.addValue(DataFields.LST_TRADE_QT, cols[16]);
					qd.addValue(DataFields.LST_PRICE, cols[17]);
					// qd.addValue(DataFields., cols[18]);
					// qd.addValue(DataFields., cols[19]);
					// qd.addValue(DataFields., cols[20]);
					qd.addValue(DataFields.VAR, cols[21]);
					// qd.addValue(DataFields., cols[22]);
					qd.addValue(DataFields.FVT, cols[23]);
					qd.addValue(DataFields.LFV, cols[24]);

					if (!quotes.containsKey(qd.getSymbol())) {
						quotes.put(qd.getSymbol(), new ArrayList<QuoteData>());
					} else {
						quotes.get(qd.getSymbol()).add(qd);
					}

				} catch (StreamException e) {

				}

			}

			// grava como TickEvent
			try {
				for (String s : quotes.keySet()) {
					List l = DBFacade.getInstance().queryNative(
							"select securityid from stock where symbol = '" + s	+ "'");
					if (l == null || l.isEmpty()) {
						// new Stock(s, true).add();
					}
				}

				DBFacade.getInstance().beginTransaction();
				for (String s : quotes.keySet()) {
					List l = DBFacade.getInstance().queryNative(
							"select securityid from stock where symbol = '" + s	+ "'");
					if (l != null && !l.isEmpty()) {
//						Stock stock = (Stock) l.get(0);
//						ArrayList<QuoteData> qts = quotes.get(s);
//
//						for (QuoteData qdt : qts) {
//							Tick tick = new Tick(stock, new Date(
//									qdt.getAcquiredDateTime()),
//									qdt.getLstTradeQt(), new BigDecimal(
//											qdt.getLstPrice()), new Date(
//											qdt.getAcquiredDateTime()),
//									new BigDecimal(0), new BigDecimal(0), 0L,
//									0L);
//							tick.add();
//						}
					}
				}

			} catch (Exception e) {
				try {
					DBFacade.getInstance().rollback();
				} catch (DataBaseException e1) {
					e1.printStackTrace();
				}
			} catch (DataBaseException e) {
				e.printStackTrace();
			} finally {
				try {
					DBFacade.getInstance().commit();
				} catch (DataBaseException e) {
					e.printStackTrace();
				}
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

}
