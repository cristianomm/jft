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
	
	public String orderID = "";
	public String eventID = "";
	public String securityID = "";
	public Date sessionDate;
	public double price;
	public double volume;
	public Date orderDate;
	public Date orderTime;
	public String orderStatus = "";
	public String orderEvent = "";
	public double tradedVolume;
	public String orderCondition = "";
	public int broker;
	
	
}
