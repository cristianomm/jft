/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.math.BigDecimal;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.cmm.jft.core.vo.OrdersVO;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.Side;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableColumn.CellDataFeatures;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.util.Callback;

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
	private Image imgStatus;
	
	
	
	
	@FXML
	private TreeTableColumn<OrdersVO, String> colSymbol;
	
	@FXML
	private TreeTableColumn<OrdersVO, OrderStatus> colStatus;
	
	@FXML
	private TreeTableColumn<OrdersVO, Side> colSide;
	
	@FXML
	private TreeTableColumn<OrdersVO, Date> colDate;
	
	@FXML
	private TreeTableColumn<OrdersVO, Double> colVolume;
	
	@FXML
	private TreeTableColumn<OrdersVO, Double> colExecVolume;
	
	@FXML
	private TreeTableColumn<OrdersVO, Double> colPrice;
	
	@FXML
	private TreeTableColumn<OrdersVO, Double> colAvgPrice;
	
	@FXML
	private TreeTableColumn<OrdersVO, Double> colStopLoss;
	
	@FXML
	private TreeTableColumn<OrdersVO, Double> colStopGain;
	
	private TreeTableView<OrdersVO> tblOrders;
	private List<OrdersVO> data;
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
		OrdersVO vo = new OrdersVO();
		vo.setPrice(12.4);
		vo.side = Side.BUY.getValue();
				
		data = FXCollections.observableArrayList();
		data.add(vo);
		
		TreeItem<OrdersVO> orders = new TreeItem<>();
		data.forEach(ordr -> orders.getChildren().add(new TreeItem<OrdersVO>(ordr)));
				
		
		colPrice.setCellValueFactory(
				(TreeTableColumn. CellDataFeatures<OrdersVO, Double> param) ->
				new ReadOnlyDoubleWrapper(param.getValue().getValue().getPrice()).asObject()
				);
		
		tblOrders.setRoot(orders);
		tblOrders.getColumns().setAll(colPrice);
		
		
	}

}
