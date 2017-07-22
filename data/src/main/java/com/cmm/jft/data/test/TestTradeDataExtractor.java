/**
 * 
 */
package com.cmm.jft.data.test;

import java.util.List;
import java.util.Properties;

import com.cmm.jft.core.util.TimeCounter;
import com.cmm.jft.data.extractor.marketdata.BovespaHistoricalFileExtractor;
import com.cmm.jft.data.extractor.marketdata.BovespaTradeFileExtractor;

/**
 * <p>
 * <code>TestTradeDataExtractor.java</code>
 * </p>
 *
 * @author cristiano
 * @version 29/03/2017 11:55:40
 *
 */
public class TestTradeDataExtractor {

    /**
     * @param args
     */
    public static void main(String[] args) {
	Properties p = new Properties();
	p.put("filename", "D:\\Disco\\Users\\Cristiano\\Downloads\\BMF Files\\Copy_NEG_BMF_20170315.TXT");

	BovespaTradeFileExtractor be = new BovespaTradeFileExtractor();
	be.config(p);

	TimeCounter tc = new TimeCounter();
	tc.start();
	List l = be.extract();
	tc.stop();

	System.out.println(l.size() + " Lines in: " + tc.getElapsedInSeconds());

    }

}
