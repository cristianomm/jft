package com.cmm.jft.ui.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.SwingConstants;

import javafx.util.converter.NumberStringConverter;

/**
 * 
 * <p>
 * <code>NumberRenderer.java</code>
 * </p>
 *
 * @author  Rob Camick 
 * @version 11/10/2008 
 *
 */
public class NumberRenderer extends FormatRenderer {
    /*
     * Use the specified number formatter and right align the text
     */
    public NumberRenderer(NumberFormat formatter) {
	super(formatter);
	setHorizontalAlignment(SwingConstants.RIGHT);
    }

    /*
     * Use the default currency formatter for the default locale
     */
    public static NumberRenderer getCurrencyRenderer() {
	return new NumberRenderer(NumberFormat.getCurrencyInstance());
    }

    /*
     * Use the default integer formatter for the default locale
     */
    public static NumberRenderer getIntegerRenderer() {
	return new NumberRenderer(NumberFormat.getIntegerInstance());
    }

    /*
     * Use the default percent formatter for the default locale
     */
    public static NumberRenderer getPercentRenderer() {
	return new NumberRenderer(NumberFormat.getPercentInstance());
    }
    
    public static NumberRenderer getDouble1DRenderer() {
	return new NumberRenderer(new DecimalFormat("#.0"));
    }
    
    public static NumberRenderer getDouble2DRenderer() {
	return new NumberRenderer(new DecimalFormat("#.00"));
    }
    
    public static NumberRenderer getDouble3DRenderer() {
	return new NumberRenderer(new DecimalFormat("#.000"));
    }
    
    public static NumberRenderer getDouble4DRenderer() {
	return new NumberRenderer(new DecimalFormat("#.0000"));
    }
    
    
}