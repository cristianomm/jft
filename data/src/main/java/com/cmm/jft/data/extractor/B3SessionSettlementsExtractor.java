/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.cmm.jft.vo.Extractable;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

/**
 * 
 * <p>
 * <code>B3SessionSettlementsExtractor.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 18, 2019
 *
 */
public class B3SessionSettlementsExtractor implements Extractor {

	private class Settlement implements Extractable {
		Date date;
		String code;
		String description;
		String expiration;
		double lastPrice;
		double price;
		double variation;
		double settlementContractPrice;
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "Settlement [" + (code != null ? "code=" + code + ", " : "")
					+ (description != null ? "description=" + description + ", " : "")
					+ (expiration != null ? "expiration=" + expiration + ", " : "") + "lastPrice=" + lastPrice
					+ ", price=" + price + ", variation=" + variation + ", settlementContractPrice="
					+ settlementContractPrice + "]";
		}
		
		public String toCSV() {
			return String.format("%tF;%s;%s;%s;%f;%f;%f;%f", 
					date, code, description, expiration, lastPrice, price, variation, settlementContractPrice);
		}
		
	}
	
	private int timeout;
	private String urlBase;
	private List<LocalDate> dateList;

	public static void main(String[] args){

		Properties pp = new Properties();
		pp.put("timeout", 15000);
		pp.put("urlbase", "http://www2.bmf.com.br/pages/portal/bmfbovespa/lumis/lum-ajustes-do-pregao-ptBR.asp");
		pp.put("dates", "all");		
		
		B3SessionSettlementsExtractor ex = new B3SessionSettlementsExtractor();
		ex.config(pp);
		
		List<Extractable> extracted = ex.extract();
		System.out.println(extracted.size());
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream("Settlements.csv");
			
			for(Extractable e : extracted) {
				fos.write((((Settlement)e).toCSV()+"\n").getBytes());
			}
					
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		} finally {
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#config(java.util.Properties)
	 */
	@Override
	public boolean config(Properties properties) {

		boolean ret = false;
		try{
			if(properties.containsKey("timeout") && properties.containsKey("urlbase")){
				timeout = (int) properties.get("timeout");
				urlBase = properties.getProperty("urlbase");			
				dateList = new ArrayList<>();
				
				String dateParam = properties.getProperty("dates");
				if(dateParam.equalsIgnoreCase("all")) {
					LocalDate.of(1990, 01, 01)
					. datesUntil(LocalDate.now())
					.filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY)
					.forEachOrdered(d -> dateList.add(d));
				}
				else {
					LocalDate.now()
					. datesUntil(LocalDate.now().plusDays(1))
					.filter(d -> d.getDayOfWeek() != DayOfWeek.SATURDAY && d.getDayOfWeek() != DayOfWeek.SUNDAY)
					.forEachOrdered(d -> dateList.add(d));
				}
				
				ret = true;
			}
		}catch(NumberFormatException e){
			ret = false;
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#extract()
	 */
	@Override
	public List<Extractable> extract() {

		List<Extractable> comps = new ArrayList<Extractable>();	

		for(LocalDate date : dateList) {
			try {
				System.out.println("Extracting " + date.toString());
				String pageContent = getSettlementPage(urlBase, date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

				comps.addAll(extractValues(pageContent));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return comps;
	}

	private List<Extractable> extractValues(String pageContent) {

		List<Extractable> extracted = new ArrayList<Extractable>();
		try {
			HashMap<String, List<Settlement>> ajustes = new HashMap<>();
			Document doc = Jsoup.parse(pageContent);

			Date date = null;
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			Element input = doc.getElementById("dData1");
			for(Element e : input.getAllElements()) {
				try{
					date = dateFormat.parse(e.attr("value"));
				}catch(ParseException ex) {
					//ex.printStackTrace();
				}
			}
						
			Element table = doc.getElementById("tblDadosAjustes");
			if (Objects.nonNull(table)) {
				String lastCode = "";
				String lastDscr = "";
				for (Element trs : table.getElementsByTag("tr")) {
					int col = 0;
					String[] cols = new String[6];
					for (Element r : trs.getElementsByTag("td")) {
						cols[col++] = r.text();
					}
					
					if(cols[0] == null) continue;
					
					if(!cols[0].isBlank()) {
						lastCode = cols[0].trim().split("-")[0].trim();
						lastDscr = cols[0].split("-")[1].trim();
						ajustes.put(lastCode, new ArrayList<Settlement>());
					} 
					
					Settlement settlement = new Settlement();
					settlement.date = date;
					settlement.code = lastCode;
					settlement.description = lastDscr;
					settlement.expiration = cols[1];
					settlement.lastPrice = Double.parseDouble(cols[2].replace(".", "").replace(",", "."));
					settlement.price = Double.parseDouble(cols[3].replace(".", "").replace(",", "."));
					settlement.variation = Double.parseDouble(cols[4].replace(".", "").replace(",", "."));
					settlement.settlementContractPrice = Double.parseDouble(cols[5].replace(".", "").replace(",", "."));
										
					//ajustes.get(lastCode).add(settlement);
					
					extracted.add(settlement);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return extracted;
	}


	private String getSettlementPage(String path, String date) {

		StringBuffer pageContent = new StringBuffer();
		try {
			String urlParameters = "dData1=" + date;
			URL url = new URL(path);
			URLConnection conn = url.openConnection();

			conn.setDoOutput(true);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

			writer.write(urlParameters);
			writer.flush();

			String line;
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "iso-8859-1"));
			
			while ((line = reader.readLine()) != null) {
				pageContent.append(line);
			}
			
			writer.close();
			reader.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		return pageContent.toString();
	}
	
}
