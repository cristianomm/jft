/**
 * 
 */
package com.cmm.jft.model.financial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.model.financial.exceptions.AccountException;
import com.cmm.logging.Logging;

/**
 * 
 * <p>
 * <code>AccountPlan</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Feb, 4 12:23:00 PM
 */
public class AccountPlan {

	private static AccountPlan instance;
	private List<Account> rootAccounts;
	private HashMap<String, Account> accounts;

	public AccountPlan() {
		this.accounts = new HashMap<String, Account>();
		this.rootAccounts = new ArrayList<Account>();
		loadAccounts();
	}

	private void loadAccounts() {

		// recupera do bd...
		try {
			DBFacade.getInstance().queryAsMap("Account.findAll", accounts, AccountPlan.class, "getAccountId");
			for (Account acc : accounts.values()) {
				if (acc.getFatherAccountId() == null) {
					rootAccounts.add(acc);
				}
			}

		} catch (DataBaseException e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	}

	public static synchronized AccountPlan getInstance() {
		if (instance == null) {
			instance = new AccountPlan();
		}
		return instance;
	}

	public void refreshAccounts() {
		loadAccounts();
	}

	public void addAccount(Account account) {
		if (!accounts.containsKey(account.getAccountId())) {
			account = storeAccount(account);
			rootAccounts.add(account);
			accounts.put(account.getAccountId(), account);
		}
	}

	public void addChildAccount(Account root, Account child)
			throws AccountException, NullPointerException {
		if (root != null && child != null) {
			if (child != root) {
				child.setFatherAccountId(root);
				child = storeAccount(child);
			} else {
				throw new AccountException("Accounts child and root are equal.");
			}
		} else {
			throw new NullPointerException(
					"Null Accounts parameter has provided: root=" + root
							+ " child=" + child);
		}
	}

	private Account storeAccount(Account account) {
		try {
			account = (Account) DBFacade.getInstance()._update(account);
		} catch (DataBaseException e) {
		}
		return account;
	}
	
	public Account getAccount(String accountId){
		
		return accounts.get(accountId);
	}
	
}
