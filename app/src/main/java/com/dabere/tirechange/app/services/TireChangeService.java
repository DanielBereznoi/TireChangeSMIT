package com.dabere.tirechange.app.services;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import com.dabere.tirechange.app.entities.Appointment;
import com.dabere.tirechange.app.entities.Workshop;
import com.dabere.tirechange.app.exceptions.CorruptedSearchFilterDataException;
import com.dabere.tirechange.app.exceptions.UnsupportedHttpRequestOperator;
import com.dabere.tirechange.app.exceptions.UnsupportedHttpResponseFormat;
import com.dabere.tirechange.app.models.BookingMessageResponse;
import com.dabere.tirechange.app.models.SearchFilterData;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class TireChangeService {

    @Autowired
    private WebService webService;

    @Autowired
    private WorkshopsReader workshopsReader;

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
                appointments.addAll(findTimes(workshop, currentDate, END_DATETIME));
            }

        });
        return appointments;
    }

    public List<Appointment> findTimes(Workshop workshop, String from, String until) {
        List<HashMap<String, String>> rawAppointmentData = new ArrayList<>();
        LocalDate startDate = java.time.LocalDate.parse(from);
        LocalDate endDate = java.time.LocalDate.parse(until);
        String response = webService.sendGetRequestForAppointments(workshop, until, from);

        try {
            rawAppointmentData = parseResponse(response, workshop);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (rawAppointmentData == null) {
            return new ArrayList<>();
        }

        return rawAppointmentData.stream()
                .filter(raw -> isAppointmentAvailable(raw, workshop.getAvailableKey()))
                .map(raw -> mapIfWithinDateRange(raw, workshop, startDate, endDate))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    List<HashMap<String, String>> parseResponse(String response, Workshop workshop)
            throws ParserConfigurationException, IOException, SAXException {
        if ("JSON".equals(workshop.getResponseFormat())) {
            return jsonParser.parseJsonResponse(response, workshop);
        } else if ("XML".equals(workshop.getResponseFormat())) {
            return xmlParser.parseXMLResponse(response, workshop);
        } else {
            throw new UnsupportedHttpResponseFormat(workshop.getResponseFormat()
                    + " is not supported Http response format. Make sure that you chose either JSON or XML format.");
        }
    }

    private Appointment mapIfWithinDateRange(HashMap<String, String> raw, Workshop workshop, LocalDate startDate,
            LocalDate endDate) {
        ZonedDateTime zonedAppointmentTime = ZonedDateTime.parse(raw.get(workshop.getTimeKey()),
                DateTimeFormatter.ISO_ZONED_DATE_TIME);
        LocalDate appointmentDate = zonedAppointmentTime.toLocalDate();
        if ((appointmentDate.isBefore(endDate) || appointmentDate.isEqual(endDate))
                && (appointmentDate.isAfter(startDate) || appointmentDate.isEqual(startDate))) {
            return mapIntoAppointment(raw, workshop);
        }
        return null;
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
            workshops = workshopsReader.getWorkshopList();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception appropriately
        }
    }

    public HashMap<String, List<Appointment>> getFilteredAppointmentTimes(String searchFilterDataUrlString) {
        SearchFilterData searchFilterData;
        try {
            searchFilterData = SearchFilterDataParser.main(searchFilterDataUrlString);
            String from = searchFilterData.getFrom();
            String until = searchFilterData.getUntil();
            List<String> workshopAddresses = searchFilterData.getWorkshopAddresses();
            List<String> vehicleTypes = searchFilterData.getVehicleTypes();
            loadWorkshops();
            String startDate = (from != null) ? from : java.time.LocalDate.now().toString();
            String endTime = (until != null) ? until : END_DATETIME;

            List<Appointment> appointments = new ArrayList<>();

            workshops.forEach(workshop -> {
                if (!workshop.isTestive()
                        && (workshopAddresses == null || workshopAddresses.isEmpty()
                                || workshopAddresses.contains(workshop.getAddress()))
                        && (vehicleTypes == null || vehicleTypes.isEmpty()
                                || workshop.getVehicleTypes().containsAll(vehicleTypes))) {
                    appointments.addAll(findTimes(workshop, startDate, endTime));
                }
            });

            // Retun appointment list sorted by date
            List<Appointment> sortedAppointments = appointments.stream()
                    .sorted((a1, a2) -> a1.getAppointmnetDateTime().compareTo(a2.getAppointmnetDateTime()))
                    .collect(Collectors.toList());
            HashMap<String, List<Appointment>> appointmentsByDate = new LinkedHashMap<>();
            sortedAppointments.forEach((appointment) -> {
                String appointmentDate = appointment.getAppointmentDate();
                if (appointmentsByDate.containsKey(appointmentDate)) {
                    appointmentsByDate.computeIfAbsent(appointmentDate, k -> new ArrayList<>()).add(appointment);
                } else {
                    appointmentsByDate.put(appointmentDate, new ArrayList<>(Arrays.asList(appointment)));
                }
            });
            return appointmentsByDate;

        } catch (JsonProcessingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        throw new CorruptedSearchFilterDataException(
                "The received search filter data has been corrupted or has wrong structure.");
    }

    public BookingMessageResponse bookFreeTireChangeTime(String id, String address) {
        loadWorkshops();
        Optional<Workshop> workshop = workshops.stream().filter(w -> w.getAddress().equals(address)).findFirst();

        if (workshop.isPresent()) {
            try {
                return webService.bookFreeTireChangeTime(id, address, workshop.get());
            } catch (UnsupportedHttpRequestOperator e) {
                e.printStackTrace();
                return new BookingMessageResponse("Rakenduses ilmnes sisemine viga. Palun proovige uuesti hiljem või valige teine töökoda.");
            }
        } else {
            return new BookingMessageResponse("Tekkis viga. Töökoda pole leitud. Palun proovige uuesti hiljem või valige teine töökoda.");
        }
    }
}
