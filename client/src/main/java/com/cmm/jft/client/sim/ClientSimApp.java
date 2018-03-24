package com.cmm.jft.client.sim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import com.cmm.jft.connector.engine.EngineConnector;
import com.cmm.jft.connector.engine.EngineStarter;
import com.cmm.jft.connector.marketdata.MarketDataStarter;

import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.DefaultMessageFactory;
import quickfix.InvalidMessage;
import quickfix.Message;
import quickfix.MessageUtils;

/**
 * <p>
 * <code>ClientSimApp.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Aug 19, 2016 8:41:55 PM
 *
 */
public class ClientSimApp {

    /**
     * @param args
     */
    public static void main(String[] args) {
	try {
	    MarketDataStarter mds = new MarketDataStarter();
	    mds.start();

	    EngineStarter es = new EngineStarter();
	    es.start();

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    public void startSending() {

	EngineConnector ec = EngineConnector.getInstance();
	try {
	    String p = "C:\\Disco\\Workspaces\\JFT\\jft_modules\\doc\\process\\bmfbovespa\\EntryPoint\\FIX44EntrypointGatewayDerivatives.xml";
	    DefaultMessageFactory dmf = new DefaultMessageFactory();
	    DataDictionary dd = new DataDictionary(p);
	    Scanner sc = new Scanner(
		    new File("C:\\Disco\\Bancos\\BM&FBovespa\\MarketData\\BMF\\apphmb\\intraday\\OFFERS.TXT"));
	    String msg = "";
	    while (sc.hasNextLine()) {
		msg = sc.nextLine();
		Message m = MessageUtils.parse(dmf, dd, msg);
		System.out.println(MessageUtils.getMessageType(msg));
		// System.out.println(m);
	    }
	} catch (InvalidMessage | ConfigError | FileNotFoundException e) {
	    e.printStackTrace();
	}

    }

}
