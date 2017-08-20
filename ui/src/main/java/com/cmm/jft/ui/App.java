package com.cmm.jft.ui;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.services.security.SecurityService;
import com.cmm.jft.services.trading.OrderService;
import com.cmm.jft.ui.utils.ImageIcons;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
	javafx.application.Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
	try {
	    Pane g = FXMLLoader.load(App.class.getResource("../../../../forms/ProgramForm.fxml"));

	    Scene scene = new Scene(g);
	    scene.getStylesheets().add("file://" + App.class.getResource("../../../../forms/forms.css").getFile());
	    stage.setMaxWidth(g.getPrefWidth() + 15);
	    stage.setScene(scene);
	    stage.getIcons().add(ImageIcons.getProgramImage().getImage());
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
	    System.out.println("Initializing Database");
	    DBFacade.getInstance();

	    System.out.println("Initializing Securities Service");
	    SecurityService.getInstance();

	    System.out.println("Initializing Orders Service");
	    OrderService.getInstance();

	    System.out.println("Initializing Trading Service");
	    // TradingService.getInstance();
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
	try {
	    DBFacade.getInstance().closeSession();
	} catch (DataBaseException e) {
	    e.printStackTrace();
	}
	super.stop();
    }

}
