package com.cmm.jft.data.vo;

import java.util.Date;

import com.cmm.jft.data.extractor.Extractable;

public class OrderEventVO implements Extractable{
		
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
