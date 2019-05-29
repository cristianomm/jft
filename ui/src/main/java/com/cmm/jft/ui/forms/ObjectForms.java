/**
 * 
 */
package com.cmm.jft.ui.forms;

/**
 * <p><code>ObjectForms.java</code></p>
 * @author Cristiano M Martins
 * @version 02/08/2013 01:48:29
 *
 */
public enum ObjectForms {
    //ABOUT("About"),
    //ACCOUNT("Account"),
    //BACKTEST("Backtest"),
    //BROKER("Broker"),
    BOOK("Book"),
    //CHART("Chart"),
    //CHART_OPTIONS("Chart Options"),
    //COMPANY("Company"),
    //CONFIGURATION("Configuration"),
    //CONNECTIONS(""),
    //COUNTRY("Country"),
    //COST("Cost"),
    //CURRENCY("Currency"),
    //CURRENCY_QUOTES("Currency Quotes"),
    //DATA_IMPORT("Data Import"),
    //DBINITIALIZATION("DB Initialization"),
    //DEPOSIT("Deposit"),
    //DISTRIBUTION_RULE("Distribution Rule"),
    //HELP("Help"),
    //JOURNAL_ENTRY("Journal Entry"),
    //NEURAL_NETWORK("Neural Network"),
    //ORDER("Order"),
    //ORDER_MANAGEMENT("Order Management"),
    //PLACE_ORDER("Place Order"),
    //POSITION("TradePosition"),
    //
    //REPORTS("Reports"),
    //RULE("Rule"),
    //SECTORS("Sectors"),
    //SEGMENTS("Segments"),
    //SETUPS("Setups"),
    //STATISTICS("Statistics"),
    //STOCK_EXCHANGE("Stock Exchange"),
    //STRATEGY("Strategy"),
    //SUBSECTORS("Sub Sectors"),
    //TAX("Tax"),
    //TAX_SETUP("Tax Setup"), 
    //TIME_SALES("Time & Sales"),
    
    DOM("DOM"),
    PROGRAM("Program"),
    //TRADE("Trade"),
    //TRADE_MANAGER("Trade Manager"),
    //MARKET_RESUME("Market Resume"),
    NEWS("News"),
    NEWS_DETAILS("News Details");
    
    
    //STRATEGY_BUILDER("Strategy Builder"), 
    //SIMULATION("Simulation");
    
    String value;
    private ObjectForms(String value) {
	this.value = value;
    }
    
    /**
     * @return the value
     */
    public String getValue() {
	return this.value;
    }
}
