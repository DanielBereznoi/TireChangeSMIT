package com.dabere.tirechange.app.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WorkshopListConfigTest {
    private static WorkshopListConfig config;

    

    @BeforeAll
    static void beforeClass() {
        config = new WorkshopListConfig();
    }



    @Test
    void testGetWorkshops() {
        assertNull(config.getWorkshops());
        List<Workshop> workshops = new ArrayList<>();
        config.setWorkshops(workshops);
        assertEquals(0, config.getWorkshops().size());
    }
}
