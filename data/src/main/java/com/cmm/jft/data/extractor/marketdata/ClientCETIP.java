package com.cmm.jft.data.extractor.marketdata;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import edu.emory.mathcs.backport.java.util.Arrays;

public class ClientCETIP {

	private static String OUTPUT_PATH = "D:\\Programs\\DataBase\\FTP\\CETIP\\";
	private static String DI_PATH = "IndiceDI/";
	private static String AVG_CDI_PATH = "MediaCDI/";
	private static String FTP_CETIP = "ftp.cetip.com.br";
	
	private class MarketIndex {
		public LocalDate date;
		public double value;
		public double average;
		
	}
	
	
	public static void main(String[] args) {
		
		
		new ClientCETIP().DIIndex();
		
	}

	public void DIIndex() {

		FTPClient ftp = new FTPClient();
		FTPClientConfig config = new FTPClientConfig();
		//config.setXXX(YYY); // change required options
		// for example config.setServerTimeZoneId("Pacific/Pitcairn")
		ftp.configure(config );
		boolean error = false;
		try {
			
			new File(OUTPUT_PATH + DI_PATH).mkdirs();
			new File(OUTPUT_PATH + AVG_CDI_PATH).mkdirs();			
			
			int reply;
			String server = FTP_CETIP;
			ftp.connect(FTP_CETIP, 21);
			ftp.enterLocalPassiveMode();
			ftp.login("anonymous", "");
			System.out.println("Connected to " + server + ".");
			System.out.print(ftp.getReplyString());

			// After connection attempt, you should check the reply code to verify success.
			reply = ftp.getReplyCode();

			if(!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				System.err.println("FTP server refused connection.");
				System.exit(1);
			}
			// transfer files
			ftp.changeWorkingDirectory(DI_PATH);
			
			FTPFile[] files = ftp.listFiles();
			System.out.println("Starting dowload of " + files.length + " files." );
			
			for(FTPFile file : files) {
				MarketIndex quote = new MarketIndex();
				
				System.out.println("Dowloading file " + file.getName() + "...");
				ftp.retrieveFile(
						file.getName(), 
						new FileOutputStream(new File(OUTPUT_PATH + DI_PATH + file.getName())));
				
				System.out.println("file " + file.getName() + " done.");
			}
			
			ftp.logout();
		} catch(IOException e) {
			error = true;
			e.printStackTrace();
		} finally {
			if(ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch(IOException ioe) {
					// do nothing
				}
			}
			System.exit(error ? 1 : 0);
		}

	}

	public void CDIAverage() {



	}


}
