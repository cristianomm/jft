/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

import com.cmm.jft.core.format.DateTimeFormatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;
import com.cmm.jft.services.trading.TradingService;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.Position;
import com.cmm.jft.trading.enums.OrderStatus;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.Side;

/**
 * <p>
 * <code>OrderManagerController.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version Oct 6, 2015 9:39:07 PM
 *
 */
public class OrderManagerController extends AbstractController {

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
    private TableColumn<Orders, String> colSymbol;

    @FXML
    private TableColumn<Orders, String> colClOrdID;

    @FXML
    private TableColumn<Orders, String> colOrderID;

    @FXML
    private TableColumn<Orders, OrderTypes> colOrderType;

    @FXML
    private TableColumn<Orders, OrderStatus> colStatus;

    @FXML
    private TableColumn<Orders, Side> colSide;

    @FXML
    private TableColumn<Orders, Date> colDate;

    @FXML
    private TableColumn<Orders, Double> colVolume;

    @FXML
    private TableColumn<Orders, Integer> colExecVolume;

    @FXML
    private TableColumn<Orders, Double> colPrice;

    @FXML
    private TableColumn<Orders, Double> colAvgPrice;

    @FXML
    private TableColumn<Orders, Double> colStopLoss;

    @FXML
    private TableColumn<Orders, Double> colStopGain;

    @FXML
    private TableView<Orders> tblOrders;

    private ObservableList<Orders> data;

    private TradingService tradingService;

    private Orders selected;

    public OrderManagerController() {
	tradingService = TradingService.getInstance();
	data = TradingService.getInstance().getOrdersData();
    }

