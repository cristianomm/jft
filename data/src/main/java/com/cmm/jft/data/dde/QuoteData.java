/**
 * 
 */
package com.cmm.jft.data.dde;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Level;

import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.data.enums.DataFields;
import com.cmm.jft.data.exceptions.StreamException;
import com.cmm.jft.vo.Extractable;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>QuoteData.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 30/07/2013 01:19:25
 * 
 */
public class QuoteData implements Extractable {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,S");
	private String date;
	private String time;
	private long elapsedTime;
	private long acquiredDateTime;
	// identificacao
	private String symbol;
	private String name;
	private String market;
	private String isin;
	private String type;
	private Date qDateTime;

	// valores
	private double open;
	private double lstClose;
	private double high;
	private double low;

	// info
	/**
	 * Quantidade de neg�cios no dia
	 */
	private long trades;
	/**
	 * Quantidade Negociada
	 */
	private long tradedQt;
	/**
	 * Quantidade do �ltimo Neg�cio
	 */
	private long lstTradeQt;
	/**
	 * �ltimo Pre�o negociado
	 */
	private double lstPrice;
	/**
	 * Quantidade da Oferta de Compra para este determinado pre�o
	 */
	private int buyOfferQt;
	/**
	 * Quantidade da Oferta de Venda para este determinado pre�o
	 */
	private int sellOfferQt;

	/**
	 * N�mero de Ofertas de Compra para este determinado pre�o
	 */
	private int buyOfferCount;
	/**
	 * N�mero de Ofertas de Venda para este determinado pre�o
	 */
	private int sellOfferCount;

	private double variation;
	private double ptVariation;
	/**
	 * Volume Financeiro Total do Dia
	 */
	private BigDecimal finVolume;
	/**
	 * Volume financeiro do �ltimo neg�cio
	 */
	private BigDecimal lstFinVolume;

	/**
	 * Melhor Preco de Compra
	 */
	private double bid;

	/**
	 * Melhor Preco de Venda
	 */
	private double ask;

	public QuoteData() {
		this.acquiredDateTime = System.currentTimeMillis();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		return this.trades == ((QuoteData) obj).trades;
	}

