/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.apache.log4j.Level;

import com.cmm.jft.core.util.TimeCounter;
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

	public static void main(String[] args) {

		Properties p = new Properties();
		p.put("filename", "D:/Development/Study/Math/COTAHIST_A2014.TXT");

		BovespaHistoricalFileExtractor be = new BovespaHistoricalFileExtractor();
		be.config(p);

		TimeCounter tc = new TimeCounter();
		tc.start();
		List l = be.extract();
		tc.stop();

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
