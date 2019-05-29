/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.apache.log4j.Level;

import com.cmm.jft.security.Security;
import com.cmm.jft.security.service.SecurityService;
import com.cmm.jft.trading.service.ExchangeTradingService;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.OrderValidityTypes;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.trading.enums.TradeTypes;
import com.cmm.jft.trading.exceptions.OrderException;
import com.cmm.jft.ui.utils.FormUtils;
import com.cmm.jft.ui.utils.ImageIcons;
import com.cmm.jft.ui.utils.Memory;
import com.cmm.jft.vo.SecurityVO;
import com.cmm.logging.Logging;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * <p>
 * <code>PlaceOrderController.java</code>
 * </p>
 * 
 * @author cristiano
 * @version Oct 11, 2015 7:22:46 PM
 *
 */
public class PlaceOrderController extends AbstractController {

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

    // @FXML
    // private Spinner<Double> spnLimit;

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

    private Security security;

    /**
     * 
     */
    public PlaceOrderController() {

    }

    /*
     * (non-Javadoc)
     * 
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

	changeSpinners("");

	txtComment.getStyleClass().add("font");
	txtSecurity.getStyleClass().add("font");

	// spnLimit.getStyleClass().add("font");
	spnLoss.getStyleClass().add("font");
	spnPrice.getStyleClass().add("font");
	spnProfit.getStyleClass().add("font");
	spnStop.getStyleClass().add("font");
	spnVolume.getStyleClass().add("font");

	cmbOrderTypes.getStyleClass().add("font");
	cmbValidityTypes.getStyleClass().add("font");

	dtDate.getStyleClass().add("font");

	cmbOrderTypes
	.setItems(FXCollections.observableArrayList(OrderTypes.values()).filtered(ot -> (ot == OrderTypes.Limit
	|| ot == OrderTypes.Market || ot == OrderTypes.Stop || ot == OrderTypes.StopLimit)));

	cmbValidityTypes.setItems(FXCollections.observableArrayList(OrderValidityTypes.values()));

	dtDate.setValue(LocalDate.now());
	dtDate.setConverter(new StringConverter<LocalDate>() {
	    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    @Override
	    public String toString(LocalDate localDate) {
		if (localDate == null)
		    return "";
		return dateTimeFormatter.format(localDate);
	    }

	    @Override
	    public LocalDate fromString(String dateString) {
		if (dateString == null || dateString.trim().isEmpty()) {
		    return null;
		}
		return LocalDate.parse(dateString, dateTimeFormatter);
	    }
	});

	txtSecurity.textProperty().addListener((obsrv, oldValue, newValue) -> {
	    if (newValue != null && !newValue.equalsIgnoreCase(oldValue)) {
		try {
		    security = Memory.getInstance().getSecurity();
		    changeSpinners(security.getSymbol());
		} catch (Exception e) {
		    Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	    }
	});

	cmbOrderTypes.valueProperty().addListener((observable, oldValue, newValue) -> {
	    if (newValue != oldValue) {
		changeOrderType(newValue);
	    }
	});
	/*
	 * cmbValidityTypes.valueProperty().addListener( (observable, oldValue,
	 * newValue) -> { if(newValue != oldValue){
	 * dtDate.setValue(LocalDate.now()); } });
	 */
	btnBuy.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    OrderTypes orderType = cmbOrderTypes.getValue();
		    String symbol = security.getSymbol();
		    int volume = spnVolume.getValue().intValue();
		    double price = spnPrice.getValue();
		    // double limitPrice = spnLimit.getValue();
		    double stopLoss = spnLoss.getValue();
		    double stopGain = spnProfit.getValue();
		    Date duration = Date.from(dtDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		    TradeTypes tradeType = TradeTypes.DAY_TRADE;
		    OrderValidityTypes validityType = cmbValidityTypes.getValue();
		    String comment = txtComment.getText();

		    int ret = TradingService.getInstance().newOrder(orderType, Side.BUY, symbol, volume, price,
			    stopLoss, stopGain, duration, tradeType, validityType, comment);

		    if (ret != 0) {
			Alert dialogoErro = new Alert(Alert.AlertType.ERROR);
			dialogoErro.setTitle("Place Order");
			dialogoErro.setHeaderText("Erro ao Criar Ordem");
			// dialogoErro.setContentText("Erro ao Criar Ordem");
			dialogoErro.showAndWait();
		    }

		} catch (Exception e) {
		    Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	    }
	});

	btnSell.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		try {
		    OrderTypes orderType = cmbOrderTypes.getValue();
		    String symbol = security.getSymbol();
		    int volume = spnVolume.getValue().intValue();
		    double price = spnPrice.getValue();
		    // double limitPrice = spnLimit.getValue();
		    double stopLoss = spnLoss.getValue();
		    double stopGain = spnProfit.getValue();
		    Date duration = Date.from(dtDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		    TradeTypes tradeType = TradeTypes.DAY_TRADE;
		    OrderValidityTypes validityType = cmbValidityTypes.getValue();
		    String comment = txtComment.getText();

		    TradingService.getInstance().newOrder(orderType, Side.SELL, symbol, volume, price, stopLoss,
			    stopGain, duration, tradeType, validityType, comment);
		} catch (Exception e) {
		    Logging.getInstance().log(getClass(), e, Level.ERROR);
		}
	    }
	});

	btnSrchSymbol.setGraphic(ImageIcons.getSecurityImage());
	btnSrchSymbol.setOnAction(new EventHandler<ActionEvent>() {
	    /*
	     * (non-Javadoc)
	     * 
	     * @see javafx.event.EventHandler#handle(javafx.event.Event)
	     */
	    @Override
	    public void handle(ActionEvent event) {
		Memory.getInstance().setTextField(txtSecurity);
		FormUtils.getInstance().openForm("../../../../../forms/SymbolsForm.fxml", "Symbols");
	    }
	});

    }

    private void changeOrderType(OrderTypes orderTypes) {

	spnLoss.getValueFactory().setValue(0d);
	spnPrice.getValueFactory().setValue(0d);
	spnProfit.getValueFactory().setValue(0d);
	spnStop.getValueFactory().setValue(0d);
	spnVolume.getValueFactory().setValue(0d);

	switch (orderTypes) {
	case Limit:
	    // spnLimit.setEditable(true);
	    spnPrice.setDisable(false);
	    spnVolume.setDisable(false);
	    spnStop.setDisable(true);
	    spnLoss.setDisable(false);
	    spnProfit.setDisable(false);
	    break;
	case Market:
	    spnPrice.setDisable(true);
	    spnVolume.setDisable(false);
	    spnStop.setDisable(true);
	    spnLoss.setDisable(false);
	    spnProfit.setDisable(false);
	    break;
	case Stop:
	    spnPrice.setDisable(true);
	    spnVolume.setDisable(false);
	    spnStop.setDisable(false);
	    spnLoss.setDisable(true);
	    spnProfit.setDisable(true);
	    break;
	case StopLimit:
	    spnPrice.setDisable(true);
	    spnVolume.setDisable(false);
	    spnStop.setDisable(false);
	    spnLoss.setDisable(true);
	    spnProfit.setDisable(true);
	    break;
	case MarketWithLeftOverAsLimit:
	default:
	    spnPrice.setDisable(true);
	    spnVolume.setDisable(true);
	    spnStop.setDisable(true);
	    spnLoss.setDisable(true);
	    spnProfit.setDisable(true);
	    break;
	}

    }

    private void changeSpinners(String symbol) {

	double min = 0, max = Double.MAX_VALUE;
	double initialPrice = 0;
	double maxVolume = 1000, minVolume = 1;
	double priceIncr = 1;
	double volumeIncr = 1;

	try {
	    Security security = SecurityService.getInstance().provideSecurity(symbol);
	    priceIncr = security.getSecurityInfoID().getTickSize();
	    volumeIncr = security.getSecurityInfoID().getStepVolume();
	    minVolume = security.getSecurityInfoID().getMinVolume();
	    maxVolume = security.getSecurityInfoID().getMaxVolume();

	} catch (NullPointerException e) {
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	} catch (Exception e) {
	    Logging.getInstance().log(getClass(), e, Level.ERROR);
	}

	// spnLimit.setValueFactory(new
	// SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialPrice,
	// priceIncr));
	spnStop.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialPrice, priceIncr));
	spnPrice.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initialPrice, priceIncr));

	spnProfit.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, 0, priceIncr));
	spnLoss.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, 0, priceIncr));

	spnVolume.setValueFactory(
		new SpinnerValueFactory.DoubleSpinnerValueFactory(minVolume, maxVolume, minVolume, volumeIncr));

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.ui.controller.AbstractController#getTitle()
     */
    @Override
    public String getTitle() {
	return "Place Order";
    }

    @Override
    public void addData(Object data) {
	// TODO Auto-generated method stub

    }

    @Override
    public void updateData(Object data) {
	// TODO Auto-generated method stub

    }

}
