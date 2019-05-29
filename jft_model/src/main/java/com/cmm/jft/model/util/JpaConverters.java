/*
 * Copyright 2015-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cmm.jft.model.util;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.data.convert.ThreeTenBackPortConverters.DateToInstantConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.DateToLocalDateConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.DateToLocalDateTimeConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.DateToLocalTimeConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.InstantToDateConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.LocalDateTimeToDateConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.LocalDateToDateConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.LocalTimeToDateConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.StringToZoneIdConverter;
import org.springframework.data.convert.ThreeTenBackPortConverters.ZoneIdToStringConverter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

/**
 * JPA 2.1 converters to turn ThreeTen back port types into legacy {@link Date}s. To activate these converters make sure
 * your persistence provider detects them by including this class in the list of mapped classes. In Spring environments,
 * you can simply register the package of this class (i.e. {@code org.springframework.data.jpa.convert.threetenbp}) as
 * package to be scanned on e.g. the {@link LocalContainerEntityManagerFactoryBean}.
 *
 * @author Oliver Gierke
 * @see <a href="https://www.threeten.org/threetenbp">https://www.threeten.org/threetenbp</a>
 * @since 1.8
 */
public class JpaConverters {

	@Converter(autoApply = true)
	public static class LocalDateConverter implements AttributeConverter<LocalDate, Date> {

		@Override
		public Date convertToDatabaseColumn(LocalDate localDate) {
			
			Date date = null;
			if(localDate != null) {
				date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			}
			
			return date;
		}

		@Override
		public LocalDate convertToEntityAttribute(Date date) {
			
			LocalDate localDate = null;			
			if(date != null) {
				localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			}
			
			return localDate;
		}
	}
	/*
	@Converter(autoApply = true)
	public static class LocalTimeConverter implements AttributeConverter<LocalTime, Date> {

		@Override
		public Date convertToDatabaseColumn(LocalTime time) {
			
		}

		@Override
		public LocalTime convertToEntityAttribute(Date date) {
			
		}
	}*/

	@Converter(autoApply = true)
	public static class LocalDateTimeConverter implements AttributeConverter<LocalDateTime, Date> {

		@Override
		public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
			
			Date date = null;
			if(localDateTime != null) {
				date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
			}
			
			return date;
		}

		@Override
		public LocalDateTime convertToEntityAttribute(Date date) {
			
			LocalDateTime localDateTime = null;
			if(date != null) {
				localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
			}
			
			return localDateTime; 
		}
	}

	/*
	@Converter(autoApply = true)
	public static class InstantConverter implements AttributeConverter<Instant, Date> {

		@Override
		public Date convertToDatabaseColumn(Instant instant) {
			
		}

		@Override
		public Instant convertToEntityAttribute(Date date) {
			
		}
	}*/
	/*
	@Converter(autoApply = true)
	public static class ZoneIdConverter implements AttributeConverter<ZoneId, String> {

		@Override
		public String convertToDatabaseColumn(ZoneId zoneId) {
			
		}

		@Override
		public ZoneId convertToEntityAttribute(String zoneId) {
			
		}
	}*/
}
