/**
 * 
 */
package com.cmm.jft.data.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;

/**
 * <p>
 * <code>CSV.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 18/09/2013 15:16:15
 *
 */
public class CSV {

	private boolean skipHeader;
	private String delimiter;
	private String lineDelimiter;
	private String headerPrefix;
	private String footerPrefix;
	private Scanner scanner;
	private String fileName;
	private String encoding;
	private HashMap<String, ArrayList<String[]>> columns;

	public CSV(String fileName, String delimiter) {
		this.footerPrefix = "";
		this.headerPrefix = "";
		this.lineDelimiter = "\n";
		this.delimiter = delimiter;
		this.fileName = fileName;
		startScanner("UTF8");
	}
	
	public CSV(String fileName, String delimiter, String headerPrefix) {
		this.footerPrefix = "";
		this.headerPrefix = headerPrefix;
		this.lineDelimiter = "\n";
		this.delimiter = delimiter;
		this.fileName = fileName;
		startScanner("UTF8");
	}

	public CSV(String fileName, String delimiter, String headerPrefix,
			String footerPrefix) {
		this.headerPrefix = headerPrefix;
		this.footerPrefix = footerPrefix;
		this.lineDelimiter = "\n";
		this.delimiter = delimiter;
		this.fileName = fileName;
		startScanner("UTF8");
	}

	public CSV(String fileName, String delimiter, String headerPrefix,
			String footerPrefix, String lineDelimiter) {
		this.headerPrefix = headerPrefix;
		this.footerPrefix = footerPrefix;
		this.lineDelimiter = lineDelimiter;
		this.delimiter = delimiter;
		this.fileName = fileName;
		startScanner("UTF8");
	}

	private void startScanner(String encoding) {
		try {
			this.scanner = new Scanner(new FileInputStream(fileName), encoding);
			this.scanner.useDelimiter(lineDelimiter);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
	}

	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return this.delimiter;
	}

	/**
	 * @param delimiter
	 *            the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * @return the lineDelimiter
	 */
	public String getLineDelimiter() {
		return this.lineDelimiter;
	}

	/**
	 * @param lineDelimiter
	 *            the lineDelimiter to set
	 */
	public void setLineDelimiter(String lineDelimiter) {
		this.lineDelimiter = lineDelimiter;
	}

	/**
	 * @return the headerPrefix
	 */
	public String getHeaderPrefix() {
		return this.headerPrefix;
	}

	/**
	 * @param headerPrefix
	 *            the headerPrefix to set
	 */
	public void setHeaderPrefix(String headerPrefix) {
		this.headerPrefix = headerPrefix;
	}

	/**
	 * @return the footerPrefix
	 */
	public String getFooterPrefix() {
		return this.footerPrefix;
	}

	/**
	 * @param footerPrefix
	 *            the footerPrefix to set
	 */
	public void setFooterPrefix(String footerPrefix) {
		this.footerPrefix = footerPrefix;
	}
	
	/**
	 * @return the encoding
	 */
	public String getEncoding() {
		return this.encoding;
	}
	
	/**
	 * @param encoding the encoding to set
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
		startScanner(encoding);
	}

	/**
	 * Le todo o arquivo e retorna um ArrayList contendo todo o seu conteudo
	 * 
	 * @return ArrayList com o conteudo do arquivo
	 */
	public ArrayList<String[]> readFile() {
		this.delimiter = delimiter;
		ArrayList<String[]> ret = new ArrayList<String[]>();

		try {
			if (lineDelimiter != null && !lineDelimiter.isEmpty()) {
				scanner.useDelimiter(lineDelimiter);
			}

			while (scanner.hasNext()) {
				String line = scanner.next();

				if (line.startsWith(headerPrefix) && headerPrefix != null
						&& !headerPrefix.equalsIgnoreCase("")) {
					line = scanner.next();
				}
				if (line.startsWith(footerPrefix) && footerPrefix != null
						&& !footerPrefix.equalsIgnoreCase("")) {
					line = scanner.next();
				}

				line = line.replaceAll("\r", "").replaceAll("\n", "")
						.replaceAll(";", "; ");
				String[] l = line.split(delimiter);
				for (int i = 0; i < l.length; i++) {
					l[i] = removeWhite(l[i]);
				}

				ret.add(l);

			}

		} catch (Exception e) {
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}

		return ret;
	}

	/**
	 * Le uma linha do CSV
	 * 
	 * @return array de String contendo todas colunas da linha ou null, caso ja
	 *         tenha chegado ao fim do arquivo
	 */
	public String[] readLine() {
		String[] ret = null;
		try {
			if (scanner.hasNext()) {
				String line = scanner.next();

				if (line.startsWith(headerPrefix) && headerPrefix != null
						&& !headerPrefix.equalsIgnoreCase("")) {
					line = scanner.next();
				}
				if (line.startsWith(footerPrefix) && footerPrefix != null
						&& !footerPrefix.equalsIgnoreCase("")) {
					line = scanner.next();
				}

				line = line.replaceAll("\r", "").replaceAll("\n", "");
				ret = line.split(delimiter);

				for (int i = 0; i < ret.length; i++) {
					ret[i] = removeWhite(ret[i]);
				}

			}
		} catch (Exception e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
		return ret;
	}

	public boolean hasNext() {
		return scanner.hasNext();
	}

	public void close() {
		scanner.close();
	}

	private String removeWhite(String txt) {
		try {
			while (txt.charAt(txt.length() - 1) == ' ') {
				txt = txt.substring(0, txt.length() - 1);
			}
		} catch (StringIndexOutOfBoundsException e) {
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
		return txt;
	}

}
