package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.cmm.jft.trading.enums.Side;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class DOMController implements Initializable{

	@FXML
	private Button btnBuy;
	
	@FXML
	private Button btnSell;
	
	@FXML
	private Button btnClose;
	
	@FXML
	private TextField txtProfit;
	
	@FXML
	private TextField txtPosition;
	
	@FXML
	private TextField txtSecurity;
	
	@FXML
	private Spinner<Double> spnStopLoss;
	
	@FXML
	private Spinner<Double> spnProfit;
	
	@FXML
	private Spinner<Double> spnVolume;
	
	@FXML
	private DatePicker dtDate;
	
	@FXML
	private TableView<DOMElement> tblDOM;
	
	
	private class DOMElement {
		
		Side side;
		double price;
		double volume;
		
	}
	
	
	
	
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		System.out.println("1");
		btnBuy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Pressed!");
			}
		});
		
		
	}
	

}
