package com.dwei.common.jackson.deserializer;

import com.dwei.common.utils.ObjectUtils;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.io.IOException;
import java.time.Instant;

@Slf4j
public class String2InstantDeserializer extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String text = p.getText();
        return parse(text);
    }

    @Nullable
    private static Instant parse(String text) {
        if (ObjectUtils.isNull(text)) return null;
        try {
            long l = Long.parseLong(text);
            return Instant.ofEpochMilli(l);
        } catch (NumberFormatException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
