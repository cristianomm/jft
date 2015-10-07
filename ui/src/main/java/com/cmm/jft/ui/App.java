package com.cmm.jft.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
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
			///ui/src/main/resources/forms/DOMForm.fxml
			///ui/src/main/java/com/cmm/jft/ui/App.java
			Pane g = FXMLLoader.load(App.class.getResource("../../../../forms/OrderManagerForm.fxml"));
			
			Scene scene = new Scene(g);
			stage.setMaxWidth(g.getPrefWidth()+15);
			stage.setScene(scene);
			stage.show();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
