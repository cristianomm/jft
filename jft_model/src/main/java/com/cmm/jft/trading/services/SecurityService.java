/**
 * 
 */
package com.cmm.jft.trading.services;

import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.security.Security;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>SecurityService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 04/08/2014 01:43:12
 *
 */
public class SecurityService {

	private static SecurityService instance;

	/**
     * 
     */
	private SecurityService() {
		// TODO Auto-generated constructor stub
	}

	public static synchronized SecurityService getInstance() {
		if (instance == null) {
			instance = new SecurityService();
		}
		return instance;
	}

	private Security loadSecurity(String symbol) {
		Security sec = null;

		try {
			HashMap<String, Object> params = new HashMap<String, Object>();
			params.put("symbol", symbol);

			List rs = DBFacade.getInstance().queryNamed(
					"Security.findBySymbol", params);
			if (rs != null && !rs.isEmpty()) {
				sec = (Security) rs.get(0);
			}

		} catch (DataBaseException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		return sec;
	}

	public Security provideSecurity(String symbol) {

		Security s = null;
		s = loadSecurity(symbol);
		try {
			if (s == null && !DBFacade.getInstance().getConnection().isClosed()) {
				s = new Security(symbol);
				s = (Security) DBFacade.getInstance()._persist(s);
			}
		} catch (SQLException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		} catch (DataBaseException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}

		return s;
	}
	

}
