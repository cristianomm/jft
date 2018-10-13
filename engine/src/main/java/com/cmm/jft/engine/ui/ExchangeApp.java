/**
 * 
 */
package com.cmm.jft.engine.ui;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.services.trading.OrderService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * <p>
 * <code>ExchangeApp.java</code>
 * </p>
 *
 * @author Cristiano M Martins
 * @version 22/02/2017 11:23:22
 *
 */
public class ExchangeApp extends Application {

	public static void main(String[] args) {
		javafx.application.Application.launch(args);
	}

	@Override
	public void start(Stage stage) {
		try {
			Pane g = FXMLLoader.load(ExchangeApp.class.getResource("../../../../../forms/EngineForm.fxml"));

			Scene scene = new Scene(g);
			scene.getStylesheets()
					.add("file://" + ExchangeApp.class.getResource("../../../../../forms/forms.css").getFile());
			stage.setMaxWidth(g.getPrefWidth() + 15);
			stage.setScene(scene);
			// stage.getIcons().add(ImageIcons.getProgramImage().getImage());
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#init()
	 */
	@Override
	public void init() throws Exception {
		// start database connection & services
		try {

		} catch (Exception e) {

		}
		super.init();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#stop()
	 */
	@Override
	public void stop() throws Exception {
//	try {
//	    DBFacade.getInstance().closeSession();
//	} catch (DataBaseException e) {
//	    e.printStackTrace();
//	}
		super.stop();
	}

}
