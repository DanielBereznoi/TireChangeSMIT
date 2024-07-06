package com.dabere.tirechange.app.models;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class WorkshopModel {
    private String name;
    private String address;
    private List<String> vehicleTypes;
}
