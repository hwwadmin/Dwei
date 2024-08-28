package com.dwei.common.jackson.deserializer;

import com.dwei.common.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class Boolean2IntDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if ("true".equalsIgnoreCase(text)) return 1;
        if ("false".equalsIgnoreCase(text)) return 0;
        if (ObjectUtils.isNull(text)) return null;
        return Integer.valueOf(text);
    }

}
