/**
 * 
 */
package com.cmm.jft.core.format;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 * <p>
 * <code>Cleaner.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 12/09/2013 01:25:50
 *
 */
public class Cleaner {

	public static void main(String[] args) {
		System.out.println(clearNumericString("102,51"));
	}

	public static String clearNumericString(String txt) {

		txt = txt.replace(")", "").replace("(", "-");
		txt = txt.replace("\n", "").replace("\r", "");
		txt = txt.replace("#REF!", "0").replace("#N/D", "0");
		// txt = txt.replace("infinity", "POSITIVE_INFINITY");
		// txt = txt.replace("inf", "POSITIVE_INFINITY");
		txt = txt.trim();
		txt = txt.isEmpty() ? "0" : txt;
		
		if (txt.contains(",") && !txt.contains(".")){
			txt = txt.replace(",", ".");
		}
		else if(txt.contains(",") && txt.contains(".")){
			txt = txt.replace(".", "");
			txt = txt.replace(",", ".");
		}

		txt = formatNumberAsDouble(txt, "###,##0.0000", null).toString();

		return txt;
	}

	private static BigDecimal formatNumberAsDouble(String value, String format,
			Locale locale) {

		NumberFormat formatter;
		int decimalPlaces;

		// create the formatter based on the specified locale
		if (locale != null) {
			formatter = NumberFormat.getNumberInstance(locale);
			// creating the above number format does not take in the format
			// string
			// so create a new one that we won't use at all just to get the
			// decimal places in it
			decimalPlaces = (new DecimalFormat(format))
					.getMaximumFractionDigits();
		} else {
			formatter = new DecimalFormat(format);
			decimalPlaces = formatter.getMaximumFractionDigits();
		}

		// get the result as number
		Double result = null;
		try {
			result = formatter.parse(value).doubleValue();
		} catch (ParseException ex) {
			ex.printStackTrace();
		}

		// round the Double to the precision specified in the format string

		BigDecimal bd = new BigDecimal(result.toString());
		return bd;
		// Double roundedValue = bd.setScale( decimalPlaces,
		// RoundingMode.HALF_UP ).doubleValue();
		//
		// // output summary
		// System.out.println("\tValue = " + value);
		// System.out.println( locale == null ? "\tLocale not specified" :
		// "\tLocale = " + locale.toString());
		// System.out.println( format == null || format.length() == 0 ?
		// "\tFormat = Not specified" : "\tFormat = " + format);
		// System.out.println("\tResult (Double) = " + result);
		// System.out.println("\tRounded Result (Double) (" + decimalPlaces +
		// "dp) = " + roundedValue);
		// System.out.println("");
	}

	public static int formatNumberAsInteger(String number) {
		BigDecimal bdec = new BigDecimal(number);
		return bdec.intValue();
	}

}
