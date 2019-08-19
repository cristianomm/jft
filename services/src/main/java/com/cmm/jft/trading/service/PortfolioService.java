/**
 * 
 */
package com.cmm.jft.trading.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.apache.log4j.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.model.marketdata.HistoricalQuote;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.trading.Allocation;
import com.cmm.jft.model.trading.AppliedAllocation;
import com.cmm.jft.model.trading.Closure;
import com.cmm.jft.model.trading.Operation;
import com.cmm.jft.model.trading.Portfolio;
import com.cmm.jft.model.trading.Position;
import com.cmm.jft.trading.repository.OperationRepository;
import com.cmm.jft.trading.repository.PortfolioRepository;
import com.cmm.logging.Logging;


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
	
	/**
	 * Realiza o processamento de um portfolio para a data passada por parametro.
	 * caso a data seja inferior a data atual do portfolio, o portfolio sera retroagido ate a data
	 * informada. Caso a data seja posterior a data atual do portfolio o portfolio sera processado
	 * progressivamente ate a data informada.
	 * @param portfolio
	 * @param date
	 */
	public void processPortfolio(Portfolio portfolio, LocalDateTime dateTime) {
		
		if(portfolio != null) {
			
			int operation = dateTime.compareTo(portfolio.getPositionDate());
			
			if(operation == 1) {
				processForward(portfolio, dateTime);
			}
			else if(operation == -1) {
				processBackward(portfolio, dateTime);
			}
			else{ //(operation == 0)
				Logging.getInstance().log(getClass(), "Portfolio is at same date.", Level.INFO);
			}
		}
	}
	
	public void processForward(Portfolio portfolio, LocalDateTime dateTime) {
		
		//check closure date
		
		//forward a day
		
		//update and create positions for date
		List<Position> positions = processPositions(portfolio, dateTime);
		
		//create new allocations for positions
		List<AppliedAllocation> allocations = processAllocations(positions, portfolio);
		
		//create new closure for date
		Closure closure = processClosure(allocations);
		
		//update portfolio closure date
		portfolio.getClosures().add(closure);
		portfolio.setPositionDate(closure.getDate());
		
		portfolioRepository.save(portfolio);
		
	}
	
	private List<Position> processPositions(Portfolio portfolio, LocalDateTime dateTime) {
		
		List<Position> positions = new ArrayList<Position>();
		
		//get last positions
		Closure lastClosure = portfolio.getClosures()
				.stream()
				.max(new Closure.ClosureComparator())
				.orElse(new Closure());
		
		List<Security> securities = lastClosure
				.getAppliedAllocations()
				.stream()
				.collect(Collectors.mapping(AppliedAllocation::getSecurityId, Collectors.toList()));
		
		//find quote for security allocations
		TreeMap<String, BigDecimal> secQuotes = new TreeMap<>();
		for(Security s : securities) {
			HistoricalQuote quote = s.getHistoricalQuoteList()
					.stream()
					.filter(h->h.getqDateTime().compareTo(ChronoLocalDate.from(dateTime)) == 0)
					.findFirst()
					.orElse(null);
			
			secQuotes.put(s.getSymbol(), quote.getAdjClose());
		}
		
		//for each position and security quote
		for(AppliedAllocation allocation : lastClosure.getAppliedAllocations()) {
			
			double price = secQuotes.get(allocation.getSecurityId().getSymbol()).doubleValue();
			for(Position lastPosition : allocation.getPositions()) {
				Position position = new Position();
				position.setPositionDate(LocalDateTime.now());
				position.setPrice(price);
				position.setQuantity(lastPosition.getQuantity());
				
				double value = price * lastPosition.getQuantity();
				double earnings = lastPosition.getEarnings() + (value - lastPosition.getValue());
				double variation = (price - lastPosition.getPrice())/lastPosition.getPrice();
				
				position.setValue(value);
				position.setEarnings(earnings);
				position.setVariation(variation);
				
				position.setStatus(GeneralStatus.ACTIVE);
				position.setSecurityId(allocation.getSecurityId());
				
				positions.add(position);
			}
		}
		
		return positions;
	}
	
	private List<AppliedAllocation> processAllocations(List<Position> positions, Portfolio portfolio){
		
		List<AppliedAllocation> allocations = new ArrayList<AppliedAllocation>();
		
		Closure lastClosure = portfolio.getClosures()
				.stream()
				.max(new Closure.ClosureComparator())
				.orElse(new Closure());
		
		List<Position> lastPositions = new ArrayList<Position>();
		lastClosure.getAppliedAllocations().forEach(a -> lastPositions.addAll(a.getPositions()));		
		
		Map<Security, List<Position>> securityPositions = positions.stream().collect(Collectors.groupingBy(Position::getSecurityId));
		Map<Security, List<Position>> securityPrevPositions = lastPositions.stream().collect(Collectors.groupingBy(Position::getSecurityId));
				
		securityPositions
		.forEach((s,p)->
		{
			AppliedAllocation allocation = new AppliedAllocation();
			allocation.setSecurityId(s);
			allocation.setDate(positions.stream().findFirst().get().getPositionDate());
			
			double closeValue = p.stream().mapToDouble(po -> po.getValue()).sum();
			allocation.setValue(closeValue);
			allocation.setLastValue(p.stream().mapToDouble(po -> po.getValue()).sum());
			allocation.setEarnings(p.stream().mapToDouble(po -> po.getEarnings()).sum());
			
			double prevPrice = securityPrevPositions.get(s).stream().findFirst().get().getPrice();
			double price = securityPositions.get(s).stream().findFirst().get().getPrice();
			allocation.setVariation((price - prevPrice)/prevPrice);
			
			allocations.add(allocation);
		});
		
		//adjust percentual allocation		
		double totalValue = allocations.stream().mapToDouble(a -> a.getValue()).sum();
		
		allocations
		.forEach(a -> {
			a.setPercentual(a.getValue()/totalValue);
		});
		
		return allocations;
	}
	
	private Closure processClosure(List<AppliedAllocation> allocations) {
		
		Closure closure = new Closure();
		
		closure.setDate(allocations.stream().findFirst().get().getDate());
		closure.setValue(allocations.stream().mapToDouble(a->a.getValue()).sum());
		closure.setLastValue(allocations.stream().mapToDouble(a->a.getLastValue()).sum());
		closure.setEarnings(allocations.stream().mapToDouble(a->a.getEarnings()).sum());
		
		double variation = (closure.getValue() - closure.getLastValue()) / closure.getLastValue();
		closure.setVariation(variation);
		
		closure.setAppliedAllocations(allocations);
		
		return closure;
	}
	
	
	public void processBackward(Portfolio portfolio, LocalDateTime dateTime) {
		
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
