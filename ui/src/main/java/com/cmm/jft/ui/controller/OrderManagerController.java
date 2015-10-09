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
	private TableColumn<OrdersVO, OrderStatus> colStatus;

	@FXML
	private TableColumn<OrdersVO, Side> colSide;

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

		colSymbol.setCellValueFactory(new PropertyValueFactory<>("securityID"));
		colDate.setCellValueFactory(new PropertyValueFactory<>("orderDateTime"));
		colDate.setCellFactory(column -> {
			return new TableCell<OrdersVO, Date>(){
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


		colSide.setCellValueFactory(new PropertyValueFactory<>("side"));
		colStatus.setCellValueFactory(new PropertyValueFactory<>("orderStatus"));
		colStatus.setCellFactory(column -> {
			return new TableCell<OrdersVO, OrderStatus>(){
								
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
						
//						getStyleClass().add(style);
						getTableRow().getStyleClass().add(style);
					}
				}
			};
		});



		colVolume.setCellValueFactory(new PropertyValueFactory<>("volume"));
		colExecVolume.setCellValueFactory(new PropertyValueFactory<>("executedVolume"));
		colExecVolume.setCellFactory(column -> {
			return new TableCell<OrdersVO, Double>(){
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
		
		

		colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
		colStopGain.setCellValueFactory(new PropertyValueFactory<>("stopPrice"));
		colStopLoss.setCellValueFactory(new PropertyValueFactory<>("stopPrice"));
		colAvgPrice.setCellValueFactory(new PropertyValueFactory<>("avgPrice"));

		tblOrders.setItems(data);
		

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
					data.add(vo);
					try {
						Thread.sleep(2500);
						vo.setOrderStatus(OrderStatus.PARTIALLY_FILLED);
						vo.setExecutedVolume(1+i);
						//tblOrders .getColumns().get(0).setVisible(false);
						//tblOrders.getColumns().get(0).setVisible(true);
						ObservableList<OrdersVO> aux = data;
						data = FXCollections.observableArrayList(aux);
						
						
						Thread.sleep(2500);
						vo.setOrderStatus(OrderStatus.FILLED);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					System.out.println(tblOrders.getItems().size());
				}
			}
		}).start();

	}

}
