package org.gov4j.commons.api.converter.jackson;

import org.joda.time.DateTime;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class DateTimeModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

	public DateTimeModule(String pattern) {
        super();
        addSerializer(DateTime.class, new DateTimeSerializer(pattern));
        addDeserializer(DateTime.class, new DateTimeDeserializer(pattern));
    }
}

