package org.gov4j.commons.api.converter.jackson;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;

public class JacksonXmlProvider extends com.fasterxml.jackson.jaxrs.xml.JacksonXMLProvider {

	public static XmlMapper getObjectMapper() {
		XmlMapper mapper = new XmlMapper();
		mapper.registerModule(new JodaModule());
		mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.
			    WRITE_DATES_AS_TIMESTAMPS , false);
		return mapper;
	}
	
	public JacksonXmlProvider() {
		super(getObjectMapper());
	}
	
}
