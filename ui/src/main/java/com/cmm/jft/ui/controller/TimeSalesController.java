/**
 * 
 */
package com.cmm.jft.ui.controller;


import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.security.Security;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.ui.utils.ImageIcons;
import com.cmm.jft.vo.TimeSalesVO;

/**
 * @author Cristiano M Martins
 * @version October 29, 2015 at 10:24:44 PM
 *
 */
public class TimeSalesController extends AbstractController {

	@FXML
	private TextField txtSymbol;

	@FXML
	private Button btnSrchSymbol;
	
	@FXML
	private Label lblTradeCount;
	
	@FXML
	private Label lblLstVolume;
	
	@FXML
	private Label lblLstPrice;
	


	@FXML
	private TableView<TimeSalesVO> tblTimesSales;

	@FXML
	private TableColumn<TimeSalesVO, Date> colDate;

	@FXML
	private TableColumn<TimeSalesVO, Double> colPrice;

	@FXML
	private TableColumn<TimeSalesVO, Double> colVolume;

	@FXML
	private TableColumn<TimeSalesVO, String> colSell;

	@FXML
	private TableColumn<TimeSalesVO, String> colBuy;

	@FXML
	private TableColumn<TimeSalesVO, Character> colAgressor;

	private ObservableList<TimeSalesVO> data;

	private Security security;



	public TimeSalesController() {
		data = FXCollections.observableArrayList();
	}


	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {

		btnSrchSymbol.setGraphic(ImageIcons.getSecurityImage());
		
		btnSrchSymbol.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String symbol = txtSymbol.getText();
				if(symbol != null && symbol.length()>3){
					security = SecurityService.getInstance().provideSecurity(symbol);
				}
			}
		});
		
		DateTimeFormatter formatter = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.TIME_F3);
		colDate.setCellValueFactory(new PropertyValueFactory<>("dateTime"));
		colDate.setCellFactory(column -> {
			return new TableCell<TimeSalesVO, Date>(){
				protected void updateItem(Date item, boolean empty){
					super.updateItem(item, empty);
					if(item != null){
						setText(formatter.format(item));
					}
				}
			};
		});
		
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
		colBuy.setCellValueFactory(new PropertyValueFactory<>("buyer"));
		colSell.setCellValueFactory(new PropertyValueFactory<>("seller"));
		colAgressor.setCellValueFactory(new PropertyValueFactory<>("side"));
		colAgressor.setCellFactory(column ->{
			return new TableCell<TimeSalesVO, Character>(){
				protected void updateItem(Character item, boolean empty){
					super.updateItem(item, empty);
					if(item != null){
						long t0 = System.currentTimeMillis();
						tblTimesSales.getSelectionModel().selectLast();
						tblTimesSales.scrollTo(tblTimesSales.getItems().size());
						switch(item){
						case 'B':
							getTableRow().getStyleClass().add("time_sales_buy");
							break;
							
						case 'S':
							getTableRow().getStyleClass().add("time_sales_sell");
							break;
						}
						
						System.out.println(System.currentTimeMillis() - t0);
						
					}
				}
			};
		});
		
		
		tblTimesSales.setItems(data);
		
		new Thread(new Runnable(){

			@Override
			public void run() {
				for(int i=0;i<20;i++){
					TimeSalesVO vo = new TimeSalesVO();
					vo.dateTime = new Date();
					vo.price = 3988;
					vo.volume = 3+i;
					vo.buyer = "";
					vo.seller = "";
					vo.side = 'B';
					addData(vo);
					
					try {
						Thread.sleep(150);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					TimeSalesVO vo2 = new TimeSalesVO();
					vo2.dateTime = new Date();
					vo2.price = 3982;
					vo2.volume = 2+i;
					vo2.buyer = "";
					vo2.seller = "";
					vo2.side = 'S';
					addData(vo2);
					try {
						Thread.sleep(150 );
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			
		}).start();

	}

	@Override
	public void addData(Object data) {
		Platform.runLater(() -> {
			TimeSalesVO timeSalesVO = (TimeSalesVO) data;
			if(timeSalesVO!=null) {
				this.data.add(timeSalesVO);
				lblLstPrice.setText(timeSalesVO.getPrice() + "");
				lblLstVolume.setText(timeSalesVO.getVolume() + "");
			}
		});
		
	}


	@Override
	public void updateData(Object data) {
		// TODO Auto-generated method stub
		
	}



}
