package com.dabere.tirechange.app.models;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class WorkshopModelTest {
    private static WorkshopModel model;

    @BeforeAll
    static void beforeClass() {
        model = new WorkshopModel("London", "London City 1, UK");
    }

    @Test
    void testGetAddress() {
        assertEquals("London City 1, UK", model.getAddress());
    }

    @Test
    void testGetName() {
        assertEquals("London", model.getName());
    }

}
