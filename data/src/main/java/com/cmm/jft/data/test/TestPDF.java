package com.cmm.jft.data.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.apache.pdfbox.util.TextPosition;

import com.cmm.jft.data.extractor.Invoice;


public class TestPDF extends PDFTextStripper{
	
	private static String currPtrn = "(R$\\s)?";
	private static String valuePtrn = "([\\d|.]+,[\\d]{2})";
	private static String valuePrefixPtrn = "([R$|\\s]|[C|D|-])*";
	private static String valueSufixPtrn = "([\\w|\\s|.|ç|ã|õ|à|$|/]*)?";
	
	private static String date = "([\\d]{2}/[\\d]{2}/[\\d]{4})";
	private static String invoice = "([\\d|.]$)";	
	
	private static String value = valuePrefixPtrn + valuePtrn;
	private static String sellTotal =value + "(Vendas à vista)";
	private static String buyTotal = value + "(Compras à vista)";
	private static String netOp = value + "(Valor líquido das operações)";
	private static String sumOp = value + "(Valor das operações)";
	private static String cblcTotal = value + "(Total[\\s]+CBLC)";
	private static String exchangeTotal = value + "([\\s|D]*Total Bovespa / Soma)";
	
	private static String liquidationTax = value + "(Taxa de liquidação)";
	private static String registerTax = value + "(Taxa de Registro)";
	private static String fee = value + "(Emolumentos)";
	private static String brokerage = value + "(Corretagem)";
	private static String iss = value + "(ISS)";
	private static String dayTradeBase = "(IRRF Day-Trade: Base[\\s]?)" + value;
	private static String projection = "(Projeção[\\s]?)"+value;
	private static String others = value + "(Outras)";
	private static String sumExpense = value + "(Total corretagem / Despesas)";
	
	public TestPDF() throws IOException {
		super();
	}

	public static void main(String[] args) throws IOException {
		
		//C:/Disco/Users/Cristiano M Martins/Google Drive/Documentos/Notas de Corretagem/
		String path = "C:/Disco/Users/Cristiano M Martins/Google Drive/Documentos/Notas de Corretagem/";
		File pdfFile = new File(path + "12560 - Bovespa.pdf");
		
		PDFParser parser = new PDFParser(new FileInputStream(pdfFile));
		
		parser.parse();
		COSDocument cosDoc =  parser.getDocument();
		
		PDDocument pdDoc = PDDocument.load(pdfFile);
		
		PDFTextStripper stripper = new PDFTextStripper();
		stripper.setStartPage(1);
		stripper.setEndPage(1);
		TestPDF testPDF = new TestPDF();
		
		String text = stripper.getText(pdDoc);
		System.out.println(text);
		extractValues(text);
		
	}
	
	/**
     * A method provided as an event interface to allow a subclass to perform
     * some specific functionality when text needs to be processed.
     *
     * @param text The text to be processed
     */
	float last=0;
    protected void processTextPosition(TextPosition text )
    {
    	if(last == text.getYDirAdj()){
    		System.out.print(text.getCharacter());
    	}
    	else{
    		System.out.println();
    		last = text.getYDirAdj();
    	}
    	
    }
    
    /**
     * @param text
     */
    private static void extractValues(String text){
    	
    	Invoice inv = new Invoice();
    	
    	inv.date = extract(text, date);
    	inv.invoice = extract(text, invoice);
    	inv.sellTotal = extract(text, sellTotal);
    	inv.buyTotal = extract(text, buyTotal);
    	inv.netOp = extract(text, netOp);
    	inv.sumOp = extract(text, sumOp);
    	inv.cblcTotal = extract(text, cblcTotal);
    	inv.exchangeTotal = extract(text, exchangeTotal);
    	inv.liquidationTax = extract(text, liquidationTax);
    	inv.registerTax = extract(text, registerTax);
    	inv.fee = extract(text, fee);
    	inv.brokerage = extract(text, brokerage);
    	inv.iss = extract(text, iss);
    	//
    	inv.dayTradeBase = extract(text, dayTradeBase);
    //	inv.projection = extract(text, projection);
    	inv.others = extract(text, others);
    	inv.sumExpense = extract(text, sumExpense);
    	
    	System.out.println(inv);
    	
    }
    
    
    private static String extract(String text, String pattern){
    	String ret = "";
    	
    	Pattern p = Pattern.compile(pattern, Pattern.MULTILINE);
    	Matcher m = p.matcher(text);
    	
    	while(m.find()){
    		ret +=(m.group()+" ");
    	}
    	
    	if(ret.length()>0){
    		ret = ret.replaceAll("\n", "");
    		ret = ret.substring(0, ret.length()-1);
    	}
    	return ret;
    }
	
}
