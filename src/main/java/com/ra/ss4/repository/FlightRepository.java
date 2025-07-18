package com.ra.ss4.repository;

import com.ra.ss4.entity.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface FlightRepository extends JpaRepository<Flight,Long> {
    @Query("SELECT f FROM Flight f WHERE " +
            "(:departure IS NULL OR f.departure LIKE %:departure%) AND " +
            "(:destination IS NULL OR f.destination LIKE %:destination%)")
    Page<Flight> findFlightsByDepartureAndDestination(String departure, String destination, Pageable pageable);
}