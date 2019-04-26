/**
 * 
 */
package com.cmm.jft.data.extractor.marketdata;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;

import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.data.extractor.Extractor;
import com.cmm.jft.data.files.CSV;
import com.cmm.jft.model.marketdata.MDEntry;
import com.cmm.jft.model.security.Isin;
import com.cmm.jft.model.trading.enums.Side;
import com.cmm.jft.vo.Extractable;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>B3ISINFileExtractor.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 17, 2019
 * 
 */
public class B3ISINFileExtractor implements Extractor {
		
	private String fileName;
	private int rowCount;
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#config(java.util.Properties)
	 */
	@Override
	public boolean config(Properties properties) {
		// TODO Auto-generated method stub
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#extract()
	 */
	@Override
	public List<Extractable> extract() {
		
		List<Extractable> bsEvents = new ArrayList<>(1000000);
		try {
			DateTimeFormatter df = DateTimeFormatter.ofPattern(FormatterTypes.DATE_F8.getFormat());
			DateTimeFormatter tf = DateTimeFormatter.ofPattern(FormatterTypes.TIME_F4.getFormat());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(FormatterTypes.DATE_TIME_F8.getFormat());
						
			CSV csv = new CSV(fileName, ";", "RT", "RH");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();
				
				rowCount++;
				
				if (vs != null && vs[0] != null) {
					Isin isin = new Isin();					

					/*isin.setDescription(description);
					
					entry.setSymbol(vs[1]);
					entry.setSide(Side.getByValue(vs[2]));
					entry.setOrderId(Long.parseLong(vs[3]));
					entry.setMdEntryId(Long.parseLong(vs[4]));
					entry.setOrderEvent(Integer.parseInt(vs[5]));
					entry.setMdEntryDateTime(LocalDateTime.of(date, LocalTime.parse(vs[6], tf)));
					entry.setMdEntryPx(Double.parseDouble(vs[8]));
					entry.setMdEntrySize(Integer.parseInt(vs[9]));
					entry.setTradeVolume(Integer.parseInt(vs[10]));
					entry.setOrderDate(LocalDateTime.parse(vs[12], dtf));
					entry.setOrderStatus(vs[13].charAt(0));
					entry.setOrderCondition(Integer.parseInt(vs[14]));

					bsEvents.add(entry);*/
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}

		return bsEvents;
	}
	
}
