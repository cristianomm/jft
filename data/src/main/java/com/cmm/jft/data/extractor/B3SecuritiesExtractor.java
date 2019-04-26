/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.TreeMap;

import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.security.SecurityInfo;
import com.cmm.jft.model.security.enums.MarketTypes;
import com.cmm.jft.model.security.enums.OptionStyles;
import com.cmm.jft.model.security.enums.SecurityCategory;
import com.cmm.jft.vo.Extractable;

/**
 * <p>
 * <code>B3SecuritiesExtractor.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 24, 2019
 * 
 */
public class B3SecuritiesExtractor implements Extractor {

	private Map<String, String> files;
	
	
	/**
	 * 
	 */
	public B3SecuritiesExtractor() {
		this.files = new HashMap<>();
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#config(java.util.Properties)
	 */
	@Override
	public boolean config(Properties properties) {
		
		files.put("titulos_negociaveis", properties.getProperty("titulos_negociaveis"));
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#extract()
	 */
	@Override
	public List<Extractable> extract() {		
		Map<String, Security> securities = new TreeMap<>();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FormatterTypes.DATE_F9.getFormat());
		
		//read titulos_negociaveis file
		try {
			Scanner sc = new Scanner(new File(files.get("titulos_negociaveis")));
			
			while(sc.hasNext()) {
				String line = sc.nextLine();
				
				String companyName = "";
				String marketName = "";
				if(line.startsWith("00") || line.startsWith("09")) continue;
								
				if(line.startsWith("01")) {
					companyName = line.substring(7,66).stripTrailing();
					marketName = line.substring(67,78).stripTrailing();
				}
				else if(line.startsWith("02")) {
					Security sec = new Security();
										
					sec.setSymbol(line.substring(3,14).strip());
					sec.setIsin(line.substring(82,93).strip());
					sec.setMarketName(marketName);
					sec.setDescription(companyName);
					
					SecurityInfo secInfo = new SecurityInfo();					
					MarketTypes marketType = MarketTypes.getByValue(Integer.parseInt(line.substring(109, 111).strip()));
					secInfo.setMarketType(marketType);
					secInfo.setExpirationDate(LocalDate.parse(line.substring(144,153), dateTimeFormatter));
					secInfo.setStrikePrice(Double.parseDouble(line.substring(154,171))/10_000_000);
					secInfo.setOptionStyle(OptionStyles.getByValue(line.substring(172,172)));
					
					sec.setSecurityInfoId(secInfo);
					
					securities.put(sec.getSymbol(), sec);
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	private String removeWhite(String txt) {
		if(txt!=null){
			txt = txt.replaceAll("\\t|(\\s+){2}", "");
		}
		return txt;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Properties properties = new Properties();
		properties.put("titulos_negociaveis", "");
						

	}

}
