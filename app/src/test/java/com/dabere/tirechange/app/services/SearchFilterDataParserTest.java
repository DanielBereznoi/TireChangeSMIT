package com.dabere.tirechange.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.UnsupportedEncodingException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dabere.tirechange.app.models.SearchFilterData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@SpringBootTest
public class SearchFilterDataParserTest {
    @Test
    void testMain() throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
        String encoded = "%7B%22from%22%3A%222024-08-01%22%2C%22until%22%3A%222024-08-30%22%2C%22workshopAddresses%22%3A%5B%5D%2C%22vehicleTypes%22%3A%5B%5D%7D";
        SearchFilterData data = SearchFilterDataParser.main(encoded);
        assertEquals("2024-08-01", data.getFrom());
        assertEquals("2024-08-30", data.getUntil());
        assertTrue(data.getVehicleTypes().isEmpty());
        assertTrue(data.getWorkshopAddresses().isEmpty());
        
    }
}
