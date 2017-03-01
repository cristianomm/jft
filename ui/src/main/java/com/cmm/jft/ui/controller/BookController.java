/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.cmm.jft.marketdata.MDEntry;
import com.cmm.jft.security.Security;
import com.sun.javafx.collections.ObservableListWrapper;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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

    private ObservableListWrapper<MDEntry> buyOrders;
    private ObservableListWrapper<MDEntry> sellOrders;

    private Security security;

    
    
    /**
     * 
     */
    public BookController() {
	
	this.buyOrders = FXCollections.observableList(list);
	this.sellOrders = FXCollections.observableList(lst);
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

	} catch (Exception e) {
	    e.printStackTrace();
	}

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
