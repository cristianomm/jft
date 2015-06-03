/**
 * 
 */
package com.cmm.jft.data.loader;

import com.cmm.jft.core.enums.LoadableSource;

/**
 * <p>
 * <code>DataLoader.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 03/08/2014 23:55:06
 *
 */
public class DataLoader {

	private static DataLoader instance;

	/**
     * 
     */
	private DataLoader() {
		// TODO Auto-generated constructor stub
	}

	public synchronized static DataLoader getInstance() {
		if (instance == null) {
			instance = new DataLoader();
		}
		return instance;
	}

	public void importData(String fileName, LoadableSource lSource)
			throws Exception {
		switch (lSource) {

		case BVMFBOVESPA_TickData_Negociation:
			new BMFBovespaTDNegociation().importData(fileName);
			break;

		default:
			throw new Exception("LoadableSource has not implemented.");

		}
	}

}
