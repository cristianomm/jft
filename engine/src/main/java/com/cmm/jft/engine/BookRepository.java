package com.cmm.jft.engine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Level;

import com.cmm.jft.engine.enums.MatchTypes;
import com.cmm.logging.Logging;


/**
 * <p><code>BookRepository.java</code></p>
 * @author Cristiano M Martins
 * @version 27/07/2015 15:30:38
 *
 */
public class BookRepository {

	private class BookDef{
		
		double priceLevel;
		HashSet<String> validOrderTypes;
		
	}
	
	private static BookRepository instance;
	private HashMap<String, Book> books;
	private HashMap<String, BookDef> symbolList;


	private BookRepository() {
		this.books = new HashMap<String, Book>();
		this.symbolList = new HashMap<String, BookDef>();
		loadSymbols();
		loadBooks();
	}

	/**
	 * @return the instance
	 */
	public static BookRepository getInstance() {
		if(instance == null){
			instance = new BookRepository();
		}
		return instance;
	}



	private void loadSymbols() {

		Scanner sc = null;
		try{
			sc = new Scanner(BookRepository.class.getResourceAsStream("Symbols.csv"));

			while(sc.hasNext()) {
				String line = sc.next().trim();
				
				String[] vals = line.split(";");
				
				HashSet<String> validOrderTypes = new HashSet<>();
				List<String> orderTypes = Arrays.asList(vals[1].split("\\s*,\\s*"));
		        validOrderTypes.addAll(orderTypes);
		        
		        BookDef bd = new BookDef();
		        bd.priceLevel = Double.parseDouble(vals[2]);
		        bd.validOrderTypes = validOrderTypes;
		        
				symbolList.put(vals[0], bd);
			}
			
			Logging.getInstance().log(getClass(), "Symbols list has loaded.", Level.INFO);
			
		}catch(Exception e){
			Logging.getInstance().log(getClass(), "Error on symbols loading.", e, Level.ERROR, false);
		}finally{
			if(sc != null){
				sc.close();
			}
		}

	}
	
	private void loadBooks(){
		try{
			//recover last books snapshots from database...
			
			
			//create remaining books
			for(String symbol : symbolList.keySet()){
				if(!books.containsKey(symbol)){
					BookDef bd = symbolList.get(symbol);
					createBook(symbol, bd.validOrderTypes, bd.priceLevel);
				}
			}
			
		}catch(Exception e){
			
		}
		
	}

	private void createBook(String symbol, HashSet<String> orderTypes, double protectionLevel) {

		if(symbol != null & !symbol.isEmpty()) {
			books.put(symbol, new Book(symbol, orderTypes, MatchTypes.FIFO, protectionLevel));
		}

	}


	public Book getBook(String symbol) {
		return books.get(symbol);
	}


}
