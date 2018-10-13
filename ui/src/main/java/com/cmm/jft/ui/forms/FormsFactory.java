package com.cmm.jft.ui.forms;

import com.cmm.jft.ui.forms.ObjectForms;
import com.cmm.jft.ui.forms.trading.DOMForm;
import com.cmm.jft.ui.utils.GenericTableModel;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * <p><code>FormsFactory.java</code></p>
 *
 * @author Cristiano M Martins
 * @version 02/08/2013 01:48:29
 *
 */
public class FormsFactory {

    private static Forms getForm(ObjectForms objForm) {
	Forms frm = null;
	switch (objForm) {
	case DOM:
	    frm = new DOMForm();
	    break;
	case PROGRAM:
	    frm = new ProgramForm();
	    
	}

	return frm;
    }

    public static void openForm(final ObjectForms objForm) {
	openForm(objForm, null, FormStates.OK);
    }

    public static void openForm(final ObjectForms objForm, final FormStates state) {
	openForm(objForm, null, state);
    }

    public static void openForm(final ObjectForms objForm, final Object data) {
	openForm(objForm, data, FormStates.OK);
    }

    public static void openForm(final ObjectForms objForm, final Object data, final FormStates state) {

	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		Forms frm = getForm(objForm);
		frm.setState(state);
		frm.addListeners();
		frm.setData(data);
		frm.loadData();
		((JFrame) frm).setVisible(true);
		((JFrame) frm).setLocationRelativeTo(null);
	    }
	});
    }

    public static void openFormBySelectedObject(final JTable table, final ObjectForms objForm, final FormStates state) {
	int r = table.getSelectedRow();
	if(r>=0) {
	    GenericTableModel mdl = (GenericTableModel) table.getModel();
	    FormsFactory.openForm(objForm, mdl.getValue(r), state);
	}
	else {
	    JOptionPane.showMessageDialog(null, "You must select an record!", "", JOptionPane.INFORMATION_MESSAGE);
	}
    }

}
