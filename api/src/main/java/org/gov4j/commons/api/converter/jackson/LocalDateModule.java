package org.gov4j.commons.api.converter.jackson;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class LocalDateModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

	public LocalDateModule(String pattern) {
        super();
        addSerializer(LocalDate.class, new LocalDateSerializer(pattern));
        addDeserializer(LocalDate.class, new LocalDateDeserializer(pattern));
    }
}

