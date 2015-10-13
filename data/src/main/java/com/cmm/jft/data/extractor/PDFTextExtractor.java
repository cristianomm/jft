/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

import com.cmm.logging.Logging;

/**
 * Extrai o texto contido de todas as paginas do PDF...
 * 
 * <p><code>PDFTextExtractor.java</code></p>
 * @author Cristiano
 * @version 06/04/2015 20:58:14
 *
 */
public class PDFTextExtractor implements Extractor {

	private String fileName;
	private PDDocument pdDocument;
	private PDFTextStripper stripper;


	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#config(java.util.Properties)
	 */
	@Override
	public boolean config(Properties properties) {

		boolean ret = false;

		try{

			if(!Extractor.checkKeyWords(properties, new String[]{"filename"})){
				throw new Exception(); 
			}
			
			this.fileName = properties.getProperty("filename");
			
			File pdfFile = new File(fileName);
			PDFParser parser = new PDFParser(new FileInputStream(pdfFile));
			parser.parse();

			pdDocument = PDDocument.load(pdfFile);

			stripper = new PDFTextStripper();
			stripper.setSortByPosition(true);

		}catch(IOException e){
			Logging.getInstance().log(getClass(), "Erro ao realizar configuracao", Level.ERROR);
		} catch(Exception e){
			Logging.getInstance().log(getClass(), "Erro ao realizar configuracao", Level.ERROR);
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#extract()
	 */
	@Override
	public List<Extractable> extract() {
		List<Extractable> extr = new ArrayList<Extractable>();

		try {
			for(int page=1; page<pdDocument.getNumberOfPages(); page++){
				stripper.setStartPage(page);
				stripper.setEndPage(page);
				String t = stripper.getText(pdDocument);
				if(t!=null)	extr.add(new Text(t));
			}
		} catch (Exception e) {
			Logging.getInstance().log(getClass(), "Erro ao extrair", Level.ERROR);
		}

		return extr;
	}
	
	
	public static void main(String[] args){
		
		PDFTextExtractor pdext = new PDFTextExtractor();
		
		String path = "D:\\Development\\git\\jft\\doc\\process\\bmfbovespa\\";;//"C:/Disco/Users/Cristiano M Martins/Google Drive/Documentos/Notas de Corretagem/";
		String nm = (path + "EntryPointErrorCodes.pdf"/*"12560 - Bovespa.pdf"*/);
		
		Properties p = new Properties();
		p.put("filename", nm);
		pdext.config(p);
		List<Extractable> exts = pdext.extract();
		for (int i = 0; i < exts.size(); i++) {
			System.out.println("PAGE:");
			System.out.println(((Text)exts.get(i)).getText());
		}
		
	}
	
}
