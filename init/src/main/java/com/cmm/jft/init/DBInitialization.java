package com.cmm.jft.init;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.juli.DateFormatCache;
import org.apache.log4j.Level;

import com.cmm.jft.core.enums.GeneralStatus;
import com.cmm.jft.core.format.DoubleFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.core.util.InputRowReport;
import com.cmm.jft.data.files.CSV;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.DBObject;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.model.financial.Account;
import com.cmm.jft.model.financial.Broker;
import com.cmm.jft.model.financial.Brokerage;
import com.cmm.jft.model.financial.Commission;
import com.cmm.jft.model.financial.Currency;
import com.cmm.jft.model.financial.ExchangeTax;
import com.cmm.jft.model.financial.enums.AccountCategories;
import com.cmm.jft.model.financial.enums.AccountTypes;
import com.cmm.jft.model.marketdata.ContractAdjust;
import com.cmm.jft.model.marketdata.HistoricalQuote;
import com.cmm.jft.model.marketdata.Holiday;
import com.cmm.jft.model.security.Company;
import com.cmm.jft.model.security.Country;
import com.cmm.jft.model.security.Isin;
import com.cmm.jft.model.security.MarketIndex;
import com.cmm.jft.model.security.Security;
import com.cmm.jft.model.security.SecurityInfo;
import com.cmm.jft.model.security.StockExchange;
import com.cmm.jft.model.security.enums.AssetTypes;
import com.cmm.jft.model.security.enums.FutureSeries;
import com.cmm.jft.model.security.enums.MarketTypes;
import com.cmm.jft.model.security.enums.OptionRights;
import com.cmm.jft.model.security.enums.OptionStyles;
import com.cmm.jft.model.security.enums.SecurityCategory;
import com.cmm.jft.model.trading.enums.StockSpecifications;
import com.cmm.jft.model.trading.enums.TradeTypes;
import com.cmm.jft.model.trading.enums.ValueTypes;
import com.cmm.logging.Logging;

import io.swagger.models.Model;

/**
 * <p>
 * <code>DBInitialization.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 10/09/2013 17:25:45
 *
 */
public class DBInitialization {

	public static void main(String[] args) {

		DBInitialization dbini = new DBInitialization();
		dbini.initializeDB();
		System.exit(0);
	}

	private InputRowReport report;

	public DBInitialization() {
		this.report = new InputRowReport();
		DBFacade.getInstance();
	}

	public void initializeDB() {
		/*
		 * try { 
		 * DBFacade.getInstance()._truncateTable("public.configuration");
		 * DBFacade.getInstance()._truncateTable("financial.broker");
		 * DBFacade.getInstance()._truncateTable("financial.costcenter");
		 * DBFacade.getInstance()._truncateTable("financial.entryregister");
		 * DBFacade.getInstance()._truncateTable("financial.currencyquote");
		 * DBFacade.getInstance()._truncateTable("financial.deposit");
		 * DBFacade.getInstance()._truncateTable("financial.exchangetax");
		 * DBFacade.getInstance()._truncateTable("financial.distributionrule");
		 * DBFacade.getInstance()._truncateTable("financial.ruleformula");
		 * DBFacade.getInstance()._truncateTable("security.country");
		 * DBFacade.getInstance()._truncateTable("financial.tax");
		 * DBFacade.getInstance()._truncateTable("marketdata.marketorder");
		 * DBFacade.getInstance()._truncateTable("security.security");
		 * DBFacade.getInstance()._truncateTable("marketdata.marketevent");
		 * DBFacade.getInstance()._truncateTable("marketdata.markettrade");
		 * DBFacade.getInstance()._truncateTable("trading.orderevent");
		 * DBFacade.getInstance()._truncateTable("financial.currency");
		 * DBFacade.getInstance()._truncateTable("financial.account");
		 * DBFacade.getInstance()._truncateTable("security.securityinfo");
		 * DBFacade.getInstance()._truncateTable("financial.commission");
		 * DBFacade.getInstance()._truncateTable("security.stockexchange");
		 * DBFacade.getInstance()._truncateTable("trading.orders");
		 * DBFacade.getInstance()._truncateTable("financial.rule");
		 * DBFacade.getInstance()._truncateTable("financial.journalentry");
		 * DBFacade.getInstance()._truncateTable("financial.brokerage");
		 * DBFacade.getInstance()._truncateTable("financial.taxsetup");
		 * DBFacade.getInstance()._truncateTable("marketdata.historicalquote"); 
		 * } catch
		 * (DataBaseException e) { e.printStackTrace(); }
		 */

		
		addCountries("../file/Country.csv");
		addCurrencies("../file/Currencies.csv");
		addAccounts("../file/Accounts.csv");
		addBrokers("../file/Brokers.csv");
		addBrokerage("../file/Brokerage.csv");
		addExchanges("../file/Exchanges.csv");
		addMarketIndexes("../file/MarketIndexes.csv");
		addCompanies("../file/isin/EMISSOR.txt");
		//addIsin("../file/Isin.csv");
		
		addSecurities();
		
		addHistoricalQuotes("../file/Bovespa_Consolidated.csv");
		
		addHolidays("../file/feriados_nacionais.csv");
		addContractAdjusts("../file/Settlements.csv");
		// inuteis
		// addMarketCodes("../jft_core/file/MarketCodes.csv");
		// addSecurityTypes("../jft_core/file/SecurityTypes.csv");
		// addDatesInCodes("../jft_core/file/CodesAndDates.csv");

		// addEarnings("../jft_core/file/Earnings.csv");
		// addUsers("../jft_core/file/");

		System.out.println(report.reportAll());
	}

