package com.dwei.common.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class Int2BooleanDeserializer extends JsonDeserializer<Boolean> {

    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        if (Boolean.parseBoolean(text)) return Boolean.TRUE;
        try {
            long l = Long.parseLong(text);
            return l != 0;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

}
