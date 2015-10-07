/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.cmm.jft.trading.Orders;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;

/**
 * <p><code>OrderManagerController.java</code></p>
 * @author Cristiano M Martins
 * @version Oct 6, 2015 9:39:07 PM
 *
 */
public class OrderManagerController implements Initializable {

	
	@FXML
	private Button btnCancelAll;
	
	@FXML
	private Button btnModifyOrdr;
	
	@FXML
	private Button btnCancelOrdr;
	
	@FXML
	private Label lblOrdrCount;
	
	@FXML
	private Label lblPosition;
	
	@FXML
	private Label lblProfit;
	
	@FXML
	private Label lblOpenValue;
	
	@FXML
	private Image imgStatus;
	
	
	private TreeTableView<Orders> tblOrders;
	
	
	
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		
	}

}
