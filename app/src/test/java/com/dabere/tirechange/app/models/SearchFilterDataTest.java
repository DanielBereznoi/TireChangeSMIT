package com.dabere.tirechange.app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SearchFilterDataTest {


    private static SearchFilterData data;

    @BeforeAll
    static void setUp() {
        data = new SearchFilterData();
    }

    @Test
    void testGetFrom() {
        assertNull(data.getFrom());
        data.setFrom("from");
        assertEquals("from", data.getFrom());
    }

    @Test
    void testGetUntil() {
        assertNull(data.getUntil());
        data.setUntil("until");
        assertEquals("until", data.getUntil());
    }

    @Test
    void testGetVehicleTypes() {
        assertNull(data.getVehicleTypes());
        data.setVehicleTypes(new ArrayList<>());
        assertTrue(data.getVehicleTypes().isEmpty());
    }

    @Test
    void testGetWorkshopAddresses() {
        assertNull(data.getWorkshopAddresses());
        data.setWorkshopAddresses(new ArrayList<>());
        assertTrue(data.getWorkshopAddresses().isEmpty());
    }
}
