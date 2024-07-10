package com.dabere.tirechange.app.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
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
import com.dabere.tirechange.app.exceptions.CorruptedSearchFilterDataException;
import com.dabere.tirechange.app.exceptions.UnsupportedHttpResponseFormat;
import com.dabere.tirechange.app.models.SearchFilterData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

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
                appointments.addAll(findTimes(workshop, currentDate, END_DATETIME));
            }

        });
        return appointments;
    }

    public List<Appointment> findTimes(Workshop workshop, String from, String until) {
        List<Appointment> workshopAppointments = new ArrayList<>();
        List<HashMap<String, String>> rawAppointmentData = new ArrayList<>();
        LocalDate startDate = java.time.LocalDate.parse(from);
        LocalDate endDate = java.time.LocalDate.parse(until);
        WebClient client = WebClient.builder()
                .baseUrl(workshop.getBaseUrl())
                .build();

        // TODO Make "from" and "until" parameter names be configurable from config block. Check if they are even needed.
        String response = client.get()
                .uri(UriBuilder -> UriBuilder.path(workshop.getGetUrl()).queryParam("from", from)
                        .queryParam("until", until)
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
        
        }

        rawAppointmentData.forEach(raw -> {
            if (isAppointmentAvailable(raw, workshop.getAvailableKey())) {
                ZonedDateTime zonedAppointmentTime = ZonedDateTime.parse(raw.get(workshop.getTimeKey()), DateTimeFormatter.ISO_ZONED_DATE_TIME);
                LocalDate appointmentDate = zonedAppointmentTime.toLocalDate();
                if ((appointmentDate.isBefore(endDate) || appointmentDate.isEqual(endDate))
                        && (appointmentDate.isAfter(startDate) || appointmentDate.isEqual(startDate))) {
                    workshopAppointments.add(mapIntoAppointment(raw, workshop));
                }

            }
        });

        return workshopAppointments;
    }

    public Appointment mapIntoAppointment(HashMap<String, String> raw, Workshop workshop) {
        String id = raw.get(workshop.getIdKey());
        String time = raw.get(workshop.getTimeKey());
        LocalDateTime localDateTime = LocalDateTime.parse(time,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
        LocalDate localDate = localDateTime.toLocalDate();
        LocalTime localTime = localDateTime.toLocalTime();

        return new Appointment(workshop.getName(), workshop.getAddress(), localDate.toString(), localTime.toString(),
                workshop.getVehicleTypes(),
                id);
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

    public List<Appointment> getFilteredAppointmentTimes(String searchFilterDataUrlString) {
        SearchFilterData searchFilterData;
        try {
            searchFilterData = SearchFilterDataParser.main(searchFilterDataUrlString);
            String from = searchFilterData.getFrom();
            String until = searchFilterData.getUntil();
            List<String> workshopAddresses = searchFilterData.getWorkshopAddresses();
            List<String> vehicleTypes = searchFilterData.getVehicleTypes();
            loadWorkshops();
            System.out.println(vehicleTypes.toString());
            String startDate = (from != null) ? from : java.time.LocalDate.now().toString();
            String endTime = (until != null) ? until : END_DATETIME;
    
            List<Appointment> appointments = new ArrayList<>();
    
            System.out.println(endTime);
            workshops.forEach(workshop -> {
                if (!workshop.isTestive()
                        && (workshopAddresses == null || workshopAddresses.isEmpty() || workshopAddresses.contains(workshop.getAddress()))
                        && (vehicleTypes == null || vehicleTypes.isEmpty() || workshop.getVehicleTypes().containsAll(vehicleTypes))) {
                    appointments.addAll(findTimes(workshop, startDate, endTime));
                }
            });
            return appointments;
        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            System.out.println(e);
        } 
        throw new CorruptedSearchFilterDataException("The received search filter data has been corrupted or has wrong structure.");
    }

}
