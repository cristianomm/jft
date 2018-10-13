/**
 * 
 */
package com.cmm.jft.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import com.cmm.jft.data.dde.QuoteData;

/**
 * <p>
 * <code>FileStore.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 13/09/2013 14:51:45
 *
 */
public class FileStore implements Storer {

	private String fileName;
	private FileOutputStream fos;

	/**
	 * @throws FileNotFoundException
	 * 
	 */
	public FileStore(String fileName) {
		this.fileName = fileName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.Storer#store(com.cmm.jft.data.QuoteData)
	 */
	public void store(QuoteData quoteData) {
		try {
			fos.write(quoteData.toString().getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.Storer#init()
	 */
	public void init() {
		try {
			this.fos = new FileOutputStream(new File(System.currentTimeMillis()
					+ "_" + fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.Storer#close()
	 */
	public void close() {
		try {
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
