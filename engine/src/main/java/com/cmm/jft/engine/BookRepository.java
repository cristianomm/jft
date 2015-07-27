package com.cmm.jft.engine;

import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Scanner;



/**
 * <p><code>BookRepository.java</code></p>
 * @author Cristiano M Martins
 * @version 27/07/2015 15:30:38
 *
 */
public class BookRepository {
	
	
	private HashMap<String, Book> books;
	
	
	public BookRepository() {
		this.books = new HashMap<String, Book>();
	}
	
	private void loadSymbols() {
		Scanner sc = new Scanner(BookRepository.class.getResourceAsStream("Symbols.csv"));
		
		while(sc.hasNext()) {
			String line = sc.next();
			
			createBook(line);
			
		}
		
		
	}
	
	public void createBook(String symbol) {
		
		if(symbol != null & !symbol.isEmpty()) {
			books.put(symbol, new Book(symbol));
		}
		
	}
	
	
	public Book getBook(String symbol) {
		return books.get(symbol);
	}
	
	
}
