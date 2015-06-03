package com.cmm.jft.data;

import org.apache.log4j.Level;

import com.cmm.jft.data.dde.DDEStream;
import com.cmm.jft.data.exceptions.InvalidConfigurationException;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>DataStore.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 13/09/2013 14:07:48
 *
 */
public class DataStore implements Runnable {

	private boolean started;
	private boolean running;
	private Storer storer;
	private DataStream dataStream;

	public DataStore(Config storeConfig, Config streamConfig) {
		this.storer = getStorer(storeConfig);
		this.dataStream = getDataStream(streamConfig);
	}

	private static DataStream getDataStream(Config streamConfig) {

		DataStream ret = null;
		try {
			Streams st = Streams.valueOf(streamConfig.getConfig("streamtype"));
			String[] symbols = streamConfig.getConfig("symbols")
					.replace("[", "").replace("]", "").split(",");
			String time = streamConfig.getConfig("acquiretime");

			switch (st) {
			case DDE: {
				String service = streamConfig.getConfig("service");
				String topic = streamConfig.getConfig("topic");
				ret = new DDEStream(service, topic);
			}
				break;

			default:
				break;
			}

			// adiciona os simbolos configurados
			for (String sybl : symbols) {
				ret.addSymbol(sybl);
			}

			// ajusta o tempo de captura
			ret.acquireData(Long.parseLong(time));

		} catch (InvalidConfigurationException e) {
			Logging.getInstance().log(DataStore.class, e, Level.FATAL);
		} catch (NullPointerException e) {
			Logging.getInstance().log(DataStore.class, e, Level.FATAL);
		} catch (NumberFormatException e) {
			Logging.getInstance().log(DataStore.class, e, Level.FATAL);
		}
		return ret;
	}

	private static Storer getStorer(Config storeConfig) {
		Storer ret = null;
		try {
			String storeType = storeConfig.getConfig("storetype");

			if (storeType.equalsIgnoreCase("file")) {
				String fileName = storeConfig.getConfig("filename");
				ret = new FileStore(fileName);
			}
		} catch (InvalidConfigurationException e) {
			Logging.getInstance().log(DataStore.class, e, Level.FATAL);
		}
		return ret;
	}

	public void run() {
		while (started) {

			if (running) {
				storer.init();
			}

			while (running) {
				try {
					storer.store(dataStream.getData().take());
				} catch (InterruptedException e) {
				}
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void startStore() {

		if (!started) {
			running = true;
			started = true;
			dataStream.open();
			new Thread(this).start();

		}

	}

	public void stopStore() {
		started = running = false;
		storer.close();
	}

	public void pauseStore() {
		running = false;
	}

}
