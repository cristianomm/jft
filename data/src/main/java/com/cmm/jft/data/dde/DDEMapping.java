/**
 * 
 */
package com.cmm.jft.data.dde;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

import com.cmm.jft.data.enums.DataFields;
import com.pretty_tools.dde.DDEException;
import com.pretty_tools.dde.client.DDEClientConversation;

/**
 * <p>
 * <code>DDEMapping.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 30/07/2013 01:36:57
 * 
 */
public class DDEMapping {

	public enum Language {
		PORTUGUESE, ENGLISH
	}

	private Language lang;
	private HashMap<String, HashMap<DataFields, Map>> mapFields;

	public DDEMapping() {
		this.mapFields = new HashMap<String, HashMap<DataFields, Map>>();
	}

	public void addSymbol(String symbol) {
		mapFields.put(symbol, new HashMap<DataFields, Map>());
	}

	public void addMap(String symbol, DataFields code, int row, int col) {
		Map m = new Map();
		m.code = code;
		m.column = col;
		m.row = row;

		if (mapFields.containsKey(symbol)) {
			mapFields.get(symbol).put(code, m);
		}
	}

	public HashMap<DataFields, Map> getMaps(String symbol) {
		return mapFields.get(symbol);
	}

	public Iterator<String> getSymbols() {
		return mapFields.keySet().iterator();
	}

	public Language getMapLanguage() {
		return lang;
	}

	public boolean contains(String symbol) {
		return mapFields.containsKey(symbol);
	}

	public void map(DDEClientConversation ddeConv) {

		Properties p = new Properties();
		try {
			p.load(new FileInputStream("./src/main/resources/mapdde.properties"));
			int iniRow = Integer.parseInt(p.getProperty("initialRow", "8"));
			boolean pt = Boolean.parseBoolean(p.getProperty("excelLangPT",
					"true"));
			String colFile = p.getProperty("columnsFile","MapColumns.properties");

			if (pt) {
				lang = Language.PORTUGUESE;
			} else {
				lang = Language.ENGLISH;
			}

			Properties pc = new Properties();
			pc.load(new FileInputStream(colFile));

			boolean loop = true;
			while (loop) {
				Map m = new Map();
				m.code = DataFields.SYMBOL;
				m.column = Integer.parseInt((String) pc.getProperty(
						DataFields.SYMBOL.name(), "1"));
				m.row = iniRow;
				String sym = ddeConv.request(m.getPosition(lang))
						.replace("\n", "").replace("\r", "");
				if (sym.isEmpty())
					loop = false;
				if (!mapFields.containsKey(sym)) {
					iniRow++;
					continue;
				}

				for (Object k : pc.keySet()) {
					DataFields df = DataFields.valueOf((String) k);

					addMap(sym, df, iniRow,
							Integer.parseInt(pc.getProperty(df.name())));
				}
				iniRow++;

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (DDEException e) {
			e.printStackTrace();
		}

	}

}
