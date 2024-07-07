package com.dabere.tirechange.app.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WorkshopTest {
    private static Workshop workshop;

    @BeforeAll
    static void createWorkshop() {
        workshop = new Workshop();
    }

    @Test
    void testGetAddress() {
        assertNull(workshop.getAddress());
        workshop.setAddress("address");
        assertEquals("address", workshop.getAddress());
    }

    @Test
    void testGetAppointments() {
        assertEquals(0, workshop.getAppointments().size());
        workshop.setAppointments(null);
        assertNull(workshop.getAppointments());
    }

    @Test
    void testGetAvailableKey() {
        assertNull(workshop.getAvailableKey());
        workshop.setAvailableKey("key");
        assertEquals("key", workshop.getAvailableKey());
    }

    @Test
    void testGetBaseUrl() {
        assertNull(workshop.getBaseUrl());
        workshop.setBaseUrl("url");
        assertEquals("url", workshop.getBaseUrl());
    }

    @Test
    void testGetGetUrl() {
        assertNull(workshop.getGetUrl());
        workshop.setGetUrl("GET url");
        assertEquals("GET url", workshop.getGetUrl());
    }

    @Test
    void testGetIdKey() {
        assertNull(workshop.getIdKey());
        workshop.setIdKey("1");
        assertEquals("1", workshop.getIdKey());
    }

    @Test
    void testGetName() {
        assertNull(workshop.getName());
        workshop.setName("London");
        assertEquals("London", workshop.getName());
    }

    @Test
    void testGetReservationHttpRequestType() {
        assertNull(workshop.getReservationHttpRequestType());
        workshop.setReservationHttpRequestType("POST");
        assertEquals("POST", workshop.getReservationHttpRequestType());
    }

    @Test
    void testGetReservationHttpRequestUrl() {
        assertNull(workshop.getReservationHttpRequestUrl());
        workshop.setReservationHttpRequestUrl("/tire-change-times/{id}/booking");
        assertEquals("/tire-change-times/{id}/booking", workshop.getReservationHttpRequestUrl());
    }

    @Test
    void testGetResponseFormat() {
        assertNull(workshop.getResponseFormat());
        workshop.setResponseFormat("JSON");
        assertEquals("JSON", workshop.getResponseFormat());
    }

    @Test
    void testGetTimeKey() {
        assertNull(workshop.getTimeKey());
        workshop.setTimeKey("time");
        assertEquals("time", workshop.getTimeKey());
    }

    @Test
    void testGetVehicleTypes() {
        assertNull(workshop.getVehicleTypes());
        List<String> vehicelTypes = new ArrayList<String>();
        vehicelTypes.add("S6iduauto");
        workshop.setVehicleTypes(vehicelTypes);
        assertEquals("time", workshop.getTimeKey());
    }
}
