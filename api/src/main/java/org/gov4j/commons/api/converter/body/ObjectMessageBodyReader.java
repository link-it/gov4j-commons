package org.gov4j.commons.api.converter.body;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;

@Provider
public class ObjectMessageBodyReader implements javax.ws.rs.ext.MessageBodyReader<Object> {

	@Override
	public boolean isReadable(Class<?> paramClass, Type paramType,
			Annotation[] paramArrayOfAnnotation, MediaType paramMediaType) {
		// paramMediaType is application/zip, application/xml ...
		// realizzare un if se si desidera gestire solo un tipo in particolare
		
		return paramMediaType.equals(MediaType.valueOf("application/zip"));
	}

	@Override
	public Object readFrom(Class<Object> paramClass, Type paramType,
			Annotation[] paramArrayOfAnnotation, MediaType paramMediaType,
			MultivaluedMap<String, String> paramMultivaluedMap,
			InputStream paramInputStream) throws IOException, WebApplicationException {
		return paramInputStream;
	}

}

