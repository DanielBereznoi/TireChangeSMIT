package com.dabere.tirechange.app.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class Appointment {
    private String workshopName;
    private String workshopAddress;
    private String appointmentDate;
    private String appointmentTime;
    private String appointmentId;
}
