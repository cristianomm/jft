
package com.cmm.jft.data.fixlogs;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Properties;
import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.data.connection.Events;
import com.cmm.jft.data.extractor.marketdata.BovespaOfferFileExtractor;
import com.cmm.jft.data.extractor.marketdata.BovespaTradeFileExtractor;
import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.vo.Extractable;

/**
 * <p>
 * <code>FIXLogCreator.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Nov 6, 2015 1:40:02 AM
 *
 */
public class FIXLogCreator {


	private class EventsEntry implements Comparable<EventsEntry>{
		private LocalDateTime datetime;
		private List<MDEntry> buy;
		private List<MDEntry> sell;
		private List<MDEntry> trade;

		public  EventsEntry(LocalDateTime dateTime){
			this.datetime = dateTime;
			this.buy = new ArrayList<>();
			this.sell = new ArrayList<>();
			this.trade = new ArrayList<>();
		}

		/* (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(EventsEntry o) {
			return this.datetime.compareTo(o.datetime);
		}

		public HashMap<Integer, Object> toEntryType(){
			HashMap<Integer, Object> retMap = new HashMap<>();



			return retMap;
		}


		public List<Orders> toOrders(){


			return null;
			//			NewOrderSingle orderSingle = new NewOrderSingle(
			//					new ClOrdID(order.getClOrdID()), new Side(order.getSide().getValue()), 
			//					new TransactTime(), new OrdType(order.getOrderType().getValue())
			//					);
			//			
			//			addIdFields(orderSingle);
			//			
			//			orderSingle.set(new Symbol(order.getSecurityID().getSymbol()));
			//			orderSingle.set(new SecurityExchange("BVMF"));
			//			
			//			orderSingle.set(new OrderQty(order.getVolume()));
			//			orderSingle.set(new Price(order.getPrice()));
			//			orderSingle.set(new TimeInForce(order.getValidityType().getValue()));
			//			
			//			orderSingle.set(new ExpireDate(((DateTimeFormatter)FormatterFactory.getFormatter(FormatterTypes.DATE_F9)).format(order.getDuration())));
			//			orderSingle.setString(5149, order.getComment());

		}


	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private static SimpleDateFormat stf = new SimpleDateFormat("HH:mm:ss.SSS");
	private static SimpleDateFormat sdtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	public static void main(String[] args) throws ParseException{

		//		String time = "09:55:00.572000";
		//		
		//		System.out.println(stf.parse(time));
		//		
		//		System.exit(0);

		String path = "C:/Disco/Bancos/BM&FBovespa/MarketData/BMF/apphmb/intraday/";

		String buyFileName = path + "OFER_CPA_BMF_20160118_WDOG16 - 100.TXT";
		String sellFileName = path + "OFER_VDA_BMF_20160118_WDOG16 - 100.TXT";
		String tradeFileName = path + "NEG_BMF_20160118_WDOG16 - 100.TXT";
		String symbol = "WDOG16";
		new FIXLogCreator().logFromClient(buyFileName, sellFileName, tradeFileName, symbol);
	}


	private TreeMap<LocalDateTime, EventsEntry> loadMDFiles(String buyFileName, String sellFileName, String tradeFileName, String symbol) {

		TreeMap<LocalDateTime, EventsEntry> mapentries = new TreeMap<>();

		List<Extractable> buyOrders = readOfferMDFile(buyFileName);
		List<Extractable> sellOrders = readOfferMDFile(sellFileName);
		List<Extractable> trades = readTradeMDFile(tradeFileName);

		// caso os eventos ocorram no mesmo tempo, sera criado um MDIR

		// para eventos em tempos diferentes separar cada um em um MDIR

		PriorityQueue<MDEntry> buyEvents = new PriorityQueue<>();
		PriorityQueue<MDEntry> sellEvents = new PriorityQueue<>();
		PriorityQueue<MDEntry> tradeEvents = new PriorityQueue<>();

		if (symbol != null || !symbol.isEmpty()) {
			buyOrders.stream()
			.filter(bo -> ((MDEntry)bo).getSymbol().equalsIgnoreCase(symbol))
			.forEach(bo -> buyEvents.add((MDEntry) bo));
			buyEvents.stream().forEach(b -> b.setSide(Side.BUY));
			buyOrders.clear();

			sellOrders.stream()
			.filter(so -> ((MDEntry)so).getSymbol().equalsIgnoreCase(symbol))
			.forEach(so -> sellEvents.add((MDEntry) so));
			sellEvents.stream().forEach(s -> s.setSide(Side.SELL));
			sellOrders.clear();

			trades.stream()
			.filter(t -> ((MDEntry)t).getSymbol().equalsIgnoreCase(symbol))
			.forEach(t -> tradeEvents.add((MDEntry) t));
			trades.clear();
		}

		trades = null;
		buyOrders = null;
		sellOrders = null;

		while (!buyEvents.isEmpty()) {
			MDEntry buy = buyEvents.poll();
			EventsEntry entry = new EventsEntry(buy.getMdEntryDateTime());
			if(mapentries.containsKey(entry.datetime)){
				mapentries.get(entry.datetime).buy.add(buy);
			}else{
				entry.buy.add(buy);
				mapentries.put(entry.datetime, entry);
			}
		}

		while (!sellEvents.isEmpty()) {
			MDEntry sell = sellEvents.poll();
			EventsEntry entry = new EventsEntry(sell.getMdEntryDateTime());
			if(mapentries.containsKey(entry.datetime)){
				mapentries.get(entry.datetime).sell.add(sell);
			}else{
				entry.sell.add(sell);
				mapentries.put(entry.datetime, entry);
			}
		}

		while (!tradeEvents.isEmpty()) {
			MDEntry trade = tradeEvents.poll();
			EventsEntry entry = new EventsEntry(trade.getMdEntryDateTime());
			if(mapentries.containsKey(entry.datetime)){
				mapentries.get(entry.datetime).trade.add(trade);
			}else{
				entry.trade.add(trade);
				mapentries.put(entry.datetime, entry);
			}
		}

		System.out.println(sdtf.format(mapentries.firstKey()) + " " + sdtf.format(mapentries.lastKey()));
		return mapentries;
	}


	/**
	 * 
	 * @param buyFileName
	 * @param sellFileName
	 */
	public void logFromClient(String buyFileName, String sellFileName, String tradeFileName, String symbol) {

		// ordens que possuem dois eventos no mesmo tempo como por exemplo 1 e 3
		// sao ordens do tipo Fill Or Kill
		TreeMap<LocalDateTime, EventsEntry> mapentries = loadMDFiles(buyFileName, sellFileName, tradeFileName, symbol);


		for(EventsEntry ee : mapentries.values()){
			//agrupa por tipo de evento
			Map<Integer, List<MDEntry>> resB = ee.buy.stream().collect(
					Collectors.groupingBy(MDEntry::getOrderEvent)
					);

			Map<Integer, List<MDEntry>> resS = ee.sell.stream().collect(
					Collectors.groupingBy(MDEntry::getOrderEvent)
					);

			if(resB.size() > 0){
				resB.values().stream().count();
			}

			if(resS.size() > 0){

			}


		}


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
	public void fromMDFile(String buyFileName, String sellFileName, String tradeFileName, String symbol){
		TreeMap<LocalDateTime, EventsEntry> mapentries = loadMDFiles(buyFileName, sellFileName, tradeFileName, symbol);



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
