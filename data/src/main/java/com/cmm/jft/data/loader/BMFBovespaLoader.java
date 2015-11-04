/**
 * 
 */
package com.cmm.jft.data.loader;

import java.io.File;
import java.util.ArrayList;

import org.apache.log4j.Level;

import com.cmm.jft.core.enums.LoadableSource;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>BMFBovespaLoader.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Jul 10, 2013 7:38:05 PM
 *
 */
public abstract class BMFBovespaLoader {

	/**
	 * @param colDelimiter
	 */
	public BMFBovespaLoader(String colDelimiter) {
		super();
	}

	abstract void importHistoricalData(String fileName);

	abstract void importNegociationData(String fileName);

	abstract void importBuyData(String fileName);

	abstract void importSellData(String fileName);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.loader.Loadable#importFiles(java.lang.String,
	 * com.cmm.jft.core.enums.LoadableSource)
	 */
	public String importFiles(String dirName, LoadableSource lSource) {
		String r = "Importing files from: " + dirName + "\n";
		try {
			File dir = new File(dirName);
			for (File f : dir.listFiles()) {

				r += importFile(f.getAbsolutePath(), lSource) + "\n";

			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}
		return r;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.cmm.jft.data.loader.Loadable#importFile(java.lang.String,
	 * com.cmm.jft.core.enums.LoadableSource)
	 */
	public String importFile(String fileName, LoadableSource lSource) {
		String r = "Importing Type: " + lSource + "\n";
		switch (lSource) {
		case BVMFBOVESPA_Historical:
			r += importHistorical(fileName);
			break;

		case BVMFBOVESPA_TickData_Buy:
			r += importTickDataBuy(fileName);
			break;

		case BVMFBOVESPA_TickData_Negociation:
			importTickDataNeg(fileName);
			break;

		case BVMFBOVESPA_TickData_Sell:
			importTickDataSell(fileName);
			break;

		}
		return r;
	}

	private String importHistorical(String fileName) {
		String r = "";
		int rows = 0;
		int stopRow = 0;
		try {
			File f = new File(fileName);
			r += "Importing file: " + f.getName() + "\n";

			ArrayList<String[]> rowsFile = null;// = new
												// CSV("\n").readFile(fileName,
												// colDelimiter);

			DBFacade.getInstance().beginTransaction();

			// /////////////////////////////////////////////////////////////////
			for (String[] line : rowsFile) {
				rows++;
			}

			DBFacade.getInstance().commit();

		} catch (NullPointerException e) {
			stopRow = rows;
			rows = 0;
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		} catch (DataBaseException e) {
			try {
				DBFacade.getInstance().rollback();
			} catch (DataBaseException e1) {
				e1.printStackTrace();
			}
			stopRow = rows;
			rows = 0;
			e.printStackTrace();
			Logging.getInstance().log(this.getClass(), e, Level.ERROR);
		}

		r += "Rows imported: " + rows + "\n"
				+ (stopRow > 0 ? "Stoped at: " + stopRow : "") + "\n";
		return r;
	}

	private String importTickDataNeg(String fileName) {
		String r = "";

		return r;
	}

	private String importTickDataBuy(String fileName) {
		String r = "";

		return r;
	}

	private String importTickDataSell(String fileName) {
		String r = "";

		return r;
	}

	// private static String exchangeMarketID = "BMFBOVESPA";
	//
	// public StockExchange addExchange() {
	// StockExchange ex =
	// DBFacade.getInstance().findByExchangeKey(exchangeMarketID);
	// if(ex==null) {
	// ex=new StockExchange(exchangeMarketID);
	// ex.setCountry("BR");
	// ex.setExchangeName("BM&FBOVESPA");
	// ex = DBFacade.getInstance().persistExchange(ex);
	//
	// //adiciona os tipos de mercado
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(10L, "VISTA"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(12L,
	// "EXERC�CIO DE OP��ES DE COMPRA"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(13L,
	// "EXERC�CIO DE OP��ES DE VENDA"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(17L, "LEIL�O"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(20L,
	// "FRACION�RIO"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(30L, "TERMO"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(50L,
	// "FUTURO COM RETEN��O DE GANHO"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(60L,
	// "FUTURO COM MOVIMENTA��O CONT�NUA"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(70L,
	// "OP��ES DE COMPRA"));
	// DBFacade.getInstance().persistMarkettype(new MarketTypes(80L,
	// "OP��ES DE VENDA"));
	// }
	//
	// return ex;
	//
	// }
	//
	// public void addCompanies(String filename) {
	//
	// try {
	// StockExchange ex =
	// DBFacade.getInstance().findByExchangeKey(exchangeMarketID);
	// Scanner sc = new Scanner(new File(filename));
	// sc.useDelimiter("\n");
	// sc.next();
	// while(sc.hasNext()) {
	// String[] cols = sc.next().split(";");
	// List l = DBFacade.getInstance().queryNamed(
	// "Company.findByStockCode",
	// "stockcode="+removeWhite(cols[1],0));
	//
	// if(l.isEmpty()) {
	// Company c = new Company();
	// // c.setStockcode(removeWhite(cols[1]));
	// c.setName(removeWhite(cols[3],0));
	// c.setCompanyName(removeWhite(cols[2],0));
	// c.setExchangeID(ex);
	// c.setOnShares(Long.parseLong(cols[10]));
	// c.setPnShares(Long.parseLong(cols[12]));
	//
	// DBFacade.getInstance().persistCompany(c);
	// }
	// }
	//
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// } catch(ArrayIndexOutOfBoundsException e) {
	// e.printStackTrace();
	// }
	//
	// }
	//
	// private String removeWhite(String txt,int line) {
	// try {
	// while(txt.charAt(txt.length()-1)== ' ') {
	// txt = txt.substring(0, txt.length()-1);
	// }
	// }catch(StringIndexOutOfBoundsException e) {
	// System.out.println("Empresa " + txt +
	// " n�o possui codigo de negociacao. Linha:"+line);
	// }
	// return txt;
	// }
	//
	// public void storeFileOnDB(File[] historicalFiles) {
	//
	// HashMap<String, Company> companies;
	//
	// StockExchange ex = addExchange();
	// HashMap<Integer,MarketTypes> types = new HashMap<Integer, MarketTypes>();
	// types.put(10, DBFacade.getInstance().findByMarkettypeKey(10L));
	// types.put(12, DBFacade.getInstance().findByMarkettypeKey(12L));
	// types.put(13, DBFacade.getInstance().findByMarkettypeKey(13L));
	// types.put(17, DBFacade.getInstance().findByMarkettypeKey(17L));
	// types.put(20, DBFacade.getInstance().findByMarkettypeKey(20L));
	// types.put(30, DBFacade.getInstance().findByMarkettypeKey(30L));
	// types.put(50, DBFacade.getInstance().findByMarkettypeKey(50L));
	// types.put(60, DBFacade.getInstance().findByMarkettypeKey(60L));
	// types.put(70, DBFacade.getInstance().findByMarkettypeKey(70L));
	// types.put(80, DBFacade.getInstance().findByMarkettypeKey(80L));
	//
	// for(File file:historicalFiles) {
	// // System.out.println(file.getName());
	// if(Integer.parseInt(file.getName().substring(10, 14))>=2011)continue;
	//
	// companies = new HashMap<String, Company>();
	// System.out.println("Lendo arquivo: " + file.getName());
	//
	// int regcont=0;
	// int linecount=0;
	// Scanner sc;
	// String line;
	// try {
	// sc = new Scanner(file);
	// sc.useDelimiter("\n");
	// //para cada linha do arquivo
	// while(sc.hasNext()) {
	//
	// line = sc.next();
	// linecount++;
	// //registros que iniciam com 01....
	// if(line.startsWith("01")) {
	//
	// //tenta encontrar o papel e a empresa
	// String symbol = removeWhite(line.substring(12, 24),
	// linecount).replace("'", "''");
	// String stockcode = removeWhite(line.substring(12, 16),
	// linecount).replace("'", "''");
	// String companyName = removeWhite(line.substring(27, 39),
	// linecount).replace("'", "''");
	// int year, month, day;
	// year = Integer.parseInt(line.substring(2, 6));
	// month = Integer.parseInt(line.substring(6, 8));
	// day = Integer.parseInt(line.substring(8, 10));
	//
	// String datetime = String.format("%02d-%02d-%02d", year, month, day);
	//
	// //nao adiciona a linha caso falte alguma informacao
	// if(symbol.isEmpty() || stockcode.isEmpty() ||
	// companyName.isEmpty()||datetime.isEmpty())continue;
	//
	// if(!companies.containsKey(companyName)) {
	// Company c = new Company();
	// c.setExchangeID(ex);
	// // c.setStockcode(stockcode);
	// c.setName(companyName);
	// c.setCompanyName(companyName);
	// c.setOnShares(0L);
	// c.setPnShares(0L);
	// companies.put(companyName, c);
	// }
	//
	//
	// if(!companies.get(companyName).getImportStocks().containsKey(symbol)) {
	// Security exitem = new Stock();
	// exitem.setExchangeID(ex);
	// // ((Stock)exitem).setCompanyid(companies.get(companyName));
	// exitem.setSymbol(symbol);
	// exitem.setCurrencyID(line.substring(52, 56).trim());
	// exitem.setDescription("");
	// exitem.setMarketTypeID(types.get(Integer.parseInt(line.substring(24,
	// 27))));
	// exitem.setIsin(removeWhite(line.substring(230, 242), linecount));
	// exitem.setName(companyName);
	// exitem.setSymbol(removeWhite(line.substring(12, 24), linecount));
	// companies.get(companyName).getImportStocks().put(symbol, (Stock) exitem);
	// }
	//
	// if(!companies.get(companyName).getImportStocks().get(symbol).getImportQuotes().containsKey(symbol+datetime))
	// {
	// Quote quote = new Quote();
	// //quote.setExchangeitemid(companies.get(companyName).getImportStocks().get(symbol));
	// quote.setQDateTime(new GregorianCalendar(year, month-1, day).getTime());
	// quote.setFatquote(Integer.parseInt(line.substring(210, 217)));
	// quote.setQHigh(new BigDecimal(Float.parseFloat(line.substring(69,
	// 82))/100f));
	// quote.setQLow(new BigDecimal(Float.parseFloat(line.substring(82,
	// 95))/100f));
	// quote.setQOpen(new BigDecimal(Float.parseFloat(line.substring(56,
	// 69))/100f));
	// quote.setQClose(new BigDecimal(Float.parseFloat(line.substring(108,
	// 121))/100f));
	// quote.setQAsk(new BigDecimal(Float.parseFloat(line.substring(121,
	// 134))/100f));
	// quote.setQBid(new BigDecimal(Float.parseFloat(line.substring(134,
	// 147))/100f));
	// quote.setVolume(Long.parseLong(line.substring(170, 188)));
	// quote.setTradedUnits(Long.parseLong(line.substring(147, 152)));
	// quote.setTradedQuantity(Long.parseLong(line.substring(152, 170)));
	//
	// companies.get(companyName).getImportStocks().get(symbol).getImportQuotes().put(symbol+datetime,
	// quote);
	// //exitem.getQuoteSet().add(quote);
	// //exitens.put(exitem.getSymbol(), exitem);
	// regcont++;
	// }
	// }
	//
	// }
	//
	// } catch (FileNotFoundException e) {
	// e.printStackTrace();
	// }
	// catch(Exception e) {
	// e.printStackTrace();
	// }
	//
	//
	// int stocks=0;
	// int quotes=0;
	// for(String k:companies.keySet()) {
	// System.out.println("Adicionando " + k);
	// Company comp = DBFacade.getInstance().persistCompany(companies.get(k));
	//
	// for(String st:companies.get(k).getImportStocks().keySet()) {
	// companies.get(k).getImportStocks().get(st).setCompanyID(comp);
	// Security exi =
	// DBFacade.getInstance().persistExchangeitem(companies.get(k).getImportStocks().get(st));
	//
	// for(Quote
	// sq:companies.get(k).getImportStocks().get(st).getImportQuotes().values())
	// {
	// sq.setExchangeItemID(exi);
	// DBFacade.getInstance().persistQuote(sq);
	// }
	// quotes+=companies.get(k).getImportStocks().get(st).getImportQuotes().size();
	// }
	// stocks+=companies.get(k).getImportStocks().size();
	// }
	//
	//
	// System.out.println("Arquivo "+ file.getName()
	// +" inserido com sucesso, n� de registros adicionados: " + regcont +
	// " linhas: " + linecount);
	//
	// System.out.println("Empresas:"+companies.size());
	// System.out.println("Papeis:"+stocks);
	// System.out.println("Precos:"+quotes);
	//
	// }
	//
	//
	// }

}
