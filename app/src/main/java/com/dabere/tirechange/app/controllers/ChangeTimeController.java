package com.dabere.tirechange.app.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dabere.tirechange.app.entities.Appointment;
import com.dabere.tirechange.app.models.BookingMessageResponse;
import com.dabere.tirechange.app.services.TireChangeService;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/appointments")
public class ChangeTimeController {
    @Autowired
    TireChangeService tireChangeService;

    @GetMapping
    public List<Appointment> getAllFreeTimes() {
        return tireChangeService.getAllAvailableTimes();
    }

    @GetMapping("/filtered")
    public HashMap<String, List<Appointment>> getFilteredAppointments(@RequestParam(required = true) String searchFilterDataJSON) {
        
        return tireChangeService.getFilteredAppointmentTimes(searchFilterDataJSON);
    }


    @PutMapping("/{id}")
    public BookingMessageResponse bookAnAppointment(@PathVariable String id, @RequestParam String workshopAddress) {
        return tireChangeService.bookFreeTireChangeTime(id, workshopAddress);
    }

    
}
