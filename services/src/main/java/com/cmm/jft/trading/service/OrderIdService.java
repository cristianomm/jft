/**
 * 
 */
package com.cmm.jft.trading.service;

import java.time.format.DateTimeFormatter;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmm.jft.model.trading.Orders;
import com.cmm.jft.security.service.SecurityService;
import com.google.common.util.concurrent.AtomicLongMap;

import edu.emory.mathcs.backport.java.util.Collections;

/**
 * <p>
 * <code>OrderIdService.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version Apr 7, 2019
 * 
 */
@Service
public class OrderIdService {

	private AtomicLongMap<String> symbolIds;
	private DateTimeFormatter format;
	
	public OrderIdService() {
		format = DateTimeFormatter.ofPattern("yyyyMMddHHmmssS");
		this.symbolIds = AtomicLongMap.create();
		loadSymbolCounters();
	}
	
	private void loadSymbolCounters() {
		/*
		securityService
		.getSecurityList()
		.parallelStream()
		.forEach(s -> symbolIds.put(s.getSymbol(), 0L));*/
	}
	
	public synchronized String createClOrderId(Orders order) {
				
		String symbol = order.getSecurityId().getSymbol();
		
		if(!symbolIds.containsKey(symbol)) {
			symbolIds.put(symbol, 0);
		}
		
		long symbolCounter = symbolIds.getAndIncrement(symbol);
		
		String clOrderId = String.format("jft.%s%s%s%s%d.0", 
				order.getOrderDateTime().format(format), 
				order.getBrokerId(), 
				order.getTraderId(), 
				order.getSenderLocation(), 
				symbolCounter);
		
		return clOrderId;
	}
	
	public synchronized String updateClOrderId(Orders order) {
		
		String origClOrderId = order.getClOrdId();
		
		int dotPos = origClOrderId.lastIndexOf(".");
		int updateNum = 1 + Integer.parseInt(origClOrderId.substring(dotPos, origClOrderId.length()));
		String clOrderId = origClOrderId.substring(0, dotPos) + updateNum;
				
		return clOrderId;
	}
	
}
