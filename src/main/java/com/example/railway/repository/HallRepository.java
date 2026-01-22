package com.example.railway.repository;

import com.example.railway.model.entities.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HallRepository extends JpaRepository<Hall, String> {

    Optional<Hall> findById(String id);

    List<Hall> findByType(String type);

    List<Hall> findByActiveTrue();
}