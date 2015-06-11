package com.cmm.jft.ui.forms;

import com.cmm.jft.db.DBFacade;
import com.cmm.jft.db.DBObject;
import com.cmm.jft.db.exceptions.DataBaseException;
import com.cmm.jft.ui.forms.binding.Binder;

import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.cmm.logging.Logging;

import org.apache.log4j.Level;

/**
 * <p><code>AbstractForm.java</code></p>
 * @author Cristiano Martins
 * @version 21/01/2014 17:46:37
 *
 */
public abstract class AbstractForm extends JFrame implements Forms {

    protected DBObject data;
    protected Binder binder;
    protected JButton btnOK;
    protected JButton btnCancel;
    protected FormStates state;

    private class GerEvents extends Events{

	/**
	 * @param frame
	 */
	public GerEvents(AbstractForm frame) {
	    super(frame);
	}
	
	/* (non-Javadoc)
	 * @see com.cmm.jft_ui.forms.Events#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
	    if(e.getSource()==btnCancel) {
		close();
	    }
	    else if(e.getSource()==btnOK) {
		switch(state) {
		case ADD:
		    save();
		    break;
		case CANCELED:
		    close();
		    break;
		case CLOSED:
		    close();
		    break;
		case OK:
		    close();
		    break;
		case OPEN:
		    close();
		    break;
		case UPDATE:
		    update();
		    break;
		}
	    }
	}
	
    }
    
    
    
    /* (non-Javadoc)
     * @see com.cmm.jft_ui.Forms#setData(java.lang.Object)
     */
    @Override
    public void setData(Object data) {
	this.data = (DBObject) data;
    }
    
    /* (non-Javadoc)
     * @see com.cmm.jft_ui.forms.Forms#parseData()
     */
    @Override
    public void parseData() {
	binder.updateModel(data);
    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.Forms#addListeners()
     */
    @Override
    public void addListeners() {
	btnCancel.addActionListener(new GerEvents(this));
	btnOK.addActionListener(new GerEvents(this));
    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.Forms#loadData()
     */
    @Override
    public void loadData() {
	if(binder!=null){
	    binder.updateView(data);
	}
    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.Forms#save()
     */
    @Override
    public void save() {
	try {
	    parseData();
	    data = ((DBObject) DBFacade.getInstance()._persist(data));
	    setState(FormStates.OK);
	} catch (DataBaseException e) {
	    Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	}

    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.forms.Forms#update()
     */
    @Override
    public void update() {
	try {
	    parseData();
	    data = ((DBObject) DBFacade.getInstance()._update(data));
	    setState(FormStates.OK);
	} catch (DataBaseException e) {
	    Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	}
    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.Forms#remove()
     */
    @Override
    public void remove() {
	try {
	    data = ((DBObject) DBFacade.getInstance()._remove(data));
	} catch (DataBaseException e) {
	    Logging.getInstance().log(this.getClass(), e, Level.ERROR);
	}
    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.Forms#close()
     */
    @Override
    public void close() {
	this.dispose();
    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.forms.Forms#setState(com.cmm.jft_ui.forms.FormStates)
     */
    @Override
    public void setState(FormStates state) {
	//ok->close
	//add->ok->close
	//update->ok->close
	this.state = state;


	if(btnOK!=null && btnCancel !=null) {
	    switch(state) {
	    case ADD:
		btnOK.setText("Add");
		break;
	    case CANCELED:
		btnOK.setText("OK");
		break;
	    case CLOSED:
		btnOK.setText("OK");
		break;
	    case OK:
		btnOK.setText("OK");
		break;
	    case OPEN:
		btnOK.setText("OK");
		break;
	    case UPDATE:
		btnOK.setText("Update");
		break;
	    }
	}

    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.Forms#setPosition(int, int)
     */
    @Override
    public void setPosition(int x, int y) {
	this.setLocation(x, y);
    }

}
