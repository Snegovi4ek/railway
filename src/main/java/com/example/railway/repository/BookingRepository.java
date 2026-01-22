package com.example.railway.repository;

import com.example.railway.model.entities.Booking;
import com.example.railway.model.entities.Trip;
import com.example.railway.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {

    Optional<Booking> findById(String id);

    List<Booking> findByUser(User user);

    List<Booking> findByTrip(Trip trip);

    List<Booking> findByUserAndStatus(User user, String status);
}