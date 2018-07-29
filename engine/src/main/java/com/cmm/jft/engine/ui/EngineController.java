/**
 * 
 */
package com.cmm.jft.engine.ui;

import java.net.URL;
import java.util.ResourceBundle;

import com.cmm.jft.core.services.Service;
import com.cmm.jft.engine.entrypoint.EntryPointService;
import com.cmm.jft.engine.marketdata.incrementals.MarketDataService;
import com.cmm.jft.engine.marketdata.instrument.InstrumentDefinitionService;
import com.cmm.jft.engine.marketdata.news.NewsService;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;

/**
 * <p>
 * <code>EngineController.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 22/02/2017 11:26:45
 *
 */
public class EngineController implements Initializable {

    @FXML // fx:id="tgbtnStartEngine"
    private ToggleButton tgbtnStartEngine; // Value injected by FXMLLoader

    @FXML // fx:id="lblMsgCountEngine"
    private Label lblMsgCountEngine; // Value injected by FXMLLoader

    @FXML // fx:id="lblConnectionsEngine"
    private Label lblConnectionsEngine; // Value injected by FXMLLoader

    @FXML // fx:id="tgbtnStopEngine"
    private ToggleButton tgbtnStopEngine; // Value injected by FXMLLoader

    @FXML // fx:id="tgbtnStartMD"
    private ToggleButton tgbtnStartMD; // Value injected by FXMLLoader

    @FXML // fx:id="lblMsgCountMD"
    private Label lblMsgCountMD; // Value injected by FXMLLoader

    @FXML // fx:id="lblConnectionsMD"
    private Label lblConnectionsMD; // Value injected by FXMLLoader

    @FXML // fx:id="tgbtnStopMD"
    private ToggleButton tgbtnStopMD; // Value injected by FXMLLoader

    @FXML // fx:id="tgbtnStartInstrument"
    private ToggleButton tgbtnStartInstrument; // Value injected by FXMLLoader

    @FXML // fx:id="lblMsgCountInstrument"
    private Label lblMsgCountInstrument; // Value injected by FXMLLoader

    @FXML // fx:id="lblConnectionsInstrument"
    private Label lblConnectionsInstrument; // Value injected by FXMLLoader

    @FXML // fx:id="tgbtnStopInstrument"
    private ToggleButton tgbtnStopInstrument; // Value injected by FXMLLoader

    @FXML // fx:id="tgbtnStartNews"
    private ToggleButton tgbtnStartNews; // Value injected by FXMLLoader

    @FXML // fx:id="lblMsgCountNews"
    private Label lblMsgCountNews; // Value injected by FXMLLoader

    @FXML // fx:id="lblConnectionsNews"
    private Label lblConnectionsNews; // Value injected by FXMLLoader

    @FXML // fx:id="tgbtnStopNews"
    private ToggleButton tgbtnStopNews; // Value injected by FXMLLoader

    private ToggleGroup entryGroup;
    private ToggleGroup mdGroup;
    private ToggleGroup instrumentGroup;
    private ToggleGroup newsGroup;
    
    
    private EntryPointService entryService;
    private MarketDataService mdService;
    private InstrumentDefinitionService instrumentService;
    private NewsService newsService;
    
    
    
    

    /*
     * (non-Javadoc)
     * 
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    public void initialize(URL location, ResourceBundle resources) {

	entryService = new EntryPointService();
	mdService = new MarketDataService();
	instrumentService = new InstrumentDefinitionService();
	newsService = new NewsService();

	
	entryGroup = new ToggleGroup();
	mdGroup = new ToggleGroup();
	instrumentGroup = new ToggleGroup();
	newsGroup = new ToggleGroup();
	
	tgbtnStartEngine.setToggleGroup(entryGroup);
	tgbtnStopEngine.setToggleGroup(entryGroup);
	
	tgbtnStartMD.setToggleGroup(mdGroup);
	tgbtnStopMD.setToggleGroup(mdGroup);
	
	tgbtnStartInstrument.setToggleGroup(instrumentGroup);
	tgbtnStopInstrument.setToggleGroup(instrumentGroup);
	
	tgbtnStartNews.setToggleGroup(newsGroup);
	tgbtnStopNews.setToggleGroup(newsGroup);
	
	
	tgbtnStartEngine.setOnAction(event -> {
	    startService(entryService);
	});
	tgbtnStopEngine.setOnAction(event -> {
	    stopService(entryService);
	});

	tgbtnStartMD.setOnAction(event -> {
	    startService(mdService);
	});
	tgbtnStopMD.setOnAction(event -> {
	    stopService(mdService);
	});

	tgbtnStartInstrument.setOnAction(event -> {
	    startService(instrumentService);
	});
	tgbtnStopInstrument.setOnAction(event -> {
	    stopService(instrumentService);
	});

	tgbtnStartNews.setOnAction(event -> {
	    startService(newsService);
	});
	tgbtnStopNews.setOnAction(event -> {
	    stopService(newsService);
	});

    }

    private void startService(Service service) {
	if (!service.isStarted()) {
	    service.start();
	}
    }

    private void stopService(Service service) {
	if (service.isStarted()) {
	    service.stop();
	}
    }

}
