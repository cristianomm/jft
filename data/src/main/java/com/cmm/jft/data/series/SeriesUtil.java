/**
 * 
 */
package com.cmm.jft.data.series;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * <p><code>SeriesUtil.java</code></p>
 * @author Cristiano M Martins
 * @version 08/09/2014 01:28:41
 *
 */
public class SeriesUtil {

	/**
	 * 
	 */
	public SeriesUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Agrupa os dados de acordo com o intervalo de tempo passado por parametro
	 * @param duration Tipo de duracao do intervalo
	 * @param interval quantidade a ser atribuida ao intervalo
	 * @return List com os dados de preco agrupados de acordo 
	 * com o intervalo passado por parametro.
	 */
//	public static TimeSerie createOHLCSerie(List<MarketTrade> trades, long duration, int interval){
//		TimeSerie serie = new TimeSerie();
//		MarketTrade[] points = (MarketTrade[]) trades.toArray();
//		Arrays.sort(points, new MarketTradeComparator(CompareType.DATE));
//
//		long step = duration * interval;
//		Date actualTime = points[0].getTradeDateTime();
//		Date next = new Date(actualTime.getTime() + step);
//
//		double open = points[0].getPrice().doubleValue();
//		double close = open;
//		double max = open;
//		double min = open;
//		int tradeCount = 0;
//		int volume = 0;
//		double financialVol = 0;
//		Date lastTime = actualTime;
//
//		for(MarketTrade trade:trades) {
//
//			if(trade.getTradeDateTime().before(next)) {
//				double price = trade.getPrice().doubleValue();
//				max = (max<price)? price : max;
//				min = (min>price)? price : min;
//				close = price;
//				tradeCount++;
//				volume += trade.getVolume();
//				financialVol += trade.getVolume() * price;
//				lastTime = trade.getTradeDateTime();
//			}
//			else {
//				
//				MarketPoint point = new MarketPoint(open, close, max, min, actualTime, lastTime, volume, financialVol, tradeCount);
//				serie.addPoint(actualTime, point);
//
//				actualTime = next;
//				next = new Date(actualTime.getTime() + step);
//
//				open = trade.getPrice().doubleValue();
//				close = open;
//				min = open;
//				max = open;
//				tradeCount = 1;
//				volume = trade.getVolume();
//				financialVol = trade.getVolume() * open;
//				
//				//adiciona este unico pois pode ser o ultimo, caso nao seja,
//				//ira sobrescrever quando adiconar os valores restantes.
//				point = new MarketPoint(open, close, max, min, actualTime, lastTime, volume, financialVol, tradeCount);
//				serie.addPoint(actualTime, point);
//				
//			}
//
//		}
//
//		return serie;
//	}


}