	private void addCountries(String fileName) {
		String repName = "Countries";
		try {
			report.startReport(repName);
			// DBFacade.getInstance().beginTransaction();

			CSV csv = new CSV(fileName, "\t");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();
				Country c = new Country(vs[0], vs[4],
						((Double) FormatterFactory.getFormatter(FormatterTypes.DOUBLE).parse(vs[6])),
						((Double) FormatterFactory.getFormatter(FormatterTypes.DOUBLE).parse(vs[7])).longValue(),
						vs[8]);

				DBFacade.getInstance().addToBatch(c);
				report.count(repName);
			}
			DBFacade.getInstance().finalizeBatch();
			// DBFacade.getInstance().commit();

		} catch (Exception e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

	}

	private void addCurrencies(String fileName) {

		String repName = "Currencies";
		try {
			report.startReport(repName);
			// DBFacade.getInstance().beginTransaction();

			// HashMap<String, Country> countries = new HashMap<String,
			// Country>();
			// DBFacade.getInstance().queryAsMap("Country.findAll", countries,
			// Country.class, "getCountryName");

			// HashMap<String, Currency> currencies = new HashMap<String,
			// Currency>();

			CSV csv = new CSV(fileName, ";", "#");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();
				if (vs.length < 4)
					continue;

				Currency c = new Currency(vs[2]);
				c.setDescription(vs[1]);
				c.setCurrSymbol(vs[3]);
				DBFacade.getInstance().addToBatch(c);
				report.count(repName);
				/*
				 * if(!currencies.containsKey(vs[2])) { currencies.put(vs[2], new
				 * Currency(vs[2])); } currencies.get(vs[2]).setDescription(vs[1]);
				 * currencies.get(vs[2]).setCurrSymbol(vs[3]);
				 * 
				 * for(String sc:countries.keySet()) { if(sc.equalsIgnoreCase(vs[0])) {
				 * countries.get(sc).getCurrencySet().add(currencies.get(vs[2])) ;
				 * currencies.get(vs[2]).getCountrySet().add(countries.get(sc)); break; } }
				 */

			}

			/*
			 * for(Currency curr:currencies.values()) { curr.add(); report.count(repName); }
			 */

			DBFacade.getInstance().finalizeBatch();

		} catch (Exception e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
	}

