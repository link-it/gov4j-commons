package org.gov4j.commons.api.converter.jackson;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJsonProvider extends com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider {

	private static final String DEFAULT_DATA_PATTERN_JSON = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

	public static ObjectMapper getObjectMapper() {
		return getObjectMapper(DEFAULT_DATA_PATTERN_JSON);
	}
	
	public static ObjectMapper getObjectMapper(String pattern) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new DateTimeModule(pattern));
		mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
			    WRITE_DATES_AS_TIMESTAMPS , false);
		return mapper;
	}
	
	public JacksonJsonProvider() {
		super(getObjectMapper());
	}
	
}
