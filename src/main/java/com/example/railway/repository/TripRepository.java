package com.example.railway.repository;

import com.example.railway.model.entities.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, String> {

    Optional<Trip> findById(String id);

    List<Trip> findByDepartureCity(String departureCity);

    List<Trip> findByArrivalCity(String arrivalCity);

    List<Trip> findByStationName(String stationName);

    void deleteById(String id);
}