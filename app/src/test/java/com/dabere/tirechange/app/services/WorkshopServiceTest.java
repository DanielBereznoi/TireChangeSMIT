package com.dabere.tirechange.app.services;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WorkshopServiceTest {

    @Autowired
    WorkshopService workshopService;

    @Test
    void testGetAllAvailableVehicleTypes() {
        assertTrue(workshopService.getAllAvailableVehicleTypes() != null);
    }

    @Test
    void testGetWorkshopList() {
        HashSet<String> vehicleTypes = workshopService.getAllAvailableVehicleTypes();
        assertFalse(vehicleTypes.isEmpty());
    }
}
