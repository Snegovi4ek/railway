package com.example.railway.services;

import com.example.railway.dto.AddTripDto;
import com.example.railway.dto.ShowTripDto;
import com.example.railway.dto.ShowDetailedTripDto;

import java.util.List;

public interface TripService {

    void addTrip(AddTripDto tripDto);

    List<ShowTripDto> allTrips();

    List<ShowTripDto> searchByDepartureCity(String city);

    List<ShowTripDto> searchByArrivalCity(String city);

    List<ShowTripDto> searchByStationName(String stationName);

    ShowDetailedTripDto tripDetails(String tripId);

    void removeTrip(String tripId);

    AddTripDto getTripById(String tripId);

    void updateTrip(AddTripDto tripDto);
}