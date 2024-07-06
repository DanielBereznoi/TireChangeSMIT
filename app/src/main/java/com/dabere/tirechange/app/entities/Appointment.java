package com.dabere.tirechange.app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Appointment {
    private String workshopName;
    private String workshopAddress;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentId;
}
