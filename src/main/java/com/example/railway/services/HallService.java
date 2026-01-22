package com.example.railway.services;

import com.example.railway.dto.AddHallDto;
import com.example.railway.dto.ShowHallDto;
import java.util.List;

public interface HallService {
    void addHall(AddHallDto hallDto);

    List<ShowHallDto> allHalls();

    List<ShowHallDto> findActiveHalls();

    List<ShowHallDto> findByType(String type);

    AddHallDto getHallById(String hallId);

    void updateHall(AddHallDto hallDto);

    void removeHall(String hallId);
}