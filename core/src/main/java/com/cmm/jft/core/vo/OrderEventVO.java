package com.cmm.jft.core.vo;

import java.util.Date;

/**
 * 
 * <p><code>OrderEventVO.java</code></p>
 * @author Cristiano
 * @version 4 de set de 2015 21:24:43
 * 
 */
public class OrderEventVO implements Extractable {
		
	public Date sessionDate;
	public double price;
	public String externalID = "";
	public String securityID = "";
	public double volume;
	public Date orderDate;
	public Date orderTime;
	public String orderStatus = "";
	public String orderEvent = "";
	public double tradedVolume;
	public String orderCondition = "";
	public String broker = "";
	
	
}
