package com.dabere.tirechange.app.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;

import com.dabere.tirechange.app.entities.Appointment;
import com.dabere.tirechange.app.entities.Workshop;
import com.dabere.tirechange.app.exceptions.UnsupportedHttpResponseFormat;

@SpringBootTest
public class TireChangeServiceTest {
    
    @Autowired
    private TireChangeService service;

    @Autowired
    private JSONParser jsonParser;

    @Autowired
    private XMLParser xmlParser;

    private static Workshop workshop;

    @BeforeAll
    static void beforeAll() throws IOException {
        workshop = WorkshopsReader.main(null).get(2);
    }

    @BeforeEach
    void setUp() {
        assertNotNull(service);
        assertNotNull(jsonParser);
        assertNotNull(xmlParser);
    }

    @Test
    void testFindTimes() {
        LocalDate localDate = LocalDate.now();
        String endTime = "2040-01-01";
        List<Appointment> appointments = service.findTimes(workshop, localDate.toString(), endTime);
        assertNotNull(appointments);

        Appointment appointment = appointments.get(0);
        assertNotNull(appointment.getAppointmentId());
        assertNotNull(appointment.getWorkshopName());
        assertNotNull(appointment.getWorkshopAddress());
        assertNotNull(appointment.getAppointmentTime());
        assertNotNull(appointment.getAppointmentDate());
        assertNotNull(appointment.getVehicleTypes());

        workshop.setResponseFormat("MUSTFAIL");
        UnsupportedHttpResponseFormat exception = assertThrows(
                UnsupportedHttpResponseFormat.class,
                () -> service.findTimes(workshop, localDate.toString(), endTime));
        assertEquals(
                "MUSTFAIL is not supported Http response format. Make sure that you chose either JSON or XML format.",
                exception.getMessage());
    }

    @Test
    void testGetAllAvailableTimes() {
        List<Appointment> appointments = service.getAllAvailableTimes();
        Appointment appointment = appointments.get(0);
        assertNotNull(appointment.getAppointmentId());
        assertNotNull(appointment.getWorkshopName());
        assertNotNull(appointment.getWorkshopAddress());
        assertNotNull(appointment.getAppointmentTime());
        assertNotNull(appointment.getAppointmentDate());
        assertNotNull(appointment.getVehicleTypes());
        
    }

    @Test
    void testIsAppointmentAvailable() {
        HashMap<String, String> test1 = new HashMap<>();
        HashMap<String, String> test2 = new HashMap<>();
        HashMap<String, String> test3 = new HashMap<>();
        test1.put("available", "true");
        test2.put("available", "false");
        assertTrue(service.isAppointmentAvailable(test1, "available"));
        assertFalse(service.isAppointmentAvailable(test2, "available"));
        assertTrue(service.isAppointmentAvailable(test3, null));
    }

    @Test
    void testMapIntoAppointment() {
        Workshop workshop = new Workshop();
        workshop.setName("Test JSON");
        workshop.setAddress("test address");
        workshop.setBaseUrl("http://localhost:0000/api/test");
        workshop.setVehicleTypes(List.of("Sõiduauto", "Veoauto"));
        workshop.setGetUrl("/test-url");
        workshop.setReservationHttpRequestUrl("/test-url/{id}/booking");
        workshop.setReservationHttpRequestType("POST");
        workshop.setResponseFormat("JSON");
        workshop.setAvailableKey("available");
        workshop.setIdKey("id");
        workshop.setTimeKey("time");

        HashMap<String, String> testData = new HashMap<>();
        testData.put("id", "1");
        testData.put("time", "2024-06-28T07:00:00Z");
        Appointment appointment = service.mapIntoAppointment(testData, workshop);

        assertNotNull(appointment);
        assertEquals("Test JSON", appointment.getWorkshopName());
        assertEquals("test address", appointment.getWorkshopAddress());
        assertEquals("2024-06-28", appointment.getAppointmentDate());
        assertEquals("07:00", appointment.getAppointmentTime());
        assertEquals("1", appointment.getAppointmentId());
        assertTrue(appointment.getVehicleTypes().contains("Sõiduauto"));

    }
}