	private void addAccounts(String fileName) {
		String repName = "Accounts";
		try {
			report.startReport(repName);
			// DBFacade.getInstance().beginTransaction();

			HashMap<String, Currency> currencies = new HashMap<String, Currency>();
			DBFacade.getInstance().queryAsMap("Currency.findAll", currencies, Currency.class, "getCurrencyID");
			CSV csv = new CSV(fileName, ";", "#");
			while (csv.hasNext()) {

				String[] vs = csv.readLine();
				String account = vs[0];
				String accountName = vs[1];
				double credit = Double.parseDouble(vs[2]);
				String currency = vs[3];
				String type = vs[4];
				String category = vs[5];

				AccountTypes types = AccountTypes.valueOf(type);
				AccountCategories cats = AccountCategories.valueOf(category);

				Account acc = new Account(account, accountName, credit, currencies.get(currency), types, cats);

				DBFacade.getInstance().addToBatch(acc);
				report.count(repName);
			}
			DBFacade.getInstance().finalizeBatch();

		} catch (DataBaseException e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

	}

	private void addBrokers(String fileName) {
		String repName = "Brokers";
		report.startReport(repName);
		CSV csv = new CSV(fileName, ";", "#");
		while (csv.hasNext()) {
			try {
				String[] vs = csv.readLine();
				Broker b = new Broker(vs[0], vs[1]);
				DBFacade.getInstance().addToBatch(b);;
				report.count(repName);
			} catch (Exception e) {
				report.reportError(repName, e.getMessage());
				Logging.getInstance().log(getClass(), "Erro ao adicionar Corretora.", e, Level.ERROR, false);
			}
		}
		
		DBFacade.getInstance().finalizeBatch();
	}

	private void addBrokerage(String fileName) {
		String repName = "Brokerage";
		try {
			report.startReport(repName);
			DoubleFormatter dblFrt = (DoubleFormatter) FormatterFactory.getFormatter(FormatterTypes.DOUBLE);
			CSV csv = new CSV(fileName, ";", "#");
			String[] vs = csv.readLine();

			while (csv.hasNext()) {

				Brokerage brk = null;
				if (vs[0].equals("01")) {
					TradeTypes tradeTypes = TradeTypes.getByValue(vs[1]);
					SecurityCategory securityCategory = SecurityCategory.getByValue(vs[2]);
					Broker broker = (Broker) DBFacade.getInstance().findObject("Broker.findByBrokerCode", "brokerCode",
							vs[3]);
					brk = new Brokerage(tradeTypes, securityCategory, broker);
					brk = (Brokerage) DBFacade.getInstance()._persist(brk);
					report.count(repName);
					vs = csv.readLine();
					while (vs != null && !vs[0].equals("01")) {

						if (vs != null && vs[0].equals("02")) {
							String taxName = vs[1];
							double tax = Double.parseDouble(vs[2]);
							ValueTypes calcType = ValueTypes.valueOf(vs[3]);
							// Account crdAccount = (Account)
							// DBFacade.getInstance().findObject("Account.findByAccountID",
							// "accountID", vs[4]);
							// Account dbtAccount = (Account)
							// DBFacade.getInstance().findObject("Account.findByAccountID",
							// "accountID", vs[5]);

							ExchangeTax ext = new ExchangeTax(taxName, tax, calcType, brk);
							DBFacade.getInstance().addToBatch(ext);
							//ext = (ExchangeTax) DBFacade.getInstance().addToBatch(ext);
						}

						if (vs != null && vs[0].equals("03")) {
							double valueMin = Double.parseDouble(vs[1]);
							double valueMax = Double.parseDouble(vs[2]);
							double commValue = Double.parseDouble(vs[3]);
							ValueTypes calcType = ValueTypes.valueOf(vs[4]);
							// Account crdAccount = (Account)
							// DBFacade.getInstance().findObject("Account.findByAccountID",
							// "accountID", vs[5]);
							// Account dbtAccount = (Account)
							// DBFacade.getInstance().findObject("Account.findByAccountID",
							// "accountID", vs[6]);

							Commission com = new Commission(valueMin, valueMax, commValue, calcType, brk);
							DBFacade.getInstance().addToBatch(com);
							//com = (Commission) DBFacade.getInstance().addToBatch(com);
						}

						vs = csv.readLine();
					}
					
					DBFacade.getInstance().finalizeBatch();
					
				}
			}
		} catch (DataBaseException e) {
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(getClass(), "Erro ao adicionar Corretagem.", e, Level.ERROR, false);
		}

	}

	private void addExchanges(String fileName) {
		String repName = "Exchanges";
		try {
			report.startReport(repName);
			DBFacade.getInstance().beginTransaction();

			HashMap<String, Country> countries = new HashMap<String, Country>();
			DBFacade.getInstance().queryAsMap("Country.findAll", countries, Country.class, "getCountryName");

			CSV csv = new CSV(fileName, ";", "#");
			while (csv.hasNext()) {

				String[] vs = csv.readLine();
				if (countries.containsKey(vs[2])) {
					StockExchange se = new StockExchange(vs[0], vs[1], countries.get(vs[2]));
					DBFacade.getInstance().addToBatch(se);
					report.count(repName);
				}
			}
			DBFacade.getInstance().finalizeBatch();

		} catch (DataBaseException e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
		}
	}

	private void addMarketIndexes(String fileName) {
		String repName = "MarketIndex";
		report.startReport(repName);
		CSV csv = new CSV(fileName, ";", "#");
		while (csv.hasNext()) {
			try {
				String[] vs = csv.readLine();
				MarketIndex mi = new MarketIndex(vs[0]);
				mi.setName(vs[1]);
				DBFacade.getInstance().addToBatch(mi);
				report.count(repName);
			} catch (Exception e) {
				report.reportError(repName, e.getMessage());
				Logging.getInstance().log(getClass(), "Erro ao adicionar Market Index.", e, Level.ERROR, false);
			}
		}
	}

	private void addCompanies(String fileName) {
		String repName = "Companies";
		try {
			report.startReport(repName);
			
			HashMap<Long, StockExchange> exchanges = new HashMap<Long, StockExchange>();
			StockExchange exchange = (StockExchange) DBFacade.getInstance()._findByKey(StockExchange.class, "BVMF");
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FormatterTypes.DATE_F9.getFormat());
			
			HashMap<String, String> cvmCompanies = new HashMap<String, String>();
			CSV cvmCSV = new CSV("../file/CVMCompanies.csv", ";","#");
			while(cvmCSV.hasNext()) {
				String line[] = cvmCSV.readLine();
				String cnpj = line[0].replaceAll("\\.", "").replaceAll("/", "").replaceAll("-", "");
				if(!cnpj.isEmpty()) {
					cvmCompanies.put(cnpj, line[2]);
				}
			}						
			//CodigoEmissor;NomePregao;NomeEmissor;CNPJ;QtON;QtPN;DataEmissor;StockExchange
			//CODIGO DO EMISSOR CARACTER 4, NOME DO EMISSOR CARACTER 70, CNPJ DO EMISSOR CARACTER 14, DATA CRIAÇÃO DO EMISSOR DATA 8
			CSV csv = new CSV(fileName, ",");
			csv.setRemoveQuotes(true);
			
			while(csv.hasNext()) {
				String[] vs = csv.readLine();

				if(vs.length == 4) {
					Company company = new Company();
					company.setEmitterCode(vs[0]);
					company.setCompanyName(vs[1]);
					company.setCnpj(vs[2]);
					company.setStatus(GeneralStatus.ACTIVE);
					company.setStockExchangeID(exchange);
					
					if(cvmCompanies.containsKey(company.getCnpj())){
						company.setCvmCode(cvmCompanies.get(company.getCnpj()));
					}
					
					if(vs[3] != null && vs[3].length() == 8) {
						LocalDate dataEmissor =  LocalDate.parse(vs[3], dateTimeFormatter);						
						company.setCompanyDate(Date.from(dataEmissor.atStartOfDay(ZoneId.systemDefault()).toInstant()));
					}
					
					DBFacade.getInstance().addToBatch(company);
					report.count(repName);
				}
			}
			DBFacade.getInstance().finalizeBatch();

		} catch (DataBaseException e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
	}
		
	private void addSecurities() {
		int lineCount = 0;
		String repName = "Securities";
		Map<String, Security> securities = new TreeMap<>();
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FormatterTypes.DATE_F8.getFormat());
		
		//read titulos_negociaveis file
		try (Scanner sc = new Scanner(new File("../file/TITULOS_NEGOCIAVEIS.TXT"))){
			report.startReport(repName);
			
			StockExchange exchange = (StockExchange) DBFacade.getInstance()._findByKey(StockExchange.class, "BVMF");
			Currency currency = (Currency) DBFacade.getInstance()._findByKey(Currency.class, "BRL");
			DBFacade.getInstance().beginTransaction();
			
			String companyName = "";
			String marketName = "";
			while(sc.hasNext()) {
				String line = sc.nextLine();
				
				if(line.startsWith("00") || line.startsWith("09")) continue;
								
				if(line.startsWith("01")) {
					companyName = line.substring(6,66).stripTrailing();
					marketName = line.substring(66,78).stripTrailing();
				}
				else if(line.startsWith("02")) {
					Security sec = new Security();
										
					sec.setSymbol(line.substring(2,14).strip());
					sec.setIsin(line.substring(81,93).strip());
					sec.setMarketName(marketName);
					sec.setDescription(companyName);
					sec.setStockExchangeId(exchange);
					
					SecurityInfo secInfo = new SecurityInfo();					
					MarketTypes marketType = MarketTypes.getByValue(Integer.parseInt(line.substring(108, 111).strip()));
					secInfo.setMarketType(marketType);
					secInfo.setExpirationDate(LocalDate.parse(line.substring(143,153), dateTimeFormatter));
					secInfo.setStrikePrice(Double.parseDouble(line.substring(153,171))/10_000_000);
					secInfo.setOptionStyle(OptionStyles.getByValue(line.substring(171,172)));
					secInfo.setCurrencyId(currency);
					
					sec.setSecurityInfoId(secInfo);
					
					securities.put(sec.getSymbol(), sec);
					
					DBFacade.getInstance().addToBatch(sec);
					report.count(repName);
				}
			}
			
			DBFacade.getInstance().finalizeBatch();
			
		} catch (FileNotFoundException | DataBaseException e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} 
	}
		
