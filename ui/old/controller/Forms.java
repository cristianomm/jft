package com.cmm.jft.ui.controller;

import java.util.LinkedHashMap;


/**
 * <p><code>Forms.java</code></p>
 * @author Cristiano M Martins
 * @version 06-11-2015 09:11:11
 *
 */
public class Forms {
	
	private LinkedHashMap<String, AbstractController> forms;
	
	private static Forms instance;
	
	
	private Forms() {
		this.forms = new LinkedHashMap<String, AbstractController>();
	}
	
	public static Forms getInstance() {
		if(instance == null) {
			instance = new Forms();
		}
		return instance;
	}
	
	
	public void addForm(String formName, AbstractController abstractController) {
		forms.put(formName, abstractController);
	}
	
	public void removeForm(String formName) {
		forms.remove(formName);
	}
	
}
