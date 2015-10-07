/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.cmm.jft.core.vo.OrdersVO;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.Side;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * <p><code>OrderManagerController.java</code></p>
 * @author Cristiano M Martins
 * @version Oct 6, 2015 9:39:07 PM
 *
 */
public class OrderManagerController implements Initializable {

	
	@FXML
	private Button btnCancelAll;
	
	@FXML
	private Button btnModifyOrdr;
	
	@FXML
	private Button btnCancelOrdr;
	
	@FXML
	private Label lblOrdrCount;
	
	@FXML
	private Label lblPosition;
	
	@FXML
	private Label lblProfit;
	
	@FXML
	private Label lblOpenValue;
	
	@FXML
	private ImageView imgStatus;
	
	
	
	
	@FXML
	private TableColumn<OrdersVO, String> colSymbol;
	
	@FXML
	private TableColumn<OrdersVO, Character> colStatus;
	
	@FXML
	private TableColumn<OrdersVO, Character> colSide;
	
	@FXML
	private TableColumn<OrdersVO, Date> colDate;
	
	@FXML
	private TableColumn<OrdersVO, Double> colVolume;
	
	@FXML
	private TableColumn<OrdersVO, Double> colExecVolume;
	
	@FXML
	private TableColumn<OrdersVO, Double> colPrice;
	
	@FXML
	private TableColumn<OrdersVO, Double> colAvgPrice;
	
	@FXML
	private TableColumn<OrdersVO, Double> colStopLoss;
	
	@FXML
	private TableColumn<OrdersVO, Double> colStopGain;
	
	@FXML
	private TableView<OrdersVO> tblOrders;
	
	
	private ObservableList<OrdersVO> data;
	
	
	public OrderManagerController() {
		data = FXCollections.observableArrayList();
	}
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
				
		OrdersVO vo = new OrdersVO();
		vo.setSecurityID("WDOV15");
		vo.setOrderDateTime(new Date());
		vo.setSide(Side.BUY.getValue());
		vo.setOrderStatus(OrderStatus.NEW.getValue());
		vo.setVolume(1);
		vo.setExecutedVolume(0);
		vo.setPrice(12.4);
		vo.setStopPrice(0);
		vo.setAvgPrice(0);
		
		
		data.add(vo);
				
		colSymbol.setCellValueFactory(new PropertyValueFactory<>("securityID"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("orderDateTime"));
		
		colSide.setCellValueFactory(new PropertyValueFactory<>("side"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
				
		colVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
		colExecVolume.setCellValueFactory(new PropertyValueFactory<>("executedVolume"));
		
		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colStopGain.setCellValueFactory(new PropertyValueFactory<>("stopPrice"));
		colStopLoss.setCellValueFactory(new PropertyValueFactory<>("stopPrice"));
		colAvgPrice.setCellValueFactory(new PropertyValueFactory<>("avgPrice"));
				
		tblOrders.setItems(data);
		System.out.println(tblOrders.getItems().size());
	}

}
