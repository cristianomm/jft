package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.cmm.jft.trading.enums.Side;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

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
	
	@FXML
	private TableColumn<DOMElement, Button> colBuyBtn;
	
	@FXML
	private TableColumn<DOMElement, Integer> colBuy;
	
	@FXML
	private TableColumn<DOMElement, Double> colPrice;
	
	@FXML
	private TableColumn<DOMElement, Integer> colSell;
	
	@FXML
	private TableColumn<DOMElement, Button> colSellBtn;
	
	
	
	
	private ObservableList<DOMElement> data;
	
	
	/**
	 * 
	 */
	public DOMController() {
		//this.tblDOM = new TableView<>();
		this.data = FXCollections.observableArrayList();
	}
	
	
	
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		btnBuy.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				colBuy.setStyle("-fx-background-color: " + txtSecurity.getText());
			}
		});
		
		btnSell.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				colSell.setStyle("-fx-background-color: " + txtSecurity.getText());
			}
		});
		
		
		//colBuyBtn.setCellValueFactory(new PropertyValueFactory<>("volume"));
		colBuy.setCellValueFactory(new PropertyValueFactory<>("volume"));
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colSell.setCellValueFactory(new PropertyValueFactory<>("volume"));
		//colSellBtn.setCellValueFactory(new PropertyValueFactory<>("volume"));
		
		data.add(new DOMElement(Side.SELL, 12.49, 100));
		data.add(new DOMElement(Side.SELL, 12.49, 200));
		data.add(new DOMElement(Side.SELL, 12.49, 300));
		data.add(new DOMElement(Side.SELL, 12.49, 1000));
		data.add(new DOMElement(Side.SELL, 12.48, 400));
		data.add(new DOMElement(Side.SELL, 12.47, 300));
		data.add(new DOMElement(Side.SELL, 12.46, 1200));
		
		data.add(new DOMElement(Side.BUY, 12.45, 100));
		data.add(new DOMElement(Side.BUY, 12.45, 200));
		data.add(new DOMElement(Side.BUY, 12.45, 300));
		data.add(new DOMElement(Side.BUY, 12.45, 400));
		data.add(new DOMElement(Side.BUY, 12.44, 500));
		data.add(new DOMElement(Side.BUY, 12.43, 400));
		data.add(new DOMElement(Side.BUY, 12.42, 1000));
		tblDOM.setItems(data);
		
	}
	

}
