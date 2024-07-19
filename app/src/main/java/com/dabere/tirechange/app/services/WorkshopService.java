package com.dabere.tirechange.app.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dabere.tirechange.app.entities.Workshop;
import com.dabere.tirechange.app.models.WorkshopModel;

@Service
public class WorkshopService {

    @Autowired
    private WorkshopsReader workshopsReader;

    public List<WorkshopModel> getWorkshopList() {
        try {
            List<Workshop> nonTestiveWorkshops = workshopsReader.getWorkshopList().stream()
                    .filter(workshop -> !workshop.isTestive()).collect(Collectors.toList());
            return nonTestiveWorkshops.stream()
                    .map(workshop -> new WorkshopModel(workshop.getName(), workshop.getAddress()))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public HashSet<String> getAllAvailableVehicleTypes() {
        try {
            List<Workshop> workshops = workshopsReader.getWorkshopList();
            HashSet<String> vehicleTypes = new HashSet<>();
            workshops.subList(2, workshops.size() - 1)
                    .forEach(workshop -> vehicleTypes.addAll(workshop.getVehicleTypes()));
            return vehicleTypes;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new HashSet<>();
    }

}
