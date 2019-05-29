/**
 * 
 */
package com.cmm.jft.core.util;

import java.util.HashMap;

/**
 * <p>
 * <code>InputRowReport.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 19/09/2013 21:30:48
 *
 */
public class InputRowReport {

	private class Report {
		String report;
		int lineCount;
		int lines;
		String error = "";

		public Report(String report) {
			this.report = report;
		}

		String getReport() {
			return "Report: " + report + "\n" + "Lines read: " + lineCount
					+ "\n" + "Lines inserted: " + lines + "\n"
					+ (error.isEmpty() ? "" : "Error: " + error) + "\n";
		}

	}

	private HashMap<String, Report> reports;

	public InputRowReport() {
		this.reports = new HashMap<String, Report>();
	}

	public void startReport(String report) {
		reports.put(report, new Report(report));
		System.out.println(report);
	}

	public void count(String report) {
		reports.get(report).lineCount++;
	}

	public void stop(String report) {
		reports.get(report).lines = reports.get(report).lineCount;
	}

	public void reportError(String report, String error) {
		reports.get(report).error += "Error at line: "
				+ reports.get(report).lineCount + "\n";
	}

	public String report(String report) {
		return reports.get(report).getReport();
	}

	public String reportAll() {
		String ret = "";
		for (Report r : reports.values()) {
			stop(r.report);
			ret += r.getReport();
		}

		return ret;
	}

}
