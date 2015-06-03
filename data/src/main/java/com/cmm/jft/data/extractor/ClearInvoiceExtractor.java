/**
 * 
 */
package com.cmm.jft.data.extractor;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Level;

import com.cmm.logging.Logging;

/**
 * <p><code>ClearInvoiceExtractor.java</code></p>
 * @author Cristiano M Martins
 * @version 25/03/2015 00:54:27
 *
 */
public abstract class ClearInvoiceExtractor implements Extractor {

	protected static String currPtrn = "(R$\\s)?";
	protected static String valuePtrn = "([\\d|\\.]*,\\d{2,4})";//([\\d|\\.]*,\\d{2,4}(\\s[CD]|\\s)?)
	protected static String valuePrefixPtrn = "([R$|\\s+|-])*";
	protected static String valueSufixPtrn = "(\\s[CD]|\\s)*";
	
	protected static String date = "([\\d]{2}/[\\d]{2}/[\\d]{4})";
	protected static String invoice = "^([\\d+|.]+)";
	
	protected String fileName;
	
	
	/* (non-Javadoc)
	 * @see com.cmm.jft.data.extractor.Extractor#config(java.util.Properties)
	 */
	@Override
	public boolean config(Properties properties) {
		boolean conf = false;
		try{
			if((conf = Extractor.checkKeyWords(properties, new String[]{"filename"})) == false){
				throw new Exception(); 
			}
			
			fileName = properties.getProperty("filename");
			
		}catch(Exception e){
			Logging.getInstance().log(getClass(), "Error configuring extractor", Level.ERROR);
		}
		
		return conf;
	}
    
    
    protected String extractValue(String text, String pattern, int group){
    	String ret = "";
    	
    	Pattern p = Pattern.compile(pattern, Pattern.MULTILINE);
    	Matcher m = p.matcher(text);
    	int gp=1;
    	if(group > 0){
    		while(m.find()){
	    		String ext = m.group();
	    		if(gp++ == group){
	    			ret = ext;
	    			break;
	    		}
	    	}
    		
    	}
    	else{
    		if(m.find()){
    			ret = m.group();
    		}
    	}
    	
    	return ret;
    }

	
	
}
