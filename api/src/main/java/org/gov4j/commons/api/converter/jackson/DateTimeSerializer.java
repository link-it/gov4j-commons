package org.gov4j.commons.api.converter.jackson;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

public class DateTimeSerializer extends StdScalarSerializer<DateTime> {

	private static final long serialVersionUID = 1L;
	private String pattern;
	
	public DateTimeSerializer(String pattern) {
        super(DateTime.class);
        this.pattern = pattern;
    }


	private static DateFormat newSimpleDateFormat(String pattern) {
		DateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);
		return sdf;
	}

    @Override
    public void serialize(DateTime dateTime,
                          JsonGenerator jsonGenerator,
                          SerializerProvider provider) throws IOException, JsonGenerationException {
        String dateTimeAsString = DateTimeSerializer.newSimpleDateFormat(this.pattern).format(dateTime.toDate());
        jsonGenerator.writeString(dateTimeAsString);
    }
}
