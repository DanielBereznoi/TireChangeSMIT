package com.dabere.tirechange.app.services;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.xml.sax.SAXException;

import com.dabere.tirechange.app.entities.Appointment;
import com.dabere.tirechange.app.entities.Workshop;
import com.dabere.tirechange.app.exceptions.UnsupportedHttpResponseFormat;

@Service
public class TireChangeService {
    @Autowired
    private XMLParser xmlParser;

    @Autowired
    private JSONParser jsonParser;

    private List<Workshop> workshops;

    private static final String END_DATETIME = "2040-01-01";

    public List<Appointment> getAllAvailableTimes() {

            loadWorkshops();

        String currentDate = java.time.LocalDate.now().toString();
        List<Appointment> appointments = new ArrayList<>();

        workshops.forEach(workshop -> {
            if (!workshop.isTestive()) {
                System.out.println(workshop.toString());
                appointments.addAll(findTimes(workshop, currentDate));
            }
            
        });
        return appointments;
    }



    public List<Appointment> findTimes(Workshop workshop, String from) {
        List<Appointment> workshopAppointments = new ArrayList<>();
        List<HashMap<String, String>> rawAppointmentData = new ArrayList<>();

        WebClient client = WebClient.builder()
                .baseUrl(workshop.getBaseUrl())
                .build();

        String response = client.get()
                .uri(UriBuilder -> UriBuilder.path(workshop.getGetUrl()).queryParam("from", from)
                        .queryParam("until", END_DATETIME)
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .block();

        try {
            if ("JSON".equals(workshop.getResponseFormat())) {
                rawAppointmentData = jsonParser.parseJsonResponse(response, workshop);
            } else if ("XML".equals(workshop.getResponseFormat())) {
                rawAppointmentData = xmlParser.parseXMLResponse(response, workshop);
            } else {
                throw new UnsupportedHttpResponseFormat(workshop.getResponseFormat()
                        + " is not supported Http response format. Make sure that you chose either JSON or XML format.");
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
            // TODO: handle exception
        }

        rawAppointmentData.forEach(raw -> {
            if (isAppointmentAvailable(raw, workshop.getAvailableKey())) {
                workshopAppointments.add(mapIntoAppointment(raw, workshop));
            }
        });

        return workshopAppointments;
    }

    public Appointment mapIntoAppointment(HashMap<String, String> raw, Workshop workshop) {
        String id = raw.get(workshop.getIdKey());
        String time = raw.get(workshop.getTimeKey());
        LocalDateTime localDateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();

        return new Appointment(workshop.getName(), workshop.getAddress(), localDate.toString(), localTime.toString(), id);
    }

   public boolean isAppointmentAvailable(HashMap<String, String> raw, String availableKey) {
        return !("false".equals(raw.get(availableKey)));
    }

    private void loadWorkshops() {
        try {
            workshops = WorkshopsReader.main(null);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

}
