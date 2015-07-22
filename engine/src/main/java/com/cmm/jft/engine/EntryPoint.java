/**
 * 
 */
package com.cmm.jft.engine;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DoNotSend;
import quickfix.FieldConvertError;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.MessageCracker;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.UnsupportedMessageType;
import quickfix.field.OrdType;

/**
 * <p><code>EntryPoint.java</code></p>
 * @author Cristiano
 * @version 17/06/2015 17:00:55
 *
 */
public class EntryPoint extends MessageCracker implements Application {

	
	private HashSet<String> validOrderTypes;
	private static final String VALID_ORDER_TYPES_KEY = "ValidOrderTypes";
	
	
	
	/**
	 * http://www.codeproject.com/Articles/757708/Mock-FIX-Trading-Server
	 * @param settings 
	 * @throws FieldConvertError 
	 * @throws ConfigError 
	 * 
	 */
	public EntryPoint(SessionSettings settings) throws ConfigError, FieldConvertError {
		this.validOrderTypes = new HashSet<String>();
		initializeValidOrderTypes(settings);
	}
	
	
	private void initializeValidOrderTypes(SessionSettings settings) throws ConfigError, FieldConvertError {
        if (settings.isSetting(VALID_ORDER_TYPES_KEY)) {
            List<String> orderTypes = Arrays
                    .asList(settings.getString(VALID_ORDER_TYPES_KEY).trim().split("\\s*,\\s*"));
            validOrderTypes.addAll(orderTypes);
        } else {
            validOrderTypes.add(OrdType.LIMIT + "");
        }
    }
	
	
	/* (non-Javadoc)
	 * @see quickfix.Application#onCreate(quickfix.SessionID)
	 */
	public void onCreate(SessionID sessionId) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogon(quickfix.SessionID)
	 */
	public void onLogon(SessionID sessionId) {
		SessionRepository.getInstance().addSession(sessionId);
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#onLogout(quickfix.SessionID)
	 */
	public void onLogout(SessionID sessionId) {
		SessionRepository.getInstance().removeSession(sessionId);
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toAdmin(quickfix.Message, quickfix.SessionID)
	 */
	public void toAdmin(Message message, SessionID sessionId) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromAdmin(quickfix.Message, quickfix.SessionID)
	 */
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			RejectLogon {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#toApp(quickfix.Message, quickfix.SessionID)
	 */
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see quickfix.Application#fromApp(quickfix.Message, quickfix.SessionID)
	 */
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue,
			UnsupportedMessageType {
		
		crack(message, sessionId);
		
	}
	
	
	
	
/*
	public void onMessage(NewOrderSingle order, SessionID sessionID) throws FieldNotFound,
	UnsupportedMessageType, IncorrectTagValue {
		try {
			validateOrder(order);

			OrderQty orderQty = order.getOrderQty();
			Price price = getPrice(order);

			quickfix.fix44.ExecutionReport accept = new quickfix.fix44.ExecutionReport(
					genOrderID(), genExecID(), new ExecType(ExecType.FILL), new OrdStatus(
							OrdStatus.NEW), order.getSide(), new LeavesQty(order.getOrderQty()
									.getValue()), new CumQty(0), new AvgPx(0));

			accept.set(order.getClOrdID());
			accept.set(order.getSymbol());
			sendMessage(sessionID, accept);

			if (isOrderExecutable(order, price)) {
				quickfix.fix44.ExecutionReport executionReport = new quickfix.fix44.ExecutionReport(genOrderID(),
						genExecID(), new ExecType(ExecType.FILL), new OrdStatus(OrdStatus.FILLED), order.getSide(),
						new LeavesQty(0), new CumQty(orderQty.getValue()), new AvgPx(price.getValue()));

				executionReport.set(order.getClOrdID());
				executionReport.set(order.getSymbol());
				executionReport.set(orderQty);
				executionReport.set(new LastQty(orderQty.getValue()));
				executionReport.set(new LastPx(price.getValue()));

				sendMessage(sessionID, executionReport);
			}
		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		}
	}

	public void onMessage(quickfix.fix50.NewOrderSingle order, SessionID sessionID)
			throws FieldNotFound, UnsupportedMessageType, IncorrectTagValue {
		try {
			validateOrder(order);

			OrderQty orderQty = order.getOrderQty();
			Price price = getPrice(order);

			quickfix.fix50.ExecutionReport accept = new quickfix.fix50.ExecutionReport(
					genOrderID(), genExecID(), new ExecType(ExecType.FILL), new OrdStatus(
							OrdStatus.NEW), order.getSide(), new LeavesQty(order.getOrderQty()
									.getValue()), new CumQty(0));

			accept.set(order.getClOrdID());
			accept.set(order.getSymbol());
			sendMessage(sessionID, accept);

			if (isOrderExecutable(order, price)) {
				quickfix.fix50.ExecutionReport executionReport = new quickfix.fix50.ExecutionReport(
						genOrderID(), genExecID(), new ExecType(ExecType.FILL), new OrdStatus(
								OrdStatus.FILLED), order.getSide(), new LeavesQty(0), new CumQty(
										orderQty.getValue()));

				executionReport.set(order.getClOrdID());
				executionReport.set(order.getSymbol());
				executionReport.set(orderQty);
				executionReport.set(new LastQty(orderQty.getValue()));
				executionReport.set(new LastPx(price.getValue()));
				executionReport.set(new AvgPx(price.getValue()));

				sendMessage(sessionID, executionReport);
			}
		} catch (RuntimeException e) {
			LogUtil.logThrowable(sessionID, e.getMessage(), e);
		}
	}
*/


}
