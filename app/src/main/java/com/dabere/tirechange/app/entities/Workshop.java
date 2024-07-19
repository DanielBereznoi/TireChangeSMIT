package com.dabere.tirechange.app.entities;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Workshop {
    private String name;
    private String address;
    private List<String> vehicleTypes;
    private String baseUrl;
    private String getUrl;
    private String reservationHttpRequestUrl;
    private String reservationHttpRequestType;
    private String reservationHttpRequestBodyFormat;
    private String reservationHttpRequestBody;
    private String responseFormat;
    private String availableKey;
    private String idKey;
    private String timeKey;
    private String isTestive = "false";
    private List<Appointment> appointments = new ArrayList<>();

    public boolean isTestive() {
        return isTestive.equals("true");
    }
}
