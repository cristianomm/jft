package com.cmm.jft.ui.forms;

import com.cmm.jft.ui.ObjectForms;
import com.cmm.jft.ui.Program;
import com.cmm.jft.ui.analysis.NeuralNetForm;
import com.cmm.jft.ui.chart.ChartForm;
import com.cmm.jft.ui.configuration.DBInitializationForm;
import com.cmm.jft.ui.financial.DistributionRuleForm;
import com.cmm.jft.ui.financial.JournalEntryForm;
import com.cmm.jft.ui.financial.RuleForm;
import com.cmm.jft.ui.financial.TaxSetupForm;
import com.cmm.jft.ui.masterdata.BrokerForm;
import com.cmm.jft.ui.masterdata.CompanyForm;
import com.cmm.jft.ui.masterdata.CurrencyForm;
import com.cmm.jft.ui.masterdata.CurrencyQuoteForm;
import com.cmm.jft.ui.simulation.BacktestForm;
import com.cmm.jft.ui.trading.BookForm;
import com.cmm.jft.ui.trading.MarketResumeForm;
import com.cmm.jft.ui.trading.MarketTradesForm;
import com.cmm.jft.ui.trading.NewsDetailsForm;
import com.cmm.jft.ui.trading.NewsForm;
import com.cmm.jft.ui.trading.OrderForm;
import com.cmm.jft.ui.trading.OrderManagerForm;
import com.cmm.jft.ui.trading.PlaceOrderForm;
import com.cmm.jft.ui.trading.TradeForm;
import com.cmm.jft.ui.trading.TradeManagerForm;
import com.cmm.jft.ui.utils.GenericTableModel;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * <p><code>FormsFactory.java</code></p>
 *
 * @author Cristiano M Martins
 * @version 02/08/2013 01:48:29
 *
 */
public class FormsFactory {

    private static Forms getForm(ObjectForms objForm) {
	Forms frm = null;
	switch (objForm) {
	case ABOUT:
	    break;
	case ACCOUNT:

	    break;
	case BACKTEST:
	    frm = new BacktestForm();
	    break;
	case BROKER:
	    frm = new BrokerForm();
	    break;
	case CHART:
	    frm = new ChartForm();
	    break;
	case CHART_OPTIONS:
	    break;
	case COMPANY:
	    frm = new CompanyForm();
	    break;
	case CONFIGURATION:
	    break;
	case CONNECTIONS:
	    break;
	case COUNTRY:
	    break;
	case COST:
	    break;
	case CURRENCY:
	    frm = new CurrencyForm();
	    break;
	case CURRENCY_QUOTES:
	    frm = new CurrencyQuoteForm();
	    break;
	case DATA_IMPORT:
	    break;
	case DBINITIALIZATION:
	    frm = new DBInitializationForm();
	    break;
	case DEPOSIT:
	    break;
	case DISTRIBUTION_RULE:
	    frm = new DistributionRuleForm();
	    break;
	case HELP:
	    break;
	case JOURNAL_ENTRY:
	    frm = new JournalEntryForm();
	    break;
	case NEURAL_NETWORK:
	    frm = new NeuralNetForm();
	    break;
	case ORDER:
	    frm = new OrderForm();
	    break;    
	case ORDER_MANAGEMENT:
	    frm = new OrderManagerForm();
	    break;
	case PLACE_ORDER:
	    frm = new PlaceOrderForm();
	    break;
	case POSITION:
	    break;
	case PROGRAM:
	    frm = new Program();
	    break;
	case REPORTS:
	    break;
	case RULE:
	    frm = new RuleForm();
	    break;
	case SECTORS:
	    break;
	case SEGMENTS:
	    break;
	case SETUPS:
	    break;
	case STATISTICS:
	    break;
	case STOCK_EXCHANGE:
	    break;
	case SUBSECTORS:
	    break;
	case TAX:
	    break;
	case TAX_SETUP:
	    frm = new TaxSetupForm();
	    break;

	    
	    
	    
	case BOOK:
	    frm = new BookForm();
	    break;
	case TIME_SALES:
	    frm = new MarketTradesForm();
	    break;
	case TRADE:
	    frm = new TradeForm();
	    break;
	case TRADE_MANAGER:
	    frm = new TradeManagerForm();
	    break;
	case MARKET_RESUME:
	    frm = new MarketResumeForm();
	    break;
	case NEWS:
	    frm = new NewsForm();
	    break;
	case NEWS_DETAILS:
	    frm = new NewsDetailsForm();
	    break;
	    
	    
	    
	    
	case SIMULATION:
	    break;
	case STRATEGY:
	    break;
	case STRATEGY_BUILDER:
	    break;
	default:
	    break;

	}

	return frm;
    }

    public static void openForm(final ObjectForms objForm) {
	openForm(objForm, null, FormStates.OK);
    }

    public static void openForm(final ObjectForms objForm, final FormStates state) {
	openForm(objForm, null, state);
    }

    public static void openForm(final ObjectForms objForm, final Object data) {
	openForm(objForm, data, FormStates.OK);
    }

    public static void openForm(final ObjectForms objForm, final Object data, final FormStates state) {

	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		Forms frm = getForm(objForm);
		frm.setState(state);
		frm.addListeners();
		frm.setData(data);
		frm.loadData();
		((JFrame) frm).setVisible(true);
		((JFrame) frm).setLocationRelativeTo(null);
	    }
	});
    }

    public static void openFormBySelectedObject(final JTable table, final ObjectForms objForm, final FormStates state) {
	int r = table.getSelectedRow();
	if(r>=0) {
	    GenericTableModel mdl = (GenericTableModel) table.getModel();
	    FormsFactory.openForm(objForm, mdl.getValue(r), state);
	}
	else {
	    JOptionPane.showMessageDialog(null, "You must select an record!", "", JOptionPane.INFORMATION_MESSAGE);
	}
    }

}
