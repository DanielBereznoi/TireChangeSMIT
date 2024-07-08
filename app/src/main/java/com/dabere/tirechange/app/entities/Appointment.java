package com.dabere.tirechange.app.entities;

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
}
