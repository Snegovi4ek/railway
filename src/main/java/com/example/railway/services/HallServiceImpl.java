package com.example.railway.services;

import com.example.railway.dto.AddHallDto;
import com.example.railway.dto.ShowHallDto;
import com.example.railway.model.entities.Hall;
import com.example.railway.repository.HallRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final ModelMapper mapper;

    public HallServiceImpl(HallRepository hallRepository, ModelMapper mapper) {
        this.hallRepository = hallRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public void addHall(AddHallDto hallDto) {
        Hall hall = mapper.map(hallDto, Hall.class);
        hall.setAvailableSeats(hall.getTotalSeats());
        hall.setActive(true);
        hallRepository.save(hall);
    }

    @Override
    public List<ShowHallDto> allHalls() {
        return hallRepository.findAll().stream()
                .map(hall -> mapper.map(hall, ShowHallDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowHallDto> findActiveHalls() {
        return hallRepository.findByActiveTrue().stream()
                .map(hall -> mapper.map(hall, ShowHallDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ShowHallDto> findByType(String type) {
        return hallRepository.findByType(type).stream()
                .map(hall -> mapper.map(hall, ShowHallDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public AddHallDto  getHallById(String hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new RuntimeException("Зал не найден"));
        return mapper.map(hall, AddHallDto.class);
    }

    @Override
    @Transactional
    public void updateHall(AddHallDto hallDto) {
        Hall hall = hallRepository.findById(hallDto.getId())
                .orElseThrow(() -> new RuntimeException("Зал не найден"));

        Integer oldTotalSeats = hall.getTotalSeats();
        Integer newTotalSeats = hallDto.getTotalSeats();

        mapper.map(hallDto, hall);

        if (!oldTotalSeats.equals(newTotalSeats)) {
            Integer difference = newTotalSeats - oldTotalSeats;
            hall.setAvailableSeats(hall.getAvailableSeats() + difference);
        }

        hallRepository.save(hall);
    }

    @Override
    @Transactional
    public void removeHall(String hallId) {
        if (!hallRepository.existsById(hallId)) {
            throw new RuntimeException("Попытка удалить несуществующий зал");
        }
        hallRepository.deleteById(hallId);
    }
}