/**
 * 
 */
package com.cmm.jft.data.extractor.marketdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Level;

import com.cmm.jft.core.util.TimeCounter;
import com.cmm.jft.data.dde.QuoteData;
import com.cmm.jft.data.enums.DataFields;
import com.cmm.jft.data.extractor.Data;
import com.cmm.jft.data.extractor.Extractable;
import com.cmm.logging.Logging;
import com.sun.glass.ui.SystemClipboard;
import com.sun.glass.ui.Timer;

/**
 * <p><code>BovespaHistoricalFileExtractor.java</code></p>
 * @author Cristiano M Martins
 * @version 06/03/2015 14:11:49
 *
 */
public class BovespaHistoricalFileExtractor extends BovespaFileExtractor {
	
		
	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#extract()
	 */
	@Override
	public List<Extractable> extract() {
		List<Extractable> prices = new ArrayList<Extractable>();
				
		try {
			int regcont=0;
			int linecount=0;
			String line;
			scanner.useDelimiter("\n");
			while(scanner.hasNext()) {
				
				line = scanner.next();
				linecount++;
				//registros que iniciam com 01....
				if(line.startsWith("01")) {
					
					//cria o quotedata
					Data quote = new Data();
					
					//recupera os valores da linha e preenche o objeto
					String symbol = removeWhite(line.substring(12, 24)).replace("'", "''");
					String stockcode = removeWhite(line.substring(12, 16)).replace("'", "''");
					String companyName = removeWhite(line.substring(27, 39)).replace("'", "''");
					int year, month, day;
					year = Integer.parseInt(line.substring(2, 6));
					month = Integer.parseInt(line.substring(6, 8));
					day = Integer.parseInt(line.substring(8, 10));

					String datetime = String.format("%02d-%02d-%02d", year, month, day);

					//nao adiciona a linha caso falte alguma informacao
					if(symbol.isEmpty() || stockcode.isEmpty() || companyName.isEmpty()||datetime.isEmpty())continue;
					
					String isin = removeWhite(line.substring(230, 242));
					String currencyID = line.substring(52, 56).trim();
					
					quote.put("symbol", symbol);
					quote.put("stockCode", stockcode);
					quote.put("companyName", companyName);
					quote.put("dateTime", datetime);
					quote.put("isin", isin);
					quote.put("currency", currencyID);
					quote.put("marquetType",Integer.parseInt(line.substring(24,27)));
					quote.put("fatquote", Integer.parseInt(line.substring(210, 217)));
					quote.put("QHigh",new BigDecimal(Float.parseFloat(line.substring(69,82))/100f));
					quote.put("QLow",new BigDecimal(Float.parseFloat(line.substring(82,95))/100f));
					quote.put("QOpen", new BigDecimal(Float.parseFloat(line.substring(56,69))/100f));
					quote.put("QClose", new BigDecimal(Float.parseFloat(line.substring(108,121))/100f));
					quote.put("QAsk", new BigDecimal(Float.parseFloat(line.substring(121,134))/100f));
					quote.put("QBid", new BigDecimal(Float.parseFloat(line.substring(134,147))/100f));
					quote.put("volume", Long.parseLong(line.substring(170, 188)));
					quote.put("tradedUnits", Long.parseLong(line.substring(147, 152)));
					quote.put("tradedQuantity", Long.parseLong(line.substring(152, 170)));
					
					//adiciona na lista
					prices.add(quote);
					
				}

			}

		} catch (Exception e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} finally{
			if(scanner!=null){
				try{
					scanner.close();
				}catch(Exception e){
					Logging.getInstance().log(getClass(), e, Level.ERROR);
				}
			}
		}

		return prices;
	}

	
	private String removeWhite(String txt) {
		if(txt!=null){
			txt = txt.replaceAll("\\t|(\\s+){2}", "");
		}
		return txt;
	}

}
