package org.gov4j.commons.json.utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = RawObjectSerializer.class)
@JsonDeserialize(using = RawObjectDeserializer.class)
public class RawObject {

    public final String value;

    public RawObject(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}