    /*
     * (non-Javadoc)
     * 
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

	btnCancelAll.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		for (Position p : tradingService.getPositions().values()) {
		    if (p.getPosition() != 0) {
			tradingService.closePosition(p);
		    }
		}
	    }
	});

	btnCancelOrdr.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {
		if (selected != null) {
		    tradingService.cancelOrder(selected.getClOrdID());
		}
	    }
	});

	btnModifyOrdr.setOnAction(new EventHandler<ActionEvent>() {
	    @Override
	    public void handle(ActionEvent event) {

	    }
	});

	bindLabels();

	colSymbol.setCellValueFactory((TableColumn.CellDataFeatures<Orders, String> val) -> new ReadOnlyStringWrapper(
		val.getValue().getSecurityID().getSymbol()));

	colClOrdID.setCellValueFactory((TableColumn.CellDataFeatures<Orders, String> val) -> new ReadOnlyStringWrapper(
		val.getValue().getClOrdID()));

	colOrderID.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, String> val) -> new ReadOnlyStringWrapper(
			val.getValue().getOrderID().toString()));

	colOrderType.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, OrderTypes> val) -> new ReadOnlyObjectWrapper<OrderTypes>(
			val.getValue().getOrderType()));

	colDate.setCellValueFactory((TableColumn.CellDataFeatures<Orders, Date> val) -> new ReadOnlyObjectWrapper<Date>(
		val.getValue().getOrderDateTime()));

	colDate.setCellFactory(column -> {
	    return new TableCell<Orders, Date>() {
		@Override
		protected void updateItem(Date item, boolean empty) {
		    super.updateItem(item, empty);
		    if (item == null || empty) {
			setText(null);
			setStyle("");
		    } else {
			setText(((DateTimeFormatter) FormatterFactory.getFormatter(FormatterTypes.DATE_TIME_F6))
				.format(item));
		    }
		}
	    };
	});

	colSide.setCellValueFactory((TableColumn.CellDataFeatures<Orders, Side> val) -> new ReadOnlyObjectWrapper<Side>(
		val.getValue().getSide()));

	colStatus.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, OrderStatus> val) -> new ReadOnlyObjectWrapper<OrderStatus>(
			val.getValue().getOrderStatus()));

	colStatus.setCellFactory(column -> {
	    return new TableCell<Orders, OrderStatus>() {
		@Override
		protected void updateItem(OrderStatus status, boolean empty) {
		    super.updateItem(status, empty);
		    if (status != null) {
			setText(status.name());

			String style = "";
			switch (status) {
			case CANCELED:
			    style = "order_canceled";
			    break;
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
			    style = "order_new";
			    break;
			case NEW:
			    style = "order_new";
			    break;
			case REPLACED:
			    style = "order_new";
			    break;
			case SUBMITTED:
			    style = "order_new";
			    break;
			case REJECTED:
			    style = "order_rejected";
			    break;
			case SUSPENDED:
			    style = "order_rejected";
			    break;
			default:
			    style = "order_new";
			    break;
			}

			getTableRow().getStyleClass().add(style);
		    }
		}
	    };
	});

	colVolume.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, Double> val) -> new ReadOnlyObjectWrapper<Double>(
			val.getValue().getVolume()));

	colExecVolume.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, Integer> val) -> new ReadOnlyObjectWrapper<Integer>(
			val.getValue().getExecutedVolume()));

	colExecVolume.setCellFactory(column -> {
	    return new TableCell<Orders, Integer>() {
		@Override
		protected void updateItem(Integer item, boolean empty) {
		    if (item != null && !empty) {
			super.updateItem(item, empty);
			setText(item.toString());
		    }
		}
	    };
	});

	colPrice.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, Double> val) -> new ReadOnlyObjectWrapper<Double>(
			val.getValue().getPrice()));

	colStopGain.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, Double> val) -> new ReadOnlyObjectWrapper<Double>(
			val.getValue().getStopPrice()));

	colStopLoss.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, Double> val) -> new ReadOnlyObjectWrapper<Double>(
			val.getValue().getStopPrice()));

	colAvgPrice.setCellValueFactory(
		(TableColumn.CellDataFeatures<Orders, Double> val) -> new ReadOnlyObjectWrapper<Double>(
			val.getValue().getAvgPrice()));

	tblOrders.setRowFactory(call -> {
	    TableRow<Orders> row = new TableRow<>();
	    row.setOnMouseClicked(event -> {
		if (row != null && row.isEmpty()) {
		    selected = row.getItem();
		}
	    });
	    return row;
	});

	tblOrders.setItems(data);

	/*
	 * new Thread(new Runnable(){
	 * 
	 * @Override public void run() { try{ Thread.sleep(5000);
	 * }catch(InterruptedException e){
	 * 
	 * }
	 * 
	 * for(int i=0;i<4;i++){ OrdersVO vo = new OrdersVO();
	 * vo.setClOrdID(""+i); vo.setSecurityID("WDOV15");
	 * vo.setOrderDateTime(new Date()); vo.setSide(Side.BUY);
	 * vo.setOrderStatus(OrderStatus.NEW); vo.setVolume(1);
	 * vo.setExecutedVolume(0); vo.setPrice(12.4); vo.setStopPrice(0);
	 * vo.setAvgPrice(0); addData(vo); try { Thread.sleep(2500);
	 * vo.setOrderStatus(OrderStatus.PARTIALLY_FILLED);
	 * vo.setExecutedVolume(1+i); updateData(vo); Thread.sleep(2500);
	 * vo.setOrderStatus(OrderStatus.CANCELED); vo.setSecurityID("WDOV15");
	 * updateData(vo); Thread.sleep(2500); } catch (InterruptedException e)
	 * { e.printStackTrace(); } } } }).start();
	 */
    }

    private void bindLabels() {

	Task<Void> tskOrdCnt = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
		while (true) {
		    Thread.sleep(100);
		    updateMessage(tradingService.getOrderCount() + "");
		}
	    }
	};

	Task<Void> tskPositions = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
		while (true) {
		    Thread.sleep(100);
		    updateMessage(tradingService.getPosition() + "");
		}
	    }
	};

	Task<Void> tskPosValue = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
		while (true) {
		    Thread.sleep(100);
		    updateMessage(tradingService.getPositionValue() + "");
		}
	    }
	};

	Task<Void> tskProfit = new Task<Void>() {
	    @Override
	    protected Void call() throws Exception {
		while (true) {
		    Thread.sleep(100);
		    updateMessage(tradingService.getProfit() + "");
		}
	    }
	};

	Thread t1 = new Thread(tskPosValue);
	t1.setDaemon(true);
	t1.start();

	Thread t2 = new Thread(tskOrdCnt);
	t2.setDaemon(true);
	t2.start();

	Thread t3 = new Thread(tskPositions);
	t3.setDaemon(true);
	t3.start();

	Thread t4 = new Thread(tskProfit);
	t4.setDaemon(true);
	t4.start();

	lblOpenValue.textProperty().bind(tskPosValue.messageProperty());
	lblOrdrCount.textProperty().bind(tskOrdCnt.messageProperty());
	lblPosition.textProperty().bind(tskPositions.messageProperty());
	lblProfit.textProperty().bind(tskProfit.messageProperty());

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.cmm.jft.ui.controller.AbstractController#getTitle()
     */
    @Override
    public String getTitle() {
	return "Order Manager";
    }

    @Override
    public void addData(Object ordr) {
	Platform.runLater(() -> {
	    if (ordr != null && ordr instanceof Orders) {
		this.data.add(((Orders) ordr));
	    }
	});
    }

    @Override
    public void updateData(Object ordr) {
	Platform.runLater(() -> {
	    if (ordr != null && ordr instanceof Orders) {
		int idx = data.indexOf(ordr);
		if (idx >= 0) {
		    // data.remove(idx);
		    data.set(idx, (Orders) ordr);
		}
	    }
	});
    }

}
