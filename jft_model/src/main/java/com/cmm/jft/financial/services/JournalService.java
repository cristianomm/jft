/**
 * 
 */
package com.cmm.jft.financial.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Level;

import com.cmm.jft.financial.Account;
import com.cmm.jft.financial.Currency;
import com.cmm.jft.financial.DistributionRule;
import com.cmm.jft.financial.EntryRegister;
import com.cmm.jft.financial.JournalEntry;
import com.cmm.jft.financial.enums.EntryType;
import com.cmm.jft.financial.enums.JournalStatus;
import com.cmm.jft.financial.exceptions.RegistrationException;
import com.cmm.jft.core.enums.Objects;
import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>JournalService.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 15/08/2013 00:19:58
 *
 */
public class JournalService {

	private AccountingService accService;
	private static JournalService instance;

	private JournalService() {
		this.accService = AccountingService.getInstance();
	}

	public static JournalService getInstance() {
		if (instance == null) {
			instance = new JournalService();
		}
		return instance;
	}

	public JournalEntry createEntry() {

		JournalEntry je = new JournalEntry();
		return je;
	}

	/**
	 * Busca a regra de distribuicao relacionada ao tipo de objeto passado por
	 * parametro.
	 * 
	 * @param object
	 *            Enumeracao que informa o tipo do objeto relacionado a regra de
	 *            distribuicao
	 * @return regra de distribuicao relacionada ao objeto.
	 * @throws RegistrationException
	 *             Caso nao exista uma regra de distribuicao relacionada.
	 */
	public DistributionRule getDistributionRule(Objects object) throws RegistrationException {
		//busca a regra de distribuicao de acordo com o obj indicado
		HashMap<String, Object> params = new HashMap<String, Object>();
		params.put("objectRule", object);

		List<DistributionRule> lres=null;
		try {
			lres = (List<DistributionRule>) DBFacade.getInstance().queryNamed(
					"DistributionRule.findByObjectRule", params);
		} catch (DataBaseException e) {
			Logging.getInstance().log(JournalService.class, e, Level.ERROR);
		}

		if(lres==null || lres.size()==0) {
			throw new RegistrationException("DistributionRule not defined to: " + object.name());
		}

		return lres.get(0);
	}

	// public void preRegisterEntry(JournalEntry journalEntry, Registrable
	// registrable) {
	//
	// try {
	// DistributionRule distRule = getDistributionRule(registrable.getObject());
	//
	// PreRegister pr = new PreRegister(journalEntry, distRule,
	// registrable.getObject());
	// pr.add();
	//
	// } catch (RegistrationException e) {
	// e.printStackTrace();
	// Logging.getInstance().log(JournalService.class, e, Level.ERROR);
	// } catch (DataBaseException e) {
	// e.printStackTrace();
	// Logging.getInstance().log(JournalService.class, e, Level.ERROR);
	// }
	// }

	public JournalEntry registerEntry(JournalEntry journalEntry,
			DistributionRule distrRule) throws RegistrationException {

		return null;
	}

	// public JournalEntry registerEntry(JournalEntry journalEntry, Registrable
	// registrable) throws RegistrationException {
	//
	// DistributionRule distRule = getDistributionRule(registrable.getObject());
	//
	// for ( Rule rule : distRule.getRuleSet()) {
	// try {
	// //
	// MapRegister reg = rule.getMapRegisterID();
	// BigDecimal value = new BigDecimal(0);
	// if(reg!=null && reg.getFormula()!=null) {
	//
	// for(Method m: registrable.getClass().getMethods()) {
	// if(m.isAnnotationPresent(FormulaField.class)) {
	// Object obj = m.invoke(registrable, null);
	// if(obj!=null) {
	// FormulaField ann = m.getAnnotation(FormulaField.class);
	// reg.getFormula().addParameter(ann.name(), ann.type(), obj);
	// }
	// }
	// }
	// value = reg.getFormula().evalAsBigDecimal();
	// }
	//
	// if(rule.isApplyTax()) {
	// value = value.multiply(new
	// BigDecimal(rule.getTaxSetupID().getAliquota()));
	// }
	//
	// if(rule.isApplyValue()) {
	// value = value.add(rule.getTaxSetupID().getTaxValue());
	// }
	//
	// registerEntry(journalEntry,
	// rule.getCreditAccountID(),
	// rule.getDebitAccountID(), value, reg.getDescription());
	//
	// } catch (AccountException e) {
	// Logging.getInstance().log(JournalService.class, e, Level.ERROR);
	// throw new RegistrationException(e);
	// } catch (IllegalAccessException e) {
	// Logging.getInstance().log(JournalService.class, e, Level.ERROR);
	// throw new RegistrationException(e);
	// } catch (IllegalArgumentException e) {
	// Logging.getInstance().log(JournalService.class, e, Level.ERROR);
	// throw new RegistrationException(e);
	// } catch (InvocationTargetException e) {
	// Logging.getInstance().log(JournalService.class, e, Level.ERROR);
	// throw new RegistrationException(e);
	// } catch (FormulaException e) {
	// Logging.getInstance().log(JournalService.class, e, Level.ERROR);
	// throw new RegistrationException(e);
	// } catch (DataBaseException e) {
	// Logging.getInstance().log(JournalService.class, e, Level.ERROR);
	// throw new RegistrationException(e);
	// }
	// }
	//
	// return journalEntry;
	// }
		