	private void addSecurities(String fileName) {
		int line = 0;
		String repName = "Securities";
		try {
			report.startReport(repName);

			HashMap<String, Currency> currencies = new HashMap<String, Currency>();
			DBFacade.getInstance().queryAsMap("Currency.findAll", currencies, Currency.class, "getCurrencyId");

			HashMap<String, Isin> isins = new HashMap<String, Isin>();
			DBFacade.getInstance().queryAsMap("Isin.findAll", isins, Isin.class, "getIsin");

			// HashMap<String, SecurityType> specis = new HashMap<String,
			// SecurityType>();
			// DBFacade.getInstance().queryAsMap("SecurityType.findAll", specis,
			// SecurityType.class, "getCode");

			CSV csv = new CSV(fileName, ";", "#");
			csv.setEncoding("UTF-8");
			csv.readLine();

			DBFacade.getInstance().beginTransaction();

			StockExchange exchange = (StockExchange) DBFacade.getInstance()
					.findObject("StockExchange.findByStockExchangeId", "stockExchangeId", "BVMF");
			//String exchange = "BVMF";

			Date defDate = new Date();
			while (csv.hasNext()) {
				String[] vs = csv.readLine();

				line++;
				if (vs.length < 16)
					continue;
				// System.out.println(vs);
				// [0]#SYMBOL;DESCRIPTION;ISIN;SYMBOL_TYPE;CURRENCY_BASE;
				// [5]CONTRACT_SIZE;TICK_SIZE;TICK_VALUE;DIGITS;MIN_VOLUME;
				// [10]MAX_VOLUME;STEP_VOLUME;START_TIME;EXPIRATION_TIME;OPTION_MODE;
				// [15]OPTION_RIGHT;OPTION_STRIKE
				String symbol = vs[0];
				String description = vs[1];
				String ISIN = vs[2].trim();
				String assetType = vs[3];
				String currency = vs[4];

				int contractSize = (int) Double.parseDouble(vs[5]);
				double tickSize = Double.parseDouble(vs[6]);
				double tickValue = Double.parseDouble(vs[7]);
				int digits = (int) Double.parseDouble(vs[8]);
				int minVolume = (int) Double.parseDouble(vs[9]);
				int maxVolume = (int) Double.parseDouble(vs[10]);
				int stepVolume = (int) Double.parseDouble(vs[11]);

				// String category = vs[12];
				Date emissionDate = ((Date) FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F10).parse(vs[12]));
				Date expirationDate = ((Date) FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F10)
						.parse(vs[13]));
				OptionStyles style = OptionStyles.getByValue(vs[14]);
				OptionRights optionRight = OptionRights.getByValue(vs[15]);
				double strikePrice = Double.parseDouble(vs[16]);

				SecurityCategory secCat = SecurityCategory.getByISIN(ISIN);
				AssetTypes objAsset = null;// AssetTypes.getByValue(tipoAtivoObjeto);
				switch (assetType) {
				case "32":
					objAsset = AssetTypes.STOCK;
					secCat = SecurityCategory.STOCK;
					break;
				case "33":
					objAsset = AssetTypes.FUTURE;
					secCat = SecurityCategory.FUTURE;
					break;
				default:
					objAsset = AssetTypes.STOCK;
					secCat = SecurityCategory.STOCK;
				}

				Security security = new Security(symbol);
				security.setDescription(description);

				security.setStockExchangeId(exchange);
				security.setIsin(ISIN); 

				SecurityInfo info = new SecurityInfo(security, secCat);
				info.setCurrencyId(currencies.get(currency));
				info.setContractSize(contractSize);
				info.setTickSize(tickSize);
				info.setTickValue(tickValue);
				info.setDigits(digits);
				info.setMinVolume(minVolume);
				info.setMaxVolume(maxVolume);
				info.setStepVolume(stepVolume);
				info.setObjectAsset(objAsset);
				info.setCategory(secCat);
				//info.setEmissionDate(emissionDate == null ? defDate : emissionDate);
				//info.setExpirationDate(expirationDate == null ? defDate : expirationDate);
				info.setOptionStyle(style == null ? OptionStyles.AMERICAN : style);
				info.setOptionRight(optionRight == null ? OptionRights.Call : optionRight);
				info.setStrikePrice(strikePrice);

				// info = (SecurityInfo) DBFacade.getInstance()._persist(info);

				security.setSecurityInfoId(info);
				// DBFacade.getInstance().addToBatch(security);
				DBFacade.getInstance()._persist(security);

				report.count(repName);
			}

