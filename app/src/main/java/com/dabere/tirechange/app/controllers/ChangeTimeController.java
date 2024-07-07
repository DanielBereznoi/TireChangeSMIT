package com.dabere.tirechange.app.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.dabere.tirechange.app.entities.Appointment;
import com.dabere.tirechange.app.services.TireChangeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class ChangeTimeController {
    @Autowired
    TireChangeService tireChangeService;

    @GetMapping("")
    public String greet() {
        System.out.println("Tere");
        tireChangeService.getAllAvailableTimes();
        return "Hola";
    }

    @GetMapping("free-times")
    public List<Appointment> getAllFreeTimes() {
        return tireChangeService.getAllAvailableTimes();
    }
    

}
