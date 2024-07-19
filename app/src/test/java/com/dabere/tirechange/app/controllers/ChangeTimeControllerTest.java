package com.dabere.tirechange.app.controllers;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.dabere.tirechange.app.entities.Appointment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class ChangeTimeControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllFreeTimes() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/appointments")).andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<Appointment> appointments = objectMapper.readValue(contentAsString,
                new TypeReference<List<Appointment>>() {
                });
        System.out.println(appointments.toString());
        assertTrue(appointments.size() > 1);
    }

    @Test
    void testGetFilteredAppointments() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get(
                "/appointments/filtered?searchFilterDataJSON=%7B%22from%22%3A%222024-08-01%22%2C%22until%22%3A%222024-08-30%22%2C%22workshopAddresses%22%3A%5B%5D%2C%22vehicleTypes%22%3A%5B%22Veoauto%22%5D%7D"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        HashMap<String, List<Appointment>> appointments = objectMapper.readValue(contentAsString,
                new TypeReference<HashMap<String, List<Appointment>>>() {
                });
        assertTrue(appointments.size() > 1);
    }
}
