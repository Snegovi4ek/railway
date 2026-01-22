package com.example.railway.services;

import com.example.railway.dto.AddBookingDto;
import com.example.railway.dto.ShowBookingDto;
import java.util.List;

public interface BookingService {

    void addBooking(AddBookingDto bookingDto, String username);

    List<ShowBookingDto> allBookings();

    List<ShowBookingDto> findByUser(String username);

    ShowBookingDto getBookingById(String bookingId);

    void cancelBooking(String bookingId, String username);
}