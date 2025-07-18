package com.ra.ss4.service;

import com.ra.ss4.entity.Flight;
import com.ra.ss4.repository.FlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightService {
    @Autowired
    FlightRepository flightRepository;

    public Page<Flight> getAllFlights(String departure, String destination, int page, int size) {
        return flightRepository.findFlightsByDepartureAndDestination(departure, destination, PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Flight getFlightById(Long id) {
        return flightRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Flight with given ID does not exist"));
    }
}