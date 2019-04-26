/**
 * 
 */
package com.cmm.jft.security.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.security.repository.SecurityRepository;
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
@Service
public class SecurityService {

	private HashMap<String, Security> securities;
	
	@Autowired
	private SecurityRepository securityRepository;
	
	public SecurityService() {
		
	}

	public void loadSecurity(Security security) {
		if (security != null) {
			securities.put(security.getSymbol(), security);
		}
	}

	private Security loadSecurity(String symbol) {
		Security sec = null;

		try {
			if (!securities.containsKey(symbol)) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("symbol", symbol);
				
				sec = securityRepository.findBySymbolIgnoreCase(symbol);
				securities.put(symbol, sec);				
			}

			sec = securities.get(symbol);

		} catch (Exception e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
		return sec;
	}

	public Security findSecurity(long securityID, char secIDSrc, String securityExchange) {

		Security sec = securities.values()
				.parallelStream()
				.filter(
						s -> s.getSecurityId() == securityID
						&& s.getSecurityIdSrc() == secIDSrc
						&& s.getStockExchangeId().getStockExchangeId().equalsIgnoreCase(securityExchange))
				.findFirst().orElse(null);

		return sec;
	}

	public Security provideSecurity(String symbol) {

		Security s = null;
		s = loadSecurity(symbol);
		try {
			if (s == null && !DBFacade.getInstance().getConnection().isClosed() && symbol != "") {
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

	private boolean loadSecurityList() {
		boolean ret = false;
		try {
			securityRepository.findAll().forEach(s -> securities.put(s.getSymbol(), s));			
			ret = securities.size() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return ret;
	}
	
	public Security findBySymbol(String symbol) {
		Security security = securityRepository.findBySymbolIgnoreCase(symbol);
		
		return security;
	}
	
	public List<Security> getSecurityList() {
		
		if(securities == null || securities.size()==0) {
			securities = new HashMap<>();
			loadSecurityList();			
		}
		
		List<Security> data = new LinkedList<>();
		securities.values().parallelStream().forEach(s -> data.add(s));
		
		return data;
	}

}
