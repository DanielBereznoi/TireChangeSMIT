package com.dabere.tirechange.app.models;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SearchFilterData {
    private String from;
    private String until;
    private List<String> workshopAddresses;
    private List<String> vehicleTypes;
}
