package com.dabere.tirechange.app.entities;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkshopListConfig {
private List<WorkshopConfig> workshops;
    public List<WorkshopConfig> getWorkshops() {
        return workshops;
    }

    public void setWorkshops(List<WorkshopConfig> workshops) {
        this.workshops = workshops;
    }
}
