/**
 * 
 */
package com.cmm.jft.ui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;

import com.zoicapital.stockchartsfx.BarData;
import com.zoicapital.stockchartsfx.CandleStickChart;
import com.zoicapital.stockchartsfx.DecimalAxisFormatter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

/**
 * <p><code>ChartController.java</code></p>
 * @author Cristiano M Martins
 * @version Nov 4, 2016 10:24:58 AM
 *
 */
public class ChartController extends AbstractController {
	
	@FXML
	private Label lblPrice;
	
	@FXML
	private Label lblCoords;
	
//	@FXML
//	private Label lbl;
//	
//	@FXML
//	private Label lbl;
	
	@FXML
	private final LineChart<Number, Number> chart;
	
	
	private final CandleStickChart chart2;
	
	private ObservableList<XYChart.Series<Number, Number>> data;
	
	private final NumberAxis xAxis;
	private final NumberAxis yAxis;
	private XYChart.Series serie;
	
	/**
	 * 
	 */
	public ChartController() {
		this.xAxis = new NumberAxis();
		this.yAxis = new NumberAxis();
		this.data = FXCollections.observableArrayList();
		this.chart = new LineChart<Number, Number>(xAxis, yAxis);
		
		chart2 = new  CandleStickChart("test", buildBars());
		chart2.setYAxisFormatter(new DecimalAxisFormatter("#000.00"));
		
		AtomicLong lng = new AtomicLong(1);
		//new Random().doubles(100).forEach(rd -> 
			//data.add(new XYChart.Data<Long, Double>(lng.getAndIncrement(), rd))
		//);
		
		this.serie = new XYChart.Series<Number, Number>();
		
		serie.setName("data");
		
		serie.getData().add(new XYChart.Data<Number, Number>(1, 23));
        serie.getData().add(new XYChart.Data<Number, Number>(2, 14));
        serie.getData().add(new XYChart.Data<Number, Number>(3, 15));
        serie.getData().add(new XYChart.Data<Number, Number>(4, 24));
        serie.getData().add(new XYChart.Data<Number, Number>(5, 34));
        serie.getData().add(new XYChart.Data<Number, Number>(6, 36));
        serie.getData().add(new XYChart.Data<Number, Number>(7, 22));
        serie.getData().add(new XYChart.Data<Number, Number>(8, 45));
        serie.getData().add(new XYChart.Data<Number, Number>(9, 43));
        serie.getData().add(new XYChart.Data<Number, Number>(10, 17));
        serie.getData().add(new XYChart.Data<Number, Number>(11, 29));
        serie.getData().add(new XYChart.Data<Number, Number>(12, 25));
        
        data.add(serie);
        //chart.setData(data);
        
	}
	
	public List<BarData> buildBars() {
        double previousClose = 1850;
        
        final List<BarData> bars = new ArrayList<>();
        GregorianCalendar now = new GregorianCalendar();
        for (int i = 0; i < 26; i++) {
            double open = getNewValue(previousClose);
            double close = getNewValue(open);
            double high = Math.max(open + getRandom(),close);
            double low = Math.min(open - getRandom(),close);
            previousClose = close;
            
            BarData bar = new BarData((GregorianCalendar) now.clone(), open, high, low, close, 1);
            now.add(Calendar.MINUTE, 5);
            bars.add(bar);
        }
        return bars;
    }
    
    
    protected double getNewValue( double previousValue ) {
        int sign;
        
        if( Math.random() < 0.5 ) {
            sign = -1;
        } else {
            sign = 1;
        }
        return getRandom() * sign + previousValue;
    }
    
    protected double getRandom() {
        double newValue = 0;
        newValue = Math.random() * 10;
        return newValue;
    }
	
	
	/* (non-Javadoc)
	 * @see javafx.fxml.Initializable#initialize(java.net.URL, java.util.ResourceBundle)
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	    final Node chartBackground = chart.lookup(".chart-plot-background");
	    for (Node n: chartBackground.getParent().getChildrenUnmodifiable()) {
	      if (n != chartBackground && n != xAxis && n != yAxis) {
	        n.setMouseTransparent(true);
	      }
	    }

	    chartBackground.setOnMouseEntered(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	        lblCoords.setVisible(true);
	      }
	    });

	    chartBackground.setOnMouseMoved(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  lblCoords.setText(
	          String.format(
	            "(%.2f, %.2f)",
	            xAxis.getValueForDisplay(mouseEvent.getX()),
	            yAxis.getValueForDisplay(mouseEvent.getY())
	          )
	        );
	      }
	    });

	    chartBackground.setOnMouseExited(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  lblCoords.setVisible(false);
	      }
	    });

	    xAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  lblCoords.setVisible(true);
	      }
	    });

	    xAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  lblCoords.setText(
	          String.format(
	            "x = %.2f",
	            xAxis.getValueForDisplay(mouseEvent.getX())
	          )
	        );
	      }
	    });

	    xAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  lblCoords.setVisible(false);
	      }
	    });

	    yAxis.setOnMouseEntered(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  lblCoords.setVisible(true);
	      }
	    });

	    yAxis.setOnMouseMoved(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  lblCoords.setText(
	          String.format(
	            "y = %.2f",
	            yAxis.getValueForDisplay(mouseEvent.getY())
	          )
	        );
	      }
	    });

	    yAxis.setOnMouseExited(new EventHandler<MouseEvent>() {
	      @Override public void handle(MouseEvent mouseEvent) {
	    	  lblCoords.setVisible(false);
	      }
	    });
		
	}

	/* (non-Javadoc)
	* @see com.cmm.jft.ui.controller.AbstractController#getTitle()
	*/
	@Override
	public String getTitle() {
	return "Chart";
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.ui.controller.AbstractController#addData(java.lang.Object)
	 */
	@Override
	public void addData(Object data) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.cmm.jft.ui.controller.AbstractController#updateData(java.lang.Object)
	 */
	@Override
	public void updateData(Object data) {
		// TODO Auto-generated method stub

	}

}
