/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.cmm.jft.vo.SecurityVO;
import com.cmm.jft.security.Security;
import com.cmm.jft.security.service.SecurityService;
import com.cmm.jft.ui.utils.Memory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 * <p>
 * <code>SymbolsController.java</code>
 * </p>
 * 
 * @author cristiano
 * @version Nov 2, 2015 2:23:58 PM
 *
 */
public class SymbolsController extends AbstractController {

    @FXML
    private Button btnOK;

    @FXML
    private Button btnCancel;

    @FXML
    private TextField txtSymbol;

    @FXML
    private TableView<Security> tblSymbols;

    @FXML
    private TableColumn<Security, String> colSymbol;

    @FXML
    private TableColumn<Security, String> colDescription;

    private ObservableList<Security> data;

    /**
     * 
     */
    public SymbolsController() {
	this.data = FXCollections.observableArrayList();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

	colSymbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
	colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));

	/*
	 * txtSymbol.setOnKeyPressed(new EventHandler<Event>() {
	 * 
	 * @Override public void handle(Event event) {
	 * if(txtSymbol.getText().length()>1){
	 * filterResults(txtSymbol.getText()); }else{ filterResults(null); } }
	 * });
	 */

	txtSymbol.textProperty().addListener((observable, oldValue, newValue) -> {
	    if (!newValue.equalsIgnoreCase(oldValue)) {
		filterResults(newValue);
	    } else {
		filterResults(null);
	    }
	});

	btnOK.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		if (tblSymbols.getSelectionModel().getSelectedIndex() >= 0) {
		    selectSecurity(tblSymbols.getSelectionModel().getSelectedItem());
		}
	    }
	});

	btnCancel.setOnAction(new EventHandler<ActionEvent>() {
	    /*
	     * (non-Javadoc)
	     * 
	     * @see javafx.event.EventHandler#handle(javafx.event.Event)
	     */
	    @Override
	    public void handle(ActionEvent event) {
		Stage s = (Stage) btnCancel.getScene().getWindow();
		s.close();
	    }
	});

	tblSymbols.setRowFactory(call -> {
	    TableRow<Security> row = new TableRow<>();
	    row.setOnMouseClicked(event -> {
		if ((event.getClickCount() == 1) && !row.isEmpty()) {
		    Memory.getInstance().setSecurity(row.getItem());
		} else if ((event.getClickCount() == 2) && !row.isEmpty()) {
		    selectSecurity(row.getItem());
		}
	    });

	    row.setOnKeyTyped(new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
		    if ((event.getCode() == KeyCode.ENTER) && !row.isEmpty()) {
			selectSecurity(row.getItem());
		    }
		}
	    });

	    return row;
	});

	tblSymbols.setItems(data);

	txtSymbol.requestFocus();

	addData();

    }

    private void selectSecurity(Security security) {
	if (security != null) {
	    Memory.getInstance().getTextField().setText(security.getSymbol() + " - " + security.getDescription());
	    Memory.getInstance().setSecurity(security);
	    Stage s = (Stage) btnOK.getScene().getWindow();
	    s.close();
	}
    }

    private void addData() {
	data.clear();
	List<Security> lst = SecurityService.getInstance().getSecurityList();
	lst.forEach(sec -> data.add(sec));

    }

    private void filterResults(String filter) {
	addData();
	if (filter != null) {
	    data.removeIf(s -> !s.getSymbol().startsWith(filter.toUpperCase()));
	}
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.ui.controller.AbstractController#getTitle()
     */
    @Override
    public String getTitle() {
	return "Symbols";
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
