package com.dabere.tirechange.app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dabere.tirechange.app.entities.Appointment;
import com.dabere.tirechange.app.entities.Workshop;
import com.dabere.tirechange.app.exceptions.CorruptedSearchFilterDataException;
import com.dabere.tirechange.app.exceptions.UnsupportedHttpResponseFormat;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

@SpringBootTest
public class TireChangeServiceTest {
    
    @Autowired
    private TireChangeService service;

    @Autowired
    private JSONParser jsonParser;

    @Autowired
    private WorkshopsReader workshopsReader;

    @Autowired
    private XMLParser xmlParser;

    private static Workshop workshop;

    @BeforeEach
    void setUp() throws StreamReadException, DatabindException, IOException {
        workshop = workshopsReader.getWorkshopList().get(2);
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

    @Test
    void getFilteredAppointmentTimes() {
        String encodedWithVehicleTypes = "%7B%22from%22%3A%222024-08-01%22%2C%22until%22%3A%222024-08-30%22%2C%22workshopAddresses%22%3A%5B%5D%2C%22vehicleTypes%22%3A%5B%22Veoauto%22%5D%7D";
        String encodedWithAddress = "%7B%22from%22%3A%222024-08-01%22%2C%22until%22%3A%222024-08-30%22%2C%22workshopAddresses%22%3A%5B%221A%20Gunton%20Rd%2C%20London%22%5D%2C%22vehicleTypes%22%3A%5B%5D%7D";
        HashMap<String, List<Appointment>> result = service.getFilteredAppointmentTimes(encodedWithVehicleTypes);
        List<Appointment> appointments = result.get(result.keySet().iterator().next());
        assertNotNull(appointments);
        Appointment appointment = appointments.get(0);
        assertTrue(appointment.getVehicleTypes().contains("Veoauto"));

        result = service.getFilteredAppointmentTimes(encodedWithAddress);
        List<Appointment> appointments1 = result.get(result.keySet().iterator().next());
        assertNotNull(appointments1);
        Appointment appointment1 = appointments1.get(0);
        assertEquals("1A Gunton Rd, London", appointment1.getWorkshopAddress());

        CorruptedSearchFilterDataException error = assertThrows(CorruptedSearchFilterDataException.class, ()  -> service.getFilteredAppointmentTimes("Must fail"));
        assertEquals("The received search filter data has been corrupted or has wrong structure.", error.getLocalizedMessage());
    }

    
}
