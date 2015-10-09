/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;

import com.cmm.jft.vo.Extractable;
import com.cmm.logging.Logging;

/**
 * <p><code>ClearBMFInvoiceExtractor.java</code></p>
 * @author Cristiano
 * @version 26/04/2015 02:36:14
 *
 */
public class ClearBMFInvoiceExtractor extends ClearInvoiceExtractor {
	
	private static String value = "(" + valuePtrn + valueSufixPtrn + ")";
		
	//Venda dispon�vel Compra dispon�vel Venda op��es Compra op��es Valor dos neg�cios
	//16,000,00 D0,000,000,00
	//IRRF  IRRF Day Trade ( Proje��o ) Taxa operacional Taxas BM&F ( emol+f.gar)Taxa registro BM&F
	//0,00 0,00 D5,00 0,10 0,08
	//+ Outros Custos I.S.S Ajuste day trade Total das despesasAjuste de posi��o
	//0,250,00 16,00 DC D5,180,00
	//Outros  IRRF  Corretagem Total Conta Investimento Total Conta Normal Total l�quido (#) Total l�quido da nota
	//0,00 0,00 C0,00 D21,18 D D21,18 21,43
	//private static String vals = "([\\d|\\.]*,\\d{2,4}(\\s[CD]|\\s)?)"; //([\\d|.]*,[\\d]{2,4}[\\s|C|D]*){4,6}";
	private static String vals = value + "{5,6}";
	private static String vals_1 = "(Valor dos neg�cios[\\n|\\r]*)" + value + "{5}";
	private static String vals_2 = "(Taxas BM\\&F \\( emol\\+f\\.gar\\)[\\n|\\r]*)" + value + "{5}";
	private static String vals_3 = "(Total das despesas[\\n|\\r]*)" + value + "{5}";
	private static String vals_4 = "(Total l�quido da nota[\\n|\\r]*)" + value + "{6}";
	
	private Invoice extractValues(String text) {
		Invoice inv = new Invoice();
		inv.date = extractValue(text, date, 0);
		inv.invoice = extractValue(text, invoice, 0);
		
		//1
		String v1 = extractValue(text, vals, 1);
		inv.sellTotal = clearString(extractValue(v1, value, 1));
		inv.buyTotal = clearString(extractValue(v1, value, 2));
		inv.optionSell = clearString(extractValue(v1, value, 3));
		inv.optionBuy = clearString(extractValue(v1, value, 4));
		inv.exchangeTotal = clearString(extractValue(v1, value, 5));
		
		//2
		String v2 = extractValue(text, vals, 2);
		inv.swingTradeIRRF = clearString(extractValue(v2, value, 1));
		inv.dayTradeIRRF = clearString(extractValue(v2, value, 2));
		inv.brokerage = clearString(extractValue(v2, value, 3));
		inv.registerTax = clearString(extractValue(v2, value, 4));
		inv.fee = clearString(extractValue(v2, value, 5));
		
		//3
		String v3 = extractValue(text, vals, 3);
		inv.otherCost = clearString(extractValue(v3, value, 1));
		inv.iss = clearString(extractValue(v3, value, 2));
		inv.swingAdjust = clearString(extractValue(v3, value, 3));
		inv.dayTradeAdjust = clearString(extractValue(v3, value, 4));
		inv.sumExpense = clearString(extractValue(v3, value, 5));
		
		//4
		String v4 = extractValue(text, vals, 4);
		inv.others = clearString(extractValue(v4, value, 1));
		inv.brokerageIRRF = clearString(extractValue(v4, value, 2));
		inv.investmentAccTotal = clearString(extractValue(v4, value, 3));
		inv.total = clearString(extractValue(v4, value, 4));
		inv.netOp = clearString(extractValue(v4, value, 5));
		inv.invoiceTotal = clearString(extractValue(v4, value, 6));
		
		return inv;
	}
	
