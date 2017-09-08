package com.cmm.jft.messaging;

import java.util.Date;

import com.cmm.jft.messaging.fix44.Fix44EngineMessageEncoder;
import com.cmm.jft.messaging.fix50sp2.Fix50SP2MDMessageEncoder;
import com.cmm.jft.trading.OrderEvent;
import com.cmm.jft.trading.Orders;
import com.cmm.jft.trading.enums.OrderTypes;
import com.cmm.jft.trading.enums.RejectTypes;

import quickfix.Message;
import quickfix.SessionID;
import quickfix.field.MsgType;

public interface MessageEncoder {


    static MessageEncoder getEncoder(SessionID sessionId) {

	MessageEncoder encoder = null;
	switch(sessionId.getBeginString()) {
	case "FIX.4.4":
	    encoder = Fix44EngineMessageEncoder.getInstance();
	    break;

	case "FIX.5.0SP2":
	    encoder = Fix50SP2MDMessageEncoder.getInstance();

	}

	return encoder;
    }


    //[start]-------------------------------------------Session Specific
    Message heartbeat();

    Message sequenceReset();

    //[end]




}