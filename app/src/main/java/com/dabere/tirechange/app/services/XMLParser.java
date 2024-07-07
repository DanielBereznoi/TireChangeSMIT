package com.dabere.tirechange.app.services;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.dabere.tirechange.app.entities.Workshop;

@Service
public class XMLParser {
public List<HashMap<String, String>> parseXMLResponse(String response, Workshop workshop) throws ParserConfigurationException, IOException, org.xml.sax.SAXException {

        List<HashMap<String, String>> rawAppointmentData = new ArrayList<>();

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(response)));

        NodeList idNodes = document.getElementsByTagName(workshop.getIdKey());
        NodeList timeNodes = document.getElementsByTagName(workshop.getTimeKey());
        NodeList availableNodes = workshop.getAvailableKey() != null ?
                document.getElementsByTagName(workshop.getAvailableKey()) : null;

        int responseLength = document.getElementsByTagName(workshop.getIdKey()).getLength();

        for (int i = 0; i < responseLength; i++) {
            HashMap<String, String> appointment = new HashMap<>();
            appointment.put(workshop.getIdKey(), idNodes.item(i).getTextContent());
            appointment.put(workshop.getTimeKey(), timeNodes.item(i).getTextContent());

            if (availableNodes != null) {
                appointment.put(workshop.getAvailableKey(), availableNodes.item(i).getTextContent());
            }

            rawAppointmentData.add(appointment);
        }
        return rawAppointmentData;
    }
}