	private String clearString(String str){
		String ret = str;
		if(ret.length()>0){
    		ret = ret.replaceAll("\n", "").replaceAll("\r", "");
    		ret = ret.trim().replace("C", "").replace("R$", "").replace(" ", "");
    		if(ret.contains("D")){
    			ret = "-" + ret.replace("D", "");
    		}	
    	}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.extractor.Extractor#extract()
	 */
	@Override
	public List<Extractable> extract() {
		List<Extractable> invs = new ArrayList<Extractable>();
		try {
			PDFTextExtractor pdext = new PDFTextExtractor();
			
			Properties p = new Properties();
			p.put("filename", fileName);
			pdext.config(p);
			List<Extractable> exts = pdext.extract();
			for (int i = 0; i < exts.size(); i++) {
				//System.out.println("\n");
				//System.out.println(">>>>" + ((Text) exts.get(i)).getText());
				if(!((Text) exts.get(i)).getText().isEmpty()){
					invs.add(extractValues(((Text) exts.get(i)).getText()));
				}
			}
		} catch (Exception e) {
			Logging.getInstance().log(getClass(), "Error extracting values", Level.ERROR);
		}

		return invs;
	}

	public static void main(String[] args) throws IOException{
		/*
		String s = "Venda dispon�vel Compra dispon�vel Venda op��es Compra op��es Valor dos neg�cios\r\n0,00 0,00 0,00 0,00 14,00 C\r\n";
		Pattern pd = Pattern.compile(vals_1);
		
		Matcher m = pd.matcher(s);
		//System.out.println(pd.pattern());
		if(m.find()){
			Matcher ma = Pattern.compile(value).matcher(s);
			int i=0;
			while(ma.find()){
				System.out.println(i++ + ": " + ma.group());
			}
		}
		System.exit(0);
		*/
		
		String path = "C:/Disco/Users/Cristiano M Martins/Google Drive/Documentos/Notas de Corretagem/";
		String nm = (path + "12560 - BM&F.pdf");
		
		Properties p = new Properties();
		p.put("filename", nm);
		ClearBMFInvoiceExtractor extractor = new ClearBMFInvoiceExtractor();
		extractor.config(p);
		FileOutputStream fos = new FileOutputStream(new File("BMF.csv"));
		StringBuffer sb = new StringBuffer(1024);
		sb.append("invoice;date;sellTotal;buyTotal;optionSell;optionBuy;exchangeTotal;swingTradeIRRF;dayTradeIRRF;brokerage;registerTax;fee;otherCost;iss;swingAdjust;dayTradeAdjust;sumExpense;others;brokerageIRRF;investmentAccTotal;total;netOp;invoiceTotal\n");
		
		for(Extractable ext : extractor.extract()){
			Invoice inv = (Invoice)ext;
			
			sb.append(inv.invoice).append(";");
			sb.append(inv.date).append(";");
			sb.append(inv.sellTotal).append(";");
			sb.append(inv.buyTotal).append(";");
			sb.append(inv.optionSell).append(";");
			sb.append(inv.optionBuy).append(";");
			sb.append(inv.exchangeTotal).append(";");
			
			sb.append(inv.swingTradeIRRF).append(";");
			sb.append(inv.dayTradeIRRF).append(";");
			sb.append(inv.brokerage).append(";");
			sb.append(inv.registerTax).append(";");
			sb.append(inv.fee).append(";");
			
			sb.append(inv.otherCost).append(";");
			sb.append(inv.iss).append(";");
			sb.append(inv.swingAdjust).append(";");
			sb.append(inv.dayTradeAdjust).append(";");
			sb.append(inv.sumExpense).append(";");
			
			sb.append(inv.others).append(";");
			sb.append(inv.brokerageIRRF).append(";");
			sb.append(inv.investmentAccTotal).append(";");
			sb.append(inv.total).append(";");
			sb.append(inv.netOp).append(";");
			sb.append(inv.invoiceTotal).append("\n");
		}
		fos.write(sb.toString().getBytes());
		fos.close();
	}

}
