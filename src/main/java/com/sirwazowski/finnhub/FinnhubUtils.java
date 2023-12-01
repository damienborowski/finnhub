package com.sirwazowski.finnhub;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

public class FinnhubUtils {

    public static final ObjectMapper mapper = new ObjectMapper();

    public static Map<String, Object> parseJSON(String responseBody){
        try{
            return mapper.readValue(responseBody, Map.class);
        } catch (Exception e){
            throw new FinnhubException("Unable to parse responseBody", e);
        }
    }
}
