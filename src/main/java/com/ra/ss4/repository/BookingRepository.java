package com.ra.ss4.repository;

import com.ra.ss4.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    List<Booking> findByCustomerPhone(String customerPhone);
}