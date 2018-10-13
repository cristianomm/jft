/**
 * 
 */
package com.cmm.jft.core.test;





/**
 * <p>
 * <code>TestEntities.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 04/08/2013 04:53:19
 *
 */
public class TestEntities {
//
//	/**
//	 * @param args
//	 * @throws IllegalAccessException
//	 * @throws IllegalArgumentException
//	 * @throws InvocationTargetException
//	 */
//	public static void main(String[] args) {
//
//		DBFacade.getInstance();
//		Logging.getInstance().printStackTrace(true);
//		testTrading();
//
//		System.exit(0);
//
//	}
//
//	public static void addTestData() {
//		try {
//
//			Country c = new Country("BRA", "Brazil", 0d, 190000L, "SA").add();
//			StockExchange ex = new StockExchange("BVMF", "BVMF", c);
//			ex.setExchangeName("Bovespa");
//			ex = ex.add();
//
//			Currency curr = new Currency("BRL");
//			curr.setCurrSymbol("R$");
//			curr.setDescription("Real");
//			curr = curr.add();
//			// System.exit(0);
//
//			AccountTypes t1 = new AccountTypes("Ativo");
//			t1 = t1.add();
//			AccountTypes t3 = new AccountTypes("Despesas");
//			t3 = t3.add();
//			AccountTypes t4 = new AccountTypes("Receitas");
//			t4 = t4.add();
//
//			Account accCx = new Account("100000", "Caixa", 1000000000, null);
//			accCx.setCurrencyID(curr);
//			accCx.setAccountTypesID(t1);
//			// accCx.setBalance(new BigDecimal(5000));
//			// accCx.setOpen(true);
//			accCx.setDescription("");
//			accCx = accCx.add();
//
//			Account accCorr = new Account("300000", "Corretagem", 0, null);
//			accCorr.setCurrencyID(curr);
//			accCorr.setAccountTypesID(t3);
//			// accCorr.setBalance(new BigDecimal(0));
//			// accCorr.setOpen(true);
//			accCorr.setDescription("");
//			accCorr = accCorr.add();
//
//			Account accImpos = new Account("300001", "Impostos", 0, null);
//			accImpos.setCurrencyID(curr);
//			accImpos.setAccountTypesID(t3);
//			// accImpos.setBalance(new BigDecimal(0));
//			// accImpos.setOpen(true);
//			accImpos.setDescription("");
//			accImpos = accImpos.add();
//
//			Account accEmol = new Account("300002", "Emolumentos", 0, null);
//			accEmol.setCurrencyID(curr);
//			accEmol.setAccountTypesID(t3);
//			// accEmol.setBalance(new BigDecimal(0));
//			// accEmol.setOpen(true);
//			accEmol.setDescription("");
//			accEmol = accEmol.add();
//
//			Account accLiq = new Account("300003", "Taxa Liquidacao", 0, null);
//			accLiq.setCurrencyID(curr);
//			accLiq.setAccountTypesID(t3);
//			// accLiq.setBalance(new BigDecimal(0));
//			// accLiq.setOpen(true);
//			accLiq.setDescription("");
//			accLiq = accLiq.add();
//
//			Account accLuc = new Account("400000", "Lucros", 0, null);
//			accLuc.setCurrencyID(curr);
//			accLuc.setAccountTypesID(t4);
//			// accLuc.setBalance(new BigDecimal(0));
//			// accLuc.setOpen(true);
//			accLuc.setDescription("");
//			accLuc = accLuc.add();
//
//			Tax iss = new Tax("ISS", "ISS");
//			TaxSetup stpiss = new TaxSetup("ISS Bovespa", .05f, new BigDecimal(
//					0));
//			stpiss.setTaxID(iss);
//			iss.getTaxSetupSet().add(stpiss);
//			iss = iss.add();
//
//			Tax taxliq = new Tax("LIQ", "Taxa Liquidacao");
//			TaxSetup stpliq = new TaxSetup("Taxa Liquidacao", .0002f,
//					new BigDecimal(0));
//			stpliq.setTaxID(taxliq);
//			taxliq.getTaxSetupSet().add(stpliq);
//			taxliq = taxliq.add();
//
//			Tax taxem = new Tax("EM", "Emolumentos");
//			TaxSetup stpem = new TaxSetup("Emolumentos", .00005f,
//					new BigDecimal(0));
//			stpem.setTaxID(taxem);
//			taxem.getTaxSetupSet().add(stpem);
//			taxem = taxem.add();
//
//			Tax taxbrk = new Tax("BRK", "Corretagem");
//			TaxSetup stpbrk = new TaxSetup("Corretagem", 0, new BigDecimal(3.9));
//			stpbrk.setTaxID(taxbrk);
//			taxbrk.getTaxSetupSet().add(stpbrk);
//			taxbrk = taxbrk.add();
//
//			MapRegister mrCor = new MapRegister(Objects.Orders, "",
//					"Corretagem").add();
//			MapRegister mrImp = new MapRegister(Objects.Orders, "3.9", "ISS")
//					.add();
//			MapRegister mrEm = new MapRegister(Objects.Orders,
//					"Orders.price * Orders.volume", "Emolumento").add();
//			MapRegister mrLiq = new MapRegister(Objects.Orders,
//					"Orders.price * Orders.volume", "Taxa Liquidacao").add();
//			// MapRegister mrProfit = new MapRegister(Objects.Orders,
//			// "Orders.ExecutedPrice * Orders.ExecutedVolume", "Lucro").add();
//
//			DistributionRule dr = new DistributionRule(Objects.Orders,
//					"Orders Distribution");
//			// corretagem
//			Rule rule = new Rule();
//			rule.setCreditAccountID(accCorr);
//			rule.setDebitAccountID(accCx);
//			rule.setDistributionRuleID(dr);
//			rule.setMapRegisterID(mrCor);
//			rule.setApplyValue(true);
//			rule.setTaxSetupID(stpbrk);
//			dr.getRuleSet().add(rule);
//			// iss
//			Rule r2 = new Rule();
//			r2.setCreditAccountID(accImpos);
//			r2.setDebitAccountID(accCx);
//			r2.setDistributionRuleID(dr);
//			r2.setMapRegisterID(mrImp);
//			r2.setApplyTax(true);
//			r2.setTaxSetupID(stpiss);
//			dr.getRuleSet().add(r2);
//			// emolumentos
//			Rule r3 = new Rule();
//			r3.setCreditAccountID(accEmol);
//			r3.setDebitAccountID(accCx);
//			r3.setDistributionRuleID(dr);
//			r3.setMapRegisterID(mrEm);
//			r3.setApplyTax(true);
//			r3.setTaxSetupID(stpem);
//			dr.getRuleSet().add(r3);
//			// liquidacao
//			Rule r4 = new Rule();
//			r4.setCreditAccountID(accLiq);
//			r4.setDebitAccountID(accCx);
//			r4.setDistributionRuleID(dr);
//			r4.setMapRegisterID(mrLiq);
//			r4.setApplyTax(true);
//			r4.setTaxSetupID(stpliq);
//			dr.getRuleSet().add(r4);
//
//			// Rule r5 = new Rule();
//			// r5.setCreditAccountID(accLuc);
//			// r5.setDebitAccountID(accCx);
//			// r5.setDistributionRuleID(dr);
//			// r5.setMapRegisterID(mr2);
//			// r5.setApplyTax(true);
//			// r5.setTaxSetupID(stpiss);
//			// dr.getRuleSet().add(r2);
//
//			dr = dr.add();
//
//			Configuration conf = Configuration.getInstance();
//			conf.setDefaultCurrency(curr);
//			conf = conf.add();
//
//			Company comp = new Company("PETROBRAS", 0L, 0L, ex).add();
//
//			Security item = new Security("PETR4");
//			item.setTradableUnits(100);
//			// item.setStockExchangeID(ex);
//			// item.setCurrencyID(curr);
//			// ((Stock)item).setCompanyID(comp);
//			item = item.add();
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public static void testTrading() {
//		try {
//			Configuration c = Configuration.getInstance();
//			Configuration conf = (Configuration) DBFacade.getInstance()
//					.findObject("Configuration.findByConfigurationID",
//							"configurationID", 1L);
//			c.setDefaultCurrency(conf.getDefaultCurrency());
//			// habilita a receber respostas do mercado
//			OrderService.getInstance().listenMarket();
//
//			// habilita a enviar ordens
//			OrderService.getInstance().sendOrders();
//			DBCache.getInstance();
//
//			Broker brk = (Broker) DBFacade.getInstance().findObject(
//					"Broker.findByBrokerCode", "brokerCode", "308");
//
//			Trader trader = new Trader(brk);
//
//			OrderTypes orderType = OrderTypes.LIMIT;
//			Side operation = Side.BUY;
//			String symbol = "WINM14";
//			Date tradeDate = new Date();
//			int volume = 1;
//			OrdersPrices buyPrices = new OrdersPrices();
//			buyPrices.price = 51880.00;
//			OrdersPrices sellPrices = new OrdersPrices();
//			sellPrices.price = 51980.00;
//
//			for (int i = 0; i < 2; i++) {
//				TimeCounter tc = new TimeCounter();
//				tc.start();
//				// TODO Adicionar metodo para executar ordem sem passar pelo
//				// mercado
//
//				try {
//					TradingService.getInstance().addTrade(symbol, orderType,
//							TradeTypes.DAY_TRADE, volume, buyPrices,
//							sellPrices, tradeDate, brk);
//				} catch (DataBaseException e) {
//					e.printStackTrace();
//				}
//
//				// trader.putOrder(orderType, Side.BUY, symbol, volume,
//				// buyPrices, TradeTypes.DAY_TRADE);
//				// trader.putOrder(orderType, Side.SELL, symbol, volume,
//				// sellPrices, TradeTypes.DAY_TRADE);
//				//
//				// Trade trade = trader.getTrades().values().iterator().next();
//				// trade = (Trade) DBFacade.getInstance().attachToSession(trade,
//				// trade.getTradeID());
//				// trader.closePosition(trade);
//				// tc.stop();
//				// System.out.println("Elapsed time: " +
//				// tc.getElapsedInMilis());
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
//
//	public static void testTrade() {
//
//		Security s = null;
//		try {
//
//			s = (Security) DBFacade.getInstance()._findByKey(Security.class,
//					((Long) 1229l));
//			// s = (Security) DBFacade.getInstance().attachToSession(s);
//
//			System.out.println(s);
//
//			Trade t = ((TradingService) Services
//					.getService(ServiceTypes.Trading)).getTrade(s.getSymbol(),
//					TradeTypes.NORMAL, "308");
//
//			System.out.println(t);
//
//			Orders o = new Orders(100, new Date(), Side.BUY, OrderTypes.LIMIT,
//					t, s);
//			o.setPrice(new BigDecimal("15.76"));
//
//			t.addOrder(o);
//
//		} catch (DataBaseException e) {
//			e.printStackTrace();
//		}
//
//	}

}
