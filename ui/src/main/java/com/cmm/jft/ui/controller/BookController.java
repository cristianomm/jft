/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Level;

import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.security.Security;
import com.cmm.jft.services.marketdata.Market;
import com.cmm.jft.services.marketdata.MarketDataService;
import com.cmm.jft.ui.utils.FormUtils;
import com.cmm.jft.ui.utils.ImageIcons;
import com.cmm.jft.ui.utils.Memory;
import com.cmm.logging.Logging;
import com.sun.javafx.collections.ObservableListWrapper;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * <p>
 * <code>BookController.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 21/02/2017 09:39:48
 *
 */
public class BookController extends AbstractController {

    @FXML
    private Pane formBook;

    @FXML
    private TableView<MDEntry> tblBid;

    @FXML
    private TableColumn<MDEntry, String> colBidBroker;

    @FXML
    private TableColumn<MDEntry, Integer> colBidNOrd;

    @FXML
    private TableColumn<MDEntry, Integer> colBidQt;

    @FXML
    private TableColumn<MDEntry, Double> colBidPrice;

    @FXML
    private TableView<MDEntry> tblAsk;

    @FXML
    private TableColumn<MDEntry, String> colAskBroker;

    @FXML
    private TableColumn<MDEntry, Integer> colAskNOrd;

    @FXML
    private TableColumn<MDEntry, Integer> colAskQt;

    @FXML
    private TableColumn<MDEntry, Double> colAskPrice;

    @FXML
    private Button btnSrchSymbol;

    @FXML
    private TextField txtSecurity;


    private ObservableList<MDEntry> buyOrders;
    private ObservableList<MDEntry> sellOrders;

    private Security security;
    
    
    /**
     * 
     */
    public BookController() {
	this.buyOrders = FXCollections.observableArrayList();
	this.sellOrders = FXCollections.observableArrayList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
	try {

	    colAskBroker.setCellValueFactory(
		    (TableColumn.CellDataFeatures<MDEntry, String> val) -> new ReadOnlyStringWrapper(
			    val.getValue().getMdEntrySeller()));

	    colAskNOrd.setCellValueFactory(
		    (TableColumn.CellDataFeatures<MDEntry, Integer> val) -> new ReadOnlyObjectWrapper<>(
			    val.getValue().getNumberOfOrders()));

	    colAskPrice.setCellValueFactory(
		    (TableColumn.CellDataFeatures<MDEntry, Double> val) -> new ReadOnlyObjectWrapper<>(
			    val.getValue().getMdEntryPx()));

	    colAskQt.setCellValueFactory(
		    (TableColumn.CellDataFeatures<MDEntry, Integer> val) -> new ReadOnlyObjectWrapper<>(
			    val.getValue().getMdEntrySize()));

	    colBidBroker.setCellValueFactory(
		    (TableColumn.CellDataFeatures<MDEntry, String> val) -> new ReadOnlyStringWrapper(
			    val.getValue().getMdEntryBuyer()));

	    colBidNOrd.setCellValueFactory(
		    (TableColumn.CellDataFeatures<MDEntry, Integer> val) -> new ReadOnlyObjectWrapper<>(
			    val.getValue().getNumberOfOrders()));

	    colBidPrice.setCellValueFactory(
		    (TableColumn.CellDataFeatures<MDEntry, Double> val) -> new ReadOnlyObjectWrapper<>(
			    val.getValue().getMdEntryPx()));

	    colBidQt.setCellValueFactory(
		    (TableColumn.CellDataFeatures<MDEntry, Integer> val) -> new ReadOnlyObjectWrapper<>(
			    val.getValue().getMdEntrySize()));

	    tblAsk.setItems(sellOrders);
	    tblBid.setItems(buyOrders);

	    tblAsk.setRowFactory(call -> {
		final TableRow<MDEntry> row = new TableRow<>();
		row.setOnMouseClicked(event -> {
		    if (row != null && row.isEmpty()) {

		    }
		});
		return row;
	    });

	    tblBid.setRowFactory(call -> {
		final TableRow<MDEntry> row = new TableRow<>();
		row.setOnMouseClicked(event -> {
		    if (row != null && row.isEmpty()) {

		    }
		});
		return row;
	    });

	    txtSecurity.textProperty().addListener((obsrv, oldValue, newValue) -> {
		if (newValue != null && !newValue.equalsIgnoreCase(oldValue)) {
		    try {
			security = Memory.getInstance().getSecurity();
			loadMarket();
		    } catch (Exception e) {
			Logging.getInstance().log(getClass(), e, Level.ERROR);
		    }
		}
	    });

	    btnSrchSymbol.setGraphic(ImageIcons.getSecurityImage());
	    btnSrchSymbol.setOnAction(new EventHandler<ActionEvent>() {
		@Override
		public void handle(ActionEvent event) {
		    Memory.getInstance().setTextField(txtSecurity);
		    FormUtils.getInstance().openForm("../../../../../forms/SymbolsForm.fxml", "Symbols");
		}
	    });

	} catch (Exception e) {
	    e.printStackTrace();
	}

    }

    private void loadMarket(){
	Market market = MarketDataService.getInstance().getMarket(security.getSecurityID());
	this.buyOrders = FXCollections.observableList(market.getBidMBO());
	this.sellOrders = FXCollections.observableList(market.getAskMBO());
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.ui.controller.AbstractController#getTitle()
     */
    @Override
    public String getTitle() {
	return security != null ? security.getSymbol() : "Book";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmm.jft.ui.controller.AbstractController#addData(java.lang.Object)
     */
    @Override
    public void addData(Object data) {
	if (data instanceof Security) {
	    security = (Security) data;
	    loadMarket();
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.cmm.jft.ui.controller.AbstractController#updateData(java.lang.Object)
     */
    @Override
    public void updateData(Object data) {
	// TODO Auto-generated method stub

    }

}
