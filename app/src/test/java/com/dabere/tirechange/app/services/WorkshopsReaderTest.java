package com.dabere.tirechange.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dabere.tirechange.app.entities.Workshop;

@SpringBootTest
public class WorkshopsReaderTest {

    @Autowired
    private WorkshopsReader workshopsReader;

    private List<Workshop> workshops;
    
    @Test
    void creation() {
        WorkshopsReader reader = new WorkshopsReader();
        assertTrue(reader!=null);
    }

    @Test
    void testGetWorkshopList() {
        try {
            workshops = workshopsReader.getWorkshopList();
        } catch (IOException e) {
            e.printStackTrace();
            fail("There was an error while reading the yaml file.");
        }
        assertFalse(workshops.isEmpty());
        Workshop workshop = workshops.get(0);
        assertEquals("Test JSON", workshop.getName());
        assertEquals("test address json", workshop.getAddress());
        assertEquals("http://localhost:0000/api/test", workshop.getBaseUrl());
        assertEquals("Sõiduauto", workshop.getVehicleTypes().get(0));
        assertEquals("Veoauto", workshop.getVehicleTypes().get(1));
        assertEquals("/test-url", workshop.getGetUrl());
        assertEquals("/test-url/{id}/booking", workshop.getReservationHttpRequestUrl());
        assertEquals("POST", workshop.getReservationHttpRequestType());
        assertEquals("JSON", workshop.getResponseFormat());
        assertEquals("available", workshop.getAvailableKey());
        assertEquals("id", workshop.getIdKey());
        assertEquals("time", workshop.getTimeKey());
        assertTrue(workshop.getAppointments() != null);
    }
}
