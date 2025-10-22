package com.vezh.lab.api.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonSerializer<T> {

    private static ObjectMapper mapper = new ObjectMapper();

    /**
     * Object mapping into JSON
     * @param object - object, that will be mapped
     * @return - JSON-string
     */
    public static String getJson(Object object) {
        String json = null;
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            json = mapper.writeValueAsString(object);
        } catch (IOException e) {
            log.error(e.toString());
        }
        return json;
    }

    /**
     * Returns formatted json-string
     * Too make logs more good looking
     * If something goes wrong, returns sting without changes
     * @param value - json-string
     * @return - formatted json-string
     */
    public static String formatJson(String value) {
        try {
            mapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
            Object json = mapper.readValue(value, Object.class);
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
        } catch (IOException e) {
            log.debug("Unable to format json");
            log.debug(e.getMessage());
        }
        return value;
    }

    /**
     * Mapping json string to java class
     * @param json - json string
     * @param clazz - mapping class
     * @return - mapped object
     */
    public T parseJson(String json, Class clazz) {
        try {
            return (T) mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.error("Unable to parse json \"" + json + "\"");
        }
        return null;
    }
}
