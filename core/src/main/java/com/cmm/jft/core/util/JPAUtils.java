/**
 * 
 */
package com.cmm.jft.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * <p>
 * <code>JPAUtils.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 06/08/2013 02:51:43
 *
 */
public class JPAUtils {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String path = "../jft_core/src/com/cmm/";
		String[] files = new String[] {
				// "Company.java", "LogRegisters.java", "Sector.java",
				// "Subsector.java", "Segment.java",
				"jft_core/Subsector.java",

				"jft_financial/AccountTypes.java",
				"jft_financial/Deposit.java",
				"jft_financial/DistributionRule.java",
				"jft_financial/JournalEntry.java",
				"jft_financial/EntryRegister.java", "jft_financial/Rule.java",
				"jft_financial/TaxSetup.java",

				"jft_trading/Security.java", "jft_trading/LimitOrder.java",
				"jft_trading/MarketOrder.java", "jft_trading/Tick.java",
				"jft_trading/MarketTrade.java", "jft_trading/MarketTypes.java",
				"jft_trading/Orders.java", "jft_trading/Quote.java",
				"jft_trading/StopOrder.java", "jft_trading/Trade.java" };

		for (String fn : files) {
			generateSequence(new File(path + fn));
		}

	}

	public static void generateSequence(File source) {

		Scanner sc;
		String className = source.getName().replace(".java", "").toUpperCase();
		String sequence = "@Id"
				+ "\n@TableGenerator(name = \""
				+ className
				+ "_SEQ\", table = \"SEQUENCE\", allocationSize = 1, initialValue = 1) "
				+ "\n@GeneratedValue(generator = \"" + className
				+ "_SEQ\", strategy = GenerationType.TABLE)";
		try {
			sc = new Scanner(source);
			sc.useDelimiter("\n");
			StringBuffer lines = new StringBuffer();
			while (sc.hasNext()) {
				String line = sc.next();
				// System.out.println(line);
				if (line.contains("@Id")) {
					line = sequence;
				}
				lines.append(line + "\n");

			}
			sc.close();
			// System.out.println(lines);
			FileOutputStream fos = new FileOutputStream(source);
			fos.write(lines.toString().getBytes());
			fos.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			// Logging.getInstance().log(JPAUtils.class, e, Level.ERROR);
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
