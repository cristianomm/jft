package com.cmm.jft.data.test;

import java.io.IOException;

import org.apache.log4j.Level;

import com.cmm.jft.core.enums.LoadableSource;
import com.cmm.jft.data.Config;
import com.cmm.jft.data.DataStore;
import com.cmm.jft.data.dde.DDEStream;
import com.cmm.jft.data.loader.DataLoader;
import com.cmm.logging.Logging;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		 DDEStream dde = new DDEStream("Excel", "Teste");
		//
		// dde.addSymbol("IBOV");
		// dde.addSymbol("PETR4");
		// dde.addSymbol("VALE5");
		// dde.addSymbol("CSNA3");
		// dde.addSymbol("BBAS3");
		// dde.addSymbol("TIMP3");
		// dde.addSymbol("BVMF3");
		 dde.addSymbol("WINM15");
		// dde.addSymbol("ELET6");
		// dde.addSymbol("ALLL3");
		// dde.addSymbol("ITUB4");
		// dde.addSymbol("BBDC4");
		// dde.addSymbol("OGXP3");
		// dde.addSymbol("ARTR3");
		// dde.addSymbol("OSXB3");
		// dde.addSymbol("PDGR3");
		// dde.addSymbol("GFSA3");
		// dde.addSymbol("ITSA4");
		// dde.addSymbol("RSID3");
		// dde.addSymbol("USIM5");
		//
		 dde.open();
		//
		 dde.acquireData(50);

		Logging.getInstance().log(Test.class, "Teste", Level.ALL);
		// testNegLoader();

	}

	public static void testNegLoader() {

		String fName = "C:\\Disco\\Bancos\\Bolsa\\BM&FBovespa\\MarketData\\Bovespa-Vista\\NEG_20140526.TXT";

		try {
			DataLoader.getInstance().importData(fName,
					LoadableSource.BVMFBOVESPA_TickData_Negociation);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void tesDataStore() {
		try {
			Config store = new Config("../jft_data/StoreConfig.properties");
			Config stream = new Config("../jft_data/StreamConfig.properties");

			DataStore ds = new DataStore(store, stream);
			ds.startStore();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
