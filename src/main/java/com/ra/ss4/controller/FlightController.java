package com.ra.ss4.controller;

import com.ra.ss4.entity.Flight;
import com.ra.ss4.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @GetMapping
    public String listFlights(
            @RequestParam(defaultValue = "") String departure,
            @RequestParam(defaultValue = "") String destination,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {

        Page<Flight> flights = flightService.getAllFlights(departure, destination, page, size);

        model.addAttribute("flights", flights);
        model.addAttribute("departure", departure);
        model.addAttribute("destination", destination);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", flights.getTotalPages());
        model.addAttribute("totalElements", flights.getTotalElements());

        return "flights/list";
    }
}