/**
 * 
 */
package com.cmm.jft.core.format;

/**
 * <p>
 * <code>FormatterFactory.java</code>
 * </p>
 * 
 * @author Cristiano Martins
 * @version 02/08/2013 16:06:24
 *
 */
public class FormatterFactory {

	public static Formatter<?> getFormatter(FormatterTypes type) {

		switch (type) {
		case BIGDECIMAL:
			return new BigDecimalFormatter();
		case BOOLEAN:
			return new BooleanFormatter();
		case DOUBLE:
			return new DoubleFormatter();
		case INT:
			return new IntFormatter();
		case LONG:
			return new LongFormatter();

		case DATE_F1:
			return new DateTimeFormatter(FormatterTypes.DATE_F1.getFormat());
		case DATE_F2:
			return new DateTimeFormatter(FormatterTypes.DATE_F2.getFormat());
		case DATE_F3:
			return new DateTimeFormatter(FormatterTypes.DATE_F3.getFormat());
		case DATE_F4:
			return new DateTimeFormatter(FormatterTypes.DATE_F4.getFormat());
		case DATE_F5:
			return new DateTimeFormatter(FormatterTypes.DATE_F5.getFormat());
		case DATE_F6:
			return new DateTimeFormatter(FormatterTypes.DATE_F6.getFormat());
		case DATE_F7:
			return new DateTimeFormatter(FormatterTypes.DATE_F7.getFormat());
		case DATE_F8:
			return new DateTimeFormatter(FormatterTypes.DATE_F8.getFormat());
		case DATE_F9:
			return new DateTimeFormatter(FormatterTypes.DATE_F9.getFormat());
		case DATE_F10:
			return new DateTimeFormatter(FormatterTypes.DATE_F10.getFormat());

		case TIME_F1:
			return new DateTimeFormatter(FormatterTypes.TIME_F1.getFormat());
		case TIME_F2:
			return new DateTimeFormatter(FormatterTypes.TIME_F2.getFormat());
		case TIME_F3:
			return new DateTimeFormatter(FormatterTypes.TIME_F3.getFormat());

		case DATE_TIME_F1:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F1.getFormat());
		case DATE_TIME_F2:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F2.getFormat());
		case DATE_TIME_F3:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F3.getFormat());
		case DATE_TIME_F4:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F4.getFormat());
		case DATE_TIME_F5:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F5.getFormat());
		case DATE_TIME_F6:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F6.getFormat());
		case DATE_TIME_F7:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F7.getFormat());
		case DATE_TIME_F8:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F8.getFormat());
		case DATE_TIME_F9:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F9.getFormat());
		case DATE_TIME_F10:
			return new DateTimeFormatter(
					FormatterTypes.DATE_TIME_F10.getFormat());
		default:
			return null;
		}

	}

}
