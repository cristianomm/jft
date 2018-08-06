/**
 * 
 */
package com.cmm.jft.data.loader;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Level;

import com.cmm.jft.core.format.BigDecimalFormatter;
import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.core.format.IntFormatter;
import com.cmm.jft.data.files.CSV;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.DBObject;
import com.cmm.logging.Logging;
/**
 * <p>
 * <code>BMFBovespaTDNegociation.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 04/08/2014 01:05:50
 *
 */
public class BMFBovespaTDNegociation implements Loadable {

    /**
     * 
     */
    public BMFBovespaTDNegociation() {
	// TODO Auto-generated constructor stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.data.loader.Loadable#importFiles(java.lang.String)
     */
    public String importFiles(String dirName) {
	// TODO Auto-generated method stub
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.data.loader.Loadable#importData(java.lang.String)
     */
    public int importData(String fileName) {

	int ret = 0;
	if (fileName == null || fileName.isEmpty()) {
	    ret = -1;
	} else {// caso o parametro de entrada esteja ok...

	    try {
		CSV csv = new CSV(fileName, ";", "RH", "RT");

		IntFormatter ift = (IntFormatter) FormatterFactory
			.getFormatter(FormatterTypes.INT);
		DateTimeFormatter dtft = (DateTimeFormatter) FormatterFactory
			.getFormatter(FormatterTypes.DATE_TIME_F9);
		BigDecimalFormatter bdft = (BigDecimalFormatter) FormatterFactory
			.getFormatter(FormatterTypes.BIGDECIMAL);

		Queue<DBObject> trades = new LinkedBlockingQueue<DBObject>();

		while (csv.hasNext()) {
		    try {
			String[] line = csv.readLine();

			String date = line[0].trim();
			String symbol = line[1].trim();
			BigDecimal price = bdft.parse(line[3]);
			int volume = ift.parse(line[4]);
			String time = line[5].replace(".", ",");
			LocalDateTime dtTime = dtft.parse(date + " " + time);

			//						Security sec = SecurityService.getInstance()
			//								.loadSecurity(symbol);
			//						if (sec != null) {
			//							MarketTrade mt = new MarketTrade();
			//							mt.setPrice(price);
			//							mt.setTradeDateTime(dtTime);
			//							mt.setVolume(volume);
			//							mt.setSecurityID(sec);
			//							trades.add(mt);
			//						}

		    } catch (Exception e) {
			e.printStackTrace();
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		    }
		}

		int i = DBFacade.getInstance().batchInsert(trades);
		Logging.getInstance().log(getClass(),
			"Inseridos " + i + " Objetos.", Level.INFO);

	    } catch (Exception e) {
		ret = -1;
		Logging.getInstance().log(getClass(), e, Level.ERROR);
	    }

	}

	return ret;
    }

}
