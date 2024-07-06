package com.dabere.tirechange.app.entities;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppointmentTest {
    private static Appointment appointment;
    
    @BeforeAll
    static void createAppointment() {
        appointment = new Appointment("London", "London SW1A 1AA, UK", "2024-05-08", "13:00", "1");
    }

    @Test
    void testGetAppointmentDate() {
        assertEquals("2024-05-08", appointment.getAppointmentDate());
    }

    @Test
    void testGetAppointmentId() {
        assertEquals("1", appointment.getAppointmentId());
    }

    @Test
    void testGetAppointmentTime() {
        assertEquals("13:00", appointment.getAppointmentTime());
    }

    @Test
    void testGetWorkshopAddress() {
        assertEquals("London SW1A 1AA, UK", appointment.getWorkshopAddress());
    }

    @Test
    void testGetWorkshopName() {
        assertEquals("London", appointment.getWorkshopName());
    }
}
