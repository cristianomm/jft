/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.cmm.jft.vo.SecurityVO;
import com.cmm.jft.security.Security;
import com.cmm.jft.trading.services.SecurityService;
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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * <p><code>SymbolsController.java</code></p>
 * @author cristiano
 * @version Nov 2, 2015 2:23:58 PM
 *
 */
public class SymbolsController implements Initializable {

	@FXML
	private Button btnOK;
	
	@FXML
	private Button btnCancel;
	
	@FXML
	private TextField txtSymbol;
	
	@FXML
	private TableView<SecurityVO> tblSymbols;
	
	@FXML
	private TableColumn<SecurityVO, String> colSymbol;
	
	@FXML
	private TableColumn<SecurityVO, String> colDescription;
	
	
	private ObservableList<SecurityVO> data;
	
	private SecurityVO selected;
	
	/**
	 * 
	 */
	public SymbolsController() {
		this.data = FXCollections.observableArrayList();
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		colSymbol.setCellValueFactory(new PropertyValueFactory<>("symbol"));
		colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		
		txtSymbol.setOnKeyPressed(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				if(txtSymbol.getText().length()>1){
					filterResults(txtSymbol.getText());
				}else{
					filterResults(null);
				}
			}
		});
		
		
		
		btnOK.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(tblSymbols.getSelectionModel().getSelectedIndex()>=0){
					selected = tblSymbols.getSelectionModel().getSelectedItem();
					Memory.getInstance().getTextField().setText(selected.getSymbol());
					Memory.getInstance().setSecurityVO(selected);
					Stage s = (Stage) btnOK.getScene().getWindow();
					s.close();
				}
			}
		});
		
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			/* (non-Javadoc)
			 * @see javafx.event.EventHandler#handle(javafx.event.Event)
			 */
			@Override
			public void handle(ActionEvent event) {
				Stage s = (Stage) btnCancel.getScene().getWindow();
				s.close();
			}
		});
		
		tblSymbols.setItems(data);
		
		addData();

	}
	
	
	private void addData(){
		data.clear();
		List<Security> lst = SecurityService.getInstance().getSecurityList();
		lst.forEach(
			sec -> data.add(new SecurityVO(sec.getSymbol(), sec.getDescription()))
		);
		
	}
	
	private void filterResults(String filter){
		addData();
		if(filter!=null){
			data.removeIf( s -> !s.getSymbol().startsWith(filter));
		}
	}

}
