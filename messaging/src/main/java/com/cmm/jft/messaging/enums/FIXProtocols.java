package com.cmm.jft.messaging.enums;

public enum FIXProtocols {
	
	FIX40("FIX.4.0"),
	FIX41("FIX.4.1"),
	FIX42("FIX.4.2"),
	FIX43("FIX.4.3"),
	FIX44("FIX.4.4"),
	FIX50("FIX.5.0"),
	FIX50SP1("FIX.5.0SP1"),
	FIX50SP2("FIX.5.0SP2");	
	
	String value;
	FIXProtocols(String value){
		this.value = value;
	}
	
	public String getValue() {
		return value;
	}
	
}
