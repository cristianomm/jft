/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.vo.Extractable;

/**
 * <p><code>CVMCiasExtractor.java</code></p>
 * @author Cristiano M Martins
 * @version 02/03/2015 15:14:52
 *
 */
public class CVMCiasExtractor implements Extractor {

	private int timeout;
	private String urlBase;

	private static final char[] alf = { 
			'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 
			'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 
			'y', 'z', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' 
	};


	public static void main(String[] args){

		Properties pp = new Properties();
		pp.put("timeout", 15000);
		pp.put("urlbase", "http://cvmweb.cvm.gov.br/SWB/Sistemas/SCW/CPublica/CiaAb/FormBuscaCiaAbOrdAlf.aspx?LetraInicial=");

		CVMCiasExtractor ex = new CVMCiasExtractor();
		ex.config(pp);
		List<Extractable> s = ex.extract();
		System.out.println(s.size());
		for (Extractable ext : s) {
			System.out.println(((CVMCia)ext).toCSV());
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
				//"http://cvmweb.cvm.gov.br/SWB/Sistemas/SCW/CPublica/CiaAb/FormBuscaCiaAbOrdAlf.aspx?LetraInicial=";
				timeout = (int) properties.get("timeout");
				urlBase = properties.getProperty("urlbase");
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
		for(char c:alf){
			try {
				List<Extractable> aux = extractLetter(c, new URL(urlBase+c), timeout);
				if(!aux.isEmpty())comps.addAll(aux);
			} catch (MalformedURLException e) {

			}
		}

		return comps;
	}

	private List<Extractable> extractLetter(char param, URL url, int timeoutMillis) {

		List<Extractable> extracted = new ArrayList<Extractable>();
		try {

			Document doc = Jsoup.parse(url, timeoutMillis);

			Element table = doc.getElementById("dlCiasCdCVM");
			if (Objects.nonNull(table)) {

				for (Element trs : table.getElementsByTag("tr")) {
					int col = 0;
					String[] cols = new String[5];
					for (Element r : trs.getElementsByTag("td")) {
						cols[col++] = r.text();
					}

					if(!(cols[0].equalsIgnoreCase("cnpj") || cols[4].equalsIgnoreCase("situação registro"))){

						LocalDate statusDate = null;
						GeneralStatus status = null;

						Matcher m = Pattern.compile("([\\d]+/[\\d]+/[\\d]+)").matcher(cols[4]);
						if(m.find()){
							statusDate = DateTimeFormatter.ofPattern("dd/MM/yyyy").parse(m.group(), LocalDate::from);
							cols[4] = cols[4].split(" em ")[0];
						}

						switch(cols[4]){
						case "Cancelado":
							status = GeneralStatus.CANCELED;
							break;
						case "Concedido":
							status = GeneralStatus.ACTIVE;
							break;
						}

						CVMCia cia = new CVMCia(cols[0], cols[1], cols[3], statusDate, status);
						extracted.add(cia);
					}
				}

			}

		} catch(SocketTimeoutException e){
			//Logging.getInstance().log(this.getClass(), "Too short timeout:", e, Level.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return extracted;

	}

}
