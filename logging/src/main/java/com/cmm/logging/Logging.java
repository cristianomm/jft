package com.cmm.logging;

import javax.swing.JOptionPane;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;



/**
 * <p><code>Logging.java</code></p>
 * @author Cristiano M Martins
 * @version 06/07/2011 10:26:41
 *
 */
public class Logging implements ILog {

	private static ILog log;

	private Logger logger;
	private boolean printStackTrace;

	private Logging() {
		logger = Logger.getRootLogger();
	}

	/* (non-Javadoc)
	 * @see com.cmm.logging.ILog#printStackTrace(boolean)
	 */
	@Override
	public void printStackTrace(boolean print) {
		printStackTrace = print;
	}

	private void printStackTrace(Object object) {
		if(printStackTrace) {
			if(object instanceof String) {
				System.out.println(object);
			}
			else if(object instanceof Throwable) {
				((Throwable)object).printStackTrace();
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.cmm.logging.ILog#log(java.lang.Class, java.lang.String, org.apache.log4j.Level)
	 */
	@Override
	public void log(Class<?> cls, String message, Level level) {
		printStackTrace(message);
		Logger.getLogger(cls).log(level, message);	
	}

	/* (non-Javadoc)
	 * @see com.cmm.logging.ILog#log(java.lang.Class, java.lang.Throwable, org.apache.log4j.Level)
	 */
	@Override
	public void log(Class<?> cls, Throwable throww, Level level) {
		printStackTrace(throww);
		Logger.getLogger(cls).log(level,throww.getMessage(), throww);	
	}

	/* (non-Javadoc)
	 * @see com.cmm.logging.ILog#log(java.lang.Class, java.lang.String, java.lang.Throwable, org.apache.log4j.Level, boolean)
	 */
	@Override
	public void log(Class<?> cls, String message, Throwable throww, Level level, boolean showMessage) {
		printStackTrace(throww);
		Logger.getLogger(cls).log(level, message, throww);

		if(showMessage) {
			int type = JOptionPane.INFORMATION_MESSAGE;
			switch(level.toInt()) {
			case Level.TRACE_INT:
				type = JOptionPane.PLAIN_MESSAGE;
				break;
			case Level.DEBUG_INT:
				type = JOptionPane.PLAIN_MESSAGE;
				break;
			case Level.ERROR_INT:
				type = JOptionPane.ERROR_MESSAGE;
				break;
			case Level.FATAL_INT:
				type = JOptionPane.ERROR_MESSAGE;
				break;
			case Level.INFO_INT:
				type = JOptionPane.INFORMATION_MESSAGE;
				break;
			case Level.OFF_INT:
				type = JOptionPane.PLAIN_MESSAGE;
				break;
			case Level.WARN_INT:
				type = JOptionPane.WARNING_MESSAGE;
				break;
			default:
				type = JOptionPane.PLAIN_MESSAGE;
			}

			JOptionPane.showMessageDialog(null, message, null, type);
		}

	}

	public static synchronized ILog getInstance() {

		if(log == null) {
			log = new Logging();
		}

		return log;

	}

}
