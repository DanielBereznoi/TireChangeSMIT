package com.dabere.tirechange.app.services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dabere.tirechange.app.entities.Appointment;
import com.dabere.tirechange.app.entities.Workshop;
import com.dabere.tirechange.app.exceptions.UnsupportedHttpRequestOperator;
import com.dabere.tirechange.app.models.BookingMessageResponse;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;

@SpringBootTest
public class WebServiceTest {

    @Autowired
    WorkshopsReader workshopsReader;

    @Autowired
    WebService webService;

    @Autowired
    TireChangeService tireChangeService;

    Workshop workshop;
    String from;
    String until;

    @BeforeEach
    void setUp() throws StreamReadException, DatabindException, IOException {
        workshop = workshopsReader.getWorkshopList().get(2);
        from = LocalDate.now().toString();
        until = LocalDate.now().toString();
    }

    @Test
    void testBookFreeTireChangeTime() {
        String response = webService.sendGetRequestForAppointments(workshop, until, from);
        assertTrue(response != null);
    }

    @Test
    void testSendGetRequestForAppointments() {
        Appointment appointment = tireChangeService.findTimes(workshop, from, "2040-12-30").get(0);
        BookingMessageResponse response = webService.bookFreeTireChangeTime(appointment.getAppointmentId(), appointment.getWorkshopAddress(), workshop);
        assertEquals("Broneering õnnestus.", response.getMessage());
        response = webService.bookFreeTireChangeTime(appointment.getAppointmentId(), appointment.getWorkshopAddress(), workshop);
        assertEquals("See aeg on võetud. Palun valige teine.", response.getMessage());
        workshop.setReservationHttpRequestType("MUSTFAIL");
        UnsupportedHttpRequestOperator error = 
        assertThrows(UnsupportedHttpRequestOperator.class, 
        () -> webService.bookFreeTireChangeTime(appointment.getAppointmentId(), appointment.getWorkshopAddress(), workshop));
        assertEquals("Wrong HTTP request operator. Can only use PUT and POST.", error.getMessage());
    }
}
