/**
 * 
 */
package com.cmm.jft.data.dde;

import com.cmm.jft.data.dde.DDEMapping.Language;
import com.cmm.jft.data.enums.DataFields;

/**
 * <p>
 * <code>Map.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 30/07/2013 03:25:52
 * 
 */
public class Map {
	public static final String ptFormat = "L{rv}C{cv}";
	public static final String engFormat = "R{rv}C{cv}";

	DataFields code;
	int row;
	int column;

	public String getPosition(Language lang) {
		if (lang == Language.ENGLISH)
			return getExcelPositionEngFormat();
		return getExcelPositionPTFormat();
	}

	private String getExcelPositionPTFormat() {
		return ptFormat.replace("{rv}", "" + row).replace("{cv}", "" + column);
	}

	private String getExcelPositionEngFormat() {
		return engFormat.replace("{rv}", "" + row).replace("{cv}", "" + column);
	}

}
