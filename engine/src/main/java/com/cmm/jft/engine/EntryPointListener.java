/**
 * 
 */
package com.cmm.jft.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import quickfix.Acceptor;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.LogFactory;
import quickfix.MessageFactory;
import quickfix.MessageStoreFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;
import quickfix.ThreadedSocketAcceptor;
import quickfix.ThreadedSocketInitiator;

/**
 * <p><code>EntryPointListener.java</code></p>
 * @author Cristiano
 * @version 13 de jul de 2015 00:05:40
 *
 */
public class EntryPointListener {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new EntryPointListener().runEngine();
	}



	private void runEngine(){
		try{
			// FooApplication is your class that implements the Application interface
			Application application = new EntryPoint();

			SessionSettings settings = new SessionSettings(new FileInputStream(""));
			MessageStoreFactory storeFactory = new FileStoreFactory(settings);
			LogFactory logFactory = new FileLogFactory(settings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			
			ThreadedSocketInitiator initiator = 
					new ThreadedSocketInitiator(application, storeFactory, settings, logFactory, messageFactory);
			
			
			
			ThreadedSocketAcceptor acceptor = new ThreadedSocketAcceptor(application, storeFactory, settings, logFactory, messageFactory);
			
			
			acceptor.start();
			//while( condition == true ) { do something; }
			acceptor.stop();
			
		}catch(ConfigError | FileNotFoundException e){

		}

	}

}
