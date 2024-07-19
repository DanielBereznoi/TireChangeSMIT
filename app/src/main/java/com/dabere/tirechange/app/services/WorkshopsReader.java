package com.dabere.tirechange.app.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.springframework.stereotype.Service;

import com.dabere.tirechange.app.entities.Workshop;
import com.dabere.tirechange.app.entities.WorkshopListConfig;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

@Service
public class WorkshopsReader {

    public List<Workshop> getWorkshopList() throws StreamReadException, DatabindException, IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        InputStream inputStream = WorkshopsReader.class.getClassLoader().getResourceAsStream("workshops.yaml");

        WorkshopListConfig config = mapper.readValue(inputStream, WorkshopListConfig.class);

        // Access workshops from the config object
        List<Workshop> workshops = config.getWorkshops();

        return workshops;
    }
}
