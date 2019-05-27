package org.gov4j.commons.api.converter.jackson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

public class LocalDateDeserializer extends StdScalarDeserializer<LocalDate> {

	private static final long serialVersionUID = 1L;
	private String pattern;

	public LocalDateDeserializer(String pattern) {
        super(LocalDate.class);
        this.pattern = pattern;
    }


	private static DateFormat newSimpleDateFormat(String pattern) {
		DateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return sdf;
	}

    @Override
    public LocalDate deserialize(JsonParser jsonParser,
                                DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        try {
            JsonToken currentToken = jsonParser.getCurrentToken();
            if (currentToken == JsonToken.VALUE_STRING) {
                String dateTimeAsString = jsonParser.getText().trim();
                return new LocalDate(LocalDateDeserializer.newSimpleDateFormat(this.pattern).parse(dateTimeAsString));
            } else {
            	return null;
            }
        } catch (ParseException e) {
        	throw new IOException(e);
        }
    }
}
