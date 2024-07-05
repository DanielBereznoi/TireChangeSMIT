package com.dabere.tirechange.app.entities;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class WorkshopConfig {
    private String name;
    private String address;
    private List<String> vehicleTypes;
    private String baseUrl;

    public WorkshopConfig(String name, String address, List<String> vehicleTypes, String baseUrl) {
        this.name = name;
        this.address = address;
        this.vehicleTypes = vehicleTypes;
        this.baseUrl = baseUrl;
    }



}
