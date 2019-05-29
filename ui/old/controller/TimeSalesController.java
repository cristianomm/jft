/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.TreeMap;

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
import com.cmm.jft.data.extractor.marketdata.BovespaTradeFileExtractor;
import com.cmm.jft.security.Security;
import com.cmm.jft.security.service.SecurityService;
import com.cmm.jft.ui.utils.ImageIcons;
import com.cmm.jft.vo.Extractable;
import com.cmm.jft.vo.TimeSalesVO;
import com.cmm.jft.vo.TradeVO;

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
    private TableView<TradeVO> tblTimesSales;

    @FXML
    private TableColumn<TradeVO, String> colID;

    @FXML
    private TableColumn<TradeVO, Date> colDate;

    @FXML
    private TableColumn<TradeVO, Double> colPrice;

    @FXML
    private TableColumn<TradeVO, Integer> colVolume;

    @FXML
    private TableColumn<TradeVO, String> colSell;

    @FXML
    private TableColumn<TradeVO, String> colBuy;

    @FXML
    private TableColumn<TradeVO, Character> colAgressor;

    private ObservableList<TradeVO> data;

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
		if (symbol != null && symbol.length() > 3) {
		    security = SecurityService.getInstance().provideSecurity(symbol);
		}
	    }
	});

	colID.setCellValueFactory(new PropertyValueFactory<>("tradeID"));
	
	DateTimeFormatter formatter = (DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.TIME_F4);
	colDate.setCellValueFactory(new PropertyValueFactory<>("tradeTime"));
//	colDate.setCellFactory(column -> {
//	    return new TableCell<TradeVO, Date>() {
//		protected void updateItem(Date item, boolean empty) {
//		    super.updateItem(item, empty);
//		    if (item != null) {
//			setText(formatter.format(item));
//		    }
//		}
//	    };
//	});

	colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
//	colPrice.setCellFactory(column -> {
//	    return new TableCell<TradeVO, Double>(){
//		protected void updateItem(Double item, boolean empty){
//		    super.updateItem(item, empty);
//		    if(item != null){
//			setText(String.format("%1$.3f", item));
//		    }
//		}
//	    };
//	});
	
	colVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
	
	colBuy.setCellValueFactory(new PropertyValueFactory<>("buyBroker"));
	colSell.setCellValueFactory(new PropertyValueFactory<>("sellBroker"));
	
	colAgressor.setCellValueFactory(new PropertyValueFactory<>("agressor"));
	colAgressor.setCellFactory(column -> {
	    return new TableCell<TradeVO, Character>() {
		protected void updateItem(Character item, boolean empty) {
		    super.updateItem(item, empty);
		    if (item != null) {
			//long t0 = System.currentTimeMillis();
			tblTimesSales.getSelectionModel().selectLast();
			tblTimesSales.scrollTo(tblTimesSales.getItems().size());
			switch (item.charValue()) {
			case 'B':
			    getTableRow().getStyleClass().add("time_sales_buy");
			    break;

			case 'S':
			    getTableRow().getStyleClass().add("time_sales_sell");
			    break;
			}

		    }
		}
	    };
	});

	tblTimesSales.setItems(data);

	new Thread(new Runnable() {

	    @Override
	    public void run() {

		Properties p = new Properties();
		p.put("filename", "D:\\Disco\\Users\\Cristiano\\Downloads\\BMF Files\\Copy_NEG_BMF_20170315.TXT");
		p.put("columnFilter", "1;WDOJ17");


		BovespaTradeFileExtractor be = new BovespaTradeFileExtractor();
		be.config(p);

		List<Extractable> l = be.extract();

		TreeMap<Date, List<Extractable>> sales = new TreeMap<>();
		for(Extractable et:l){
		    TradeVO t = (TradeVO) et;
		    if(!sales.containsKey(t.tradeTime)){
			sales.put(t.tradeTime, new ArrayList<>());
		    }
		    sales.get(t.tradeTime).add(t);

		}


		for(List<Extractable> et:sales.values()){
		    for(Extractable ex:et){
			addData(ex);
		    }

		    try {
			Thread.sleep(150);
		    } catch (InterruptedException e) {
			e.printStackTrace();
		    }

		}

	    }

	}).start();

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.ui.controller.AbstractController#getTitle()
     */
    @Override
    public String getTitle() {
	return "Time & Sales";
    }

    @Override
    public void addData(Object data) {
	Platform.runLater(() -> {
	    TradeVO timeSalesVO = (TradeVO) data;
	    if (timeSalesVO != null) {
		this.data.add(timeSalesVO);
		lblLstPrice.setText(timeSalesVO.price + "");
		lblLstVolume.setText(timeSalesVO.volume + "");
		lblTradeCount.setText(this.data.size() + "");
	    }
	});

    }

    @Override
    public void updateData(Object data) {
	// TODO Auto-generated method stub

    }

}