			// DBFacade.getInstance().finalizeBatch();
			DBFacade.getInstance().commit();

		} catch (DataBaseException e) {
			e.printStackTrace();
			System.out.println(line);
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
	}

	private void addHistoricalQuotes(String fileName) {
		String repName = "HistoricalQuotes";
		try {
			report.startReport(repName);

			HashMap<String, Security> securities = new HashMap<String, Security>();
			DBFacade.getInstance().queryAsMap("Security.findAll", securities, Security.class, "getSymbol");
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FormatterTypes.DATE_F8.getFormat());
			
			List<DBObject> quotes = new ArrayList<DBObject>(100000);
			CSV csv = new CSV(fileName, ";", "#");
			while (csv.hasNext()) {
				// System.out.println("Carregado!");
				/*
				 * Symbol;stockcode;Isin;companyName;CurrencyID;MarketTypeID;
				 * QDatetime;Fatquote;
				 * QHigh;QLow;QOpen;QClose;QAsk;QBid;PreMed;Volume;TradedUnits; TradedQuantity;
				 * TradableUnits;QuoteFactor;ExPrice;ExPoint;ExDate;Prazot; StockExchange\n
				 */
				String[] vs = csv.readLine();
				MarketTypes mType = MarketTypes.getByValue(Integer.parseInt(vs[5]));
				
				Optional<Security> security = securities.values()
						.parallelStream()
						.filter(s -> s.getSymbol().equalsIgnoreCase(vs[0]) && s.getSecurityInfoId().getMarketType() == mType)
						.findFirst();				
				
				if (security.isPresent()) {
					
					LocalDate QDatetime = LocalDate.parse(vs[6], dateTimeFormatter);
					BigDecimal QHigh = (BigDecimal) FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[8]);
					BigDecimal QLow = (BigDecimal) FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[9]);
					BigDecimal QOpen = (BigDecimal) FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[10]);
					BigDecimal QClose = (BigDecimal) FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[11]);
					BigDecimal QAsk = (BigDecimal) FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[12]);
					BigDecimal QBid = (BigDecimal) FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[13]);
					BigDecimal QAvg = (BigDecimal) FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[14]);
					BigDecimal Volume = (BigDecimal) FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[15]);
					Long TradedUnits = (Long) FormatterFactory.getFormatter(FormatterTypes.LONG).parse(vs[16]);
					Long TradedQuantity = (Long) FormatterFactory.getFormatter(FormatterTypes.LONG).parse(vs[17]);

					HistoricalQuote hqte = new HistoricalQuote();
					hqte.setSecurityId(security.get());
					hqte.setqDateTime(QDatetime);
					hqte.setAsk(QAsk);
					hqte.setBid(QBid);
					hqte.setClose(QClose);
					hqte.setHigh(QHigh);
					hqte.setLow(QLow);
					hqte.setOpen(QOpen);
					hqte.setAvgPrice(QAvg);
					hqte.setVolume(Volume);
					hqte.setTradedUnits(TradedUnits);
					hqte.setTradedQuantity(TradedQuantity);

					quotes.add(hqte);
					
					if(quotes.size() == 100000) {
						
						List<DBObject> tQuotes = new ArrayList(quotes);
						
						new Thread(new Runnable() {
							@Override
							public void run() {
								tQuotes.forEach(q -> {
									DBFacade.getInstance().addToBatch(q);
									report.count(repName);
								});
							}							
						}).start();
												
						quotes.clear();
					}
					
				}
			}
			
			quotes.forEach(q -> {
				DBFacade.getInstance().addToBatch(q);
				report.count(repName);
			});
			
			DBFacade.getInstance().finalizeBatch();

		} catch (DataBaseException e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
	}

	private void addContractAdjusts(String fileName) {
		String repName = "ContractAdjust";
		try {
			report.startReport(repName);
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(FormatterTypes.DATE_F8.getFormat());
			
			HashMap<String, Security> securities = new HashMap<String, Security>();
			DBFacade.getInstance().queryAsMap("Security.findAll", securities, Security.class, "getSymbol");
						
			CSV csv = new CSV(fileName, ";");
			while (csv.hasNext()) {
				//date, code, description, expiration, lastPrice, price, variation, settlementContractPrice
				String[] vs = csv.readLine();
				ContractAdjust adjust = new ContractAdjust();
				
				LocalDate date = LocalDate.parse(vs[0], dateTimeFormatter);
				
				String expCode = vs[3];
				if(expCode.startsWith("000")) continue;
				
				if(vs[3].length() > 3) {
					expCode = vs[3].substring(0, 3);
					int yearDigit = Integer.parseInt(vs[3].substring(3, 4));
					int yearDateDigit = Integer.parseInt(("" + date.getYear()).substring(3, 4));
					
					switch(expCode) {
					case "JAN":
						expCode = FutureSeries.F.name();
						break;
					case "FEV":
						expCode = FutureSeries.G.name();
						break;
					case "MAR":
						expCode = FutureSeries.H.name();
						break;
					case "ABR":
						expCode = FutureSeries.J.name();
						break;
					case "MAI":
						expCode = FutureSeries.K.name();
						break;
					case "JUN":
						expCode = FutureSeries.M.name();
						break;
					case "JUL":
						expCode = FutureSeries.N.name();
						break;
					case "AGO":
						expCode = FutureSeries.Q.name();
						break;
					case "SET":
						expCode = FutureSeries.U.name();
						break;
					case "OUT":
						expCode = FutureSeries.V.name();
						break;
					case "NOV":
						expCode = FutureSeries.X.name();
						break;
					case "DEZ":
						expCode = FutureSeries.Z.name();
						break;
					}
					
					int year = date.getYear();
					if(yearDigit != yearDateDigit) {
						while(Integer.parseInt((++year +"").substring(3, 4)) != yearDigit);						
					}
					
					expCode = expCode + (""+year).substring(2, 4);
				}
								
				//System.out.println(vs[0] + " " + vs[3] + " " + vs[1] + expCode + " ");
				String symbol = vs[1] + expCode;
				if(securities.containsKey(symbol)) {
					adjust.setSecurityId(securities.get(symbol));
				}
				
				adjust.setDate(date);
				adjust.setExpirationCode(expCode);
				adjust.setAdjPrice(Double.parseDouble(vs[5]));
				adjust.setVariation(Double.parseDouble(vs[6]));
				adjust.setAdjContractPrice(Double.parseDouble(vs[7]));
				
				DBFacade.getInstance().addToBatch(adjust);
				report.count(repName);
			}
			DBFacade.getInstance().finalizeBatch();

		} catch (Exception | DataBaseException e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

	}	
	
	private void addHolidays(String fileName) {
		String repName = "Holidays";
		report.startReport(repName);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				
		CSV csv = new CSV(fileName, ",", "#");
		while (csv.hasNext()) {
			try {
				String[] vs = csv.readLine();
				Holiday h = new Holiday();
				h.setDate(LocalDate.parse(vs[0], dateFormatter));
				h.setDescription(vs[2]);
				
				DBFacade.getInstance().addToBatch(h);
				report.count(repName);
			} catch (Exception e) {
				report.reportError(repName, e.getMessage());
				Logging.getInstance().log(getClass(), "Erro ao adicionar Feriado.", e, Level.ERROR, false);
			}
		}
		
		DBFacade.getInstance().finalizeBatch();
	}
	

	//
	// private void addMarketCodes(String fileName) {
	// String repName = "MarketCodes";
	// try {
	// report.startReport(repName);
	//
	// HashMap<String, Company> companies = new HashMap<String, Company>();
	// DBFacade.getInstance().queryAsMap("Company.findAll", companies,
	// Company.class, "getMarketName");
	//
	// HashMap<String, MarketCode> mCodes = new HashMap<String, MarketCode>();
	// DBFacade.getInstance().queryAsMap("MarketCode.findAll", mCodes,
	// MarketCode.class, "getMarketCode");
	//
	// CSV csv = new CSV(fileName, ";", "#");
	// while(csv.hasNext()) {
	//
	// String[] vs = csv.readLine();
	//
	// String code = vs[0];
	// String marketName = vs[1];
	//
	// if(companies.containsKey(marketName) && !mCodes.containsKey(code)) {
	// try{
	// MarketCode mc = new MarketCode(companies.get(marketName), code);
	// mc.add();
	// //DBFacade.getInstance().addToBatch(mc);
	// report.count(repName);
	// }catch(Exception e) {
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	// }
	// DBFacade.getInstance().finalizeBatch();
	//
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	//
	// private void addDatesInCodes(String fileName) {
	// String repName = "MarketCodes";
	// try {
	// report.startReport(repName);
	//
	// HashMap<String, Security> securities = new HashMap<String, Security>();
	// DBFacade.getInstance().queryAsMap("Security.findAll", securities,
	// Security.class, "getSymbol");
	//
	// HashMap<String, MarketCode> mCodesAux = new HashMap<String,
	// MarketCode>();
	// HashMap<String, MarketCode> mCodes = new HashMap<String, MarketCode>();
	// DBFacade.getInstance().queryAsMap("MarketCode.findAll", mCodes,
	// MarketCode.class, "getMarketCode");
	//
	// CSV csv = new CSV(fileName, ";", "#");
	// while(csv.hasNext()) {
	//
	// String[] vs = csv.readLine();
	//
	// String symbol=vs[0];
	// String code = vs[1];
	// Date date =
	// (Date)FormatterFactory.getFormatter(FormatterTypes.DATE_F8).parse(vs[2]);
	//
	// if(mCodes.containsKey(code)) {
	// try{
	// mCodes.get(code).setCodeDate(date);
	// mCodesAux.put(code, mCodes.get(code));
	// report.count(repName);
	// }catch(Exception e) {
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	// if(securities.containsKey(symbol)) {
	// try {
	// securities.get(symbol).setSymbolDate(date);
	// }catch(Exception e) {
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	// }
	//
	// System.out.println("atualizando mcodes");
	// for(String k : mCodesAux.keySet()) {
	// DBFacade.getInstance().addToBatch(mCodesAux.get(k));
	// }
	// DBFacade.getInstance().finalizeBatch();
	//
	// System.out.println("Atualizando secs");
	// for(String k : securities.keySet()) {
	// DBFacade.getInstance().addToBatch(securities.get(k));
	// }
	// DBFacade.getInstance().finalizeBatch();
	//
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	//
	//
	// private void addSecurityTypes(String fileName) {
	// String repName = "SecurityTypes";
	// try {
	// report.startReport(repName);
	//
	// CSV csv = new CSV(fileName, ";");
	// while(csv.hasNext()) {
	// String[] vs = csv.readLine();
	// String category = vs[0];
	// String code=vs[3];
	// String descr=vs[2];
	//
	// SecurityCategory secCat = SecurityCategory.getByValue(category);
	//
	// if(secCat!=null) {
	// SecurityType st = new SecurityType(code, descr, secCat);
	// st.add();
	// DBFacade.getInstance().addToBatch(st);
	// }
	//
	// report.count(repName);
	// }
	// DBFacade.getInstance().finalizeBatch();
	//
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	//
	// }
	//

	private void addIsin(String fileName) {
		String repName = "Isin";
		try {
			report.startReport(repName);
			// DBFacade.getInstance().beginTransaction();
			
			HashMap<String, Currency> currencies = new HashMap<String, Currency>();
			//HashMap<String, MarketCode> mCodes = new HashMap<String, MarketCode>();
			//HashMap<String, SecurityType> sTypes = new HashMap<String, SecurityType>();
			/*DBFacade.getInstance().queryAsMap("Currency.findAll", currencies, Currency.class, "getCurrencyID");
			DBFacade.getInstance().queryAsMap("MarketCode.findAll", mCodes, MarketCode.class, "getMarketCode");
			DBFacade.getInstance().queryAsMap("SecurityType.findAll", sTypes, SecurityType.class, "getCode");

			CSV csv = new CSV(fileName, ";");
			while (csv.hasNext()) {
				String[] vs = csv.readLine();
				String ISIN = vs[0];
				String emissor = vs[1];
				String descricao = vs[2];
				String moeda = vs[3];
				String tipoAtivoObjeto = vs[4];
				String situacao = vs[6];

				if (mCodes.containsKey(emissor) && currencies.containsKey(moeda)) {

					Isin isin = new Isin(ISIN, currencies.get(moeda), mCodes.get(emissor).getCompanyID());
					isin.setDescription(descricao);

					//isin.setObjAssetType(AssetTypes.getByValue(tipoAtivoObjeto));

					GeneralStatus sts = GeneralStatus.ACTIVE;
					if (situacao.equalsIgnoreCase("i")) {
						sts = GeneralStatus.INACTIVE;
					}
					isin.setStatus(sts);
					// isin.add();
					DBFacade.getInstance().addToBatch(isin);
					report.count(repName);
				}
			}
			// DBFacade.getInstance().commit();
			DBFacade.getInstance().finalizeBatch();*/
		} catch (Exception e) {
			e.printStackTrace();
			report.reportError(repName, e.getMessage());
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

	}

	//
	// private void addEarnings(String fileName) {
	// String repName = "Earnings";
	// try {
	// report.startReport(repName);
	//
	// String query =
	// " select s.securityID, s.Symbol, s.symbolDate "+
	// " from Company c inner join Isin i on c.companyID=i.companyID" +
	// " inner join Stock s on s.isinID=i.isinID" +
	// " where c.MarketName = '@{0}' and s.stockSpecification = '@{1}'" +
	// " order by s.symbolDate desc";
	// /*" except " +
	// " select s.Symbol, s.securityID"+
	// " from Company c inner join Isin i on c.companyID=i.companyID" +
	// " inner join Stock s on s.isinID=i.isinID" +
	// " where c.MarketName = '@{0}' and s.stockSpecification = '@{1}' and
	// s.symbolDate > '@{2}'";
	// */
	//
	// DateTimeFormatter df8 = (DateTimeFormatter)
	// FormatterFactory.getFormatter(FormatterTypes.DATE_F8);
	//
	// CSV csv = new CSV(fileName, ";", "#");
	// while(csv.hasNext()) {
	// String[] vs = csv.readLine();
	//
	// String marketName = vs[0];
	// String secSpeciID = vs[1];
	// EarningType et = EarningType.getByValue(vs[5]);
	// BigDecimal eVal = (BigDecimal)
	// FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[3]);
	// Date exDate = (Date)
	// FormatterFactory.getFormatter(FormatterTypes.DATE_F10).parse(vs[6]);
	// BigDecimal exPrice = (BigDecimal)
	// FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[8]);
	//
	// String qaux = query.replace("@{0}", marketName).replace("@{1}",
	// secSpeciID).replace("@{2}", df8.format(exDate));
	//
	// List<Object> l = DBFacade.getInstance().queryNative(qaux);
	// Security sec = null;
	// if(l!=null && !l.isEmpty()) {
	// BigInteger sID=new BigInteger("0");
	// for(Object o:l) {
	// Object[] v=(Object[])o;
	// Date d = (Date) v[2];
	// int comp=exDate.compareTo(d);
	// if(comp==0 || comp==1) {
	// sID=(BigInteger) v[0];
	// break;
	// }
	//
	// }
	//
	// //sec = new Stock().loadByKey(sID.longValue());
	// HashMap<String,Object> params=new HashMap<String,Object>();
	// params.put("securityID", sID.longValue());
	// List<Security> ls = (List<Security>)
	// DBFacade.getInstance().queryNamed("Stock.findByStockID", params);
	// if(ls!=null && !ls.isEmpty()) {
	// sec=ls.get(0);
	// }
	//
	// }
	//
	// if(et!=null && sec!=null) {
	// Earning er = new Earning(et, eVal, exDate, exPrice, sec);
	// DBFacade.getInstance().addToBatch(er);
	// report.count(repName);
	// }
	//
	// }
	//
	// DBFacade.getInstance().finalizeBatch();
	//
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	//
	//
	// private void addBuyTickData(String fileName) {
	// String repName = "BuyTickData";
	// try {
	// report.startReport(repName);
	//
	// HashMap<String, Security> securities = new HashMap<String, Security>();
	// DBFacade.getInstance().queryAsMap("Security.findAll", securities,
	// Security.class, "getSymbol");
	// CSV csv = new CSV(fileName, ";","RT", "RH");
	// while(csv.hasNext()) {
	// String[] vs = csv.readLine();
	// MarketOrder mo = new MarketOrder();
	//
	// Date sessionDate = (Date)
	// FormatterFactory.getFormatter(FormatterTypes.DATE_F8).parse(vs[0]);
	// BigDecimal price = null;
	// String externalID = "";
	// Security securityID = null;
	// int volume = 0;
	// Date orderDate = null;
	// Date expirationDate = null;
	// OrderStatus orderStatus = null;
	//
	// //layout dos arquivos foi alterado devido a mudanca para o sistema de
	// negociacao Puma
	// if(sessionDate.compareTo(new Date(2013, 4, 5))<=0) {///....!fk
	// // -----------------------------------------------------------
	// // Coluna Posição Inicial Tamanho Descrição
	// // -----------------------------------------------------------
	// // Data Sessão 1 10 Data da Sessão
	// // Papel 50 12 Código do Instrumento
	// // Sequência 63 10 Número de Sequência da Oferta
	// // Preço Of.Compra 74 20 Preço da Oferta
	// // Qtd.Total Of.Compra 95 18 Quantidade Total
	// // Qtd.Negociada Of.Compra 114 18 Quantidade Negociada
	// // Hora Prioridade 133 15 Hora de registro da oferta no sistema (com a
	// precisão Tandem, no formato, HH:MM:SS.NNNNNN), utilizada como indicadora
	// de prioridade
	// // Data de Entrada Of.Compra 149 19 Data/Hora de Entrada da Oferta
	// // Estado Of.Compra 169 1 Indicador de estado da ordem " " - aceite "E" -
	// eliminada (EOC) "G" - congelada "O" - cancelada seguido de uma ação no
	// instrumento (por ex- Papel Reservado) "X" - totalmente executada "M" -
	// modificada "D" - disparada "A" - anulada (corretora) "R" - rejeitada pelo
	// Surveillance, seguido de um congelamento. Após 04/03/2013 devido a
	// migração para o PUMA alguns ativos estarão valorizados com: 0 - Novo / 1
	// - Negociada parcialmente / 2 - Totalmente executada / 4 - Cancelada / 5 -
	// Modificada / 8 - Rejeitada / C - Expirada
	// // Data Modif. Of.Compra 171 10 Data de Modificação da Oferta
	// // Nr.Of.Compra Modif. 182 10 Número da Oferta Modificada
	// // Hora Fim Tratam. Of.Compra 193 19 Hora de Fim de Tratamento (contém
	// Hora da Anulação quando Indicador de Estado da Orderm for igual a "A")
	// securityID = securities.get(vs[1]);
	// externalID = vs[2];
	// price = (BigDecimal)
	// FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[3]);
	// volume = (Integer)
	// FormatterFactory.getFormatter(FormatterTypes.INT).parse(vs[4]);
	// orderDate = (Date)
	// FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F9).parse(vs[5].split("
	// ")[0] + vs[6]);
	// expirationDate = (Date)
	// FormatterFactory.getFormatter(FormatterTypes.DATE_F8).parse(vs[1]);
	// orderStatus = OrderStatus.getByValue(vs[8]);
	// }else {
	// // -----------------------------------------------------------
	// // Coluna Posição Inicial Tamanho Descrição
	// // -----------------------------------------------------------
	// // Data Sessão 1 10 Data da Sessão
	// // Símbolo do Instrumento 12 50 Símbolo do Instrumento
	// // Sentido Of.Compra 63 1 Indicador de sentido da ordem: "1" - compra /
	// "2" - venda
	// // Sequência 65 10 Número de Sequência da Oferta
	// // GenerationID - Of.Compra 76 10 Número de geração (GenerationID) da
	// Oferta de Compra. Quando um negócio for gerado por 2 ofertas com
	// quantidade escondida e isso gerar "n" linhas será gravado aqui a maior
	// geração.
	// // Cód do Evento da Of.Compra 87 3 Código do Evento da Ordem: 1 - New / 2
	// - Update / 3 - Cancel - Solicitado pelo participante / 4 - Trade / 5 -
	// Reentry - Processo interno (quantidade escondida) / 6 - New Stop Price /
	// 7 - Reject / 8 - Remove - Removida pelo Sistema (final de dia ou quando é
	// totalmente fechada) / 9 - Stop Price Triggered / 11 - Expire - Oferta com
	// validade expirada.
	// // Hora Prioridade 91 15 Hora de registro da oferta no sistema (no
	// formato, HH:MM:SS.NNN), utilizada como indicadora de prioridade
	// // Ind de Prioridade Of.Compra 107 10 Indicador de Prioridade. Além do
	// preço é a ordem para aparecer no Order Book.
	// // Preço Of.Compra 118 20 Preço da Oferta
	// // Qtd.Total Of.Compra 139 18 Quantidade Total da Oferta. Se tiver
	// alteração ela reflete a nova quantidade.
	// // Qtd.Negociada Of.Compra 158 18 Quantidade Negociada
	// // Data Oferta Compra 177 10 Data de Inclusão da Oferta. Pode ser uma
	// data anterior à Data da Sessão, quando se tratar de uma Oferta com
	// Validade.
	// // Data de Entrada Of.Compra 188 19 Data/Hora de Entrada da Oferta
	// (formato: DD/MM/AAAA HH:MM:SS)
	// // Estado Of.Compra 208 1 Indicador de estado da ordem: 0 - Novo / 1 -
	// Negociada parcialmente / 2 - Totalmente executada / 4 - Cancelada / 5 -
	// Modificada / 8 - Rejeitada / C - Expirada
	// // Condição Oferta 210 1 Código que identifica a condição da oferta. Pode
	// ser: 0 - Oferta Neutra - é aquela que entra no mercado e não fecha com
	// oferta existente. / 1 - Oferta Agressora - é aquela que ingressa no
	// mercado para fechar com uma oferta existente. / 2 - Oferta Agredida - é a
	// oferta (existente) que é fechada com uma oferta agressora.
	//
	// securityID = securities.get(vs[1]);
	// externalID = vs[2];
	// price = (BigDecimal)
	// FormatterFactory.getFormatter(FormatterTypes.BIGDECIMAL).parse(vs[1]);
	// volume = (Integer)
	// FormatterFactory.getFormatter(FormatterTypes.INT).parse(vs[1]);
	// orderDate = (Date)
	// FormatterFactory.getFormatter(FormatterTypes.DATE_F8).parse(vs[1]);
	// expirationDate = (Date)
	// FormatterFactory.getFormatter(FormatterTypes.DATE_F8).parse(vs[1]);
	// orderStatus = OrderStatus.CANCELED;
	// }
	//
	// mo.setSide(Side.BUY);
	// mo.setPrice(price);
	// mo.setSecurityID(securityID);
	// mo.setVolume(volume);
	// mo.setOrderDateTime(orderDate);
	// mo.setExpirationDate(expirationDate);
	// mo.setOrderStatus(orderStatus);
	// mo.add();
	//
	// report.count(repName);
	// }
	//
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	//
	// private void addSellTickData(String fileName) {
	// String repName = "SellTickData";
	// try {
	// report.startReport(repName);
	//
	// DBFacade.getInstance().beginTransaction();
	//
	// report.count(repName);
	//
	// DBFacade.getInstance().commit();
	//
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	//
	// private void addTradeTickData(String FileName) {
	// String repName = "TradeTickData";
	// try {
	// report.startReport(repName);
	//
	// DBFacade.getInstance().beginTransaction();
	//
	// report.count(repName);
	//
	// DBFacade.getInstance().commit();
	//
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// report.reportError(repName, e.getMessage());
	// Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	// }
	// }
	//
	// private void addUsers(String fileName){
	//
	// }

}
