package com.ra.ss4.service;

import com.ra.ss4.entity.Booking;
import com.ra.ss4.entity.BookingStatus;
import com.ra.ss4.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking addBooking(Booking booking) {
        return bookingRepository.save(booking);
    }

    public Booking changeStatusCancelled(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Booking with given ID does not exist"));
        booking.setStatus(BookingStatus.CANCELLED);
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByCustomerPhone(String phone) {
        return bookingRepository.findByCustomerPhone(phone);
    }
}