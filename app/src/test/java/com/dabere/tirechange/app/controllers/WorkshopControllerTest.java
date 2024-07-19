package com.dabere.tirechange.app.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.dabere.tirechange.app.models.WorkshopModel;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class WorkshopControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void testGetAllWorkshops() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/workshops"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<WorkshopModel> workshopModels = objectMapper.readValue(contentAsString,
                new TypeReference<List<WorkshopModel>>() {
                });
        assertNotNull(workshopModels);
    }

    @Test
    void testBookAnAppointment() throws Exception {
        MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/workshops/vehicle-types"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        List<String> types = objectMapper.readValue(contentAsString,
                new TypeReference<List<String>>() {
                });
        assertTrue(types.size() >= 2);
    }
}