	public void updateDateTime() {

		SimpleDateFormat sdtf = new SimpleDateFormat("dd/MM/yy");
		SimpleDateFormat stf = new SimpleDateFormat("hh:mm");

		GregorianCalendar dt = new GregorianCalendar();
		GregorianCalendar tm = new GregorianCalendar();
		try {
			dt.setTime(sdtf.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
			Logging.getInstance().log(QuoteData.class, e, Level.ERROR);
		} catch (Exception e) {
			Logging.getInstance().log(QuoteData.class, e, Level.ERROR);
		}

		try {
			tm.setTime(stf.parse(time));
		} catch (ParseException e) {
			e.printStackTrace();
			Logging.getInstance().log(QuoteData.class, e, Level.ERROR);
		} catch (Exception e) {
			Logging.getInstance().log(QuoteData.class, e, Level.ERROR);
		}

		GregorianCalendar gc = new GregorianCalendar();
		gc.set(GregorianCalendar.DATE, dt.get(GregorianCalendar.DATE));
		gc.set(GregorianCalendar.MONTH, dt.get(GregorianCalendar.MONTH));
		gc.set(GregorianCalendar.YEAR, dt.get(GregorianCalendar.YEAR));
		gc.set(GregorianCalendar.HOUR, tm.get(GregorianCalendar.HOUR));
		gc.set(GregorianCalendar.MINUTE, tm.get(GregorianCalendar.MINUTE));

		elapsedTime = acquiredDateTime - gc.getTimeInMillis();
		acquiredDateTime = gc.getTimeInMillis();
		tm.setTimeInMillis(elapsedTime);
		qDateTime = gc.getTime();

	}

	private String clean(String st) {
		st = st.replace("-", "0");
		st = st.replace(")", "").replace("(", "-");
		st = st.replace("\n", "").replace("\r", "");
		st = st.replace("#REF!", "0").replace("#N/D", "0");
		st = st.replace(".", "").replace(",", ".");
		st = st.trim();
		return st;
	}

	public void addValue(DataFields code, String data) throws StreamException {

		data = clean(data);
		try {
			switch (code) {
			case SYMBOL:
				this.symbol = data;
				break;
			case OPN:
				this.open = (Double) FormatterFactory.getFormatter(
						FormatterTypes.DOUBLE).parse(data);
				break;
			case NME:
				this.name = data;
				break;
			case DAT:
				date = data;
				break;
			case LST_CLOSE:
				this.lstClose = (Double) FormatterFactory.getFormatter(
						FormatterTypes.DOUBLE).parse(data);
				break;
			case TIME:
				time = data;
				break;
			case HIGH:
				this.high = (Double) FormatterFactory.getFormatter(
						FormatterTypes.DOUBLE).parse(data);
				break;
			case LOW:
				this.low = (Double) FormatterFactory.getFormatter(
						FormatterTypes.DOUBLE).parse(data);
				break;
			case TRADES:
				this.trades = (Long) FormatterFactory.getFormatter(
						FormatterTypes.LONG).parse(data);
				break;
			case TRADED_QT:
				this.tradedQt = (Long) FormatterFactory.getFormatter(
						FormatterTypes.LONG).parse(data);
				break;
			case LST_PRICE:
				this.lstPrice = (Double) FormatterFactory.getFormatter(
						FormatterTypes.DOUBLE).parse(data);
				break;
			case LST_TRADE_QT:
				this.lstTradeQt = (Long) FormatterFactory.getFormatter(
						FormatterTypes.LONG).parse(data);
				break;
			case VAR:
				this.variation = (Double) FormatterFactory.getFormatter(
						FormatterTypes.DOUBLE).parse(data);
				break;
			case FVT:
				this.finVolume = (BigDecimal) FormatterFactory.getFormatter(
						FormatterTypes.BIGDECIMAL).parse(data);
				break;
			case VAP:
				break;
			case LFV:
				this.lstFinVolume = (BigDecimal) FormatterFactory.getFormatter(
						FormatterTypes.BIGDECIMAL).parse(data);
				break;
			case BOPx:
				this.bid = (Double) FormatterFactory.getFormatter(
						FormatterTypes.DOUBLE).parse(data);
				break;
			case SOPx:
				this.ask = (Double) FormatterFactory.getFormatter(
						FormatterTypes.DOUBLE).parse(data);
				break;
			case BOQTx:
				break;
			case SOQTx:
				break;
			case BONx:
				break;
			case SONx:
				break;
			case SELL:
				break;
			case BUY:
				break;
			case ISI:
				this.isin = data;
				break;
			case MKT:
				this.market = data;
				break;
			case TYPE:
				this.type = data;
				break;
			case BBRK:
				break;
			case SBRK:
				break;

			// case QDT:
			// this.qDateTime =
			// break;
			case ADT:
				this.acquiredDateTime = (Long) FormatterFactory.getFormatter(
						FormatterTypes.LONG).parse(data);
				break;

			default:
				break;
			}
		} catch (NumberFormatException e) {
			Logging.getInstance().log(QuoteData.class, e, Level.ERROR);
			throw new StreamException("Erro ao ler valor: " + data
					+ " para o codigo " + code.name());
		}
	}

	public long getAcquiredDateTime() {
		return acquiredDateTime;
	}

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return this.symbol;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @return the market
	 */
	public String getMarket() {
		return this.market;
	}

	/**
	 * @return the isin
	 */
	public String getIsin() {
		return this.isin;
	}

	/**
	 * @return the type
	 */
	public String getType() {
		return this.type;
	}

	/**
	 * @return the qDateTime
	 */
	public Date getqDateTime() {
		return this.qDateTime;
	}

	/**
	 * @return the open
	 */
	public double getOpen() {
		return this.open;
	}

	/**
	 * @return the lstClose
	 */
	public double getLstClose() {
		return this.lstClose;
	}

	/**
	 * @return the high
	 */
	public double getHigh() {
		return this.high;
	}

	/**
	 * @return the low
	 */
	public double getLow() {
		return this.low;
	}

	/**
	 * @return the trades
	 */
	public long getTrades() {
		return this.trades;
	}

	/**
	 * @return the tradedQt
	 */
	public long getTradedQt() {
		return this.tradedQt;
	}

	/**
	 * @return the lstTradeQt
	 */
	public long getLstTradeQt() {
		return this.lstTradeQt;
	}

	/**
	 * @return the lastPrice
	 */
	public double getLstPrice() {
		return this.lstPrice;
	}

	/**
	 * @return the buyOfferQt
	 */
	public int getBuyOfferQt() {
		return this.buyOfferQt;
	}

	/**
	 * @return the sellOfferQt
	 */
	public int getSellOfferQt() {
		return this.sellOfferQt;
	}

	/**
	 * @return the buyOfferCount
	 */
	public int getBuyOfferCount() {
		return this.buyOfferCount;
	}

	/**
	 * @return the sellOfferCount
	 */
	public int getSellOfferCount() {
		return this.sellOfferCount;
	}

	/**
	 * @return the variation
	 */
	public double getVariation() {
		return this.variation;
	}

	/**
	 * @return the ptVariation
	 */
	public double getPtVariation() {
		return this.ptVariation;
	}

	/**
	 * @return the finVolume
	 */
	public BigDecimal getFinVolume() {
		return this.finVolume;
	}

	/**
	 * @return the lstFinVolume
	 */
	public BigDecimal getLstFinVolume() {
		return this.lstFinVolume;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return (this.date != null ? this.date + ";" : ";")
				+ (this.time != null ? this.time + ";" : ";")
				+ this.elapsedTime + ";" + this.acquiredDateTime + ";"
				+ (this.symbol != null ? this.symbol + ";" : ";")
				+ (this.name != null ? this.name + ";" : ";")
				+ (this.market != null ? this.market + ";" : ";")
				+ (this.isin != null ? this.isin + ";" : ";")
				+ (this.type != null ? this.type + ";" : ";")
				+ (this.qDateTime != null ? this.qDateTime + ";" : ";")
				+ this.open + ";" + this.lstClose + ";" + this.high + "; "
				+ this.low + ";" + this.trades + ";" + this.tradedQt + ";"
				+ this.lstTradeQt + ";" + this.lstPrice + ";" + this.buyOfferQt
				+ ";" + this.sellOfferQt + ";" + this.buyOfferCount + ";"
				+ this.sellOfferCount + ";" + this.variation + ";"
				+ this.ptVariation + ";"
				+ (this.finVolume != null ? this.finVolume + ";" : ";")
				+ (this.lstFinVolume != null ? this.lstFinVolume + ";" : ";")
				+ this.bid + ";" + this.ask;
	}

}
