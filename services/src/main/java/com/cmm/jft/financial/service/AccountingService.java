/**
 * 
 */
package com.cmm.jft.financial.service;

import java.math.BigDecimal;
import java.util.Date;

import org.apache.log4j.Level;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.model.financial.Account;
import com.cmm.jft.model.financial.AccountPlan;
import com.cmm.jft.model.financial.Currency;
import com.cmm.jft.model.financial.Deposit;
import com.cmm.jft.model.financial.JournalEntry;
import com.cmm.jft.model.financial.enums.AccountCategories;
import com.cmm.jft.model.financial.enums.AccountTypes;
import com.cmm.jft.model.financial.exceptions.AccountException;
import com.cmm.jft.model.financial.exceptions.RegistrationException;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>AccountingService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 14/08/2013 23:47:49
 *
 */
public class AccountingService {

	private AccountPlan accountPlan;
	private static AccountingService instance;

	private AccountingService() {
		this.accountPlan = AccountPlan.getInstance();
	}

	public static AccountingService getInstance() {
		if (instance == null) {
			instance = new AccountingService();
		}
		return instance;
	}

	public void addInitialDeposit(Account account, double value) {
		Deposit deposit = new Deposit(new Date(), new BigDecimal(value), "Initial Deposit", account);

	}

	public void transfer(Account creditAccount, Account debitAccount, double value) {
		try {
			transferFunds(creditAccount, debitAccount, new BigDecimal(value), "Transfer");
		} catch (AccountException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}

	public void transfer(Account creditAccount, Account debitAccount, BigDecimal value) {
		try {
			transferFunds(creditAccount, debitAccount, value, "Transfer");
		} catch (AccountException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}

	public void deposit(Account creditAccount, Account debitAccount, double value) {
		try {
			transferFunds(creditAccount, debitAccount, new BigDecimal(value), "Deposit");
		} catch (AccountException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}
	
	public void withdrawal(Account creditAccount, Account debitAccount, double value) {
		try {
			transferFunds(creditAccount, debitAccount, new BigDecimal(value), "Withdrawal");
		}catch(AccountException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}

	private void transferFunds(Account creditAccount, Account debitAccount, BigDecimal value, String description)
			throws AccountException {

		try {
			debit(debitAccount, value);
			deposit(creditAccount, value);

			JournalEntry je = (JournalEntry)DBFacade.getInstance()._persist(JournalService.getInstance().createEntry());
			JournalService.getInstance().registerEntry(je, creditAccount, debitAccount, value.doubleValue(), description);
			JournalService.getInstance().closeEntry(je);

		} catch (AccountException | DataBaseException | RegistrationException e) {
			throw new AccountException(e);
		}

	}

	private void debit(Account account, BigDecimal value) throws AccountException {
		if (account.debit(value)) {
			try {
				account = (Account) DBFacade.getInstance()._update(account);
			} catch (DataBaseException e) {
				throw new AccountException(e);
			}
		}
	}

	private void deposit(Account account, BigDecimal value) throws AccountException {
		if (account.deposit(value)) {
			try {
				account = (Account) DBFacade.getInstance()._update(account);
			} catch (DataBaseException e) {
				throw new AccountException(e);
			}
		}
	}

	public Account createAccount(String accountID, String accountName, double creditLimit, Currency currency,
			AccountTypes accountTypes, AccountCategories accountCategories) {

		Account account = new Account(accountID, accountName, creditLimit, currency, accountTypes, accountCategories);

		accountPlan.addAccount(account);

		return account;
	}

	public void addChildAccount(Account father, Account child) {
		if (father != null & child != null) {
			try {
				accountPlan.addChildAccount(father, child);
			} catch (NullPointerException e) {
				Logging.getInstance().log(getClass(), e, Level.ERROR);
			} catch (AccountException e) {
				Logging.getInstance().log(getClass(), e, Level.ERROR);
			}
		}

	}

	public AccountPlan getAccountPlan() {
		return accountPlan;
	}

}
