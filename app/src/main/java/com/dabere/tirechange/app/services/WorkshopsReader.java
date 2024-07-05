package com.dabere.tirechange.app.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.dabere.tirechange.app.entities.WorkshopConfig;
import com.dabere.tirechange.app.entities.WorkshopListConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class WorkshopsReader {
    public static List<WorkshopConfig> main(String[] args) throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = WorkshopsReader.class.getClassLoader().getResourceAsStream("workshops.yaml");

        WorkshopListConfig config = mapper.readValue(inputStream, WorkshopListConfig.class);

        // Access workshops from the config object
        List<WorkshopConfig> workshops = config.getWorkshops();

        return workshops;
    }
}
