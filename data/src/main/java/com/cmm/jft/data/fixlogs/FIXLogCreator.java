/**
 * 
 */
package com.cmm.jft.data.fixlogs;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Properties;

import com.cmm.jft.data.extractor.marketdata.BovespaOfferFileExtractor;
import com.cmm.jft.data.extractor.marketdata.BovespaTradeFileExtractor;
import com.cmm.jft.vo.Extractable;
import com.cmm.jft.vo.OrderEventVO;

/**
 * <p>
 * <code>FIXLogCreator.java</code>
 * </p>
 * 
 * @author cristiano
 * @version Nov 6, 2015 1:40:02 AM
 *
 */
public class FIXLogCreator {

	private class EventComparator implements Comparator<OrderEventVO> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(OrderEventVO o1, OrderEventVO o2) {

			int compDt = o1.orderDate.compareTo(o2.orderDate);
			int compTm = o1.orderTime.compareTo(o2.orderTime);
			int ret = 0;
			if (compDt == 0) {
				ret = compTm;
			} else if (compDt < 0) {
				ret = compDt;
			} else {
				ret = 1;
			}

			return ret;
		}

	}

	/**
	 * 
	 * @param buyFileName
	 * @param sellFileName
	 */
	public void logFromClient(String buyFileName, String sellFileName) {

		// ordens que possuem dois eventos no mesmo tempo como por exemplo 1 e 3
		// sao ordens do tipo Fill Or Kill
		List<Extractable> buyOrders = readOfferMDFile(buyFileName);
		List<Extractable> sellOrders = readOfferMDFile(sellFileName);

	}

	/**
	 * Cria um arquivo contento mensagens do tipo
	 * MarketDataIncrementalRefresh(35=X) de acordo com as informações nos
	 * arquivos passados por parametro.
	 * 
	 * @param buyFileName
	 * @param sellFileName
	 * @param tradeFileName
	 */
	public void fromMDFile(String buyFileName, String sellFileName, String tradeFileName, String symbol) {

		List<Extractable> buyOrders = readOfferMDFile(buyFileName);
		List<Extractable> sellOrders = readOfferMDFile(sellFileName);
		List<Extractable> trades = readTradeMDFile(tradeFileName);

		// caso os eventos ocorram no mesmo tempo, sera criado um MDIR

		// para eventos em tempos diferentes separar cada um em um MDIR

		PriorityQueue<OrderEventVO> buyEvents = new PriorityQueue<>(new EventComparator());
		PriorityQueue<OrderEventVO> sellEvents = new PriorityQueue<>(new EventComparator());
		PriorityQueue<OrderEventVO> tradeEvents = new PriorityQueue<>(new EventComparator());

		if (symbol == null || symbol.isEmpty()) {
			buyOrders.forEach(bo -> buyEvents.add((OrderEventVO) bo));
			sellOrders.forEach(so -> sellEvents.add((OrderEventVO) so));
			trades.forEach(t -> tradeEvents.add((OrderEventVO) t));
		} else {
			buyOrders.stream()
			.filter(bo -> ((OrderEventVO)bo).securityID.equalsIgnoreCase(symbol))
			.forEach(bo -> buyEvents.add((OrderEventVO) bo));
			
			sellOrders.stream()
			.filter(so -> ((OrderEventVO)so).securityID.equalsIgnoreCase(symbol))
			.forEach(so -> sellEvents.add((OrderEventVO) so));
			
			trades.stream()
			.filter(t -> ((OrderEventVO)t).securityID.equalsIgnoreCase(symbol))
			.forEach(t -> tradeEvents.add((OrderEventVO) t));
		}
		
		

	}

	private List<Extractable> readOfferMDFile(String fileName) {

		Properties p = new Properties();
		p.put("filename", fileName);
		BovespaOfferFileExtractor bfe = new BovespaOfferFileExtractor();
		bfe.config(p);
		List<Extractable> orders = bfe.extract();

		return orders;
	}

	private List<Extractable> readTradeMDFile(String fileName) {

		Properties p = new Properties();
		p.put("filename", fileName);
		BovespaTradeFileExtractor bte = new BovespaTradeFileExtractor();
		bte.config(p);
		List<Extractable> orders = bte.extract();

		return orders;
	}

}
