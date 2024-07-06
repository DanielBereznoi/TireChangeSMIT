package com.dabere.tirechange.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.dabere.tirechange.app.entities.Workshop;
import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
public class JSONParserTest {

    private JSONParser jsonParser;
    private Workshop workshop;

    @BeforeEach
    public void setUp() throws IOException {
        jsonParser = new JSONParser();
        workshop = WorkshopsReader.main(null).get(0);
    }

    @Test
    public void testParseJsonResponse() throws JsonProcessingException {
        String jsonResponse = """
                [
                  {
                    "id": 1,
                    "time": "2024-06-28T05:00:00Z",
                    "available": true
                  },
                  {
                    "id": 2,
                    "time": "2024-06-28T06:00:00Z",
                    "available": true
                  },
                  {
                    "id": 3,
                    "time": "2024-06-28T07:00:00Z",
                    "available": true
                  },
                  {
                    "id": 4,
                    "time": "2024-06-28T08:00:00Z",
                    "available": true
                  },
                  {
                    "id": 5,
                    "time": "2024-06-28T09:00:00Z",
                    "available": false
                  },
                  {
                    "id": 6,
                    "time": "2024-06-28T10:00:00Z",
                    "available": true
                  }
                ]""";

        List<HashMap<String, String>> result = jsonParser.parseJsonResponse(jsonResponse, workshop);

        assertNotNull(result);
        assertEquals(6, result.size());
        
        HashMap<String, String> firstAppointment = result.get(0);
        assertEquals("1", firstAppointment.get("id"));
        assertEquals("2024-06-28T05:00:00Z", firstAppointment.get("time"));
        assertEquals("true", firstAppointment.get("available"));
    }
}
