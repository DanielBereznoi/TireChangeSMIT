package com.dabere.tirechange.app.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.dabere.tirechange.app.services.TireChangeService;

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
    public String getAllFreeTimes() {
        tireChangeService.getAllAvailableTimes();
        return "No Times Available";
    }
    

}
