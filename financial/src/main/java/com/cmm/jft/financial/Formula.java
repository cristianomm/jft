/**
 * 
 */
package com.cmm.jft.financial;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Level;

import bsh.EvalError;
import bsh.Interpreter;

import com.cmm.jft.financial.exceptions.FormulaException;
import com.cmm.logging.Logging;

/**
 * <p>
 * <code>Formula.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 10/08/2013 03:54:16
 *
 */
public class Formula {

	private static String operations = "[+|-|//|*]";
	private static String field = "\\w+\\.\\w+";
	private static String parameter = "[T|D|N]-P[\\d]*";

	private String formula;
	private Interpreter interpreter;
	private HashMap<String, Object> parameters;

	public Formula(String formula) throws FormulaException {
		try {
			if (validate(formula)) {
				this.interpreter = new Interpreter();
				this.formula = "res=" + formula;
				this.parameters = new HashMap<String, Object>();
			}
		} catch (FormulaException e) {
			throw new FormulaException("Invalid Formula: " + formula, e);
		}
	}

	public String getFormula() {
		return formula;
	}

	public void addParameter(String paramName, Object value)
			throws FormulaException {
		// System.out.println(formula+ " -> " + paramName + ":" + value);
		if (getFormula().contains(paramName)) {
			try {

				Object aux = null;
				/*
				 * switch (type) { case BIGDECIMAL: aux =
				 * FormatterFactory.getFormatter
				 * (FormatterTypes.BIGDECIMAL).parse(value.toString()); break;
				 * case DOUBLE: aux =
				 * FormatterFactory.getFormatter(FormatterTypes
				 * .DOUBLE).parse(value.toString()); break; case INT: aux =
				 * FormatterFactory
				 * .getFormatter(FormatterTypes.INT).parse(value.toString());
				 * break; case LONG: aux =
				 * FormatterFactory.getFormatter(FormatterTypes
				 * .LONG).parse(value.toString()); break; case DATE_TIME_F6: aux
				 * =
				 * FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F6).parse
				 * (value.toString()); break; }
				 */

				parameters.put(paramName, value);
				interpreter.set(paramName, value);
			} catch (EvalError e) {
				e.printStackTrace();
			}
		}
		// else {
		// throw new FormulaException(
		// "Invalid parameter: " + paramName +
		// " in formula: " + formula);
		// }
	}

	private boolean validate(String formula) throws FormulaException {

		boolean ret = false;
		String[] fields = formula.split(" ");
		for (String fld : fields) {
			// System.out.println("\n" + fld);
			if (fld.matches(field)) {
				ret = true;
				// System.out.println("isField " + fld);
			} else if (fld.matches(operations)) {
				ret = true;
				// System.out.println("isOper " + fld);
			} else if (fld.matches(parameter)) {
				ret = true;
				// System.out.println("isParam " + fld);
			} else {
				ret = false;
			}
		}

		if (formula.isEmpty()) {
			ret = true;
		}

		return ret;
	}

	private Object evaluate() throws FormulaException {
		Object ret = null;

		try {
			interpreter.eval(formula);
			ret = interpreter.get("res");
		} catch (EvalError e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
			throw new FormulaException(e);
		}

		// for(Method m:object.getClass().getDeclaredMethods()) {
		// try {
		//
		// if(m.isAnnotationPresent(FormulaField.class)) {
		// //System.out.println(m.getAnnotation(FormulaField.class).name());
		// ret = m.invoke(object, null);
		// }
		// } catch (IllegalArgumentException e) {
		// Logging.getInstance().log(Formula.class, e, Level.ERROR);
		// throw new FormulaException(e);
		// } catch (IllegalAccessException e) {
		// Logging.getInstance().log(Formula.class, e, Level.ERROR);
		// throw new FormulaException(e);
		// } catch (InvocationTargetException e) {
		// Logging.getInstance().log(Formula.class, e, Level.ERROR);
		// throw new FormulaException(e);
		// }
		//
		// }

		return ret;
	}

	public String evalAsString() {
		String ret = "";
		try {
			ret = (String) evaluate();
		} catch (FormulaException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		} catch (ClassCastException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		}
		return ret;
	}

	public Double evalAsDouble() {
		Double ret = 0d;
		try {
			ret = Double.valueOf(evaluate().toString());
		} catch (FormulaException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		} catch (ClassCastException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		}
		return ret;
	}

	public Long evalAsLong() {
		Long ret = 0L;
		try {
			ret = Long.valueOf(evaluate().toString());
		} catch (FormulaException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		} catch (ClassCastException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		}
		return ret;
	}

	public BigDecimal evalAsBigDecimal() {
		BigDecimal ret = new BigDecimal(0);
		try {
			ret = new BigDecimal(evaluate().toString());
		} catch (FormulaException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		} catch (ClassCastException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		}
		return ret;
	}

	public Date evalAsDate() {
		Date ret = null;
		try {
			ret = (Date) evaluate();
		} catch (FormulaException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		} catch (ClassCastException e) {
			Logging.getInstance().log(Formula.class, e, Level.ERROR);
		}

		return ret;
	}

}
