/**
 * 
 */
package com.cmm.jft.data.fixlogs;

import java.util.List;
import java.util.Properties;

import com.cmm.jft.data.extractor.marketdata.BovespaOfferFileExtractor;
import com.cmm.jft.vo.Extractable;
import com.cmm.jft.vo.OrderEventVO;

/**
 * <p><code>FIXLogCreator.java</code></p>
 * @author cristiano
 * @version Nov 6, 2015 1:40:02 AM
 *
 */
public class FIXLogCreator {
	
	public void fromMDFile(String buyFileName, String sellFileName, String tradeFileName) {
		
		
		
	}
	
	
	private void readOfferMDFile(String fileName) {
		
		Properties p = new Properties();
		p.put("filename", fileName);
		BovespaOfferFileExtractor bfe = new BovespaOfferFileExtractor();
		bfe.config(p);
		List<Extractable> orders =  bfe.extract();
		
		
		
	}
	
}
