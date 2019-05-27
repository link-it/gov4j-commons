package org.gov4j.commons.api.converter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonProvider extends com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider {

	private static final String DEFAULT_DATE_TIME_PATTERN_JSON = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";
	private static final String DEFAULT_LOCAL_DATE_PATTERN_JSON = "yyyy-MM-dd";

	public static ObjectMapper getObjectMapper() {
		return getObjectMapper(DEFAULT_DATE_TIME_PATTERN_JSON, DEFAULT_LOCAL_DATE_PATTERN_JSON);
	}
	
	public static ObjectMapper getObjectMapper(String dateTimePattern,String localDatePattern) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new DateTimeModule(dateTimePattern));
		mapper.registerModule(new LocalDateModule(localDatePattern));
		mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
			    WRITE_DATES_AS_TIMESTAMPS , false);
		return mapper;
	}
	
	public JacksonJsonProvider() {
		super(getObjectMapper());
	}
	
}
