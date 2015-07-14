/**
 * 
 */
package com.cmm.jft.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.cmm.jft.core.services.Service;

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
public class EntryPointListener implements Service {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new EntryPointListener().start();
	}

	
	/* (non-Javadoc)
	 * @see com.cmm.jft.core.services.Service#start()
	 */
	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.core.services.Service#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}
	

	private void initListener(){
		try{
			// FooApplication is your class that implements the Application interface
			Application application = new EntryPoint();

			SessionSettings settings = new SessionSettings(new FileInputStream(""));
			MessageStoreFactory storeFactory = new FileStoreFactory(settings);
			LogFactory logFactory = new FileLogFactory(settings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			
			ThreadedSocketAcceptor acceptor = new ThreadedSocketAcceptor(application, storeFactory, settings, logFactory, messageFactory);
						
			acceptor.start();
			//while( condition == true ) { do something; }
			acceptor.stop();
			
		}catch(ConfigError | FileNotFoundException e){

		}

	}

}
