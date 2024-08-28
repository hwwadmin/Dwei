package com.dwei.common.jackson.deserializer;

import com.dwei.common.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import javax.annotation.Nullable;
import java.io.IOException;
import java.sql.Time;

public class String2TimeDeserializer extends JsonDeserializer<Time> {

    @Override
    public Time deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        return parse(text);
    }

    @Nullable
    private static Time parse(String text) {
        if (ObjectUtils.isNull(text)) return null;
        try {
            int firstColon = text.indexOf(':');
            int secondColon = text.indexOf(':', firstColon + 1);
            if (firstColon > 0 && secondColon < 0) {
                text = text + ":00";
            }
            return Time.valueOf(text);
        } catch (Exception e) {
            return null;
        }
    }

}
