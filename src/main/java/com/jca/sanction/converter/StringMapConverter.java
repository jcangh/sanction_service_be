package com.jca.sanction.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter
public class StringMapConverter implements AttributeConverter<Map<String, String>, String> {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
    }

    @Override
    public String convertToDatabaseColumn(Map<String, String> data) {
        if (null == data) {
            return "{}";
        }

        try {
            return mapper.writeValueAsString(data);

        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting map to JSON", e);
        }
    }

    @Override
    public Map<String, String> convertToEntityAttribute(String s) {
        if (null == s) {
            return new HashMap<>();
        }

        try {
            return mapper.readValue(s, new TypeReference<>() {});
        } catch (IOException e) {
            throw new IllegalArgumentException("Error converting JSON to map", e);
        }
    }
}
