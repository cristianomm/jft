/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.Side;
import com.cmm.jft.vo.OrdersVO;

import javafx.beans.property.ReadOnlyDoubleWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
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

	@FXML
	private TreeTableView<OrdersVO> tblOrders;


	private ObservableList<OrdersVO> data;

	private TreeItem<OrdersVO> root; 

	public OrderManagerController() {
		root = new TreeItem<>(new OrdersVO());
		data = FXCollections.observableArrayList();
	}


	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		colSymbol.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, String> val) ->
				new ReadOnlyStringWrapper(val.getValue().getValue().getSecurityID())
				);
		
		colDate.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, Date> val) ->
				new ReadOnlyObjectWrapper<Date>(val.getValue().getValue().getOrderDateTime())
				);
		
		colDate.setCellFactory(column -> {
			return new TreeTableCell<OrdersVO, Date>(){
				@Override
				protected void updateItem(Date item, boolean empty){
					super.updateItem(item, empty);
					if(item == null || empty){
						setText(null);
						setStyle("");
					}
					else{
						setText(((DateTimeFormatter)FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F6)).format(item));
					}
				}
			};
		});
		colSide.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, Side> val) ->
				new ReadOnlyObjectWrapper<Side>(val.getValue().getValue().getSide())
				);
		
		colStatus.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, OrderStatus> val) ->
				new ReadOnlyObjectWrapper<OrderStatus>(val.getValue().getValue().getOrderStatus())
				);
		colStatus.setCellFactory(column -> {
			return new TreeTableCell<OrdersVO, OrderStatus>(){
				@Override
				protected void updateItem(OrderStatus status, boolean empty){
					super.updateItem(status, empty);
					if(status != null || !empty){
						setText(status.name());
						
						String style = "";
						switch (status) {
						case CANCELED:					
						case EXPIRED:
							style = "order_canceled";
							break;
						case FILLED:
							style = "order_filled";
							break;
						case PARTIALLY_FILLED:
							style = "order_partially_filled";
							break;
						case CREATED:
						case NEW:
						case REPLACED:
						case SUBMITTED:
							style = "order_new";
							break;
						case REJECTED:
						case SUSPENDED:
							style = "order_rejected";
							break;
						default:
							break;
						}
						
						getTreeTableRow().getStyleClass().add(style);
					}
				}
			};
		});
		
		colVolume.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, Double> val) ->
				new ReadOnlyObjectWrapper<Double>(val.getValue().getValue().getVolume())
				);
		
		colExecVolume.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, Double> val) ->
				new ReadOnlyObjectWrapper<Double>(val.getValue().getValue().getExecutedVolume())
				);
		colExecVolume.setCellFactory(column -> {
			return new TreeTableCell<OrdersVO, Double>(){
				@Override
				protected void updateItem(Double item, boolean empty){
					super.updateItem(item, empty);
					if(item != null || !empty){
						setText(item.toString());
						//getTableColumn().
						//getTableRow().get getStyleClass().add("order_rejected");
					}
				}
			};
		});
		
		
		
		colPrice.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, Double> val) ->
				new ReadOnlyObjectWrapper<Double>(val.getValue().getValue().getPrice())
				);
		
		colStopGain.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, Double> val) ->
				new ReadOnlyObjectWrapper<Double>(val.getValue().getValue().getStopPrice())
				);
		
		colStopLoss.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, Double> val) ->
				new ReadOnlyObjectWrapper<Double>(val.getValue().getValue().getStopPrice())
				);
		
		colAvgPrice.setCellValueFactory(
				(TreeTableColumn.CellDataFeatures<OrdersVO, Double> val) ->
				new ReadOnlyObjectWrapper<Double>(val.getValue().getValue().getAvgPrice())
				);
		
		 
		tblOrders.setRoot(root);
		
		new Thread(new Runnable(){
			@Override
			public void run() {
				for(int i=0;i<4;i++){
					OrdersVO vo = new OrdersVO();
					vo.setSecurityID("WDOV15");
					vo.setOrderDateTime(new Date());
					vo.setSide(Side.BUY);
					vo.setOrderStatus(OrderStatus.NEW);
					vo.setVolume(1);
					vo.setExecutedVolume(0);
					vo.setPrice(12.4);
					vo.setStopPrice(0);
					vo.setAvgPrice(0);
					add(vo);
					try {
						Thread.sleep(2500);
						vo.setOrderStatus(OrderStatus.PARTIALLY_FILLED);
						vo.setExecutedVolume(1+i);
												
						Thread.sleep(2500);
						vo.setOrderStatus(OrderStatus.FILLED);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.out.println();
				}
			}
		}).start();

	}
	
	
	
	
	public void add(OrdersVO ordr) {
		data.add(ordr);
		root.getChildren().add(new TreeItem(ordr));
	}
	
	
	
}
