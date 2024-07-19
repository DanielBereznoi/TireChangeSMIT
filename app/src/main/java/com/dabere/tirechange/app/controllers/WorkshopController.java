package com.dabere.tirechange.app.controllers;

import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dabere.tirechange.app.models.WorkshopModel;
import com.dabere.tirechange.app.services.WorkshopService;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/workshops")
@CrossOrigin(origins = "http://localhost:4200")
public class WorkshopController {

    @Autowired
    WorkshopService workshopService;
    
    @GetMapping()
    public List<WorkshopModel> getAllWorkshops() {
        return workshopService.getWorkshopList();
    }

    @GetMapping("/vehicle-types")
    public HashSet<String> getAllAvailableVehicleTypes() {
        return workshopService.getAllAvailableVehicleTypes();
    }
    
    
}
