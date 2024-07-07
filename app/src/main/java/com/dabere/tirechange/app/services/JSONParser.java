package com.dabere.tirechange.app.services;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dabere.tirechange.app.entities.Workshop;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class JSONParser {
public List<HashMap<String, String>> parseJsonResponse(String response, Workshop workshop)
            throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        TypeReference<List<HashMap<String, String>>> typeRef = new TypeReference<List<HashMap<String, String>>>() {
        };
        List<HashMap<String, String>> rawAppointmentData = mapper.readValue(response, typeRef);

        return rawAppointmentData;
    }
}
