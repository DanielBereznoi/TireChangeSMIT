package com.dabere.tirechange.app.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Workshop {
    private String name;
    private String address;
    private List<String> vehicleTypes;
    private String baseUrl;
    private String getUrl;
    private String reservationHttpRequestUrl;
    private String reservationHttpRequestType;
    private String responseFormat;
    private String availableKey;
    private String idKey;
    private String timeKey;
    private List<Appointment> appointments = new ArrayList<>();
}
