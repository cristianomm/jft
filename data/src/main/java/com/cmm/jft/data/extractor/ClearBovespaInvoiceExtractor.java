/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;

import com.cmm.jft.vo.Extractable;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>ClearBovespaInvoiceExtractor.java</code>
 * </p>
 * 
 * @author Cristiano
 * @version 25/03/2015 01:08:12
 *
 */
public class ClearBovespaInvoiceExtractor extends ClearInvoiceExtractor {

	private static String invoice = "([\\d]{5}$)";
	private static String value = "(" + valuePrefixPtrn + valuePtrn + ")";

	private static String sellTotal = value + "(Vendas � vista)";
	private static String buyTotal = value + "(Compras � vista)";
	private static String netOp = value + "(Valor l�quido das opera��es)";
	private static String sumOp = value + "(Valor das opera��es)";
	private static String cblcTotal = value + "(Total[\\s]+CBLC)";
	private static String exchangeTotal = value + "([\\s|D]*Total Bovespa / Soma)";

	private static String liquidationTax = value + "(Taxa de liquida��o)";
	private static String registerTax = value + "(Taxa de Registro)";
	private static String fee = value + "(Emolumentos)";
	private static String brokerage = value + "(Corretagem)";
	private static String iss = value + "(ISS)";
	
	private static String swingTradeBase = "(I.R.R.F. s/ opera��es,[\\s]+base[\\s]+)" + value;
	private static String dayTradeBase = "(IRRF Day-Trade: Base[\\s]?)" + value;

	private static String swingTradeIRRF = "^" + valuePtrn + "(I.R.R.F.)";
	private static String dayTradeIRRF = "(Proje��o[\\s]?)" + value;
	private static String others = value + "(Outras)";
	private static String sumExpense = value + "(Total corretagem / Despesas)";

	
	private Invoice extractValues(String text) {
		Invoice inv = new Invoice();
		inv.invoice = extractValue(text, invoice, 0);
		inv.date = extractValue(text, date, 0);
		inv.sellTotal = extractValue(text, sellTotal, 1);
		inv.buyTotal = extractValue(text, buyTotal, 1);
		inv.netOp = extractValue(text, netOp, 1);
		inv.sumOp = extractValue(text, sumOp, 1);
		inv.cblcTotal = extractValue(text, cblcTotal, 1);
		inv.exchangeTotal = extractValue(text, exchangeTotal, 1);
		inv.liquidationTax = extractValue(text, liquidationTax, 1);
		inv.registerTax = extractValue(text, registerTax, 1);
		inv.fee = extractValue(text, fee, 1);
		inv.brokerage = extractValue(text, brokerage, 1);
		inv.iss = extractValue(text, iss, 1);
		
		inv.swingTradeBase = extractValue(text, swingTradeBase, 2);
		inv.dayTradeBase = extractValue(text, dayTradeBase, 2);
		inv.swingTradeIRRF = extractValue(text, swingTradeIRRF, 1);
		inv.dayTradeIRRF = extractValue(text, dayTradeIRRF, 2);
		
		inv.others = extractValue(text, others, 1);
		inv.sumExpense = extractValue(text, sumExpense, 1);
		return inv;
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
				invs.add(extractValues(((Text) exts.get(i)).getText()));
			}
		} catch (Exception e) {
			Logging.getInstance().log(getClass(), "Error extracting values", Level.ERROR);
		}

		return invs;
	}

	public static void main(String[] args) throws IOException{
		
		String path = "C:/Disco/Users/Cristiano M Martins/Google Drive/Documentos/Notas de Corretagem/";
		String nm = (path + "12560 - Bovespa.pdf");
		
		Properties p = new Properties();
		p.put("filename", nm);
		ClearBovespaInvoiceExtractor extractor = new ClearBovespaInvoiceExtractor();
		extractor.config(p);
		FileOutputStream fos = new FileOutputStream(new File("Bovespa.csv"));
		StringBuffer sb = new StringBuffer(1024);
		sb.append("invoice;date;sellTotal;buyTotal;sumOp;netOp;liquidationTax;registerTax;cblcTotal;fee;exchangeTotal;brokerage;iss;dayTradeBase;dayTradeIRRF;swingTradeBase;swingTradeIRRF;others;sumExpense\n");
		int headerLen = sb.length();
		for(Extractable ext : extractor.extract()){
			Invoice inv = (Invoice)ext;
			
			sb.append(inv.invoice).append(";");
			sb.append(inv.date).append(";");
			sb.append(inv.sellTotal).append(";");
			sb.append(inv.buyTotal).append(";");
			sb.append(inv.sumOp).append(";");
			
			sb.append(inv.netOp).append(";");
			sb.append(inv.liquidationTax).append(";");
			sb.append(inv.registerTax).append(";");
			sb.append(inv.cblcTotal).append(";");
			
			sb.append(inv.fee).append(";");
			sb.append(inv.exchangeTotal).append(";");
			
			sb.append(inv.brokerage).append(";");
			sb.append(inv.iss).append(";");
			sb.append(inv.dayTradeBase).append(";");
			sb.append(inv.dayTradeIRRF).append(";");
			sb.append(inv.swingTradeBase).append(";");
			sb.append(inv.swingTradeIRRF).append(";");
			sb.append(inv.others).append(";");
			sb.append(inv.sumExpense).append("\n");
		}
		fos.write(sb.toString().getBytes());
		fos.close();
	}
	
	
}
