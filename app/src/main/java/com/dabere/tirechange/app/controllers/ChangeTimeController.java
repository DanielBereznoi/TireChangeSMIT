package com.dabere.tirechange.app.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dabere.tirechange.app.entities.Appointment;
import com.dabere.tirechange.app.services.TireChangeService;

@RestController
@RequestMapping("/appointments")
public class ChangeTimeController {
    @Autowired
    TireChangeService tireChangeService;

    @GetMapping
    public List<Appointment> getAllFreeTimes() {
        return tireChangeService.getAllAvailableTimes();
    }

    @GetMapping("/filtered")
    public List<Appointment> getMethodName(@RequestParam(required = true) String searchFilterDataJSON) {
        return tireChangeService.getFilteredAppointmentTimes(searchFilterDataJSON);
    }

}
