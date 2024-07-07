package com.dabere.tirechange.app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WorkshopModelTest {
    private static WorkshopModel model;

    @BeforeAll
    static void beforeClass() {
        List<String> vehicleTypes = new ArrayList<>();
        model = new WorkshopModel("London", "London City 1, UK", vehicleTypes);
    }

    @Test
    void testGetAddress() {
        assertEquals("London City 1, UK", model.getAddress());
    }

    @Test
    void testGetName() {
        assertEquals("London", model.getName());
    }

    @Test
    void testGetVehicleTypes() {
        assertEquals(0, model.getVehicleTypes().size());
    }

}
