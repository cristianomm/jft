/**
 * 
 */
package com.cmm.jft.ui.utils;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

import com.cmm.jft.ui.controller.AbstractController;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * <p>
 * <code>FormUtils.java</code>
 * </p>
 * 
 * @author Cristiano M Martins
 * @version 27/01/2014 01:11:06
 *
 */
public class FormUtils {

    private static FormUtils instance;
    private HashMap<String, Object> parameters;

    /**
     * 
     */
    private FormUtils() {
	this.parameters = new HashMap<String, Object>();
    }

    public static FormUtils getInstance() {
	if (instance == null) {
	    instance = new FormUtils();
	}
	return instance;
    }

    /**
     * Adiciona Itens ao <code>JComboBox</code> passado por parametro.
     * 
     * @param cmb
     *            <code>JComboBox</code>.
     * @param itens
     *            Valores a serem adicionados ao <code>JComboBox</code>.
     */
    public void addItensToCombo(JComboBox cmb, Object[] items) {
	DefaultComboBoxModel boxModel = new DefaultComboBoxModel<>(items);
	cmb.setModel(boxModel);

    }

    public boolean indexInRange(List list, int index) {

	boolean ret = false;
	if (index >= 0 && index < list.size()) {
	    ret = true;
	} else {
	    JOptionPane.showMessageDialog(null, "Index out of bounds", "Index out of bounds",
		    JOptionPane.ERROR_MESSAGE);
	}
	return ret;
    }

    public void clearParameters() {
	parameters.clear();
    }

    public void addParameter(String key, Object value) {
	parameters.put(key, value);
    }

    public Object getParameter(String key) {
	return parameters.get(key);
    }

    public AbstractController openForm(String form) {
	AbstractController controller = null;
	try {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(form));
	    Parent root = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    stage.initModality(Modality.NONE);
	    stage.initStyle(StageStyle.DECORATED);
	    stage.setResizable(false);
	    stage.setScene(new Scene(root));
	    stage.getScene().getStylesheets()
		    .add("file://" + getClass().getResource("../../../../../forms/forms.css").getFile());

	    controller = fxmlLoader.getController();
	    stage.setTitle(controller.getTitle());

	    stage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
	return controller;
    }

    public AbstractController openForm(String form, String cssLocation) {
	AbstractController controller = null;
	try {
	    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(form));
	    Parent root = (Parent) fxmlLoader.load();
	    Stage stage = new Stage();
	    stage.initModality(Modality.NONE);
	    stage.initStyle(StageStyle.DECORATED);
	    stage.setResizable(false);
	    stage.setScene(new Scene(root));
	    stage.getScene().getStylesheets().add(cssLocation);

	    controller = fxmlLoader.getController();
	    stage.setTitle(controller.getTitle());
	    stage.show();

	} catch (IOException e) {
	    e.printStackTrace();
	}
	return controller;
    }

}
