/**
 * 
 */
package com.cmm.jft.engine;

import java.util.Scanner;
import java.util.TreeMap;

/**
 * <p>
 * <code>ErrorCodes.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 25/08/2017 02:58:59
 *
 */
public class ErrorCodes {

	private static ErrorCodes instance;
	private TreeMap<Integer, String> messages;

	/**
	 * 
	 */
	private ErrorCodes() {
		this.messages = new TreeMap<>();
		loadMessages();
	}

	/**
	 * @return the instance
	 */
	public synchronized static ErrorCodes getInstance() {
		if (instance == null) {
			instance = new ErrorCodes();
		}
		return instance;
	}

	public String getMessage(int errorCode) {
		return messages.get(errorCode);
	}

	private void loadMessages() {

		Scanner sc = null;
		try {
			sc = new Scanner(
					Thread.currentThread().getContextClassLoader().getResourceAsStream("EntryPointErrorCodes.csv"));
			sc.nextLine();
			while (sc.hasNext()) {
				String line = sc.nextLine();
				String[] dec = line.split(";");
				messages.put(Integer.parseInt(dec[0]), dec[1]);
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			if (sc != null) {
				sc.close();
			}
		}

	}

}
