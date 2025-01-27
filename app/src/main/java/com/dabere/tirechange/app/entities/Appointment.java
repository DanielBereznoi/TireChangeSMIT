package com.dabere.tirechange.app.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Appointment {
    private String workshopName;
    private String workshopAddress;
    private String appointmentDate;
    private String appointmentTime;
    private List<String> vehicleTypes;
    private String appointmentId;

    public LocalDateTime getAppointmnetDateTime() {
        LocalDate localDate = LocalDate.parse(appointmentDate);
        LocalTime localTime = LocalTime.parse(appointmentTime);
        return LocalDateTime.of(localDate, localTime);
    }
}
