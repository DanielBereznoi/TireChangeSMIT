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
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/free-times")).andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<Appointment> appointments = objectMapper.readValue(contentAsString, new TypeReference<List<Appointment>>() {});
        System.out.println(appointments.toString());
        assertTrue(appointments.size()>1);
    }

    @Test
    void testGreet() throws Exception {
        this.mockMvc.perform(MockMvcRequestBuilders.get("/"))
        .andExpect(status().isOk()).andReturn();
    }
}
