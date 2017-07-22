package com.cmm.jft.ui.controller;

import javafx.fxml.Initializable;


/**
 * 
 * @author Cristiano M Martins
 * @version 06-11-2015 09:34
 *
 */
public abstract class AbstractController implements Initializable {
	
	public abstract String getTitle();
	
	public abstract void addData(Object data);
	
	public abstract void updateData(Object data);
	
}
