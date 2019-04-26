/**
 * 
 */
package com.cmm.jft.ui.utils;

import com.cmm.jft.model.security.Security;
import com.cmm.jft.vo.SecurityVO;

import javafx.scene.control.TextField;

/**
 * <p>
 * <code>Memory.java</code>
 * </p>
 * 
 * @author cristiano
 * @version Nov 2, 2015 10:02:23 PM
 *
 */
public class Memory {

    private static Memory instance;

    private Security security;
    private TextField textField;

    /*
     * adicionar hashtable para poder referenciar objetos e poder trabalhar com
     * mais de um mesmo objeto na memoria para multiplas janelas
     */

    /**
     * @return the instance
     */
    public static Memory getInstance() {
	if (instance == null) {
	    instance = new Memory();
	}
	return instance;
    }

    /**
     * @return the security
     */
    public Security getSecurity() {
	return this.security;
    }

    /**
     * @param security
     *            the security to set
     */
    public void setSecurity(Security security) {
	this.security = security;
    }

    /**
     * @return the textField
     */
    public TextField getTextField() {
	return this.textField;
    }

    /**
     * @param textField
     *            the textField to set
     */
    public void setTextField(TextField textField) {
	this.textField = textField;
    }

}
