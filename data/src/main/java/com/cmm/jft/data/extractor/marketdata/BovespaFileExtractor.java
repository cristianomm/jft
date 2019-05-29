/**
 * 
 */
package com.cmm.jft.data.extractor.marketdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;

import com.cmm.jft.core.util.TimeCounter;
import com.cmm.jft.data.extractor.Extractor;
import com.cmm.jft.data.files.ParquetConverter;
import com.cmm.logging.Logging;

/**
 * <p><code>BovespaFileExtractor.java</code></p>
 * @author Cristiano M Martins
 * @version 06/03/2015 16:10:21
 *
 */
public abstract class BovespaFileExtractor implements Extractor {

	protected String fileName;
	protected Scanner scanner;

	protected Pattern pTime = Pattern.compile("[\\d\\d|:]+[.|:|,][\\d]{3}");
	protected Matcher matcher = pTime.matcher("");

	public static void main(String[] args) throws IOException {

		Properties p = new Properties();
		p.put("filename", "/home/cristiano/Data/MarketData/OFER_VDA_BMF_20141103.TXT");
		
		extractOffer(p);
	} 

	public static void extractHistorical(Properties p) {
		BovespaHistoricalFileExtractor be = new BovespaHistoricalFileExtractor();
		be.config(p);

		TimeCounter tc = new TimeCounter();
		tc.start();
		List l = be.extract();
		tc.stop();

		System.out.println(l.size() + " Lines in: " + tc.getElapsedInSeconds());
	}
	
	public static void extractOffer(Properties p) throws IOException {
		BovespaOfferFileExtractor be = new BovespaOfferFileExtractor();
		be.config(p);

		TimeCounter tc = new TimeCounter();
		tc.start();
		List l = be.extract();
		tc.stop();
		
		ParquetConverter converter = new ParquetConverter();
		converter.convertOffers(l);

		System.out.println(l.size() + " Lines in: " + tc.getElapsedInSeconds());
	}
	
	public static void extractTrades(Properties p) throws IOException {
		BovespaTradeFileExtractor be = new BovespaTradeFileExtractor();
		be.config(p);

		TimeCounter tc = new TimeCounter();
		tc.start();
		List l = be.extract();
		tc.stop();
		
		ParquetConverter converter = new ParquetConverter();
		converter.convertTrades(l);

		System.out.println(l.size() + " Lines in: " + tc.getElapsedInSeconds());
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#config(java.util.Properties)
	 */
	@Override
	public boolean config(Properties properties) {
		boolean ret = false;
		if(properties!= null && properties.containsKey("filename")){
			this.fileName = properties.getProperty("filename");
			try {
				this.scanner = new Scanner(new File(fileName));
				ret = true;
			} catch (FileNotFoundException e) {
				ret = false;
				Logging.getInstance().log(getClass(), e, Level.ERROR);
			}
		}
		return ret;
	}
}
