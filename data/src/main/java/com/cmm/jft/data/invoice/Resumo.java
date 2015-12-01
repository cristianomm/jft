/**
 * 
 */
package com.cmm.jft.data.invoice;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

//import net.htmlparser.jericho.Element;
//import net.htmlparser.jericho.HTMLElementName;
//import net.htmlparser.jericho.MasonTagTypes;
//import net.htmlparser.jericho.MicrosoftTagTypes;
//import net.htmlparser.jericho.PHPTagTypes;
//import net.htmlparser.jericho.Source;

/**
 * <p>
 * <code>Resumo.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 04/04/2014 16:15:26
 *
 */
public class Resumo {

	/*
	 * Quadro Geral 14/11/2013 Day Trade Volume Bovespa R$ 24.168,00 Volume BM&F
	 * 2 contratos Número de ordens 6 Lucro/Prejuízo R$ 78,00 Corretagem R$
	 * 17,60 Ativos Operados 2
	 */
	private Date date;
	private double vBovespa;
	private double vBMF;
	private int orders;
	private double profit;
	private double comission;

	public Resumo() {

	}

	public void readResumo(String fileName) {

//		try {
//			MicrosoftTagTypes.register();
//			PHPTagTypes.register();
//			PHPTagTypes.PHP_SHORT.deregister();
//			MasonTagTypes.register();
//
//			File f = new File(fileName);
//			Source source = new Source(f.toURI().toURL());
//			source.fullSequentialParse();
//
//			for (Element tbl : source.getAllElements(HTMLElementName.TABLE)) {
//				List<Element> spans = tbl.getAllElements(HTMLElementName.SPAN);
//				String date = getText(spans.get(3));
//				String vBovespa = getText(spans.get(6));
//				String vBMF = getText(spans.get(8));
//				String orders = getText(spans.get(10));
//				String profit = getText(spans.get(12));
//				String comission = getText(spans.get(14));
//				String securities = getText(spans.get(16));
//				System.out.println(date + ";" + vBovespa + ";" + vBMF + ";"
//						+ orders + ";" + profit + ";" + comission + ";"
//						+ securities);
//
//				break;
//			}
//
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	private String getText(Element element) {
//		String txt = element.getContent().getTextExtractor().toString();
//		return txt.replace("contratos", "").replace(" ", "").replace("R$", "")
//				.replace(".", "").replace(",", ".");
//	}
//
//	public static void main(String[] args) {
//		Resumo r = new Resumo();
//		File dir = new File(
//				"C:\\Disco\\Workspaces\\JFT\\jft_data\\file\\relatoriosDT\\");
//		for (File f : dir.listFiles()) {
//			if (f.isFile()) {
//				r.readResumo(f.getAbsolutePath());
//			}
//		}
	}

}
