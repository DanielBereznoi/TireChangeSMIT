package com.dabere.tirechange.app.services;

import org.springframework.stereotype.Service;

import com.dabere.tirechange.app.models.SearchFilterData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;

@Service
public class SearchFilterDataParser {
    public static SearchFilterData main(String jsonDataEncoded) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
        String jsonData = URLDecoder.decode(jsonDataEncoded, StandardCharsets.UTF_8.toString());
        System.out.println(jsonData);
        ObjectMapper mapper = new ObjectMapper();
        SearchFilterData data = mapper.readValue(jsonData, SearchFilterData.class);
        return data;
    }
}
