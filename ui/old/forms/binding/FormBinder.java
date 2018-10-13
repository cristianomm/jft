/**
 * 
 */
package com.cmm.jft.ui.forms.binding;

import com.cmm.jft.core.format.Formatter;
import com.cmm.logging.Logging;
import java.awt.Component;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;

import org.apache.log4j.Level;

import com.cmm.searchbox.SearchBox;
import com.toedter.calendar.JDateChooser;

/**
 * <p><code>FormBinder.java</code></p>
 * @author Cristiano M Martins
 * @version 28/01/2014 23:31:44
 *
 */
public class FormBinder implements Binder {

    private Map<String, Component> registers;
    private Map<String, Formatter<Object>> formatters;

    /**
     * 
     */
    public FormBinder() {
	this.registers = new HashMap<String, Component>();
	this.formatters = new HashMap<String, Formatter<Object>>();
    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.forms.binding.Binder#updateView(java.lang.Object)
     */
    @Override
    public void updateView(Object object) {

	for(String property:registers.keySet()) {
	    try {
		setComponentValue(property, object);
	    } catch (IllegalArgumentException e) {
		Logging.getInstance().log(FormBinder.class, new Throwable("Property: " + property, e), Level.ERROR);
	    } catch (IllegalAccessException e) {
		Logging.getInstance().log(FormBinder.class, new Throwable("Property: " + property, e), Level.ERROR);
	    } catch (NoSuchFieldException e) {
		e.printStackTrace();
		Logging.getInstance().log(FormBinder.class, new Throwable("Property: " + property, e), Level.ERROR);
	    } catch (SecurityException e) {
		e.printStackTrace();
		Logging.getInstance().log(FormBinder.class, new Throwable("Property: " + property, e), Level.ERROR);
	    } catch(ClassCastException e) {
		e.printStackTrace();
		Logging.getInstance().log(FormBinder.class, new Throwable("Property: " + property, e), Level.ERROR);
	    }
	}

    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.forms.binding.Binder#updateModel(java.lang.Object)
     */
    @Override
    public void updateModel(Object object) {
	
	for(String property:registers.keySet()) {
	    try {
		setModelValue(property, object);
	    } catch (IllegalArgumentException e) {
		Logging.getInstance().log(FormBinder.class, e, Level.ERROR);
	    } catch (IllegalAccessException e) {
		Logging.getInstance().log(FormBinder.class, e, Level.ERROR);
	    } catch (NoSuchFieldException e) {
		e.printStackTrace();
		Logging.getInstance().log(FormBinder.class, e, Level.ERROR);
	    } catch (SecurityException e) {
		e.printStackTrace();
		Logging.getInstance().log(FormBinder.class, e, Level.ERROR);
	    } 
	}
	
    }

    private Field getField(String fieldName, Object object) throws NoSuchFieldException, SecurityException {
	
	Field ret = null;
	String[] fields = fieldName.split("\\.");
	Field f = null;
	Class temp = object.getClass();
	int i=0;
	while(i<fields.length) {
	    f = temp.getDeclaredField(fields[i]);
	    temp = f.getType();
	    i++;
	}
	
	if(f!= null && fields[fields.length-1].equalsIgnoreCase(f.getName())) {
	    ret = f;
	}
	
	return ret;
    }

    private void setComponentValue(String fieldName, Object object) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException, ClassCastException{
	
	Field field = getField(fieldName, object);
	Component component = registers.get(fieldName);
	Object objValue = getFieldObject(fieldName, object);
	
	field.setAccessible(true);
	Object value = (objValue!=null?field.get(objValue):"");
	value = (value!=null?value:"");
	if(component instanceof JDateChooser) {
	    if(value!="")((JDateChooser)component).setDate((Date) value);
	}
	else if(component instanceof JTextComponent) {
	    ((JTextComponent)component).setText(value.toString());
	}
	else if (component instanceof JComboBox) {
	    ((JComboBox)component).setSelectedItem(value);
	}
	else if(component instanceof SearchBox) {
	    //((SearchBox)component).setf Value(value);
	    ((SearchBox)component).setResultFieldValue((String) value);
	}
	else if(component instanceof JLabel) {
	    ((JLabel)component).setText(value.toString());
	}

    }
    
    private Object getComponentValue(Component component) {
	Object value = null;
	
	if(component instanceof JDateChooser) {
	    value = ((JDateChooser)component).getDate();
	}
	else if(component instanceof JTextComponent) {
	    value = ((JTextComponent)component).getText();
	}
	else if (component instanceof JComboBox) {
	    value = ((JComboBox)component).getSelectedItem();
	}
	else if(component instanceof SearchBox) {
	    value = ((SearchBox)component).getResultFieldValue();
	}
	else if(component instanceof JLabel) {
	    value = ((JLabel)component).getText();
	}
	
	
	return value;
    }
    
    private Object getFieldObject(String fieldName, final Object object) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
	Object ret = object;
	
	String[] fields = fieldName.split("\\.");
	Field f = null;
	Class temp = object.getClass();
	int i=0;
	while(i<fields.length-1) {
	    f = temp.getDeclaredField(fields[i]);
	    f.setAccessible(true);
	    temp = f.getType();
	    ret = f.get(ret);
	    if(ret == null) {
		i=fields.length;
	    }
	    i++;
	}
	return ret;
    }
    
    
    private void setModelValue(String fieldName, Object object) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException {
	
	Field field = getField(fieldName, object);
	Component component = registers.get(fieldName);
	
	field.setAccessible(true);
	Object value = getComponentValue(component);
	if(formatters.containsKey(fieldName)) {
	    value = formatters.get(fieldName).parse(value.toString());
	}
	
	field.set(object, value);
	
	
    }
    

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.forms.binding.Binder#register(java.lang.String, java.awt.Component)
     */
    @Override
    public void register(String property, Component component) {
	registers.put(property, component);
    }

    /* (non-Javadoc)
     * @see com.cmm.jft_ui.forms.binding.Binder#addFormatter(java.lang.String, com.cmm.jft_core.format.Formatter)
     */
    @Override
    public void addFormatter(String property, Formatter formatter) {
	formatters.put(property, formatter);
    }

}