	public void registerEntry(JournalEntry journalEntry, Account creditAccount,
			Account debitAccount, BigDecimal value, String descr)
			throws RegistrationException, DataBaseException {

		// verificar se o proprio je esta aberto
		if (journalEntry.getJournalStatus() != JournalStatus.OPEN) {
			throw new RegistrationException("Invalid JournalEntry State: "
					+ journalEntry.getJournalStatus());
		}

		BigDecimal crv = new BigDecimal(value.toString());
		BigDecimal dbtv = new BigDecimal(0);
		Currency defCurr = null;// Configuration.getInstance().getDefaultCurrency();

		EntryRegister creditReg = new EntryRegister(EntryType.CREDIT, crv,
				dbtv, descr, defCurr, creditAccount, debitAccount);

		EntryRegister debitReg = new EntryRegister(EntryType.DEBIT, dbtv, crv,
				descr, defCurr, debitAccount, creditAccount);

		creditReg.setEntryID(journalEntry);
		debitReg.setEntryID(journalEntry);

		creditReg = (EntryRegister) DBFacade.getInstance()._persist(creditReg);
		debitReg = (EntryRegister) DBFacade.getInstance()._persist(debitReg);

		journalEntry.getEntryRegisterSet().add(creditReg);
		journalEntry.getEntryRegisterSet().add(debitReg);
		
		accService.transfer(creditAccount, debitAccount, value);
		
		journalEntry =  (JournalEntry) DBFacade.getInstance()._persist(journalEntry);

	}

	public boolean reverseEntry(JournalEntry journalEntry) {
		boolean ret = false;
		if (journalEntry != null) {
			if (journalEntry.getJournalStatus() == JournalStatus.OPEN) {
				try {
					JournalEntry je = createEntry();
					je.setDescription("Reverse of JournalEntry: "
							+ journalEntry.getEntryID());

					for (EntryRegister er : journalEntry.getEntryRegisterSet()) {
						if (er.getEntryType() == EntryType.CREDIT) {
							registerEntry(je, er.getDebitAccountID(),
									er.getCreditAccountID(), er.getCredit(),
									er.getDescription() + " - Reverse");
						}
					}
					
					DBFacade.getInstance()._persist(je);
					ret = true;
				} catch (RegistrationException e) {
					Logging.getInstance().log(getClass(), e, Level.ERROR);
				} catch (DataBaseException e) {
					Logging.getInstance().log(getClass(), e, Level.ERROR);
				}

			}

		}

		return ret;
	}

	/**
	 * @param je
	 * @return
	 */
	public JournalEntry closeEntry(JournalEntry je) {

		try {
			je.setEntryClose(new Date());
			je.setJournalStatus(JournalStatus.CLOSE);
			je = (JournalEntry) DBFacade.getInstance()._update(je);
		} catch (DataBaseException e) {
			Logging.getInstance().log(getClass(), "Erro ao fechar registro", e,
					Level.ERROR, false);
		}
		return je;
	}

	/**
	 * @param je
	 * @return
	 */
	public JournalEntry cancelEntry(JournalEntry je) {

		try {
			if (reverseEntry(je)) {
				je.setEntryClose(new Date());
				je.setJournalStatus(JournalStatus.CANCELED);
				je = (JournalEntry) DBFacade.getInstance()._update(je);
			} else {
				Logging.getInstance().log(
						getClass(),
						"Não foi possível reverter o Lancamento: "
								+ je.getEntryID(), Level.WARN);
			}
		} catch (DataBaseException e) {
			Logging.getInstance().log(getClass(), "Erro ao cancelar registro",
					e, Level.ERROR, false);
		}
		return je;
	}
	
	public void addCreditRegister(JournalEntry journalEntry, Account creditAccount, 
			double value, String description) throws RegistrationException{
		
		
		if (journalEntry.getJournalStatus() != JournalStatus.OPEN) {
			throw new RegistrationException("Invalid JournalEntry State: "
					+ journalEntry.getJournalStatus());
		}
		
		BigDecimal crv = new BigDecimal(value+"");
		BigDecimal dbtv = new BigDecimal(0);
		Currency defCurr = null;// Configuration.getInstance().getDefaultCurrency();

		EntryRegister creditReg = new EntryRegister(EntryType.CREDIT, crv,
				dbtv, description, defCurr, creditAccount, null);

		creditReg.setEntryID(journalEntry);

		//creditReg = (EntryRegister) DBFacade.getInstance()._persist(creditReg);

		journalEntry.getEntryRegisterSet().add(creditReg);
		
		
	}
	
}
