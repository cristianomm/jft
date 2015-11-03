/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import com.cmm.jft.security.Security;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.services.SecurityService;
import com.cmm.jft.trading.services.TradingService;
import com.cmm.jft.ui.utils.FormUtils;
import com.cmm.jft.ui.utils.ImageIcons;
import com.cmm.jft.ui.utils.Memory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * <p><code>PlaceOrderController.java</code></p>
 * @author cristiano
 * @version Oct 11, 2015 7:22:46 PM
 *
 */
public class PlaceOrderController implements Initializable {
	
	@FXML
	private Button btnBuy;
	
	@FXML
	private Button btnSell;
	
	@FXML
	private Button btnSrchSymbol;
	
	@FXML
	private Spinner<Double> spnVolume;
	
	@FXML
	private Spinner<Double> spnPrice;
	
	@FXML
	private Spinner<Double> spnLimit;
	
	@FXML
	private Spinner<Double> spnStop;
	
	@FXML
	private Spinner<Double> spnProfit;
	
	@FXML
	private Spinner<Double> spnLoss;
	
	@FXML
	private ComboBox<OrderTypes> cmbOrderTypes;
	
	@FXML
	private ComboBox<OrderValidityTypes> cmbValidityTypes;
	
	@FXML
	private TextField txtSecurity;
	
	@FXML
	private TextField txtComment;
	
	@FXML
	private DatePicker dtDate;
	
	
	/**
	 * 
	 */
	public PlaceOrderController() {
		
	}
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		setSpinners(null);
		
		
		txtComment.getStyleClass().add("font");
		txtSecurity.getStyleClass().add("font");
		
		spnLimit.getStyleClass().add("font");
		spnLoss.getStyleClass().add("font");
		spnPrice.getStyleClass().add("font");
		spnProfit.getStyleClass().add("font");
		spnStop.getStyleClass().add("font");
		spnVolume.getStyleClass().add("font");
		
		cmbOrderTypes.getStyleClass().add("font");
		cmbValidityTypes.getStyleClass().add("font");
		
		dtDate.getStyleClass().add("font");
		
		txtSecurity.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				if(txtSecurity.getText().length()>1){
					setSpinners(txtSecurity.getText());
				}
			}
		});
		
		
		cmbOrderTypes.setItems(FXCollections.observableArrayList(
				OrderTypes.values())
				.filtered(ot -> 
				(ot == OrderTypes.Limit || ot == OrderTypes.Market || 
				ot == OrderTypes.Stop || ot == OrderTypes.StopLimit)));
		
		cmbValidityTypes.setItems(FXCollections.observableArrayList(OrderValidityTypes.values()));
				
		
		btnBuy.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				OrderTypes orderType = cmbOrderTypes.getValue();
				String symbol = txtSecurity.getText();
				double volume = spnVolume.getValue();
				double price = spnPrice.getValue();
				double limitPrice = spnLimit.getValue();
				double stopLoss = spnLoss.getValue();
				double stopGain = spnProfit.getValue();
				Date duration = Date.from(dtDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
				TradeTypes tradeType =TradeTypes.DAY_TRADE;
				
				TradingService.getInstance().newOrder(
						orderType, Side.BUY, symbol, 
						volume, price, limitPrice, stopLoss, stopGain, 
						duration, tradeType
				);
			}
		});
		
		btnSell.setOnAction(new EventHandler<ActionEvent>() {
			
			@Override
			public void handle(ActionEvent event) {
				OrderTypes orderType = cmbOrderTypes.getValue();
				String symbol = txtSecurity.getText();
				double volume = spnVolume.getValue();
				double price = spnPrice.getValue();
				double limitPrice = spnLimit.getValue();
				double stopLoss = spnLoss.getValue();
				double stopGain = spnProfit.getValue();
				Date duration = Date.from(dtDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
				TradeTypes tradeType =TradeTypes.DAY_TRADE;
				
				TradingService.getInstance().newOrder(
						orderType, Side.SELL, symbol, 
						volume, price, limitPrice, stopLoss, stopGain, 
						duration, tradeType
				);
			}
		});
		
		
		btnSrchSymbol.setGraphic(ImageIcons.getSecurityImage());
		btnSrchSymbol.setOnAction(new EventHandler<ActionEvent>() {
			/* (non-Javadoc)
			 * @see javafx.event.EventHandler#handle(javafx.event.Event)
			 */
			@Override
			public void handle(ActionEvent event) {
				Memory.getInstance().setTextField(txtSecurity);
				FormUtils.getInstance().openForm("../../../../../forms/SymbolsForm.fxml", "Symbols");
			}
		});
		
	}
	
	
	private void setSpinners(String symbol){
		
		double min=0, max=0; 
		double initialPrice = 0, initialVolume = 0;
		double maxVolume = 1000, minVolume = 1;
		double priceIncr = 1;
		double volumeIncr = 1;
		
		try{
			Security security = SecurityService.getInstance().provideSecurity(symbol);
			priceIncr = security.getSecurityInfoID().getTickSize();
			volumeIncr = security.getSecurityInfoID().getStepVolume();
			minVolume = security.getSecurityInfoID().getMinVolume();
			maxVolume = security.getSecurityInfoID().getMaxVolume();
		}catch(NullPointerException e){}
		catch(Exception e){}
		
		spnLimit.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialPrice, priceIncr));
		spnStop.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialPrice, priceIncr));
		spnPrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialPrice, priceIncr));
		
		spnProfit.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, 0, priceIncr));
		spnLoss.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, 0, priceIncr));
		
		spnVolume.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(minVolume, maxVolume, initialVolume, volumeIncr));
		
	}

}
