/**
 * 
 */
package com.cmm.jft.engine.ui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;

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

    /*
     * (non-Javadoc)
     * 
     * @see javafx.fxml.Initializable#initialize(java.net.URL,
     * java.util.ResourceBundle)
     */
    public void initialize(URL location, ResourceBundle resources) {

	tgbtnStartEngine.setOnAction(event -> {

	});
	tgbtnStopEngine.setOnAction(event -> {
	});

	tgbtnStartMD.setOnAction(event -> {
	});
	tgbtnStopMD.setOnAction(event -> {
	});

	tgbtnStartInstrument.setOnAction(event -> {
	});
	tgbtnStopInstrument.setOnAction(event -> {
	});

	tgbtnStartNews.setOnAction(event -> {
	});
	tgbtnStopNews.setOnAction(event -> {
	});

    }

}
