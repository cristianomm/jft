package com.cmm.jft.ui.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.text.DateFormat;
import javax.swing.table.DefaultTableCellRenderer;

import com.cmm.jft.core.format.Formatter;
import com.cmm.jft.core.format.FormatterFactory;
import com.cmm.jft.core.format.FormatterTypes;

/**
 * 
 * <p>
 * <code>FormatRenderer.java</code>
 * </p>
 *
 * @author  Rob Camick 
 * @version 11/10/2008 
 *
 */
/*
 *	Use a formatter to format the cell Object
 */
public class FormatRenderer extends DefaultTableCellRenderer {
    private Format formatter;

    /*
     * Use the specified formatter to format the Object
     */
    public FormatRenderer(Format formatter) {
	this.formatter = formatter;
    }

    public void setValue(Object value) {
	// Format the Object before setting its value in the renderer

	try {
	    if (value != null)
		value = formatter.format(value);
	} catch (IllegalArgumentException e) {
	}

	super.setValue(value);
    }

    /*
     * Use the default date/time formatter for the default locale
     */
    public static FormatRenderer getDateTimeRenderer() {
	return new FormatRenderer(new SimpleDateFormat(FormatterTypes.DATE_TIME_F5.getFormat()));
    }

    /*
     * Use the default time formatter for the default locale
     */
    public static FormatRenderer getTimeRenderer() {
	return new FormatRenderer(new SimpleDateFormat(FormatterTypes.TIME_F4.getFormat()));
    }
}