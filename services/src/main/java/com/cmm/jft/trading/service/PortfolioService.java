/**
 * 
 */
package com.cmm.jft.trading.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmm.jft.model.trading.Allocation;
import com.cmm.jft.model.trading.Operation;
import com.cmm.jft.model.trading.Portfolio;
import com.cmm.jft.trading.repository.OperationRepository;
import com.cmm.jft.trading.repository.PortfolioRepository;

/**
 * <p>
 * <code>PortfolioService.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Mar 31, 2019
 * 
 */
@Service
public class PortfolioService {

	@Autowired
	private PortfolioRepository portfolioRepository;
	@Autowired
	private OperationRepository operationRepository;
	
	
	public void createPortfolio(Portfolio portfolio) {
		
	}
	
	public void processPortfolio(Portfolio portfolio) {
		
	}
		
	public void addOperation(Operation operation) {
		
	}
	
	/**
	 * Ajusta as alocacoes atuais do portfolio para que fiquem conforme a nova configuracao passada por parametro
	 * Caso necessario as alocacoes atuais serao vendidas.
	 * 
	 * @param portfolio Portfolio a ser ajustado
	 * @param allocations novas alocacoes
	 */
	public void updateAllocations(Portfolio portfolio, List<Allocation> allocations) {
		
	}	
	
}
