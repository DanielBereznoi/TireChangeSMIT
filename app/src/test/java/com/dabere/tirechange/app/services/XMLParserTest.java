package com.dabere.tirechange.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.dabere.tirechange.app.entities.Workshop;

public class XMLParserTest {

    private XMLParser xmlParser;
    private String xmlResponse1;
    private String xmlResponse2;
    private Workshop workshop;

    @BeforeEach
    public void setUp() throws IOException {
        xmlParser = new XMLParser();

        xmlResponse1 = "<tireChangeTimesResponse>\n" +
                "  <availableTime>\n" +
                "    <uuid>096e50e7-db4e-4503-bdef-96714a1243b2</uuid>\n" +
                "    <time>2024-06-28T12:00:00Z</time>\n" +
                "  </availableTime>\n" +
                "  <availableTime>\n" +
                "    <uuid>907dc24b-e7b3-46b4-8ea2-414df5d93bac</uuid>\n" +
                "    <time>2024-06-28T13:00:00Z</time>\n" +
                "  </availableTime>\n" +
                "  <availableTime>\n" +
                "    <uuid>8fd4524f-c00f-417c-aac1-a390071de1fc</uuid>\n" +
                "    <time>2024-07-01T06:00:00Z</time>\n" +
                "  </availableTime>\n" +
                "</tireChangeTimesResponse>";

        xmlResponse2 = "<tireChangeTimesResponse>\n" +
                "  <availableTime>\n" +
                "    <uuid>096e50e7-db4e-4503-bdef-96714a1243b2</uuid>\n" +
                "    <time>2024-06-28T12:00:00Z</time>\n" +
                "    <available>true</available>\n" +
                "  </availableTime>\n" +
                "  <availableTime>\n" +
                "    <uuid>907dc24b-e7b3-46b4-8ea2-414df5d93bac</uuid>\n" +
                "    <time>2024-06-28T13:00:00Z</time>\n" +
                "    <available>false</available>\n" +
                "  </availableTime>\n" +
                "  <availableTime>\n" +
                "    <uuid>8fd4524f-c00f-417c-aac1-a390071de1fc</uuid>\n" +
                "    <time>2024-07-01T06:00:00Z</time>\n" +
                "    <available>true</available>\n" +
                "  </availableTime>\n" +
                "</tireChangeTimesResponse>";

        workshop = WorkshopsReader.main(null).get(1);
    }

    @Test
    public void testParseXMLResponse() throws ParserConfigurationException, IOException, org.xml.sax.SAXException {
        List<HashMap<String, String>> parsedData = xmlParser.parseXMLResponse(xmlResponse1, workshop);

        assertNotNull(parsedData);
        assertEquals(3, parsedData.size());

        HashMap<String, String> appointment1 = parsedData.get(0);
        assertEquals("096e50e7-db4e-4503-bdef-96714a1243b2", appointment1.get("uuid"));
        assertEquals("2024-06-28T12:00:00Z", appointment1.get("time"));

        HashMap<String, String> appointment2 = parsedData.get(1);
        assertEquals("907dc24b-e7b3-46b4-8ea2-414df5d93bac", appointment2.get("uuid"));
        assertEquals("2024-06-28T13:00:00Z", appointment2.get("time"));

        HashMap<String, String> appointment3 = parsedData.get(2);
        assertEquals("8fd4524f-c00f-417c-aac1-a390071de1fc", appointment3.get("uuid"));
        assertEquals("2024-07-01T06:00:00Z", appointment3.get("time"));


        workshop.setAvailableKey("available");
        parsedData = xmlParser.parseXMLResponse(xmlResponse2, workshop);
        assertNotNull(parsedData);
        assertEquals(3, parsedData.size());

        appointment1 = parsedData.get(0);
        assertEquals("096e50e7-db4e-4503-bdef-96714a1243b2", appointment1.get("uuid"));
        assertEquals("2024-06-28T12:00:00Z", appointment1.get("time"));
        assertEquals("true", appointment1.get("available"));

        appointment2 = parsedData.get(1);
        assertEquals("907dc24b-e7b3-46b4-8ea2-414df5d93bac", appointment2.get("uuid"));
        assertEquals("2024-06-28T13:00:00Z", appointment2.get("time"));
        assertEquals("false", appointment2.get("available"));

        appointment3 = parsedData.get(2);
        assertEquals("8fd4524f-c00f-417c-aac1-a390071de1fc", appointment3.get("uuid"));
        assertEquals("2024-07-01T06:00:00Z", appointment3.get("time"));
        assertEquals("true", appointment3.get("available"));
    }
}
