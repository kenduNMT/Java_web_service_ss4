package com.ra.ss4.controller;

import com.ra.ss4.entity.Booking;
import com.ra.ss4.entity.BookingStatus;
import com.ra.ss4.entity.Flight;
import com.ra.ss4.service.BookingService;
import com.ra.ss4.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private FlightService flightService;

    @GetMapping("/form")
    public String showBookingForm(@RequestParam Long flightId, Model model) {
        Flight flight = flightService.getFlightById(flightId);
        model.addAttribute("flight", flight);
        model.addAttribute("booking", new Booking());
        return "bookings/form";
    }

    @PostMapping
    public String createBooking(@ModelAttribute Booking booking, @RequestParam Long flightId) {
        Flight flight = flightService.getFlightById(flightId);
        booking.setFlight(flight);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.BOOKED);
        bookingService.addBooking(booking);
        return "redirect:/bookings/customer?phone=" + booking.getCustomerPhone();
    }

    @GetMapping("/customer")
    public String listCustomerBookings(@RequestParam String phone, Model model) {
        List<Booking> bookings = bookingService.getBookingsByCustomerPhone(phone);
        model.addAttribute("bookings", bookings);
        model.addAttribute("customerPhone", phone);
        return "bookings/customer-list";
    }

    @GetMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, @RequestParam String phone) {
        bookingService.changeStatusCancelled(id);
        return "redirect:/bookings/customer?phone=" + phone;
    }
}