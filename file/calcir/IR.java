/**
 * 
 */
package com.cmm.jft.core.calcir;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import javax.swing.text.DateFormatter;

import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;

/**
 * <p>
 * <code>IR.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 27/04/2014 14:47:51
 *
 */
public class IR {

	static HashMap<Integer, Comission> comissions;
	static HashMap<Integer, SETax> seTax;

	/**
     * 
     */
	public IR() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		comissions = new HashMap<Integer, Comission>();
		CSV csv = new CSV("../jft_core/file/IR/Comission.csv", ";");
		while (csv.hasNext()) {
			String[] line = csv.readLine();
			Comission com = new Comission();
			com.init(line[0], line[1], line[2], line[3]);
			comissions.put(com.codComission, com);
			// System.out.println(com);
		}

		// 1-TotalOperacoes
		// 2-Lucro
		seTax = new HashMap<Integer, SETax>();
		csv = new CSV("../jft_core/file/IR/SETax.csv", ";");
		while (csv.hasNext()) {
			String[] line = csv.readLine();
			SETax tx = new SETax();
			tx.init(line[0], line[1], line[2], line[3]);
			seTax.put(tx.codSETax, tx);
			// System.out.println(tx);
		}

		HashMap<Date, Resumo> resumos = new HashMap<Date, Resumo>();
		csv = new CSV("../jft_core/file/IR/Resumos.csv", ";", "#");
		while (csv.hasNext()) {
			String[] line = csv.readLine();
			Resumo r = new Resumo();
			r.init(line[0], line[1], line[2], line[3], line[4], line[5],
					line[6]);
			resumos.put(r.date, r);
			// if(r.profit>0) {
			System.out.println(r);
			// }
		}

		HashMap<Integer, Trade> trades = new HashMap<Integer, Trade>();
		// Trade;Type;Security;Date;Buy/Sell;Quantity;Value
		// ArrayList<Operation> ops = new ArrayList<Operation>();
		csv = new CSV("../jft_core/file/IR/Operations.csv", ";", "#");
		while (csv.hasNext()) {
			String[] line = csv.readLine();
			Operation op = new Operation();
			op.init(line[0], line[1], line[2], line[3], line[4], line[5],
					line[6]);
			op.comission = getComissionValue(op);
			if (trades.containsKey(op.tradeID)) {
				trades.get(op.tradeID).addOperation(op);
			} else {
				Trade t = new Trade(op.tradeID, op.security, op.type,
						resumos.get(op.date));
				t.addOperation(op);

				trades.put(t.codTrade, t);
			}
		}

		for (Trade t : trades.values()) {
			String msg = String
					.format("Trade %d: Data: %s Papel: %s Total Operacoes: %f "
							+ "Valor das Operacoes: %f Corretagem: %f Emolumentos %f "
							+ "Taxa de Liquidacao: %f ISS: %f Valor Liquido: %f IR: %f IR Prov: %f",
							t.codTrade,
							DateFormat.getDateInstance(DateFormat.SHORT)
									.format(t.date), t.security, t
									.getTotalOperation(), t.getProfit(), t
									.getTotalComission(), t.getEmolumentos(), t
									.getTaxLiquidacao(), t.getISS(), t
									.getValorLiquido(), t.getIR(), t
									.getIRProvisao());

			// if(t.getValorLiquido()>0) {
			System.out.println(msg);
			// }
		}

		// calcula o valor por mes/ano
		HashMap<String, Integer> periods = new HashMap<String, Integer>();
		for (Trade t : trades.values()) {
			GregorianCalendar gc = new GregorianCalendar();
			gc.setTime(t.date);
			periods.put(
					gc.get(GregorianCalendar.MONTH) + 1 + "-"
							+ gc.get(GregorianCalendar.YEAR), 0);
		}

		for (String sk : periods.keySet()) {
			double valueD = 0;
			double valueS = 0;
			double irFonteD = 0;
			double irFonteS = 0;
			double irD = 0;
			double irS = 0;
			for (Trade t : trades.values()) {
				GregorianCalendar gc = new GregorianCalendar();
				gc.setTime(t.date);
				if (sk.equalsIgnoreCase(gc.get(GregorianCalendar.MONTH) + 1
						+ "-" + gc.get(GregorianCalendar.YEAR))) {
					if (t.type.equals(TradeTypes.DAY_TRADE)) {
						valueD += t.getValorLiquido();
						irD += t.getIR();
						irFonteD += t.getIRProvisao();
					} else if (t.type.equals(TradeTypes.NORMAL)) {
						valueS += t.getValorLiquido();
						irS += t.getIR();
						irFonteS += t.getIRProvisao();
					}

				}
			}
			System.out.println("Total em " + sk + " (Day Trade: " + valueD
					+ ")|(Swing Trade: " + valueS + ")|" + "(IRRF na Fonte: D:"
					+ irFonteD + " | S:" + irFonteS + ")" + "(IRRF: D:" + irD
					+ " | S:" + irS + ")");
		}

		double valOper = 0;
		double valLiquido = 0;
		for (Trade t : trades.values()) {
			valOper += t.getProfit();
			valLiquido += t.getValorLiquido();
		}

		System.out.println("Total atual(Operacoes)|(Valor Liquido): ("
				+ valOper + ")|(" + valLiquido + ")");

	}

	private static double getComissionValue(Operation operation) {
		double ret = 0;
		for (Comission co : comissions.values()) {
			if (operation.type.equals(co.type)
					&& (co.date.compareTo(operation.date)) <= 0) {
				ret = co.value;
			}
		}
		return ret;
	}

}
