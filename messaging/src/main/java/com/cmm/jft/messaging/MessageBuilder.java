/**
 * 
 */
package com.cmm.jft.messaging;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.DefaultMessageFactory;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.MessageFactory;
import quickfix.MessageUtils;
import quickfix.field.CheckSum;

/**
 * <p><code>MessageBuilder.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 5, 2015 1:03:38 AM
 *
 */
public class MessageBuilder {

	private MessageFactory factory;
	private DataDictionary dictionary;
	
	
	public MessageBuilder(String dictionaryPath){
		try {
			dictionary = new DataDictionary(dictionaryPath);
			factory = new DefaultMessageFactory();
		} catch (ConfigError e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param args
	 */
	public Message build(String message) {
		Message msg = null;
		try {
			msg = MessageUtils.parse(factory, dictionary, message);
		} catch (InvalidMessage e) {
			e.printStackTrace();
		}
		return msg;
	}

}
